package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(pdc.get(player, "inCombat").contains("true")){
                player.sendMessage(ChatColor.RED + "You are in combat!");
                return true;
            }
            player.teleport(new Location(SurvivalAddons2.getPlugin().getServer().getWorld("world"), 0, 86, 0));
            player.sendMessage(ChatColor.GREEN + "Teleported to spawn !");
        }
        return true;
    }
}
