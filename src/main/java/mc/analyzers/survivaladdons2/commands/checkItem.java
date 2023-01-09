package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class checkItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        player.sendMessage("CustomEnchas " + pdc.get(player.getInventory().getItemInMainHand(), "enchantments"));
        player.sendMessage("vanil ench " + player.getInventory().getItemInMainHand().getEnchantments());
        player.sendMessage("inCombat " + pdc.get(player, "inCombat"));
        player.sendMessage("attr " + pdc.get(player.getInventory().getItemInMainHand(), "attributes"));
        player.sendMessage("id " + pdc.has(player.getInventory().getItemInMainHand(), "Id"));
        return true;
    }
}
