package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.ItemStackUtils;
import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;

import static java.lang.Math.max;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.quests.Quest.checkQuest;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.getCustomEnchantments;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.giveItem;
import static mc.analyzers.survivaladdons2.utility.ItemList.item;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.*;

public class OnBlockDropItem implements Listener {
    @EventHandler
    public void onBlockMine(BlockDropItemEvent e){
        Player player = e.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        checkQuest("mine", "block", player, 1);
        BlockState block = e.getBlockState();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if(block.getType().equals(Material.DIAMOND_ORE) || block.getType().equals(Material.DEEPSLATE_DIAMOND_ORE)){
            checkQuest("mine", "diamond", player, 1);
        }


        if(getCustomEnchantments(tool) != null && getCustomEnchantments(tool).containsKey("smelt")){
            HashMap<Material, Material> oreToIngot = new HashMap<>();
            oreToIngot.put(Material.IRON_ORE, Material.IRON_INGOT);
            oreToIngot.put(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT);
            oreToIngot.put(Material.DEEPSLATE_COPPER_ORE, Material.COPPER_INGOT);
            oreToIngot.put(Material.COPPER_ORE, Material.COPPER_INGOT);
            oreToIngot.put(Material.GOLD_ORE, Material.GOLD_INGOT);
            oreToIngot.put(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT);
            if(oreToIngot.containsKey(block.getType())){
                e.setCancelled(true);
                ItemStack toDrop = new ItemStack(oreToIngot.get(block.getType()), player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1);
                block.getWorld().dropItemNaturally(block.getLocation(), toDrop);
            }
        }

        if(block.getType().equals(Material.REDSTONE_ORE) || block.getType().equals(Material.DEEPSLATE_REDSTONE_ORE)){
            //Redstone!
            int increase = 0;
            if(heldItem.getItemMeta() != null){
                if(PDCUtils.has(heldItem, "enchantments") && ItemStackUtils.getCustomEnchantments(heldItem).containsKey("harvest")){
                    int harvestLevel = ItemStackUtils.getCustomEnchantments(heldItem).get("harvest");
                    increase += harvestLevel;
                    int extra = max(harvestLevel / 2, 1);
                    if(percentChance(0.05 * harvestLevel)){
                        increase += extra;
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "LUCKY! " + ChatColor.RESET + ChatColor.GRAY + "Harvested " + ChatColor.RED + "+"
                        + extra + " " + dustIcon + " dust.");
                    }
                }
                if(heldItem.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)){
                    int fortuneLevel = heldItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                    increase += fortuneLevel;
                }
            }
            int amount = 4 + increase;
            ItemStack redstone = new ItemStack(Material.REDSTONE);
            redstone.setAmount(amount);
            checkQuest("mine", "redstone", player, amount);
            e.setCancelled(true);
            player.getWorld().dropItemNaturally(block.getLocation(), redstone);
            boolean dropHarvest = percentChance(0.01);
            if(dropHarvest){
                ItemStack harvestBook = new ItemStack(Material.ENCHANTED_BOOK);
                PDCUtils.set(harvestBook, "enchantments", "harvest/1");
                ItemStackUtils.syncItem(harvestBook);
                player.sendMessage(ChatColor.LIGHT_PURPLE + "RARE DROP! " + ChatColor.GRAY + "Harvest I");
                giveItem(player, harvestBook, 1);
            }
        } else if(block.getBlockData() instanceof Ageable){
            //Is a crop
            if(!(((Ageable) block.getBlockData()).getAge() == ((Ageable) block.getBlockData()).getMaximumAge())){
                return;
            }
            checkQuest("mine", "crops", player, 1);

            if(ItemStackUtils.getCustomEnchantments(heldItem).containsKey("bountiful")){
                int level = ItemStackUtils.getCustomEnchantments(heldItem).get("bountiful");
                if(!percentChance(level*0.01)){
                    return;
                }
                HashMap<ItemStack, String> customDrops = new HashMap<>();

                customDrops.put(item("insta_melon"), "16 Instantaneous Melon");

                customDrops.put(new ItemStack(Material.GOLDEN_APPLE), "Golden Apple");

                ItemStack farm = new ItemStack(Material.FARMLAND);
                farm.setAmount(32);
                customDrops.put(farm, "32 Farmland");

                ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT);
                carrot.setAmount(4);
                customDrops.put(carrot, "4 Golden carrots");

                ItemStack dust = new ItemStack(Material.REDSTONE);
                dust.setAmount(16);
                customDrops.put(dust, "16 Redstone Dust");

                ItemStack rabbit = new ItemStack(Material.RABBIT_FOOT);
                customDrops.put(rabbit, "Rabbit's Foot");

                ItemStack stew = item("sugar_soup");
                customDrops.put(stew, "Sugar Soup");

                ItemStack gold = new ItemStack(Material.GOLD_INGOT);
                gold.setAmount(2);
                customDrops.put(gold, "2 Golden ingots");

                ItemStack berrie = new ItemStack(item("sussy_berry"));
                customDrops.put(berrie, "Suspicious berry");

                ItemStack[] drops = new ItemStack[]{item("insta_melon"), new ItemStack(Material.GOLDEN_APPLE), farm, dust, rabbit, stew, carrot, gold, berrie};
                //Other Items
                ItemStack picked = getRandom(drops);
                System.out.println(picked.getType());
                giveItem(player, picked, picked.getAmount());
                player.sendMessage(ChatColor.LIGHT_PURPLE + "RARE DROP! " + ChatColor.GRAY + customDrops.get(picked));
            }
        }
    }
}
