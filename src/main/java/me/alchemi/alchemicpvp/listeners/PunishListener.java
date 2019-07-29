package me.alchemi.alchemicpvp.listeners;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.SirBlobman.combatlogx.config.ConfigOptions;
import com.SirBlobman.combatlogx.event.PlayerPunishEvent;
import com.SirBlobman.combatlogx.utility.CombatUtil;

import me.alchemi.alchemicpvp.main;

public class PunishListener implements Listener {

	public PunishListener() {
		Bukkit.getPluginManager().registerEvents(this, main.getInstance());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPunish(PlayerPunishEvent e) {
		
		Entity enemy = e.getPreviousEnemy();
		
		if (enemy == null) CombatUtil.getEnemy(e.getPlayer());
		if (enemy == null) {
			int randomInt = new Random().nextInt(Bukkit.getOnlinePlayers().size());
			enemy = (Entity) Arrays.asList(Bukkit.getOnlinePlayers()).get(randomInt);
		}
		
		if (ConfigOptions.PUNISH_KILL) {
			main.getInstance().addToKill(e.getPlayer());
		}
		
		if (ConfigOptions.PUNISH_SUDO) {
            List<String> commandList = ConfigOptions.PUNISH_SUDO_COMMANDS;
            for(String command : commandList) {
                if(command.startsWith("[CONSOLE]")) {
                    String cmd = CombatUtil.getSudoCommand(command.substring(9), e.getPlayer());
                    CommandSender console = Bukkit.getConsoleSender();
                    Bukkit.dispatchCommand(console, cmd);
                    continue;
                }
                
                if(command.startsWith("[PLAYER]")) {
                    String cmd = CombatUtil.getSudoCommand(command.substring(8), e.getPlayer());
                    e.getPlayer().performCommand(cmd);
                    continue;
                }
                
                if(command.startsWith("[OP]")) {
                    String cmd = CombatUtil.getSudoCommand(command.substring(4), e.getPlayer());
                    if(e.getPlayer().isOp()) {
                        e.getPlayer().performCommand(cmd);
                        continue;
                    }
                    
                    e.getPlayer().setOp(true);
                    e.getPlayer().performCommand(cmd);
                    e.getPlayer().setOp(false);
                    continue;
                }
            }
        }
	
		e.setCancelled(true);
	}
	
}
