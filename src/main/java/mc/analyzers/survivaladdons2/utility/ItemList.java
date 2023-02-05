package mc.analyzers.survivaladdons2.utility;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.heartIcon;
import static mc.analyzers.survivaladdons2.customenchantments.customEnchantments.teleportEnchantment;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.addCustomEnchantment;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.itemStackBuilder;

public class ItemList {
    private final ItemStack funky_feather = itemStackBuilder(ChatColor.DARK_AQUA + "Funky Feather", "id/funky_feather", 1, Material.FEATHER,
            new String[]{ChatColor.YELLOW + "Special item", ChatColor.GRAY + "Protects inventory and position on", ChatColor.GRAY + "death but gets consumed if",
                    ChatColor.GRAY + "in your hotbar or offhand."});
    private static ItemStack ender_core = null;
    private final ItemStack healing_stick = itemStackBuilder(ChatColor.RED + "Healing Stick", "id/healing_stick healing/1", 1, Material.STICK, new
            String[]{ChatColor.GRAY + "Use this item to heal for", ChatColor.RED + "1" + heartIcon + ChatColor.GRAY + ", costing" + ChatColor.RED + "2" +
            dustIcon + " dust."});

    private final ItemStack food_stick = itemStackBuilder(ChatColor.GOLD + "Food Stick", "id/food_stick food/1", 1, Material.STICK, new
            String[]{ChatColor.GRAY + "Use this item to satiate for", ChatColor.GOLD + "2" + ChatColor.GRAY + "saturation, costing" + ChatColor.RED + "1" +
            dustIcon + " dust."});

    private final ItemStack fling_stick = itemStackBuilder(ChatColor.RED + "Healing Stick", "id/healing_stick", 1, Material.STICK, new
            String[]{ChatColor.RED + "ADMIN ITEM"});
    private static ItemStack shortbow_core = null;
    private static ItemStack shortbow1 = null;
    private static ItemStack shortbow2 = null;
    private static ItemStack shortbow3 = null;
    private static ItemStack wooden_helmet = null;
    private static ItemStack wooden_chestplate = null;
    private static ItemStack wooden_leggings = null;
    private static ItemStack wooden_boots = null;
    private final ItemStack ice_wand = itemStackBuilder(net.md_5.bungee.api.ChatColor.of("#68FBFB") + "Ice " + ChatColor.of("#27FBE2") + "Spray " + ChatColor.of("#10FB44") + "Wand",
            "doNotInteract/true id/ice_wand", 1, Material.SOUL_TORCH, new String[]{ChatColor.YELLOW + "" + ChatColor.YELLOW + "Right-click" + ChatColor.RESET + ChatColor.GRAY + " to summon an " + ChatColor.of("#68FBFB") + "ice explosion",
                    ChatColor.GRAY + "in front of you, dealing " + ChatColor.AQUA + "11 Magic damage " + ChatColor.GRAY + "and", ChatColor.GRAY + "applying " + ChatColor.RED + "Slowness I " + ChatColor.GRAY + ("for 10s. (costs ") + ChatColor.RED + "4 " + dustIcon + " dust)",
                    ChatColor.GRAY + "(3s cooldown)"});

    private static final ItemStack sussy_berry = itemStackBuilder(ChatColor.YELLOW + "Suspicious Berries", "id/sussy_berry doNotInteract/true", 1, Material.SWEET_BERRIES, new String[]{ChatColor.YELLOW + "Potion ingredient", ChatColor.GRAY + "Very suspicious..."});

    private static final ItemStack fallen_star = itemStackBuilder(ChatColor.YELLOW + "Fallen Star", "doNotInteract/true id/fallen_star", 1,
            Material.SNOWBALL, new String[]{ChatColor.YELLOW + "Modifier", ChatColor.GRAY + "Bonus" + ChatColor.AQUA + " magic damage " + ChatColor.GRAY + ": " + ChatColor.WHITE + "1", ChatColor.GRAY + "You can only apply one modifier per item!"});

