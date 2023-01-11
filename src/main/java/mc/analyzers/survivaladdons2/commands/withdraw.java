package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.giveItem;

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
            player.sendMessage(ChatColor.RED + "Improper usage ! Provide one valid numerical argument.");
        }
        return true;
    }
}
