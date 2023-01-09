package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.pdc;
import mc.analyzers.survivaladdons2.utility.utility;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;

public class onProjectileHit implements Listener {
    @EventHandler
    public void onArrowHit(ProjectileHitEvent e){
        if(e.getEntity().getShooter() instanceof HumanEntity && e.getEntity() instanceof AbstractArrow){
            Player player = (Player) e.getEntity().getShooter();
            AbstractArrow arrow = (AbstractArrow)  e.getEntity();
            Location playerLocation = player.getLocation();
            if(pdc.has(arrow, "data")){
                Location arrowLocation = arrow.getLocation();
                String rawData = pdc.get(arrow, "data");
                String[] modifiers = rawData.split(" ");
                for(String modifier : modifiers){
                    if(modifier.equals("") || modifier.equals(" ")){
                        continue;
                    }
                    String type = modifier.split("/")[0];
                    int power = Integer.parseInt(modifier.split("/")[1]);
                    switch (type){
                        case "teleport":
                            arrowLocation.setPitch(playerLocation.getPitch());
                            arrowLocation.setYaw(playerLocation.getYaw());
                            player.teleport(arrowLocation);
                            arrow.remove();
                            //player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "POOF! " + ChatColor.RESET + ChatColor.GRAY + "Teleported !");
                            break;
                        case "explosive":
                            int excost = 5/power;
                            if(Integer.parseInt(pdc.get(player, "dust")) >= excost){
                                arrow.remove();
                                player.getWorld().createExplosion(arrowLocation, power, false, false);
                                pdc.set(player, "dust", String.valueOf(Integer.parseInt(pdc.get(player, "dust")) - excost));
                                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "BAM! " + ChatColor.RESET + ChatColor.GRAY + "Explosion cost " + ChatColor.RED + excost + " " + dustIcon + " dust.");
                            }
                            break;
                        case "ftts":
                            if(e.getHitEntity() != null){
                                if(!pdc.has(player, "fttsCount")){
                                    pdc.set(player, "fttsCount", "1");
                                    return;
                                }
                                pdc.set(player, "fttsCount", String.valueOf(Integer.parseInt(pdc.get(player, "fttsCount")) + 1));
                                int lenght = 6;
                                int requiredShots = 3;
                                if(power > 1){
                                    requiredShots = 2;
                                    lenght = 7;
                                }
                                int speedAmplifier = 0;
                                if(power >= 3){
                                    speedAmplifier = 1;
                                }
                                if(Integer.parseInt(pdc.get(player, "fttsCount")) >= requiredShots){
                                    pdc.set(player, "fttsCount", "0");
                                    player.sendMessage(net.md_5.bungee.api.ChatColor.of("#D0FF48") +""+ ChatColor.BOLD + "FTTS! " + ChatColor.RESET + ChatColor.GRAY + "Speed " +
                                            utility.roman(speedAmplifier + 1) + " for " + lenght + " seconds.");
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, lenght * 20, speedAmplifier));
                                }
                            }else{
                                pdc.set(player, "fttsCount", "0");
                            }
                    }
                }
            }
            if(e.getHitBlock() != null){
                arrow.remove();
            }
        }
    }
}