    private static final ItemStack stock_of_stonks = ItemStackUtils.itemStackBuilder(ChatColor.DARK_RED + "Stock of Stonks", "id/stock_of_stonks", 1, Material.CYAN_DYE, new String[]{ChatColor.GRAY + "Does nothing! Good investment, though..."});
    private static final ItemStack investor_firework = itemStackBuilder(ChatColor.WHITE + "Investor's Firework", "id/investor_firework doNotInteract/true", 1, Material.FIREWORK_ROCKET, new String[]{ChatColor.GRAY + "Use of this firework does not consume it", ChatColor.GRAY + "but instead buys one off the shop for market value"});
    private static final ItemStack power_stick = itemStackBuilder(ChatColor.DARK_RED + "Power Stick", "id/power_stick power/150", 1, Material.STICK, new String[]{
            ChatColor.GRAY + "Use this item to empower your next", ChatColor.GRAY + "strike for " + ChatColor.RED + "+150% " + ChatColor.GRAY + "damage",
            ChatColor.GRAY + "(costs " + ChatColor.RED + "2" + dustIcon + " dust" + ChatColor.GRAY + ")"});
    private static final ItemStack god_stick = itemStackBuilder(ChatColor.of("#C7CCFB") + "Stick of God", "id/god_stick", 1, Material.STICK, new String[]{
            ChatColor.GRAY + "Item Abilities " + ChatColor.DARK_GRAY + "(RIGHT CLICK)" + ChatColor.GRAY + ":", ChatColor.GRAY + "• " + ChatColor.GRAY + "Heal " + ChatColor.RED + "1.5 " + heartIcon,
            ChatColor.GRAY + "• Empower next strike for " + ChatColor.DARK_RED + "+150% damage", ChatColor.GRAY + "• Satiate " + ChatColor.YELLOW + "4 food " + ChatColor.GRAY + "and" + ChatColor.GOLD + " 3 saturation",
            ChatColor.GRAY + "• Costs " + ChatColor.RED + "5" + dustIcon + " dust" + ChatColor.GRAY + " (3s cooldown)"}
    );
    private static final ItemStack tomeOfKnowledge = itemStackBuilder(net.md_5.bungee.api.ChatColor.of("#C73FD3") + "Tome " + ChatColor.of("#BD53FF") + "of " + ChatColor.of("#D3497A") + "Knowledge",
            "id/tomeOfKnowledge doNotInteract/true", 1, Material.KNOWLEDGE_BOOK, new String[]{ChatColor.GRAY + "Right-click to open the " + ChatColor.LIGHT_PURPLE + "Custom", ChatColor.YELLOW + "Enchanting " + ChatColor.GRAY + "menu !"});

    private static final ItemStack repair_kit = itemStackBuilder(ChatColor.DARK_GREEN + "Repair Kit", "id/repair_kit", 1, Material.SHEARS, new String[]{
            ChatColor.GRAY + "Right-click to repair all the items in your inventory", ChatColor.RED + "Consumed on use."
    });
    private static final ItemStack warden_heart = itemStackBuilder(ChatColor.DARK_RED + "Warden's Heart", "id/warden_heart doNotInteract/true",
            1, Material.RED_GLAZED_TERRACOTTA, new String[]{ChatColor.YELLOW + "Modifier", ChatColor.GRAY + "Bonus" + ChatColor.RED + " health " + ChatColor.GRAY + ": " + ChatColor.WHITE + "2",
                    ChatColor.GRAY + "You can only apply one modifier per item!"});

    private static final ItemStack feather_steel = itemStackBuilder(ChatColor.WHITE + "" + ChatColor.BOLD + "Feathersteel Alloy", "id/feather_steel",
            1, Material.PAPER, new String[]{ChatColor.YELLOW + "Modifier", ChatColor.GRAY + "Bonus" + ChatColor.YELLOW + " attack speed " + ChatColor.GRAY + ": " + ChatColor.WHITE + "0.2",
                    ChatColor.GRAY + "You can only apply one modifier per item!"});

    private static final ItemStack insta_melon = itemStackBuilder(ChatColor.GREEN + "Instantaneous " + ChatColor.RED + "Melon", "id/insta_melon", 16,
            Material.MELON_SLICE, new String[]{});

