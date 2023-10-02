package de.mertero.falldown.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			e.setCancelled(false);
		} else {
			e.setCancelled(true);
		}
	}

}
