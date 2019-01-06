package com.alchemi.alchemicpvp.listeners.cmds;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alchemi.al.Messenger;
import com.alchemi.alchemicpvp.main;
import com.alchemi.alchemicpvp.meta.NickMeta;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class NickCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player && main.instance.hasPermission(sender, "alchemicpvp.nick") && args.length == 1) {
			Player player = (Player) sender;
			
			Matcher m = Pattern.compile("&[1234567890klnor]").matcher(args[0]);
			
			if (args[0].length() > main.instance.config.getInt("Nickname.characterlimit")) {
				main.instance.messenger.sendMessage("Nick.TooLong", sender, new HashMap<String, Object>(){
					{
						put("$name$", args[0]);
						put("$amount$", main.instance.config.getInt("Nickname.characterlimit"));
					}
				});
				return true;
			} 
			
			if (m.find() && !main.instance.hasPermission(sender, "alchemicpvp.nick.format") && !main.instance.config.getBoolean("Nickname.allowFormat")) {
				sender.sendMessage(Messenger.parseVars(Messenger.cc(main.instance.messenger.getMessage("Nick.NoFormat")), new HashMap<String, Object>(){
					{
						put("$name$", args[0]);
						put("$eg$", "&k, &6, &1, etc.");
					}
				}));
				return true;
			}
			
			if (WhoCommand.whoIs(args[0]) != null 
					&& !WhoCommand.whoIs(args[0]).equals(player)) {
				
				TextComponent mainComponent = new TextComponent(Messenger.cc(Messenger.parseVars(main.instance.messenger.getMessage("Nick.Taken1"), new HashMap<String, Object>(){
					{
						put("$name$", args[0]);
					}
				}))) ;
				TextComponent clickComponent = new TextComponent("\n" + Messenger.cc(main.instance.messenger.getMessage("Nick.Taken2")));
				clickComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("Click me for the command").create()));
				clickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/nick "));
				mainComponent.addExtra(clickComponent);
				player.spigot().sendMessage(mainComponent);
				
				return true;
			}
			
			player.setDisplayName(args[0]);
			player.setMetadata(NickMeta.NAME, new NickMeta(args[0]));
			main.instance.getPlayer(player.getName()).setNickname(args[0]);
			
			main.instance.messenger.sendMessage("Nick.New", sender, new HashMap<String, Object>(){
				{
					put("$name$", args[0]);
					put("$player$", sender.getName());
				}
			});
		} else if (sender instanceof Player) {
			main.instance.messenger.sendMessage("NoPermission", sender, new HashMap<String, Object>(){
				{
					put("$command$", "/nick");
				}
			});
		}
		
		return true;
	}

}
