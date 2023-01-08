package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.pdc;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;


import java.util.Arrays;
import java.util.HashMap;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getAttributes;
import static mc.analyzers.survivaladdons2.utility.utility.getCustomEnchantments;

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

            //Settingarrow attr values
            StringBuilder attributes = new StringBuilder();
            for(String attribute : getAttributes(bow)){
                attributes.append(attribute).append(" ");
            }
            pdc.set(arrow, "attributeValues", attributes.toString());
            if(bow.getItemMeta().getEnchants().containsKey(Enchantment.ARROW_DAMAGE)){
                pdc.set(arrow, "powerEnchantmentValue", String.valueOf(bow.getEnchantments().get(Enchantment.ARROW_DAMAGE)));
            }else{
                pdc.set(arrow, "powerEnchantmentValue", "0");
            }

            if(pdc.has(bow, "enchantments")){
                pdc.set(arrow, "customEnchants", pdc.get(bow, "enchantments"));
            }
            if(arrow instanceof Arrow){
                PotionData potionData = ((Arrow) arrow).getBasePotionData();
                if(potionData.getType().equals(PotionType.INSTANT_DAMAGE) && potionData.isUpgraded()){
                    pdc.set(arrow, "harmingValue", "2");
                    ((Arrow) arrow).clearCustomEffects();
                    ((Arrow) arrow).setBasePotionData(new PotionData(PotionType.AWKWARD));
                }else if (potionData.getType().equals(PotionType.INSTANT_DAMAGE)){
                    pdc.set(arrow, "harmingValue", "1");
                    ((Arrow) arrow).clearCustomEffects();
                    ((Arrow) arrow).setBasePotionData(new PotionData(PotionType.AWKWARD));
                }
            }



            HashMap<String, Integer> bowCustomEnchants = getCustomEnchantments(bow);
            String oldData = "";
            if(bowCustomEnchants.containsKey("teleport") && player.isSneaking()){ //90, 70, 40, cooldown in seconds.
                if(pdc.has(arrow, "data")){
                    oldData = pdc.get(arrow, "data");
                }
                int level = bowCustomEnchants.get("teleport");
                int cooldown;
                if(level == 1){
                    cooldown = 20;
                }else if(level == 2){
                    cooldown = 15;
                }else if(level == 3){
                    cooldown = 10;
                }else{
                    cooldown = 0;
                }
                int tpCooldown = 0;
                if(pdc.has(player, "teleportCooldown")){
                    tpCooldown = Integer.parseInt(pdc.get(player, "teleportCooldown"));
                }
                if((System.currentTimeMillis()/1000 - tpCooldown) >= cooldown ){
                    String teleportPotency = "teleport/" + bowCustomEnchants.get("teleport");
                    pdc.set(arrow, "data", oldData + teleportPotency + " ");
                    pdc.set(player, "teleportCooldown", String.valueOf(System.currentTimeMillis()/1000));
                }else{
                    String remaining = String.valueOf(cooldown - (System.currentTimeMillis()/1000 - Integer.parseInt(pdc.get(player, "teleportCooldown"))));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Telebow Cooldown! " + ChatColor.GRAY + "(" + remaining + "s)"));
                }
            }
            if(bowCustomEnchants.containsKey("explosive")){
                if(pdc.has(arrow, "data")){
                    oldData = pdc.get(arrow, "data");
                }
                int level = bowCustomEnchants.get("explosive");
                int cooldown;
                if(level == 1){
                    cooldown = 10;
                }else if(level == 2){
                    cooldown = 5;
                }else{
                    cooldown = 0;
                }
                int exCooldown = 0;
                if(pdc.has(player, "explosiveCooldown")){
                    exCooldown = Integer.parseInt(pdc.get(player, "explosiveCooldown"));
                }
                if((System.currentTimeMillis()/1000 - exCooldown) >= cooldown ){
                    String explosivePotency = "explosive/" + bowCustomEnchants.get("explosive");
                    pdc.set(arrow, "data", oldData + explosivePotency + " ");
                    pdc.set(player, "explosiveCooldown", String.valueOf(System.currentTimeMillis()/1000));
                }else{
                    String remaining = String.valueOf(cooldown - (System.currentTimeMillis()/1000 - Integer.parseInt(pdc.get(player, "explosiveCooldown"))));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Explosive Cooldown! " + ChatColor.GRAY + "(" + remaining + "s)"));
                }
            }
            if(bowCustomEnchants.containsKey("ftts")){
                if(pdc.has(arrow, "data")){
                    oldData = pdc.get(arrow, "data");
                }
                String potency = "ftts/" + bowCustomEnchants.get("ftts");
                pdc.set(arrow, "data", oldData + potency + " ");
            }
            if(bowCustomEnchants.containsKey("sorcery")){
                if(pdc.has(arrow, "data")){
                    oldData = pdc.get(arrow, "data");
                }
                String potency = "sorcery/" + bowCustomEnchants.get("sorcery");
                pdc.set(arrow, "data", oldData + potency + " ");
            }
            if(bowCustomEnchants.containsKey("glasscannon")){
                if(pdc.has(arrow, "data")){
                    oldData = pdc.get(arrow, "data");
                }
                String potency = "glasscannon/" + bowCustomEnchants.get("glasscannon");
                pdc.set(arrow, "data", oldData + potency + " ");
            }
            if(bowCustomEnchants.containsKey("parasite")){
                if(pdc.has(arrow, "data")){
                    oldData = pdc.get(arrow, "data");
                }
                String potency = "parasite/" + bowCustomEnchants.get("parasite");
                pdc.set(arrow, "data", oldData + potency + " ");
            }
            if(bowCustomEnchants.containsKey("critical")){
                if(pdc.has(arrow, "data")){
                    oldData = pdc.get(arrow, "data");
                }
                String potency = "critical/" + bowCustomEnchants.get("critical");
                pdc.set(arrow, "data", oldData + potency + " ");
            }
        }
    }
}
