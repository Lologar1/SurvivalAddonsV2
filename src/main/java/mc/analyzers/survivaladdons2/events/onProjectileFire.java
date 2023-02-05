package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.ItemStackUtils;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import mc.analyzers.survivaladdons2.utility.PlayerUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;


import java.util.HashMap;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getAttributes;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.*;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.percentChance;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.sendActionBarMessage;

public class onProjectileFire implements Listener {
    @EventHandler
    public void onProjectileFire(ProjectileLaunchEvent e){
        if(e.getEntity().getShooter() instanceof HumanEntity && e.getEntity() instanceof AbstractArrow){
            Player player = (Player) e.getEntity().getShooter();
            AbstractArrow arrow = (AbstractArrow) e.getEntity();
            ItemStack bow = new ItemStack(Material.AIR);
            if(player.getInventory().getItemInMainHand().getType().equals(Material.BOW) || player.getInventory().getItemInMainHand().getType().equals(Material.CROSSBOW)){
                bow = player.getInventory().getItemInMainHand();
            }else if(player.getInventory().getItemInOffHand().getType().equals(Material.BOW) || player.getInventory().getItemInOffHand().getType().equals(Material.CROSSBOW)){
                bow = player.getInventory().getItemInOffHand();
            }
            ItemStack item = bow;
            if(PDCUtils.has(item, "enchantments")){
                if(ItemStackUtils.getCustomEnchantments(item).containsKey("shortbow") && item.getType().equals(Material.BOW)){
                    int shortbowCooldown = 0;
                    int cooldown = 4 - ItemStackUtils.getCustomEnchantments(item).get("shortbow");
                    if(PDCUtils.has(player, "shortbowCooldown")){
                        shortbowCooldown = Integer.parseInt(PDCUtils.get(player, "shortbowCooldown"));
                    }
                    if((System.currentTimeMillis()/1000 - shortbowCooldown) >= cooldown ){
                        boolean hasArrows = false;
                        for(ItemStack arrows : player.getInventory()){
                            if(arrows != null && (arrows.getType().equals(Material.ARROW) || arrows.getType().equals(Material.SPECTRAL_ARROW) || arrows.getType().equals(Material.TIPPED_ARROW))){
                                hasArrows = true;
                                if(!item.getEnchantments().containsKey(Enchantment.ARROW_INFINITE)){
                                    arrows.setAmount(arrows.getAmount() - 1);
                                    break;
                                }
                            }
                        }
                        if(!hasArrows){
                            return;
                        }
                        e.setCancelled(true);
                        PDCUtils.set(player, "shortbowCooldown", String.valueOf(System.currentTimeMillis()/1000));
                        Location loc = player.getEyeLocation();
                        Arrow launchedArrow = player.launchProjectile(Arrow.class);
                        launchedArrow.setVelocity(loc.getDirection().normalize().multiply(2.6));
                        if(!player.getGameMode().equals(GameMode.CREATIVE)){
                            double unbreakingFactor = 0;
                            if(item.getEnchantments().containsKey(Enchantment.DURABILITY)){
                                int unbreakingLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
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
                            if(!percentChance(unbreakingFactor)){
                                Damageable shortbowmeta = (org.bukkit.inventory.meta.Damageable) item.getItemMeta();
                                shortbowmeta.setDamage(shortbowmeta.getDamage() + 1);
                                item.setItemMeta(shortbowmeta);
                            }
                        }
                    }else{
                        String remaining = String.valueOf(cooldown - (System.currentTimeMillis()/1000 - Integer.parseInt(PDCUtils.get(player, "shortbowCooldown"))));
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Shortbow Cooldown! " + ChatColor.GRAY + "(" + remaining + "s)"));
                    }
                }
            }

            //Settingarrow attr values
            StringBuilder attributes = new StringBuilder();
            for(String attribute : getAttributes(bow)){
                attributes.append(attribute).append(" ");
            }
            PDCUtils.set(arrow, "attributeValues", attributes.toString());
            if(bow.getItemMeta().getEnchants().containsKey(Enchantment.ARROW_DAMAGE)){
                PDCUtils.set(arrow, "powerEnchantmentValue", String.valueOf(bow.getEnchantments().get(Enchantment.ARROW_DAMAGE)));
            }else{
                PDCUtils.set(arrow, "powerEnchantmentValue", "0");
            }

            if(PDCUtils.has(bow, "enchantments")){
                PDCUtils.set(arrow, "customEnchants", PDCUtils.get(bow, "enchantments"));
            }
            if(arrow instanceof Arrow){
                PotionData potionData = ((Arrow) arrow).getBasePotionData();
                if(potionData.getType().equals(PotionType.INSTANT_DAMAGE) && potionData.isUpgraded()){
                    PDCUtils.set(arrow, "harmingValue", "2");
                    ((Arrow) arrow).clearCustomEffects();
                    ((Arrow) arrow).setBasePotionData(new PotionData(PotionType.AWKWARD));
                }else if (potionData.getType().equals(PotionType.INSTANT_DAMAGE)){
                    PDCUtils.set(arrow, "harmingValue", "1");
                    ((Arrow) arrow).clearCustomEffects();
                    ((Arrow) arrow).setBasePotionData(new PotionData(PotionType.AWKWARD));
                }
            }



            HashMap<String, Integer> bowCustomEnchants = getCustomEnchantments(bow);
            StringBuilder arrowData = new StringBuilder();
            for(String customEnchant : bowCustomEnchants.keySet()){
                boolean canAdd = true;
                int level = bowCustomEnchants.get(customEnchant);
                int cooldown;
                switch (customEnchant){
                    case "explosive":
                        if(level == 1){
                            cooldown = 10;
                        }else if(level == 2){
                            cooldown = 5;
                        }else{
                            cooldown = 0;
                        }
                        if(!checkCooldown(bow, "explosiveCooldown")){
                            sendActionBarMessage(player, ChatColor.RED + "Explosive Cooldown! " + ChatColor.GRAY + "(" + getRemainingCooldown(bow, "explosiveCooldown")/1000 + "s)");
                            canAdd = false;
                        }else{
                            createCooldown(bow, "explosiveCooldown", cooldown*1000);
                        }
                        break;
                    case "teleport":
                        if(!player.isSneaking()){
                            continue;
                        }
                        if(level == 1){
                            cooldown = 15;
                        }else if(level == 2){
                            cooldown = 10;
                        }else if(level == 3){
                            cooldown = 7;
                        }else{
                            cooldown = 0;
                        }
                        if(!checkCooldown(bow, "teleportCooldown")){
                            sendActionBarMessage(player, ChatColor.RED + "Telebow Cooldown! " + ChatColor.GRAY + "(" + getRemainingCooldown(bow, "teleportCooldown")/1000 + "s)");
                            canAdd = false;
                        }else{
                            createCooldown(bow, "teleportCooldown", cooldown*1000);
                        }
                        break;
                }
                if(canAdd){
                    String enchant = customEnchant + "/" + level;
                    arrowData.append(enchant).append(" ");
                }
            }
            PDCUtils.set(arrow, "data", String.valueOf(arrowData));
        }
    }
}
