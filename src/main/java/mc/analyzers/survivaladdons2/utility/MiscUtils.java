package mc.analyzers.survivaladdons2.utility;

import org.bukkit.inventory.ItemStack;

import java.util.*;

import static java.lang.Math.min;

public class MiscUtils {
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

    public static String capFirst(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
