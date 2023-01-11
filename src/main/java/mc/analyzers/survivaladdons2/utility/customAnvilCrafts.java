package mc.analyzers.survivaladdons2.utility;

import java.util.ArrayList;
import java.util.HashMap;

public class customAnvilCrafts {
    public static final HashMap<String, String[]> crafts;
    static{
        HashMap<String, String[]> table = new HashMap<>(); //Don't forget ebooks and custom armor/thingies !
        table.put("bow", new String[]{"ender_core", "activatedShortbowCore"});
        table.put("crossbow", new String[]{"ender_core"});
        table.put("enchanted_book", new String[]{"ender_core"});
        crafts = table;
    }
}
