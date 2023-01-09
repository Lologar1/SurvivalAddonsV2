package mc.analyzers.survivaladdons2.commands;

import mc.analyzers.survivaladdons2.utility.pdc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Quests implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Inventory quests = Bukkit.createInventory(player, 27, ChatColor.GRAY + "Quests");
        ItemStack anvilGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta anvilGlassMeta = anvilGlass.getItemMeta();
        anvilGlassMeta.setDisplayName(ChatColor.BLACK + "");
        anvilGlass.setItemMeta(anvilGlassMeta);
        for(int i = 0; i<27; i++){
            quests.setItem(i, anvilGlass);
        }

        long weeklyLast = 0;
        long dailyLast = 0;
        long hourlyLast = 0;

        boolean weeklyActive = true;
        boolean dailyActive = true;
        boolean hourlyActive = true;

        if(!pdc.get(player, "weeklyLast").equals("active")){
            weeklyLast = Long.parseLong(pdc.get(player, "weeklyLast"));
            weeklyActive = false;
        }
        if(!pdc.get(player, "dailyLast").equals("active")){
            dailyLast = Long.parseLong(pdc.get(player, "dailyLast"));
            dailyActive = false;
        }
        if(!pdc.get(player, "hourlyLast").equals("active")){
            hourlyLast = Long.parseLong(pdc.get(player, "hourlyLast"));
            hourlyActive = false;
        }

        boolean weeklySelect = false;
        boolean dailySelect = false;
        boolean hourlySelect = false;

        if(weeklyLast + 604800000 <= System.currentTimeMillis()){
            weeklySelect = true;
        }
        if(dailyLast + 43200000 <= System.currentTimeMillis()){
            dailySelect = true;
        }
        if(hourlyLast + 3600000 <= System.currentTimeMillis()){
            hourlySelect = true;
        }

        ItemStack weekly = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta weeklyMeta = weekly.getItemMeta();
        weeklyMeta.setDisplayName(ChatColor.GOLD + "Weekly Quest");
        if(weeklyActive && !pdc.get(player, "activeWeeklyQuest").equals("null")){
            weeklyMeta.setDisplayName(mc.analyzers.survivaladdons2.quests.Quests.weeklyQuest.getPrettyNameFromId(pdc.get(player, "activeWeeklyQuest")));
        }
        ArrayList<String> weeklyLore = new ArrayList<>();
        if(weeklySelect){
            if(weeklyActive){
                weeklyLore.add(ChatColor.GRAY + "Progress : " + pdc.get(player, "weeklyQuestProgress"));
            }else{
                weeklyLore.add(ChatColor.GREEN + "Click to pick a random weekly quest!");
            }
        }else {
            weeklyLore.add(ChatColor.RED + "You already did a quest this week!");
            weeklyLore.add(ChatColor.GRAY + "You can do this quest again in: " +(168 - (((System.currentTimeMillis() - weeklyLast)/1000)/60)/60 + " hours."));
        }
        weeklyMeta.setLore(weeklyLore);
        weekly.setItemMeta(weeklyMeta);

        ItemStack daily = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta dailyMeta = daily.getItemMeta();
        dailyMeta.setDisplayName(ChatColor.GOLD + "Daily Quest");
        if(dailyActive && !pdc.get(player, "activeDailyQuest").equals("null")){
            dailyMeta.setDisplayName(mc.analyzers.survivaladdons2.quests.Quests.dailyQuest.getPrettyNameFromId(pdc.get(player, "activeDailyQuest")));
        }
        ArrayList<String> dailyLore = new ArrayList<>();
        if(dailySelect){
            if(dailyActive){
                dailyLore.add(ChatColor.GRAY + "Progress : " + pdc.get(player, "dailyQuestProgress"));
            }else{
                dailyLore.add(ChatColor.GREEN + "Click to pick a random daily quest!");
            }
        }else {
            dailyLore.add(ChatColor.RED + "You already did a quest today!");
            dailyLore.add(ChatColor.GRAY + "You can do this quest again in: " +(12 - (((System.currentTimeMillis() - dailyLast)/1000)/60)/60 + " hours."));
        }
        dailyMeta.setLore(dailyLore);
        daily.setItemMeta(dailyMeta);

        ItemStack hourly = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta hourlyMeta = hourly.getItemMeta();
        hourlyMeta.setDisplayName(ChatColor.GOLD + "Hourly Quest");
        if(hourlyActive && !pdc.get(player, "activeHourlyQuest").equals("null")){
            hourlyMeta.setDisplayName(mc.analyzers.survivaladdons2.quests.Quests.hourlyQuest.getPrettyNameFromId(pdc.get(player, "activeHourlyQuest")));
        }
        ArrayList<String> hourlyLore = new ArrayList<>();
        if(hourlySelect){
            if(hourlyActive){
                hourlyLore.add(ChatColor.GRAY + "Progress : " + pdc.get(player, "hourlyQuestProgress"));
            }else{
                hourlyLore.add(ChatColor.GREEN + "Click to pick a random hourly quest!");
            }
        }else {
            hourlyLore.add(ChatColor.RED + "You already did a quest this hour!");
            hourlyLore.add(ChatColor.GRAY + "You can do this quest again in: " +(60 - (((System.currentTimeMillis() - hourlyLast)/1000)/60) + " minutes.") );
        }
        hourlyMeta.setLore(hourlyLore);
        hourly.setItemMeta(hourlyMeta);

        quests.setItem(15, weekly);
        quests.setItem(13, daily);
        quests.setItem(11, hourly);

        player.openInventory(quests);

        return true;
    }
}
