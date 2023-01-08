package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OnPlayerEat implements Listener {
    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent e){
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        if(pdc.has(item, "id") && pdc.get(item, "id").equals("sugar_soup")){
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600 ,1));
            player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "SLURP! " + ChatColor.GRAY + "Speed II for 30 seconds");
        }
    }
}
