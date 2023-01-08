package mc.analyzers.survivaladdons2.tasks;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.mergeModifiers;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.setAttribute;
import static mc.analyzers.survivaladdons2.utility.itemList.item;
import static mc.analyzers.survivaladdons2.utility.utility.*;

public class RefreshInventory extends BukkitRunnable {

    private Player player;
    public RefreshInventory(Player player){
        this.player = player;
    }

    @Override
    public void run() {
        for(ItemStack grindedItem : player.getInventory()){
            if(grindedItem == null || !SyncAttributes.materialAttributes.containsKey(getAbsoluteIdMaterial(grindedItem))){
                continue;
            }else{
                try{
                    syncItem(grindedItem);
                }catch (Exception ignored){};
                String[] rawAttributes = SyncAttributes.materialAttributes.get(getAbsoluteIdMaterial(grindedItem)).split(" ");
                pdc.set(grindedItem, "attributes", "");
                for(String rawAttribute : rawAttributes){
                    String attribute = rawAttribute.split("/")[0];
                    double potency = Double.parseDouble(rawAttribute.split("/")[1]);
                    setAttribute(grindedItem, attribute, potency);
                    pdc.set(grindedItem, "attributed", "true");
                }
            }
            if(pdc.has(grindedItem, "currentAttributeModifier")){
                ItemStack modif = item(pdc.get(grindedItem, "currentAttributeModifier"));
                mergeModifiers(grindedItem, modif);
            }
            syncItem(grindedItem);
        }
        if(SurvivalAddons2.getPlugin().getServer().getOnlinePlayers().contains(player)){
            BukkitTask refreshPlayer = new RefreshInventory(player).runTaskLater(SurvivalAddons2.getPlugin(), 750L);
        }
    }
}
