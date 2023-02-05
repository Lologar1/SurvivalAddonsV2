package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.shop.ShopItems;
import mc.analyzers.survivaladdons2.utility.EntityUtils;
import mc.analyzers.survivaladdons2.utility.ItemStackUtils;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import mc.analyzers.survivaladdons2.utility.PlayerUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static java.lang.Math.min;
import static java.lang.Math.nextAfter;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.heartIcon;
import static mc.analyzers.survivaladdons2.customenchantments.customEnchantmentsWrapper.*;
import static mc.analyzers.survivaladdons2.customenchantments.customEnchantmentsWrapper.legendaryEnchantments;
import static mc.analyzers.survivaladdons2.utility.ItemList.item;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.*;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.*;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.*;

public class PlayerInteractEvent implements Listener {
    @EventHandler
    public void playerInteractEvent(org.bukkit.event.player.PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
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
        if((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))){
            int potency;
            if(!PDCUtils.has(item, "id")){
                return;
            }
            switch (PDCUtils.get(item, "id")){
                case "power_stick":
                    potency = Integer.parseInt(PDCUtils.get(item, "power"));
                    if(Integer.parseInt(PDCUtils.get(player, "dust")) >= (potency+1)){
                        if(checkCooldown(item, "stickCooldown")){
                            createCooldown(item, "stickCooldown", 5000);
                            player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "EMPOWER! " + ChatColor.RESET + ChatColor.GRAY + "for " + potency + "% damage.");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 2);
                            subtractDust(player, 2);
                            PDCUtils.set(player, "temporaryDamageBoostPercent", potency);
                        }else{
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Stick Cooldown! " + ChatColor.GRAY + "(" + getRemainingCooldown(item, "stickCooldown")/1000 + "s)"));
                        }
                    }
                    break;
                case "healing_stick":
                    potency = Integer.parseInt(PDCUtils.get(item, "healing"));
                    if(Integer.parseInt(PDCUtils.get(player, "dust")) >= (potency+1)){
                        if(checkCooldown(item, "stickCooldown")){
                            createCooldown(item, "stickCooldown", 5000);
                            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "HEAL! " + ChatColor.RESET + ChatColor.GRAY + "for " + potency + " hearts.");
                            player.setHealth(min(player.getHealth() + 2*potency, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
                            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 2);
                            subtractDust(player, potency+1);
                        }else{
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Stick Cooldown! " + ChatColor.GRAY + "(" + getRemainingCooldown(item, "stickCooldown")/1000 + "s)"));
                        }
                    }
                    break;
                case "food_stick":
                    potency = Integer.parseInt(PDCUtils.get(item, "food"));
                    if(Integer.parseInt(PDCUtils.get(player, "dust")) >= potency){
                        if(checkCooldown(item, "stickCooldown")){
                            createCooldown(item, "stickCooldown", 5000);
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "EAT! " + ChatColor.RESET + ChatColor.GRAY + "for " + potency*2 + " satruation.");
                            player.setFoodLevel(min(player.getFoodLevel() + potency*2, 20));
                            player.setSaturation(min(player.getSaturation() + 3, 20));
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
                            subtractDust(player, potency);
                        }else{
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Stick Cooldown! " + ChatColor.GRAY + "(" + getRemainingCooldown(item, "stickCooldown")/1000 + "s)"));
                        }
                    }else{
                        sendActionBarMessage(player, ChatColor.RED + "Not enough dust!");
                    }
                    break;
                case "god_stick":
                    if(checkCooldown(item, "stickCooldown")){
                        if(hasDust(player, 5)){
                            createCooldown(item, "stickCooldown", 3000);
                            player.sendMessage(net.md_5.bungee.api.ChatColor.of("#C7CCFB") + "" + ChatColor.BOLD + "GOD! " + ChatColor.RESET + ChatColor.RED + "+ 1.5" + heartIcon +
                                    net.md_5.bungee.api.ChatColor.GRAY + ", " + ChatColor.YELLOW + "+ 4 food" + ChatColor.GRAY + ", " + ChatColor.GOLD + "+ 3 saturation" +
                                    ChatColor.GRAY + ", " + ChatColor.DARK_RED + "+150% damage");
                            player.setHealth(min(player.getHealth() + 3, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
                            player.setFoodLevel(min(player.getFoodLevel() + 4, 20));
                            player.setSaturation(min(player.getSaturation() + 3, 20));
                            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 2);
                            PDCUtils.set(player, "temporaryDamageBoostPercent", 150);
                            subtractDust(player, 5);
                        }else{
                         sendActionBarMessage(player, ChatColor.RED + "" + ChatColor.BOLD + "GODFAIL! " + ChatColor.RESET + ChatColor.GRAY + "Not enough dust!");
                        }
                    }else{
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Stick Cooldown! " + ChatColor.GRAY + "(" + getRemainingCooldown(item, "stickCooldown")/1000 + "s)"));
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
                    if(checkCooldown(item, "icewandCooldown")){
                        if(Integer.parseInt(PDCUtils.get(player, "dust")) >= 4){
                            PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - 4));
                        }else {
                            return;
                        }
                        createCooldown(item, "icewandCooldown", 3000);
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
                                EntityUtils.damageEntity((LivingEntity) toDamage, 0, 11, player, true);
                            }
                        }
                    }else{
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Ice Wand Cooldown! " + ChatColor.GRAY + "(" + getRemainingCooldown(item, "icewandCooldown")/1000 + "s)"));
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
                    if((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && player.isGliding()) {
                        e.setCancelled(true);
                        int price = ShopItems.firework_rocket.buyItem(1);
                        if (hasDust(player, price)) {
                            subtractDust(player, price);
                            ItemStack toBoost = new ItemStack(Material.FIREWORK_ROCKET);
                            FireworkMeta boostMeta = (FireworkMeta) toBoost.getItemMeta();
                            boostMeta.setPower(3);
                            toBoost.setItemMeta(boostMeta);
                            player.fireworkBoost(toBoost);
                            sendActionBarMessage(player, ChatColor.GREEN + "" + ChatColor.BOLD + "INVEST! " + ChatColor.RESET + ChatColor.GRAY + "Firework bought for " + ChatColor.RED + price + " " + dustIcon + " dust!");
                        } else {
                            ShopItems.firework_rocket.sellItem(1);
                            sendActionBarMessage(player, net.md_5.bungee.api.ChatColor.RED + "" + net.md_5.bungee.api.ChatColor.BOLD + "INVEST! " + net.md_5.bungee.api.ChatColor.RESET + net.md_5.bungee.api.ChatColor.GRAY + "Not enough dust!");
                        }
                    }
                    break;
                case "redstone_bundle":
                    if(!PDCUtils.hasInt(item, "redstone") || PDCUtils.getInt(item, "redstone") == 0){
                        int toStore = 0;
                        for(ItemStack potential : player.getInventory()){
                            if(potential == null){
                                continue;
                            }
                            if(potential.getType().equals(Material.REDSTONE) || potential.getType().equals(Material.REDSTONE_BLOCK)){
                                if(potential.getType().equals(Material.REDSTONE_BLOCK)){
                                    toStore += potential.getAmount()*9;
                                }else {
                                    toStore += potential.getAmount();
                                }
                                potential.setAmount(0);
                            }
                        }
                        if(toStore == 0){
                            player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "Put redstone dust or blocks in your inventory!");
                        }else{
                            PDCUtils.set(item, "redstone", toStore);
                            item.setType(Material.CHEST_MINECART);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(ChatColor.DARK_AQUA + "Redstone Bundle : " + ChatColor.RED + toStore + dustIcon);
                            meta.setLore(new ArrayList<String>(Collections.singleton(ChatColor.GRAY + "Right-Click to redeem the dust inside!")));
                            item.setItemMeta(meta);
                        }
                    }else{
                        int toGive = PDCUtils.getInt(item, "redstone");
                        addDust(player, toGive);
                        item.setAmount(0);
                        giveItem(player, item("redstone_bundle"), 1);
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "REDEEMED! " + ChatColor.RESET + ChatColor.GRAY + "Received " + ChatColor.RED + toGive + dustIcon + " dust!");
                    }
            }
        }
    }
}

