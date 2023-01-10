package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.ItemStackUtils;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static mc.analyzers.survivaladdons2.quests.Quest.incrementQuest;

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
            if(ItemStackUtils.getCustomEnchantments(killer.getInventory().getItemInMainHand()).containsKey("experience")){
                int potency = ItemStackUtils.getCustomEnchantments(killer.getInventory().getItemInMainHand()).get("experience");
                int droppedXp = e.getDroppedExp();
                int newXp = (int) Math.round(droppedXp + droppedXp*0.11*potency);
                System.out.println(newXp);
                e.setDroppedExp(newXp);
            }
            //Quests
            if(PDCUtils.get(killer, "activeHourlyQuest").contains("kill")){
                if(PDCUtils.get(killer, "activeHourlyQuest").contains("passive") && !(e.getEntity() instanceof Monster)){
                    incrementQuest("hourly", PDCUtils.get(killer, "activeHourlyQuest"),killer, 1);
                }else if(PDCUtils.get(killer, "activeHourlyQuest").contains("hostile") && e.getEntity() instanceof Monster){
                    incrementQuest("hourly", PDCUtils.get(killer, "activeHourlyQuest"),killer, 1);
                }else if(PDCUtils.get(killer, "activeHourlyQuest").contains("nether") && e.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    incrementQuest("hourly", PDCUtils.get(killer, "activeHourlyQuest"),killer, 1);
                }
            }
            if(!PDCUtils.get(killer, "activeDailyQuest").equals("null")){
                if(PDCUtils.get(killer, "activeDailyQuest").contains("passive") && !(e.getEntity() instanceof Monster)){
                    incrementQuest("daily", PDCUtils.get(killer, "activeDailyQuest"),killer, 1);
                }else if(PDCUtils.get(killer, "activeDailyQuest").contains("hostile") && e.getEntity() instanceof Monster){
                    incrementQuest("daily", PDCUtils.get(killer, "activeDailyQuest"),killer, 1);
                }else if(PDCUtils.get(killer, "activeDailyQuest").contains("nether") && e.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    incrementQuest("daily", PDCUtils.get(killer, "activeDailyQuest"),killer, 1);
                }
            }
            if(!PDCUtils.get(killer, "activeWeeklyQuest").equals("null")){
                if(PDCUtils.get(killer, "activeWeeklyQuest").contains("passive") && !(e.getEntity() instanceof Monster)){
                    incrementQuest("weekly", PDCUtils.get(killer, "activeWeeklyQuest"),killer, 1);
                }else if(PDCUtils.get(killer, "activeWeeklyQuest").contains("hostile") && e.getEntity() instanceof Monster){
                    incrementQuest("weekly", PDCUtils.get(killer, "activeWeeklyQuest"),killer, 1);
                }else if(PDCUtils.get(killer, "activeWeeklyQuest").contains("nether") && e.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    incrementQuest("weekly", PDCUtils.get(killer, "activeWeeklyQuest"),killer, 1);
                }
            }
        }
    }
}
