package de.mertero.falldown.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.mertero.falldown.main.Main;
import de.piet.cloud.api.CloudAPI;
import de.piet.cloud.spigotplugin.permissions.util.PacketScoreboard;
import net.twerion.nick.PlayerNickData;

public class ScoreboardManager {

	private final Main plugin;

	private HashMap<Player, PacketScoreboard> scoreboards;

	public ScoreboardManager(Main plugin) {
		this.plugin = plugin;
		this.scoreboards = new HashMap<>();
	}

	public void insertData(Player player) {
		this.scoreboards.put(player, new PacketScoreboard(player));
	}

	public void setLobbyScoreboard() {
		Bukkit.getOnlinePlayers().forEach((player) -> {

			PacketScoreboard packetScoreboard = scoreboards.get(player);

			packetScoreboard.sendSidebar(" §6Twerion.net");
			packetScoreboard.setLine(6, " ");
			packetScoreboard.setLine(5,
					" " + CloudAPI.getPlayerAPI().getRank(player).getRankColor() + player.getName());
			packetScoreboard.setLine(4, " §1 ");
			packetScoreboard.setLine(3, " §6Spieler:");
			packetScoreboard.setLine(2, " " + this.plugin.getAlive().size() + "§8/§f" + this.plugin.getMax());
			packetScoreboard.setLine(1, " §4 ");
		});
	}

	public void setFallScoreboard() {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			if (Main.getInstance().getAlive().contains(player)) {
				PacketScoreboard packetScoreboard = scoreboards.get(player);

				packetScoreboard.sendSidebar(" §6Twerion.net");
				packetScoreboard.setLine(4, " §1");
				packetScoreboard.setLine(3, " §6Freier Fall:");
				packetScoreboard.setLine(2, " §e" + Main.getInstance().getCountdown().ingame
						+ ((Main.getInstance().getCountdown().ingame == 1) ? " Sekunde" : " Sekunden"));
				packetScoreboard.setLine(1, "");
			} else {
				PacketScoreboard packetScoreboard = scoreboards.get(player);
				if (packetScoreboard != null)
					packetScoreboard.remove();
			}
		});
	}

	public void setShopScoreboard() {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			if (Main.getInstance().getAlive().contains(player)) {
				PacketScoreboard packetScoreboard = scoreboards.get(player);

				packetScoreboard.sendSidebar(" §6Twerion.net");
				packetScoreboard.setLine(4, " §1 ");
				packetScoreboard.setLine(3, " §6Mixphase");
				packetScoreboard.setLine(2, " §e" + Main.getInstance().getCountdown().buy
						+ ((Main.getInstance().getCountdown().buy == 1) ? " Sekunde" : " Sekunden"));
				packetScoreboard.setLine(1, "");
			} else {
				PacketScoreboard packetScoreboard = scoreboards.get(player);
				if (packetScoreboard != null)
					packetScoreboard.remove();
			}
		});
	}

	public void setPvPScoreboard() {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			if (Main.getInstance().getAlive().contains(player)) {
				PacketScoreboard packetScoreboard = scoreboards.get(player);

				packetScoreboard.sendSidebar("§6Twerion.net");
				packetScoreboard.setLine(13, " ");
				packetScoreboard.setLine(12, " §6Spielmodus");
				packetScoreboard.setLine(11," " + ((Main.getInstance().getModusChange() == 0) ? "§3Normal" : "§4Hardcore"));
				packetScoreboard.setLine(10, " §b ");
				packetScoreboard.setLine(9, " §6Teams");
				packetScoreboard.setLine(8, " " + ((Main.getInstance().getTeams() == 0) ? "§4Verboten" : "§3Erlaubt"));
				packetScoreboard.setLine(7, " §2 ");
				packetScoreboard.setLine(6, " §6Leben");
				
				if (Main.getInstance().getLeben().get(player) == 1) {
					packetScoreboard.setLine(5, "  §b" + Main.getInstance().getLeben().get(player) + "§8/§a3");
				} else {
					packetScoreboard.setLine(5, " §c" + Main.getInstance().getLeben().get(player) + "§8/§a3");
				}
				
				packetScoreboard.setLine(4, " §3 ");
				packetScoreboard.setLine(3, " §6Kills");
				packetScoreboard.setLine(2, " §b" + Main.getInstance().getKills().get(player));
				packetScoreboard.setLine(1, " §4 ");
			} else {
				PacketScoreboard packetScoreboard = scoreboards.get(player);
				if (packetScoreboard != null)
					packetScoreboard.remove();
			}
		});
	}
	
	public void removeScoreboard(Player player) {
		PacketScoreboard packetScoreboard = this.scoreboards.get(player);
		if (packetScoreboard != null) {
			packetScoreboard.remove();
		}
	}

	public Player getWinner(int i) {
		int players = 1;
		for (Player player : Main.getInstance().sortByValues(Main.getInstance().getKills()).keySet()) {
			if (players == i) {
				return player;
			}
			players++;
		}
		return null;
	}

	public HashMap<Player, PacketScoreboard> getScoreboards() {
		return scoreboards;
	}

	public void setEndScoreboard() {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			insertData(player);
			PacketScoreboard packetScoreboard = scoreboards.get(player);
			packetScoreboard.sendSidebar("§6Twerion.net");
			packetScoreboard.setLine(12, " §9");
			packetScoreboard.setLine(11, " §6Spiel ist beendet!");
			packetScoreboard.setLine(10, "  ");
			packetScoreboard.setLine(9, " §fPlatz 1:");
			String firstWinnerName = CloudAPI.getPlayerAPI().getRank(getWinner(1)).getRankColor()
					+ getWinner(1).getName();

			if (CloudAPI.getPlayerAPI().hasNickName(player)) {
				PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(player);

				firstWinnerName = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
			}
			String secondWinnerName = ((getWinner(2) == null) ? "§cNiemand"
					: CloudAPI.getPlayerAPI().getRank(getWinner(2)).getRankColor() + getWinner(2).getName());

			if (CloudAPI.getPlayerAPI().hasNickName(player) && !secondWinnerName.contains("Niemand")) {
				PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(player);

				secondWinnerName = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
			}

			String thirdWinnerName = ((getWinner(3) == null) ? "§cNiemand"
					: CloudAPI.getPlayerAPI().getRank(getWinner(3)).getRankColor() + getWinner(3).getName());

			if (CloudAPI.getPlayerAPI().hasNickName(player) && !thirdWinnerName.contains("Niemand")) {
				PlayerNickData playerNickData = CloudAPI.getPlayerAPI().getNickData(player);

				thirdWinnerName = playerNickData.getRank().getRankColor() + playerNickData.getNickname();
			}
			if (getWinner(3) == null) {
				thirdWinnerName = "§cNiemand!";
			}

			packetScoreboard.setLine(8, " " + firstWinnerName);
			packetScoreboard.setLine(7, " §1 ");
			packetScoreboard.setLine(6, " §fPlatz 2:");
			packetScoreboard.setLine(5, " " + secondWinnerName);
			packetScoreboard.setLine(4, " ");
			packetScoreboard.setLine(3, " §fPlatz 3:");
			packetScoreboard.setLine(2, " " + thirdWinnerName);
			packetScoreboard.setLine(1, " §4");
		});
	}

}