package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.ItemList;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Locale;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.heartIcon;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.giveItem;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.syncItem;

public class getItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = SurvivalAddons2.getPlugin().getServer().getPlayer(args[0]);
        if(!sender.isOp()){
            player.sendMessage(ChatColor.RED + "You aren't allowed to use this command!");
            return false;
        }
        int amount = Integer.parseInt(args[2]);
        try{
            giveItem(player, ItemList.item(args[1]), amount);
        }catch (Exception ignored){}

        switch(args[1].toLowerCase()){
            case "enchanted_book":
                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                PDCUtils.set(book, "enchantments", args[3] + "/" + args[4] + " ");
                syncItem(book);
                giveItem(player, book, amount);
                break;
            case "healing_stick":
                ItemStack stick = ItemList.item("healing_stick");
                PDCUtils.set(stick, "healing", args[2]);
                ArrayList<String> healinglore = new ArrayList<>();
                healinglore.add(ChatColor.GRAY + "Use this item to heal for");
                healinglore.add(ChatColor.RED + args[2] + heartIcon + ChatColor.GRAY + ", costing " + ChatColor.RED + args[2]+1 + dustIcon + " dust.");
                ItemMeta stickmeta = stick.getItemMeta();
                stickmeta.setLore(healinglore);
                stick.setItemMeta(stickmeta);
                giveItem(player, stick, amount);
                break;
            case "food_stick":
                stick = ItemList.item("food_stick");
                PDCUtils.set(stick, "food", args[2]);
                ArrayList<String> foodlore = new ArrayList<>();
                foodlore.add(ChatColor.GRAY + "Use this item to satiate for");
                foodlore.add(ChatColor.GOLD + String.valueOf(Integer.parseInt(args[2])*2) + " saturation" + ChatColor.GRAY + ", costing " + ChatColor.RED + args[2] + dustIcon + " dust.");
                ItemMeta foodmeta = stick.getItemMeta();
                foodmeta.setLore(foodlore);
                stick.setItemMeta(foodmeta);
                giveItem(player, stick, amount);
                break;
            case "fling_stick":
                stick = ItemList.item("fling_stick");
                PDCUtils.set(stick, "fling", args[2]);
                ArrayList<String> flinglore = new ArrayList<>();
                flinglore.add(ChatColor.DARK_RED + "ADMIN ITEM");
                flinglore.add(ChatColor.GRAY + "Use this item to launch yourself!");
                ItemMeta flingmeta = stick.getItemMeta();
                flingmeta.setLore(flinglore);
                stick.setItemMeta(flingmeta);
                giveItem(player, stick, amount);
                break;
            case "wooden_armor":
                giveItem(player, ItemList.item("wooden_helmet"), 1);
                giveItem(player, ItemList.item("wooden_chestplate"), 1);
                giveItem(player, ItemList.item("wooden_leggings"), 1);
                giveItem(player, ItemList.item("wooden_boots"), 1);
                break;
        }
        return true;
    }
}
