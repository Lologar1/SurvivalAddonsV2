package mc.analyzers.survivaladdons2.utility;

import de.tr7zw.nbtapi.*;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class OfflineInventories {

    private final File playerDataFolder;

    public OfflineInventories() {
        // Player-data is stored in the main world's world folder
        File mainWorldFolder = Bukkit.getWorlds().get(0).getWorldFolder();
        this.playerDataFolder = new File(mainWorldFolder, "playerdata");
    }
    public @Nullable
    Inventory read(UUID holder, String title) throws IOException {
        NBTFile nbtFile = getNBTFor(holder);

        if (nbtFile == null)
            return null;

        NBTCompoundList itemsList = nbtFile.getCompoundList("Inventory");

        if (itemsList == null)
            return null;

        Inventory inv = Bukkit.createInventory(null, 9 * 5, title);

        for (ReadWriteNBT item : itemsList) {
            if (!(item instanceof NBTCompound))
                continue;

            NBTCompound itemCompound = (NBTCompound) item;

            inv.setItem(
                    mapNBTSlotToInventorySlot(itemCompound.getInteger("Slot")),
                    NBTItem.convertNBTtoItem(itemCompound)
            );
        }

        return inv;
    }

    public boolean write(UUID holder, Inventory inv) throws IOException {
        NBTFile nbtFile = getNBTFor(holder);

        if (nbtFile == null)
            return false;

        NBTCompoundList itemsList = nbtFile.getCompoundList("Inventory");

        if (itemsList == null)
            return false;

        itemsList.clear();

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);

            if (item == null || item.getType() == Material.AIR)
                continue;

            NBTContainer itemNBT = NBTItem.convertItemtoNBT(item);
            itemNBT.setInteger("Slot", mapInventorySlotToNBTSlot(i));
            itemsList.addCompound(itemNBT);
        }

        nbtFile.save();
        return true;
    }

    private @Nullable NBTFile getNBTFor(UUID holder) throws IOException {
        File targetData = new File(playerDataFolder, holder + ".dat");

        if (!targetData.exists())
            return null;

        return new NBTFile(targetData);
    }

    public boolean isUsableSlot(int slot) {
        return !((slot >= 5 && slot <= 8) || slot < 0 || slot > 44);
    }

  /*
    Mapping layout:

    Inventory | NBT
    0         | 103    (Helmet)
    1         | 102    (Chestplate)
    2         | 101    (Leggings)
    3         | 100    (Boots)
    4         | -106   (Shield)
    5-8       | NONE   Always empty
    9-35      | 9-35   (Inventory without hotbar)
    36-44     | 0-8    (hotbar)
   */
    private int mapNBTSlotToInventorySlot(int slot) {
        if (slot >= 100 && slot <= 103)
            return 103 - slot;

        if (slot == -106)
            return 4;

        if (slot >= 9 && slot <= 35)
            return slot;

        if (slot >= 0 && slot <= 8)
            return 36 + slot;

        throw new IllegalStateException("Mapping for " + slot + " is undefined");
    }

    private int mapInventorySlotToNBTSlot(int slot) {
        if (slot <= 3)
            return 103 - slot;

        if (slot == 4)
            return -106;

        if (slot <= 8)
            throw new IllegalStateException("Mapping for " + slot + " is undefined");

        if (slot <= 35)
            return slot;

        if (slot <= 45)
            return slot - 36;

        throw new IllegalStateException("Mapping for " + slot + " is undefined");
    }
}
