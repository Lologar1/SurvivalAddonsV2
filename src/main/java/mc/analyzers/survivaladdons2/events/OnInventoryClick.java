package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.quests.Quest;
import mc.analyzers.survivaladdons2.quests.Quests;
import mc.analyzers.survivaladdons2.shop.ShopItem;
import mc.analyzers.survivaladdons2.shop.ShopItems;
import mc.analyzers.survivaladdons2.tasks.SyncAttributes;
import mc.analyzers.survivaladdons2.utility.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.*;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.quests.Quest.checkQuest;
import static mc.analyzers.survivaladdons2.shop.ShopItem.getByMaterial;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.mergeModifiers;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.setAttribute;
import static mc.analyzers.survivaladdons2.customenchantments.customEnchantmentsWrapper.*;
import static mc.analyzers.survivaladdons2.utility.ItemStackUtils.itemStackBuilder;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.giveItem;
import static mc.analyzers.survivaladdons2.utility.ItemList.item;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.*;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void onAnvilClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Anvil - Combine & Modify")) {

            ItemStack anvilGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta anvilGlassMeta = anvilGlass.getItemMeta();
            anvilGlassMeta.setDisplayName(ChatColor.BLACK + "");
            anvilGlass.setItemMeta(anvilGlassMeta);

            Inventory anvil = e.getInventory();


            if (!(e.getSlot() == 20 || e.getSlot() == 22)) {
                if (e.getClickedInventory() != null && !(e.getClickedInventory()).getType().equals(InventoryType.PLAYER)) {
                    e.setCancelled(true);
                }
            }

            ItemStack item1 = anvil.getItem(20);
            ItemStack item2 = anvil.getItem(22);

            int slot = e.getSlot();
            ClickType clickType = e.getClick();
            ItemStack cursorItem = e.getCursor();
            int button = e.getHotbarButton();

            if (e.getAction().equals(InventoryAction.HOTBAR_SWAP) || e.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD)) { // hot-b
                if (slot == 20) {
                    item1 = player.getInventory().getItem(button);
                }
                if (slot == 22) {
                    item2 = player.getInventory().getItem(button);
                }
            }
            if(clickType.isShiftClick() && e.getClickedInventory().getType().equals(InventoryType.CHEST) && (slot == 20 || slot == 22)){
                if(slot == 20){
                    item1 = null;
                }else{
                    item2 = null;
                }
            }
            if (clickType.isShiftClick() && e.getClickedInventory().getType().equals(InventoryType.PLAYER)) { // Shift clicking
                if (anvil.firstEmpty() == 20) {
                    item1 = e.getCurrentItem();
                }
                if (anvil.firstEmpty() == 22) {
                    item2 = e.getCurrentItem();
                }
            } else if ((slot == 20 || slot == 22) && clickType.equals(ClickType.LEFT)) { //normal left
                if (slot == 20) {
                    item1 = cursorItem;
                }
                if (slot == 22) {
                    item2 = cursorItem;
                }
            }
            if (item1 == null) {
                ItemStack combinedItem = new ItemStack(Material.BARRIER);
                ItemMeta cm = combinedItem.getItemMeta();
                cm.setDisplayName(ChatColor.RED + "Put two valid items to combine!");
                combinedItem.setItemMeta(cm);
                anvil.setItem(24, combinedItem);
                return;
            }
            if (item2 == null) {
                ItemStack combinedItem = new ItemStack(Material.BARRIER);
                ItemMeta cm = combinedItem.getItemMeta();
                cm.setDisplayName(ChatColor.RED + "Put two valid items to combine!");
                combinedItem.setItemMeta(cm);
                anvil.setItem(24, combinedItem);
                return;
            }

            ItemStack combinedItem = new ItemStack(item1);

            int dustCost = 0;
            //Custom handling : ench & modif.
            boolean canCraft = false;
            String absoluteID = ItemStackUtils.getAbsoluteId(item1);
            if (customAnvilCrafts.crafts.containsKey(absoluteID)) {
                if (Arrays.asList(customAnvilCrafts.crafts.get(absoluteID)).contains(ItemStackUtils.getAbsoluteId(item2))){
                    //Match found - > only for attributs though, customEnch items : put them on directly
                    canCraft = true;
                }
            }
            Map<Enchantment, Integer> item1ench = item1.getEnchantments();
            Map<Enchantment, Integer> item2ench = item2.getEnchantments();

            if (item1.getType().equals(Material.ENCHANTED_BOOK)) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item1.getItemMeta();
                item1ench = meta.getStoredEnchants();
            }
            if (item2.getType().equals(Material.ENCHANTED_BOOK)) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item2.getItemMeta();
                item2ench = meta.getStoredEnchants();
            }

            combinedItem = ItemStackUtils.combineAllEnchantments(item1, item2, item1ench, item2ench);

            //re-evaluate dust cost
            if (item1.getType().equals(Material.ENCHANTED_BOOK) || item2.getType().equals(Material.ENCHANTED_BOOK)) {
                if (!(PDCUtils.has(item1, "id") || PDCUtils.has(item2, "id"))) {
                    dustCost += 10;
                }
            }
            if ((!(PDCUtils.has(item1, "id") || PDCUtils.has(item2, "id"))) && item1.getType().equals(item2.getType()) && !item1.getType().equals(Material.ENCHANTED_BOOK) && !item1.getType().equals(Material.AIR)) {
                dustCost += 15;
            }
            dustCost = dustCost + (ItemStackUtils.getCustomEnchantments(combinedItem).size() * 7 + combinedItem.getEnchantments().size() * 4);
            if (PDCUtils.has(item1, "id")) {
                dustCost += ItemStackUtils.getCustomDustCost(PDCUtils.get(item1, "id"));
            }
            if (PDCUtils.has(item2, "id")) {
                dustCost += ItemStackUtils.getCustomDustCost(PDCUtils.get(item2, "id"));
            }
            ItemStack price = new ItemStack(Material.REDSTONE);
            ItemMeta pricemeta = price.getItemMeta();
            pricemeta.setDisplayName(ChatColor.GOLD + "Total cost: " + ChatColor.RED + dustCost + dustIcon + " dust");
            price.setItemMeta(pricemeta);
            PDCUtils.set(price, "price", String.valueOf(dustCost));
            anvil.setItem(4, price);

            //Attribute modifiers
            if(PDCUtils.has(item2, "id") && mc.analyzers.survivaladdons2.modifiers.Modifier.getById(PDCUtils.get(item2, "id")) != null){
                mergeModifiers(combinedItem, item2);
                canCraft = true;
            }

            ItemStackUtils.syncItem(combinedItem);

            if(combinedItem.equals(item1)){
                combinedItem = new ItemStack(Material.BARRIER);
                ItemMeta cm = combinedItem.getItemMeta();
                cm.setDisplayName(ChatColor.RED + "Put two valid items to combine!");
                combinedItem.setItemMeta(cm);
                anvil.setItem(24, combinedItem);
                return;
            }
            ItemStack bar = new ItemStack(Material.BARRIER);
            ItemMeta cm = bar.getItemMeta();
            cm.setDisplayName(ChatColor.RED + "Put two valid items to combine!");
            bar.setItemMeta(cm);
            if (((item1.getType().getMaxDurability() != 0 && item2.getType().getMaxDurability() != 0) && item1.getType().equals(item2.getType())) || (item1.getType().equals(Material.ENCHANTED_BOOK) && item2.getType().equals(Material.ENCHANTED_BOOK))
                    || (PDCUtils.has(item1, "id") && PDCUtils.has(item2, "id")) || (PDCUtils.has(item2, "id") && item1.getType().getMaxDurability() != 0) ||
                    (item1.getType().getMaxDurability() != 0 && item2.getType().equals(Material.ENCHANTED_BOOK)) || (PDCUtils.has(item1, "id") && item2.getType().getMaxDurability() != 0)
                    || canCraft || (PDCUtils.has(item1, "id") && item2.getType().equals(Material.ENCHANTED_BOOK)) || (PDCUtils.has(item2, "id") && item1.getType().equals(Material.ENCHANTED_BOOK))) {
                if (((item1.getType().getMaxDurability() != 0 && item2.getType().getMaxDurability() != 0) && item1.getType().equals(item2.getType())) || (item1.getType().equals(Material.ENCHANTED_BOOK) && item2.getType().equals(Material.ENCHANTED_BOOK)
                        || (item1.getType().getMaxDurability() != 0 && item2.getType().equals(Material.ENCHANTED_BOOK)))) {
                    anvil.setItem(24, combinedItem);
                } else if (canCraft) {
                    //Anvil operations with custom things.
                    anvil.setItem(24, combinedItem);
                }

            } else {
                anvil.setItem(24, bar);
            }

            if(anvil.getItem(24).getType().equals(Material.BARRIER)){
                return;
            }

            if (e.getSlot() == 13) {
                if(anvil.getItem(20) == null || anvil.getItem(22) == null){
                    anvil.setItem(24, new ItemStack(Material.AIR));
                }
                if (e.getClickedInventory().getType().equals(InventoryType.CHEST) && e.getInventory().getItem(24) != null) {
                    if(anvil.getItem(24).getType().equals(Material.BARRIER)){
                        player.sendMessage(ChatColor.RED + "Provide appropriate ingredients!");
                        return;
                    }
                    if (Integer.parseInt(PDCUtils.get(player, "dust")) >= dustCost && anvil.getItem(24) != null) {
                        PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - dustCost));
                        player.sendMessage(ChatColor.GREEN + "Successfully combined for " + ChatColor.RED + dustCost + dustIcon + " dust.");
                        checkQuest("combine", "item", player, 1);
                        anvil.getItem(20).setAmount(anvil.getItem(20).getAmount() - 1);
                        anvil.getItem(22).setAmount(anvil.getItem(22).getAmount() - 1);
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
                        giveItem(player, anvil.getItem(24), anvil.getItem(24).getAmount());
                        anvil.setItem(24, bar);
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have enough " + dustIcon + " dust!");
                    }
                }
            }
        }else if(e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Grindstone")){
            if(!(e.getSlot() == 13 || e.getClickedInventory().getType().equals(InventoryType.PLAYER))){
                e.setCancelled(true);
            }
            if(e.getSlot() == 4){
                if(e.getInventory().getItem(13) == null){
                    player.sendMessage(ChatColor.RED + "Provide an item!");
                    return;
                }
                ItemStack grindedItem = new ItemStack(e.getInventory().getItem(13));
                PDCUtils.set(grindedItem, "enchantments", "");
                String[] rawAttributes = SyncAttributes.materialAttributes.get(grindedItem.getType()).split(" ");
                PDCUtils.set(grindedItem, "attributes", "");
                for(String rawAttribute : rawAttributes){
                    String attribute = rawAttribute.split("/")[0];
                    double potency = Double.parseDouble(rawAttribute.split("/")[1]);
                    setAttribute(grindedItem, attribute, potency);
                }
                PDCUtils.set(grindedItem, "attributed", "true");
                for(Enchantment enchantment : grindedItem.getEnchantments().keySet()){
                    grindedItem.removeEnchantment(enchantment);
                }
                ItemStackUtils.syncItem(grindedItem);
                e.getInventory().setItem(13, grindedItem);
                player.playSound(player.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 1, 1);
            }
        }else if(e.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "Tome of Knowledge")) {
            Inventory tome = e.getInventory();
            if (!(e.getSlot() == 11 || e.getClickedInventory().getType().equals(InventoryType.PLAYER))) {
                e.setCancelled(true);
            }
            if (e.getSlot() == 15 && !e.getInventory().getType().equals(InventoryType.PLAYER)) {
                //Reroll
                if (!(Integer.parseInt(PDCUtils.get(player, "dust")) >= 2)) {
                    player.sendMessage(ChatColor.RED + "Not enough dust!");
                    return;
                }
                int random = (int) (Math.random() * 100);
                PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - 2));
                /*
                Common (50%) 25 dust, 10 lapis
                Uncommon (20%) 40 dust, 20 lapis
                Rare (15%) 65 dust, 30 lapis
                Epic  (10%) 85 dust, 50 lapis
                Legendary (5%) 115 dust, 64 lapis
                 */
                String current = "";
                if(!tome.getItem(22).getType().equals(Material.BARRIER)){
                    current = PDCUtils.get(tome.getItem(22), "enchantments").split("/")[0];
                }

                if (random <= 50) {
                    //Common
                    String pickedEnchant;
                    do {
                        pickedEnchant = getRandom(commonEnchantments);
                    } while (pickedEnchant.equals(current));
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    PDCUtils.set(book, "enchantments", pickedEnchant + "/1 ");
                    PDCUtils.set(player, "oldTomeEnchantment", pickedEnchant);
                    ItemStackUtils.syncItem(book);

                    ItemStack dustCost = new ItemStack(Material.REDSTONE);
                    ItemMeta dustMeta = dustCost.getItemMeta();
                    dustMeta.setDisplayName(ChatColor.GOLD + "Dust cost : " + ChatColor.RED + "25 " + dustIcon + " dust.");
                    dustCost.setItemMeta(dustMeta);

                    tome.setItem(4, dustCost);
                    tome.setItem(22, book);
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "TOME! " + ChatColor.WHITE + getById(pickedEnchant).getPrettyname());
                    player.playSound(player.getLocation(), Sound.ITEM_AXE_STRIP, 1, 1);
                } else if (random <= 70) {
                    //Uncommon
                    String pickedEnchant;
                    do {
                        pickedEnchant = getRandom(uncommonEnchantments);
                    } while (pickedEnchant.equals(current));
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    PDCUtils.set(book, "enchantments", pickedEnchant + "/1 ");
                    PDCUtils.set(player, "oldTomeEnchantment", pickedEnchant);
                    ItemStackUtils.syncItem(book);

                    ItemStack dustCost = new ItemStack(Material.REDSTONE);
                    ItemMeta dustMeta = dustCost.getItemMeta();
                    dustMeta.setDisplayName(ChatColor.GOLD + "Dust cost : " + ChatColor.RED + "40 " + dustIcon + " dust.");
                    dustCost.setItemMeta(dustMeta);

                    tome.setItem(4, dustCost);
                    tome.setItem(22, book);
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "TOME! " + ChatColor.GREEN + getById(pickedEnchant).getPrettyname());
                    player.playSound(player.getLocation(), Sound.ITEM_AXE_STRIP, 1, 2);
                } else if (random <= 85) {
                    //Rare
                    String pickedEnchant;
                    do {
                        pickedEnchant = getRandom(rareEnchantments);
                    } while (pickedEnchant.equals(current));
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    PDCUtils.set(book, "enchantments", pickedEnchant + "/1 ");
                    PDCUtils.set(player, "oldTomeEnchantment", pickedEnchant);
                    ItemStackUtils.syncItem(book);

                    ItemStack dustCost = new ItemStack(Material.REDSTONE);
                    ItemMeta dustMeta = dustCost.getItemMeta();
                    dustMeta.setDisplayName(ChatColor.GOLD + "Dust cost : " + ChatColor.RED + "65 " + dustIcon + " dust.");
                    dustCost.setItemMeta(dustMeta);

                    tome.setItem(4, dustCost);
                    tome.setItem(22, book);
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD +  "TOME! " + ChatColor.BLUE + getById(pickedEnchant).getPrettyname());
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                } else if (random <= 95) {
                    //Epic
                    String pickedEnchant;
                    do {
                        pickedEnchant = getRandom(epicEnchantments);
                    } while (pickedEnchant.equals(current));
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    PDCUtils.set(book, "enchantments", pickedEnchant + "/1 ");
                    PDCUtils.set(player, "oldTomeEnchantment", pickedEnchant);
                    ItemStackUtils.syncItem(book);

                    ItemStack dustCost = new ItemStack(Material.REDSTONE);
                    ItemMeta dustMeta = dustCost.getItemMeta();
                    dustMeta.setDisplayName(ChatColor.GOLD + "Dust cost : " + ChatColor.RED + "85 " + dustIcon + " dust.");
                    dustCost.setItemMeta(dustMeta);

                    tome.setItem(4, dustCost);
                    tome.setItem(22, book);
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "TOME! " + ChatColor.DARK_PURPLE + getById(pickedEnchant).getPrettyname());
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
                } else if (random <= 100) {
                    //Legendary
                    String pickedEnchant;
                    do {
                        pickedEnchant = getRandom(legendaryEnchantments);
                    } while (pickedEnchant.equals(current));
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    PDCUtils.set(book, "enchantments", pickedEnchant + "/1 ");
                    PDCUtils.set(player, "oldTomeEnchantment", pickedEnchant);
                    ItemStackUtils.syncItem(book);

                    ItemStack dustCost = new ItemStack(Material.REDSTONE);
                    ItemMeta dustMeta = dustCost.getItemMeta();
                    dustMeta.setDisplayName(ChatColor.GOLD + "Dust cost : " + ChatColor.RED + "115 " + dustIcon + " dust.");
                    dustCost.setItemMeta(dustMeta);

                    tome.setItem(4, dustCost);
                    tome.setItem(22, book);
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "TOME! " + ChatColor.GOLD + getById(pickedEnchant).getPrettyname());
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                }
            } else if (e.getSlot() == 22 && !e.getInventory().getType().equals(InventoryType.PLAYER)) {
                //Buying the enchantment
                if(tome.getItem(22) == null || tome.getItem(22).getType().equals(Material.BARRIER)){
                    return;
                }
                int lapisAmount = 10;
                int dustCost = 25;
                String enchantment = PDCUtils.get(tome.getItem(22), "enchantments").split("/")[0];
                if (Arrays.asList(uncommonEnchantments).contains(enchantment)){
                    dustCost = 40;
                    lapisAmount = 20;
                }else if (Arrays.asList(rareEnchantments).contains(enchantment)){
                    dustCost = 65;
                    lapisAmount = 30;
                }else if (Arrays.asList(epicEnchantments).contains(enchantment)){
                    dustCost = 85;
                    lapisAmount = 50;
                }else if (Arrays.asList(legendaryEnchantments).contains(enchantment)){
                    dustCost = 115;
                    lapisAmount = 64;
                }

                if(Integer.parseInt(PDCUtils.get(player, "dust")) >= dustCost ){
                    if(tome.getItem(11) == null){
                        player.sendMessage(ChatColor.RED + "Provide at least " + lapisAmount + " lapis lazuli!");
                        return;
                    }
                    if(!(tome.getItem(11).getAmount() >= lapisAmount)){
                        player.sendMessage(ChatColor.RED + "Provide at least " + lapisAmount + " lapis lazuli!");
                        return;
                    }
                    ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI);
                    lapis.setAmount(tome.getItem(11).getAmount() - lapisAmount);
                    tome.setItem(11, lapis);
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);

                    PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - dustCost));
                    giveItem(player, tome.getItem(22), 1);
                    ItemStack combinedItem = new ItemStack(Material.BARRIER);
                    ItemMeta cm = combinedItem.getItemMeta();
                    cm.setDisplayName(ChatColor.RED + "Reroll to get a custom enchantment!");
                    combinedItem.setItemMeta(cm);
                    tome.setItem(22, combinedItem);
                    checkQuest("get", "custom enchant", player, 1);
                    PDCUtils.set(player, "oldTomeEnchantment", "none");
                }else {
                    player.sendMessage(ChatColor.RED + "Not enough dust!");
                }
            }
        }else if(e.getView().getTitle().equals(ChatColor.GRAY + "Viewing")){
            e.setCancelled(true);
        }else if(e.getView().getTitle().equals(ChatColor.DARK_GREEN + "Brewing")){
            if(!(e.getSlot() == 2 || e.getSlot() == 6 || e.getClickedInventory().getType().equals(InventoryType.PLAYER))){
                e.setCancelled(true);
            }
            if(!(e.getSlot() == 4)){
                return;
            }
            Inventory stand = e.getInventory();
            ItemStack ingredient = stand.getItem(2);
            ItemStack recipient = stand.getItem(6);
            if(ingredient == null || recipient == null){
                player.sendMessage(ChatColor.RED + "Provide adequate items !");
                return;
            }
            if(!recipient.hasItemMeta() || !(recipient.getItemMeta() instanceof PotionMeta)){
                player.sendMessage(ChatColor.RED + "Provide an adequate recipient !");
                return;
            }
            PotionMeta recipientMeta = (PotionMeta) recipient.getItemMeta();
            List<PotionEffect> effects = recipientMeta.getCustomEffects();
            if(PDCUtils.has(recipient, "doNotBrew")){
                player.sendMessage(ChatColor.RED + "Cannot add effects to this item!");
                return;
            }

            ItemStack[] possibleIngredients = new ItemStack[]{new ItemStack(Material.SUGAR), new ItemStack(Material.BLAZE_POWDER),
            new ItemStack(Material.GLISTERING_MELON_SLICE), new ItemStack(Material.GLOWSTONE_DUST), new ItemStack(Material.REDSTONE), new ItemStack(Material.GOLDEN_CARROT),
            new ItemStack(Material.FERMENTED_SPIDER_EYE), new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.GHAST_TEAR), new ItemStack(Material.DRAGON_BREATH),
            new ItemStack(Material.GUNPOWDER), new ItemStack(Material.SPIDER_EYE), new ItemStack(Material.PHANTOM_MEMBRANE), new ItemStack(Material.PUFFERFISH),
            new ItemStack(Material.GOLDEN_APPLE), new ItemStack(Material.MAGMA_CREAM), new ItemStack(Material.POISONOUS_POTATO), new ItemStack(Material.GOLD_BLOCK), item("sussy_berry")};
            ItemStack getter = new ItemStack(ingredient);
            getter.setAmount(1);
            if(!Arrays.asList(possibleIngredients).contains(getter)){
                player.sendMessage(ChatColor.RED + "Provide an adequate ingredient !");
                return;
            }
            if(ingredient.getType().equals(Material.REDSTONE) || ingredient.getType().equals(Material.GLOWSTONE_DUST) || ingredient.getType().equals(Material.GUNPOWDER) || ingredient.getType().equals(Material.DRAGON_BREATH) || ingredient.getType().equals(Material.FERMENTED_SPIDER_EYE)){
                if(effects.isEmpty() && !PDCUtils.has(recipient, "effects")){
                    player.sendMessage(ChatColor.RED + "Recipient needs to have a potion effect to apply this modifier !");
                    return;
                }
                PotionEffect effect = effects.get(0);
                PotionEffect newEffect = new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier());
                boolean upgraded = false;
                boolean corrupt = false;
                boolean isCustom = false;
                if(PDCUtils.has(recipient, "id")){
                    isCustom=true;
                }
                String name = recipientMeta.getDisplayName();
                switch (ingredient.getType()){
                    case REDSTONE:
                        if(PDCUtils.has(recipient, "upgraded")){
                            player.sendMessage(ChatColor.RED + "Already upgraded once !");
                            return;
                        }
                        if(isCustom){
                            String rawEffect = PDCUtils.get(recipient, "effects");
                            String effectName = rawEffect.split("/")[0];
                            int potency = Integer.parseInt(rawEffect.split("/")[1]);
                            int duration = Integer.parseInt(rawEffect.split("/")[2]);
                            duration = duration*2;
                            PDCUtils.set(recipient, "effects", effectName + "/" + potency + "/" + duration);
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(ChatColor.BLUE + effectName.substring(0, 1).toUpperCase() + effectName.substring(1) + " " + roman(potency) + " (" + (int) duration/60 + ":" + (int) duration%60 +")");
                            recipientMeta.setLore(lore);
                            if(!effects.isEmpty()){
                                newEffect = new PotionEffect(effect.getType(), effect.getDuration()*2, effect.getAmplifier());
                            }
                        }else{
                            newEffect = new PotionEffect(effect.getType(), effect.getDuration()*2 ,effect.getAmplifier());
                        }
                        name = ChatColor.BLUE + "Lenghtened " + ChatColor.RESET + name;
                        upgraded = true;
                        break;
                    case GLOWSTONE_DUST:
                        if(PDCUtils.has(recipient, "upgraded")){
                            player.sendMessage(ChatColor.RED + "Already upgraded once !");
                            return;
                        }
                        if(isCustom){
                            String rawEffect = PDCUtils.get(recipient, "effects");
                            String effectName = rawEffect.split("/")[0];
                            int potency = Integer.parseInt(rawEffect.split("/")[1]);
                            int duration = Integer.parseInt(rawEffect.split("/")[2]);
                            potency = potency + 1;
                            duration = duration/2;
                            PDCUtils.set(recipient, "effects", effectName + "/" + potency + "/" + duration);
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(ChatColor.BLUE + effectName.substring(0, 1).toUpperCase() + effectName.substring(1) + " " + roman(potency) + " (" + (int) duration/60 + ":" + (int) duration%60 +")");
                            recipientMeta.setLore(lore);
                            if(!effects.isEmpty()){
                                newEffect = new PotionEffect(effect.getType(), effect.getDuration()/2, effect.getAmplifier()+1);
                            }
                        }else{
                            newEffect = new PotionEffect(effect.getType(), effect.getDuration()/2, effect.getAmplifier()+1);
                        }
                        upgraded = true;
                        name = ChatColor.AQUA + "Empowered " + ChatColor.RESET + name;
                        break;
                    case DRAGON_BREATH:
                        if(isCustom){
                            player.sendMessage(ChatColor.RED + "Can't apply this modifier to this item!");
                        }
                        recipient.setType(Material.LINGERING_POTION);
                        name = ChatColor.WHITE + "Lingering " + ChatColor.RESET + name;
                        break;
                    case GUNPOWDER:
                        if(isCustom){
                            player.sendMessage(ChatColor.RED + "Can't apply this modifier to this item!");
                        }
                        recipient.setType(Material.SPLASH_POTION);
                        name = ChatColor.WHITE + "Splash " + ChatColor.RESET + name;
                        break;
                    case FERMENTED_SPIDER_EYE:
                        if(isCustom){
                            player.sendMessage(ChatColor.RED + "Can't apply this modifier to this item!");
                        }
                        if(PDCUtils.has(recipient, "corrupt")){
                            player.sendMessage(ChatColor.RED + "Already corrupted !");
                            return;
                        }
                        corrupt = true;
                        HashMap<PotionEffectType, PotionEffectType> corruption = new HashMap<>();

                        corruption.put(PotionEffectType.SPEED, PotionEffectType.SLOW);
                        corruption.put(PotionEffectType.JUMP, PotionEffectType.SLOW);
                        corruption.put(PotionEffectType.HEAL, PotionEffectType.HARM);
                        corruption.put(PotionEffectType.POISON, PotionEffectType.HARM);
                        corruption.put(PotionEffectType.NIGHT_VISION, PotionEffectType.INVISIBILITY);
                        if(!corruption.containsKey(effect.getType())){
                            player.sendMessage(ChatColor.RED + "Can't corrupt this effect!");
                            break;
                        }
                        int duration = effect.getDuration();
                        if(corruption.get(effect.getType()).equals(PotionEffectType.HARM)){
                            duration = 1;
                        }
                        if(corruption.get(effect.getType()).equals(PotionEffectType.SLOW)){
                            duration = 1800;
                        }
                        newEffect = new PotionEffect(corruption.get(effect.getType()), duration, effect.getAmplifier());
                        String vanillaEffectName = corruption.get(effect.getType()).getName();
                        name = ChatColor.WHITE + vanillaEffectName.substring(0, 1).toUpperCase() + vanillaEffectName.substring(1) + " potion";
                        break;
                }
                recipientMeta.clearCustomEffects();
                recipientMeta.addCustomEffect(newEffect, true);
                recipientMeta.setDisplayName(name);
                recipient.setItemMeta(recipientMeta);
                if(upgraded){
                    PDCUtils.set(recipient, "upgraded", "true/1");
                }
                if(corrupt){
                    PDCUtils.set(recipient, "corrupt", "true/1");
                }
                player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
                player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "BREW! " + ChatColor.RESET + ChatColor.GRAY + "Potion successful!");
                ingredient.setAmount(ingredient.getAmount() - 1);
                return;
            }
            if(!effects.isEmpty()){
                player.sendMessage(ChatColor.RED + "Can't have more than 1 potion effect per potion !");
                return;
            }
            PotionEffect newEffect = new PotionEffect(PotionEffectType.SPEED, 6000, 0);
            String name = "EL POTION DEL NOTHING";
            boolean custom = false;
            String id = "";
            String customEffect = "";
            ArrayList<String> customlore = new ArrayList<>();
            switch (ingredient.getType()){
                case SUGAR:
                    newEffect = new PotionEffect(PotionEffectType.SPEED, 3600, 0);
                    name = ChatColor.WHITE + "Speed ";
                    break;
                case RABBIT_FOOT:
                    newEffect = new PotionEffect(PotionEffectType.JUMP, 3600, 0);
                    name = ChatColor.WHITE + "Jump boost ";
                    break;
                case BLAZE_POWDER:
                    newEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 0);
                    name = ChatColor.WHITE + "Strenght ";
                    break;
                case GLISTERING_MELON_SLICE:
                    newEffect = new PotionEffect(PotionEffectType.HEAL, 1, 0);
                    name = ChatColor.WHITE + "Health ";
                    break;
                case SPIDER_EYE:
                    newEffect = new PotionEffect(PotionEffectType.POISON, 900, 0);
                    name = ChatColor.WHITE + "Poison ";
                    break;
                case GHAST_TEAR:
                    newEffect = new PotionEffect(PotionEffectType.REGENERATION, 3600, 0);
                    name = ChatColor.WHITE + "Regen ";
                    break;
                case MAGMA_CREAM:
                    newEffect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 0);
                    name = ChatColor.WHITE + "Fire res ";
                    break;
                case PUFFERFISH:
                    newEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, 3600, 0);
                    name = ChatColor.WHITE + "Water breathing ";
                    break;
                case GOLDEN_CARROT:
                    newEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, 3600, 0);
                    name = ChatColor.WHITE + "Night vision ";
                    break;
                case PHANTOM_MEMBRANE:
                    newEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 3600, 0);
                    name = ChatColor.WHITE + "Slow fall ";
                    break;
                case POISONOUS_POTATO:
                    newEffect = new PotionEffect(PotionEffectType.WEAKNESS, 1800, 0);
                    name = ChatColor.WHITE + "Weakness ";
                    break;
                case GOLDEN_APPLE:
                    newEffect = new PotionEffect(PotionEffectType.HEALTH_BOOST, 3600, 0);
                    name = ChatColor.WHITE + "Health boost ";
                    break;
                case GOLD_BLOCK:
                    name = ChatColor.DARK_AQUA + "Golden ";
                    custom = true;
                    id = "golden_potion";
                    customEffect = "golden/1/300";
                    customlore.add(ChatColor.GOLD + "Golden I " + ChatColor.GRAY + "(5:00)");
                    customlore.add(ChatColor.GRAY + "Provides immunity to " + ChatColor.GOLD + "billionaire " + ChatColor.GRAY + "for 5 minutes.");
                    break;
                case SWEET_BERRIES:
                    //sussyberry
                    name = ChatColor.DARK_AQUA + "Sussy ";
                    PotionEffectType[] possible = new PotionEffectType[]{PotionEffectType.CONFUSION, PotionEffectType.GLOWING, PotionEffectType.LEVITATION, PotionEffectType.DOLPHINS_GRACE, PotionEffectType.ABSORPTION, PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.HERO_OF_THE_VILLAGE, PotionEffectType.FAST_DIGGING, PotionEffectType.WEAKNESS, PotionEffectType.BAD_OMEN, PotionEffectType.WITHER, PotionEffectType.SLOW, PotionEffectType.SATURATION, PotionEffectType.SPEED};
                    PotionEffectType picked = possible[new Random().nextInt(possible.length)];
                    newEffect = new PotionEffect(picked, 1200, 2);
                    player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "SUSSY! " + ChatColor.GRAY + "Got " + picked.getName().substring(0, 1).toUpperCase() + picked.getName().substring(1));
                    break;
            }
            name = name + "potion";
            checkQuest("brew", "potion", player, 1);
            player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
            player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "BREW! " + ChatColor.RESET + ChatColor.GRAY + "Potion successful!");
            ingredient.setAmount(ingredient.getAmount() - 1);
            recipientMeta.setDisplayName(name);
            if(custom){
                recipientMeta.setLore(customlore);
                recipientMeta.addCustomEffect(newEffect, true);
                recipient.setItemMeta(recipientMeta);
                PDCUtils.set(recipient, "id", id);
                PDCUtils.set(recipient, "effects", customEffect);
            }else {
                recipientMeta.addCustomEffect(newEffect, true);
                recipient.setItemMeta(recipientMeta);
            }
        }else if(e.getView().getTitle().equals(ChatColor.GRAY + "Quests")){
            e.setCancelled(true);
            if(e.getSlot() == 11 || e.getSlot() == 13 || e.getSlot() == 15) {
                //Hourly
                boolean alreadyDone = true;
                String questType = "hourly";
                if (e.getSlot() == 13) {
                    questType = "daily";
                } else if(e.getSlot() == 15){
                    questType = "weekly";
                }
                String questLast = questType + "Last";
                String activeQuestQuest = "active" + capFirst(questType) + "Quest";
                String deactivatedQuestQuest = "deactivated" + capFirst(questType) + "Quest";
                String questQuestProgress = questType + "QuestProgress";
                Quest hourly = null;
                try {
                    hourly = (Quest) Quests.class.getField(questType + "Quest").get(Quests.class);
                } catch (Exception ignore) {
                }

                if (!PDCUtils.get(player, questLast).equals("active")) {
                    long lastDid = Long.parseLong(PDCUtils.get(player, questLast));
                    if (lastDid + hourly.getDelay() <= System.currentTimeMillis()) {
                        alreadyDone = false;
                    }
                }
                if (!PDCUtils.get(player, activeQuestQuest).equals("null") || alreadyDone) {
                    player.sendMessage(ChatColor.RED + "Can't do a quest at this time!");
                    return;
                }
                if (!PDCUtils.get(player, "currentQuestType").equals("none")) {
                    String lastType = PDCUtils.get(player, "currentQuestType");
                    PDCUtils.set(player, lastType + "Last", "0");
                    PDCUtils.set(player, "active" + capFirst(lastType) + "Quest", "null");
                }
                String picked = hourly.getRandomQuestId();
                if (!PDCUtils.get(player, deactivatedQuestQuest).equals("null")) {
                    picked = PDCUtils.get(player, deactivatedQuestQuest);
                }
                int savedProgress = Integer.parseInt(PDCUtils.get(player, questQuestProgress).split("/")[0]);
                ItemStack hourlyQuest = itemStackBuilder(hourly.getPrettyNameFromId(picked), null, 1, Material.WRITABLE_BOOK,
                        new String[]{ChatColor.GRAY + "Progress: " + savedProgress + "/" + picked.split("/")[1]});

                PDCUtils.set(player, questQuestProgress, savedProgress + "/" + picked.split("/")[1]);
                PDCUtils.set(player, activeQuestQuest, picked);
                PDCUtils.set(player, questLast, "active");
                PDCUtils.set(player, "currentQuestType", questType);
                PDCUtils.set(player, deactivatedQuestQuest, picked);

                e.getInventory().setItem(e.getSlot(), hourlyQuest);
            }
        }else if(e.getView().getTitle().equals(ChatColor.GRAY + "Shop")){
            e.setCancelled(true);
            Inventory shop = e.getClickedInventory();
            if(!(shop.getItem(e.getSlot()) == null)){
                if(shop.getType().equals(InventoryType.PLAYER)){
                    ItemStack sold = e.getCurrentItem();
                    if(getByMaterial(sold)==null || !getByMaterial(sold).isCanSell()){
                        player.sendMessage(ChatColor.RED + "Can't sell this to the shop!");
                        return;
                    }
                    ShopItem shopItem = getByMaterial(sold);
                    shop.setItem(e.getSlot(), new ItemStack(Material.AIR));
                    int price = shopItem.sellItem(sold.getAmount());
                    PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) + price));
                    player.sendMessage(ChatColor.GREEN + "Sold " + sold.getAmount() + " " + capFirst(ItemStackUtils.getAbsoluteId(sold).replace('_', ' ').toLowerCase(Locale.ROOT)) + " for " + ChatColor.RED + price + " " + dustIcon + " dust.");
                    shop = e.getInventory();
                }else{
                    if(shop.getItem(0).getType().equals(Material.GRAY_STAINED_GLASS_PANE)){
                        //Still selecting
                        if(e.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)){
                            return;
                        }
                        String category;
                        switch (e.getCurrentItem().getType()){
                            case GRASS_BLOCK:
                                category = "blocks";
                                break;
                            case IRON_PICKAXE:
                                category = "mining";
                                break;
                            case ROTTEN_FLESH:
                                category = "mob";
                                break;
                            case WHEAT:
                                category = "farming";
                                break;
                            default:
                                category = "other";
                                break;
                        }
                        shop.clear();
                        for (Field added : ShopItems.class.getFields()){
                            try {
                                ShopItem toAdd = ((ShopItem) added.get(ShopItems.class));
                                if(toAdd.getCategory().toLowerCase(Locale.ROOT).equals(category)){
                                    shop.addItem(toAdd.getActualItem());
                                }
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        }
                        for(ItemStack addedItem : shop.getContents()){
                            if(addedItem == null){
                                continue;
                            }
                            addedItem.setAmount(1);
                            ItemMeta meta = addedItem.getItemMeta();
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add(ChatColor.GRAY + "Click to buy for " + ChatColor.RED + getByMaterial(addedItem).getBiasedPrice() + " " + dustIcon + " dust");
                            if(!getByMaterial(addedItem).isCanSell()){
                                lore.add(ChatColor.RED + "Can't sell back!");
                            }
                            meta.setLore(lore);
                            addedItem.setItemMeta(meta);
                        }
                        return;
                    }
                    //Bought an item!!!
                    int qty = 1;
                    if(e.getClick().equals(ClickType.RIGHT)){
                        qty = 64;
                    }
                    ItemStack clicked = e.getCurrentItem();
                    ShopItem bought;
                    bought = getByMaterial(clicked);


                    int price = bought.buyItem(qty);
                    if(Integer.parseInt(PDCUtils.get(player, "dust")) < price){
                        player.sendMessage(ChatColor.RED + "Not enough dust!");
                        bought.sellItem(qty);
                        return;
                    }
                    PDCUtils.set(player, "dust", String.valueOf(Integer.parseInt(PDCUtils.get(player, "dust")) - price));
                    giveItem(player, bought.getActualItem(), qty);
                    player.sendMessage(ChatColor.GREEN + "Bought " + qty + " " + capFirst(ItemStackUtils.getAbsoluteId(clicked).replace('_', ' ').toLowerCase(Locale.ROOT)) + " for " + ChatColor.RED + price + " " + dustIcon + " dust.");
                }
                for(ItemStack addedItem : shop.getContents()){
                    if(addedItem == null){
                        continue;
                    }
                    if(addedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)){
                        break;
                    }
                    addedItem.setAmount(1);
                    ItemMeta meta = addedItem.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Click to buy for " + ChatColor.RED + getByMaterial(addedItem).getBiasedPrice() + " " + dustIcon + " dust");
                    if(!getByMaterial(addedItem).isCanSell()){
                        lore.add(ChatColor.RED + "Can't sell back!");
                    }
                    meta.setLore(lore);
                    addedItem.setItemMeta(meta);
                }
            }
        }
    }
}
