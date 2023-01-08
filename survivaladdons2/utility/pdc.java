package mc.analyzers.survivaladdons2.utility;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.xml.stream.events.Namespace;

public class pdc {
    public static String get(ItemStack item, String key){
        PersistentDataContainer itemPDC = item.getItemMeta().getPersistentDataContainer();
        String data = itemPDC.get(new NamespacedKey(SurvivalAddons2.getPlugin(), key), PersistentDataType.STRING);
        return data;
    }
    public static void set(ItemStack item, String key, String data){
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer itemPDC = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        itemPDC.set(namespacedKey, PersistentDataType.STRING, data);
        item.setItemMeta(meta);
    }
    public static boolean has(ItemStack item, String key){
        if(item == null){
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if(meta == null){
            return false;
        }
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        return meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING);
    }

    //Entity stuff

    public static String get(Entity player, String key){
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        return playerPDC.get(namespacedKey, PersistentDataType.STRING);
    }
    public static void set(Entity player, String key, String data){
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        player.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, data);
    }
    public static boolean has(Entity player, String key){
        if(player == null){
            return false;
        }
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        return player.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING);
    }

}
