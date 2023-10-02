package de.mertero.falldown.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockChangeListener implements Listener{

	@EventHandler
	public void handleBlockChange(BlockSpreadEvent event) {
		event.setCancelled(true);
	}
	
}