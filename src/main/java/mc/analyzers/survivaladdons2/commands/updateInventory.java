package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.tasks.SyncAttributes;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.setAttribute;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.syncItem;

public class updateInventory implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        for(ItemStack grindedItem : player.getInventory()){
            if(grindedItem == null || !SyncAttributes.materialAttributes.containsKey(grindedItem.getType())){
                continue;
            }
            String[] rawAttributes = SyncAttributes.materialAttributes.get(grindedItem.getType()).split(" ");
            System.out.println("gus " + grindedItem.getType() + Arrays.toString(rawAttributes));
            PDCUtils.set(grindedItem, "attributes", "");
            for(String rawAttribute : rawAttributes){
                String attribute = rawAttribute.split("/")[0];
                double potency = Double.parseDouble(rawAttribute.split("/")[1]);
                System.out.println("attr " + attribute + " " + potency);
                setAttribute(grindedItem, attribute, potency);
            }
            syncItem(grindedItem);
            PDCUtils.set(grindedItem, "attributed", "true");
        }
        return true;
    }
}
