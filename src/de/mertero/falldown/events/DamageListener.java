package de.mertero.falldown.events;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;

public class DamageListener implements Listener {
	
	
	int bleedcd;
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		
		if(Main.getInstance().getState() != GameState.PVP || Main.getInstance().getSpectate().contains(e.getDamager())) {
			e.setCancelled(true);
			return;
		}
			
			if (Main.getInstance().getState() == GameState.PVP) {

				if(e.getEntity() instanceof  Player &&  e.getDamager() instanceof Player || e.getCause() == DamageCause.PROJECTILE) {
					Player target = (Player)e.getEntity();
					if(e.getEntity() instanceof  Player &&  e.getDamager() instanceof Player){

						Main.getInstance().getLastDamager().put((Player)e.getEntity(), (Player)e.getDamager());
						
						
						Player damager = (Player)e.getDamager();
						
						try {
				    		if(damager.getItemInHand().getType() == Material.IRON_SWORD) {
						    	 if(damager.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§fThors Schwert")){
						    		 	if(Main.getInstance().getItemVanish().contains(damager)){
						    		 		e.setCancelled(true);
						    		 		return;
						    		 	}
										target.getWorld().strikeLightning(target.getLocation());
										addNewDamager(target, damager);
										
								} else if(damager.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§2Gift Schwert")) {
										if(Main.getInstance().getItemVanish().contains(damager)){
											e.setCancelled(true);
						    		 		return;
										}
									
						    		 	target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60 , 1));
										addNewDamager(target, damager);
								 }
						     }
				      
				     
						     if(Main.getInstance().getItemVanish().contains(e.getDamager())){
						    	 e.setCancelled(true);
						     }
				     
				    } catch(Exception ex){ }
				      
				      
				      
					 if(Main.getInstance().getModusChange() == 1){
						 if(Main.getInstance().getAlive().contains(target) && !Main.getInstance().getBleeding().containsKey(target)){
							 Main.getInstance().getBleeding().put(target, target.getHealth());
							startBleeding(target); 
						 }
					}
					 return;
					} 
					if(e.getCause() == DamageCause.PROJECTILE) {
						try {
						 Entity damager=((EntityDamageByEntityEvent)e.getEntity().getLastDamageCause()).getDamager();
						    if (damager instanceof Projectile) {
						      Projectile projectile=(Projectile)damager;
						      if (projectile.getShooter() instanceof Player) {
						    	  Player p = (Player) projectile.getShooter();
						    	  if(Main.getInstance().getModusChange() == 1){
										if(Main.getInstance().getAlive().contains(target) && !Main.getInstance().getBleeding().containsKey(target)){
											 Main.getInstance().getBleeding().put(target, target.getHealth());
									    	  	
												 addNewDamager(target, p);
												 startBleeding(target);
											
										}
									} else {
										addNewDamager(target, p);
									}
						      }
						    }
						}catch(Exception ex) {}
						
						
						
					}
				}
				
			}
			
			
	}
	
	@EventHandler
	public void getDamage(EntityDamageEvent e) {
		if(Main.getInstance().getState() != GameState.PVP || Main.getInstance().getSpectate().contains(e.getEntity()) || Main.getInstance().getRespawnDamage().contains(e.getEntity())) {
			e.setCancelled(true);
		}
		
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			if(e.getCause().equals(DamageCause.LIGHTNING)){
				try {
				if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§fThors Schwert")){
					e.setCancelled(true);
				}
				}catch(Exception ex){}
			}
		}
	}
	// Wenn Hardcore Modus aktiviert ist
		
				@SuppressWarnings("deprecation")
				@EventHandler
				public void onBandage(PlayerInteractEvent e) {
					if(Main.getInstance().getState() == GameState.PVP){
						Player p = e.getPlayer();
						
						if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
								try {
									if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§eBandage")){
										if(Main.getInstance().getModusChange() == 1){
											if(Main.getInstance().getBleeding().containsKey(p)){
												Main.getInstance().getBleeding().remove(p);
												
												int c = p.getItemInHand().getAmount();
												ItemStack b = new ItemStack(Material.INK_SACK, c-1, DyeColor.RED.getData());
												ItemMeta bm = b.getItemMeta();
												bm.setDisplayName("§eBandage");
												b.setItemMeta(bm);
												p.setItemInHand(b);
												
												
												p.sendMessage(Main.getInstance().getPrefix() + "Blutung gestoppt");
												Bukkit.getScheduler().cancelTask(bleedcd);
											}
										}
											
									}
								} catch(Exception ex) {}
						}
					}
				}

				
				@SuppressWarnings("deprecation")
				private void startBleeding(Player p) {
					if(Main.getInstance().getBleeding().containsKey(p)){
						
						bleedcd = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								try {
									if(Main.getInstance().getBleeding().containsKey(p) && Main.getInstance().getAlive().contains(p)){
										
										p.damage(0.5);
										Main.getInstance().getBleeding().replace(p, p.getHealth());
									}
								} catch(Exception ex) {}
								
							}
						}, 0, 40);
						
					}
					
				}
				
				private void addNewDamager(Player target, Player damager){
					if(Main.getInstance().getLastDamager().containsKey(target)){
						Main.getInstance().getLastDamager().replace(target, damager);
					} else {
						Main.getInstance().getLastDamager().put(target, damager);
					}
				}

}
