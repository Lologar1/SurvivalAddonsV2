package mc.analyzers.survivaladdons2.events;

import com.google.common.primitives.Ints;
import mc.analyzers.survivaladdons2.utility.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Double.max;
import static java.lang.Double.min;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.heartIcon;
import static mc.analyzers.survivaladdons2.quests.Quest.checkQuest;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.*;
import static mc.analyzers.survivaladdons2.utility.DamagePlayer.dealDamage;
import static mc.analyzers.survivaladdons2.utility.customEnchantments.lightningEnchantment;
import static mc.analyzers.survivaladdons2.utility.utility.*;
import static mc.analyzers.survivaladdons2.utility.utility.giveItem;

public class EntityDamageByEntityEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void EntityDamageByEntityEvent(org.bukkit.event.entity.EntityDamageByEntityEvent e){
        if(pdc.has(e.getEntity(), "damaged") && pdc.get(e.getEntity(), "damaged").equals("true")){
            pdc.set(e.getEntity(), "damaged", "false");
            return;
        }
        double finalDamage = e.getDamage();
        double finalMagicDamage = 0;
        if(e.getDamager() instanceof HumanEntity || e.getDamager() instanceof AbstractArrow){
            if(e.getDamager()instanceof HumanEntity){
                Player damager = (Player) e.getDamager();
                ItemStack weapon = damager.getInventory().getItemInMainHand();
                double critchance = 0;
                double critdamage = 0;
                double damage = 0;
                double magicdamage = 0;
                if(pdc.has(weapon, "attributes") && !(weapon.getType().equals(Material.BOW) || weapon.getType().equals(Material.CROSSBOW))){
                    String[] attributes = pdc.get(weapon, "attributes").split(" ");
                    for(String attribute : attributes){
                        switch (attribute.split("/")[0]){
                            case "critchance":
                                critchance = Double.parseDouble(attribute.split("/")[1]);
                                break;
                            case "critdamage":
                                critdamage = Double.parseDouble(attribute.split("/")[1]);
                                break;
                            case "damage":
                                damage = Double.parseDouble(attribute.split("/")[1]);
                                break;
                            case "magicdamage":
                                magicdamage = Double.parseDouble(attribute.split("/")[1]);
                                break;
                        }
                    }
                    finalMagicDamage = magicdamage;
                    finalDamage = damage;
                }else{
                    finalDamage=0.5;
                }
                double sharpnessFactor;
                LivingEntity damaged = (LivingEntity) e.getEntity();
                if(weapon.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)){
                    sharpnessFactor = 0.5 * max(0, weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL) - 1) + 0.5;
                }else{
                    sharpnessFactor = 0;
                }
                finalDamage += sharpnessFactor;

