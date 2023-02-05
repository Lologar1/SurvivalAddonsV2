package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.*;

public class withdraw implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(!(args.length == 1)){
            player.sendMessage(ChatColor.RED + "Proper syntax: /withdraw [amount]");
        }

        try{
            Integer.parseInt(args[0]);
        }catch (Exception e){
            player.sendMessage(ChatColor.RED + "Proper syntax: /withdraw [amount]");
        }
        int capacity = 0;
        for(ItemStack slot : player.getInventory()){
            if(slot == null){
                capacity = capacity + 64;
            }else if(PDCUtils.has(slot, "id") && PDCUtils.get(slot, "id").equals("redstone_bundle")){
                if(PDCUtils.hasInt(slot, "redstone") && PDCUtils.getInt(slot, "redstone") > 0) {
                    continue;
                }else if(hasDust(player, Integer.parseInt(args[0]))){
                    PDCUtils.set(slot, "redstone", Integer.parseInt(args[0]));
                    slot.setType(Material.CHEST_MINECART);
                    ItemMeta meta = slot.getItemMeta();
                    meta.setDisplayName(ChatColor.DARK_AQUA + "Redstone Bundle : " + ChatColor.RED + args[0] + dustIcon);
                    meta.setLore(Arrays.asList(ChatColor.GRAY + "Right-Click to redeem the dust inside!", net.md_5.bungee.api.ChatColor.GRAY + "Mined dust directly goes inside!"));
                    slot.setItemMeta(meta);
                    player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "STORE! " + ChatColor.RESET + ChatColor.GRAY + "Stored redstone in bundle!");
                    subtractDust(player, Integer.parseInt(args[0]));
                    return true;
                }else{
                    continue;
                }
            }
        }
        if(args.length == 1 && Integer.parseInt(PDCUtils.get(player, "dust")) >= Integer.parseInt(args[0])){
            if(capacity >= Integer.parseInt(args[0])){
                PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - Integer.parseInt(args[0])));
                giveItem(player, new ItemStack(Material.REDSTONE), Integer.parseInt(args[0]));
                player.sendMessage(ChatColor.GRAY + "Withdrew " + ChatColor.GREEN + args[0] + ChatColor.RED + " " + dustIcon + " dust.");
            }else{
                player.sendMessage(ChatColor.RED + "Not enough inventory space !");
            }
        }else{
            player.sendMessage(ChatColor.RED + "Not enough dust or improper usage!");
        }
        return true;
    }
}
