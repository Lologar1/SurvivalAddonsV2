package mc.analyzers.survivaladdons2.shop;

import jdk.tools.jlink.plugin.Plugin;
import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.commands.Shop;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static mc.analyzers.survivaladdons2.shop.ShopItems.*;

public class ShopItem {
    private final ItemStack actualItem;
    private final int priceChangeRequiredVolume;
    private final Material type;
    private final boolean canSell;
    private final int basePrice;
    private int offset;

    public static ShopItem getByMaterial(Material type){
        switch (type){
            case NETHERITE_SCRAP:
                return netherite_scrap;
            case EMERALD:
                return emerald;
            case GOLD_INGOT:
                return gold_ingot;
            case COBBLESTONE:
                return cobblestone;
            case DIRT:
                return dirt;
            case LAPIS_LAZULI:
                return lapis_lazuli;
            case DIAMOND:
                return diamond;
            case IRON_INGOT:
                return iron_ingot;
            case BEDROCK:
                return bedrock;
            case COAL:
                return coal;
            case STRING:
                return string;
            case ROTTEN_FLESH:
                return flesh;
            case BONE:
                return bone;
            case BLAZE_ROD:
                return blazerod;
            case SPIDER_EYE:
                return spidereye;
            case GUNPOWDER:
                return gunpowder;
            case GHAST_TEAR:
                return ghast_tear;
            case MAGMA_CREAM:
                return magma_cream;
            case ENDER_PEARL:
                return ender_pearl;
            case QUARTZ:
                return quartz;
        }
        return null;
    }

    public ShopItem(ItemStack item, int priceChangeRequiredVolume, int basePrice, boolean canSell){
        this.canSell = canSell;
        this.actualItem = item;
        this.priceChangeRequiredVolume = priceChangeRequiredVolume;
        this.type = item.getType();
        this.basePrice = basePrice;
        this.offset = 0;
        SurvivalAddons2 plugin = SurvivalAddons2.getPlugin();
        if(plugin.getConfig().get("shop." + item.getType() + ".bought") == null){
            plugin.getConfig().set("shop." + item.getType() + ".bought", 0);
        }
        if(plugin.getConfig().get("shop." + item.getType() + ".sold") == null){
            plugin.getConfig().set("shop." + item.getType() + ".sold", 0);
        }
        if(plugin.getConfig().get("shop." + item.getType() + ".transactions") == null){
            plugin.getConfig().set("shop." + item.getType() + ".transactions", 0);
        }
        if(plugin.getConfig().get("shop." + item.getType() + ".bias") == null){
            plugin.getConfig().set("shop." + item.getType() + ".bias", 0);
        }
        if(plugin.getConfig().get("shop." + item.getType() + ".offset") == null){
            plugin.getConfig().set("shop." + item.getType() + ".offset", 0);
        }
        if(plugin.getConfig().get("shop." + item.getType() + ".price") == null){
            plugin.getConfig().set("shop." + item.getType() + ".price", basePrice);
        }
        plugin.getConfig().set("shop." + item.getType() + ".baseprice", basePrice);
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
        double newPrice = plugin.getConfig().getDouble("shop." + type + ".price");
        for(int i = 0; i<amount; i++){
            offset += 1;
            if(offset >= priceChangeRequiredVolume){
                oldBias+=1;
                offset = 0;
                newPrice = basePrice + basePrice*0.1*oldBias; //+ newPrice*0.1*oldBias;
            }
            dustPrice += Math.round(newPrice);
        }
        plugin.getConfig().set("shop." + type + ".price", newPrice);
        plugin.getConfig().set("shop." + type + ".bias", oldBias);
        plugin.getConfig().set("shop." + type + ".offset", offset);
        plugin.saveConfig();
        return Math.max(1, dustPrice);
    }

    public int sellItem(int amount){
        int dustPrice = 0;
        SurvivalAddons2 plugin = SurvivalAddons2.getPlugin();
        int basePrice = plugin.getConfig().getInt("shop." + type + ".baseprice");
        int offset = plugin.getConfig().getInt("shop." + type + ".offset");
        int oldBias = plugin.getConfig().getInt("shop." + type + ".bias");
        plugin.getConfig().set("shop." + type + ".sold", plugin.getConfig().getInt("shop." + type + ".sold") + amount);
        plugin.getConfig().set("shop." + type + ".transactions", plugin.getConfig().getInt("shop." + type + ".transactions") + 1);
        double newPrice = plugin.getConfig().getDouble("shop." + type + ".price");
        for(int i = 0; i<amount; i++){
            dustPrice += Math.round(newPrice);
            offset -= 1;
            if(offset < 0){
                oldBias-=1;
                offset = priceChangeRequiredVolume-1;
                newPrice = (basePrice + basePrice*0.1*oldBias);//newPrice*0.1*oldBias);
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
