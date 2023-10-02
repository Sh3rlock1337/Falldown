package de.mertero.falldown.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;
import de.mertero.falldown.manager.Coins;
import de.mertero.falldown.manager.Points;
import de.piet.cloud.api.CloudAPI;
import net.twerion.api.CloudServerState;
import net.twerion.nick.PlayerNickData;

public class DeathListener implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage(null);
		e.setKeepInventory(true);

		Player p = e.getEntity();

		// Wenn er in Alive drin ist und GAMESTATE = PVP ist und es keinen
		// Gewinner gibt
		if (Main.getInstance().getAlive().contains(p) && Main.getInstance().getState() == GameState.PVP) {
			e.setKeepLevel(true);
			// Toter ist in Last Damager drin
			if (Main.getInstance().getLastDamager().containsKey(p)) {
				// Wurde gekillt von
				Player k = Main.getInstance().getLastDamager().get(p);
				String playerName = CloudAPI.getPlayerAPI().getRank(p).getRankColor() + p.getName(),
						killerName = CloudAPI.getPlayerAPI().getRank(k).getRankColor() + k.getName();

				if (CloudAPI.getPlayerAPI().hasNickName(p)) {
					PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(p);
					playerName = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
				}

				if (CloudAPI.getPlayerAPI().hasNickName(k)) {
					PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(k);
					killerName = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
				}

				double hp = Math.round(k.getHealth()) / 2;
				if (hp == 0.0)
					hp = 0.5;

				Bukkit.broadcastMessage(Main.getInstance().getPrefix() + playerName + " §7wurde von " + killerName
						+ " §c" + hp + " ❤ " + " §7getötet.");

				setLeben(p, p.getLocation(), e);
				addKill(Main.getInstance().getLastDamager().get(p));
			} else {

				String name = CloudAPI.getPlayerAPI().getRank(p).getRankColor() + p.getName();

				if (CloudAPI.getPlayerAPI().hasNickName(p)) {
					PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(p);
					name = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
				}

				Bukkit.broadcastMessage("§c" + Main.getInstance().getPrefix() + name + " §7 ist gestorben");
				setLeben(p, p.getLocation(), e);
				return;
			}

			// Wenn er der letzte Spieler war hat er gewonnen
			if (Main.getInstance().getAlive().size() == 1) {
				Player k = Main.getInstance().getLastDamager().get(p);
				killerWin(k);
				return;
				// Wenn der letzte Tote gewonnen hat
			} else if (Main.getInstance().getAlive().size() == 0) {
				lastPlayerWin(p);
				return;
			}

			// forcerespawn
			doRespawn(p);

		} else {
			if (Main.getInstance().getAlive().contains(p)) {
				Main.getInstance().getAlive().remove(p);
				if (!Main.getInstance().getSpectate().contains(p)) {
					Main.getInstance().getSpectate().add(p);
				}
			} else {
				if (!Main.getInstance().getSpectate().contains(p)) {
					Main.getInstance().getSpectate().add(p);
				}
			}
		}

	}

	private void doRespawn(Player p) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				p.spigot().respawn();
				Main.getInstance().getLm().teleportAfterDeath(p);
			}
		}, 20);
	}

	@SuppressWarnings("deprecation")
	private void lastPlayerWin(Player p) {
		String name = CloudAPI.getPlayerAPI().getRank(p).getRankColor() + p.getName();

		if (CloudAPI.getPlayerAPI().hasNickName(p)) {
			PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(p);
			name = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
		}

		Bukkit.broadcastMessage(Main.getInstance().getPrefix() + name + "§7 hat gewonnen");

		for (Player alive : Bukkit.getOnlinePlayers()) {
			for (Player dead : Main.getInstance().getSpectate()) {
				alive.showPlayer(dead);
				dead.sendTitle("§6Falldown", name + " §3hat gewonnen");
			}
			alive.sendTitle("§6Falldown", name + " §3hat gewonnen");
		}

		int players = 1;
		for (Player player : Main.getInstance().sortByValues(Main.getInstance().getKills()).keySet()) {
			String nameWithPrefix = CloudAPI.getPlayerAPI().getRank(player).getRankColor() + player.getName();

			if (CloudAPI.getPlayerAPI().hasNickName(player)) {
				PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(player);
				nameWithPrefix = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
			}
			switch (players) {
			case 1:
				Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§6 Platz 1§8: " + nameWithPrefix);
				CloudAPI.getStatsAPI().increaseStatistic(player.getName(), "fd_points",
						Points.FIRST_PLAYER_WIN.getPoints());
				CloudAPI.getPlayerAPI().addCoins(player.getName(), Coins.WINNER.getCoins());
				break;
			case 2:
				Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§7 Platz 2§8: " + nameWithPrefix);
				CloudAPI.getStatsAPI().increaseStatistic(player.getName(), "fd_points",
						Points.SECOND_PLAYER_WIN.getPoints());
				CloudAPI.getPlayerAPI().addCoins(player.getName(), Coins.WINNER2.getCoins());
				break;
			case 3:
				Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§c Platz 3§8: " + nameWithPrefix);

				CloudAPI.getStatsAPI().increaseStatistic(player.getName(), "fd_points",
						Points.THIRD_PLAYER_WIN.getPoints());
				CloudAPI.getPlayerAPI().addCoins(player.getName(), Coins.WINNER3.getCoins());
				break;
			default:
				break;
			}
			players++;
		}

		Bukkit.getOnlinePlayers().forEach((player) -> {
			Main.getInstance().getScoreboardManager().setEndScoreboard();
		});

		int kills = Main.getInstance().getKills().get(p);
		int leben = Main.getInstance().getLeben().get(p);
		int tode = 3 - leben;
		p.sendMessage("§7-= §aStatistiken der Runde §7=-");
		p.sendMessage("§7-= §3Kills: §f" + kills + " §7=-");
		p.sendMessage("§7-= §3Leben: §f" + leben + " §7=-");
		p.sendMessage("§7-= §3Tode: §f" + tode + " §7=-");

		CloudAPI.getStatsAPI().increaseStatistic(p.getName(), "fd_kills", kills);
		CloudAPI.getStatsAPI().increaseStatistic(p.getName(), "fd_deaths", tode);
		CloudAPI.getStatsAPI().increaseStatistic(p.getName(), "fd_wins", 1);
		CloudAPI.getStatsAPI().increaseStatistic(p.getName(), "fd_played_games", 1);

		// Restart startet
		Main.getInstance().getCountdown().startrestartcd();
		CloudAPI.getServerAPI().changeServer(CloudServerState.INGAME, "ENDING 8x1");
	}

	@SuppressWarnings("deprecation")
	private void killerWin(Player k) {

		if (Main.getInstance().getAlive().size() == 1) {
			// Alle Sichtbar machen
			String name = CloudAPI.getPlayerAPI().getRank(k).getRankColor() + k.getName();

			if (CloudAPI.getPlayerAPI().hasNickName(k)) {
				PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(k);
				name = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
			}

			Bukkit.broadcastMessage(Main.getInstance().getPrefix() + name + "§7 hat gewonnen");

			for (Player alive : Bukkit.getOnlinePlayers()) {
				for (Player dead : Main.getInstance().getSpectate()) {
					alive.showPlayer(dead);
					dead.sendTitle("§6Falldown", name + " §3hat gewonnen");
				}
				alive.sendTitle("§6Falldown", name + " §3hat gewonnen");
			}

			int players = 1;
			for (Player player : Main.getInstance().sortByValues(Main.getInstance().getKills()).keySet()) {
				String nameWithPrefix = CloudAPI.getPlayerAPI().getRank(player).getRankColor() + player.getName();

				if (CloudAPI.getPlayerAPI().hasNickName(player)) {
					PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(player);
					nameWithPrefix = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
				}
				switch (players) {
				case 1:
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§6 Platz 1§8: " + nameWithPrefix);
					CloudAPI.getStatsAPI().increaseStatistic(player.getName(), "fd_points",
							Points.FIRST_PLAYER_WIN.getPoints());
					CloudAPI.getPlayerAPI().addCoins(player.getName(), Coins.WINNER.getCoins());
					break;
				case 2:
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§7 Platz 2§8: " + nameWithPrefix);
					CloudAPI.getStatsAPI().increaseStatistic(player.getName(), "fd_points",
							Points.SECOND_PLAYER_WIN.getPoints());
					CloudAPI.getPlayerAPI().addCoins(player.getName(), Coins.WINNER2.getCoins());
					break;
				case 3:
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§c Platz 3§8: " + nameWithPrefix);

					CloudAPI.getStatsAPI().increaseStatistic(player.getName(), "fd_points",
							Points.THIRD_PLAYER_WIN.getPoints());
					CloudAPI.getPlayerAPI().addCoins(player.getName(), Coins.WINNER3.getCoins());
					break;
				default:
					break;
				}
				players++;
			}

			Bukkit.getOnlinePlayers().forEach((player) -> {
				Main.getInstance().getScoreboardManager().setEndScoreboard();
			});

			int kills = Main.getInstance().getKills().getOrDefault(k, 0);
			int leben = Main.getInstance().getLeben().getOrDefault(k, 3);
			int tode = 3 - leben;
			k.sendMessage("§7-= §aStatistiken der Runde §7=-");
			k.sendMessage("§7-= §3Kills: §f" + kills + " §7=-");
			k.sendMessage("§7-= §3Leben: §f" + 0 + " §7=-");
			k.sendMessage("§7-= §3Tode: §f" + tode + " §7=-");

			CloudAPI.getStatsAPI().increaseStatistic(k.getName(), "fd_kills", kills);
			CloudAPI.getStatsAPI().increaseStatistic(k.getName(), "fd_deaths", 3 - leben);
			CloudAPI.getStatsAPI().increaseStatistic(k.getName(), "fd_wins", 1);
			CloudAPI.getStatsAPI().increaseStatistic(k.getName(), "fd_played_games", 1);

			CloudAPI.getPlayerAPI().addCoins(k.getName(), Coins.KILL.getCoins());
			CloudAPI.getServerAPI().changeServer(CloudServerState.INGAME, "ENDING");

			Main.getInstance().getCountdown().startrestartcd();
		}
	}

	private void setLeben(Player p, Location loc, PlayerDeathEvent e) {
		if (Main.getInstance().getLeben().containsKey(p) && Main.getInstance().getAlive().contains(p)) {
			int current = Main.getInstance().getLeben().getOrDefault(p, 1);
			if (current - 1 == 0) {

				int kills = Main.getInstance().getKills().getOrDefault(p, 0);
				p.sendMessage("§7-= §aStatistiken der Runde §7=-");
				p.sendMessage("§7-= §3Kills: §f" + kills + " §7=-");
				p.sendMessage("§7-= §3Leben: §f" + 0 + " §7=-");
				p.sendMessage("§7-= §3Tode: §f" + 3 + " §7=-");

				CloudAPI.getStatsAPI().increaseStatistic(p.getName(), "fd_kills", kills);
				CloudAPI.getStatsAPI().increaseStatistic(p.getName(), "fd_deaths", 3);
				CloudAPI.getStatsAPI().increaseStatistic(p.getName(), "fd_played_games", 1);

				Main.getInstance().getLeben().remove(p);
				Main.getInstance().getAlive().remove(p);

				Main.getInstance().getSpectate().add(p);
				Main.getInstance().getScoreboardManager().removeScoreboard(p);

				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 90000, 2));
					}
				}, 20);
				p.setLevel(0);
				p.setExp(0);
				// Unsichtbar für alle lebenden
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
					for (Player alive : Main.getInstance().getAlive()) {
						alive.hidePlayer(p);
					}

					// Sichtbar für alle toten und toten sichtbar für ihn
					for (Player dead : Main.getInstance().getSpectate()) {
						dead.showPlayer(p);
						p.showPlayer(dead);
					}
				});

				createDeathChest(p, loc);
				e.getDrops().clear();

				Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
					p.closeInventory();
					loc.getBlock().setType(Material.FLOWER_POT);

					Block block = loc.getBlock();
					
					

					if (Main.getInstance().getChestLoc().containsKey(block))
						Main.getInstance().getChestLoc().remove(block);
				}, 200);
				return;
			}
			Main.getInstance().getLeben().replace(p, current - 1);
			return;
		} else {
			p.kickPlayer("ERROR 440");
		}
	}

	private void addKill(Player p) {
		Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
			if (!Main.getInstance().getKills().containsKey(p)) {
				Main.getInstance().getKills().put(p, 1);
			} else {
				int current = Main.getInstance().getKills().get(p);
				Main.getInstance().getKills().replace(p, current + 1);
			}

			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
			CloudAPI.getStatsAPI().increaseStatistic(p.getName(), "fd_points", Points.KILL.getPoints());
			CloudAPI.getPlayerAPI().addCoins(p.getName(), Coins.KILL.getCoins());
			p.playSound(p.getLocation(), Sound.ENDERMAN_HIT, 3.0F, 0.5F);
		});
	}

	private void createDeathChest(Player p, Location loc) {
		Block blockchest = loc.getWorld().getBlockAt(loc.add(0, 0.5, 0));
		blockchest.setType(Material.ENDER_CHEST);

		Inventory inv = Bukkit.createInventory(null, 5 * 9, "DeathChest");
		inv.clear();
		inv.setContents(p.getInventory().getContents());
		inv.setItem(40, p.getInventory().getHelmet());
		inv.setItem(41, p.getInventory().getChestplate());
		inv.setItem(42, p.getInventory().getLeggings());
		inv.setItem(43, p.getInventory().getBoots());

		Main.getInstance().getChestLoc().put(blockchest, inv);

		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
	}
}