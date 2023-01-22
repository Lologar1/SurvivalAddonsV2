package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.shop.ShopItems;
import mc.analyzers.survivaladdons2.utility.ItemStackUtils;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import mc.analyzers.survivaladdons2.utility.PlayerUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static java.lang.Math.min;
import static java.lang.Math.nextAfter;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.customenchantments.customEnchantmentsWrapper.*;
import static mc.analyzers.survivaladdons2.customenchantments.customEnchantmentsWrapper.legendaryEnchantments;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.*;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.*;

public class PlayerInteractEvent implements Listener {
    @EventHandler
    public void playerInteractEvent(org.bukkit.event.player.PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(e.getItem() != null && PDCUtils.has(e.getItem(), "doNotInteract")){
            e.setCancelled(true);
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) && player.getInventory().getItemInMainHand().getType().equals(Material.KNOWLEDGE_BOOK) && PDCUtils.has(player.getInventory().getItemInMainHand(), "id")){
            if(!PDCUtils.get(player.getInventory().getItemInMainHand(), "id").equals("tomeOfKnowledge")){
                return;
            }
            e.setCancelled(true);
            Inventory tome = Bukkit.createInventory(player, 27, ChatColor.LIGHT_PURPLE + "Tome of Knowledge");

            ItemStack anvilGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta anvilGlassMeta = anvilGlass.getItemMeta();
            anvilGlassMeta.setDisplayName(ChatColor.BLACK + "");
            anvilGlass.setItemMeta(anvilGlassMeta);
            for(int i = 0; i < 27; i++){
                tome.setItem(i, anvilGlass);
            }

            ItemStack dustCost = new ItemStack(Material.REDSTONE);
            ItemMeta dustMeta = dustCost.getItemMeta();
            int cost = 25;
            String enchantment = "AnALYZERS IS THE BESTTT";
            if(PDCUtils.has(tome.getItem(22), "enchantments")){
                enchantment = PDCUtils.get(tome.getItem(22), "enchantments").split("/")[0];
            }

            if (Arrays.asList(uncommonEnchantments).contains(enchantment)){
                cost = 40;
            }else if (Arrays.asList(rareEnchantments).contains(enchantment)){
                cost = 65;
            }else if (Arrays.asList(epicEnchantments).contains(enchantment)){
                cost = 85;
            }else if (Arrays.asList(legendaryEnchantments).contains(enchantment)){
                cost = 115;
            }
            dustMeta.setDisplayName(ChatColor.GOLD + "Dust cost : " + ChatColor.RED + cost + " " + dustIcon + " dust.");
            dustCost.setItemMeta(dustMeta);

            ItemStack reroll = new ItemStack(Material.KNOWLEDGE_BOOK);
            ItemMeta rerollMeta = reroll.getItemMeta();
            rerollMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#1ED3B2") + "Reroll Enchantment");
            ArrayList<String> rerollLore = new ArrayList<>();
            rerollLore.add(ChatColor.GRAY + "Right-click to reroll the");
            rerollLore.add(ChatColor.GRAY + "enchantment for " + ChatColor.RED + "2 " + dustIcon + " dust.");
            rerollMeta.setLore(rerollLore);
            reroll.setItemMeta(rerollMeta);

            ItemStack oldEnchant = new ItemStack(Material.ENCHANTED_BOOK);
            if(PDCUtils.has(player, "oldTomeEnchantment") && !PDCUtils.get(player, "oldTomeEnchantment").equals("none")){
                PDCUtils.set(oldEnchant, "enchantments", PDCUtils.get(player, "oldTomeEnchantment") + "/1 ");
                ItemStackUtils.syncItem(oldEnchant);
            }else{
                ItemStack combinedItem = new ItemStack(Material.BARRIER);
                ItemMeta cm = combinedItem.getItemMeta();
                cm.setDisplayName(ChatColor.RED + "Reroll to get a custom enchantment!");
                combinedItem.setItemMeta(cm);
                oldEnchant = combinedItem;
            }

            tome.setItem(4, dustCost);
            tome.setItem(11, new ItemStack(Material.AIR));
            tome.setItem(15, reroll);
            tome.setItem(22, oldEnchant); //Old Enchant
            player.openInventory(tome);
            return;
        }

