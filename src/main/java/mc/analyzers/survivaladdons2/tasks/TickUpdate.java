package mc.analyzers.survivaladdons2.tasks;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.getCustomEnchantments;

public class TickUpdate extends BukkitRunnable {
    SurvivalAddons2 plugin;
    Player player;

    public TickUpdate(SurvivalAddons2 plugin, Player player){
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        int secondsLeft = Integer.parseInt(PDCUtils.get(player, "inCombat").split("/")[1]);
        if(secondsLeft >= 1){
            player.setGliding(false);
        }
        if(player.getInventory().getBoots() != null && player.getInventory().getBoots().hasItemMeta() && getCustomEnchantments(player.getInventory().getBoots()).containsKey("rush")){
            player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation(), 1, 0, 0, 0, 0.05);
        }
        if(SurvivalAddons2.getPlugin().getServer().getOnlinePlayers().contains(player)){
            BukkitTask anotherTick = new TickUpdate(plugin, player).runTaskLater(SurvivalAddons2.getPlugin(), 1L);
        }
    }
}
