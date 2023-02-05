package mc.analyzers.survivaladdons2.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;

public class OnInventoryOpen implements Listener {
    @EventHandler
    public void onAnvilOpen(InventoryOpenEvent e){
        Player player = (Player) e.getPlayer();
        ItemStack anvilGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta anvilGlassMeta = anvilGlass.getItemMeta();
        anvilGlassMeta.setDisplayName(ChatColor.BLACK + "");
        anvilGlass.setItemMeta(anvilGlassMeta);
        if(e.getInventory().getType().equals(InventoryType.ANVIL)){
            e.setCancelled(true);
            Inventory anvil = Bukkit.createInventory(player, 27, ChatColor.DARK_GRAY + "Anvil - Combine & Modify");
            for(int i = 0; i < 27; i++){
                anvil.setItem(i, anvilGlass);
            }

            anvil.setItem(20, new ItemStack(Material.AIR));
            anvil.setItem(22, new ItemStack(Material.AIR));
            ItemStack combinedItem = new ItemStack(Material.BARRIER);
            ItemMeta cm = combinedItem.getItemMeta();
            cm.setDisplayName(ChatColor.RED + "Put two valid items to combine!");
            combinedItem.setItemMeta(cm);
            anvil.setItem(24, combinedItem);

            ItemStack combine = new ItemStack(Material.ANVIL);
            ItemMeta combineMeta = combine.getItemMeta();
            combineMeta.setDisplayName(ChatColor.GREEN + "Click to combine !");
            combine.setItemMeta(combineMeta);
            anvil.setItem(13, combine);

            ItemStack price = new ItemStack(Material.REDSTONE);
            ItemMeta pricemeta = price.getItemMeta();
            pricemeta.setDisplayName(ChatColor.GOLD + "Total cost: " + ChatColor.RED + "0 " + dustIcon + " dust");
            price.setItemMeta(pricemeta);
            anvil.setItem(4, price);
            player.openInventory(anvil);
        }else if(e.getInventory().getType().equals(InventoryType.GRINDSTONE)){
            e.setCancelled(true);
            Inventory anvil = Bukkit.createInventory(player, 27, ChatColor.DARK_GRAY + "Grindstone");
            for(int i = 0; i < 27; i++){
                anvil.setItem(i, anvilGlass);
            }

            anvil.setItem(13, new ItemStack(Material.AIR));

            ItemStack price = new ItemStack(Material.GRINDSTONE);
            ItemMeta pricemeta = price.getItemMeta();
            pricemeta.setDisplayName(ChatColor.GREEN + "Click to grind the item below!");
            price.setItemMeta(pricemeta);
            anvil.setItem(4, price);
            player.openInventory(anvil);
        }else if(e.getInventory().getType().equals(InventoryType.BREWING)){
            e.setCancelled(true);
            Inventory stand = Bukkit.createInventory(player, 9, ChatColor.DARK_GREEN + "Brewing");
            ItemStack brew = new ItemStack(Material.BREWING_STAND);
            ItemMeta brewMeta = brew.getItemMeta();
            brewMeta.setDisplayName(ChatColor.GREEN + "Click to brew!");
            brew.setItemMeta(brewMeta);
            stand.setItem(6, new ItemStack(Material.AIR));
            stand.setItem(2, new ItemStack(Material.AIR));
            stand.setItem(4, brew);

            stand.setItem(0, anvilGlass);
            stand.setItem(1, anvilGlass);
            stand.setItem(3, anvilGlass);
            stand.setItem(5, anvilGlass);
            stand.setItem(7, anvilGlass);
            stand.setItem(8, anvilGlass);
            player.openInventory(stand);
        }
    }
}
