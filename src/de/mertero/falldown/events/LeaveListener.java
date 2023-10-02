package de.mertero.falldown.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;
import de.piet.cloud.api.CloudAPI;

public class LeaveListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		

		e.setQuitMessage(null);

		p.getInventory().clear();

		
		// Er ist in Alive drin beim rausgehen
		if (Main.getInstance().getAlive().contains(p)) {
			// Er wird von Alive rausgenommen
			Main.getInstance().getAlive().remove(p);
			
			
			if (Main.getInstance().getLeben().containsKey(p)) {
				Main.getInstance().getLeben().remove(p);
			}
			
			
			if (Main.getInstance().getAlive().size() == 1 && Main.getInstance().getState() != GameState.LOBBY) {
				if (Main.getInstance().getState() == GameState.PVP || Main.getInstance().getState().equals(GameState.BUY)) {
						Bukkit.getOnlinePlayers().forEach((player) -> {
							if(CloudAPI.getPlayerAPI().hasNickName(player)) {
								player.sendTitle( CloudAPI.getPlayerAPI().getNickData(player).colorizedName(), "§6hat gewonnen");
							} else {
								player.sendTitle(player.getDisplayName(), "§6hat gewonnen");
							}
							
						});
				}
				Main.getInstance().getCountdown().startrestartcd();
			}
			
			if (CloudAPI.getPlayerAPI().hasNickName(p)) {
				Bukkit.broadcastMessage(Main.getInstance().getPrefix()
						+ CloudAPI.getPlayerAPI().getNickData(p).colorizedName() + "§7 hat das Spiel verlassen");
			} else {
				Bukkit.broadcastMessage(Main.getInstance().getPrefix() + CloudAPI.getPlayerAPI().getRank(p.getName()).getRankColor() + p.getName()
						+ "§7 hat das Spiel verlassen");
			}
			
				
			
			if (Main.getInstance().getAlive().size() == 0 && Main.getInstance().getState() != GameState.LOBBY) {
				Main.getInstance().getCountdown().startrestartcd();
			}
			Main.getInstance().getScoreboardManager().setLobbyScoreboard();
			// Er ist in Spectate drin beim rausgehen
			return;
		} 
		if (Main.getInstance().getSpectate().contains(p)) {
			if(Main.getInstance().getAlive().contains(p)){
				Main.getInstance().getAlive().remove(p);
			}
			
			// Er wird von Spectate rausgenommen
			Main.getInstance().getSpectate().remove(p);
			return;
			// Er war der Gewinner
		}
		if (Main.getInstance().getAlive().size() <= 1) {
			if (Main.getInstance().getState() != GameState.ENDING) {
				Main.getInstance().getCountdown().startrestartcd();
			}
			return;
		}
	}
}