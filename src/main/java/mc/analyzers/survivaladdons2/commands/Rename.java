package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.utility.PDCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static mc.analyzers.survivaladdons2.modifiers.Modifier.getById;

public class Rename implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "Provide one argument!");
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        StringBuilder prettyName = new StringBuilder();
        if(PDCUtils.has(item, "currentAttributeModifier")){
            prettyName.append(getById(PDCUtils.get(item, "currentAttributeModifier")).getPrefix()).append(" ").append(ChatColor.RESET);
        }
        for(String arg: args){
            prettyName.append(arg).append(" ");
        }
        meta.setDisplayName(prettyName.toString());
        item.setItemMeta(meta);
        return true;
    }
}