    private static final ItemStack sugar_soup = itemStackBuilder(ChatColor.WHITE + "Sugar Soup", "id/sugar_soup", 1, Material.RABBIT_STEW, new String[]{
            ChatColor.GRAY + "Gives Speed II for 30 seconds"});
    private static ItemStack sing_flask = null;
    private static ItemStack redstone_bundle = itemStackBuilder(ChatColor.AQUA + "Empty Redstone Bundle", "id/redstone_bundle doNotInteract/true", 1
    , Material.MINECART, new String[]{ChatColor.GRAY + "Right-click to fill with all " + ChatColor.RED + dustIcon + " dust", ChatColor.GRAY + "from your inventory!", ChatColor.GRAY + "Mined dust directly goes inside!"});
    public ItemList(){
        ender_core = new ItemStack(Material.ENDER_PEARL);
        PDCUtils.set(ender_core, "id", "ender_core");
        PDCUtils.set(ender_core, "doNotInteract", "true");
        addCustomEnchantment(ender_core, teleportEnchantment, 1);
        ItemMeta endermeta = ender_core.getItemMeta();
        endermeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ender " + ChatColor.DARK_PURPLE + "Core");
        ArrayList<String> enderlore = new ArrayList<>();
        enderlore.add(ChatColor.YELLOW + "Enchantment");
        enderlore.add(ChatColor.GRAY + "Combine in an anvil with a");
        enderlore.add(ChatColor.GRAY + "bow or enchanted book to add the ");
        enderlore.add(ChatColor.LIGHT_PURPLE + "Teleport I " + ChatColor.GRAY + "enchantment.");
        endermeta.setLore(enderlore);
        ender_core.setItemMeta(endermeta);

        shortbow_core = new ItemStack(Material.EMERALD);
        PDCUtils.set(shortbow_core, "id", "shortbow_core");
        ItemMeta shortMeta = shortbow_core.getItemMeta();
        shortMeta.setDisplayName(ChatColor.DARK_GRAY + "Shortbow Core");
        ArrayList<String> shortlore = new ArrayList<>();
        shortlore.add(ChatColor.YELLOW + "Modifier");
        shortlore.add(ChatColor.GRAY + "Surround with gold blocks, diamonds");
        shortlore.add(ChatColor.GRAY + "or netherite to " + ChatColor.GREEN + " activate " +ChatColor.GRAY + "the core!");
        shortMeta.setLore(shortlore);
        shortbow_core.setItemMeta(shortMeta);

        shortbow1 = new ItemStack(Material.GOLD_INGOT);
        PDCUtils.set(shortbow1, "enchantments", "shortbow/1");
        PDCUtils.set(shortbow1, "id", "activatedShortbowCore");
        ItemMeta shortMeta1 = shortbow1.getItemMeta();
        shortMeta1.setDisplayName(ChatColor.DARK_GRAY + "Shortbow Core " + ChatColor.GOLD + "- Tier I");
        ArrayList<String> shortlore1 = new ArrayList<>();
        shortlore1.add(ChatColor.YELLOW + "Modifier");
        shortlore1.add(ChatColor.GRAY + "Combine with a bow to convert");
        shortlore1.add(ChatColor.GRAY + "it into a " + ChatColor.GOLD + "shortbow " + ChatColor.GRAY + "(instantly shoots!)");
        shortMeta1.setLore(shortlore1);
        shortbow1.setItemMeta(shortMeta1);

        shortbow2 = new ItemStack(Material.DIAMOND);
        PDCUtils.set(shortbow2, "enchantments", "shortbow/2");
        PDCUtils.set(shortbow2, "id", "activatedShortbowCore");
        ItemMeta shortMeta2 = shortbow2.getItemMeta();
        shortMeta2.setDisplayName(ChatColor.DARK_GRAY + "Shortbow Core " + ChatColor.AQUA + "- Tier II");
        ArrayList<String> shortlore2 = new ArrayList<>();
        shortlore2.add(ChatColor.YELLOW + "Modifier");
        shortlore2.add(ChatColor.GRAY + "Combine with a bow to convert");
        shortlore2.add(ChatColor.GRAY + "it into a " + ChatColor.AQUA + "shortbow " + ChatColor.GRAY + "(instantly shoots!)");
        shortMeta2.setLore(shortlore2);
        shortbow2.setItemMeta(shortMeta2);

        shortbow3 = new ItemStack(Material.NETHERITE_INGOT);
        PDCUtils.set(shortbow3, "enchantments", "shortbow/3");
        PDCUtils.set(shortbow3, "id", "activatedShortbowCore");
        ItemMeta shortMeta3 = shortbow3.getItemMeta();
        shortMeta3.setDisplayName(ChatColor.DARK_GRAY + "Shortbow Core " + ChatColor.GREEN + "- Tier III");
        ArrayList<String> shortlore3 = new ArrayList<>();
        shortlore3.add(ChatColor.YELLOW + "Modifier");
        shortlore3.add(ChatColor.GRAY + "Combine with a bow to convert");
        shortlore3.add(ChatColor.GRAY + "it into a " + ChatColor.GREEN + "shortbow " + ChatColor.GRAY + "(instantly shoots!)");
        shortMeta3.setLore(shortlore3);
        shortbow3.setItemMeta(shortMeta3);

        wooden_helmet = new ItemStack(Material.LEATHER_HELMET);
        PDCUtils.set(wooden_helmet, "id", "wooden_helmet");
        LeatherArmorMeta woodenhelmetMeta = (LeatherArmorMeta) wooden_helmet.getItemMeta();
        woodenhelmetMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#5D3111") + "Wooden Helmet");
        woodenhelmetMeta.setColor(org.bukkit.Color.fromRGB(93, 49, 17));
        wooden_helmet.setItemMeta(woodenhelmetMeta);

        wooden_chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        PDCUtils.set(wooden_chestplate, "id", "wooden_chestplate");
        LeatherArmorMeta woodenchestplateMeta = (LeatherArmorMeta) wooden_chestplate.getItemMeta();
        woodenchestplateMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#5D3111") + "Wooden Chestplate");
        woodenchestplateMeta.setColor(org.bukkit.Color.fromRGB(93, 49, 17));
        wooden_chestplate.setItemMeta(woodenchestplateMeta);

        wooden_leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        PDCUtils.set(wooden_leggings, "id", "wooden_leggings");
        LeatherArmorMeta woodenleggingsMeta = (LeatherArmorMeta) wooden_leggings.getItemMeta();
        woodenleggingsMeta.setColor(org.bukkit.Color.fromRGB(93, 49, 17));
        woodenleggingsMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#5D3111") + "Wooden Leggings");
        wooden_leggings.setItemMeta(woodenleggingsMeta);

        wooden_boots = new ItemStack(Material.LEATHER_BOOTS);
        PDCUtils.set(wooden_boots, "id", "wooden_boots");
        LeatherArmorMeta woodenbootsMeta = (LeatherArmorMeta) wooden_boots.getItemMeta();
        woodenbootsMeta.setColor(org.bukkit.Color.fromRGB(93, 49, 17));
        woodenbootsMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#5D3111") + "Wooden Boots");
        wooden_boots.setItemMeta(woodenbootsMeta);

        sing_flask = new ItemStack(Material.POTION);
        PDCUtils.set(sing_flask, "id", "sing_flask");
        PDCUtils.set(sing_flask, "effects", "singularity/1/120");
        PDCUtils.set(sing_flask, "doNotBrew", "true");
        PotionMeta singMeta = (PotionMeta) sing_flask.getItemMeta();
        singMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        singMeta.setColor(Color.fromRGB(124,31,74));
        singMeta.setDisplayName(ChatColor.RED + "Flask of" + ChatColor.DARK_PURPLE + " Singularity");
        ArrayList<String> singLore = new ArrayList<>();
        singLore.add(ChatColor.GRAY + "Drinking this potion gives a " + ChatColor.GREEN + "2 minute");
        singLore.add(ChatColor.GRAY + "effect of " + ChatColor.DARK_PURPLE + "singularity " + ChatColor.GRAY + "(all incoming damage");
        singLore.add(ChatColor.GRAY + "is capped at " + ChatColor.RED + "2 "+ heartIcon + ChatColor.GRAY + " but bypasses armor)");
        singMeta.setLore(singLore);
        sing_flask.setItemMeta(singMeta);
    }

    public static ItemStack item(String id){
        try{
            return new ItemStack( (ItemStack) ItemList.class.getDeclaredField(id).get(new ItemList()) );
        }catch (Exception e){
            System.out.println("No such item! " + id);
            return itemStackBuilder(ChatColor.DARK_RED + "Programmer's Nightmare", "id/programmer_nightmare", 1, Material.RED_DYE, new String[]{ChatColor.RED + "NoSuchItemException: " + ChatColor.GRAY + id, ChatColor.GRAY + "Please contact Analyzers (Laurent#8545)"});
        }
    }

}
