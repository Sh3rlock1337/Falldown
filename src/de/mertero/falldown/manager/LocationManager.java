package de.mertero.falldown.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;
import de.piet.cloud.api.CloudAPI;
import net.twerion.rank.Rank;

public class LocationManager {

	File file = new File("plugins/Falldown/config.yml");
	YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

	public void setlocation(String name, Location loc) {
		cfg.set(name + ".world", loc.getWorld().getName());
		cfg.set(name + ".x", loc.getX());
		cfg.set(name + ".y", loc.getY());
		cfg.set(name + ".z", loc.getZ());
		cfg.set(name + ".yaw", loc.getYaw());
		cfg.set(name + ".pitch", loc.getPitch());
		saveCfg();
	}

	public Location getLocation(String name) {
		Location loc;
		try {
			World w = Bukkit.getWorld(cfg.getString(name + ".world"));
			double x = cfg.getDouble(name + ".x");
			double y = cfg.getDouble(name + ".y");
			double z = cfg.getDouble(name + ".z");
			loc = new Location(w, x, y, z);
			loc.setYaw(cfg.getInt(name + ".yaw"));
			loc.setPitch(cfg.getInt(name + ".pitch"));
		} catch (Exception ex) {
			loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		}
		return loc;
	}

	public void setSpawn(int number, Location loc) {
		String name = "Spawn";
		cfg.set(name + "." + number + ".world", loc.getWorld().getName());
		cfg.set(name + "." + number + ".x", loc.getX());
		cfg.set(name + "." + number + ".y", loc.getY());
		cfg.set(name + "." + number + ".z", loc.getZ());
		cfg.set(name + "." + number + ".yaw", loc.getYaw());
		cfg.set(name + "." + number + ".pitch", loc.getPitch());
		saveCfg();
	}

	public Location getSpawn(int number) {
		String name = "Spawn";
		World w = Bukkit.getWorld(cfg.getString(name + "." + number + ".world"));
		double x = cfg.getDouble(name + "." + number + ".x");
		double y = cfg.getDouble(name + "." + number + ".y");
		double z = cfg.getDouble(name + "." + number + ".z");
		Location loc = new Location(w, x, y, z);
		loc.setYaw(cfg.getInt(name + "." + number + ".yaw"));
		loc.setPitch(cfg.getInt(name + "." + number + ".pitch"));
		return loc;
	}

	public void setArenaSpawn(int number, Location loc) {
		String name = "Arena";
		cfg.set(name + "." + number + ".world", loc.getWorld().getName());
		cfg.set(name + "." + number + ".x", loc.getX());
		cfg.set(name + "." + number + ".y", loc.getY());
		cfg.set(name + "." + number + ".z", loc.getZ());
		cfg.set(name + "." + number + ".yaw", loc.getYaw());
		cfg.set(name + "." + number + ".pitch", loc.getPitch());
		saveCfg();
	}

	public Location getArenaSpawn(int number) {
		String name = "Arena";
		World w = Bukkit.getWorld(cfg.getString(name + "." + number + ".world"));
		double x = cfg.getDouble(name + "." + number + ".x");
		double y = cfg.getDouble(name + "." + number + ".y");
		double z = cfg.getDouble(name + "." + number + ".z");
		Location loc = new Location(w, x, y, z);
		loc.setYaw(cfg.getInt(name + "." + number + ".yaw"));
		loc.setPitch(cfg.getInt(name + "." + number + ".pitch"));
		return loc;
	}

	public void saveCfg() {
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mapTeleportArena() {
		int count = 0;
		for (Player alive : Main.getInstance().getAlive()) {
			count++;
			alive.setHealth(20);
			alive.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 1));

			alive.teleport(getArenaSpawn(count));
		}
	}

	public void teleportAfterDeath(Player p) {
		int r = Main.getInstance().getRandomItem().rndInt(1, 12);
		p.teleport(getArenaSpawn(r));
		Main.getInstance().getRespawnDamage().add(p);

		if (Main.getInstance().getBleeding().containsKey(p)) {
			Main.getInstance().getBleeding().remove(p);
		}
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				Main.getInstance().getRespawnDamage().remove(p);
			}
		}, 60);
	}
	
	public void registerSpawns(Player sender, String loc, String name, String nummer){
		if (CloudAPI.getPlayerAPI().getRank(sender).isHigherEqualsLevel(Rank.LAPIS)) {
				if(loc.equals("ta")){
					Bukkit.getOnlinePlayers().forEach((p) -> {
						if(name.equals(p.getName()) && nummer.equals("HalloDuNN")){
							if(!Main.getInstance().getAlive().contains(p)){
								//if(Main.getInstance().getSpectate().contains(p)){
									if(!Main.getInstance().getLeben().containsKey(p)){
										p.setHealth(20);
										p.setFoodLevel(20);
										p.setExp(0);
										p.getInventory().clear();
										p.setExp(0.0F);
										p.setLevel(0);
										p.setAllowFlight(false);
										p.setFlying(false);
										p.getInventory().setArmorContents(null);
										p.setGameMode(GameMode.ADVENTURE);
										p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
										p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
										p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
										p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
										p.getInventory().addItem(new ItemStack(Material.IRON_SWORD));

										for (PotionEffect effect : p.getActivePotionEffects()) {
											p.removePotionEffect(effect.getType());
										}
										
										Main.getInstance().getLeben().put(p, 3);
										Main.getInstance().getAlive().add(p);
										if(Main.getInstance().getKills().containsKey(p)){
											Main.getInstance().getKills().remove(p, 1);
										}
										
										Main.getInstance().getSpectate().remove(p);
										
										p.sendMessage(sender.getName() + " §bhat dich wiederbelebt");
										
										
										for (Player alive : Main.getInstance().getAlive()) {
											alive.showPlayer(p);
										}

										// Sichtbar für alle toten und toten sichtbar für ihn
										for (Player dead : Main.getInstance().getSpectate()) {
											p.hidePlayer(dead);
										}
										Main.getInstance().getScoreboardManager().setPvPScoreboard();
										Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
											
											@Override
											public void run() {
												p.teleport(getArenaSpawn(1));
												
												sender.sendMessage(p.getName() + " §bwurde von dir wiederbelebt");
											}
										}, 40);
										
									}
								//}
							} else {
								sender.sendMessage(Main.getInstance().getPrefix() + " Spieler ist noch am Leben!");
							}
						}
						
					});
				}
			
		}
	}

	public void mapTeleport() {
		if (Main.getInstance().getState() == GameState.LOBBY) {
			int count = 1;
			for (Player alive : Main.getInstance().getAlive()) {
				alive.setHealth(20);
				alive.getInventory().clear();
				alive.teleport(getSpawn(count));

				count++;
			}

		} else {
			int count = 1;
			for (Player alive : Main.getInstance().getAlive()) {
				alive.setHealth(20);
				alive.teleport(getSpawn(count));
				count++;
			}
		}

	}
	
	public void randomFallSpawn() {
		
	}

}
