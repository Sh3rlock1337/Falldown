package de.mertero.falldown.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import de.mertero.falldown.main.Main;

public class InventoryClickListener implements Listener {

	int benutzt;

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		if (e.getInventory().getName() == "§eTeleporter" && e.getCurrentItem() != null) {
			if (Main.getInstance().getSpectate().contains(p)) {
				e.setCancelled(true);

				String playername = e.getCurrentItem().getItemMeta().getDisplayName();

				if (Main.getInstance().getAlive().contains(Bukkit.getPlayer(playername))) {
					Player tar = Bukkit.getPlayer(playername);
					p.teleport(tar);
					p.sendMessage(Main.getInstance().getPrefix() + "§7Du beobachtest nun §a" + tar.getName());
				} else {
					p.sendMessage(Main.getInstance().getPrefix() + "§cDieser Spieler ist nicht am leben");
				}
			}
		}
	}
}