package mc.analyzers.survivaladdons2.discord;

import mc.analyzers.survivaladdons2.SurvivalAddons2;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static mc.analyzers.survivaladdons2.SurvivalAddons2.chatChannel;

public class MessageSendEvent extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        if(!event.getChannel().equals(chatChannel)) return;
        Member member = event.getMember();
        if(member == null || member.getUser().isBot()) return;
        String message = event.getMessage().getContentDisplay();
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Discord " + ChatColor.GRAY + "|" + " " + ChatColor.of(Objects.requireNonNull(member.getColor()))+ member.getEffectiveName() + ChatColor.WHITE + ": " + message);
    }
}
