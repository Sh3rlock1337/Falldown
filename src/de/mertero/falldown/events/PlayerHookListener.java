package de.mertero.falldown.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;

public class PlayerHookListener implements Listener {

	@EventHandler
	public void onFish(PlayerFishEvent e) {
		Player p = e.getPlayer();
		FishHook hook = e.getHook();

		if (Main.getInstance().getState() == GameState.BUY || Main.getInstance().getState() == GameState.PVP
				&& Main.getInstance().getAlive().contains(p) && !Main.getInstance().getItemVanish().contains(p)) {

			if (hook.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
				if (hook.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.WATER) {
					Location ploc = p.getLocation();
					Location hloc = hook.getLocation();

					Vector vec = p.getVelocity();
					double distance = ploc.distance(hloc);
					vec.setX((0.9D * distance) * (hloc.getX() - ploc.getX()) / distance);
					vec.setY((0.4D * distance) * (hloc.getY() - ploc.getY()) / distance);
					vec.setZ((0.9D * distance) * (hloc.getZ() - ploc.getZ()) / distance);

					p.setVelocity(vec);
					p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 3.0F, 0.5F);
				} else {
					e.setCancelled(true);
				}
			}
		}
	}
}