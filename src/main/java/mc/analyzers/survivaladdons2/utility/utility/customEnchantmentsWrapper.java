package mc.analyzers.survivaladdons2.utility;

import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import static mc.analyzers.survivaladdons2.utility.customEnchantments.*;

public class customEnchantmentsWrapper {
    private String prettyname;
    private String id;
    private int maxLevel;
    private String[] applicable;
    private String[] conflicts;
    private Enchantment[] vanillaConflicts;
    private boolean combineable;
    public customEnchantmentsWrapper(Enchantment[] vanillaConflicts, String prettyname, String id, int maxLevel, String[] conflicts, String[] applicable, boolean combineable){
        this.prettyname = prettyname;
        this.id = id;
        this.maxLevel = maxLevel;
        this.conflicts = conflicts;
        this.applicable = applicable;
        this.vanillaConflicts = vanillaConflicts;
        this.combineable = combineable;
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
    public static String[] legendaryEnchantments = new String[]{"glasscannon", "critical"}; //add smelt

    public static customEnchantmentsWrapper getById(String id){
        HashMap<String, customEnchantmentsWrapper> ids = new HashMap<>();
        ids.put("teleport", teleportEnchantment);
        ids.put("lightning", lightningEnchantment);
        ids.put("explosive", explosiveEnchantment);
        ids.put("harvest", harvestEnchantment);
        ids.put("nightvision", nightVisionEnchantment);
        ids.put("shortbow", shortBowEnchantment);
        ids.put("ftts", fttsEnchantment);
        ids.put("dodge", dodgeEnchantment);
        ids.put("experience", experienceEnchantment);
        ids.put("sorcery", sorceryEnchantment);
        ids.put("billionaire", billionaireEnchantment);
        ids.put("lifesteal", lifestealEnchantment);
        ids.put("netheritestomp", netheriteStompEnchantment);
        ids.put("glasscannon", glasscannonEnchantment);
        ids.put("venom", venomEnchantment);
        ids.put("bountiful", bountifulEnchantment);
        ids.put("critical", criticalEnchantment);
        ids.put("parasite", parasiteEnchantment);
        ids.put("gamble", gambleEnchantment);
        ids.put("smelt", smeltEnchantment);
        return ids.getOrDefault(id, null);
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
