package mc.analyzers.survivaladdons2.tasks;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.ItemStackUtils;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.mergeModifiers;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.setAttribute;
import static mc.analyzers.survivaladdons2.utility.ItemList.item;

public class RefreshInventory extends BukkitRunnable {

    private Player player;
    public RefreshInventory(Player player){
        this.player = player;
    }

    @Override
    public void run() {
        for(ItemStack grindedItem : player.getInventory()){
            if(grindedItem == null || !SyncAttributes.materialAttributes.containsKey(ItemStackUtils.getAbsoluteIdMaterial(grindedItem))){
                continue;
            }else{
                try{
                    ItemStackUtils.syncItem(grindedItem);
                }catch (Exception ignored){};
                String[] rawAttributes = SyncAttributes.materialAttributes.get(ItemStackUtils.getAbsoluteIdMaterial(grindedItem)).split(" ");
                PDCUtils.set(grindedItem, "attributes", "");
                for(String rawAttribute : rawAttributes){
                    String attribute = rawAttribute.split("/")[0];
                    double potency = Double.parseDouble(rawAttribute.split("/")[1]);
                    setAttribute(grindedItem, attribute, potency);
                    PDCUtils.set(grindedItem, "attributed", "true");
                }
            }
            if(PDCUtils.has(grindedItem, "currentAttributeModifier")){
                ItemStack modif = item(PDCUtils.get(grindedItem, "currentAttributeModifier"));
                mergeModifiers(grindedItem, modif);
            }
            ItemStackUtils.syncItem(grindedItem);
        }
        if(SurvivalAddons2.getPlugin().getServer().getOnlinePlayers().contains(player)){
            BukkitTask refreshPlayer = new RefreshInventory(player).runTaskLater(SurvivalAddons2.getPlugin(), 750L);
        }
    }
}
