package de.mertero.falldown.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.mertero.falldown.main.Main;
import net.twerion.api.events.PlayerNickEvent;
import net.twerion.api.events.PlayerUnnickEvent;

public class NickListener implements Listener {

	@EventHandler
	public void handleNick(PlayerNickEvent event) {
		this.handleNick();
	}

	@EventHandler
	public void handleUnnick(PlayerUnnickEvent event) {
		this.handleNick();
	}

	private void handleNick() {
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
			Main.getInstance().getSpectate().forEach((spectate) -> {
				Main.getInstance().getAlive().forEach((alive) -> {
					alive.hidePlayer(spectate);
				});
			});
		} , 5);
	}
}