package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.chatChannel;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.inCombat;

public class OnPlayerLeave implements Listener {
    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent e){
        chatChannel.sendMessage("`" + e.getPlayer().getDisplayName() + " left the server !`").queue();
        if(inCombat(e.getPlayer())){
            PDCUtils.set(e.getPlayer(), "killOnJoin", "true");
        }
    }
}
