package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.analyzers.survivaladdons2.utility.MiscUtils.capFirst;

public class SetLives implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()){
            Player player = (Player) sender;
            if(args[0].equals("resetQuests")){
                PDCUtils.set(player, args[1] + "Last", "0");
                PDCUtils.set((player), "active" + capFirst(args[1]) + "Quest", "null");
                PDCUtils.set(player, args[1] + "QuestProgress", "0");
                PDCUtils.set(player, "currentQuestType", "none");
                PDCUtils.set((player), "deactivated" + capFirst(args[1]) + "Quest", "null");
            }
            if(args[0].equals("getQuestPDC")){
                Player target = SurvivalAddons2.getPlugin().getServer().getPlayer(args[1]);
                player.sendMessage(PDCUtils.get(target, "hourlyLast"));
                player.sendMessage(PDCUtils.get(target, "activeHourlyQuest"));
                player.sendMessage(PDCUtils.get(target, "hourlyQuestProgress"));
                player.sendMessage(PDCUtils.get(target, "deactivatedHourlyQuest"));
                player.sendMessage(PDCUtils.get(target, "dailyLast"));
                player.sendMessage(PDCUtils.get(target, "activeDailyQuest"));
                player.sendMessage(PDCUtils.get(target, "dailyQuestProgress"));
                player.sendMessage(PDCUtils.get(target, "deactivatedDailyQuest"));
                player.sendMessage(PDCUtils.get(target, "weeklyLast"));
                player.sendMessage(PDCUtils.get(target, "activeWeeklyQuest"));
                player.sendMessage(PDCUtils.get(target, "weeklyQuestProgress"));
                player.sendMessage(PDCUtils.get(target, "deactivatedWeeklyQuest"));
            }
            return true;
        }else{
         sender.sendMessage(ChatColor.RED + "You aren't allowed to use this!");
         return true;
        }
    }
}
