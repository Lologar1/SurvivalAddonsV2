package mc.analyzers.survivaladdons2.utility;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
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

import java.util.Objects;

import static com.google.common.primitives.Doubles.min;
import static java.lang.Double.max;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.heartIcon;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getProtectionFactors;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getVanillaEnchantmentProtectionFactors;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.percentChance;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.preciseRound;

public class PlayerUtils {
    public static void killPlayer(Player player, EntityDamageEvent.DamageCause cause){
        boolean save = false;
        player.sendMessage(org.bukkit.ChatColor.RED + "You Died!");
        ItemMeta offhand = player.getInventory().getItemInOffHand().getItemMeta();
        for(int i = 0; i<9; i++){
            ItemStack item = player.getInventory().getItem(i);
            if((item != null && Objects.equals(PDCUtils.get(item, "id"), "funky_feather")) || (offhand != null && Objects.equals(PDCUtils.get(player.getInventory().getItemInOffHand(), "id"), "funky_feather"))){
                if(offhand != null && Objects.equals(PDCUtils.get(player.getInventory().getItemInOffHand(), "id"), "funky_feather")){
                    item = player.getInventory().getItemInOffHand();
                }
                save = true;
                item.setAmount(item.getAmount() - 1);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2000 ,0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200 ,3));
                player.sendMessage(org.bukkit.ChatColor.DARK_AQUA + "" + org.bukkit.ChatColor.BOLD + "FUNKY FEATHER!" + org.bukkit.ChatColor.RESET + org.bukkit.ChatColor.GRAY + " Inventory saved.");
                player.setHealth(5);
                player.setAbsorptionAmount(10);
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 2);
                break;
            }
        }

        if(!save){
            //Implement lives system
            //player.sendMessage(ChatColor.GRAY + "Inventory cleared from death.");
            //depleteLife(player); //Hardcore
            Bukkit.broadcastMessage(net.md_5.bungee.api.ChatColor.WHITE + player.getDisplayName() + " died because of " + cause);
            PDCUtils.set(player, "effects", "");
            player.sendMessage(org.bukkit.ChatColor.YELLOW + "Death location : " + player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ());
            for(ItemStack todrop : player.getInventory()){
                if(todrop != null){
                    player.getWorld().dropItemNaturally(player.getLocation(), todrop);
                }
            }
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.getInventory().clear();
            player.getActivePotionEffects().clear();

            if(player.getBedSpawnLocation() != null){
                player.teleport(player.getBedSpawnLocation());
            }else{
                player.teleport(SurvivalAddons2.getPlugin().getServer().getWorld("world").getSpawnLocation());
            }
        }
    }
    public static void setEffect(Player player, String addeffect, int potency, int duration){
        if(PDCUtils.get(player, "effects").contains(addeffect)){
            StringBuilder newEffects = new StringBuilder();
            for(String effect : PDCUtils.get(player, "effects").split(" ")){
                if(!(effect.contains(addeffect))){
                    newEffects.append(effect).append(" ");
                }else {
                    newEffects.append(addeffect).append("/").append(potency).append("/").append(System.currentTimeMillis()).append("/").append(duration).append(" ");
                }
            }
            PDCUtils.set(player, "effects", newEffects.toString());
        }else {
            PDCUtils.set(player, "effects", PDCUtils.get(player, "effects") + addeffect + "/" + potency + "/" + System.currentTimeMillis() + "/" + duration + " ");
        }
    }

    public static boolean hasEffect(Player player, String effect, boolean show){
        boolean has = false;

        if(PDCUtils.get(player, "effects").contains(effect)){
            long time = 0;
            int duration = 0;
            StringBuilder newEffects = new StringBuilder();
            for(String effect1 : PDCUtils.get(player, "effects").split(" ")){
                if(!(effect1.contains(effect))){
                    newEffects.append(effect1).append(" ");
                    continue;
                }
                time = Long.parseLong(effect1.split("/")[2]);
                duration = Integer.parseInt(effect1.split("/")[3]);
            }
            if((time + duration * 1000L) >= System.currentTimeMillis()){
                has = true;
                if(show){
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(org.bukkit.ChatColor.DARK_PURPLE + effect.substring(0, 1).toUpperCase() + effect.substring(1) + org.bukkit.ChatColor.WHITE + " : " + ((time + duration*1000L) - System.currentTimeMillis())/1000 + "s left"));
                }
            }else{
                PDCUtils.set(player, "effects", newEffects.toString());
            }
        }
        return has;
    }
    public static void giveItem(Player player, ItemStack item, int amount){
        item.setAmount(amount);
        if(player.getInventory().firstEmpty() == -1){
            player.getWorld().dropItem(player.getLocation(), item);
        }else{
            player.getInventory().addItem(item);
        }
    }

    public static void dealDamage(Player player, double damage, double magicdamage, EntityDamageEvent.DamageCause cause){
        if(PDCUtils.get(player, "effects").contains("singularity")){
            if(hasEffect(player, "singularity", true)){
                double truedamage = Math.min(damage + magicdamage, 4.0);
                boolean success = depleteTotalPlayerHealth(player, truedamage);
                System.out.println("Damage taken by " + player.getDisplayName() + " : " + truedamage);
                damageArmor(truedamage, player);
                EntityUtils.displayDamage(player, 0, truedamage);
                if(success){
                    return;
                }
                killPlayer(player, cause);
            }
        }
        //System.out.println("MAGICdmg " + (magicdamage * getProtectionFactors(player)[1]));
        //System.out.println("DMGdmg " + (damage * getProtectionFactors(player)[0]));
        double totalFinalDamage = (damage * getProtectionFactors(player)[0]) + (magicdamage * getProtectionFactors(player)[1]);
        double displayDamage = damage * getProtectionFactors(player)[0];
        double displayMagicDamage = magicdamage * getProtectionFactors(player)[1];
        switch (cause){
            case PROJECTILE:
                totalFinalDamage = totalFinalDamage - totalFinalDamage * getVanillaEnchantmentProtectionFactors(player)[1]/100;
                break;
            case ENTITY_EXPLOSION:
                totalFinalDamage = totalFinalDamage - totalFinalDamage * getVanillaEnchantmentProtectionFactors(player)[2]/100;
                break;
        }
        totalFinalDamage = totalFinalDamage - totalFinalDamage * getVanillaEnchantmentProtectionFactors(player)[0]/100;
        displayDamage = displayDamage - displayDamage * getVanillaEnchantmentProtectionFactors(player)[0]/100;
        displayMagicDamage = displayMagicDamage - displayMagicDamage * getVanillaEnchantmentProtectionFactors(player)[0]/100;
        if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
            totalFinalDamage = totalFinalDamage - totalFinalDamage * (0.15 * (player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier()+1));
            displayDamage = displayDamage - displayDamage * (0.15 * (player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier()+1));
            displayMagicDamage = displayMagicDamage - displayMagicDamage * (0.15 * (player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier()+1));
        }
        if(totalFinalDamage < 0){
            totalFinalDamage = 0;
            displayDamage = 0;
            displayMagicDamage = 0;
        }
        if(ItemStackUtils.getCustomEnchantments(player.getInventory().getLeggings()).containsKey("dodge")){
            int potency = ItemStackUtils.getCustomEnchantments(player.getInventory().getLeggings()).get("dodge");
            if(percentChance(0.04 * potency)){
                displayDamage = 0;
                displayMagicDamage = 0;
                totalFinalDamage = 0.0;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "" + ChatColor.BOLD + "HIT DODGED!"));
                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getEyeLocation(), 1, 0, 0 ,0, 0.25);
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 2, 2);
            }
        }
        EntityUtils.displayDamage(player, displayDamage, displayMagicDamage);
        double abs = player.getAbsorptionAmount();
        player.setAbsorptionAmount(max(0, player.getAbsorptionAmount() - totalFinalDamage));
        if(Math.min(player.getHealth(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) - (totalFinalDamage - abs) > 0){
            player.setHealth(min(Math.min(player.getHealth(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) - max((totalFinalDamage - abs), 0), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        }else{
            killPlayer(player, cause);
        }
        if(ItemStackUtils.getCustomEnchantments(player.getInventory().getChestplate()).containsKey("reflect")){
            int potency = ItemStackUtils.getCustomEnchantments(player.getInventory().getChestplate()).get("reflect");
            if(player.getAbsorptionAmount()<=0 && player.getHealth() - totalFinalDamage*0.25*potency > 0){
                sendActionBarMessage(player, ChatColor.GREEN + "" + ChatColor.BOLD + "REFLECT! " + ChatColor.RESET + ChatColor.GRAY + preciseRound((totalFinalDamage*0.25*potency)/2, 1) + ChatColor.RED + " " + heartIcon);
                player.setHealth(player.getHealth() - totalFinalDamage*0.25*potency);
                player.setAbsorptionAmount(totalFinalDamage*0.25*potency);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 3, 1);
            }
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

    public static void subtractDust(Player player, int amount){
        PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - amount));
    }

    public static void addDust(Player player, int amount){
        PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) + amount));
    }

    public static boolean hasDust(Player player, int amount){
        boolean has = false;
        if(Integer.parseInt(PDCUtils.get(player, "dust")) >= amount){
            has = true;
        }
        return has;
    }

    public static boolean inCombat(Player player){
        boolean combat = false;
        if(PDCUtils.get(player, "inCombat").split("/")[0].equals("true")){
            combat = true;
        }
        return combat;
    }

    public static void sendActionBarMessage(Player player, String message){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message  ));
    }

    public static void damageArmor(double totalFinalDamage, Player player){
        int toDeplete = (int) max((totalFinalDamage/4), 1);
        for(ItemStack piece : player.getInventory().getArmorContents()){
            if(piece == null){
                continue;
            }
            if(piece.getType().equals(Material.ELYTRA) || piece.getType().equals(Material.PLAYER_HEAD)){
                continue;
            }
            double unbreakingFactor = 0;
            try{
                if(piece.getEnchantments().containsKey(Enchantment.DURABILITY)){
                    int unbreakingLevel = piece.getEnchantmentLevel(Enchantment.DURABILITY);
                    switch (unbreakingLevel){
                        case 1:
                            unbreakingFactor = 0.25;
                            break;
                        case 2:
                            unbreakingFactor = 0.3;
                            break;
                        case 3:
                            unbreakingFactor = 0.37;
                            break;
                    }
                }
            }catch (Exception ignored){}
            if(piece.getItemMeta() instanceof Damageable && !percentChance(unbreakingFactor)){
                Damageable meta = (Damageable) piece.getItemMeta();
                meta.setDamage(meta.getDamage() + toDeplete);
                if(meta.getDamage() >= piece.getType().getMaxDurability()){
                    piece.setAmount(0);
                }
                piece.setItemMeta(meta);
            }
        }
    }

    public static void depleteLife(Player player){
        if(!PDCUtils.has(player, "lives")){
            PDCUtils.set(player, "lives", "3");
        }
        int lives = Integer.parseInt(PDCUtils.get(player, "lives"));
        lives--;
        if(lives == 0){
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(ChatColor.RED + "You used up your final life!");
            PDCUtils.set(player, "lives", String.valueOf(lives));
            return;
        }else{
            PDCUtils.set(player, "lives", String.valueOf(lives));
            player.sendMessage(ChatColor.GREEN + "" + lives + " lives remaining !");
        }
    }
}
