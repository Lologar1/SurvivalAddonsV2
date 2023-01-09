package mc.analyzers.survivaladdons2.shop;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import sun.jvm.hotspot.types.JDoubleField;

public class ShopItems {
    public static final ShopItem iron_ingot = new ShopItem(new ItemStack(Material.IRON_INGOT), 16, 10, true);
    public static final ShopItem lapis_lazuli = new ShopItem(new ItemStack(Material.LAPIS_LAZULI), 32, 4, true);
    public static final ShopItem gold_ingot = new ShopItem(new ItemStack(Material.GOLD_INGOT), 16, 6, true);
    public static final ShopItem diamond = new ShopItem(new ItemStack(Material.DIAMOND), 8, 35, true);
    public static final ShopItem netherite_scrap = new ShopItem(new ItemStack(Material.NETHERITE_SCRAP), 4, 175, true);
    public static final ShopItem bedrock = new ShopItem(new ItemStack(Material.BEDROCK), 1000, 100000, true);
    public static final ShopItem coal = new ShopItem(new ItemStack(Material.COAL), 32, 2, true);
    public static final ShopItem bone = new ShopItem(new ItemStack(Material.BONE), 16, 2, true);
    public static final ShopItem string = new ShopItem(new ItemStack(Material.STRING), 32, 2, true);
    public static final ShopItem flesh = new ShopItem(new ItemStack(Material.ROTTEN_FLESH), 32, 2, true);
    public static final ShopItem emerald = new ShopItem(new ItemStack(Material.EMERALD), 32, 5, true);
    public static final ShopItem gunpowder = new ShopItem(new ItemStack(Material.GUNPOWDER), 32, 5, true);
    public static final ShopItem blazerod = new ShopItem(new ItemStack(Material.BLAZE_ROD), 16, 2, true);
    public static final ShopItem spidereye = new ShopItem(new ItemStack(Material.SPIDER_EYE), 32, 2, true);
    public static final ShopItem magma_cream = new ShopItem(new ItemStack(Material.MAGMA_CREAM), 32, 4, true);
    public static final ShopItem ghast_tear = new ShopItem(new ItemStack(Material.GHAST_TEAR), 32, 5, true);
    public static final ShopItem quartz = new ShopItem(new ItemStack(Material.QUARTZ),64, 3, true);
    public static final ShopItem ender_pearl = new ShopItem(new ItemStack(Material.ENDER_PEARL), 16, 5, true);

    public static final ShopItem dirt = new ShopItem(new ItemStack(Material.DIRT), 10000000, 1, false);
    public static final ShopItem cobblestone = new ShopItem(new ItemStack(Material.COBBLESTONE), 10000000, 2, false);
}
