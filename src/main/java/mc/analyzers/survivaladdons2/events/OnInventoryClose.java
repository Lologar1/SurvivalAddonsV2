package mc.analyzers.survivaladdons2.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static mc.analyzers.survivaladdons2.utility.PlayerUtils.giveItem;


public class OnInventoryClose implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent  e){
        Player player = (Player) e.getPlayer();
        if(e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Anvil - Combine & Modify")){

            ItemStack anvilGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta anvilGlassMeta = anvilGlass.getItemMeta();
            anvilGlassMeta.setDisplayName(ChatColor.BLACK + "");
            anvilGlass.setItemMeta(anvilGlassMeta);

            ItemStack item1 = e.getInventory().getItem(20);
            ItemStack item2 = e.getInventory().getItem(22);
            if(!(item1 == null) && !item1.equals(anvilGlass)){
                try{
                    giveItem(player, item1, item1.getAmount());
                }catch (Exception ignored){}
            }
            if(!(item2 == null) &&!item2.equals(anvilGlass)){
                try{
                    giveItem(player, item2, item2.getAmount());
                }catch (Exception ignored){}
            }
        }else if(e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Anvil - Finished")){
            ItemStack item1 = e.getInventory().getItem(24);
            ItemStack item2 = e.getInventory().getItem(20);
            ItemStack item3 = e.getInventory().getItem(22);
            try{
                giveItem(player, item1, item1.getAmount());
            }catch (Exception ignored){}
            try{
                giveItem(player, item2, item2.getAmount());
            }catch (Exception ignored){}
            try{
                giveItem(player, item3, item3.getAmount());
            }catch (Exception ignored){}
        }else if(e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Grindstone")) {
            ItemStack item1 = e.getInventory().getItem(13);
            try {
                giveItem(player, item1, item1.getAmount());
            } catch (Exception ignored) {}
        }else if(e.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "Tome of Knowledge")){
            ItemStack item1 = e.getInventory().getItem(11);
            try{
                giveItem(player, item1, item1.getAmount());
            }catch (Exception ignored){}
        }else if(e.getView().getTitle().equals(ChatColor.DARK_GREEN + "Brewing")){
            ItemStack item1 = e.getInventory().getItem(2);
            try{
                giveItem(player, item1, item1.getAmount());
            }catch (Exception ignored){}
            ItemStack item2 = e.getInventory().getItem(6);
            try{
                giveItem(player, item2, item2.getAmount());
            }catch (Exception ignored){}
        }
    }
}
