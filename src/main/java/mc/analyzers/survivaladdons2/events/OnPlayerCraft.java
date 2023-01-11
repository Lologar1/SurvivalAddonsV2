package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static mc.analyzers.survivaladdons2.quests.Quest.checkQuest;
import static mc.analyzers.survivaladdons2.utility.ItemList.item;

public class OnPlayerCraft implements Listener {
    @EventHandler
    public void OnPlayerCraft(CraftItemEvent e){
        ItemStack craftedItem = e.getInventory().getResult(); //Get result of recipe
        Inventory Inventory = e.getInventory(); //Get crafting inventory
        ClickType clickType = e.getClick();
        int realAmount = craftedItem.getAmount();
        if(clickType.isShiftClick())
        {
            int lowerAmount = craftedItem.getMaxStackSize() + 1000; //Set lower at recipe result max stack size + 1000 (or just highter max stacksize of reciped item)
            for(ItemStack actualItem : Inventory.getContents()) //For each item in crafting inventory
            {
                if(!actualItem.getType().isAir() && lowerAmount > actualItem.getAmount() && !actualItem.getType().equals(craftedItem.getType())) //if slot is not air && lowerAmount is highter than this slot amount && it's not the recipe amount
                    lowerAmount = actualItem.getAmount(); //Set new lower amount
            }
            //Calculate the final amount : lowerAmount * craftedItem.getAmount
            realAmount = lowerAmount * craftedItem.getAmount();
        }
        checkQuest("craft", "item", (Player) e.getWhoClicked(), realAmount);
        if(e.getInventory().getResult() != null && e.getInventory().getResult().getType().equals(Material.SHIELD)){
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(ChatColor.RED + "Shields are disabled!");
        }
        if(e.getInventory().getResult().equals(item("funky_feather"))){
            checkQuest("craft", "feather", (Player) e.getWhoClicked(), 1);
        }
        if(PDCUtils.has(e.getInventory().getResult(), "id")){
            checkQuest("craft", "custom", (Player) e.getWhoClicked(), 1);
        }
    }
}
