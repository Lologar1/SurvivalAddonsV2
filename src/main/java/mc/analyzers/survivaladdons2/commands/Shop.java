package mc.analyzers.survivaladdons2.commands;

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

public class Shop implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Inventory shop = Bukkit.createInventory(player, 27, ChatColor.GRAY + "Shop");

        ItemStack anvilGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta anvilGlassMeta = anvilGlass.getItemMeta();
        anvilGlassMeta.setDisplayName(ChatColor.BLACK + "");
        anvilGlass.setItemMeta(anvilGlassMeta);

        for(int i = 0; i<27; i++){
            shop.setItem(i, anvilGlass);
        }
        ItemStack building = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta buildingMeta = building.getItemMeta();
        buildingMeta.setDisplayName(ChatColor.GREEN + "Building blocks");
        building.setItemMeta(buildingMeta);

        ItemStack mining = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta miningMeta = mining.getItemMeta();
        miningMeta.setDisplayName(ChatColor.GREEN + "Mining");
        mining.setItemMeta(miningMeta);

        ItemStack mob = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta mobMeta = mob.getItemMeta();
        mobMeta.setDisplayName(ChatColor.GREEN + "Mob drops");
        mob.setItemMeta(mobMeta);

        ItemStack farming = new ItemStack(Material.WHEAT);
        ItemMeta farmingMeta = farming.getItemMeta();
        farmingMeta.setDisplayName(ChatColor.GREEN + "Farming");
        farming.setItemMeta(farmingMeta);

        ItemStack other = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta otherMeta = other.getItemMeta();
        otherMeta.setDisplayName(ChatColor.GREEN + "Miscellaneous");
        other.setItemMeta(otherMeta);

        shop.setItem(11, building);
        shop.setItem(12, mining);
        shop.setItem(13, mob);
        shop.setItem(14, farming);
        shop.setItem(15, other);


        player.openInventory(shop);
        return true;
    }
}
