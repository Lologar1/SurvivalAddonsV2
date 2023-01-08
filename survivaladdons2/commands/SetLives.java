package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.jda;

public class SetLives implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()){
            Player player = (Player) sender;
            pdc.set(player, "hourlyLast", "0");
            pdc.set(player, "dailyLast", "0");
            pdc.set(player, "weeklyLast", "0");
            pdc.set((player), "activeHourlyQuest", "null");
            pdc.set((player), "activeDailyQuest", "null");
            pdc.set((player), "activeWeeklyQuest", "null");
            pdc.set(player, "hourlyQuestProgress", "0");
            pdc.set(player, "dailyQuestProgress", "0");
            pdc.set(player, "weeklyQuestProgress", "0");
            pdc.set(player, "currentQuestType", "none");
            pdc.set((player), "deactivatedHourlyQuest", "null");
            pdc.set((player), "deactivatedDailyQuest", "null");
            pdc.set((player), "deactivatedWeeklyQuest", "null");
            return true;
        }else{
         sender.sendMessage(ChatColor.RED + "You aren't allowed to use this!");
         return true;
        }
    }
}
