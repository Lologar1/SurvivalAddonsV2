package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Objects;

import static mc.analyzers.survivaladdons2.quests.Quest.incrementQuest;
import static mc.analyzers.survivaladdons2.utility.utility.*;

public class onEntityDeath implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){ //Implement nasty totem o' undying mechanics... and also fix all the drop item crap :(
        Entity entity = e.getEntity();
        if(!(entity instanceof LivingEntity)){
            return;
        }
        if(entity instanceof Witch){
            e.getDrops().removeIf(drop -> drop.getType().equals(Material.REDSTONE));
        }
        if(!(entity instanceof Player) && ((LivingEntity) entity).getKiller() != null){
            Player killer = e.getEntity().getKiller();
            if(getCustomEnchantments(killer.getInventory().getItemInMainHand()).containsKey("experience")){
                int potency = getCustomEnchantments(killer.getInventory().getItemInMainHand()).get("experience");
                int droppedXp = e.getDroppedExp();
                int newXp = (int) Math.round(droppedXp + droppedXp*0.11*potency);
                System.out.println(newXp);
                e.setDroppedExp(newXp);
            }
            //Quests
            if(pdc.get(killer, "activeHourlyQuest").contains("kill")){
                if(pdc.get(killer, "activeHourlyQuest").contains("passive") && !(e.getEntity() instanceof Monster)){
                    incrementQuest("hourly", pdc.get(killer, "activeHourlyQuest"),killer, 1);
                }else if(pdc.get(killer, "activeHourlyQuest").contains("hostile") && e.getEntity() instanceof Monster){
                    incrementQuest("hourly", pdc.get(killer, "activeHourlyQuest"),killer, 1);
                }else if(pdc.get(killer, "activeHourlyQuest").contains("nether") && e.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    incrementQuest("hourly", pdc.get(killer, "activeHourlyQuest"),killer, 1);
                }
            }
            if(!pdc.get(killer, "activeDailyQuest").equals("null")){
                if(pdc.get(killer, "activeDailyQuest").contains("passive") && !(e.getEntity() instanceof Monster)){
                    incrementQuest("daily", pdc.get(killer, "activeDailyQuest"),killer, 1);
                }else if(pdc.get(killer, "activeDailyQuest").contains("hostile") && e.getEntity() instanceof Monster){
                    incrementQuest("daily", pdc.get(killer, "activeDailyQuest"),killer, 1);
                }else if(pdc.get(killer, "activeDailyQuest").contains("nether") && e.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    incrementQuest("daily", pdc.get(killer, "activeDailyQuest"),killer, 1);
                }
            }
            if(!pdc.get(killer, "activeWeeklyQuest").equals("null")){
                if(pdc.get(killer, "activeWeeklyQuest").contains("passive") && !(e.getEntity() instanceof Monster)){
                    incrementQuest("weekly", pdc.get(killer, "activeWeeklyQuest"),killer, 1);
                }else if(pdc.get(killer, "activeWeeklyQuest").contains("hostile") && e.getEntity() instanceof Monster){
                    incrementQuest("weekly", pdc.get(killer, "activeWeeklyQuest"),killer, 1);
                }else if(pdc.get(killer, "activeWeeklyQuest").contains("nether") && e.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    incrementQuest("weekly", pdc.get(killer, "activeWeeklyQuest"),killer, 1);
                }
            }
        }
    }
}
