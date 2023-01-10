package mc.analyzers.survivaladdons2.shop;

import jdk.tools.jlink.plugin.Plugin;
import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.commands.Shop;
import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

import static mc.analyzers.survivaladdons2.shop.ShopItems.*;
import static mc.analyzers.survivaladdons2.utility.utility.getAbsoluteId;

public class ShopItem {
    private final ItemStack actualItem;
    private final int priceChangeRequiredVolume;
    private final String type;
    private final boolean canSell;
    private final int basePrice;
    private int offset;
    private String category;

    public static ShopItem getByMaterial(ItemStack type){
        String id;
        if(pdc.has(type, "id")){
            id = pdc.get(type, "id");
        }else {
            id = type.getType().name();
        }
        try{
            return (ShopItem) ShopItems.class.getField(id.toLowerCase(Locale.ROOT)).get(ShopItems.class);
        }catch (Exception e){
            return null;
        }
    }

    public ShopItem(ItemStack item, int priceChangeRequiredVolume, int basePrice, boolean canSell, String category){
        this.category = category;
        this.canSell = canSell;
        this.actualItem = item;
        this.priceChangeRequiredVolume = priceChangeRequiredVolume;
        this.basePrice = basePrice;
        this.offset = 0;
        SurvivalAddons2 plugin = SurvivalAddons2.getPlugin();
        String type = getAbsoluteId(item).toUpperCase();
        this.type = type;
        if(plugin.getConfig().get("shop." + type + ".bought") == null){
            plugin.getConfig().set("shop." + type + ".bought", 0);
        }
        if(plugin.getConfig().get("shop." + type + ".sold") == null){
            plugin.getConfig().set("shop." + type + ".sold", 0);
        }
        if(plugin.getConfig().get("shop." + type + ".transactions") == null){
            plugin.getConfig().set("shop." + type + ".transactions", 0);
        }
        if(plugin.getConfig().get("shop." + type + ".bias") == null){
            plugin.getConfig().set("shop." + type + ".bias", 0);
        }
        if(plugin.getConfig().get("shop." + type + ".offset") == null){
            plugin.getConfig().set("shop." + type + ".offset", 0);
        }
        if(plugin.getConfig().get("shop." + type + ".price") == null){
            plugin.getConfig().set("shop." + type + ".price", basePrice);
        }
        plugin.getConfig().set("shop." + type + ".baseprice", basePrice);
        SurvivalAddons2.getPlugin().saveConfig();
    }

    public boolean isCanSell(){
        return canSell;
    }

    public double getBiasedPrice(){
        SurvivalAddons2 plugin = SurvivalAddons2.getPlugin();
        return Math.max(1, Math.round(plugin.getConfig().getInt("shop." + type + ".price")));
    }

    public int buyItem(int amount){
        int dustPrice = 0;
        SurvivalAddons2 plugin = SurvivalAddons2.getPlugin();
        int basePrice = plugin.getConfig().getInt("shop." + type + ".baseprice");
        int offset = plugin.getConfig().getInt("shop." + type + ".offset");
        int oldBias = plugin.getConfig().getInt("shop." + type + ".bias");
        plugin.getConfig().set("shop." + type + ".bought", plugin.getConfig().getInt("shop." + type + ".bought") + amount);
        plugin.getConfig().set("shop." + type + ".transactions", plugin.getConfig().getInt("shop." + type + ".transactions") + 1);
        int newPrice = plugin.getConfig().getInt("shop." + type + ".price");
        for(int i = 0; i<amount; i++){
            offset += 1;
            if(offset >= priceChangeRequiredVolume){
                oldBias+=1;
                offset = 0;
                newPrice = basePrice + oldBias; //+ newPrice*0.1*oldBias;
            }
            dustPrice += Math.round(newPrice);
        }
        plugin.getConfig().set("shop." + type + ".price", newPrice);
        plugin.getConfig().set("shop." + type + ".bias", oldBias);
        plugin.getConfig().set("shop." + type + ".offset", offset);
        plugin.saveConfig();
        return Math.max(1, dustPrice);
    }

    public String getCategory() {
        return category;
    }

    public int sellItem(int amount){
        int dustPrice = 0;
        SurvivalAddons2 plugin = SurvivalAddons2.getPlugin();
        int basePrice = plugin.getConfig().getInt("shop." + type + ".baseprice");
        int offset = plugin.getConfig().getInt("shop." + type + ".offset");
        int oldBias = plugin.getConfig().getInt("shop." + type + ".bias");
        plugin.getConfig().set("shop." + type + ".sold", plugin.getConfig().getInt("shop." + type + ".sold") + amount);
        plugin.getConfig().set("shop." + type + ".transactions", plugin.getConfig().getInt("shop." + type + ".transactions") + 1);
        int newPrice = plugin.getConfig().getInt("shop." + type + ".price");
        for(int i = 0; i<amount; i++){
            dustPrice += Math.round(newPrice);
            offset -= 1;
            if(offset < 0){
                oldBias-=1;
                offset = priceChangeRequiredVolume-1;
                newPrice = (basePrice + oldBias);//newPrice*0.1*oldBias);
            }
        }
        plugin.getConfig().set("shop." + type + ".price", newPrice);
        plugin.getConfig().set("shop." + type + ".bias", oldBias);
        plugin.getConfig().set("shop." + type + ".offset", offset);
        plugin.saveConfig();
        return Math.max(0, dustPrice);
    }

    public ItemStack getActualItem() {
        return actualItem;
    }
}
