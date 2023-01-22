package mc.analyzers.survivaladdons2.commands;

import jdk.vm.ci.meta.SpeculationLog;
import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            if(args[0].equals("renameColor")){
                ItemStack gre = player.getInventory().getItemInMainHand();
                ItemMeta greMeta = gre.getItemMeta();
                greMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of(args[2]) + args[1]);
                gre.setItemMeta(greMeta);
            }
            if(args[0].equals("renameColorBold")){
                ItemStack gre = player.getInventory().getItemInMainHand();
                ItemMeta greMeta = gre.getItemMeta();
                greMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of(args[2]) + "" + ChatColor.BOLD + args[1]);
                gre.setItemMeta(greMeta);
            }
            if(args[0].equals("setPDC")){
                PDCUtils.set(player, args[1], args[2]);
                player.sendMessage(ChatColor.DARK_GREEN + "PDC SET!");
            }
            return true;
        }else{
         sender.sendMessage(ChatColor.RED + "You aren't allowed to use this!");
         return true;
        }
    }
}
