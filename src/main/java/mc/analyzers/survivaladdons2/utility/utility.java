package mc.analyzers.survivaladdons2.utility;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.math3.util.Precision;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.type.Observer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static com.google.common.primitives.Ints.max;
import static java.lang.Math.min;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getAttributeName;
import static mc.analyzers.survivaladdons2.utility.AttributeUtils.getAttributes;
import static mc.analyzers.survivaladdons2.utility.customEnchantmentLore.getThelore;
import static mc.analyzers.survivaladdons2.utility.customEnchantmentsWrapper.getById;

public class utility {
    public static Boolean percentChance(double chance) {
        return Math.random() <= chance;
    }

    public static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    public static ItemStack getRandom(ItemStack[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    public static ItemStack combineAllEnchantments(ItemStack item1, ItemStack item2, Map<Enchantment, Integer> vanillaEnchants1, Map<Enchantment, Integer> vanillaEnchants2){
        ItemStack combinedItem = new ItemStack(item1);

        HashMap<Enchantment, Integer> finalVanillaEnchants = new HashMap<>();
        HashMap<String, Integer> finalCustomEnchants = new HashMap<>();

        for(Enchantment vanillaEnchantment2 : vanillaEnchants2.keySet()){
            if(vanillaEnchants1.containsKey(vanillaEnchantment2)){ //Check for vanilla combining
                if(Objects.equals(vanillaEnchants1.get(vanillaEnchantment2), vanillaEnchants2.get(vanillaEnchantment2))){
                    finalVanillaEnchants.put(vanillaEnchantment2, min((vanillaEnchants1.get(vanillaEnchantment2) + 1), vanillaEnchantment2.getMaxLevel()));
                }else{
                    finalVanillaEnchants.put(vanillaEnchantment2, max(vanillaEnchants1.get(vanillaEnchantment2), vanillaEnchants2.get(vanillaEnchantment2)));
                }
            }else{
                finalVanillaEnchants.put(vanillaEnchantment2, vanillaEnchants2.get(vanillaEnchantment2));
            }
        }
        //Now for first item onto itself, checking for custom enchantment conflicts
        for(Enchantment vanillaEnchantment1 : vanillaEnchants1.keySet()){
            if(finalVanillaEnchants.containsKey(vanillaEnchantment1)){
                continue;
            }
            boolean conflictWithCustom = false; //addEnchant method filters applicability ! yay!
            boolean conflictWithVanilla = false;
            for(String customEnchantment2 : getCustomEnchantments(item2).keySet()){
                if(Arrays.asList(getById(customEnchantment2).getVanillaConflicts()).contains(vanillaEnchantment1)){
                    conflictWithCustom = true;
                }
            }

            for(Enchantment vanillaEnchantment2 : vanillaEnchants2.keySet()){
                if(vanillaEnchantment2.conflictsWith(vanillaEnchantment1)){
                    conflictWithVanilla = true;
                }
            }
            if(!conflictWithCustom && !conflictWithVanilla){
                finalVanillaEnchants.put(vanillaEnchantment1, vanillaEnchants1.get(vanillaEnchantment1));
            }
        }
        //Now for custom enchantments

        for(String customEnchantment2 : getCustomEnchantments(item2).keySet()){
            int level2 = getCustomEnchantments(item2).get(customEnchantment2);
            if(getCustomEnchantments(item1).containsKey(customEnchantment2) && getById(customEnchantment2).isCombineable()){//Combine!
                int level1 = getCustomEnchantments(item1).get(customEnchantment2);
                if(level1 == level2){
                    finalCustomEnchants.put(customEnchantment2, min(getById(customEnchantment2).getMaxLevel(), level1 + 1));
                }else{
                    finalCustomEnchants.put(customEnchantment2, max(level2, level1));
                }
            }else{//Isn't on the first item, so check applicability (no godsend method) and add it also if noCombineable then its goodddd
                if(Arrays.asList(getById(customEnchantment2).getApplicable()).contains(getAbsoluteId(item1))){//Applicable !
                    finalCustomEnchants.put(customEnchantment2, level2);
                }
            }
        }
        //Now for second round  (item1), check for conflicts between vanilla and custom.

        for(String customEnchantment1 : getCustomEnchantments(item1).keySet()){
            boolean vanillaEnchantConflict = false;
            boolean customEnchantConflict = false;
            for(Enchantment vanillaEnchantment2 : vanillaEnchants2.keySet()){
                if(Arrays.asList(getById(customEnchantment1).getVanillaConflicts()).contains(vanillaEnchantment2)){
                    vanillaEnchantConflict = true;
                }
            }
            for(String customEnchantment2 : getCustomEnchantments(item2).keySet()){
                if(Arrays.asList(getById(customEnchantment2).getConflicts()).contains(customEnchantment1)){
                    customEnchantConflict = true;
                }
            }

            if(!customEnchantConflict && !vanillaEnchantConflict && !finalCustomEnchants.containsKey(customEnchantment1)){
                finalCustomEnchants.put(customEnchantment1, getCustomEnchantments(item1).get(customEnchantment1));
            }
        }

        //Now for applying enchants
        for(Enchantment remove : combinedItem.getEnchantments().keySet()){//Remove all enchants
            combinedItem.removeEnchantment(remove);
        }
        if(item1.getType().equals(Material.ENCHANTED_BOOK)){
            EnchantmentStorageMeta bookiemeta =((EnchantmentStorageMeta) combinedItem.getItemMeta());
            for(Enchantment remove : bookiemeta.getStoredEnchants().keySet()){
                bookiemeta.removeStoredEnchant(remove);
            }
            combinedItem.setItemMeta(bookiemeta);
        }
        for(Enchantment vanillaToAdd : finalVanillaEnchants.keySet()){
            int level = finalVanillaEnchants.get(vanillaToAdd);
            if(item1.getType().equals(Material.ENCHANTED_BOOK)){
                try {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) combinedItem.getItemMeta();
                    meta.addStoredEnchant(vanillaToAdd, level, true);
                    combinedItem.setItemMeta(meta);
                }catch (IllegalArgumentException ignored){}
            }else{
                try {
                    combinedItem.addEnchantment(vanillaToAdd, level);
                }catch (IllegalArgumentException ignored){}
            }
        }
        StringBuilder customEnchantmentString = new StringBuilder();
        for(String customToAdd : finalCustomEnchants.keySet()){
            int level = finalCustomEnchants.get(customToAdd);
            customEnchantmentString.append(customToAdd).append("/").append(level).append(" ");
        }
        pdc.set(combinedItem, "enchantments", customEnchantmentString.toString());
        return combinedItem;
    }
    public static int getCustomDustCost(String id){
        HashMap<String, Integer> dustCosts = new HashMap<>();

        dustCosts.put("ender_core", 150);
        dustCosts.put("activatedShortbowCore", 400);

        return dustCosts.getOrDefault(id, 0);
    }

    public static void giveItem(Player player, ItemStack item, int amount){
        item.setAmount(amount);
        if(player.getInventory().firstEmpty() == -1){
            player.getWorld().dropItem(player.getLocation(), item);
        }else{
            player.getInventory().addItem(item);
        }
    }

    public static String getAbsoluteId(ItemStack item){
        String absoluteID;
        if(pdc.has(item, "id")){
            absoluteID = pdc.get(item, "id");
        }else{
            absoluteID = item.getType().toString().toLowerCase();
        }
        return absoluteID;
    }

    public static Object getAbsoluteIdMaterial(ItemStack item){
        Object absoluteID;
        if(pdc.has(item, "id")){
            absoluteID = pdc.get(item, "id");
        }else{
            absoluteID = item.getType();
        }
        return absoluteID;
    }

    public static void addCustomEnchantment(ItemStack item, customEnchantmentsWrapper enchantment, int level){
        if(!pdc.has(item, "enchantments")){
            String stringifiedEnchant = enchantment.toString() + "/" + level + " ";
            pdc.set(item, "enchantments", stringifiedEnchant);
        }else{
            if(Arrays.asList(enchantment.getApplicable()).contains(getAbsoluteId(item))){
                //Applicable!
                String[] rawEnchantments = pdc.get(item, "enchantments").split(" ");

                HashMap<String, Integer> customEnchantments = new HashMap<>();
                HashMap<String, Integer> newEnchantments = new HashMap<>();

                for(String raw : rawEnchantments){
                    customEnchantments.put(raw.split("/")[0], Integer.valueOf(raw.split("/")[1]));
                }

                for(String ench : customEnchantments.keySet()){
                    if(ench.equals(enchantment.getId()) && enchantment.isCombineable()){
                        if(customEnchantments.get(ench) == level){
                            newEnchantments.put(enchantment.getId(), min(enchantment.getMaxLevel(), level + 1));
                        }else{
                            newEnchantments.put(enchantment.getId(), max(customEnchantments.get(ench), level));
                        }

                    }else{
                        newEnchantments.put(enchantment.getId(), customEnchantments.get(ench));
                    }
                }
                boolean conflictVanilla = false;
                for(Enchantment vanilla : item.getEnchantments().keySet()){
                    if(Arrays.asList(enchantment.getVanillaConflicts()).contains(vanilla)){
                        conflictVanilla = true;
                    }
                }
                for(String ench : customEnchantments.keySet()){
                    if(Arrays.asList(enchantment.getConflicts()).contains(getById(ench).getId()) || conflictVanilla){
                        newEnchantments.remove(enchantment.getId());
                    }
                }
                StringBuilder newStringifiedEnchantments = new StringBuilder();
                for(String name : newEnchantments.keySet()){
                    newStringifiedEnchantments.append(name).append("/").append(newEnchantments.get(name).toString()).append(" ");
                }
                pdc.set(item, "enchantments", newStringifiedEnchantments.toString());
            }
        }
    }

    public static String roman(int Int) {
        LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<>();
        roman_numerals.put("M", 1000);
        roman_numerals.put("CM", 900);
        roman_numerals.put("D", 500);
        roman_numerals.put("CD", 400);
        roman_numerals.put("C", 100);
        roman_numerals.put("XC", 90);
        roman_numerals.put("L", 50);
        roman_numerals.put("XL", 40);
        roman_numerals.put("X", 10);
        roman_numerals.put("IX", 9);
        roman_numerals.put("V", 5);
        roman_numerals.put("IV", 4);
        roman_numerals.put("I", 1);
        String res = "";
        for(Map.Entry<String, Integer> entry : roman_numerals.entrySet()){
            int matches = Int/entry.getValue();
            res = res + repeat(entry.getKey(), matches);
            Int = Int % entry.getValue();
        }
        return res;
    }
    public static String repeat(String s, int n) {
        if(s == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static ChatColor getEnchantmentColor(String enchantmentId){
        HashMap<String, ChatColor> colors = new HashMap<>();
        colors.put("teleport", ChatColor.LIGHT_PURPLE);
        colors.put("lightning", ChatColor.YELLOW);
        colors.put("explosive", ChatColor.RED);
        colors.put("shortbow", ChatColor.of("#84FBDE"));
        colors.put("dodge", ChatColor.YELLOW);
        colors.put("experience", ChatColor.GREEN);
        colors.put("sorcery", ChatColor.DARK_PURPLE);
        colors.put("billionaire", ChatColor.GOLD);
        colors.put("lifesteal", ChatColor.DARK_RED);
        colors.put("netheritestomp", ChatColor.GRAY);
        colors.put("glasscannon", ChatColor.WHITE);
        colors.put("venom", ChatColor.of("#67D80E"));
        colors.put("parasite", ChatColor.of("#BDB3FB"));
        colors.put("gamble", ChatColor.of("#855BFB"));

        return colors.getOrDefault(enchantmentId, ChatColor.BLUE);
    }

    public static void syncItem(ItemStack item){
        ArrayList<String> dontsync = new ArrayList<>(); //Temp fix to add some itemLORE ! (like ench lore but 4 items) //TODO: add  itemLore when add wand enchants or smth iodk
        dontsync.add("healing_stick");
        dontsync.add("ice_wand");
        dontsync.add("repair_kit");
        dontsync.add("feather_steel");
        dontsync.add("warden_heart");
        dontsync.add("sing_flask");
        if((pdc.has(item, "id") && dontsync.contains(pdc.get(item, "id"))) || pdc.has(item, "modifier")){
            return;
        }
        //Sync attributes with vanilla attributes
        ItemMeta meta = item.getItemMeta();
        if(meta == null){
            return;
        }

        EquipmentSlot equipmentSlot = item.getType().getEquipmentSlot();
        AttributeModifier empty = new AttributeModifier(UUID.randomUUID(), "generic.armor", 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, equipmentSlot);
        AttributeModifier empty2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, equipmentSlot);
        AttributeModifier empty3 = new AttributeModifier(UUID.randomUUID(), "generic.knockbackResistance", 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, equipmentSlot);
        meta.setAttributeModifiers(null);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, empty);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, empty2);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, empty3);
        for(String customAttribute : getAttributes(item)){
            switch (customAttribute.split("/")[0]){
                case "health":
                    AttributeModifier health = new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", Double.parseDouble(customAttribute.split("/")[1]),
                            AttributeModifier.Operation.ADD_NUMBER, equipmentSlot);
                    meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, health);
                    break;
                case "atkspeed":
                    //dosomething without bows
                    if(!(item.getType().equals(Material.BOW) || item.getType().equals(Material.CROSSBOW))){
                        AttributeModifier atkspeed = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", Double.parseDouble(customAttribute.split("/")[1]) - 4d,
                                AttributeModifier.Operation.ADD_NUMBER, equipmentSlot);
                        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, atkspeed);
                    }
                    break;
            }
        }

        ArrayList<String> lore = new ArrayList<>();
        //sync attributes with what it currently has

        //Add item REALLORE (for custom armors/swords whatever

        HashMap<String, Double> attributes = new HashMap<>();
        for(String attr : getAttributes(item)){
            attributes.put(attr.split("/")[0], Double.valueOf(attr.split("/")[1]));
        }

        //sync lore with custom PDC enchantments.

        for(String attribute : attributes.keySet()){
            if(attribute.equals("critchance") && getCustomEnchantments(item).containsKey("critical")){
                lore.add(getAttributeName(attribute) + ChatColor.GRAY + ": " + ChatColor.WHITE + Precision.round(attributes.get(attribute) + getCustomEnchantments(item).get("critical")*10, 4));
            }else {
                lore.add(getAttributeName(attribute) + ChatColor.GRAY + ": " + ChatColor.WHITE + Precision.round(attributes.get(attribute), 2));
            }
        }

        if(!lore.isEmpty()){
            lore.add("");
        }
        if(pdc.has(item, "enchantments") && !Objects.equals(pdc.get(item, "enchantments"), "")){
            String[] rawEnchantments = pdc.get(item, "enchantments").split(" ");
            HashMap<String, Integer> customEnchantments = new HashMap<>();
            for(String raw : rawEnchantments){
                customEnchantments.put(raw.split("/")[0], Integer.valueOf(raw.split("/")[1]));
            }
            for(String name : customEnchantments.keySet()){
                lore.add(getEnchantmentColor(name) + getById(name).getPrettyname() + ChatColor.BLUE + " " + roman(customEnchantments.get(name)));
                if(item.getType().equals(Material.ENCHANTED_BOOK)){ //Thingy
                    //Add proper lore !
                    lore.addAll(getThelore(name, customEnchantments.get(name)));
                }
            }
        }
        Map<Enchantment, Integer> vanillaEnchs = item.getEnchantments();
        if(item.getType().equals(Material.ENCHANTED_BOOK)){
            EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) item.getItemMeta();
            vanillaEnchs = storageMeta.getStoredEnchants();
        }
        for(Enchantment e : vanillaEnchs.keySet()){
            String before = e.getKey().getKey().substring(1); //Fix that it outputs only first word
            lore.add(ChatColor.BLUE + e.getKey().getKey().substring(0, 1).toUpperCase() + before.replace("_", " ") + " " + roman(vanillaEnchs.get(e)));
        }
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
    }

    public static HashMap<String, Integer> getCustomEnchantments(ItemStack item){
        HashMap<String, Integer> enchants = new HashMap<>();
        if(pdc.has(item, "enchantments")){
            String rawEnchants = pdc.get(item, "enchantments");
            String[] cookedEnchants = rawEnchants.split(" ");
            if(rawEnchants.isEmpty()){
                return enchants;
            }
            for(String steak: cookedEnchants){
                enchants.put(steak.split("/")[0], Integer.valueOf(steak.split("/")[1]));
            }
        }
        return enchants;
    }

    public static void killPlayer(Player player, EntityDamageEvent.DamageCause cause){
        boolean save = false;
        player.sendMessage(org.bukkit.ChatColor.RED + "You Died!");
        ItemMeta offhand = player.getInventory().getItemInOffHand().getItemMeta();
        for(int i = 0; i<9; i++){
            ItemStack item = player.getInventory().getItem(i);
            if((item != null && Objects.equals(pdc.get(item, "id"), "funky_feather")) || (offhand != null && Objects.equals(pdc.get(player.getInventory().getItemInOffHand(), "id"), "funky_feather"))){
                if(offhand != null && Objects.equals(pdc.get(player.getInventory().getItemInOffHand(), "id"), "funky_feather")){
                    item = player.getInventory().getItemInOffHand();
                }
                save = true;
                item.setAmount(item.getAmount() - 1);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2000 ,0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200 ,3));
                player.sendMessage(org.bukkit.ChatColor.DARK_AQUA + "" + org.bukkit.ChatColor.BOLD + "FUNKY FEATHER!" + org.bukkit.ChatColor.RESET + org.bukkit.ChatColor.GRAY + " Inventory saved.");
                player.setHealth(5);
                player.setAbsorptionAmount(10);
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 2);
                break;
            }
        }

        if(!save){
            //Implement lives system
            //player.sendMessage(ChatColor.GRAY + "Inventory cleared from death.");
            //depleteLife(player); //Hardcore
            Bukkit.broadcastMessage(net.md_5.bungee.api.ChatColor.WHITE + player.getDisplayName() + " died because of " + cause);
            pdc.set(player, "effects", "");
            player.sendMessage(org.bukkit.ChatColor.YELLOW + "Death location : " + player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ());
            for(ItemStack todrop : player.getInventory()){
                if(todrop != null){
                    player.getWorld().dropItemNaturally(player.getLocation(), todrop);
                }
            }
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 500, 5));
            player.getInventory().clear();
            if(player.getBedSpawnLocation() != null){
                player.teleport(player.getBedSpawnLocation());
            }else{
                player.teleport(SurvivalAddons2.getPlugin().getServer().getWorld("world").getSpawnLocation());
            }
        }
    }

    public static void setEffect(Player player, String addeffect, int potency, int duration){
        if(pdc.get(player, "effects").contains(addeffect)){
            StringBuilder newEffects = new StringBuilder();
            for(String effect : pdc.get(player, "effects").split(" ")){
                if(!(effect.contains(addeffect))){
                    newEffects.append(effect).append(" ");
                }else {
                    newEffects.append(addeffect).append("/").append(potency).append("/").append(System.currentTimeMillis()).append("/").append(duration).append(" ");
                }
            }
            pdc.set(player, "effects", newEffects.toString());
        }else {
            pdc.set(player, "effects", pdc.get(player, "effects") + addeffect + "/" + potency + "/" + System.currentTimeMillis() + "/" + duration + " ");
        }
    }

    public static boolean hasEffect(Player player, String effect, boolean show){
        boolean has = false;

        if(pdc.get(player, "effects").contains(effect)){
            long time = 0;
            int duration = 0;
            StringBuilder newEffects = new StringBuilder();
            for(String effect1 : pdc.get(player, "effects").split(" ")){
                if(!(effect1.contains(effect))){
                    newEffects.append(effect1).append(" ");
                    continue;
                }
                time = Long.parseLong(effect1.split("/")[2]);
                duration = Integer.parseInt(effect1.split("/")[3]);
            }
            if((time + duration * 1000L) >= System.currentTimeMillis()){
                has = true;
                if(show){
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(org.bukkit.ChatColor.DARK_PURPLE + effect.substring(0, 1).toUpperCase() + effect.substring(1) + org.bukkit.ChatColor.WHITE + " : " + ((time + duration*1000L) - System.currentTimeMillis())/1000 + "s left"));
                }
            }else{
                pdc.set(player, "effects", newEffects.toString());
            }
        }
        return has;
    }

    public static String capFirst(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}