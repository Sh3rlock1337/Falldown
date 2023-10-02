package de.mertero.falldown.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;

public class InventoryMoveItemListener implements Listener {

	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		if (Main.getInstance().getState() == GameState.LOBBY) {
			event.setCancelled(true);
		}
		
		Main.getInstance().getSpectate().forEach((player)->{
			event.setCancelled(true);
		});
	}
}