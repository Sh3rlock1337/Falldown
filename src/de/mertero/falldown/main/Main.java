package de.mertero.falldown.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import de.mertero.falldown.commands.CMD_blocken;
import de.mertero.falldown.commands.CMD_help;
import de.mertero.falldown.commands.CMD_me;
import de.mertero.falldown.commands.CMD_op;
import de.mertero.falldown.commands.CMD_setarena;
import de.mertero.falldown.commands.CMD_setlobby;
import de.mertero.falldown.commands.CMD_setspawn;
import de.mertero.falldown.commands.CMD_start;
import de.mertero.falldown.commands.CMD_version;
import de.mertero.falldown.countdown.Countdown;
import de.mertero.falldown.events.AchievementListener;
import de.mertero.falldown.events.BlockBreakListener;
import de.mertero.falldown.events.BlockPlaceListener;
import de.mertero.falldown.events.ChatListener;
import de.mertero.falldown.events.DamageListener;
import de.mertero.falldown.events.DeathListener;
import de.mertero.falldown.events.DropItemListener;
import de.mertero.falldown.events.FlashbangListener;
import de.mertero.falldown.events.FoodListener;
import de.mertero.falldown.events.InventoryClickListener;
import de.mertero.falldown.events.InventoryMoveItemListener;
import de.mertero.falldown.events.JoinListener;
import de.mertero.falldown.events.LeaveListener;
import de.mertero.falldown.events.NickListener;
import de.mertero.falldown.events.PickUpItemListener;
import de.mertero.falldown.events.PlayerHookListener;
import de.mertero.falldown.events.PlayerInteractListener;
import de.mertero.falldown.events.RespawnListener;
import de.mertero.falldown.events.WeatherListener;
import de.mertero.falldown.manager.Coins;
import de.mertero.falldown.manager.Items;
import de.mertero.falldown.manager.LocationManager;
import de.mertero.falldown.manager.RandomItems;
import de.mertero.falldown.manager.ScoreboardManager;
import de.mertero.falldown.manager.ValueComparator;
import de.piet.cloud.api.CloudAPI;
import net.twerion.api.CloudServerState;

public class Main extends JavaPlugin {

	private static Main instance;
	private GameState state;
	private String prefix = "§6FallDown §8>> §7";
	private LocationManager lm;
	private Countdown countdown;
	private Items items;
	private RandomItems randomItem;
	public static Coins coins;
	
	private ScoreboardManager scoreboardManager;

	private ArrayList<Player> alive, spectate, itemVanish, respawnDamage;
	private HashMap<Player, Location> glowstone, schwarzpulver, blazepowder;
	private HashMap<Player, Double> bleeding;
	private HashMap<Player, Integer> leben, kills;
	private HashMap<Player, Player> lastDamager;
	private HashMap<Block, Inventory> chestLoc;
	
	private String version = "1.0.60";

	private int min = 2, max = 12, teams = 0, restartTime = 15, pvpTime = 480, buyTime = 60, ingameTime = 56, lobbyTime = 30,
			modusChange = 0;
	
	
	/*
	 * Location Manager Random Spawns int's in 12 oder 16 ändern
	 * PVP Time erhöhen
	 * MAX Players erhöhen
	 * Countdown PvP 
	 * 
	 * 12 -> 480sekun
	 * 16 -> 600 sekun
	 * */

