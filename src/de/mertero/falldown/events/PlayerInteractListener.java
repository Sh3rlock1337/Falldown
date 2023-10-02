package de.mertero.falldown.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Cauldron;

import com.mojang.authlib.GameProfile;

import de.mertero.falldown.main.GameState;
import de.mertero.falldown.main.Main;
import de.mertero.falldown.manager.SkullChanger;

public class PlayerInteractListener implements Listener {

	private int benutztcd;

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(Main.getInstance().getSpectate().contains(p)){
			if (p.getItemInHand().getType() == Material.COMPASS) {
					e.setCancelled(true);
					int lenght = (Main.getInstance().getAlive().size() / 9) + 1;
					Inventory inv = Bukkit.createInventory(null, 9 * lenght, "§eTeleporter");

					for (Player alive : Main.getInstance().getAlive()) {
						CraftPlayer entityPlayer = (CraftPlayer) alive;
						GameProfile gameProfile = entityPlayer.getProfile();
						ItemStack head = null;
						if (gameProfile.getProperties().containsKey("textures")) {
							head = SkullChanger.getSkull(alive);
						} else {
							head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
						}
						SkullMeta skullmeta = (SkullMeta) head.getItemMeta();
						skullmeta.setDisplayName(alive.getName());
						head.setItemMeta(skullmeta);

						ItemStack tre = new ItemStack(Material.SADDLE, 1, (short) 3);
						ItemMeta tremeta = tre.getItemMeta();
						tremeta.setDisplayName(alive.getName());
						tre.setItemMeta(tremeta);

						inv.addItem(head);
					}

					p.openInventory(inv);
					return;
				
				// Relikt
			}
			e.setCancelled(true);
		}

		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (e.getClickedBlock() != null) {
				if (e.getClickedBlock().getType().equals(Material.DRAGON_EGG))
					e.setCancelled(true);
			}

