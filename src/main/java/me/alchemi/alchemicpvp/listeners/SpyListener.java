package me.alchemi.alchemicpvp.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import me.alchemi.alchemicpvp.main;
import me.alchemi.alchemicpvp.Config.Messages;

public class SpyListener implements Listener{

	private final Player spy;
	
	private ArrayList<Player> ignored = new ArrayList<Player>();
	
	public SpyListener(Player spy) {
		this.spy = spy;
		Bukkit.getServer().getPluginManager().registerEvents(this, main.getInstance());
		main.getInstance().spies.put(spy.getName(), this);
	}
	
	public void ignorePlayer(Player player) {
		ignored.add(player);
		main.getInstance().getMessenger().sendMessage(Messages.SPY_IGNORESTART.value().replace("$player$", player.getDisplayName()), spy);
	}
	
	public boolean unIgnorePlayer(Player player) {
		main.getInstance().getMessenger().sendMessage(Messages.SPY_IGNORESTOP.value().replace("$player$", player.getDisplayName()), spy);
		return ignored.remove(player);
	}
	
	public boolean isIgnoring(Player player) {
		return ignored.contains(player);
	}
	
	public void stopSpying() {
		HandlerList.unregisterAll(this);
		main.getInstance().spies.remove(spy.getName());
	}
	
	@EventHandler
	public void onMessage(EventMessage e) {
		if (e.getSender() instanceof Player && !ignored.contains((Player) e.getSender()) && !main.getInstance().hasPermission(e.getSender(), "alchemicpvp.spy.hideFromBigBrother")&&
				e.getSender() != spy && e.getRecipient() != spy) {
			String msg = Messages.SPY_MESSAGE.value().replace("$sender$", ((Player)e.getSender()).getDisplayName()).replace("$message$", e.getMessage());
			
			if (e.getRecipient() instanceof Player) msg = msg.replace("$recipient$", ((Player)e.getRecipient()).getDisplayName());
			else msg = msg.replace("$recipient$", "Console");
			
			main.getInstance().getMessenger().sendMessage(msg, spy);
		}
	}
	
}
