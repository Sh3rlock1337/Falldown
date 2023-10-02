package de.mertero.falldown.events;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;

public class DropItemListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
		
		if (Main.getInstance().getState() == GameState.PVP || Main.getInstance().getState() == GameState.BUY && !Main.getInstance().getSpectate().contains(e.getPlayer())) {
			
			if(Main.getInstance().getSpectate().contains(e.getPlayer())){
				e.setCancelled(true);
				return;
			}
			
			e.setCancelled(false);
			
			
			Player p = e.getPlayer();
			
			if (getBlockUnderPlayer(p).getType() == Material.CAULDRON) {
				Block b = p.getTargetBlock((Set<Material>) null, 100);
				if (b.getType() == Material.CAULDRON) {

					Cauldron caul = (Cauldron) b.getState().getData();
					BlockState d = b.getState();
					try {
							// Er dropt glowstone
							if (e.getItemDrop().getItemStack().getType() == Material.GLOWSTONE_DUST) {
								// Die Anzahl der gedroppten Items beträgt 1
								if(e.getItemDrop().getItemStack().getAmount() == 1){
									// Er hat noch 1 oder mehr Items im Inv
									if(p.getItemInHand().getAmount() >= 2){
										remove(p, e);
									} else {
										p.getItemInHand().setType(Material.AIR);
										remove(p, e);
									}
									
								} else {
									e.setCancelled(true);
								}
								
								
								
								if(!Main.getInstance().getGlowstone().containsKey(p)){
									Main.getInstance().getGlowstone().put(p, b.getLocation());
									p.sendMessage(Main.getInstance().getPrefix() + "Glowstone Staub geaddet");
								}
								
								d.getData().setData((byte) (caul.getData() + 1));
								d.update();
							}
						
						
						
							if (e.getItemDrop().getItemStack().getType() == Material.BLAZE_POWDER) {
								if(e.getItemDrop().getItemStack().getAmount() == 1){
									if(p.getItemInHand().getAmount() >= 2){
										remove(p, e);
									} else {
										p.getItemInHand().setType(Material.AIR);
										remove(p, e);
									}
									
								} else {
									e.setCancelled(true);
								}
								if (!Main.getInstance().getBlazepowder().containsKey(p)) {
									Main.getInstance().getBlazepowder().put(p, b.getLocation());
									p.sendMessage(Main.getInstance().getPrefix() + "Blaze Staub geaddet");
								}
								
								d.getData().setData((byte) (caul.getData() + 1));
								d.update();
								
							}
						
						
						
							if (e.getItemDrop().getItemStack().getType() == Material.SULPHUR) {
								if(e.getItemDrop().getItemStack().getAmount() == 1){
									if(p.getItemInHand().getAmount() >= 2){
										remove(p, e);
									} else {
										p.getItemInHand().setType(Material.AIR);
										remove(p, e);
									}
									
								} else {
									e.setCancelled(true);
								}
								
								if (!Main.getInstance().getSchwarzpulver().containsKey(p)) {
									Main.getInstance().getSchwarzpulver().put(p, b.getLocation());
									p.sendMessage(Main.getInstance().getPrefix() + "Schwarzpulver geaddet");
								}
								d.getData().setData((byte) (caul.getData() + 1));
								d.update();
							}
						
						

					} catch (Exception ex) { }

				}
			}
		}
	}

	private void remove(Player p, PlayerDropItemEvent e) {
		e.getItemDrop().setItemStack(new ItemStack(Material.AIR));
		e.setCancelled(true);
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				p.getInventory().remove(Material.STONE);
			}
		}, 1);
	}

	private Block getBlockUnderPlayer(Player p) {
		Block b = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(p.getLocation().getBlockX(),
				p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ());
		return b;
	}

	
	public static boolean hasLeveldia(Player p, int amount) {
		int dia = 80;
		if(p.getInventory().contains(Material.EMERALD, dia)){
			return true;
		} else {
			return false;
		}
	}
}
