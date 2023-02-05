package mc.analyzers.survivaladdons2.modifiers;

import net.md_5.bungee.api.ChatColor;

public class Modifier {
    public static Modifier getById(String id){
        try{
            return (Modifier) Modifiers.class.getField(id).get(Modifiers.class);
        }catch (Exception ex){
            return null;
        }
    }
    private String id;
    private String prefix;
    private String attribute;
    public Modifier(String id, String prefix, String attribute){
        this.id = id;
        this.prefix = prefix;
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }
}
