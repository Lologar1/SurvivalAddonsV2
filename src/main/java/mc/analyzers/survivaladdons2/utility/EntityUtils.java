package mc.analyzers.survivaladdons2.utility;

import com.google.common.primitives.Ints;
import mc.analyzers.survivaladdons2.SurvivalAddons2;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Raid;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Double.max;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.percentChance;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.preciseRound;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.giveItem;

public class EntityUtils {
    public static void displayDamage(LivingEntity entity, double damage, double magicdamage){
        double DAMAGE_RESISTANCE_PERCENTILE = 0.6;
        if(!isHostile(entity)){
            DAMAGE_RESISTANCE_PERCENTILE = 1.0;
        }
        double totalFinalDamage = damage*DAMAGE_RESISTANCE_PERCENTILE + magicdamage;
        boolean can = true;
        /*
        Collection<Entity> nearby = entity.getWorld().getNearbyEntities(entity.getEyeLocation(), 1, 1 ,1);
        for(Entity near :nearby){
            if(near.getType().equals(EntityType.ARMOR_STAND) && near.isInvulnerable()){
                can = false;
            }
        }*/
        if(can){
            Location spawnLoc = entity.getLocation();
            ArmorStand damageIndicator = (ArmorStand) entity.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND);
            damageIndicator.setVisible(false);
            damageIndicator.setCustomNameVisible(true);
            damageIndicator.setGravity(false);
            damageIndicator.setInvulnerable(true);
            damageIndicator.setCollidable(false);
            damageIndicator.setCanPickupItems(false);
            damageIndicator.setSmall(true);
            if(magicdamage == 0){
                damageIndicator.setCustomName(net.md_5.bungee.api.ChatColor.of("#FB1A1A") + "\uD83D\uDDE1" + " " + preciseRound(damage*DAMAGE_RESISTANCE_PERCENTILE, 1));
            }else{
                damageIndicator.setCustomName(net.md_5.bungee.api.ChatColor.of("#FB1A1A") + "\uD83D\uDDE1" + " " +
                        Math.round(damage*DAMAGE_RESISTANCE_PERCENTILE) + ChatColor.WHITE + " + " + net.md_5.bungee.api.ChatColor.of("#34D9FD") + "\uD83E\uDDEA" + " " + preciseRound(magicdamage, 1)
                        + " " + ChatColor.YELLOW + "(" + preciseRound(totalFinalDamage, 1) + ")");
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    damageIndicator.remove();
                }

            }.runTaskLater(SurvivalAddons2.getPlugin(), 20);
        }
    }
    public static boolean isHostile(LivingEntity damager){
        return damager instanceof Monster || damager instanceof EnderDragon
                || damager instanceof Ghast || damager instanceof Hoglin
                || damager instanceof Phantom || damager instanceof Shulker
                || damager instanceof Slime;
    }
    public static double damageEntity(LivingEntity entity, double damage, double magicdamage, Entity damager, boolean overrideEvent){
        double toDo;
        displayDamage(entity, damage, magicdamage);
        double DAMAGE_RESISTANCE_PERCENTILE = 0.6;
        if(!isHostile(entity)){
            DAMAGE_RESISTANCE_PERCENTILE = 1.0;
        }
        double totalFinalDamage = damage*DAMAGE_RESISTANCE_PERCENTILE + magicdamage;
        toDo = totalFinalDamage;
        if(overrideEvent){
            entity.damage(totalFinalDamage, damager);
        }
        boolean died = false;
        if(max(entity.getHealth() - totalFinalDamage, 0) == 0){
            died = true;
        }
        if(damager == null){
            return toDo;
        }
        if(damager instanceof LivingEntity){
            if(!(damager instanceof Player)){
                return toDo;
            }
            Player killer = (Player) damager;
            if(!died){
                return toDo;
            }
            List<Raid> raids = killer.getWorld().getRaids();
            boolean inRaid = false;
            for(Raid raid : raids){
                if(raid.getLocation().distance(killer.getLocation()) <= 64){
                    inRaid = true;
                }
            }
            if((entity.getType().equals(EntityType.PILLAGER) || entity.getType().equals(EntityType.VINDICATOR) || entity.getType().equals(EntityType.EVOKER)) && ((Illager) entity).isPatrolLeader() && !inRaid){
                int amp = 0;
                if(killer.hasPotionEffect(PotionEffectType.BAD_OMEN)){
                    amp = killer.getPotionEffect(PotionEffectType.BAD_OMEN).getAmplifier() + 1;
                }
                killer.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 72000, Ints.min(amp, 4)-1));
                killer.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "" + "BAD OMEN! " + ChatColor.GRAY + "Level " + (Ints.min(amp, 4)+1));
            }

            double lootingFactor = 0;
            if(killer.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_MOBS)){
                lootingFactor = killer.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
            }

            if(entity.getType().equals(EntityType.ENDERMAN)){
                boolean dropCore = percentChance(0.01 + 0.01 * lootingFactor/2);
                if(dropCore){
                    giveItem(killer, ItemList.item("ender_core"), 1);
                    killer.sendMessage(ChatColor.LIGHT_PURPLE + "RARE DROP! " + ChatColor.GRAY + "Ender Core");
                }
            }else if(entity.getType().equals(EntityType.SKELETON)){
                boolean dropCore = percentChance(0.001 + 0.001 * lootingFactor/2);
                if(dropCore){
                    giveItem(killer, ItemList.item("shortbow_core"), 1);
                    killer.sendMessage(ChatColor.LIGHT_PURPLE + "RARE DROP! " + ChatColor.GRAY + "Shortbow Core");
                }
            }else if(entity.getType().equals(EntityType.WARDEN)){
                giveItem(killer, ItemList.item("warden_heart"), 1);
                killer.sendMessage(ChatColor.LIGHT_PURPLE + "DROP! " + ChatColor.GRAY + "Warden's Heart");
            }else if(entity.getType().equals(EntityType.WITHER)){
                boolean dropStar = percentChance(0.3 + 0.03 * lootingFactor/2);
                if(dropStar){
                    giveItem(killer, ItemList.item("fallen_star"), 1);
                    killer.sendMessage(ChatColor.LIGHT_PURPLE + "RARE DROP! " + ChatColor.GRAY + "Fallen Star");
                }
            }
        }
        return toDo;
    }
}
