package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.tasks.CombatTagTick;
import mc.analyzers.survivaladdons2.tasks.RefreshInventory;
import mc.analyzers.survivaladdons2.tasks.SyncAttributes;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.chatChannel;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.giveItem;
import static mc.analyzers.survivaladdons2.utility.ItemList.item;

public class onPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        chatChannel.sendMessage("`" + player.getDisplayName() + " joined the server !`").queue();
        if(!PDCUtils.has(player, "dust")){
            PDCUtils.set(player, "dust", "25");
        }
        if(!PDCUtils.has(player, "inCombat")){
            PDCUtils.set(player, "inCombat", "false/0");
        }
        if(!PDCUtils.has(player, "effects")){
            PDCUtils.set(player, "effects", " ");
        }
        if(!PDCUtils.has(player, "damageValues")){
            PDCUtils.set(player, "damageValues", "0/0"); //Dealt/Taken
        }
        if(!PDCUtils.has(player, "assist")){
            PDCUtils.set(player, "assist", " "); //Player's names : Analyzers CarryBit etc.
        }
        //Quests
        if(!PDCUtils.has(player, "weeklyLast")){
            PDCUtils.set(player, "weeklyLast", "0");
        }
        if(!PDCUtils.has(player, "dailyLast")){
            PDCUtils.set(player, "dailyLast", "0");
        }
        if(!PDCUtils.has(player, "hourlyLast")){
            PDCUtils.set(player, "hourlyLast", "0");
        }

        if(!PDCUtils.has(player, "activeWeeklyQuest")){
            PDCUtils.set(player, "activeWeeklyQuest", "null");
        }
        if(!PDCUtils.has(player, "activeDailyQuest")){
            PDCUtils.set(player, "activeDailyQuest", "null");
        }
        if(!PDCUtils.has(player, "activeHourlyQuest")){
            PDCUtils.set(player, "activeHourlyQuest", "null");
        }

        if(!PDCUtils.has(player, "hourlyQuestProgress")){
            PDCUtils.set(player, "hourlyQuestProgress", "0");
        }
        if(!PDCUtils.has(player, "dailyQuestProgress")){
            PDCUtils.set(player, "dailyQuestProgress", "0");
        }
        if(!PDCUtils.has(player, "weeklyQuestProgress")){
            PDCUtils.set(player, "weeklyQuestProgress", "0");
        }
        if(!PDCUtils.has(player, "currentQuestType")){
            PDCUtils.set(player, "currentQuestType", "none");
        }
        if(!PDCUtils.has(player, "deactivatedWeeklyQuest")){
            PDCUtils.set(player, "deactivatedWeeklyQuest", "null");
        }
        if(!PDCUtils.has(player, "deactivatedHourlyQuest")){
            PDCUtils.set(player, "deactivatedHourlyQuest", "null");
        }
        if(!PDCUtils.has(player, "deactivatedDailyQuest")){
            PDCUtils.set(player, "deactivatedDailyQuest", "null");
        }
        BukkitTask drainCombatTag = new CombatTagTick(SurvivalAddons2.getPlugin(), e.getPlayer()).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
        BukkitTask updateInventory = new SyncAttributes(e.getPlayer(), SurvivalAddons2.getPlugin()).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
        BukkitTask refreshPlayer = new RefreshInventory(player).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
        player.sendMessage(ChatColor.RED + "Shields and totems of undying are disabled.");
        player.sendMessage(ChatColor.DARK_RED + "Worldborder : 12000 blocks wide");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Join the discord to know custom crafts!");
        player.sendMessage(ChatColor.GOLD + "https://discord.gg/8Ktb72xDKN");
        player.sendMessage(ChatColor.GREEN + "Some useful commands :" + ChatColor.GRAY + " /view, /rename, /withdraw, /quests, /shop and /deposit");
        if(!PDCUtils.has(player, "firstTime")){
            giveItem(player, item("funky_feather"), 1);
            player.sendMessage(ChatColor.GREEN + "You have been granted 1 " + ChatColor.DARK_AQUA + "Funky Feather");
            PDCUtils.set(player, "firstTime", "true");
        }
    }
}
