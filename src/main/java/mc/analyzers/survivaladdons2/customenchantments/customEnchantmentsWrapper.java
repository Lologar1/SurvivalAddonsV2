package mc.analyzers.survivaladdons2.customenchantments;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;

public class customEnchantmentsWrapper {
    private String prettyname;
    private String id;
    private int maxLevel;
    private String[] applicable;
    private String[] conflicts;
    private Enchantment[] vanillaConflicts;
    private boolean combineable;
    public ChatColor color;
    public customEnchantmentsWrapper(net.md_5.bungee.api.ChatColor color, Enchantment[] vanillaConflicts, String prettyname, String id, int maxLevel, String[] conflicts, String[] applicable, boolean combineable){
        this.prettyname = prettyname;
        this.id = id;
        this.maxLevel = maxLevel;
        this.conflicts = conflicts;
        this.applicable = applicable;
        this.vanillaConflicts = vanillaConflicts;
        this.combineable = combineable;
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }

    public Enchantment[] getVanillaConflicts() {
        return vanillaConflicts;
    }

    public String getPrettyname() {
        return prettyname;
    }

    public static String[] commonEnchantments = new String[]{"explosive", "dodge", "experience"};
    public static String[] uncommonEnchantments = new String[]{"lightning", "venom", "bountiful", "gamble"};
    public static String[] rareEnchantments = new String[]{"netheritestomp", "ftts", "sorcery"};
    public static String[] epicEnchantments = new String[]{"lifesteal", "billionaire", "parasite"};
    public static String[] legendaryEnchantments = new String[]{"glasscannon", "critical", "smelt"}; //add smelt

    public static customEnchantmentsWrapper getById(String id){
        try {
            return (customEnchantmentsWrapper) customEnchantments.class.getField(id + "Enchantment").get(customEnchantments.class);
        }catch (Exception e){
            System.out.println("No such enchantment! " + id);
            return null;
        }
    }

    public boolean isCombineable() {
        return combineable;
    }

    public String getId(){
        return id;
    }

    public String[] getApplicable() {
        return applicable;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String[] getConflicts() {
        return this.conflicts;
    }
    @Override
    public String toString(){
        return id;
    }
}
