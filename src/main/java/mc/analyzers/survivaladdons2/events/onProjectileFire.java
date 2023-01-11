package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.PDCUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;


import java.util.HashMap;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getAttributes;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.getCustomEnchantments;

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
            String oldData = "";
            if(bowCustomEnchants.containsKey("teleport") && player.isSneaking()){ //90, 70, 40, cooldown in seconds.
                if(PDCUtils.has(arrow, "data")){
                    oldData = PDCUtils.get(arrow, "data");
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
                if(PDCUtils.has(player, "teleportCooldown")){
                    tpCooldown = Integer.parseInt(PDCUtils.get(player, "teleportCooldown"));
                }
                if((System.currentTimeMillis()/1000 - tpCooldown) >= cooldown ){
                    String teleportPotency = "teleport/" + bowCustomEnchants.get("teleport");
                    PDCUtils.set(arrow, "data", oldData + teleportPotency + " ");
                    PDCUtils.set(player, "teleportCooldown", String.valueOf(System.currentTimeMillis()/1000));
                }else{
                    String remaining = String.valueOf(cooldown - (System.currentTimeMillis()/1000 - Integer.parseInt(PDCUtils.get(player, "teleportCooldown"))));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Telebow Cooldown! " + ChatColor.GRAY + "(" + remaining + "s)"));
                }
            }
            if(bowCustomEnchants.containsKey("explosive")){
                if(PDCUtils.has(arrow, "data")){
                    oldData = PDCUtils.get(arrow, "data");
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
                if(PDCUtils.has(player, "explosiveCooldown")){
                    exCooldown = Integer.parseInt(PDCUtils.get(player, "explosiveCooldown"));
                }
                if((System.currentTimeMillis()/1000 - exCooldown) >= cooldown ){
                    String explosivePotency = "explosive/" + bowCustomEnchants.get("explosive");
                    PDCUtils.set(arrow, "data", oldData + explosivePotency + " ");
                    PDCUtils.set(player, "explosiveCooldown", String.valueOf(System.currentTimeMillis()/1000));
                }else{
                    String remaining = String.valueOf(cooldown - (System.currentTimeMillis()/1000 - Integer.parseInt(PDCUtils.get(player, "explosiveCooldown"))));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Explosive Cooldown! " + ChatColor.GRAY + "(" + remaining + "s)"));
                }
            }
            if(bowCustomEnchants.containsKey("ftts")){
                if(PDCUtils.has(arrow, "data")){
                    oldData = PDCUtils.get(arrow, "data");
                }
                String potency = "ftts/" + bowCustomEnchants.get("ftts");
                PDCUtils.set(arrow, "data", oldData + potency + " ");
            }
            if(bowCustomEnchants.containsKey("sorcery")){
                if(PDCUtils.has(arrow, "data")){
                    oldData = PDCUtils.get(arrow, "data");
                }
                String potency = "sorcery/" + bowCustomEnchants.get("sorcery");
                PDCUtils.set(arrow, "data", oldData + potency + " ");
            }
            if(bowCustomEnchants.containsKey("glasscannon")){
                if(PDCUtils.has(arrow, "data")){
                    oldData = PDCUtils.get(arrow, "data");
                }
                String potency = "glasscannon/" + bowCustomEnchants.get("glasscannon");
                PDCUtils.set(arrow, "data", oldData + potency + " ");
            }
            if(bowCustomEnchants.containsKey("parasite")){
                if(PDCUtils.has(arrow, "data")){
                    oldData = PDCUtils.get(arrow, "data");
                }
                String potency = "parasite/" + bowCustomEnchants.get("parasite");
                PDCUtils.set(arrow, "data", oldData + potency + " ");
            }
            if(bowCustomEnchants.containsKey("critical")){
                if(PDCUtils.has(arrow, "data")){
                    oldData = PDCUtils.get(arrow, "data");
                }
                String potency = "critical/" + bowCustomEnchants.get("critical");
                PDCUtils.set(arrow, "data", oldData + potency + " ");
            }
        }
    }
}
