package de.mertero.falldown.countdown;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;
import de.mertero.falldown.manager.Coins;
import de.mertero.falldown.manager.Points;
import de.piet.cloud.api.CloudAPI;
import de.piet.cloud.spigotplugin.permissions.util.PacketScoreboard;
import net.twerion.api.CloudServerState;
import net.twerion.nick.PlayerNickData;

public class Countdown {

	private boolean Lobbystarted = false;
	private boolean restarted = false;

	private int lobbycd;
	private int ingamecd;
	private int buycd;
	private int pvpcd;
	private int restartcd;
	private int waiting;
	private int blockcd;
	Player winner;

	public static int lobby = Main.getInstance().getLobbyTime();
	public int ingame = Main.getInstance().getIngameTime();
	public int buy = Main.getInstance().getBuyTime();
	public int pvp = Main.getInstance().getPvpTime();
	private int restart = Main.getInstance().getRestartTime();

	public void startlobbycd() {
		if (Lobbystarted == false) {
			Lobbystarted = true;
			lobbycd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					Main.getInstance().getScoreboardManager().getScoreboards().keySet().forEach((player) -> {
                        PacketScoreboard packetScoreboard = Main.getInstance().getScoreboardManager().getScoreboards().get(player);
                        if (packetScoreboard != null) {
                            packetScoreboard.setLine(2, " " + Main.getInstance().getAlive().size() + "§8/§f" + Main.getInstance().getMax());
                        }
                    
                });
					if (Main.getInstance().getAlive().size() >= 4) {
						if (lobby >= 1) {
							if (lobby == 20 || lobby == 15 || lobby == 10 || (lobby <= 5 && lobby >= 1)) {
								if (Countdown.lobby == 1) {
									Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Das Spiel startet in §7"
											+ Countdown.lobby + " §3Sekunde");
								} else {
									Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Das Spiel startet in §7"
											+ Countdown.lobby + " §3Sekunden");
									Bukkit.getOnlinePlayers().forEach((player) ->{
										player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0F, 10.0F);
									});
								}

							}
						} else if (lobby == 0) {
							for (Player pl : Bukkit.getOnlinePlayers()) {
								pl.sendTitle("§6FallDown", "§3Das Spiel beginnt");
							}
							Main.getInstance().getLm().mapTeleport();
							for (Player all : Main.getInstance().getAlive()) {
								all.getInventory().clear();
								all.getInventory().setItem(4, Main.getInstance().getItems().getMixerItem());
								all.getInventory().setHeldItemSlot(4);
							}
							
							CloudAPI.getServerAPI().changeServer(CloudServerState.INGAME, "Ingame");
							Main.getInstance().setState(GameState.INGAME);
							startJumpcd();
							Bukkit.getScheduler().cancelTask(lobbycd);

							Bukkit.getOnlinePlayers().forEach((player) -> {
								Main.getInstance().getKills().put(player, 0);
							});
						}
						
