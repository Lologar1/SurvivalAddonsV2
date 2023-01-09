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
            if(args[0].equals("resetQuests")){
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
            }
            if(args[0].equals("getQuestPDC")){
                Player target = SurvivalAddons2.getPlugin().getServer().getPlayer(args[1]);
                player.sendMessage(pdc.get(target, "hourlyLast"));
                player.sendMessage(pdc.get(target, "activeHourlyQuest"));
                player.sendMessage(pdc.get(target, "hourlyQuestProgress"));
                player.sendMessage(pdc.get(target, "deactivatedHourlyQuest"));
                player.sendMessage(pdc.get(target, "dailyLast"));
                player.sendMessage(pdc.get(target, "activeDailyQuest"));
                player.sendMessage(pdc.get(target, "dailyQuestProgress"));
                player.sendMessage(pdc.get(target, "deactivatedDailyQuest"));
                player.sendMessage(pdc.get(target, "weeklyLast"));
                player.sendMessage(pdc.get(target, "activeWeeklyQuest"));
                player.sendMessage(pdc.get(target, "weeklyQuestProgress"));
                player.sendMessage(pdc.get(target, "deactivatedWeeklyQuest"));
            }
            return true;
        }else{
         sender.sendMessage(ChatColor.RED + "You aren't allowed to use this!");
         return true;
        }
    }
}