                if(damaged.getCategory().equals(EntityCategory.UNDEAD)){
                    finalDamage += weapon.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD) * 2.5;
                }
                if(damaged.getCategory().equals(EntityCategory.ARTHROPOD)){
                    finalDamage += weapon.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS) * 2.5;
                }
                float cd = damager.getAttackCooldown();
                if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) && weapon.getEnchantments().containsKey(Enchantment.SWEEPING_EDGE)){
                    switch (weapon.getEnchantmentLevel(Enchantment.SWEEPING_EDGE)){
                        case 1:
                            cd = 0.5f;
                            break;
                        case 2:
                            cd = 0.67f;
                            break;
                        case 3:
                            cd = 0.75f;
                            break;
                    }
                }
                finalDamage = finalDamage * cd;
                finalMagicDamage = finalMagicDamage * cd;
                //CustomEnchants
                double criticalAddon = 0;
                if(getCustomEnchantments(weapon).containsKey("critical")){
                    criticalAddon = ((double) getCustomEnchantments(weapon).get("critical"))/10;
                }
                boolean crit = percentChance(critchance/100 + criticalAddon);
                if(getCustomEnchantments(weapon).containsKey("critical")){
                    double level = getCustomEnchantments(weapon).get("critical");
                    criticalAddon = level/10;
                }
                if(damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
                    double addDamage = damager.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() * 2 + 2;
                    finalMagicDamage += addDamage;
                }
                if(damager.hasPotionEffect(PotionEffectType.WEAKNESS)){
                    double addDamage = damager.getPotionEffect(PotionEffectType.WEAKNESS).getAmplifier() * 2 + 2;
                    finalMagicDamage -= addDamage;
                }
                if(crit){
                    finalDamage += finalDamage * critdamage/100;
                    finalMagicDamage += finalMagicDamage * critdamage/100;
                    damaged.getWorld().spawnParticle(Particle.BLOCK_CRACK, damaged.getEyeLocation(), 50, Material.REDSTONE_BLOCK.createBlockData());
                }
                if(pdc.has(weapon, "enchantments") && pdc.has(weapon, "attributes")){
                    //Custom Enchantsss!!!!
                    if(getCustomEnchantments(weapon).containsKey(lightningEnchantment.getId())){
                        if(getCustomEnchantments(weapon).containsKey("venom")){
                            int level = getCustomEnchantments(weapon).get("venom");
                            if(percentChance(level * 0.25) && crit){
                                damager.playSound(damager.getLocation(), Sound.ENTITY_SPIDER_HURT, 1, 1);
                                damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60*level, 0));
                                damaged.sendMessage(net.md_5.bungee.api.ChatColor.of("#67D80E") + "" + net.md_5.bungee.api.ChatColor.BOLD + "VENOMED! " + net.md_5.bungee.api.ChatColor.RESET + net.md_5.bungee.api.ChatColor.GRAY + "for " + level*3 + " seconds.");
                            }
                        }
                        if(getCustomEnchantments(weapon).containsKey("lightning")){
                            if(!pdc.has(weapon, "lightningCounter")){
                                pdc.set(weapon, "lightningCounter", "0");
                            }
                            int counter = Integer.parseInt(pdc.get(weapon, "lightningCounter"));
                            int level = getCustomEnchantments(weapon).get("lightning");
                            int required = 7 - level;
                            int dmg = level;
                            int dustCost = 0;
                            switch (level){
                                case 1:
                                    dmg = 4;
                                    dustCost = 3;
                                    break;
                                case 2:
                                    dmg = 5;
                                    dustCost = 3;
                                    break;
                                case 3:
                                    dmg = 6;
                                    dustCost = 3;
                                    break;
                                case 4:
                                    dmg = 7;
                                    dustCost = 2;
                                    break;
                                case 5:
                                    dmg = 10;
                                    dustCost = 2;
                                    break;
                            }
                            if(counter >= required){
                                //PERUN!
                                if(Integer.parseInt(pdc.get(damager, "dust")) >= dustCost){
                                    double billModif = 0;
                                    if(getCustomEnchantments(weapon).containsKey("billionaire")){
                                        billModif = ((double) getCustomEnchantments(weapon).get("billionaire")) * ((double) 1/3);
                                    }
                                    damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW +""+ ChatColor.BOLD + "ZAP! " +ChatColor.RESET + ChatColor.GRAY + "Struck for " +
                                            ChatColor.RED + (dmg/2 * (billModif + 1)) + heartIcon + ChatColor.GRAY + ", cost " + ChatColor.RED + dustCost + " " + dustIcon + " dust."));
                                    pdc.set(weapon, "lightningCounter", "0");
                                    pdc.set(damager, "dust", String.valueOf(Integer.parseInt(pdc.get(damager, "dust")) - dustCost));
                                    finalMagicDamage += dmg;
                                    damaged.getWorld().strikeLightningEffect(damaged.getLocation());
                                }else {
                                    damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Not enough dust! " + ChatColor.GRAY + "Lightning failed"));
                                }
                            }else{
                                counter++;
                                pdc.set(weapon, "lightningCounter", String.valueOf(counter));
                            }
                        }else{
                            pdc.set(weapon, "lightningCounter", "1");
                        }
                    }
                    if(getCustomEnchantments(weapon).containsKey("venom")){
                        int level = getCustomEnchantments(weapon).get("venom");
                        if(percentChance(level*0.25) && crit){
                            damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, level-1));
                            damaged.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GREEN + ""+ net.md_5.bungee.api.ChatColor.BOLD + "POISONED! " + net.md_5.bungee.api.ChatColor.RESET + net.md_5.bungee.api.ChatColor.GRAY + "by " + damager.getDisplayName());
                            damager.playSound(damager.getLocation(), Sound.ENTITY_SPIDER_HURT, 1, 1);
                        }
                    }
                    if(getCustomEnchantments(weapon).containsKey("netheritestomp") && damaged instanceof Player){
                        int level = getCustomEnchantments(weapon).get("netheritestomp");
                        double bonus = 0;
                        for(ItemStack piece : ((Player) damaged).getInventory().getArmorContents()){
                            if(piece != null && (piece.getType().equals(Material.NETHERITE_BOOTS) || piece.getType().equals(Material.NETHERITE_CHESTPLATE) ||
                                    piece.getType().equals(Material.NETHERITE_LEGGINGS) || piece.getType().equals(Material.NETHERITE_HELMET))){
                                bonus = level * 0.04;
                            }
                        }
                        finalDamage += finalDamage * bonus;
                    }
                    if(getCustomEnchantments(weapon).containsKey("gamble")){
                        int level = getCustomEnchantments(weapon).get("gamble");
                        switch (level){
                            case 1:
                                finalMagicDamage += finalDamage*0.5;
                                finalDamage = finalDamage*0.5;
                                break;
                            case 2:
                                finalMagicDamage += finalDamage*0.75;
                                finalDamage = finalDamage*0.25;
                                break;
                            case 3:
                                finalMagicDamage += finalDamage;
                                finalDamage = 0;
                        }
                        if(percentChance(0.5)){
                            dealDamage(damager, finalDamage, finalMagicDamage, EntityDamageEvent.DamageCause.ENTITY_ATTACK);
                            damager.playSound(damager.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(net.md_5.bungee.api.ChatColor.RED + "" + net.md_5.bungee.api.ChatColor.BOLD + "GAMBLE! " + net.md_5.bungee.api.ChatColor.RESET + net.md_5.bungee.api.ChatColor.GRAY + "Unlucky!"));
                            return;
                        }else{
                            damager.playSound(damager.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                            damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(net.md_5.bungee.api.ChatColor.GREEN + "" + net.md_5.bungee.api.ChatColor.BOLD + "GAMBLE! " + net.md_5.bungee.api.ChatColor.RESET + net.md_5.bungee.api.ChatColor.GRAY + "Lucky!"));
                        }
                    }
                    if(getCustomEnchantments(weapon).containsKey("billionaire")){
                        if(damaged instanceof Player && hasEffect((Player) damaged, "golden", true)){
                            damaged.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "GOLDEN! " + ChatColor.RESET + ChatColor.GRAY + "Cancelled " + damager.getDisplayName() + "'s " + ChatColor.GOLD + "billionaire!");
                        }else{
                            int level = getCustomEnchantments(weapon).get("billionaire");
                            if((Integer.parseInt(pdc.get(damager, "dust")) >= level*3)){
                                pdc.set(damager, "dust", String.valueOf(Integer.parseInt(pdc.get(damager, "dust")) - level*3));
                                damager.playSound(damager.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
                                finalDamage = finalDamage * (1 + Math.round(0.33 * level));
                                finalMagicDamage = finalMagicDamage * (1 + Math.round(0.33 * level));
                            }
                        }
                    }
                    if(getCustomEnchantments(weapon).containsKey("lifesteal")){
                        int level = getCustomEnchantments(weapon).get("lifesteal");
                        damager.playSound(damager.getLocation(), Sound.ENTITY_PARROT_EAT, 2, 2);
                        damager.setHealth(min(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), damager.getHealth() + 0.05*level*(finalDamage + finalMagicDamage)));
                        finalDamage = finalDamage/2;
                        //finalMagicDamage = finalMagicDamage/2;
                    }
                }
                //End custom enchants
                //Magic dmg (if need be)

                //End magic dmg
                //Quest
                if(e.getDamager() instanceof Player){
                    checkQuest("deal", "damage", (Player) e.getDamager(), (int) (finalDamage + finalMagicDamage));
                }
            }else if(e.getDamager() instanceof AbstractArrow && ((AbstractArrow) e.getDamager()).getShooter() instanceof Player){

                AbstractArrow arrow = (AbstractArrow) e.getDamager();
                Vector velocity = arrow.getVelocity();

                double critchance = 0;
                double critdamage = 0;
                double damage = 0;
                double magicdamage = 0;

                int powerLevel = Integer.parseInt(pdc.get(arrow, "powerEnchantmentValue"));

                String[] attributes = pdc.get(arrow, "attributeValues").split(" ");
                for(String attribute : attributes){
                    switch (attribute.split("/")[0]){
                        case "critchance":
                            critchance = Double.parseDouble(attribute.split("/")[1]);
                            break;
                        case "critdamage":
                            critdamage = Double.parseDouble(attribute.split("/")[1]);
                            break;
                        case "damage":
                            damage = Double.parseDouble(attribute.split("/")[1]);
                            break;
                        case "magicdamage":
                            magicdamage = Double.parseDouble(attribute.split("/")[1]);
                            break;
                    }
                }
                finalMagicDamage = magicdamage;

                if(pdc.has(arrow, "harmingValue")){
                    int potency = Integer.parseInt(pdc.get(arrow, "harmingValue"));
                    finalMagicDamage += 2 * potency;
                }
                double pita1 = Math.sqrt(Math.pow(velocity.getZ(), 2) + Math.pow(velocity.getY(), 2));
                double speed = (Math.sqrt(Math.pow(pita1, 2) + Math.pow(velocity.getX(), 2)))/3;
                finalDamage = (speed * damage);
                finalDamage = finalDamage + finalDamage * powerLevel * 0.15; //Adding power level
                //Enchants (Custom) (to do in future bow enchants, I guess :))
                Player player = ((Player) ((AbstractArrow) e.getDamager()).getShooter());

                double criticalAddon = 0;
                float para = 0;
                if(pdc.has(arrow, "data")) {
                    String rawData = pdc.get(arrow, "data");
                    String[] modifiers = rawData.split(" ");
                    for (String modifier : modifiers) {
                        if (modifier.equals("") || modifier.equals(" ")) {
                            continue;
                        }
                        String type = modifier.split("/")[0];
                        int power = Integer.parseInt(modifier.split("/")[1]);
                        switch (type) {
                            case "sorcery":
                                if (percentChance(power * 0.06)) {
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_PURPLE + "SORCERY ! " + net.md_5.bungee.api.ChatColor.GRAY + "Struck for 100% " + net.md_5.bungee.api.ChatColor.AQUA + "Magic Damage"));
                                    finalMagicDamage = finalDamage + finalMagicDamage;
                                    finalDamage = 0;
                                    player.playSound(((Player) ((AbstractArrow) e.getDamager()).getShooter()).getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 2);
                                }
                                break;
                            case "glasscannon":
                                boolean armor = false;
                                for (ItemStack piece : player.getInventory().getArmorContents()) {
                                    if (!(piece == null)) {
                                        armor = true;
                                        break;
                                    }
                                }
                                if (!armor) {
                                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 2 , 1);
                                    finalDamage = finalDamage * 2;
                                    finalMagicDamage = finalMagicDamage * 2;
                                }
                                break;
                            case "critical":
                                criticalAddon = ((double) power)/10;
                                break;
                            case "parasite":
                                para = power*0.5f;
                            break;
                        }
                    }
                }

                //Magic damage (if need !)
                //
                //
                //

                //Critical hits
                boolean crit = percentChance(critchance/100 + criticalAddon);
                if(crit){
                    finalDamage += finalDamage * critdamage/100;
                    finalMagicDamage += finalMagicDamage * critdamage/100;
                    arrow.setCritical(true);
                    arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_PARROT_EAT, 4, 1);
                    player.setHealth(min(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), player.getHealth()+para));
                    arrow.getWorld().spawnParticle(Particle.BLOCK_CRACK, arrow.getLocation(), 50, Material.REDSTONE_BLOCK.createBlockData());
                }
                //Quest
                checkQuest("deal", "damage", (Player) ((AbstractArrow) e.getDamager()).getShooter(), (int) (finalDamage + finalMagicDamage));
                checkQuest("land", "shot", (Player) ((AbstractArrow) e.getDamager()).getShooter(), 1);
            }
        }
        //Damage Calculations !
        //PvP



        //Buff mobs
        Entity damager = e.getDamager();
        if(damager instanceof Monster || damager instanceof EnderDragon
                || damager instanceof Ghast || damager instanceof Hoglin
                || damager instanceof Phantom || damager instanceof Shulker
                || damager instanceof Slime){
            finalDamage = finalDamage * 1.9;
        }
        //PVP
        if(e.getEntity() instanceof Player){
            if(damager instanceof Player){
                System.out.println(((Player) damager).getDisplayName() + " dealt " + (finalDamage + finalMagicDamage) + " damage");
                if(e.getEntity() instanceof Player){
                    pdc.set(e.getDamager(), "inCombat", "true/15");
                    pdc.set(e.getEntity(), "inCombat", "true/15");
                }
            }
            Player player = (Player) e.getEntity();
            e.setDamage(0);
            dealDamage(player, finalDamage, finalMagicDamage, e.getCause());

        }else if (e.getEntity() instanceof LivingEntity){
            //PVE
            e.setDamage(0);
            if(e.getDamager() instanceof AbstractArrow){
                damager = (Entity) ((AbstractArrow) e.getDamager()).getShooter();
            }else{
                damager = e.getDamager();
            }
            double totalFinalDamage = finalDamage + finalMagicDamage;
            e.setDamage(totalFinalDamage);
            //((LivingEntity) e.getEntity()).damage(totalFinalDamage, damager);
            //DamageEntity.dealDamage((LivingEntity) e.getEntity(), finalDamage, finalMagicDamage, damager);
            LivingEntity entity = (LivingEntity) e.getEntity();
            boolean died = false;
            if(max(entity.getHealth() - totalFinalDamage, 0) == 0){
                died = true;
            }
            if(damager instanceof LivingEntity){
                if(!(damager instanceof Player)){
                    return;
                }
                Player killer = (Player) damager;
                if(!died){
                    return;
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
                        giveItem(killer, itemList.item("ender_core"), 1);
                        killer.sendMessage(ChatColor.LIGHT_PURPLE + "RARE DROP! " + ChatColor.GRAY + "Ender Core");
                    }
                }else if(entity.getType().equals(EntityType.SKELETON)){
                    boolean dropCore = percentChance(0.001 + 0.001 * lootingFactor/2);
                    if(dropCore){
                        giveItem(killer, itemList.item("shortbow_core"), 1);
                        killer.sendMessage(ChatColor.LIGHT_PURPLE + "RARE DROP! " + ChatColor.GRAY + "Shortbow Core");
                    }
                }else if(entity.getType().equals(EntityType.WARDEN)){
                    giveItem(killer, itemList.item("warden_heart"), 1);
                    killer.sendMessage(ChatColor.LIGHT_PURPLE + "DROP! " + ChatColor.GRAY + "Warden's Heart");
                }else if(entity.getType().equals(EntityType.WITHER)){
                    boolean dropStar = percentChance(0.3 + 0.03 * lootingFactor/2);
                    if(dropStar){
                        giveItem(killer, itemList.item("fallen_star"), 1);
                        killer.sendMessage(ChatColor.LIGHT_PURPLE + "RARE DROP! " + ChatColor.GRAY + "Fallen Star");
                    }
                }
            }
        }
    }
}
