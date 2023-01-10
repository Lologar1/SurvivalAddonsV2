package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;

public class dust implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        player.sendMessage(ChatColor.GOLD + "You have " + PDCUtils.get(player, "dust") + " " + ChatColor.RED + dustIcon + " dust.");
        return true;
    }
}
