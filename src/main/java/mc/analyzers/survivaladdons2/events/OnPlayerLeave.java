package mc.analyzers.survivaladdons2.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.chatChannel;

public class OnPlayerLeave implements Listener {
    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent e){
        chatChannel.sendMessage("`" + e.getPlayer().getDisplayName() + " left the server !`").queue();
    }
}
