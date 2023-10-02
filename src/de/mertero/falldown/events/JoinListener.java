package de.mertero.falldown.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;
import de.piet.cloud.api.CloudAPI;
import net.twerion.rank.Rank;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		e.setJoinMessage(null);
		clearPlayer(p);
		
		Rank playerRank = CloudAPI.getPlayerAPI().getRank(p.getName());
		Main.getInstance().getScoreboardManager().insertData(p);
		
		if (Main.getInstance().getState() == GameState.LOBBY) {
			
			if (CloudAPI.getPlayerAPI().hasNickName(p)) {
				Bukkit.broadcastMessage(Main.getInstance().getPrefix()
						+ CloudAPI.getPlayerAPI().getNickData(p).colorizedName() + "§7 hat das Spiel betreten");
			} else {
				Bukkit.broadcastMessage(Main.getInstance().getPrefix() + playerRank.getRankColor() + p.getName()
						+ "§7 hat das Spiel betreten");
			}
			
			p.getInventory().setItem(4, book());
			p.getInventory().setHeldItemSlot(4);
			
			addPlayerToLists(p);
			addPlayerToLeben(p);

			teleportPlayerToLobby(p);

			if (Bukkit.getOnlinePlayers().size() == Main.getInstance().getMin()) {
				Main.getInstance().getCountdown().startlobbycd();
				teleportPlayerToLobby(p);
			}

			Main.getInstance().getScoreboardManager().setLobbyScoreboard();
			
		} else {

			addPlayerToSpectate(p);
			
			p.getInventory().addItem(mCompass());
			p.setAllowFlight(true);
			p.setFlying(true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 90000, 2));
			
			for (Player alive : Main.getInstance().getAlive()) {
				alive.hidePlayer(p);
			}

			for (Player dead : Main.getInstance().getSpectate()) {
				dead.showPlayer(p);
			}

		}
	}

	/*
	 * public void Scoreboard(Player p){
	 * 
	 * ScoreboardManager sm = Bukkit.getScoreboardManager(); final Scoreboard
	 * board = sm.getNewScoreboard(); final Objective o =
	 * board.registerNewObjective("PTP", "Mertero");
	 * 
	 * o.setDisplaySlot(DisplaySlot.SIDEBAR); o.setDisplayName(" §6FallDown ");
	 * 
	 * o.getScore(" ").setScore(11); o.getScore("§a" +
	 * p.getDisplayName()).setScore(10); o.getScore(" ").setScore(9);
	 * 
	 * o.getScore("§b ").setScore(8);
	 * 
	 * 
	 * p.setScoreboard(board); }
	 */

	private void teleportPlayerToLobby(Player p) {
		if (Main.getInstance().getLm().getLocation("lobby") != null) {
			p.teleport(Main.getInstance().getLm().getLocation("lobby"));
		}
	}

	private void addPlayerToLists(Player p) {
		if (!Main.getInstance().getAlive().contains(p)) {
			Main.getInstance().getAlive().add(p);
		}
	}

	private void addPlayerToSpectate(Player p) {
		if (!(Main.getInstance().getSpectate().contains(p))) {
			Main.getInstance().getSpectate().add(p);
		}
	}

	private void addPlayerToLeben(Player p) {
		if (!Main.getInstance().getLeben().containsKey(p)) {
			Main.getInstance().getLeben().put(p, 3);
		}
	}

	private void clearPlayer(Player p) {
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setExp(0);
		p.getInventory().clear();
		p.setExp(0.0F);
		p.setLevel(0);
		p.getInventory().setArmorContents(null);
		p.setGameMode(GameMode.ADVENTURE);

		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
	}

	//Jetzt nur noch für die anderen beiden Modis
	
	private ItemStack mCompass() {
		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemMeta compassmeta = compass.getItemMeta();
		compassmeta.setDisplayName("§eTeleporter");
		compass.setItemMeta(compassmeta);
		return compass;
	}
	
	private ItemStack book(){
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bm = (BookMeta)book.getItemMeta();
		bm.setAuthor("TwerionNET");
		bm.setTitle("§6Hilfe");
		
		bm.setPages(new String[] {
				"§6Hier eine kleine Hilfe für die §cMixphase§6",
				"§6Du kannst während der §cMixphase §6mit §cSchwarzpulver§6, §cGlowstonepulver §6und §cBlazepulver §6in einem Kessel spezielle Items herstellen.",
				"§6Dies tust du indem du die 3 aufgezaehlten Items in den §cKessel    §6wirfst. ",
				"§6Danach musst du mit dem §cMixer §6Rechtsklick auf den §cKessel §6machenund du erhaehlst ein Random Item."
				
		});
		book.setItemMeta(bm);
		
		return book;
	}
}