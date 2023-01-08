package mc.analyzers.survivaladdons2.utility;

import org.bukkit.ChatColor;

import java.util.ArrayList;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.dustIcon;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.heartIcon;
import static mc.analyzers.survivaladdons2.utility.customEnchantments.billionaireEnchantment;
import static mc.analyzers.survivaladdons2.utility.customEnchantments.teleportEnchantment;

public class customEnchantmentLore {
    public static ArrayList<String> thelore = new ArrayList<>();

    public static ArrayList<String> getThelore(String enchantment, int level) {
        thelore.clear();
        switch (enchantment){
            case "teleport":
                switch (level){
                    case 1:
                        thelore.add(ChatColor.GRAY + "Sneak to shoot a teleportation ");
                        thelore.add(ChatColor.GRAY + "arrow. 20 second cooldown");
                        break;
                    case 2:
                        thelore.add(ChatColor.GRAY + "Sneak to shoot a teleportation ");
                        thelore.add(ChatColor.GRAY + "arrow. 15 second cooldown");
                        break;
                    case 3:
                        thelore.add(ChatColor.GRAY + "Sneak to shoot a teleportation ");
                        thelore.add(ChatColor.GRAY + "arrow. 10 second cooldown");
                        break;
                }
                break;
            case "lightning":
                switch (level){
                    case 1:
                        thelore.add(ChatColor.GRAY + "Every " + ChatColor.YELLOW + "seventh " + ChatColor.GRAY + "hit strikes " + ChatColor.YELLOW + "lightning");
                        thelore.add(ChatColor.GRAY + "for " + ChatColor.RED + "2" + heartIcon + ChatColor.GRAY + " but costs " + ChatColor.RED + "3 " + dustIcon + " dust");
                        break;
                    case 2:
                        thelore.add(ChatColor.GRAY + "Every " + ChatColor.YELLOW + "sixth " + ChatColor.GRAY + "hit strikes " + ChatColor.YELLOW + "lightning");
                        thelore.add(ChatColor.GRAY + "for " + ChatColor.RED + "2.5" + heartIcon + ChatColor.GRAY + " but costs " + ChatColor.RED + "3 " + dustIcon + " dust");
                        break;
                    case 3:
                        thelore.add(ChatColor.GRAY + "Every " + ChatColor.YELLOW + "fifth " + ChatColor.GRAY + "hit strikes " + ChatColor.YELLOW + "lightning");
                        thelore.add(ChatColor.GRAY + "for " + ChatColor.RED + "3" + heartIcon + ChatColor.GRAY + " but costs " + ChatColor.RED + "3 " + dustIcon + " dust");
                        break;
                    case 4:
                        thelore.add(ChatColor.GRAY + "Every " + ChatColor.YELLOW + "fourth " + ChatColor.GRAY + "hit strikes " + ChatColor.YELLOW + "lightning");
                        thelore.add(ChatColor.GRAY + "for " + ChatColor.RED + "4.5" + heartIcon + ChatColor.GRAY + " but costs " + ChatColor.RED + "2 " + dustIcon + " dust");
                        break;
                    case 5:
                        thelore.add(ChatColor.GRAY + "Every " + ChatColor.YELLOW + "third " + ChatColor.GRAY + "hit strikes " + ChatColor.YELLOW + "lightning");
                        thelore.add(ChatColor.GRAY + "for " + ChatColor.RED + "5" + heartIcon + ChatColor.GRAY + " but costs " + ChatColor.RED + "2 " + dustIcon + " dust");
                        break;
                }
                break;
            case "explosive":
                switch (level){
                    case 1:
                        thelore.add(ChatColor.GRAY + "Shoots " + ChatColor.RED + "explosive " + ChatColor.GRAY + "arrows!");
                        thelore.add(ChatColor.GRAY + "costs " + ChatColor.RED + "5 " + dustIcon + " dust " + ChatColor.GRAY + "per shot.");
                        break;
                    case 2:
                        thelore.add(ChatColor.GRAY + "Shoots " + ChatColor.RED + "very explosive " + ChatColor.GRAY + "arrows!");
                        thelore.add(ChatColor.GRAY + "costs " + ChatColor.RED + "2 " + dustIcon + " dust " + ChatColor.GRAY + "per shot.");
                        break;
                }
                break;
            case "harvest":
                thelore.add(ChatColor.GRAY + "Redstone " + ChatColor.RED + dustIcon + " dust" + ChatColor.GRAY + " you mine yields");
                thelore.add(ChatColor.RED + String.valueOf(level) + " more " + dustIcon + " dust" + ChatColor.GRAY + " and has a " + String.valueOf(5 * level) + "% chance");
                thelore.add(ChatColor.GRAY + "to give extra dust.");
                break;
            case "nightvision":
                thelore.add(ChatColor.GRAY + "Gives permanent " + ChatColor.BLUE + "night vision");
                break;
            case "shortbow":
                thelore.add(ChatColor.GRAY + "Instantly shoots arrows!");
                thelore.add(ChatColor.GRAY + (4-level + "s cooldown"));
                break;
            case "ftts":
                switch (level){
                    case 1:
                        thelore.add(ChatColor.GRAY + "Hitting an arrow " + ChatColor.YELLOW + "3 times");
                        thelore.add(ChatColor.GRAY + "without missing grants " + ChatColor.BLUE + "Speed I");
                        thelore.add(ChatColor.GRAY + "(6s)");
                        break;
                    case 2:
                        thelore.add(ChatColor.GRAY + "Hitting an arrow " + ChatColor.YELLOW + "2 times");
                        thelore.add(ChatColor.GRAY + "without missing grants " + ChatColor.BLUE + "Speed I");
                        thelore.add(ChatColor.GRAY + "(7s)");
                        break;
                    case 3:
                        thelore.add(ChatColor.GRAY + "Hitting an arrow " + ChatColor.YELLOW + "2 times");
                        thelore.add(ChatColor.GRAY + "without missing grants " + ChatColor.BLUE + "Speed II");
                        thelore.add(ChatColor.GRAY + "(7s)");
                        break;
                }
                break;
            case "dodge":
                thelore.add(ChatColor.YELLOW +""+ level*4 + "%" + ChatColor.GRAY + " chance to " + ChatColor.GRAY + "dodge" + ChatColor.GRAY + " a hit.");
                break;
            case "experience":
                thelore.add(ChatColor.GRAY + "Increases gained experience by " + ChatColor.GREEN + 11*level + "%");
                break;
            case "sorcery":
                thelore.add(ChatColor.LIGHT_PURPLE + "" + level*6 + "%" + ChatColor.GRAY + " chance to convert all arrow damage to " + ChatColor.AQUA + "Magic damage");
                break;
            case "billionaire":
                thelore.add(ChatColor.GRAY + "Hits deal " + ChatColor.RED + "+" + (int) Math.round(level * 33.3) + "%" + ChatColor.GRAY + " damage, but");
                thelore.add(ChatColor.GRAY + "also cost " + ChatColor.RED + level*3 + " " + dustIcon + " dust.");
                break;
            case "lifesteal":
                thelore.add(ChatColor.GRAY + "Hits heal for " + ChatColor.RED + level * 5 + "%" + ChatColor.GRAY + " of raw damage,");
                thelore.add(ChatColor.GRAY + "but effective outgoing damage is " + ChatColor.RED + "halved.");
                break;
            case "netheritestomp":
                thelore.add(ChatColor.GRAY + "Deal " + ChatColor.RED + "+" + (level * 4) + "%" + ChatColor.GRAY + " damage if your ");
                thelore.add(ChatColor.GRAY + "opponent wears " + ChatColor.DARK_GRAY + "netherite armor");
                break;
            case "glasscannon":
                thelore.add(ChatColor.GRAY + "Shots with this bow deal " + ChatColor.RED + "x2" + ChatColor.GRAY + " damage");
                thelore.add(ChatColor.GRAY + "if you aren't wearing any armor.");
                break;
            case "venom":
                thelore.add(ChatColor.GRAY + "Critical hit have a " + ChatColor.DARK_GREEN + level * 25 + "%" + ChatColor.GRAY + " chance to inflict");
                thelore.add(ChatColor.DARK_GREEN + "poison " + ChatColor.GRAY + "for " + (level * 3) + " seconds.");
                break;
            case "bountiful":
                thelore.add(ChatColor.GOLD + "" +(level) + "% " + ChatColor.GRAY + "chance for crops to drop");
                thelore.add(ChatColor.LIGHT_PURPLE + "rare" + ChatColor.GRAY + " drops !");
                break;
            case "critical":
                thelore.add(ChatColor.RED + "+" + (level*10) + "%" + ChatColor.GRAY + " chance to get ");
                thelore.add(ChatColor.RED + "critical" + ChatColor.GRAY + " hits!");
                break;
            case "parasite":
                thelore.add(ChatColor.GRAY + "Heal for " + ChatColor.RED + level*0.25 + " " + heartIcon);
                thelore.add(ChatColor.GRAY + "per critical shot");
                break;
            case "gamble":
                switch (level){
                    case 1:
                        thelore.add(ChatColor.GRAY + "50% of dealt damage converts to " + ChatColor.AQUA + "magic damage,");
                        thelore.add(ChatColor.GRAY + "but there's a " + ChatColor.LIGHT_PURPLE + "50% chance" + ChatColor.GRAY + " to deal");
                        thelore.add(ChatColor.GRAY + "it to yourself instead.");
                        break;
                    case 2:
                        thelore.add(ChatColor.GRAY + "75% of dealt damage converts to " + ChatColor.AQUA + "magic damage,");
                        thelore.add(ChatColor.GRAY + "but there's a " + ChatColor.LIGHT_PURPLE + "50% chance" + ChatColor.GRAY + " to deal");
                        thelore.add(ChatColor.GRAY + "it to yourself instead.");
                        break;
                    case 3:
                        thelore.add(ChatColor.GRAY + "All damage converts to " + ChatColor.AQUA + "magic damage,");
                        thelore.add(ChatColor.GRAY + "but there's a " + ChatColor.LIGHT_PURPLE + "50% chance" + ChatColor.GRAY + " to deal");
                        thelore.add(ChatColor.GRAY + "it to yourself instead.");
                        break;
                }
        }
        return thelore;
    }
}
