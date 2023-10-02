package de.mertero.falldown.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.mertero.falldown.main.Main;

public class RespawnListener implements Listener {

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();

		// Wenn er nicht mehr als Lebender eingetragen ist, wird ein ein
		// Spectater
		if (!Main.getInstance().getAlive().contains(p)) {
			p.getInventory().addItem(mCompass());
			p.setAllowFlight(true);
			p.setFlying(true);
			e.setRespawnLocation(e.getPlayer().getLocation());
		}

	}

	private ItemStack mCompass() {
		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemMeta compassmeta = compass.getItemMeta();
		compassmeta.setDisplayName("§eTeleporter");
		compass.setItemMeta(compassmeta);
		return compass;
	}
}