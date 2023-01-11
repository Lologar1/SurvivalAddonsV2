package mc.analyzers.survivaladdons2.utility;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtils {
    public static void writeConfig(String path, Object value){
        SurvivalAddons2 plugin = SurvivalAddons2.getPlugin();
        FileConfiguration config = plugin.getConfig();
        config.set(path, value);
        plugin.saveConfig();
    }
    public static Object readConfig(String path){
        SurvivalAddons2 plugin = SurvivalAddons2.getPlugin();
        FileConfiguration config = plugin.getConfig();
        if(config.contains(path)){
            Object read = config.get(path);
            plugin.saveConfig();
            return read;
        }else{
            return null;
        }
    }
}
