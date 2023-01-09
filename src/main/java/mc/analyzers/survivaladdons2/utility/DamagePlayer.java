package mc.analyzers.survivaladdons2.utility;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.A;

import java.util.Objects;

import static com.google.common.primitives.Doubles.min;
import static java.lang.Double.max;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getProtectionFactors;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getVanillaEnchantmentProtectionFactors;
import static mc.analyzers.survivaladdons2.utility.utility.*;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.PROJECTILE;

public class DamagePlayer {
    public static void dealDamage(Player player, double damage, double magicdamage, EntityDamageEvent.DamageCause cause){
        if(pdc.get(player, "effects").contains("singularity")){
            if(hasEffect(player, "singularity", true)){
                double truedamage = Math.min(damage + magicdamage, 4.0);
                boolean success = depleteTotalPlayerHealth(player, truedamage);
                System.out.println("Damage taken by " + player.getDisplayName() + " : " + truedamage);
                damageArmor(truedamage, player);
                if(success){
                    return;
                }
                killPlayer(player, cause);
            }
        }
        //System.out.println("MAGICdmg " + (magicdamage * getProtectionFactors(player)[1]));
        //System.out.println("DMGdmg " + (damage * getProtectionFactors(player)[0]));
        double totalFinalDamage = (damage * getProtectionFactors(player)[0]) + (magicdamage * getProtectionFactors(player)[1]);
        switch (cause){
            case PROJECTILE:
                totalFinalDamage = totalFinalDamage - totalFinalDamage * getVanillaEnchantmentProtectionFactors(player)[1]/100;
                break;
            case ENTITY_EXPLOSION:
                totalFinalDamage = totalFinalDamage - totalFinalDamage * getVanillaEnchantmentProtectionFactors(player)[2]/100;
                break;
            default:
                totalFinalDamage = totalFinalDamage - totalFinalDamage * getVanillaEnchantmentProtectionFactors(player)[0]/100;
        }
        if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
            totalFinalDamage = totalFinalDamage - totalFinalDamage * (0.15 * (player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier()+1));
        }
        if(totalFinalDamage < 0){
            totalFinalDamage = 0;
        }
        if(getCustomEnchantments(player.getInventory().getLeggings()).containsKey("dodge")){
            int potency = getCustomEnchantments(player.getInventory().getLeggings()).get("dodge");
            if(percentChance(0.04 * potency)){
                totalFinalDamage = 0.0;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "" + ChatColor.BOLD + "HIT DODGED!"));
                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getEyeLocation(), 1, 0, 0 ,0, 0.25);
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 2, 2);
            }
        }
        double abs = player.getAbsorptionAmount();
        player.setAbsorptionAmount(max(0, player.getAbsorptionAmount() - totalFinalDamage));
        if(Math.min(player.getHealth(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) - (totalFinalDamage - abs) > 0){
            player.setHealth(min(Math.min(player.getHealth(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) - max((totalFinalDamage - abs), 0), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        }else{
            killPlayer(player, cause);
        }

        System.out.println("Damage taken by " + player.getDisplayName() + " : " + totalFinalDamage);

        //Deplete damaged's armor durability
        damageArmor(totalFinalDamage, player);
    }

    public static boolean depleteTotalPlayerHealth(Player player, double damage){
        double totalhealth = Math.min(player.getHealth(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) + player.getAbsorptionAmount();
        if((totalhealth - damage) <= 0){
            return false;
        }
        double abs = player.getAbsorptionAmount();
        player.setAbsorptionAmount(Math.max(player.getAbsorptionAmount() - damage, 0));
        if((damage-abs) > 0){
            player.setHealth(Math.min(player.getHealth(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) - (damage - abs));
        }
        return true;
    }

    public static void damageArmor(double totalFinalDamage, Player player){
        int toDeplete = (int) max((totalFinalDamage/4), 1);
        int iterator = 0;
        for(ItemStack piece : player.getInventory().getArmorContents()){
            if(piece != null && piece.getType().equals(Material.ELYTRA)){
                continue;
            }
            double unbreakingFactor = 0;
            try{
                if(piece.getEnchantments().containsKey(Enchantment.DURABILITY)){
                    int unbreakingLevel = piece.getEnchantmentLevel(Enchantment.DURABILITY);
                    switch (unbreakingLevel){
                        case 1:
                            unbreakingFactor = 0.18;
                            break;
                        case 2:
                            unbreakingFactor = 0.24;
                            break;
                        case 3:
                            unbreakingFactor = 0.3;
                            break;
                    }
                }
            }catch (Exception ignored){}
            if(!(piece == null) && piece.getItemMeta() instanceof Damageable && !percentChance(unbreakingFactor)){
                Damageable meta = (Damageable) piece.getItemMeta();
                meta.setDamage(meta.getDamage() + toDeplete);
                if(meta.getDamage() >= piece.getType().getMaxDurability()){
                    player.getInventory().getArmorContents()[iterator].setAmount(0);
                }
                piece.setItemMeta(meta);
            }
            iterator ++;
        }
    }

    public static void depleteLife(Player player){
        if(!pdc.has(player, "lives")){
            pdc.set(player, "lives", "3");
        }
        int lives = Integer.parseInt(pdc.get(player, "lives"));
        lives--;
        if(lives == 0){
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(ChatColor.RED + "You used up your final life!");
            pdc.set(player, "lives", String.valueOf(lives));
            return;
        }else{
            pdc.set(player, "lives", String.valueOf(lives));
            player.sendMessage(ChatColor.GREEN + "" + lives + " lives remaining !");
        }
    }
}
