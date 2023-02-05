package mc.analyzers.survivaladdons2.utility;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.naming.Name;

public class PDCUtils {
    public static String get(ItemStack item, String key){
        PersistentDataContainer itemPDC = item.getItemMeta().getPersistentDataContainer();
        String data = itemPDC.get(new NamespacedKey(SurvivalAddons2.getPlugin(), key), PersistentDataType.STRING);
        return data;
    }
    public static Integer getInt(ItemStack item, String key){
        PersistentDataContainer itemPDC = item.getItemMeta().getPersistentDataContainer();
        return itemPDC.get(new NamespacedKey(SurvivalAddons2.getPlugin(), key), PersistentDataType.INTEGER);
    }
    public static Long getLong(ItemStack item, String key){
        PersistentDataContainer itemPDC = item.getItemMeta().getPersistentDataContainer();
        Long data = itemPDC.get(new NamespacedKey(SurvivalAddons2.getPlugin(), key), PersistentDataType.LONG);
        return data;
    }
    public static Integer getInt(Entity item, String key){
        PersistentDataContainer itemPDC = item.getPersistentDataContainer();
        return itemPDC.get(new NamespacedKey(SurvivalAddons2.getPlugin(), key), PersistentDataType.INTEGER);
    }
    public static Long getLong(Entity item, String key){
        PersistentDataContainer itemPDC = item.getPersistentDataContainer();
        Long data = itemPDC.get(new NamespacedKey(SurvivalAddons2.getPlugin(), key), PersistentDataType.LONG);
        return data;
    }
    public static void set(ItemStack item, String key, String data){
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer itemPDC = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        itemPDC.set(namespacedKey, PersistentDataType.STRING, data);
        item.setItemMeta(meta);
    }
    public static void set(ItemStack item, String key, long data){
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer itemPDC = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        itemPDC.set(namespacedKey, PersistentDataType.LONG, data);
        item.setItemMeta(meta);
    }
    public static void set(ItemStack item, String key, int data){
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer itemPDC = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        itemPDC.set(namespacedKey, PersistentDataType.INTEGER, data);
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
    public static boolean hasInt(ItemStack item, String key){
        if(item == null){
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if(meta == null){
            return false;
        }
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        return meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.INTEGER);
    }
    public static boolean hasInt(Player item, String key){
        if(item == null){
            return false;
        }
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        return item.getPersistentDataContainer().has(namespacedKey, PersistentDataType.INTEGER);
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
    public static void set(Entity player, String key, int data){
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        player.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, data);
    }
    public static void set(Entity player, String key, long data){
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        player.getPersistentDataContainer().set(namespacedKey, PersistentDataType.LONG, data);
    }
    public static boolean has(Entity player, String key){
        if(player == null){
            return false;
        }
        NamespacedKey namespacedKey = new NamespacedKey(SurvivalAddons2.getPlugin(), key);
        return player.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING);
    }

}
