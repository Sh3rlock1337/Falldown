package de.mertero.falldown.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;

public class PickUpItemListener implements Listener {

	// Wenn es in der Arena ist und er kein Spectater ist, kann er Sachen
	// aufnehmen
	@EventHandler
	public void onPickUpItem(PlayerPickupItemEvent e) {
		if (Main.getInstance().getState() == GameState.LOBBY || Main.getInstance().getState() == GameState.INGAME
				|| Main.getInstance().getSpectate().contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
}