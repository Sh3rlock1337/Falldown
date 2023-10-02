package de.mertero.falldown.events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.mertero.falldown.main.Main;

public class FlashbangListener implements Listener {

	@EventHandler
	public void flashbangHit(ProjectileHitEvent event) {

		try {
			Player shooter = (Player) event.getEntity().getShooter();
			if (shooter.getItemInHand().getType() == Material.SNOW_BALL) {
				if (shooter.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§bFlashbang")) {
					if ((event.getEntity() instanceof Snowball)) {
						if (shooter instanceof Player) {

							int d = shooter.getItemInHand().getAmount();
							if (d - 1 == 0) {
								shooter.setItemInHand(new ItemStack(Material.AIR));
							} else {
								ItemStack sw = Main.getInstance().getItems().getFlashbang(d);
								shooter.setItemInHand(sw);
							}

							Entity e = event.getEntity();
							Location loc = event.getEntity().getLocation();
							World world = event.getEntity().getWorld();
							world.createExplosion(loc, 0.0F, false);

							List<Entity> entities = e.getNearbyEntities(5.0D, 5.0D, 5.0D);
							for (Entity entity : entities) {

								if ((entity instanceof Player)) {
									((Player) entity).addPotionEffect(
											new PotionEffect(PotionEffectType.BLINDNESS, 100, 1), true);
									((Player) entity).addPotionEffect(
											new PotionEffect(PotionEffectType.CONFUSION, 100, 1), true);
								}

							}

						}

					}
				}

			}
		} catch (Exception ex) {
		}

	}

}
