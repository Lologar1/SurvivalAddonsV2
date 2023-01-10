package mc.analyzers.survivaladdons2.tasks;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

import static mc.analyzers.survivaladdons2.utility.AttributeUtils.syncAttributes;

public class SyncAttributes extends BukkitRunnable {
    Player player;
    SurvivalAddons2 plugin;
    public static HashMap<Object, String> materialAttributes = new HashMap<>();
    public SyncAttributes(Player player, SurvivalAddons2 plugin){
        this.player = player;
        this.plugin = plugin;
        //Swords
        materialAttributes.put(Material.WOODEN_SWORD, "damage/4 atkspeed/1.3 critchance/20 critdamage/25");
        materialAttributes.put(Material.GOLDEN_SWORD, "damage/4 atkspeed/1.3 critchance/25 critdamage/30");
        materialAttributes.put(Material.STONE_SWORD, "damage/5 atkspeed/1.3 critchance/25 critdamage/35");
        materialAttributes.put(Material.IRON_SWORD, "damage/6 atkspeed/1.3 critchance/30 critdamage/40");
        materialAttributes.put(Material.DIAMOND_SWORD, "damage/7 atkspeed/1.4 critchance/30 critdamage/45");
        materialAttributes.put(Material.NETHERITE_SWORD, "damage/8 atkspeed/1.5 critchance/35 critdamage/50");
        //Shovels
        materialAttributes.put(Material.WOODEN_SHOVEL, "damage/2 atkspeed/1 critchance/20 critdamage/25");
        materialAttributes.put(Material.GOLDEN_SHOVEL, "damage/2 atkspeed/1 critchance/20 critdamage/25");
        materialAttributes.put(Material.STONE_SHOVEL, "damage/3 atkspeed/1 critchance/20 critdamage/35");
        materialAttributes.put(Material.IRON_SHOVEL, "damage/4 atkspeed/1 critchance/20 critdamage/40");
        materialAttributes.put(Material.DIAMOND_SHOVEL, "damage/5 atkspeed/1 critchance/25 critdamage/45");
        materialAttributes.put(Material.NETHERITE_SHOVEL, "damage/6 atkspeed/1 critchance/25 critdamage/50");
        //Pickaxes
        materialAttributes.put(Material.WOODEN_PICKAXE, "damage/2 atkspeed/1.1 critchance/20 critdamage/25");
        materialAttributes.put(Material.GOLDEN_PICKAXE, "damage/2 atkspeed/1.1 critchance/20 critdamage/25");
        materialAttributes.put(Material.STONE_PICKAXE, "damage/3 atkspeed/1.1 critchance/20 critdamage/35");
        materialAttributes.put(Material.IRON_PICKAXE, "damage/4 atkspeed/1.2 critchance/20 critdamage/40");
        materialAttributes.put(Material.DIAMOND_PICKAXE, "damage/5 atkspeed/1.2 critchance/25 critdamage/45");
        materialAttributes.put(Material.NETHERITE_PICKAXE, "damage/6 atkspeed/1.3 critchance/25 critdamage/50");
        //Axes
        materialAttributes.put(Material.WOODEN_AXE, "damage/5 atkspeed/0.8 critchance/25 critdamage/30");
        materialAttributes.put(Material.GOLDEN_AXE, "damage/5 atkspeed/0.8 critchance/25 critdamage/35");
        materialAttributes.put(Material.STONE_AXE, "damage/6 atkspeed/1 critchance/30 critdamage/40");
        materialAttributes.put(Material.IRON_AXE, "damage/7 atkspeed/0.9 critchance/35 critdamage/45");
        materialAttributes.put(Material.DIAMOND_AXE, "damage/8 atkspeed/1 critchance/40 critdamage/50");
        materialAttributes.put(Material.NETHERITE_AXE, "damage/9 atkspeed/1 critchance/40 critdamage/55");
        //Hoes
        materialAttributes.put(Material.WOODEN_HOE, "damage/1 atkspeed/4 critchance/40 critdamage/30");
        materialAttributes.put(Material.GOLDEN_HOE, "damage/1 atkspeed/4 critchance/40 critdamage/35");
        materialAttributes.put(Material.STONE_HOE, "damage/1 atkspeed/4 critchance/45 critdamage/40");
        materialAttributes.put(Material.IRON_HOE, "damage/1 atkspeed/4 critchance/50 critdamage/45");
        materialAttributes.put(Material.DIAMOND_HOE, "damage/1 atkspeed/4 critchance/55 critdamage/50");
        materialAttributes.put(Material.NETHERITE_HOE, "damage/1 atkspeed/4 critchance/60 critdamage/55");
        //Helmets
        //FOR ARMOR: ALso add, each heart done decreases damage by 4%, but doesn't go below 20% of the armor's reduction.
        //Each 50 def = 4%
        //Leather total def: 250 (20 % reduction) magicdef : 0 (0% reduction) Health : 0
        //Golden total def: 500 (40% reduction)   magicdef : 700 (56% reduction)Health : 4 (2 hearts)
        //Chain total def: 550 (44% reduction)    magicdef : 250 (20% reduction)Health : 2 (1 heart)
        //Iron total def : 700 (56 % reduction)   magicdef : 250 (20% reduction)Health : 4 (2 hearts)
        //Diamond total def : 800 (64 % reduction)magicdef : 500 (40% reduction)Health : 5 (2.5 hearts)
        //Netherite total def : 850 (68 % reduction)magicdef:400 (32% reductionnHealth : 6 (3 hearts)
        //Separation : Helmet = 15%, Chest = 40 %, Legs = 30 %, Boots = 15%
        materialAttributes.put(Material.LEATHER_HELMET, "defense/38 health/0 magicdefense/0");
        materialAttributes.put(Material.GOLDEN_HELMET, "defense/75 health/1 magicdefense/100");
        materialAttributes.put(Material.CHAINMAIL_HELMET, "defense/83 health/0 magicdefense/45");
        materialAttributes.put(Material.IRON_HELMET, "defense/105 health/1 magicdefense/45");
        materialAttributes.put(Material.DIAMOND_HELMET, "defense/120 health/1 magicdefense/65");
        materialAttributes.put(Material.NETHERITE_HELMET, "defense/127 health/1 magicdefense/65");
        //CHESTPLATES
        materialAttributes.put(Material.LEATHER_CHESTPLATE, "defense/100 health/0 magicdefense/0");
        materialAttributes.put(Material.GOLDEN_CHESTPLATE, "defense/200 health/1 magicdefense/280");
        materialAttributes.put(Material.CHAINMAIL_CHESTPLATE, "defense/220 health/1 magicdefense/110");
        materialAttributes.put(Material.IRON_CHESTPLATE, "defense/280 health/1 magicdefense/100");
        materialAttributes.put(Material.DIAMOND_CHESTPLATE, "defense/320 health/1 magicdefense/150");
        materialAttributes.put(Material.NETHERITE_CHESTPLATE, "defense/340 health/1 magicdefense/160");
        //LEGGINGS
        materialAttributes.put(Material.LEATHER_LEGGINGS, "defense/75 health/0 magicdefense/0");
        materialAttributes.put(Material.GOLDEN_LEGGINGS, "defense/150 health/1 magicdefense/210");
        materialAttributes.put(Material.CHAINMAIL_LEGGINGS, "defense/165 health/1 magicdefense/85");
        materialAttributes.put(Material.IRON_LEGGINGS, "defense/210 health/1 magicdefense/85");
        materialAttributes.put(Material.DIAMOND_LEGGINGS, "defense/240 health/1 magicdefense/140");
        materialAttributes.put(Material.NETHERITE_LEGGINGS, "defense/255 health/1 magicdefense/140");
        //BOOTS
        materialAttributes.put(Material.LEATHER_BOOTS, "defense/37 health/0 magicdefense/80");
        materialAttributes.put(Material.GOLDEN_BOOTS, "defense/75 health/1 magicdefense/105");
        materialAttributes.put(Material.CHAINMAIL_BOOTS, "defense/83 health/0 magicdefense/50");
        materialAttributes.put(Material.IRON_BOOTS, "defense/105 health/1 magicdefense/50");
        materialAttributes.put(Material.DIAMOND_BOOTS, "defense/120 health/1 magicdefense/70");
        materialAttributes.put(Material.NETHERITE_BOOTS, "defense/127 health/1 magicdefense/70");
        //RANGED
        materialAttributes.put(Material.BOW, "damage/5 critchance/40 critdamage/40");
        materialAttributes.put(Material.CROSSBOW, "damage/7 critchance/40 critdamage/40");
        materialAttributes.put(Material.TRIDENT, "damage/7 atkspeed/1.2 critchance/50 critdamage/65");
        //OTHER
        materialAttributes.put(Material.ELYTRA, "defense/10 health/4 magicdefense/60");
        materialAttributes.put(Material.TURTLE_HELMET, "defense/100 health/0 magicdefense/75");

        //CUSTOM
        materialAttributes.put("wooden_helmet", "defense/15 health/2 magicdefense/0");
        materialAttributes.put("wooden_chestplate", "defense/40 health/4 magicdefense/0");
        materialAttributes.put("wooden_leggings", "defense/20 health/3 magicdefense/0");
        materialAttributes.put("wooden_boots", "defense/10 health/3 magicdefense/0");

    }

    @Override
    public void run() {
        for(ItemStack item : player.getInventory().getContents()){
            syncAttributes(item);
            if(item != null && item.getType().equals(Material.SHIELD)){
                item.setAmount(0);
                player.sendMessage(ChatColor.RED + "Shields are disabled !");
            }
        }
        //Nightvision enchant
        if(PDCUtils.has(player.getInventory().getHelmet(), "enchantments") && PDCUtils.get(player.getInventory().getHelmet(), "enchantments").contains("nightvision")){
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000, 1));
        }
        if(SurvivalAddons2.getPlugin().getServer().getOnlinePlayers().contains(player)){
            BukkitTask SyncAttribs = new SyncAttributes(player, plugin).runTaskLater(SurvivalAddons2.getPlugin(), 20L);
        }
    }
}
