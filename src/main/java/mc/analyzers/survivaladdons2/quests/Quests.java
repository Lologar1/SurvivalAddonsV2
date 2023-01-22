package mc.analyzers.survivaladdons2.quests;

public class Quests {
    public static final Quest hourlyQuest = new Quest("hourly", new String[]{"kill/7/passive mobs", "mine/25/redstone dust",
            "kill/10/hostile mobs", "consume/5/foodstuffs", "mine/35/crops", "kill/6/nether mobs",
            "mine/50/blocks", "craft/40/items", "deal/100/damage", "land/50/arrow shots"}, 3600000, null, null, 0.8);
    public static final Quest dailyQuest = new Quest("hourly", new String[]{"kill/70/passive mobs", "craft/3/funky feather", "mine/250/redstone dust",
            "kill/100/hostile mobs", "consume/40/foodstuffs", "mine/160/crops", "mine/7/diamonds", "kill/110/nether mobs",
            "mine/300/blocks", "deal/1000/damage", "land/150/arrow shots", "brew/25/potions", "combine/25/items", "get/10/custom enchants",
            "craft/15/custom items"}, 43200000, null, null, 0.7);
    public static final Quest weeklyQuest = new Quest("hourly", new String[]{"craft/32/funky feather", "mine/5000/redstone dust",
            "kill/700/hostile mobs", "mine/500/crops", "mine/64/diamonds",
            "mine/2000/blocks", "brew/250/potions", "combine/100/items", "get/25/custom enchants", "craft/65/custom items"}, 604800000, null, null, 0.6);
}
