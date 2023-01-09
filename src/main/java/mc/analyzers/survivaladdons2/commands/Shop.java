package mc.analyzers.survivaladdons2.commands;

import jdk.tools.jlink.plugin.Plugin;
import mc.analyzers.survivaladdons2.shop.ShopItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.shop.ShopItem.getByMaterial;

public class Shop implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Inventory shop = Bukkit.createInventory(player, 36, ChatColor.GRAY + "Shop");

        shop.addItem(ShopItems.dirt.getActualItem());
        shop.addItem(ShopItems.cobblestone.getActualItem());

        shop.addItem(ShopItems.diamond.getActualItem());
        shop.addItem(ShopItems.emerald.getActualItem());
        shop.addItem(ShopItems.lapis_lazuli.getActualItem());
        shop.addItem(ShopItems.gold_ingot.getActualItem());
        shop.addItem(ShopItems.iron_ingot.getActualItem());
        shop.addItem(ShopItems.netherite_scrap.getActualItem());
        shop.addItem(ShopItems.bedrock.getActualItem());
        shop.addItem(ShopItems.coal.getActualItem());
        shop.addItem(ShopItems.bone.getActualItem());
        shop.addItem(ShopItems.flesh.getActualItem());
        shop.addItem(ShopItems.string.getActualItem());
        shop.addItem(ShopItems.blazerod.getActualItem());
        shop.addItem(ShopItems.spidereye.getActualItem());
        shop.addItem(ShopItems.gunpowder.getActualItem());
        shop.addItem(ShopItems.ghast_tear.getActualItem());
        shop.addItem(ShopItems.magma_cream.getActualItem());
        shop.addItem(ShopItems.quartz.getActualItem());
        shop.addItem(ShopItems.ender_pearl.getActualItem());
        shop.addItem(ShopItems.slime.getActualItem());

        for(ItemStack addedItem : shop.getContents()){
            if(addedItem == null){
                continue;
            }
            addedItem.setAmount(1);
            ItemMeta meta = addedItem.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to buy for " + ChatColor.RED + getByMaterial(addedItem.getType()).getBiasedPrice() + " " + dustIcon + " dust");
            if(!getByMaterial(addedItem.getType()).isCanSell()){
                lore.add(ChatColor.RED + "Can't sell back!");
            }
            meta.setLore(lore);
            addedItem.setItemMeta(meta);
        }

        player.openInventory(shop);
        return true;
    }
}