	public void onEnable() {
		instance = this;
		getCommands();
		getEvents();
		lm = new LocationManager();
		lm.saveCfg();
		state = GameState.LOBBY;
		countdown = new Countdown();
		items = new Items();
		randomItem = new RandomItems();

		alive = new ArrayList<>();
		spectate = new ArrayList<>();
		itemVanish = new ArrayList<>();
		bleeding = new HashMap<>();
		glowstone = new HashMap<>();
		schwarzpulver = new HashMap<>();
		blazepowder = new HashMap<>();

		leben = new HashMap<>();
		lastDamager = new HashMap<>();
		kills = new HashMap<>();
		respawnDamage = new ArrayList<>();
		chestLoc = new HashMap<>();
		
		//Deaktivierung von Feuerverbreitung
		Bukkit.getWorlds().forEach((world) -> {
			world.setGameRuleValue("doFireTick", "false");
		});
		
		getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
			Bukkit.getWorlds().forEach((world) -> {
				world.setTime(1000);
				world.setStorm(false);
				world.setThundering(false);
			});
		}, 20, 20);
		
		this.scoreboardManager = new ScoreboardManager(this);

		countdown.startlobbycd();
		CloudAPI.getServerAPI().setKick(true);
		CloudAPI.getServerAPI().changeServer(CloudServerState.LOBBY, "falldown");

		System.out.println("[FallDown] FallDown wurde aktiviert");
	}

	public void onDisable() {
		System.out.println("[FallDown] FallDown wurde deaktiviert");
		for (Block blocks : chestLoc.keySet()) {
			blocks.setType(Material.AIR);
		}
	}

	/*private void setupRecording() {
		
	}*/
	
	public void getCommands() {
		getCommand("nn").setExecutor(new CMD_op());
		getCommand("setlobby").setExecutor(new CMD_setlobby());
		getCommand("setspawn").setExecutor(new CMD_setspawn());
		getCommand("setarena").setExecutor(new CMD_setarena());
		getCommand("start").setExecutor(new CMD_start());
		getCommand("me").setExecutor(new CMD_me());
		getCommand("?").setExecutor(new CMD_blocken());
		getCommand("help").setExecutor(new CMD_help());
		getCommand("falldown").setExecutor(new CMD_version());
	}

	public void getEvents() {
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new DropItemListener(), this);
		Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);
		Bukkit.getPluginManager().registerEvents(new PickUpItemListener(), this);
		Bukkit.getPluginManager().registerEvents(new FoodListener(), this);
		Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
		Bukkit.getPluginManager().registerEvents(new AchievementListener(), this);
		Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
		Bukkit.getPluginManager().registerEvents(new RespawnListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerHookListener(), this);
		Bukkit.getPluginManager().registerEvents(new WeatherListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryMoveItemListener(), this);
		Bukkit.getPluginManager().registerEvents(new FlashbangListener(), this);
		Bukkit.getPluginManager().registerEvents(new WeatherListener(), this);
		Bukkit.getPluginManager().registerEvents(new NickListener(), this);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TreeMap<Player, Integer> sortByValues(HashMap map) {
		Comparator<Player> comparator = new ValueComparator(map);
		TreeMap<Player, Integer> result = new TreeMap<Player, Integer>(comparator);
		result.putAll(map);
		return result;
	}

	public static Main getInstance() {
		return instance;
	}

	public void setState(GameState state) {
		this.state = state;
	}
	
	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}

	public LocationManager getLm() {
		return lm;
	}

	public Countdown getCountdown() {
		return countdown;
	}

	public ArrayList<Player> getAlive() {
		return alive;
	}

	public HashMap<Player, Integer> getKills() {
		return kills;
	}

	public ArrayList<Player> getSpectate() {
		return spectate;
	}

	public ArrayList<Player> getItemVanish() {
		return itemVanish;
	}

	public HashMap<Player, Player> getLastDamager() {
		return lastDamager;
	}

	public HashMap<Player, Integer> getLeben() {
		return leben;
	}

	public ArrayList<Player> getRespawnDamage() {
		return respawnDamage;
	}

	public HashMap<Block, Inventory> getChestLoc() {
		return chestLoc;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public String getPrefix() {
		return prefix;
	}

	public GameState getState() {
		return state;
	}

	public int getLobbyTime() {
		return lobbyTime;
	}

	public int getBuyTime() {
		return buyTime;
	}

	public int getPvpTime() {
		return pvpTime;
	}

	public int getIngameTime() {
		return ingameTime;
	}

	public int getRestartTime() {
		return restartTime;
	}

	public HashMap<Player, Double> getBleeding() {
		return bleeding;
	}

	public int getModusChange() {
		return modusChange;
	}

	public void setModusChange(int modusChange) {
		this.modusChange = modusChange;
	}

	public Items getItems() {
		return items;
	}

	public RandomItems getRandomItem() {
		return randomItem;
	}

	public HashMap<Player, Location> getSchwarzpulver() {
		return schwarzpulver;
	}
	
	public HashMap<Player, Location> getBlazepowder() {
		return blazepowder;
	}
	
	public HashMap<Player, Location> getGlowstone() {
		return glowstone;
	}
	
	public String getVersion() {
		return version;
	}
	
	public int getTeams() {
		return teams;
	}

}
