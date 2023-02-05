package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.AttributeUtils;
import mc.analyzers.survivaladdons2.utility.EntityUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getVanillaEnchantmentProtectionFactors;
import static mc.analyzers.survivaladdons2.utility.EntityUtils.displayDamage;
import static mc.analyzers.survivaladdons2.utility.EntityUtils.isHostile;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.preciseRound;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.dealDamage;

public class EntityDamageByBlockEvent implements Listener {
    @EventHandler
    public void EntityDamageByBlockEvent(EntityDamageEvent e){
        if(e instanceof EntityDamageByEntityEvent){
            return;
        }
        if(!(e.getEntity() instanceof Player)){
            if(!(e.getEntity() instanceof LivingEntity)){
             return;
            }
            double DAMAGE_RESISTANCE_PERCENTILE = 0.6;
            if(!isHostile((LivingEntity) e.getEntity())){
                DAMAGE_RESISTANCE_PERCENTILE = 1.0d;
            }
            double damage = e.getFinalDamage();
            double totalFinalDamage = damage*DAMAGE_RESISTANCE_PERCENTILE;
            LivingEntity entity = (LivingEntity) e.getEntity();
            e.setDamage(totalFinalDamage);
            displayDamage(entity, damage, 0);
            return;
        }
        Player player = (Player) e.getEntity();
        if(e.getEntity() instanceof HumanEntity && e instanceof org.bukkit.event.entity.EntityDamageByBlockEvent){
            double defenseFactor = AttributeUtils.getProtectionFactors(player)[0];
            double finalDMG = (e.getDamage() * defenseFactor) - (e.getDamage() * defenseFactor)* getVanillaEnchantmentProtectionFactors(player)[2]/100;
            dealDamage(player, finalDMG, 0, e.getCause());
            e.setDamage(0); //FIX THIS! get 0 dmg :(
            return;
        }
        dealDamage(player, e.getDamage(), 0, e.getCause());
        e.setDamage(0);
    }
}
