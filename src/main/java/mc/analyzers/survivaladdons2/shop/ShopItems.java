package mc.analyzers.survivaladdons2.shop;

import mc.analyzers.survivaladdons2.commands.Shop;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static mc.analyzers.survivaladdons2.utility.ItemList.item;

public class ShopItems {
    public static final ShopItem iron_ingot = new ShopItem(new ItemStack(Material.IRON_INGOT), 16, 10, true, "mining");
    public static final ShopItem lapis_lazuli = new ShopItem(new ItemStack(Material.LAPIS_LAZULI), 16, 4, true, "mining");
    public static final ShopItem gold_ingot = new ShopItem(new ItemStack(Material.GOLD_INGOT), 16, 6, true, "mining");
    public static final ShopItem diamond = new ShopItem(new ItemStack(Material.DIAMOND), 16, 35, true, "mining");
    public static final ShopItem netherite_scrap = new ShopItem(new ItemStack(Material.NETHERITE_SCRAP), 8, 175, true, "mining");
    public static final ShopItem bedrock = new ShopItem(new ItemStack(Material.BEDROCK), 1000, 100000, true, "other");
    public static final ShopItem coal = new ShopItem(new ItemStack(Material.COAL), 16, 2, true, "mining");
    public static final ShopItem bone = new ShopItem(new ItemStack(Material.BONE), 32, 2, true, "mob");
    public static final ShopItem rotten_flesh = new ShopItem(new ItemStack(Material.ROTTEN_FLESH), 32, 2, true, "mob");
    public static final ShopItem string = new ShopItem(new ItemStack(Material.STRING), 32, 2, true, "mob");
    public static final ShopItem emerald = new ShopItem(new ItemStack(Material.EMERALD), 16, 5, true, "mining");
    public static final ShopItem gunpowder = new ShopItem(new ItemStack(Material.GUNPOWDER), 32, 5, true, "mob");
    public static final ShopItem blaze_rod = new ShopItem(new ItemStack(Material.BLAZE_ROD), 16, 2, true, "mob");
    public static final ShopItem spider_eye = new ShopItem(new ItemStack(Material.SPIDER_EYE), 32, 2, true, "mob");
    public static final ShopItem magma_cream = new ShopItem(new ItemStack(Material.MAGMA_CREAM), 16, 4, true, "mob");
    public static final ShopItem ghast_tear = new ShopItem(new ItemStack(Material.GHAST_TEAR), 16, 5, true, "mob");
    public static final ShopItem quartz = new ShopItem(new ItemStack(Material.QUARTZ),16, 3, true, "mining");
    public static final ShopItem ender_pearl = new ShopItem(new ItemStack(Material.ENDER_PEARL), 32, 5, true, "mob");
    public static final ShopItem slime_ball = new ShopItem(new ItemStack(Material.SLIME_BALL), 16, 6, true, "mob");
    public static final ShopItem copper_ingot = new ShopItem(new ItemStack(Material.COPPER_INGOT), 16, 2, true, "mining");
    public static final ShopItem wheat = new ShopItem(new ItemStack(Material.WHEAT), 32, 2, true, "farming");
    public static final ShopItem sugar_cane = new ShopItem(new ItemStack(Material.SUGAR_CANE), 32, 4, true, "farming");
    public static final ShopItem oak_log = new ShopItem(new ItemStack(Material.OAK_LOG), 8, 3, true, "blocks");
    public static final ShopItem acacia_log = new ShopItem(new ItemStack(Material.ACACIA_LOG), 8, 3, true, "blocks");
    public static final ShopItem dark_oak_log = new ShopItem(new ItemStack(Material.DARK_OAK_LOG), 8, 3, true, "blocks");
    public static final ShopItem jungle_log = new ShopItem(new ItemStack(Material.JUNGLE_LOG), 8, 3, true, "blocks");
    public static final ShopItem mangrove_log = new ShopItem(new ItemStack(Material.MANGROVE_LOG), 8, 3, true, "blocks");
    public static final ShopItem spruce_log = new ShopItem(new ItemStack(Material.SPRUCE_LOG), 8, 3, true, "blocks");
    public static final ShopItem birch_log = new ShopItem(new ItemStack(Material.BIRCH_LOG), 8, 3, true, "blocks");
    public static final ShopItem phantom_membrane = new ShopItem(new ItemStack(Material.PHANTOM_MEMBRANE), 16, 2, true, "mob");
    public static final ShopItem golden_apple = new ShopItem(new ItemStack(Material.GOLDEN_APPLE), 8, 9, true, "other");
    public static final ShopItem golden_carrot = new ShopItem(new ItemStack(Material.GOLDEN_CARROT), 8, 9, true, "other");
    public static final ShopItem funky_feather = new ShopItem(item("funky_feather"), 16, 80, true, "other");
    public static final ShopItem obsidian = new ShopItem(new ItemStack(Material.OBSIDIAN), 16, 4, true, "building");
    public static final ShopItem sand = new ShopItem(new ItemStack(Material.SAND), 16, 2, true, "building");
    public static final ShopItem shulker_shell = new ShopItem(new ItemStack(Material.SHULKER_SHELL), 4, 20, true, "other");
    public static final ShopItem stock_of_stonks = new ShopItem(item("stock_of_stonks"), 1, 1, true, "other");
    public static final ShopItem potato = new ShopItem(new ItemStack(Material.POTATO), 32, 2, true, "farming");
    public static final ShopItem beetroot = new ShopItem(new ItemStack(Material.BEETROOT), 32, 2, true, "farming");
    public static final ShopItem melon = new ShopItem(new ItemStack(Material.MELON), 32, 2, true, "farming");
    public static final ShopItem pumpkin = new ShopItem(new ItemStack(Material.PUMPKIN), 32, 2, true, "farming");
    public static final ShopItem carrot = new ShopItem(new ItemStack(Material.CARROT), 32, 2, true, "farming");
    public static final ShopItem repair_kit = new ShopItem(item("repair_kit"), 4, 10, true, "other");
    public static final ShopItem glowstone_dust = new ShopItem(new ItemStack(Material.GLOWSTONE_DUST), 32, 2, true, "mining");
    public static final ShopItem apple = new ShopItem(new ItemStack(Material.APPLE), 32, 4, true, "farming");
    public static final ShopItem arrow = new ShopItem(new ItemStack(Material.ARROW), 64, 1, true, "other");
    public static final ShopItem firework_rocket = new ShopItem(new ItemStack(Material.FIREWORK_ROCKET), 256, 2, true, "other");

    public static final ShopItem lava_bucket = new ShopItem(new ItemStack(Material.LAVA_BUCKET), 10000000, 4, false, "other");
    public static final ShopItem water_bucket = new ShopItem(new ItemStack(Material.WATER_BUCKET), 10000000, 4, false, "other");
    public static final ShopItem dirt = new ShopItem(new ItemStack(Material.DIRT), 10000000, 1, false, "blocks");
    public static final ShopItem cobblestone = new ShopItem(new ItemStack(Material.COBBLESTONE), 10000000, 2, false, "blocks");
}
