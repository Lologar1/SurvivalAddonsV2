package mc.analyzers.survivaladdons2.modifiers;

import net.md_5.bungee.api.ChatColor;

public class Modifiers {
    public static final Modifier warden_heart = new Modifier("warden_heart",ChatColor.RED + "Loving", "health/2");
    public static final Modifier fallen_star = new Modifier("fallen_star", ChatColor.AQUA + "Magical", "magicdamage/1");
    public static final Modifier feather_steel = new Modifier("feather_steel", ChatColor.YELLOW + "Light", "atkspeed/0.2");
}
