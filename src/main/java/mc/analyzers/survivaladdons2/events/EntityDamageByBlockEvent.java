package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.AttributeUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getVanillaEnchantmentProtectionFactors;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.dealDamage;

public class EntityDamageByBlockEvent implements Listener {
    @EventHandler
    public void EntityDamageByBlockEvent(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        if(e instanceof EntityDamageByEntityEvent){
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
