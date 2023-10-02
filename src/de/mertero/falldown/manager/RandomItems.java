package de.mertero.falldown.manager;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.mertero.falldown.main.Main;


public class RandomItems {
	
	public boolean hardcore = false;
	
			public int rndInt(int min, int max){
				Random r = new Random();
				int i = r.nextInt((max-min) +1 ) +min;
				return i;
			}
			
			private ItemStack create(Material mat, int amount){
				return new ItemStack(mat, amount);
			}
			
			private ArrayList<ItemStack> createItems() {
				ArrayList<ItemStack> loot = new ArrayList<ItemStack>();
				
				
				loot.add(create(Material.WOOD_AXE, 1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.IRON_BOOTS, 1));
				loot.add(create(Material.STONE_SWORD, 1));
				loot.add(create(Material.IRON_LEGGINGS, 1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.GOLD_AXE, 1));
				loot.add(create(Material.LEATHER_HELMET, 1));
				loot.add(create(Material.GOLD_CHESTPLATE, 1));
				loot.add(create(Material.EXP_BOTTLE,1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.GLOWSTONE_DUST, rndInt(1, 5)));
				loot.add(create(Material.BLAZE_POWDER, rndInt(1, 5)));
				loot.add(create(Material.SULPHUR, rndInt(1, 5)));
				loot.add(create(Material.DIAMOND_SWORD, 1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.LEATHER_HELMET, 1));
				loot.add(create(Material.WOOD_SWORD, 1));
				loot.add(create(Material.ARROW, rndInt(1, 12)));
				loot.add(create(Material.IRON_BOOTS, 1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.GOLD_BOOTS, 1));
				loot.add(create(Material.LEATHER_HELMET, 1));
				loot.add(create(Material.SULPHUR, 1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.STONE_SWORD, 1));
				loot.add(create(Material.GOLD_CHESTPLATE, 1));
				loot.add(create(Material.DIAMOND_AXE, 1));
				loot.add(create(Material.DIAMOND_BOOTS, 1));
				loot.add(create(Material.SULPHUR, 1));
				loot.add(create(Material.DIAMOND_CHESTPLATE, 1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.GOLDEN_APPLE, 1));
				loot.add(create(Material.DIAMOND_LEGGINGS, 1));
				loot.add(create(Material.IRON_HELMET, 1));
				loot.add(create(Material.STONE_SWORD, 1));
				loot.add(create(Material.IRON_BOOTS, 1));
				loot.add(create(Material.STONE_AXE, 1));
				loot.add(create(Material.IRON_SWORD, 1));
				loot.add(create(Material.ARROW, rndInt(1, 12)));
				loot.add(create(Material.GLOWSTONE_DUST, rndInt(1, 5)));
				loot.add(create(Material.BLAZE_POWDER, rndInt(1, 3)));
				loot.add(create(Material.SULPHUR, rndInt(1, 5)));
				loot.add(create(Material.LEATHER_HELMET, 1));
				loot.add(create(Material.DIAMOND_HELMET, 1));
				loot.add(create(Material.IRON_CHESTPLATE, 1));
				loot.add(create(Material.BLAZE_POWDER, 1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.WOOD_SWORD, 1));
				loot.add(create(Material.IRON_AXE, 1));
				loot.add(create(Material.GOLD_SWORD, 1));
				loot.add(create(Material.GLOWSTONE_DUST, rndInt(1, 3)));
				loot.add(create(Material.SULPHUR, 1));
				loot.add(create(Material.LEATHER_HELMET, 1));
				loot.add(create(Material.EXP_BOTTLE,1));
				loot.add(create(Material.GOLDEN_APPLE, rndInt(1,3)));
				loot.add(create(Material.DIAMOND, rndInt(1, 5)));
				loot.add(create(Material.GLOWSTONE_DUST, rndInt(1, 5)));
				loot.add(create(Material.BLAZE_POWDER, rndInt(1, 3)));
				loot.add(create(Material.SULPHUR, rndInt(1, 5)));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.LEATHER_BOOTS, 1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.LEATHER_CHESTPLATE, 1));
				loot.add(create(Material.LEATHER_LEGGINGS, 1));
				loot.add(create(Material.GOLD_LEGGINGS, 1));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.EXP_BOTTLE,1));
				loot.add(create(Material.GOLD_HELMET,1));
				loot.add(create(Material.DIAMOND, 1));
				loot.add(create(Material.IRON_INGOT, rndInt(1, 3)));
				loot.add(create(Material.ARROW, rndInt(1, 12)));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.BOW, 1));
				loot.add(create(Material.DIAMOND, 1));
				loot.add(create(Material.DIAMOND, rndInt(1, 3)));
				loot.add(create(Material.STICK, rndInt(1, 3)));
				loot.add(create(Material.AIR, 1));
				loot.add(create(Material.WOOD_SWORD, 1));
				loot.add(Main.getInstance().getItems().getBandageItem());
				loot.add(Main.getInstance().getItems().getHardcoreItem());
				
				
				return loot;
			}
			
			public ArrayList<ItemStack> getRandomItemList() {
				ArrayList<ItemStack> items = createItems();
				ArrayList<ItemStack> send = new ArrayList<ItemStack>();
				int count = 1;
				
				int c = 0;
				while(c <= count) {
					int pro = rndInt(0, 80);
					send.add(items.get(pro));
					c++;
				}
				return send;
			}

}
