package mc.analyzers.survivaladdons2.quests;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import mc.analyzers.survivaladdons2.utility.pdc;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static mc.analyzers.survivaladdons2.utility.utility.*;

public class Quest {
    private final String type;
    private final String[] questIds;
    private final long delay;
    private HashMap<String, ItemStack> rareRewards;
    private HashMap<String, ItemStack> commonRewards;
    double commonPercent;

    public static void checkQuest(String action, String kind, Player player, int amount){
        if(pdc.get(player, "activeHourlyQuest").contains(action)){
            if(pdc.get(player, "activeHourlyQuest").contains(kind)) {
                incrementQuest("hourly", pdc.get(player, "activeHourlyQuest"), player, amount);
            }
        }
        if(pdc.get(player, "activeDailyQuest").contains(action)){
            if(pdc.get(player, "activeDailyQuest").contains(kind)) {
                incrementQuest("daily", pdc.get(player, "activeDailyQuest"), player, amount);
            }
        }
        if(pdc.get(player, "activeWeeklyQuest").contains(action)){
            if(pdc.get(player, "activeWeeklyQuest").contains(kind)) {
                incrementQuest("weekly", pdc.get(player, "activeWeeklyQuest"), player, amount);
            }
        }
    }

    public static void incrementQuest(String type, String questId, Player player, int amount){
        String currentProgress = pdc.get(player, type.toLowerCase(Locale.ROOT) + "QuestProgress");
        String[] split = currentProgress.split("/");
        String newProgress = (Integer.parseInt(split[0]) + amount) + "/" + split[1];
        pdc.set(player, type.toLowerCase(Locale.ROOT) + "QuestProgress", newProgress);

        if(Integer.parseInt(split[0]) + amount >= Integer.parseInt(split[1])){
            //Quest finished!
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + capFirst(type) + " quest finished!");
            List<String> keyList;
            HashMap<String, ItemStack> drops;
            if(percentChance(getByType(type).getCommonPercent())){
                keyList = new ArrayList<String>(getByType(type).commonRewards.keySet());
                drops = getByType(type).commonRewards;
            }else{
                keyList = new ArrayList<String>(getByType(type).rareRewards.keySet());
                drops = getByType(type).rareRewards;
            }

            int size = keyList.size();
            int randIdx = new Random().nextInt(size);

            String message = keyList.get(randIdx);
            ItemStack item = drops.get(message);

            player.sendMessage(ChatColor.GRAY + "Got " + message);
            if(item.getType().equals(Material.REDSTONE)){
                pdc.set(player, "dust", (String.valueOf( (Integer.parseInt(pdc.get(player, "dust")) + item.getAmount()) )));
            }else{
                giveItem(player, item, item.getAmount());
            }
            pdc.set(player, type.toLowerCase(Locale.ROOT) + "QuestProgress", "0/0");
            pdc.set(player, type.toLowerCase(Locale.ROOT) + "Last", String.valueOf(System.currentTimeMillis()));
            pdc.set(player, "active" + capFirst(type.toLowerCase(Locale.ROOT)) + "Quest", "null");
            pdc.set(player, "currentQuestType", "none");
            pdc.set(player, "deactivated" + capFirst(type.toLowerCase(Locale.ROOT)) + "Quest", "null");
        }
    }

    public static Quest getByType(String type){
        switch (type){
            case "hourly":
                return Quests.hourlyQuest;
            case "weekly":
                return Quests.weeklyQuest;
            default:
                return Quests.dailyQuest;
        }
    }

    public Quest(String type, String[] questIds, long delay, HashMap<String, ItemStack> rareRewards, HashMap<String, ItemStack> commonRewards, double commonPercent){
        this.type = type;
        this.questIds = questIds;
        this.delay = delay;
        this.rareRewards = rareRewards;
        this.commonRewards = commonRewards;
        this.commonPercent = commonPercent;
    }

    public void setCommonRewards(HashMap<String, ItemStack> commonRewards) {
        this.commonRewards = commonRewards;
    }

    public void setCommonPercent(double commonPercent) {
        this.commonPercent = commonPercent;
    }

    public void setRareRewards(HashMap<String, ItemStack> rareRewards) {
        this.rareRewards = rareRewards;
    }

    public boolean checkAvaliability(Player player){
        boolean canDo = false;
        long lastDid = Long.parseLong(pdc.get(player, type+"Last"));
        //Types: weeklyLast,dailyLast, etc.
        if(lastDid+delay <= System.currentTimeMillis()){
            canDo = true;
        }
        return canDo;
    }

    public String getPrettyNameFromId(String id){
        String[] split = id.split("/");
        String questType = split[0];
        String amount = split[1];
        String task = split[2];
        return ChatColor.YELLOW + capFirst(questType) + " " + ChatColor.BLUE + amount + " " + task;
    }

    public HashMap<String, ItemStack> getBiasedReward(){
        if(percentChance(commonPercent)){
            int rand = new Random().nextInt(commonRewards.size());
            List<String> keyList = new ArrayList<>(commonRewards.keySet());
            String randomKey = keyList.get(rand);

            HashMap<String, ItemStack> ret = new HashMap<>();
            ret.put(randomKey, commonRewards.get(randomKey));
            return ret;
        }
        int rand = new Random().nextInt(rareRewards.size());
        List<String> keyList = new ArrayList<>(rareRewards.keySet());
        String randomKey = keyList.get(rand);

        HashMap<String, ItemStack> ret = new HashMap<>();
        ret.put(randomKey, rareRewards.get(randomKey));
        return ret;
    }

    public String getRandomQuestId(){
        return questIds[new Random().nextInt(questIds.length)];
    }

    public double getCommonPercent() {
        return commonPercent;
    }

    public HashMap<String, ItemStack> getCommonRewards() {
        return commonRewards;
    }

    public HashMap<String, ItemStack> getRareRewards() {
        return rareRewards;
    }

    public String getType() {
        return type;
    }

    public long getDelay() {
        return delay;
    }
}
