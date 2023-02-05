package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.OfflineInventories;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.profile.PlayerProfile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class View implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerInventory viewed = null;
        Inventory offInv = null;
        boolean offline = false;
        if(SurvivalAddons2.getPlugin().getServer().getOnlinePlayers().contains(SurvivalAddons2.getPlugin().getServer().getPlayer(args[0]))){
            viewed = SurvivalAddons2.getPlugin().getServer().getPlayer(args[0]).getInventory();
        }else{
            OfflinePlayer offlinePlayer = SurvivalAddons2.getPlugin().getServer().getOfflinePlayer(args[0]);
            if(!offlinePlayer.hasPlayedBefore()){
                player.sendMessage(ChatColor.RED + "That player hasn't played before !");
                return true;
            }
            offline = true;
            OfflineInventories inventories = new OfflineInventories();

            try {
                offInv = inventories.read(SurvivalAddons2.getPlugin().getServer().getOfflinePlayer(args[0]).getUniqueId(), ChatColor.GRAY + "Viewing");
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        }
          /*
    Mapping layout:

    Inventory | NBT
    0         | 103    (Helmet)
    1         | 102    (Chestplate)
    2         | 101    (Leggings)
    3         | 100    (Boots)
    4         | -106   (Shield)
    5-8       | NONE   Always empty
    9-35      | 9-35   (Inventory without hotbar)
    36-44     | 0-8    (hotbar)
   */
        Inventory display = Bukkit.createInventory(player, 45, ChatColor.GRAY + "Viewing");//First = 36
        if(offline){
            display.setItem(0, offInv.getItem(0));
            display.setItem(9, offInv.getItem(1));
            display.setItem(18, offInv.getItem(2));
            display.setItem(27, offInv.getItem(3));
            display.setItem(28, offInv.getItem(17));
            display.setItem(29, offInv.getItem(26));
            display.setItem(30, offInv.getItem(35));

            for(int i = 9; i<=35; i++){
                if(i==17 || i==26 || i==35){
                    continue;
                }
                display.setItem(i-8, offInv.getItem(i));
            }

            for(int i = 36; i<=44; i++){
                display.setItem(i, offInv.getItem(i));
            }
        }else {
            for(int i = 36; i<=44; i++){
                display.setItem(i, viewed.getItem(i-36));
            }
            display.setItem(0, viewed.getHelmet());
            display.setItem(9, viewed.getChestplate());
            display.setItem(18, viewed.getLeggings());
            display.setItem(27, viewed.getBoots());
            display.setItem(28, viewed.getItem(17));
            display.setItem(29, viewed.getItem(26));
            display.setItem(30, viewed.getItem(35));
            for(int i = 1; i <= 27; i++){
                if(i==9 || i==18 || i==27){
                    continue;
                }
                display.setItem(i, viewed.getItem(i+8));
            }
        }
        player.openInventory(display);
        return true;
    }
}
