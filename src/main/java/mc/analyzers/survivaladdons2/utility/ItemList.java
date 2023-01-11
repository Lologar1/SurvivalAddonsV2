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

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.heartIcon;
import static mc.analyzers.survivaladdons2.customenchantments.customEnchantments.teleportEnchantment;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.addCustomEnchantment;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.itemStackBuilder;

public class ItemList {
    public static ItemStack item(String id){
        ItemStack funky_feather = new ItemStack(Material.FEATHER);
        PDCUtils.set(funky_feather, "id", "funky_feather");
        ItemMeta funkymeta = funky_feather.getItemMeta();
        ArrayList<String> funkylore = new ArrayList<>();
        funkylore.add(ChatColor.YELLOW + "Special item");
        funkylore.add(ChatColor.GRAY + "Protects your inventory and position");
        funkylore.add(ChatColor.GRAY + "on death but gets consumed if");
        funkylore.add(ChatColor.GRAY + "in your hotbar or offhand.");
        funkymeta.setLore(funkylore);
        funkymeta.setDisplayName(ChatColor.DARK_AQUA + "Funky Feather");
        funky_feather.setItemMeta(funkymeta);

        ItemStack ender_core = new ItemStack(Material.ENDER_PEARL);
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

        ItemStack healingStick = new ItemStack(Material.STICK);
        PDCUtils.set(healingStick, "id", "healing_stick");
        ItemMeta netheriteBowMeta = healingStick.getItemMeta();
        netheriteBowMeta.setDisplayName(ChatColor.RED + "Healing Stick");
        healingStick.setItemMeta(netheriteBowMeta);

        ItemStack foodStick = new ItemStack(Material.STICK);
        PDCUtils.set(foodStick, "id", "food_stick");
        ItemMeta foodMeta = foodStick.getItemMeta();
        foodMeta.setDisplayName(ChatColor.GOLD + "Food Stick");
        foodStick.setItemMeta(foodMeta);

        ItemStack flingStick = new ItemStack(Material.STICK);
        PDCUtils.set(flingStick, "id", "fling_stick");
        ItemMeta flingMeta = flingStick.getItemMeta();
        flingMeta.setDisplayName(ChatColor.GREEN + "Fling Stick");
        flingStick.setItemMeta(flingMeta);

        ItemStack shortbow_core = new ItemStack(Material.EMERALD);
        PDCUtils.set(shortbow_core, "id", "shortbow_core");
        ItemMeta shortMeta = shortbow_core.getItemMeta();
        shortMeta.setDisplayName(ChatColor.DARK_GRAY + "Shortbow Core");
        ArrayList<String> shortlore = new ArrayList<>();
        shortlore.add(ChatColor.YELLOW + "Modifier");
        shortlore.add(ChatColor.GRAY + "Surround with gold blocks, diamonds");
        shortlore.add(ChatColor.GRAY + "or netherite to " + ChatColor.GREEN + " activate " +ChatColor.GRAY + "the core!");
        shortMeta.setLore(shortlore);
        shortbow_core.setItemMeta(shortMeta);

        ItemStack shortbow1 = new ItemStack(Material.GOLD_INGOT);
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

        ItemStack shortbow2 = new ItemStack(Material.DIAMOND);
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

        ItemStack shortbow3 = new ItemStack(Material.NETHERITE_INGOT);
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

        ItemStack wooden_helmet = new ItemStack(Material.LEATHER_HELMET);
        PDCUtils.set(wooden_helmet, "id", "wooden_helmet");
        LeatherArmorMeta woodenhelmetMeta = (LeatherArmorMeta) wooden_helmet.getItemMeta();
        woodenhelmetMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#5D3111") + "Wooden Helmet");
        woodenhelmetMeta.setColor(org.bukkit.Color.fromRGB(93, 49, 17));
        wooden_helmet.setItemMeta(woodenhelmetMeta);

        ItemStack wooden_chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        PDCUtils.set(wooden_chestplate, "id", "wooden_chestplate");
        LeatherArmorMeta woodenchestplateMeta = (LeatherArmorMeta) wooden_chestplate.getItemMeta();
        woodenchestplateMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#5D3111") + "Wooden Chestplate");
        woodenchestplateMeta.setColor(org.bukkit.Color.fromRGB(93, 49, 17));
        wooden_chestplate.setItemMeta(woodenchestplateMeta);

        ItemStack wooden_leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        PDCUtils.set(wooden_leggings, "id", "wooden_leggings");
        LeatherArmorMeta woodenleggingsMeta = (LeatherArmorMeta) wooden_leggings.getItemMeta();
        woodenleggingsMeta.setColor(org.bukkit.Color.fromRGB(93, 49, 17));
        woodenleggingsMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#5D3111") + "Wooden Leggings");
        wooden_leggings.setItemMeta(woodenleggingsMeta);

        ItemStack wooden_boots = new ItemStack(Material.LEATHER_BOOTS);
        PDCUtils.set(wooden_boots, "id", "wooden_boots");
        LeatherArmorMeta woodenbootsMeta = (LeatherArmorMeta) wooden_boots.getItemMeta();
        woodenbootsMeta.setColor(org.bukkit.Color.fromRGB(93, 49, 17));
        woodenbootsMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#5D3111") + "Wooden Boots");
        wooden_boots.setItemMeta(woodenbootsMeta);

        ItemStack icewand = new ItemStack(Material.SOUL_TORCH);
        PDCUtils.set(icewand, "doNotInteract", "true");
        PDCUtils.set(icewand, "id", "ice_wand");
        ItemMeta icewandmeta = icewand.getItemMeta();
        icewandmeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#68FBFB") + "Ice " + ChatColor.of("#27FBE2") + "Spray " + ChatColor.of("#10FB44") + "Wand");
        ArrayList<String> icewandlore = new ArrayList<>();
        icewandlore.add(ChatColor.YELLOW + "" + ChatColor.YELLOW + "Right-click" + ChatColor.RESET + ChatColor.GRAY + " to summon an " + ChatColor.of("#68FBFB") + "ice explosion");
        icewandlore.add(ChatColor.GRAY + "in front of you, dealing " + ChatColor.AQUA + "7 Magic damage " + ChatColor.GRAY + "and");
        icewandlore.add(ChatColor.GRAY + "applying " + ChatColor.RED + "Slowness I " + ChatColor.GRAY + ("for 10s. (costs ") + ChatColor.RED + "3 " + dustIcon + " dust)");
        icewandlore.add(ChatColor.GRAY + "(10s cooldown)");
        icewandmeta.setLore(icewandlore);
        icewand.setItemMeta(icewandmeta);

        ItemStack tome = new ItemStack(Material.KNOWLEDGE_BOOK);
        PDCUtils.set(tome, "id", "tomeOfKnowledge");
        PDCUtils.set(tome, "doNotInteract", "true");
        ItemMeta tomeMeta = tome.getItemMeta();
        tomeMeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#C73FD3") + "Tome " + ChatColor.of("#BD53FF") + "of " + ChatColor.of("#D3497A") + "Knowledge");
        ArrayList<String> tomeLore = new ArrayList<>();
        tomeLore.add(ChatColor.GRAY + "Right-click to open the " + ChatColor.LIGHT_PURPLE + "Custom");
        tomeLore.add(ChatColor.YELLOW + "Enchanting " + ChatColor.GRAY + "menu !");
        tomeMeta.setLore(tomeLore);
        tome.setItemMeta(tomeMeta);

        ItemStack repairKit = new ItemStack(Material.SHEARS);
        PDCUtils.set(repairKit, "doNotInteract", "true");
        PDCUtils.set(repairKit, "id", "repair_kit");
        ItemMeta repairMeta = repairKit.getItemMeta();
        repairMeta.setDisplayName(ChatColor.DARK_GREEN + "Repair Kit");
        ArrayList<String> repairLore = new ArrayList<>();
        repairLore.add(ChatColor.GRAY + "Right-click to repair all the items in your inventory");
        repairLore.add(ChatColor.RED + "Consumed on use.");
        repairMeta.setLore(repairLore);
        repairKit.setItemMeta(repairMeta);

        ItemStack warden_heart = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
        PDCUtils.set(warden_heart, "doNotInteract", "true");
        PDCUtils.set(warden_heart, "id", "warden_heart");
        ItemMeta wardenMeta = warden_heart.getItemMeta();
        wardenMeta.setDisplayName(ChatColor.DARK_RED + "Warden's Heart");
        ArrayList<String> wardenLore = new ArrayList<>();
        wardenLore.add(ChatColor.YELLOW + "Modifier");
        wardenLore.add(ChatColor.GRAY + "Bonus" + ChatColor.RED + " health " + ChatColor.GRAY + ": " + ChatColor.WHITE + "2");
        wardenLore.add(ChatColor.GRAY + "You can only apply one modifier per item!");
        wardenMeta.setLore(wardenLore);
        warden_heart.setItemMeta(wardenMeta);

        ItemStack feather_steel = new ItemStack(Material.PAPER);
        PDCUtils.set(feather_steel, "id", "feather_steel");
        ItemMeta featherMeta = feather_steel.getItemMeta();
        featherMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Feathersteel Alloy");
        ArrayList<String> featherLore = new ArrayList<>();
        featherLore.add(ChatColor.YELLOW + "Modifier");
        featherLore.add(ChatColor.GRAY + "Bonus" + ChatColor.YELLOW + " attack speed " + ChatColor.GRAY + ": " + ChatColor.WHITE + "0.2");
        featherLore.add(ChatColor.GRAY + "You can only apply one modifier per item!");
        featherMeta.setLore(featherLore);
        feather_steel.setItemMeta(featherMeta);

        ItemStack insta_melon = new ItemStack(Material.MELON_SLICE);
        PDCUtils.set(insta_melon, "id", "insta_melon");
        insta_melon.setAmount(16);
        ItemMeta melonMeta = insta_melon.getItemMeta();
        melonMeta.setDisplayName(ChatColor.GREEN + "Instantaneous " + ChatColor.RED + "Melon");
        insta_melon.setItemMeta(melonMeta);

        ItemStack sugar_soup = new ItemStack(Material.RABBIT_STEW);
        PDCUtils.set(sugar_soup, "id", "sugar_soup");
        ArrayList<String> soupLore = new ArrayList<>();
        soupLore.add(ChatColor.GRAY + "Gives Speed II for 30 seconds");
        ItemMeta sugarMeta = sugar_soup.getItemMeta();
        sugarMeta.setDisplayName(ChatColor.WHITE + "Sugar Soup");
        sugarMeta.setLore(soupLore);
        sugar_soup.setItemMeta(sugarMeta);

        ItemStack singFlask = new ItemStack(Material.POTION);
        PDCUtils.set(singFlask, "id", "sing_flask");
        PDCUtils.set(singFlask, "effects", "singularity/1/60");
        PDCUtils.set(singFlask, "doNotBrew", "true");
        PotionMeta singMeta = (PotionMeta) singFlask.getItemMeta();
        singMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        singMeta.setColor(Color.fromRGB(124,31,74));
        singMeta.setDisplayName(ChatColor.RED + "Flask of " + ChatColor.DARK_PURPLE + "Singularity");
        ArrayList<String> singLore = new ArrayList<>();
        singLore.add(ChatColor.GRAY + "Drinking this potion gives a " + ChatColor.GREEN + "1 minute");
        singLore.add(ChatColor.GRAY + "effect of " + ChatColor.DARK_PURPLE + "singularity " + ChatColor.GRAY + "(all incoming damage");
        singLore.add(ChatColor.GRAY + "is capped at " + ChatColor.RED + "2 "+ heartIcon + ChatColor.GRAY + " but bypasses armor)");
        singMeta.setLore(singLore);
        singFlask.setItemMeta(singMeta);

        ItemStack sussyberry = new ItemStack(Material.SWEET_BERRIES);
        PDCUtils.set(sussyberry, "doNotInteract", "true");
        ArrayList<String> sussylore = new ArrayList<>();
        ItemMeta sussymeta = sussyberry.getItemMeta();
        sussymeta.setDisplayName(ChatColor.YELLOW + "Suspicious Berries");
        sussylore.add(ChatColor.YELLOW + "Potion ingredient");
        sussylore.add(ChatColor.GRAY + "Very suspicious...");
        sussymeta.setLore(sussylore);
        sussyberry.setItemMeta(sussymeta);

        ItemStack fallen_star = new ItemStack(Material.SNOWBALL);
        PDCUtils.set(fallen_star, "doNotInteract", "true");
        PDCUtils.set(fallen_star, "id", "fallen_star");
        ItemMeta fallenMeta = fallen_star.getItemMeta();
        fallenMeta.setDisplayName(ChatColor.YELLOW + "Fallen Star");
        ArrayList<String> fallenLore = new ArrayList<>();
        fallenLore.add(ChatColor.YELLOW + "Modifier");
        fallenLore.add(ChatColor.GRAY + "Bonus" + ChatColor.AQUA + " magic damage " + ChatColor.GRAY + ": " + ChatColor.WHITE + "1");
        fallenLore.add(ChatColor.GRAY + "You can only apply one modifier per item!");
        fallenMeta.setLore(fallenLore);
        fallen_star.setItemMeta(fallenMeta);

        ItemStack stock_of_stonks = ItemStackUtils.itemStackBuilder(ChatColor.DARK_RED + "Stock of Stonks", "id/stock_of_stonks", 1, Material.CYAN_DYE, new String[]{ChatColor.GRAY + "Does nothing! Good investment, though..."});

        ItemStack theitem = null;

        switch (id){
            case "stock_of_stonks":
                theitem = stock_of_stonks;
                break;
            case "fallen_star":
                theitem = fallen_star;
                break;
            case "sussy_berry":
                theitem = sussyberry;
                break;
            case "sing_flask":
                theitem = singFlask;
                break;
            case "feather_steel":
                theitem = feather_steel;
                break;
            case "sugar_soup":
                theitem = sugar_soup;
                break;
            case "insta_melon":
                theitem = insta_melon;
                break;
                //THISISWHERE
            case "funky_feather":
                theitem = funky_feather;
                break;
            case "ender_core":
                theitem = ender_core;
                break;
            case "healing_stick":
                theitem = healingStick;
                break;
            case "food_stick":
                theitem = foodStick;
                break;
            case "fling_stick":
                theitem = flingStick;
                break;
            case "shortbow_core":
                theitem = shortbow_core;
                break;
            case "shortbow1":
                theitem = shortbow1;
                break;
            case "shortbow2":
                theitem = shortbow2;
                break;
            case "shortbow3":
                theitem = shortbow3;
                break;
            case "wooden_helmet":
                theitem = wooden_helmet;
                break;
            case "wooden_chestplate":
                theitem = wooden_chestplate;
                break;
            case "wooden_leggings":
                theitem = wooden_leggings;
                break;
            case "wooden_boots":
                theitem = wooden_boots;
                break;
            case "ice_wand":
                theitem = icewand;
                break;
            case "tomeOfKnowledge":
                theitem = tome;
                break;
            case "repair_kit":
                theitem = repairKit;
                break;
            case "warden_heart":
                theitem = warden_heart;
                break;
        }
        return theitem;
    }

}
