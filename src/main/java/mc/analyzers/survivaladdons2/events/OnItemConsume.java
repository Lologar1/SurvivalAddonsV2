package mc.analyzers.survivaladdons2.events;

import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;

import static mc.analyzers.survivaladdons2.quests.Quest.checkQuest;
import static mc.analyzers.survivaladdons2.utility.PlayerUtils.setEffect;
import static mc.analyzers.survivaladdons2.utility.MiscUtils.roman;

public class OnItemConsume implements Listener {
    @EventHandler
    public void OnItemConsume(PlayerItemConsumeEvent e){
        checkQuest("consume", "food", e.getPlayer(), 1);
        if(PDCUtils.has(e.getItem(), "id") && e.getItem().getItemMeta() instanceof PotionMeta){
            String effect = PDCUtils.get(e.getItem(), "effects");
            String name = effect.split("/")[0];
            int potency = Integer.parseInt(effect.split("/")[1]);
            int duration = Integer.parseInt(effect.split("/")[2]);
            setEffect(e.getPlayer(), name, potency, duration);
            e.getPlayer().sendMessage(ChatColor.GREEN + ""+ ChatColor.BOLD + "SLURP! " + ChatColor.GRAY + "Gained " + name.substring(0, 1).toUpperCase() + name.substring(1) + " " + roman(potency) + " for " + duration + " seconds!");
        }
    }
}
