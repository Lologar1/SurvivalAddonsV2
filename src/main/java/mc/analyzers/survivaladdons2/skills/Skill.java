package mc.analyzers.survivaladdons2.skills;

import mc.analyzers.survivaladdons2.utility.PlayerUtils;
import org.bukkit.entity.Player;

import static mc.analyzers.survivaladdons2.utility.ConfigUtils.readConfig;
import static mc.analyzers.survivaladdons2.utility.ConfigUtils.writeConfig;

public class Skill {
    private int maxLevel;
    private String id;
    public Skill(int maxLevel, String id){
        this.maxLevel = maxLevel;
        this.id = id;

    }
    public void addExperience(Player player){
        int currentExperience = (int) readConfig(player.getUniqueId() + "." + id + ".experience");
    }
}
