package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.tasks.CombatTagTick;
import mc.analyzers.survivaladdons2.tasks.RefreshInventory;
import mc.analyzers.survivaladdons2.tasks.SyncAttributes;
import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.chatChannel;
import static mc.analyzers.survivaladdons2.utility.itemList.item;
import static mc.analyzers.survivaladdons2.utility.utility.giveItem;

public class onPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        chatChannel.sendMessage("`" + player.getDisplayName() + " joined the server !`").queue();
        if(!pdc.has(player, "dust")){
            pdc.set(player, "dust", "25");
        }
        if(!pdc.has(player, "inCombat")){
            pdc.set(player, "inCombat", "false/0");
        }
        if(!pdc.has(player, "effects")){
            pdc.set(player, "effects", " ");
        }
        if(!pdc.has(player, "damageValues")){
            pdc.set(player, "damageValues", "0/0"); //Dealt/Taken
        }
        if(!pdc.has(player, "assist")){
            pdc.set(player, "assist", " "); //Player's names : Analyzers CarryBit etc.
        }
        //Quests
        if(!pdc.has(player, "weeklyLast")){
            pdc.set(player, "weeklyLast", "0");
        }
        if(!pdc.has(player, "dailyLast")){
            pdc.set(player, "dailyLast", "0");
        }
        if(!pdc.has(player, "hourlyLast")){
            pdc.set(player, "hourlyLast", "0");
        }

        if(!pdc.has(player, "activeWeeklyQuest")){
            pdc.set(player, "activeWeeklyQuest", "null");
        }
        if(!pdc.has(player, "activeDailyQuest")){
            pdc.set(player, "activeDailyQuest", "null");
        }
        if(!pdc.has(player, "activeHourlyQuest")){
            pdc.set(player, "activeHourlyQuest", "null");
        }

        if(!pdc.has(player, "hourlyQuestProgress")){
            pdc.set(player, "hourlyQuestProgress", "0");
        }
        if(!pdc.has(player, "dailyQuestProgress")){
            pdc.set(player, "dailyQuestProgress", "0");
        }
        if(!pdc.has(player, "weeklyQuestProgress")){
            pdc.set(player, "weeklyQuestProgress", "0");
        }
        if(!pdc.has(player, "currentQuestType")){
            pdc.set(player, "currentQuestType", "none");
        }
        if(!pdc.has(player, "deactivatedWeeklyQuest")){
            pdc.set(player, "deactivatedWeeklyQuest", "null");
        }
        if(!pdc.has(player, "deactivatedHourlyQuest")){
            pdc.set(player, "deactivatedHourlyQuest", "null");
        }
        if(!pdc.has(player, "deactivatedDailyQuest")){
            pdc.set(player, "deactivatedDailyQuest", "null");
        }
        BukkitTask drainCombatTag = new CombatTagTick(SurvivalAddons2.getPlugin(), e.getPlayer()).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
        BukkitTask updateInventory = new SyncAttributes(e.getPlayer(), SurvivalAddons2.getPlugin()).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
        BukkitTask refreshPlayer = new RefreshInventory(player).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
        player.sendMessage(ChatColor.RED + "Shields and totems of undying are disabled.");
        player.sendMessage(ChatColor.DARK_RED + "Worldborder : 12000 blocks wide");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Join the discord to know custom crafts!");
        player.sendMessage(ChatColor.GOLD + "https://discord.gg/8Ktb72xDKN");
        player.sendMessage(ChatColor.GREEN + "Some useful commands :" + ChatColor.GRAY + " /view, /rename, /withdraw and /deposit");
        if(!pdc.has(player, "firstTime")){
            giveItem(player, item("funky_feather"), 1);
            player.sendMessage(ChatColor.GREEN + "You have been granted 1 " + ChatColor.DARK_AQUA + "Funky Feather");
            pdc.set(player, "firstTime", "true");
        }
    }
}