        if(PDCUtils.has(item, "enchantments")){
            if(ItemStackUtils.getCustomEnchantments(item).containsKey("shortbow") && item.getType().equals(Material.BOW)){
                e.setCancelled(true);
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
                    PDCUtils.set(player, "shortbowCooldown", String.valueOf(System.currentTimeMillis()/1000));
                    Location loc = player.getEyeLocation();
                    Arrow arrow = player.launchProjectile(Arrow.class);
                    arrow.setVelocity(loc.getDirection().normalize().multiply(2.5));
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

        if(PDCUtils.has(item, "id")){
            switch (PDCUtils.get(item, "id")){
                case "healing_stick":
                    int potency = Integer.parseInt(PDCUtils.get(item, "healing"));
                    if(Integer.parseInt(PDCUtils.get(player, "dust")) >= (potency+1)){
                        int tpCooldown = 0;
                        if(PDCUtils.has(player, "stickCooldown")){
                            tpCooldown = Integer.parseInt(PDCUtils.get(player, "stickCooldown"));
                        }
                        if((System.currentTimeMillis()/1000 - tpCooldown) >= 5){
                            PDCUtils.set(player, "stickCooldown", String.valueOf(System.currentTimeMillis()/1000));
                            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "HEAL! " + ChatColor.RESET + ChatColor.GRAY + "for " + potency + " hearts.");
                            player.setHealth(min(player.getHealth() + 2*potency, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
                            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 2);
                            PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - (potency+1)));
                        }else{
                            String remaining = String.valueOf(5 - (System.currentTimeMillis()/1000 - Integer.parseInt(PDCUtils.get(player, "stickCooldown"))));
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Stick Cooldown! " + ChatColor.GRAY + "(" + remaining + "s)"));
                        }
                    }
                    break;
                case "food_stick":
                    potency = Integer.parseInt(PDCUtils.get(item, "food"));
                    if(Integer.parseInt(PDCUtils.get(player, "dust")) >= potency){
                        int tpCooldown = 0;
                        if(PDCUtils.has(player, "stickCooldown")){
                            tpCooldown = Integer.parseInt(PDCUtils.get(player, "stickCooldown"));
                        }
                        if((System.currentTimeMillis()/1000 - tpCooldown) >= 5){
                            PDCUtils.set(player, "stickCooldown", String.valueOf(System.currentTimeMillis()/1000));
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "EAT! " + ChatColor.RESET + ChatColor.GRAY + "for " + potency*2 + " satruation.");
                            player.setFoodLevel(min(player.getFoodLevel() + potency*2, 20));
                            player.setSaturation(2);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
                            PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - potency));
                        }else{
                            String remaining = String.valueOf(5 - (System.currentTimeMillis()/1000 - Integer.parseInt(PDCUtils.get(player, "stickCooldown"))));
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Stick Cooldown! " + ChatColor.GRAY + "(" + remaining + "s)"));
                        }
                    }
                    break;
                case "fling_stick":
                    potency = Integer.parseInt(PDCUtils.get(item, "fling"));
                    if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                        Vector velo = player.getVelocity();
                        velo.setY(velo.getY() + 2);
                        player.setVelocity(velo);
                    }else if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                        player.setVelocity(player.getLocation().getDirection().multiply(potency));
                    }
                    break;
                case "ice_wand":
                    int tpCooldown = 0;
                    if(PDCUtils.has(player, "wandCooldown")){
                        tpCooldown = Integer.parseInt(PDCUtils.get(player, "wandCooldown"));
                    }
                    if((System.currentTimeMillis()/1000 - tpCooldown) >= 3){
                        if(Integer.parseInt(PDCUtils.get(player, "dust")) >= 4){
                            PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - 4));
                        }else {
                            return;
                        }
                        PDCUtils.set(player, "wandCooldown", String.valueOf(System.currentTimeMillis()/1000));
                        player.sendMessage(net.md_5.bungee.api.ChatColor.of("#68FBFB") + "" + ChatColor.BOLD + "MAGIC! " + ChatColor.RESET + ChatColor.GRAY + "cost " + net.md_5.bungee.api.ChatColor.RED + " 4 " + dustIcon + " dust.");
                        //Code for ice spray wand
                        player.getWorld().spawnParticle(Particle.CLOUD, player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(2.5)), 40, 0, 0, 0, 0.5);
                        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
                        Collection<Entity> affected = player.getWorld().getNearbyEntities(player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(2.5)), 4, 4, 4);
                        for(Entity toDamage : affected){
                            if(!toDamage.getType().isAlive() || toDamage.equals(player)){
                                continue;
                            }
                            ((LivingEntity) toDamage).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 0));
                            if(toDamage instanceof Player){
                                //Magic Damage
                                toDamage.sendMessage(net.md_5.bungee.api.ChatColor.of("#5F602D") + "" + ChatColor.BOLD + "SLOW!" + ChatColor.RESET + ChatColor.GRAY + " For 10 seconds because of " + ChatColor.BLUE + player.getDisplayName());
                                PlayerUtils.dealDamage((Player) toDamage, 0, 11, EntityDamageEvent.DamageCause.MAGIC);
                            }else {
                                //Normal damage
                                ((LivingEntity) toDamage).damage(11, player);
                            }
                        }
                    }else{
                        String remaining = String.valueOf(3 - (System.currentTimeMillis()/1000 - Integer.parseInt(PDCUtils.get(player, "wandCooldown"))));
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Wand Cooldown! " + ChatColor.GRAY + "(" + remaining + "s)"));
                    }
                    break;
                case "repair_kit":
                    for(ItemStack thingy : player.getInventory()){
                        if(thingy == null){
                            continue;
                        }
                        if(thingy.getItemMeta() instanceof Damageable){
                            Damageable meta = (Damageable) thingy.getItemMeta();
                            if(meta == null){
                                continue;
                            }
                            meta.setDamage(0);
                            thingy.setItemMeta(meta);
                        }
                    }
                    item.setAmount(item.getAmount() - 1);
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, 1);
                    player.sendMessage(net.md_5.bungee.api.ChatColor.WHITE + "" + net.md_5.bungee.api.ChatColor.BOLD + "ITEMS REPAIRED!");
                    break;
                case "insta_melon":
                    if((player.getSaturation()+1.2) <= 20 || (player.getFoodLevel() + 2) <= 20){
                        e.setCancelled(true);
                        player.setSaturation((float) min((player.getSaturation() + 1.2), 20));
                        player.setFoodLevel(min(player.getFoodLevel() + 2, 20));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
                        item.setAmount(item.getAmount() - 1);
                    }
                    break;
                case "investor_firework":
                    if((e.getAction().equals(Action.RIGHT_CLICK_AIR) && player.isGliding())){
                        e.setCancelled(false);
                        if(player.getGameMode().equals(GameMode.CREATIVE)){
                            return;
                        }
                        int price = ShopItems.firework_rocket.buyItem(1);
                        if(hasDust(player, price)){
                            subtractDust(player, price);
                            e.getItem().setAmount(e.getItem().getAmount()+1);
                            sendActionBarMessage(player, ChatColor.GREEN + "" + ChatColor.BOLD + "INVEST! " + ChatColor.RESET + ChatColor.GRAY + "Firework bought for " + ChatColor.RED + price + " " + dustIcon + " dust!");
                        }else{
                            ShopItems.firework_rocket.sellItem(1);
                            sendActionBarMessage(player, net.md_5.bungee.api.ChatColor.RED + "" + net.md_5.bungee.api.ChatColor.BOLD + "INVEST! " + net.md_5.bungee.api.ChatColor.RESET + net.md_5.bungee.api.ChatColor.GRAY + "Not enough dust!");
                        }

                    }
            }
        }
    }
}
