package de.mertero.falldown.manager;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {
	
	public ItemStack getHardcoreItem() {
		ItemStack paper = new ItemStack(Material.PAPER);
		ItemMeta pm = paper.getItemMeta();
		pm.setDisplayName("§4Hardcore");
		ArrayList<String> pl = new ArrayList<>();
		pl.add("§7Spezielles §4Hardcore §7Item.");
		pl.add("§7Damit wechselst du den");
		pl.add("aktuellen Modus zu §4Hardcore");
		pl.add("§2Rechtsklick zum aktivieren");
		pm.setLore(pl);
		paper.setItemMeta(pm);
		return paper;
	}
	
	public ItemStack getSchockwelleItem(int d){
		ItemStack sw = new ItemStack(Material.NETHER_STAR, d-1);
		ItemMeta sm = sw.getItemMeta();
		sm.setDisplayName("§5Schockwelle");
		sw.setItemMeta(sm);
		return sw;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getBandageItem() {
		ItemStack b = new ItemStack(Material.INK_SACK, 3, DyeColor.RED.getData());
		ItemMeta bm = b.getItemMeta();
		bm.setDisplayName("§eBandage");
		ArrayList<String> pl = new ArrayList<>();
		pl.add("§7Mit dieser §eBandage");
		pl.add("§7kannst du Blutungen stoppen");
		pl.add("§2Rechtsklick zum benutzen");
		bm.setLore(pl);
		b.setItemMeta(bm);
		return b;
	}
	
	public ItemStack getMixerItem() {
		ItemStack b = new ItemStack(Material.STICK);
		ItemMeta bm = b.getItemMeta();
		bm.setDisplayName("§9Mixer");
		b.setItemMeta(bm);
		return b;
	}
	
	public ItemStack getToxicItem() {
		ItemStack b = new ItemStack(Material.IRON_SWORD);
		ItemMeta bm = b.getItemMeta();
		short d = 249;
		b.setDurability(d);
		bm.setDisplayName("§2Gift Schwert");
		b.setItemMeta(bm);
		return b;
	}
	
	public ItemStack getThorItem() {
		ItemStack b = new ItemStack(Material.IRON_SWORD);
		ItemMeta bm = b.getItemMeta();
		short d = 249;
		b.setDurability(d);
		bm.setDisplayName("§fThors Schwert");
		b.setItemMeta(bm);
		return b;
	}
	
	public ItemStack getReliktItem(int a) {
		ItemStack b = new ItemStack(Material.DIAMOND, a-1);
		ItemMeta bm = b.getItemMeta();
		bm.setDisplayName("§dRelikt");
		b.setItemMeta(bm);
		return b;
	}
	
	public ItemStack getFlashbang(int a){
		ItemStack b = new ItemStack(Material.SNOW_BALL, a-1);
		ItemMeta bm = b.getItemMeta();
		bm.setDisplayName("§bFlashbang");
		b.setItemMeta(bm);
		return b;
		
	}
	
	public ItemStack getHookItem() {
		ItemStack enter = new ItemStack(Material.FISHING_ROD, 1);
		ItemMeta enterm = enter.getItemMeta();
		enterm.setDisplayName("§3Enterhaken");
		short d =  60;
		enter.setDurability(d);
		enter.setItemMeta(enterm);
		return enter;
	}
	
	

}
