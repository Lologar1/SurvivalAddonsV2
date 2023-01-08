package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.utility.utility.giveItem;

public class deposit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.getInventory().contains(Material.REDSTONE) || player.getInventory().contains(Material.REDSTONE_BLOCK)){
            int dustToAdd = 0;
            for(ItemStack item : player.getInventory()){
                if(item != null && !(pdc.has(item, "id")) && (item.getType().equals(Material.REDSTONE) || item.getType().equals(Material.REDSTONE_BLOCK))){
                    if(item.getType().equals(Material.REDSTONE_BLOCK)){
                        dustToAdd += item.getAmount() * 9;
                    }else{
                        dustToAdd += item.getAmount();
                    }

                    item.setAmount(0);
                }
            }
            pdc.set(player, "dust", String.valueOf(Integer.parseInt(pdc.get(player, "dust")) + dustToAdd));
            player.sendMessage(ChatColor.GRAY + "Deposited " + ChatColor.GREEN + dustToAdd + ChatColor.RED + " " + dustIcon + " dust.");
        }else{
            player.sendMessage(ChatColor.RED + "No redstone to deposit !");
        }
        return true;
    }
}
