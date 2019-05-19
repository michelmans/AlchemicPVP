package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.al.configurations.Messenger;
import com.alchemi.alchemicpvp.Config;
import com.alchemi.alchemicpvp.Config.MESSAGES;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.meta.NickMeta;
import com.alchemi.alchemicpvp.meta.StatsMeta;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class NickCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player && main.getInstance().hasPermission(sender, "alchemicpvp.nick") && args.length == 1) {
			Player player = (Player) sender;
			
			Matcher m = Pattern.compile("&[1234567890klnor]").matcher(args[0]);
			
			if (args[0].length() > Config.NICKNAME.CHARACTERLIMIT.asInt()) {
				main.getInstance().getMessenger().sendMessage(MESSAGES.NICK_TOOLONG.value()
						.replace("$name$", args[0])
						.replace("$amount$", Config.NICKNAME.CHARACTERLIMIT.asString()), sender);
				
				return true;
			} 
			
			if (m.find() && !main.getInstance().hasPermission(sender, "alchemicpvp.nick.format") && !Config.NICKNAME.ALLOW_FORMAT.asBoolean()) {
				sender.sendMessage(Messenger.cc(MESSAGES.NICK_NOFORMAT.value()
						.replace("$name$", args[0])
						.replace("$eg$", "&k, &6, &1, etc.")));
				return true;
			}
			
			if (WhoCommand.whoIs(args[0]) != null 
					&& !WhoCommand.whoIs(args[0]).equals(player)) {
				
				TextComponent mainComponent = new TextComponent(Messenger.cc(MESSAGES.NICK_TAKEN1.value().replace("$name$", args[0])));
				TextComponent clickComponent = new TextComponent("\n" + Messenger.cc(MESSAGES.NICK_TAKEN2.value()));
				clickComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("Click me for the command").create()));
				clickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/nick "));
				mainComponent.addExtra(clickComponent);
				player.spigot().sendMessage(mainComponent);
				
				return true;
			}
			
			player.setDisplayName(args[0]);
			player.setMetadata(NickMeta.class.getSimpleName(), new NickMeta(args[0]));
			StatsMeta.getStats(player).setNickname(args[0]);
			
			main.getInstance().getMessenger().sendMessage(MESSAGES.NICK_NEW.value()
					.replace("$name$", args[0])
					.replace("$player$", sender.getName()), sender);
			
		} else if (sender instanceof Player) {
			main.getInstance().getMessenger().sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
		}
		
		return true;
	}

}
