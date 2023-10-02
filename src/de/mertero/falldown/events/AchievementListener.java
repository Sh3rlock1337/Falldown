package de.mertero.falldown.events;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public class AchievementListener implements Listener {
	
	@EventHandler
	public void achiev(PlayerAchievementAwardedEvent e){
			e.setCancelled(true);
	}
	
}
