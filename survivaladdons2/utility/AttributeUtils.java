package mc.analyzers.survivaladdons2.utility;

import mc.analyzers.survivaladdons2.tasks.SyncAttributes;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static mc.analyzers.survivaladdons2.tasks.SyncAttributes.materialAttributes;
import static mc.analyzers.survivaladdons2.utility.utility.getAbsoluteId;
import static mc.analyzers.survivaladdons2.utility.utility.syncItem;

public class AttributeUtils {
    public static void syncAttributes(ItemStack item){
        if(item != null && !pdc.has(item, "attributed") && !pdc.has(item, "id") && materialAttributes.containsKey(item.getType())){
            String[] rawAttributes = materialAttributes.get(item.getType()).split(" ");
            for(String rawAttribute : rawAttributes){
                String attribute = rawAttribute.split("/")[0];
                double potency = Double.parseDouble(rawAttribute.split("/")[1]);
                setAttribute(item, attribute, potency);
            }
            syncItem(item);
            pdc.set(item, "attributed", "true");
        }else if(pdc.has(item, "id") && materialAttributes.containsKey(pdc.get(item, "id")) && !pdc.has(item, "attributed")){
            String[] rawAttributes = materialAttributes.get(getAbsoluteId(item)).split(" ");
            for(String rawAttribute : rawAttributes){
                String attribute = rawAttribute.split("/")[0];
                double potency = Double.parseDouble(rawAttribute.split("/")[1]);
                setAttribute(item, attribute, potency);
            }
            syncItem(item);
            pdc.set(item, "attributed", "true");
        }
    }
    public static double[] getProtectionFactors(Player player){
        if(Arrays.equals(player.getInventory().getArmorContents(), new ItemStack[]{})){
            return new double[]{1d,1d};
        }else {
            double def = 0d;
            double magicdef = 0d;
            ItemStack[] armor = player.getInventory().getArmorContents();
            for(ItemStack piece : armor){
                for(String pieceAttributes : getAttributes(piece)){
                    if(pieceAttributes.split("/")[0].equals("defense")){ //Add protection calculations!
                        def += Double.parseDouble(pieceAttributes.split("/")[1]);
                    }else if(pieceAttributes.split("/")[0].equals("magicdefense")){
                        magicdef += Double.parseDouble(pieceAttributes.split("/")[1]);
                    }
                }
            }

            return new double[]{0.80*(1 - def/1000)+0.2, 0.80*(1 - magicdef/1000)+0.2};
        }
    }

    public static double[] getDefenseValues(Player player){
        if(Arrays.equals(player.getInventory().getArmorContents(), new ItemStack[]{})){
            return new double[]{1d,1d};
        }else {
            double def = 0d;
            double magicdef = 0d;
            ItemStack[] armor = player.getInventory().getArmorContents();
            for(ItemStack piece : armor){
                for(String pieceAttributes : getAttributes(piece)){
                    if(pieceAttributes.split("/")[0].equals("defense")){ //Add protection calculations!
                        def += Double.parseDouble(pieceAttributes.split("/")[1]);
                    }else if(pieceAttributes.split("/")[0].equals("magicdefense")){
                        magicdef += Double.parseDouble(pieceAttributes.split("/")[1]);
                    }
                }
            }

            return new double[]{def,magicdef};
        }
    }

    public static double[] getVanillaEnchantmentProtectionFactors(Player player){
        if(Arrays.equals(player.getInventory().getArmorContents(), new ItemStack[]{})){
            //Protection, proj prot, blast prot
            return new double[]{1d,1d, 1d};
        }else{
            double prot = 0d;
            double projprot = 0d;
            double blastprot = 0d;
            ItemStack[] armor = player.getInventory().getArmorContents();
            for(ItemStack piece : armor){
                if(piece == null){
                    continue;
                }
                for(Enchantment enchantment : piece.getEnchantments().keySet()){
                    if(enchantment.equals(Enchantment.PROTECTION_ENVIRONMENTAL)){
                        prot += piece.getEnchantmentLevel(enchantment);
                    }else if(enchantment.equals(Enchantment.PROTECTION_PROJECTILE)){
                        projprot += piece.getEnchantmentLevel(enchantment);
                    }else if(enchantment.equals(Enchantment.PROTECTION_EXPLOSIONS)){
                        blastprot += piece.getEnchantmentLevel(enchantment);
                    }
                }
            }
            return new double[]{prot*3, projprot*4, blastprot*4};
        }
    }