			try {
				// Navigation für Spectater
				 if (p.getItemInHand().getType() == Material.DIAMOND) {
					if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§dRelikt")) {
						if (Main.getInstance().getAlive().contains(p)) {
							if (Main.getInstance().getItemVanish().contains(p)) {
								p.sendMessage(Main.getInstance().getPrefix() + "§dRelikt §7 wird schon verwendet");
								return;
							} else {
								Main.getInstance().getItemVanish().add(p);
								p.sendMessage(
										Main.getInstance().getPrefix() + "Du bist nun für 5 Sekunden §aunsichtbar!");

								int d = p.getItemInHand().getAmount();
								if (d - 1 == 0) {
									p.setItemInHand(new ItemStack(Material.AIR));
								} else {
									p.setItemInHand(Main.getInstance().getItems().getReliktItem(d));
								}

								for (Player alive : Main.getInstance().getAlive()) {
									alive.hidePlayer(p);
								}

								startRelikt(p);

							}
							return;
						}

					}

					// Schockwelle
				} else if (p.getItemInHand().getType() == Material.NETHER_STAR) {

					if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§5Schockwelle")) {

						double radius = 3D;
						Location loc = p.getLocation();
						List<Entity> near = loc.getWorld().getEntities();

						for (Entity en : near) {
							if (en instanceof Player) {
								if (en.getLocation().distance(loc) <= radius) {
									if (en != p) {
										en.setVelocity(p.getLocation().getDirection().multiply(3D).setY(1D));
									}
								}
							}
						}

						int d = p.getItemInHand().getAmount();
						if (d - 1 == 0) {
							p.setItemInHand(new ItemStack(Material.AIR));
							return;
						} else {
							ItemStack sw = Main.getInstance().getItems().getSchockwelleItem(d);
							p.setItemInHand(sw);
							return;
						}

					}
					return;

				} else if (p.getItemInHand().getType() == Material.STICK) {
					if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§9Mixer")) {
						if (e.getClickedBlock().getType() == Material.CAULDRON) {

							if (Main.getInstance().getBlazepowder().containsKey(p)
									&& Main.getInstance().getSchwarzpulver().containsKey(p)
									&& Main.getInstance().getGlowstone().containsKey(p)) {

								Main.getInstance().getBlazepowder().remove(p);
								Main.getInstance().getSchwarzpulver().remove(p);
								Main.getInstance().getGlowstone().remove(p);

								Block b = e.getClickedBlock();
								Cauldron caul = (Cauldron) b.getState().getData();
								BlockState d = b.getState();
								d.getData().setData((byte) (caul.getData() - 3));
								d.update();
								p.playSound(p.getLocation(), Sound.ANVIL_USE, 3.0F, 0.5F);
								int rnd = Main.getInstance().getRandomItem().rndInt(1, 7);

								switch (rnd) {
								case 1:
									p.getInventory().addItem(Main.getInstance().getItems().getBandageItem());
									p.sendMessage(Main.getInstance().getPrefix() + "Du hast drei §eBandagen erhalten!");
									break;
								case 2:
									p.getInventory().addItem(Main.getInstance().getItems().getSchockwelleItem(2));
									p.sendMessage(
											Main.getInstance().getPrefix() + "Du hast die §5Schockwelle erhalten!");
									break;
								case 3:
									p.getInventory().addItem(Main.getInstance().getItems().getThorItem());
									p.sendMessage(
											Main.getInstance().getPrefix() + "Du hast §fThors Schwert §7erhalten!");
									break;
								case 4:
									p.getInventory().addItem(Main.getInstance().getItems().getToxicItem());
									p.sendMessage(Main.getInstance().getPrefix()
											+ "Du hast das §2toxische Schwert §7erhalten!");
									break;
								case 5:
									p.getInventory().addItem(Main.getInstance().getItems().getReliktItem(2));
									p.sendMessage(Main.getInstance().getPrefix() + "Du hast ein §dRelikt §7erhalten!");
									break;
								case 6:
									p.getInventory().addItem(Main.getInstance().getItems().getHookItem());
									p.sendMessage(
											Main.getInstance().getPrefix() + "Du hast den §3Enterhaken §7erhalten!");
									break;
								case 7:
									p.getInventory().addItem(Main.getInstance().getItems().getFlashbang(2));
									p.sendMessage(
											Main.getInstance().getPrefix() + "Du hast eine §bFlashbang §7erhalten!");
									break;
								}
								return;
							}

						}

					}

				} else if (p.getItemInHand().getType() == Material.PAPER) {
					if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§4Hardcore")) {
						if (Main.getInstance().getModusChange() == 0) {
							ItemStack b = Main.getInstance().getItems().getBandageItem();

							for (Player alive : Main.getInstance().getAlive()) {
								alive.sendTitle("§4Hardcore §7Modus", "§7Der §4Hardcore §7Modus wurde aktiviert");
								alive.getInventory().addItem(b);
								alive.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 3.0F, 0.5F);

							}

							Main.getInstance().setModusChange(1);
							p.setItemInHand(new ItemStack(Material.AIR));
							return;
						} else {
							p.sendMessage(Main.getInstance().getPrefix() + "Der §4Hardcore §7Modus ist schon aktiv");
							return;
						}
					}
					return;
				}

				if (e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
					e.setCancelled(true);
					int current = p.getLevel();
					if (current >= 5) {
						Material inHand = e.getPlayer().getItemInHand().getType();
						ItemStack it = new ItemStack(inHand);

						if (enchantable().contains(it)) {

							ItemMeta im = it.getItemMeta();
							ArrayList<Enchantment> end = enchants(inHand);

							p.setLevel(current - 5);
							
							Enchantment d = end.get(Main.getInstance().getRandomItem().rndInt(0, end.size()));
							
							
							
							if (d == Enchantment.DIG_SPEED) {
								p.getInventory().setItemInHand(new ItemStack(Material.AIR));
								p.sendMessage(Main.getInstance().getPrefix()
										+ "Dein Item ist während der Verzauberung zerstört worden!");
								return;
							} else {
								im.addEnchant(d, Main.getInstance().getRandomItem().rndInt(1, 2), true);

								it.setItemMeta(im);
								p.getInventory().setItemInHand(it);
							}

						}
					} else {

						p.sendMessage(Main.getInstance().getPrefix() + "Du brauchst mindestens 5 Level");
					}

				} else if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
					if (!Main.getInstance().getSpectate().contains(p)) {
						if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
							if (Main.getInstance().getAlive().contains(p)) {
								if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
									Block block = e.getClickedBlock();

									for (Block blocks : Main.getInstance().getChestLoc().keySet()) {
										if (blocks.getLocation().equals(block.getLocation())) {
											e.setCancelled(true);
											e.getPlayer().openInventory(Main.getInstance().getChestLoc().get(blocks));
										}
									}
								}
							}

						}

						return;
					} else {
						p.sendMessage(Main.getInstance().getPrefix() + "Du darfst keine Enderchest öffnen!");
					}
				}

			} catch (Exception ex) {
			}

		} else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (Main.getInstance().getState() == GameState.PVP) {
				try {
					if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§2Toxischesschwert")
							&& p.getItemInHand().getType() == Material.IRON_SWORD) {
						toxicDamage(p);
						return;
					}
				} catch (Exception ex) {
				}
			} else {
				e.setCancelled(true);
			}
		}

	}

	private void startRelikt(Player p) {
		benutztcd = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				Main.getInstance().getItemVanish().remove(p);

				for (Player alive : Main.getInstance().getAlive()) {
					alive.showPlayer(p);
				}

				p.sendMessage(Main.getInstance().getPrefix() + "Du bist nun wieder sichtbar!");
				Bukkit.getScheduler().cancelTask(benutztcd);
			}

		}, 100);
	}

	@SuppressWarnings("deprecation")
	private void toxicDamage(Player p) {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				try {
					if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§2Toxischesschwert")
							&& p.getItemInHand().getType() == Material.IRON_SWORD) {
						p.damage(0.5);
					}
				} catch (Exception ex) {

				}
			}
		}, 40);
	}

	private ArrayList<ItemStack> enchantable() {
		ArrayList<ItemStack> loot = new ArrayList<ItemStack>();
		loot.add(new ItemStack(Material.WOOD_SWORD, 1));
		loot.add(new ItemStack(Material.WOOD_AXE, 1));
		loot.add(new ItemStack(Material.STONE_SWORD, 1));
		loot.add(new ItemStack(Material.IRON_LEGGINGS, 1));
		loot.add(new ItemStack(Material.IRON_HELMET, 1));
		loot.add(new ItemStack(Material.GOLD_AXE, 1));
		loot.add(new ItemStack(Material.DIAMOND_SWORD, 1));
		loot.add(new ItemStack(Material.LEATHER_HELMET, 1));
		loot.add(new ItemStack(Material.IRON_BOOTS, 1));
		loot.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
		loot.add(new ItemStack(Material.GOLD_BOOTS, 1));
		loot.add(new ItemStack(Material.GOLD_CHESTPLATE, 1));
		loot.add(new ItemStack(Material.DIAMOND_AXE, 1));
		loot.add(new ItemStack(Material.DIAMOND_BOOTS, 1));
		loot.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
		loot.add(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
		loot.add(new ItemStack(Material.STONE_AXE, 1));
		loot.add(new ItemStack(Material.IRON_SWORD, 1));
		loot.add(new ItemStack(Material.DIAMOND_HELMET, 1));
		loot.add(new ItemStack(Material.IRON_AXE, 1));
		loot.add(new ItemStack(Material.GOLD_SWORD, 1));
		loot.add(new ItemStack(Material.LEATHER_BOOTS, 1));
		loot.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		loot.add(new ItemStack(Material.LEATHER_LEGGINGS, 1));
		loot.add(new ItemStack(Material.GOLD_LEGGINGS, 1));
		loot.add(new ItemStack(Material.GOLD_HELMET, 1));
		loot.add(new ItemStack(Material.BOW, 1));
		return loot;

	}

	private ArrayList<Enchantment> enchants(Material item) {
		ArrayList<Enchantment> loot = new ArrayList<Enchantment>();

		if ((item) == (Material.WOOD_SWORD) || (item) == (Material.IRON_SWORD)
				|| (item) == (Material.DIAMOND_SWORD) || (item) == (Material.STONE_SWORD)
				|| (item) == (Material.GOLD_SWORD) || (item) == (Material.WOOD_AXE)
				|| (item) == (Material.STONE_AXE) || (item) == (Material.GOLD_AXE)
				|| (item) == (Material.IRON_AXE) || (item) == (Material.DIAMOND_AXE)) {
			
			loot.add(Enchantment.KNOCKBACK);
			loot.add(Enchantment.FIRE_ASPECT);
			loot.add(Enchantment.DAMAGE_ALL);
			loot.add(Enchantment.DURABILITY);
			loot.add(Enchantment.KNOCKBACK);
			loot.add(Enchantment.FIRE_ASPECT);
			loot.add(Enchantment.DAMAGE_ALL);
			loot.add(Enchantment.DURABILITY);
			loot.add(Enchantment.KNOCKBACK);
			loot.add(Enchantment.FIRE_ASPECT);
			loot.add(Enchantment.DAMAGE_ALL);
			loot.add(Enchantment.DURABILITY);
			
			loot.add(Enchantment.DIG_SPEED);
			return loot;
		} 
		
		if (item == Material.BOW) {
			loot.add(Enchantment.ARROW_DAMAGE);
			loot.add(Enchantment.ARROW_FIRE);
			loot.add(Enchantment.ARROW_INFINITE);
			loot.add(Enchantment.ARROW_KNOCKBACK);
			loot.add(Enchantment.ARROW_DAMAGE);
			loot.add(Enchantment.ARROW_FIRE);
			loot.add(Enchantment.ARROW_INFINITE);
			loot.add(Enchantment.ARROW_KNOCKBACK);
			loot.add(Enchantment.ARROW_DAMAGE);
			loot.add(Enchantment.ARROW_FIRE);
			loot.add(Enchantment.ARROW_INFINITE);
			loot.add(Enchantment.ARROW_KNOCKBACK);
			
			loot.add(Enchantment.DIG_SPEED);
			return loot;
		} else {
			loot.add(Enchantment.PROTECTION_ENVIRONMENTAL);
			loot.add(Enchantment.PROTECTION_EXPLOSIONS);
			loot.add(Enchantment.PROTECTION_FALL);
			loot.add(Enchantment.PROTECTION_FIRE);
			loot.add(Enchantment.PROTECTION_PROJECTILE);
			loot.add(Enchantment.THORNS);
			loot.add(Enchantment.PROTECTION_ENVIRONMENTAL);
			loot.add(Enchantment.PROTECTION_EXPLOSIONS);
			loot.add(Enchantment.PROTECTION_FALL);
			loot.add(Enchantment.PROTECTION_FIRE);
			loot.add(Enchantment.PROTECTION_PROJECTILE);
			loot.add(Enchantment.THORNS);
			
			loot.add(Enchantment.DIG_SPEED);
			return loot;
		}

	}

}