						lobby--;
						for (Player all : Bukkit.getOnlinePlayers()) {
							all.setLevel(lobby + 1);
						}
					} else {
						lobby = 30;
					}
				}
			}, 0, 20);

			waiting = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					if (Main.getInstance().getAlive().size() <= 3 && Main.getInstance().getState() == GameState.LOBBY) {
						Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Warten auf weitere Spieler!");
						if (Main.getInstance().getState() != GameState.LOBBY) {
							Bukkit.getScheduler().cancelTask(waiting);
						}
					}
				}
			}, 0, 10 * 30);
		}
	}

	public void startrestartcd() {
		Main.getInstance().setState(GameState.ENDING);

		Bukkit.getScheduler().cancelAllTasks();
		if (restarted == false) {
			restarted = true;
			Main.getInstance().getScoreboardManager().setEndScoreboard();
			restartcd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					if (restart >= 1) {
						if (restart == 15 || (restart <= 5 && restart >= 1)) {
							if (restart == 1) {
								Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Das Spiel restartet in §7"
										+ restart + " §3Sekunde");
							} else {
								Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Das Spiel restartet in §7"
										+ restart + " §3Sekunden");

							}

						}

					} else if (restart == 0) {

						Bukkit.getOnlinePlayers().forEach((player) -> {
							player.kickPlayer("lobby");
						});

						Bukkit.getScheduler().cancelTask(restartcd);
						Bukkit.shutdown();

					}
					restart--;

				}

			}, 0, 20);
		}
	}

	public void startJumpcd() {
		Main.getInstance().getScoreboardManager().setFallScoreboard();
		ingamecd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				for (Player all : Main.getInstance().getAlive()) {
					block(all);
				}
				Main.getInstance().getScoreboardManager().getScoreboards().keySet().forEach((player) -> {
					if (Main.getInstance().getAlive().contains(player)) {
						PacketScoreboard packetScoreboard = Main.getInstance().getScoreboardManager().getScoreboards()
								.get(player);
						if (packetScoreboard != null) {
							packetScoreboard.setLine(2,
									" §e" + Main.getInstance()
											.getCountdown().ingame
									+ ((Main.getInstance().getCountdown().ingame == 1) ? " Sekunde" : " Sekunden"));
						}
					}
				});
				if (ingame == 49) {
					Main.getInstance().getLm().mapTeleport();
				}

				if (ingame == 42) {
					Main.getInstance().getLm().mapTeleport();
				}
				if (ingame == 35) {
					Main.getInstance().getLm().mapTeleport();
				}
				if (ingame == 28) {
					Main.getInstance().getLm().mapTeleport();
				}
				if (ingame == 21) {
					Main.getInstance().getLm().mapTeleport();
				}
				if (ingame == 14) {
					Main.getInstance().getLm().mapTeleport();
				}
				if (ingame == 7) {
					Main.getInstance().getLm().mapTeleport();
				}

				if (ingame == 0) {
					Main.getInstance().setState(GameState.BUY);
					startBuycd();
					Main.getInstance().getLm().mapTeleportArena();

					Bukkit.getScheduler().cancelTask(ingamecd);
					Bukkit.getScheduler().cancelTask(blockcd);
				}

				if (Main.getInstance().getAlive().size() <= 1 && Main.getInstance().getState() != GameState.ENDING) {
					Main.getInstance().setState(GameState.ENDING);
					startrestartcd();
					CloudAPI.getServerAPI().changeServer(CloudServerState.INGAME, "Ingame");
					Bukkit.getScheduler().cancelTask(ingamecd);
					Bukkit.getScheduler().cancelTask(blockcd);
				}
				// WENN EINE BESTIMMTE HÖCHE ERREICHT WIRD
				/*
				 * if(!ModulListener.modul5.isEmpty()){ Main.getInstance().state
				 * = GameState.BUYTIME; startBuycd();
				 * Main.getInstance().getLm().mapTeleportArena();
				 * Bukkit.getScheduler().cancelTask(jumpcd); }
				 */
				ingame--;

			}

		}, 0, 17);
	}

	public void startPvPcd() {
		Main.getInstance().getScoreboardManager().setPvPScoreboard();
		Main.getInstance().setState(GameState.PVP);
		pvpcd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Main.getInstance().getScoreboardManager().getScoreboards().keySet().forEach((player) -> {
					if (Main.getInstance().getAlive().contains(player)) {
						PacketScoreboard packetScoreboard = Main.getInstance().getScoreboardManager().getScoreboards()
								.get(player);
						packetScoreboard.setLine(11,
								" " + ((Main.getInstance().getModusChange() == 0) ? "§3Normal" : "§4Hardcore"));
						packetScoreboard.setLine(8, " " + ((Main.getInstance().getTeams() == 0) ? "§4Verboten" : "§3Erlaubt"));
						packetScoreboard.setLine(6, " §6Leben");
						
						if (Main.getInstance().getLeben().get(player) == 1) {
							packetScoreboard.setLine(5, "  §b" + Main.getInstance().getLeben().get(player) + "§8/§a3");
						} else {
							packetScoreboard.setLine(5, " §c" + Main.getInstance().getLeben().get(player) + "§8/§a3");
						}
						packetScoreboard.setLine(3, " §6Kills");
						packetScoreboard.setLine(2, " §b" + Main.getInstance().getKills().get(player));
					}
				});
				if (pvp == 480) {
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Die PvP Phase fängt an!");
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Teams sind §4VERBOTEN!");
				} else if (pvp == 240) {
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Halbzeit!");
				} else if (pvp == 60) {
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Nur noch 1 Minute!");
				} else if (pvp == 10) {
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Nur noch 10 Sekunden!");
				} else if (pvp <= 3 && pvp != 0) {
					for (Player all : Main.getInstance().getAlive()) {
						all.sendTitle("§3" + pvp, "");
					}
				} else if (pvp == 0) {

					int players = 1;
					for (Player player : Main.getInstance().sortByValues(Main.getInstance().getKills()).keySet()) {
						String nameWithPrefix = CloudAPI.getPlayerAPI().getRank(player).getRankColor()
								+ player.getName();

						if (CloudAPI.getPlayerAPI().hasNickName(player)) {
							PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(player);
							nameWithPrefix = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
						}
						switch (players) {
						case 1:
							Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§6 Platz 1§8: " + nameWithPrefix);
							winner = player;
							CloudAPI.getStatsAPI().increaseStatistic(player.getName(), "fd_points",Points.FIRST_PLAYER_WIN.getPoints());
							CloudAPI.getPlayerAPI().addCoins(player.getName(), Coins.WINNER.getCoins());
							break;
						case 2:
							Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§7 Platz 2§8: " + nameWithPrefix);
							CloudAPI.getStatsAPI().increaseStatistic(player.getName(), "fd_points", Points.SECOND_PLAYER_WIN.getPoints());
							CloudAPI.getPlayerAPI().addCoins(player.getName(), Coins.WINNER2.getCoins());
							break;
						case 3:
							Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§c Platz 3§8: " + nameWithPrefix);
							
							CloudAPI.getStatsAPI().increaseStatistic(player.getName(), "fd_points",Points.THIRD_PLAYER_WIN.getPoints());
							CloudAPI.getPlayerAPI().addCoins(player.getName(), Coins.WINNER3.getCoins());
							break;
						default:
							break;
						}
						players++;
					}

					if (winner != null) {
						String winnerName = CloudAPI.getPlayerAPI().getRank(winner).getRankColor() + winner.getName();

						if (CloudAPI.getPlayerAPI().hasNickName(winner)) {
							PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(winner);
							winnerName = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
						}

						Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Gewonnen hat " + winnerName);
						for (Player alive : Main.getInstance().getAlive()) {

							alive.sendTitle(winnerName, "§6hat gewonnen");
							if (alive == winner) {

								int kills = Main.getInstance().getKills().getOrDefault(alive, 0);
								CloudAPI.getStatsAPI().increaseStatistic(alive.getName(), "fd_kills", kills);
								CloudAPI.getStatsAPI().increaseStatistic(alive.getName(), "fd_deaths", 3);
								CloudAPI.getStatsAPI().increaseStatistic(alive.getName(), "fd_played_games", 1);

								alive.playSound(alive.getLocation(), Sound.LEVEL_UP, 3.0F, 0.5F);
								Firework fw = (Firework) winner.getWorld().spawnEntity(winner.getLocation(),
										EntityType.FIREWORK);
								FireworkMeta fwm = fw.getFireworkMeta();

								// Our random generator
								Random r = new Random();

								// Get the type
								int rt = r.nextInt(4) + 1;
								Type type = Type.BALL;
								if (rt == 1)
									type = Type.BALL;
								if (rt == 2)
									type = Type.BALL_LARGE;
								if (rt == 3)
									type = Type.BURST;
								if (rt == 4)
									type = Type.CREEPER;
								if (rt == 5)
									type = Type.STAR;

								// Get our random colours
								int r1i = r.nextInt(10) + 1;
								int r2i = r.nextInt(10) + 1;
								Color c1 = getColor(r1i);
								Color c2 = getColor(r2i);

								// Create our effect with this
								FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1)
										.withFade(c2).with(type).trail(r.nextBoolean()).build();
								// Then apply the effect to the meta
								fwm.addEffect(effect);
								// Generate some random power and set it
								int rp = r.nextInt(2) + 1;
								fwm.setPower(rp);

								// Then apply this to our rocket
								fw.setFireworkMeta(fwm);

							}
						}

					} else {
						Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Keiner hat gewonnen!");
					}

					Main.getInstance().setState(GameState.ENDING);
					Bukkit.getScheduler().cancelTask(pvpcd);
					startrestartcd();
					Main.getInstance().getScoreboardManager().setEndScoreboard();
				}
				pvp--;
			}
		}, 0, 20);
	}

	public void startBuycd() {
		Main.getInstance().getScoreboardManager().setShopScoreboard();
		Main.getInstance().setState(GameState.BUY);

		buycd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach((player) -> {
					PacketScoreboard packetScoreboard = Main.getInstance().getScoreboardManager().getScoreboards()
							.get(player);
					if (packetScoreboard != null) {
						packetScoreboard.setLine(2, " §e" + Main.getInstance().getCountdown().buy
								+ ((Main.getInstance().getCountdown().buy == 1) ? " Sekunde" : " Sekunden"));
					}
				});
				switch (buy) {
				case 60:
				case 30:
				case 5:
				case 4:
				case 3:
				case 2:
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Nur noch " + buy + " Sekunden!");
					break;
				case 1:
					Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "Nur noch 1 Sekunde!");
					break;
				case 0:
					Main.getInstance().setState(GameState.PVP);
					startPvPcd();
					Bukkit.getScheduler().cancelTask(buycd);
					break;
				default:
					break;
				}
				buy--;
			}

		}, 0, 20);
	}

	private void block(Player p) {
		if (Main.getInstance().getState() == GameState.INGAME) {

			ArrayList<ItemStack> d = Main.getInstance().getRandomItem().getRandomItemList();
			blockcd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					if (!Main.getInstance().getState().equals(GameState.INGAME)) {
						Bukkit.getScheduler().cancelTask(blockcd);
					}

					if (getNearbyEntities(p.getLocation()) == true) {
						for (int faktor = 0; faktor < d.size(); faktor++) {
							if (d.contains(new ItemStack(Material.PAPER))) {
								p.getInventory().addItem(d.get(faktor));
								d.remove(new ItemStack(Material.PAPER));
								p.sendMessage(Main.getInstance().getPrefix()
										+ "Du hast das seltene §4Hardcore §7Item erhalten!");
								continue;
							} else if (d.contains(new ItemStack(Material.EXP_BOTTLE))) {
								d.remove(new ItemStack(Material.EXP_BOTTLE));
								int amount = Main.getInstance().getRandomItem().rndInt(1, 10);
								int current = p.getLevel();
								p.setLevel(current + amount);
								p.getInventory().addItem(d.get(faktor));
								p.playSound(p.getLocation(), Sound.LEVEL_UP, 10.0F, 10.0F);
								continue;
							}
							p.getInventory().addItem(d.get(faktor));
						}
					}
				}
			}, 0, 10);
		}
	}

	private boolean getNearbyEntities(Location l) {
		for (Entity e : l.getWorld().getEntities()) {
			if (e.getType() == EntityType.ENDER_CRYSTAL) {
				if (l.distance(e.getLocation()) <= 2D) {
					e.remove();
					return true;
				}
			}
		}
		return false;
	}

	private Color getColor(int i) {
		Color c = null;
		if (i == 1) {
			c = Color.AQUA;
		}
		if (i == 2) {
			c = Color.BLACK;
		}
		if (i == 3) {
			c = Color.BLUE;
		}
		if (i == 4) {
			c = Color.FUCHSIA;
		}
		if (i == 5) {
			c = Color.GRAY;
		}
		if (i == 6) {
			c = Color.GREEN;
		}
		if (i == 7) {
			c = Color.LIME;
		}
		if (i == 8) {
			c = Color.MAROON;
		}
		if (i == 9) {
			c = Color.NAVY;
		}
		if (i == 10) {
			c = Color.OLIVE;
		}
		return c;
	}

	/*
	 * private void setCircle(Location loc){ Vector vec = new
	 * BlockVector(loc.getBlockX(), loc.getY(), loc.getZ()); for (int x =
	 * -radius; x <= radius; x++) { for (int z = -radius; z <= radius; z++) {
	 * Vector position = vec.clone().add(new Vector(x, 0, z));
	 * 
	 * if (vec.distance(position) <= radius + 0.5) {
	 * //loc.getWorld().getHighestBlockAt(position.getBlockX(),
	 * position.getBlockZ()).setType(Material.AIR);
	 * loc.getWorld().getBlockAt(position.getBlockX(), position.getBlockY(),
	 * position.getBlockZ()).setType(Material.AIR); } } } }
	 */

}
