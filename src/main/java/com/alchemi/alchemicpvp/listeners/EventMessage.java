package com.alchemi.alchemicpvp.listeners;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventMessage extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	
	private final String message;
	private final CommandSender recipient;
	private final CommandSender sender;
	private boolean cancelled;
	
	public EventMessage(String message, CommandSender recipient, CommandSender sender) {

		this.message = message;
		this.recipient = recipient;
		this.sender = sender;
		
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public String getMessage() {
		return message;
	}
	
	public CommandSender getRecipient() {
		return recipient;
	}
	
	public CommandSender getSender() {
		return sender;
	}

	@Override
	public boolean isCancelled() {

		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {

		cancelled = cancel;
		
	}

}