    public static String[] getAttributes(ItemStack item){
        StringBuilder attributes = new StringBuilder();
        if(!pdc.has(item, "attributes") || pdc.get(item,"attributes").equals("")){
            return new String[]{};
        }
        for(String attribute : pdc.get(item, "attributes").split(" ")){
            double potency = Double.parseDouble(attribute.split("/")[1]);
            if(potency != 0){
                attributes.append(attribute).append("/").append(potency).append(" ");
            }
        }
        return attributes.toString().split(" ");
    }
    public static void setAttribute(ItemStack item, String attribute, double potency){
        ItemMeta meta = item.getItemMeta();
        if(!pdc.has(item, "attributes")){
            pdc.set(item, "attributes", attribute + "/" + potency + " ");
            return;
        }
        if(!(meta == null)){
            boolean has = false;
            for(String attr : getAttributes(item)){
                String name = attr.split("/")[0];
                if (name.equals(attribute)){
                    has = true;
                    break;
                }
            }
            if(has){
                //Already has attribute
                StringBuilder newAttributes = new StringBuilder();
                for(String currentAttribute : getAttributes(item)){
                    String name = currentAttribute.split("/")[0];
                    if(Objects.equals(name, attribute)){
                        newAttributes.append(currentAttribute.split("/")[0]).append("/").append(potency).append(" ");
                    }else{
                        newAttributes.append(currentAttribute).append(" ");
                    }
                }
                pdc.set(item, "attributes", newAttributes.toString());
            }else{
                //Doesn't have it
                pdc.set(item, "attributes", pdc.get(item, "attributes") + attribute + "/" + potency + " ");
            }
        }
    }

    public static void mergeModifiers(ItemStack recipient, ItemStack modifier){
        HashMap<String, ChatColor> colors = new HashMap<>();
        colors.put("warden_heart", ChatColor.RED);
        colors.put("feather_steel", ChatColor.YELLOW);
        colors.put("fallen_star", ChatColor.AQUA);

        HashMap<String, String> prettyName = new HashMap<>();
        prettyName.put("warden_heart", "Loving");
        prettyName.put("feather_steel", "Light");
        prettyName.put("fallen_star", "Magical");
        //Reset attributes to default
        pdc.set(recipient, "attributes", "");
        String[] rawAttributes = SyncAttributes.materialAttributes.get(recipient.getType()).split(" ");
        for(String rawAttribute : rawAttributes){
            String attribute = rawAttribute.split("/")[0];
            double potency = Double.parseDouble(rawAttribute.split("/")[1]);
            setAttribute(recipient, attribute, potency);
        }
        pdc.set(recipient, "attributed", "true");

        String type = pdc.get(modifier, "modifier").split("/")[0];
        String potency = pdc.get(modifier, "modifier").split("/")[1];
        boolean has = false;
        double before = 0;
        for(String attr : getAttributes(recipient)){
            String name = attr.split("/")[0];
            if (name.equals(type)){
                has = true;
                before = Double.parseDouble(attr.split("/")[1]);
                break;
            }
        }
        if(has){
            setAttribute(recipient, type, Double.parseDouble(potency) + before);
        }else {
            setAttribute(recipient, type, Double.parseDouble(potency));
        }
        pdc.set(recipient, "currentAttributeModifier", pdc.get(modifier, "id"));
        ItemMeta name = recipient.getItemMeta();
        StringBuilder vanillaName = new StringBuilder();
        for(String part : recipient.getType().name().split("_")){
            vanillaName.append(part.substring(0,1).toUpperCase() + part.substring(1).toLowerCase()).append(" ");
        }
        name.setDisplayName(colors.get(pdc.get(modifier, "id")) + prettyName.get(pdc.get(modifier, "id")) + " " + ChatColor.WHITE + vanillaName);
        recipient.setItemMeta(name);
    }

    public static String getAttributeName(String attribute){
        switch (attribute){
            case "damage":
                return ChatColor.RED + "Damage";
            case "atkspeed":
                return ChatColor.YELLOW + "Attack speed";
            case "critchance":
                return ChatColor.BLUE + "Crit chance";
            case "critdamage":
                return ChatColor.of("#B51FFB") + "Crit damage";
            case "magicdamage":
                return ChatColor.AQUA + "Magic damage";
            case "defense":
                return ChatColor.GREEN + "Defense";
            case "health":
                return ChatColor.DARK_RED + "Health";
            case "magicdefense":
                return ChatColor.DARK_AQUA + "Magic defense";
            case "speed":
                return ChatColor.WHITE + "Speed";
            default:
                return "UNKNOWN. PLEASE CONTACT ANALYZERS.";
        }
    }
}
