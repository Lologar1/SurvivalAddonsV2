package mc.analyzers.survivaladdons2.tasks;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.Locale;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getDefenseValues;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.capFirst;

public class CombatTagTick extends BukkitRunnable {

    SurvivalAddons2 plugin;
    Player player;

    public CombatTagTick(SurvivalAddons2 plugin, Player player){
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        int secondsLeft = Integer.parseInt(PDCUtils.get(player, "inCombat").split("/")[1]);
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("info", "dummy", ChatColor.BOLD + "" + net.md_5.bungee.api.ChatColor.of("#F859FB") + "Player Info");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score status = objective.getScore(ChatColor.BLUE + "Status : " + ChatColor.GREEN + "Idling");
        if(secondsLeft >= 1){
            PDCUtils.set(player, "inCombat", "true/" + (secondsLeft - 1));
            //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "In combat (" + secondsLeft + "s left)"));
            player.setGliding(false);
            status = objective.getScore(ChatColor.BLUE + "Status : " + ChatColor.RED + "In combat (" + secondsLeft + "s left)");
        }else{
            PDCUtils.set(player, "inCombat", "false/0");
            PDCUtils.set(player, "damageValues", "0/0"); //Dealt/Taken
            PDCUtils.set(player, "assist", ""); //Player's names : Analyzers/CarryBit etc.
            //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Defense : " +
            //(int) Math.round( (1-(getProtectionFactors(player)[0] - 0.2)/0.8)*1000 ) + ChatColor.AQUA + " Magic Defense : " + (int) Math.round( (1-(getProtectionFactors(player)[1] - 0.2)/0.8)*1000)));
        }
        player.setPlayerListName(player.getDisplayName() + " " + ChatColor.GOLD + "Dust: " + ChatColor.RED + PDCUtils.get(player, "dust"));

        Score score = objective.getScore(ChatColor.RED + dustIcon + " Redstone Dust " + ChatColor.GOLD + ": " + PDCUtils.get(player, "dust"));
        double defense = getDefenseValues(player)[0];
        double magicdef = getDefenseValues(player)[1];
        score.setScore(10);
        Score score1 = objective.getScore("  ");
        score1.setScore(9);
        Score score2 = objective.getScore(ChatColor.GREEN + "Defense : " + defense);
        score2.setScore(8);
        Score score3 = objective.getScore("   ");
        score3.setScore(2);
        Score score4 = objective.getScore(ChatColor.AQUA + "Magic defense : " + magicdef);
        score4.setScore(7);
        Score em = objective.getScore("    ");
        em.setScore(6);
        status.setScore(5);
        Score em2 = objective.getScore("     ");
        em2.setScore(4);

        if(!PDCUtils.get(player, "currentQuestType").equals("none")){
            String currentProgress = PDCUtils.get(player, PDCUtils.get(player, "currentQuestType").toLowerCase(Locale.ROOT) + "QuestProgress");
            String[] split = currentProgress.split("/");
            Score activeQuest = objective.getScore((ChatColor.GRAY + capFirst(PDCUtils.get(player, "currentQuestType")) + " quest! " + Integer.parseInt(split[0]) + "/" + split[1] + " finished."));
            activeQuest.setScore(3);
        }else{
            Score activeQuest = objective.getScore("No active quests!");
            activeQuest.setScore(3);
        }


        Score bottom = objective.getScore(ChatColor.YELLOW + "" +ChatColor.BOLD + "SurvivalAddons2");
        bottom.setScore(1);

        Score credits = objective.getScore(ChatColor.WHITE + "by Analyzers");
        credits.setScore(0);
        player.setScoreboard(board);
        if(SurvivalAddons2.getPlugin().getServer().getOnlinePlayers().contains(player)){
            BukkitTask drainCombatTag = new CombatTagTick(plugin, player).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
        }
    }
}
