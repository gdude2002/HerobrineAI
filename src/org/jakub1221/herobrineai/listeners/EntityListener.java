package org.jakub1221.herobrineai.listeners;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;

public class EntityListener implements Listener {

	@EventHandler
	public void EntityTargetEvent(final EntityTargetLivingEntityEvent e) {
		final LivingEntity lv = e.getTarget();
		if (lv != null) {
			if (lv.getEntityId() == HerobrineAI.herobrineEntityID) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamageByBlock(final EntityDamageByBlockEvent event) {
		if (event.getEntity().getEntityId() == HerobrineAI.herobrineEntityID) {
			event.setCancelled(true);
			event.setDamage(0);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamage(final EntityDamageEvent event) {
		if (event.getEntity().getEntityId() == HerobrineAI.herobrineEntityID) {
			if (event instanceof EntityDamageByEntityEvent) {
				final EntityDamageByEntityEvent dEvent = (EntityDamageByEntityEvent) event;
				if (HerobrineAI.getPluginCore().getConfigDB().killable && (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() != Core.CoreType.GRAVEYARD)) {
					if (dEvent.getDamager() instanceof Player) {
						if (event.getDamage() >= HerobrineAI.HerobrineHP) {
							Random randgen = new Random();
							int chance;
							ConfigurationSection drops = HerobrineAI.getPluginCore().getConfigDB().config.getConfigurationSection("config.Drops");
							if (drops != null) {
								for (String key : drops.getKeys(false)) {
									chance = randgen.nextInt(100);
									if (chance <= drops.getInt(key + ".chance")) {
										HerobrineAI.herobrineNPC.getBukkitEntity().getLocation().getWorld().dropItemNaturally(
											HerobrineAI.herobrineNPC.getBukkitEntity().getLocation(),
											new ItemStack(Material.getMaterial(Integer.parseInt(key)), drops.getInt(key + ".count"))
										);
									}
								}
							}
							HerobrineAI.getPluginCore().getAICore().cancelTarget(Core.CoreType.ANY);
							HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
							final Player player = (Player) dEvent.getDamager();
							player.sendMessage("<Herobrine> " + HerobrineAI.getPluginCore().getConfigDB().deathMessage);
						} else {
							HerobrineAI.HerobrineHP -= (int) event.getDamage();
							HerobrineAI.herobrineNPC.hurtAnimation();
							AICore.log.info("HIT: " + event.getDamage());
						}
					} else if (dEvent.getDamager() instanceof Projectile) {
						final Arrow arrow = (Arrow) dEvent.getDamager();
						if (arrow.getShooter() instanceof Player) {
							if (event.getDamage() >= HerobrineAI.HerobrineHP) {
								int j;
								Random randgen2;
								int chance2;
								for (j = 1, j = 1; j <= 2500; ++j) {
									if (HerobrineAI.getPluginCore().getConfigDB().config.contains("config.Drops." + Integer.toString(j))) {
										randgen2 = new Random();
										chance2 = randgen2.nextInt(100);
										if (chance2 <= HerobrineAI.getPluginCore().getConfigDB().config.getInt("config.Drops." + Integer.toString(j) + ".chance")) {
											HerobrineAI.herobrineNPC
													.getBukkitEntity()
													.getLocation()
													.getWorld()
													.dropItemNaturally(
															HerobrineAI.herobrineNPC.getBukkitEntity().getLocation(),
															new ItemStack(Material.getMaterial(j), HerobrineAI.getPluginCore().getConfigDB().config.getInt("config.Drops." + Integer.toString(j)
																	+ ".count")));
										}
									}
								}
								HerobrineAI.getPluginCore().getAICore().cancelTarget(Core.CoreType.ANY);
								HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
								final Player player2 = (Player) arrow.getShooter();
								player2.sendMessage("<Herobrine> " + HerobrineAI.getPluginCore().getConfigDB().deathMessage);
							} else {
								HerobrineAI.HerobrineHP -= (int) event.getDamage();
								HerobrineAI.herobrineNPC.hurtAnimation();
								AICore.log.info("HIT: " + event.getDamage());
							}
						}
					}
				}
			}
			event.setCancelled(true);
			event.setDamage(0);
			return;
		}
	}

}