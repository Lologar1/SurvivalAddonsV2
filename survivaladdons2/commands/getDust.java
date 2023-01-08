package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class getDust implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(!player.isOp()){
            player.sendMessage(ChatColor.RED + "You aren't allowed to use this command!");
            return false;
        }
        if(args.length == 1){
            pdc.set(player, "dust", args[0]);
        }else{
            player.sendMessage(ChatColor.RED + "Improper usage! Provide one valid argument.");
        }

        return true;
    }
}
