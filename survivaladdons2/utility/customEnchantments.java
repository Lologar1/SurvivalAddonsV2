package mc.analyzers.survivaladdons2.utility;

import org.bukkit.enchantments.Enchantment;

public class customEnchantments {
    //CustomEnchantments conflicts are ID strings.
    public static final customEnchantmentsWrapper teleportEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Teleport" , "teleport", 3, new String[]{"explosive", "ftts"}, new String[]{"bow", "crossbow", "enchanted_book"}, true); //new customEnchantmentWrapper[]{*things*}
    public static final customEnchantmentsWrapper harvestEnchantment = new customEnchantmentsWrapper(new Enchantment[]{Enchantment.SILK_TOUCH}, "Harvest", "harvest", 10, new String[]{}, new String[]{"enchanted_book", "wooden_pickaxe", "golden_pickaxe", "stone_pickaxe", "iron_pickaxe", "netherite_pickaxe", "diamond_pickaxe"}, true);
    public static final customEnchantmentsWrapper nightVisionEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Night Vision", "nightvision", 1, new String[]{}, new String[]{"enchanted_book", "leather_helmet", "golden_helmet", "chainmail_helmet", "iron_helmet", "diamond_helmet", "netherite_helmet", "wooden_helmet", "turtle_helmet"}, true);
    public static final customEnchantmentsWrapper shortBowEnchantment = new customEnchantmentsWrapper(new Enchantment[]{Enchantment.ARROW_DAMAGE}, "Shortbow", "shortbow", 3, new String[]{"sorcery"}, new String[]{"bow", "crossbow", "enchanted_book"}, false);

    public static final customEnchantmentsWrapper lightningEnchantment = new customEnchantmentsWrapper(new Enchantment[]{Enchantment.DAMAGE_ALL}, "Lightning", "lightning", 5, new String[]{}, new String[]{"wooden_sword", "golden_sword", "stone_sword", "iron_sword", "netherite_sword", "diamond_sword", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper explosiveEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Explosive", "explosive", 2, new String[]{"teleport", "ftts"}, new String[]{"bow", "crossbow", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper fttsEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Faster than their shadows", "ftts", 3, new String[]{"teleport", "explosive"}, new String[]{"bow", "crossbow", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper dodgeEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Dodge", "dodge", 3, new String[]{}, new String[]{"leather_leggings", "wooden_leggings", "chainmail_leggings", "golden_leggings", "iron_leggings", "diamond_leggings", "netherite_leggings", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper experienceEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Experience", "experience", 3, new String[]{}, new String[]{"wooden_sword", "golden_sword", "stone_sword", "iron_sword", "netherite_sword", "diamond_sword", "wooden_axe", "golden_axe", "stone_axe", "iron_axe", "netherite_axe", "diamond_axe", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper sorceryEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Sorcery", "sorcery", 3, new String[]{}, new String[]{"bow", "crossbow", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper billionaireEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Billionaire", "billionaire", 3, new String[]{}, new String[]{"wooden_sword", "golden_sword", "stone_sword", "iron_sword", "netherite_sword", "diamond_sword", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper lifestealEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Lifesteal", "lifesteal", 3, new String[]{}, new String[]{"wooden_sword", "golden_sword", "stone_sword", "iron_sword", "netherite_sword", "diamond_sword", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper netheriteStompEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Netherite stomp", "netheritestomp", 4, new String[]{}, new String[]{"wooden_sword", "golden_sword", "stone_sword", "iron_sword", "netherite_sword", "diamond_sword", "wooden_axe", "golden_axe", "stone_axe", "iron_axe", "netherite_axe", "diamond_axe", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper glasscannonEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Glass cannon", "glasscannon", 1, new String[]{"shortbow"}, new String[]{"bow", "crossbow", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper venomEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Venom", "venom", 2, new String[]{}, new String[]{"wooden_sword", "golden_sword", "stone_sword", "iron_sword", "netherite_sword", "diamond_sword", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper bountifulEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Bountiful", "bountiful", 3, new String[]{}, new String[]{"wooden_hoe", "golden_hoe", "stone_hoe", "iron_hoe", "diamond_hoe", "netherite_hoe", "farmer_hoe", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper criticalEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Critical", "critical", 3, new String[]{}, new String[]{"wooden_sword", "golden_sword", "stone_sword", "iron_sword", "netherite_sword", "diamond_sword", "wooden_axe", "golden_axe", "stone_axe", "iron_axe", "netherite_axe", "diamond_axe", "enchanted_book", "bow", "crossbow"}, true);
    public static final customEnchantmentsWrapper parasiteEnchantment = new customEnchantmentsWrapper(new Enchantment[]{Enchantment.ARROW_DAMAGE}, "Parasite", "parasite", 3, new String[]{"glasscannon", "sorcery"}, new String[]{"bow", "crossbow", "enchanted_book"}, true);
    public static final customEnchantmentsWrapper gambleEnchantment = new customEnchantmentsWrapper(new Enchantment[]{}, "Gamble!", "gamble", 3, new String[]{}, new String[]{"wooden_sword", "golden_sword", "stone_sword", "iron_sword", "netherite_sword", "diamond_sword", "wooden_axe", "golden_axe", "stone_axe", "iron_axe", "netherite_axe", "diamond_axe", "enchanted_book"}, true);
}
