package de.mertero.falldown.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;
import de.piet.cloud.api.CloudAPI;
import de.piet.cloud.spigotplugin.permissions.listener.CloudChatEvent;

public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		// Nicht Lobby
		if(!Main.getInstance().getState().equals(GameState.LOBBY) && !Main.getInstance().getState().equals(GameState.ENDING)) {
			// Er ist spectator
			if (Main.getInstance().getSpectate().contains(p)) {
				
				for (Player all : Bukkit.getOnlinePlayers()) {
					// Er ist nicht am Leben
					if (!Main.getInstance().getAlive().contains(all)) {
						
						if (CloudAPI.getPlayerAPI().hasNickName(all)) {
							all.sendMessage("§7[§4X§7] " + CloudAPI.getPlayerAPI().getNickData(p).getNickname() + "§8: "
									+ e.getMessage());
						} else {
							all.sendMessage("§7[§4X§7] " + p.getName() + "§8: " + e.getMessage());
						}
					}
				}
			}	
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onCloudChat(CloudChatEvent e) {
		if (!Main.getInstance().getState().equals(GameState.LOBBY) & Main.getInstance().getSpectate().contains(e.getPlayer().getPlayer()) & !Main.getInstance().getState().equals(GameState.ENDING)) {
			e.setCancelled(true);
		}
	}
	
	
	
}
