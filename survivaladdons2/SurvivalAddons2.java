package mc.analyzers.survivaladdons2;
import com.iwebpp.crypto.TweetNaclFast;
import mc.analyzers.survivaladdons2.commands.*;
import mc.analyzers.survivaladdons2.discord.MessageSendEvent;
import mc.analyzers.survivaladdons2.events.*;
import mc.analyzers.survivaladdons2.tasks.CombatTagTick;
import mc.analyzers.survivaladdons2.tasks.RefreshInventory;
import mc.analyzers.survivaladdons2.tasks.SyncAttributes;
import mc.analyzers.survivaladdons2.utility.itemList;
import mc.analyzers.survivaladdons2.utility.pdc;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static mc.analyzers.survivaladdons2.quests.Quests.*;
import static mc.analyzers.survivaladdons2.utility.itemList.item;
import static mc.analyzers.survivaladdons2.utility.utility.syncItem;

public final class SurvivalAddons2 extends JavaPlugin {
    private static SurvivalAddons2 plugin;
    public static String dustIcon = "⏶";
    public static String heartIcon = "❤";

    public static HashMap<String, List<String>> teams = new HashMap<>();
    public static JDA jda;
    public static TextChannel chatChannel;

    @Override
    public void onEnable() {

        plugin = this;

        //Setting up quest
        HashMap<String, ItemStack> hourlyCommon = new HashMap<>();
        hourlyCommon.put("10 dust", new ItemStack(Material.REDSTONE, 10));
        hourlyCommon.put("20 dust", new ItemStack(Material.REDSTONE, 20));
        hourlyCommon.put("15 dust", new ItemStack(Material.REDSTONE, 15));
        hourlyCommon.put("1 diamond", new ItemStack(Material.DIAMOND, 1));
        hourlyCommon.put("25 dust", new ItemStack(Material.REDSTONE, 25));
        hourlyQuest.setCommonRewards(hourlyCommon);

        HashMap<String, ItemStack> hourlyRare = new HashMap<>();
        hourlyRare.put("Repair kit", item("repair_kit"));
        hourlyRare.put("Singularity flask", item("sing_flask"));
        hourlyRare.put("10 golden apples", new ItemStack(Material.GOLDEN_APPLE, 10));
        hourlyRare.put("20 gold ingots", new ItemStack(Material.GOLD_INGOT, 20));
        hourlyQuest.setRareRewards(hourlyRare);

        HashMap<String, ItemStack> dailyCommon = new HashMap<>();
        dailyCommon.put("200 dust", new ItemStack(Material.REDSTONE, 200));
        dailyCommon.put("250 dust", new ItemStack(Material.REDSTONE, 250));
        dailyCommon.put("300 dust", new ItemStack(Material.REDSTONE, 300));
        dailyCommon.put("10 diamonds", new ItemStack(Material.DIAMOND, 10));
        ItemStack feathers = item("funky_feather");
        feathers.setAmount(3);
        dailyCommon.put("3 funky feathers", feathers);
        dailyQuest.setCommonRewards(dailyCommon);

        HashMap<String, ItemStack> dailyRare = new HashMap<>();
        dailyRare.put("Fallen star", item("fallen_star"));
        feathers.setAmount(6);
        dailyRare.put("6 funky feathers", feathers);
        dailyRare.put("1 netherite ingot", new ItemStack(Material.NETHERITE_INGOT, 1));
        dailyQuest.setRareRewards(dailyRare);

        HashMap<String, ItemStack> weeklyCommon = new HashMap<>();
        weeklyCommon.put("850 dust", new ItemStack(Material.REDSTONE, 850));
        weeklyCommon.put("900 dust", new ItemStack(Material.REDSTONE, 900));
        weeklyCommon.put("1K dust", new ItemStack(Material.REDSTONE, 1000));
        weeklyCommon.put("64 diamonds", new ItemStack(Material.DIAMOND, 64));
        weeklyCommon.put("3 netherite ingots", new ItemStack(Material.NETHERITE_INGOT, 3));
        weeklyQuest.setCommonRewards(weeklyCommon);

        HashMap<String, ItemStack> weeklyRare = new HashMap<>();
        ItemStack harvest3 = new ItemStack(Material.ENCHANTED_BOOK);
        pdc.set(harvest3, "enchantments", "harvest/4");
        syncItem(harvest3);
        weeklyRare.put("Harvest IV Book", harvest3);
        weeklyRare.put("1.2K dust", new ItemStack(Material.REDSTONE, 1200));
        weeklyRare.put("1.1K dust", new ItemStack(Material.REDSTONE, 1100));
        weeklyRare.put("1.25K dust", new ItemStack(Material.REDSTONE, 1250));
        weeklyRare.put("4 netherite ingots", new ItemStack(Material.NETHERITE_INGOT, 4));
        weeklyQuest.setRareRewards(weeklyRare);


        try {
            jda = JDABuilder.createDefault("NjA5NzM3MDIyNzkzNDQ5NDg0.GGK2Nv.htvJPiofDJ3-NGLXSyvgsHK-5jqgibD1lWhvGA").enableIntents(GatewayIntent.MESSAGE_CONTENT).build().awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chatChannel = jda.getTextChannelById("1060965324285030523");
        // Plugin startup logic
        jda.addEventListener(new MessageSendEvent());

        System.out.println("Don't forget to play with showDeathMessages false and keepinventory false!");
        getServer().getPluginManager().registerEvents(new OnInventoryOpen(), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryClose(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new onProjectileHit(), this);
        getServer().getPluginManager().registerEvents(new onProjectileFire(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityDeath(), this);
        getServer().getPluginManager().registerEvents(new onBlockMine(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByBlockEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerCraft(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerEat(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new OnItemConsume(), this);
        this.getCommand("getItem").setExecutor(new getItem());
        this.getCommand("getDust").setExecutor(new getDust());
        this.getCommand("dust").setExecutor(new dust());
        this.getCommand("checkItem").setExecutor(new checkItem());
        this.getCommand("withdraw").setExecutor(new withdraw());
        this.getCommand("deposit").setExecutor(new deposit());
        this.getCommand("updateInventory").setExecutor(new updateInventory());
        this.getCommand("rename").setExecutor(new Rename());
        this.getCommand("setlives").setExecutor(new SetLives());
        this.getCommand("view").setExecutor(new View());
        this.getCommand("setEffect").setExecutor(new SetEffect());
        this.getCommand("quests").setExecutor(new Quests());

        for(Player player : this.getServer().getOnlinePlayers()){
            if(!pdc.has(player, "dust")){
                pdc.set(player, "dust", "0");
            }
            if(!pdc.has(player, "inCombat")){
                pdc.set(player, "inCombat", "false/0");
            }
            if(!pdc.has(player, "damageValues")){
                pdc.set(player, "damageValues", "0/0"); //Dealt/Taken
            }
            if(!pdc.has(player, "assist")){
                pdc.set(player, "assist", " "); //Player's names : Analyzers CarryBit etc.
            }
            BukkitTask drainCombatTag = new CombatTagTick(SurvivalAddons2.getPlugin(), player).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
            BukkitTask updateInventory = new SyncAttributes(player, SurvivalAddons2.getPlugin()).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
            BukkitTask refreshPlayer = new RefreshInventory(player).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
        }

        //Custom Crafts
        ItemStack shortbow1 = item("shortbow1");
        ShapedRecipe shortbow1recipe = new ShapedRecipe(new NamespacedKey(this, "shortbow1recipe"), shortbow1);
        shortbow1recipe.shape("ggg", "gCg", "ggg");
        shortbow1recipe.setIngredient('g', Material.GOLD_BLOCK);
        shortbow1recipe.setIngredient('C', new RecipeChoice.ExactChoice(item("shortbow_core")));
        getServer().addRecipe(shortbow1recipe);

        ItemStack shortbow2 = item("shortbow2");
        ShapedRecipe shortbow2recipe = new ShapedRecipe(new NamespacedKey(this, "shortbow2recipe"), shortbow2);
        shortbow2recipe.shape("ggg", "gCg", "ggg");
        shortbow2recipe.setIngredient('g', Material.DIAMOND);
        shortbow2recipe.setIngredient('C', new RecipeChoice.ExactChoice(item("shortbow_core")));
        getServer().addRecipe(shortbow2recipe);

        ItemStack shortbow3 = item("shortbow3");
        ShapedRecipe shortbow3recipe = new ShapedRecipe(new NamespacedKey(this, "shortbow3recipe"), shortbow3);
        shortbow3recipe.shape("ggg", "gCg", "ggg");
        shortbow3recipe.setIngredient('g', Material.NETHERITE_INGOT);
        shortbow3recipe.setIngredient('C', new RecipeChoice.ExactChoice(item("shortbow_core")));
        getServer().addRecipe(shortbow3recipe);

        ItemStack wooden_helmet = item("wooden_helmet");
        ShapedRecipe wooden1recipe = new ShapedRecipe(new NamespacedKey(this, "wooden1recipe"), wooden_helmet);
        wooden1recipe.shape("www", "waw", "aaa");
        wooden1recipe.setIngredient('w', new RecipeChoice.MaterialChoice(Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.STRIPPED_ACACIA_WOOD, Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_DARK_OAK_WOOD, Material.STRIPPED_JUNGLE_WOOD, Material.STRIPPED_MANGROVE_WOOD, Material.STRIPPED_OAK_WOOD, Material.STRIPPED_SPRUCE_WOOD, Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG));
        getServer().addRecipe(wooden1recipe);

        ItemStack wooden_chestplate = item("wooden_chestplate");
        ShapedRecipe wooden2recipe = new ShapedRecipe(new NamespacedKey(this, "wooden2recipe"), wooden_chestplate);
        wooden2recipe.shape("waw", "www", "www");
        wooden2recipe.setIngredient('w', new RecipeChoice.MaterialChoice(Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.STRIPPED_ACACIA_WOOD, Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_DARK_OAK_WOOD, Material.STRIPPED_JUNGLE_WOOD, Material.STRIPPED_MANGROVE_WOOD, Material.STRIPPED_OAK_WOOD, Material.STRIPPED_SPRUCE_WOOD, Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG));
        getServer().addRecipe(wooden2recipe);

        ItemStack wooden_leggings = item("wooden_leggings");
        ShapedRecipe wooden3recipe = new ShapedRecipe(new NamespacedKey(this, "wooden3recipe"), wooden_leggings);
        wooden3recipe.shape("www", "waw", "waw");
        wooden3recipe.setIngredient('w', new RecipeChoice.MaterialChoice(Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.STRIPPED_ACACIA_WOOD, Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_DARK_OAK_WOOD, Material.STRIPPED_JUNGLE_WOOD, Material.STRIPPED_MANGROVE_WOOD, Material.STRIPPED_OAK_WOOD, Material.STRIPPED_SPRUCE_WOOD, Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG));
        getServer().addRecipe(wooden3recipe);

        ItemStack wooden_boots = item("wooden_boots");
        ShapedRecipe wooden4recipe = new ShapedRecipe(new NamespacedKey(this, "wooden4recipe"), wooden_boots);
        wooden4recipe.shape("aaa", "waw", "waw");
        wooden4recipe.setIngredient('w', new RecipeChoice.MaterialChoice(Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.STRIPPED_ACACIA_WOOD, Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_DARK_OAK_WOOD, Material.STRIPPED_JUNGLE_WOOD, Material.STRIPPED_MANGROVE_WOOD, Material.STRIPPED_OAK_WOOD, Material.STRIPPED_SPRUCE_WOOD, Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG));
        getServer().addRecipe(wooden4recipe);

        ItemStack stick = itemList.item("healing_stick");
        pdc.set(stick, "healing", "1");
        ArrayList<String> healinglore = new ArrayList<>();
        healinglore.add(ChatColor.GRAY + "Use this item to heal for");
        healinglore.add(ChatColor.RED + "1" + heartIcon + ChatColor.GRAY + ", costing " + ChatColor.RED + "2" + dustIcon + " dust.");
        ItemMeta stickmeta = stick.getItemMeta();
        stickmeta.setLore(healinglore);
        stick.setItemMeta(stickmeta);
        ShapelessRecipe healingStickRecipe = new ShapelessRecipe(new NamespacedKey(this, "healingstickrecipe"), stick);
        healingStickRecipe.addIngredient(Material.STICK);
        healingStickRecipe.addIngredient(Material.GOLDEN_APPLE);
        getServer().addRecipe(healingStickRecipe);

        ItemStack nightvision = new ItemStack(Material.ENCHANTED_BOOK);
        pdc.set(nightvision, "enchantments", "nightvision/1");
        ItemMeta nightmeta = nightvision.getItemMeta();
        nightmeta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#481AFD") + "Nightvision Book");
        nightvision.setItemMeta(nightmeta);
        ShapelessRecipe nightvisionrecipe = new ShapelessRecipe(new NamespacedKey(this, "nightvisionrecipe"), nightvision);
        nightvisionrecipe.addIngredient(Material.BOOK);
        nightvisionrecipe.addIngredient(Material.GOLDEN_CARROT);
        nightvisionrecipe.addIngredient(Material.FERMENTED_SPIDER_EYE);
        syncItem(nightvision);
        getServer().addRecipe(nightvisionrecipe);

        ItemStack ice_wand = new ItemStack(item("ice_wand"));
        ShapedRecipe icewandrecipe = new ShapedRecipe(new NamespacedKey(this, "icewandrecipe"), ice_wand);
        icewandrecipe.shape("ada", "aba", "asa");
        icewandrecipe.setIngredient('d', Material.DIAMOND);
        icewandrecipe.setIngredient('b', Material.BLUE_ICE);
        icewandrecipe.setIngredient('s', Material.STICK);
        getServer().addRecipe(icewandrecipe);

        ItemStack tomeOfKnowledge = new ItemStack(item("tomeOfKnowledge"));
        ShapedRecipe tomerecipe = new ShapedRecipe(new NamespacedKey(this, "tomerecipe"), tomeOfKnowledge);
        tomerecipe.shape("aAa", "bTb", "bbb");
        tomerecipe.setIngredient('A', Material.AMETHYST_SHARD);
        tomerecipe.setIngredient('b', Material.BOOKSHELF);
        tomerecipe.setIngredient('T', Material.ENCHANTING_TABLE);
        getServer().addRecipe(tomerecipe);

        ItemStack repairKit = new ItemStack(item("repair_kit"));
        ShapedRecipe repairRecipe = new ShapedRecipe(new NamespacedKey(this, "repairrecipe"), repairKit);
        repairRecipe.shape("aIa", "III", "aIa");
        repairRecipe.setIngredient('I', Material.IRON_INGOT);
        getServer().addRecipe(repairRecipe);

        ItemStack feather = new ItemStack(item("funky_feather"));
        ShapelessRecipe featherRecipe = new ShapelessRecipe(new NamespacedKey(this, "featherrecipe"), feather);
        featherRecipe.addIngredient(4, Material.DIAMOND);
        featherRecipe.addIngredient(Material.TOTEM_OF_UNDYING);
        getServer().addRecipe(featherRecipe);

        ItemStack feather_steel = new ItemStack(item("feather_steel"));
        ShapedRecipe feathersteelrecipe = new ShapedRecipe(new NamespacedKey(this, "feathersteelrecipe"), feather_steel);
        feathersteelrecipe.shape("aIa", "IpI", "aIa");
        feathersteelrecipe.setIngredient('I', Material.IRON_BLOCK);
        feathersteelrecipe.setIngredient('p', Material.PHANTOM_MEMBRANE);
        getServer().addRecipe(feathersteelrecipe);

        ItemStack sing_flask = new ItemStack(item("sing_flask"));
        ShapelessRecipe singRecipe = new ShapelessRecipe(new NamespacedKey(this, "singrecipe"), sing_flask);
        singRecipe.addIngredient(Material.POTION);
        singRecipe.addIngredient(Material.ENDER_EYE);
        singRecipe.addIngredient(Material.REDSTONE_BLOCK);
        getServer().addRecipe(singRecipe);
        /*

To add modifer:
- Well do the modif. thingies
- Go into onAnvilClick and add it there also.

To add ench :
- Add custom dust cost (if it's from an ID-item, not ench bookerino)
- Add it in custom anvil crafts (if its from id item) !!! Dont forget to also add the ench book with it !!
- add enchantment wrapper
- add custom enchantment lore
- add enchantment color !!!!! (or its blue)
- also add the code to execute el enchant xdxdxdlol
- ADD enchant in the getById of customEnchantmentWrapper !!!
- ADD ench book as APPLICABLE!!! pls
         */
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        jda.shutdownNow();

        getServer().removeRecipe(new NamespacedKey(this, "shortbow1recipe"));
        getServer().removeRecipe(new NamespacedKey(this, "shortbow2recipe"));
        getServer().removeRecipe(new NamespacedKey(this, "shortbow3recipe"));

        getServer().removeRecipe(new NamespacedKey(this, "wooden1recipe"));
        getServer().removeRecipe(new NamespacedKey(this, "wooden2recipe"));
        getServer().removeRecipe(new NamespacedKey(this, "wooden3recipe"));
        getServer().removeRecipe(new NamespacedKey(this, "wooden4recipe"));

        getServer().removeRecipe(new NamespacedKey(this, "healingstickrecipe"));
        getServer().removeRecipe(new NamespacedKey(this, "nightvisionrecipe"));
        getServer().removeRecipe(new NamespacedKey(this, "icewandrecipe"));
        getServer().removeRecipe(new NamespacedKey(this, "tomerecipe"));
        getServer().removeRecipe(new NamespacedKey(this, "repairrecipe"));
        getServer().removeRecipe(new NamespacedKey(this, "featherrecipe"));
        getServer().removeRecipe(new NamespacedKey(this, "feathersteelrecipe"));
        getServer().removeRecipe(new NamespacedKey(this, "singrecipe"));
    }
    public static SurvivalAddons2 getPlugin(){
        return plugin;
    }
}
