package mc.analyzers.survivaladdons2.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.chatChannel;
import static mc.analyzers.survivaladdons2.SurvivalAddons2.jda;


public class PlayerChatEvent implements Listener {
    @EventHandler
    public void PlayerChatEvent(AsyncPlayerChatEvent e){
        String name = e.getPlayer().getDisplayName();
        String message = e.getMessage();
        try {
            chatChannel.sendMessage("**" + name + "**" + " : " + message).queue();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
