package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.AttributeUtils;
import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getVanillaEnchantmentProtectionFactors;
import static mc.analyzers.survivaladdons2.utility.DamagePlayer.dealDamage;

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
