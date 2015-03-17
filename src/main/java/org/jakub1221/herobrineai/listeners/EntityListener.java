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
import org.jakub1221.herobrineai.nms.NPC.HerobrineCore;

public class EntityListener implements Listener {

	@EventHandler
	public void EntityTargetEvent(final EntityTargetLivingEntityEvent e) {
		final LivingEntity lv = e.getTarget();
		if (lv != null) {
			if (lv.getEntityId() == HerobrineCore.getInstance().herobrineEntityID) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamageByBlock(final EntityDamageByBlockEvent event) {
		if (event.getEntity().getEntityId() == HerobrineCore.getInstance().herobrineEntityID) {
			event.setCancelled(true);
			event.setDamage(0);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamage(final EntityDamageEvent event) {
		if (event.getEntity().getEntityId() == HerobrineCore.getInstance().herobrineEntityID) {
			if (event instanceof EntityDamageByEntityEvent) {
				final EntityDamageByEntityEvent dEvent = (EntityDamageByEntityEvent) event;
				if (HerobrineAI.getPlugin().getConfigDB().killable && (HerobrineAI.getPlugin().getAICore().getCoreTypeNow() != Core.CoreType.GRAVEYARD)) {
					if (dEvent.getDamager() instanceof Player) {
						if (event.getDamage() >= HerobrineCore.getInstance().HerobrineHP) {
							Random randgen = new Random();
							int chance;
							ConfigurationSection drops = HerobrineAI.getPlugin().getConfigDB().config.getConfigurationSection("config.Drops");
							if (drops != null) {
								for (String key : drops.getKeys(false)) {
									chance = randgen.nextInt(100);
									if (chance <= drops.getInt(key + ".chance")) {
										HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation().getWorld().dropItemNaturally(
											HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation(),
											new ItemStack(Material.getMaterial(Integer.parseInt(key)), drops.getInt(key + ".count"))
										);
									}
								}
							}
							HerobrineAI.getPlugin().getAICore().cancelTarget(Core.CoreType.ANY);
							HerobrineCore.getInstance().HerobrineHP = HerobrineCore.getInstance().HerobrineMaxHP;
							final Player player = (Player) dEvent.getDamager();
							player.sendMessage("<Herobrine> " + HerobrineAI.getPlugin().getConfigDB().deathMessage);
						} else {
							HerobrineCore.getInstance().HerobrineHP -= (int) event.getDamage();
							HerobrineCore.getInstance().herobrineNPC.hurtAnimation();
							AICore.log.info("HIT: " + event.getDamage());
						}
					} else if (dEvent.getDamager() instanceof Projectile) {
						final Arrow arrow = (Arrow) dEvent.getDamager();
						if (arrow.getShooter() instanceof Player) {
							if (event.getDamage() >= HerobrineCore.getInstance().HerobrineHP) {
								int j;
								Random randgen2;
								int chance2;
								for (j = 1, j = 1; j <= 2500; ++j) {
									if (HerobrineAI.getPlugin().getConfigDB().config.contains("config.Drops." + Integer.toString(j))) {
										randgen2 = new Random();
										chance2 = randgen2.nextInt(100);
										if (chance2 <= HerobrineAI.getPlugin().getConfigDB().config.getInt("config.Drops." + Integer.toString(j) + ".chance")) {
											HerobrineCore.getInstance().herobrineNPC
													.getBukkitEntity()
													.getLocation()
													.getWorld()
													.dropItemNaturally(
															HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation(),
															new ItemStack(Material.getMaterial(j), HerobrineAI.getPlugin().getConfigDB().config.getInt("config.Drops." + Integer.toString(j)
																	+ ".count")));
										}
									}
								}
								HerobrineAI.getPlugin().getAICore().cancelTarget(Core.CoreType.ANY);
								HerobrineCore.getInstance().HerobrineHP = HerobrineCore.getInstance().HerobrineMaxHP;
								final Player player2 = (Player) arrow.getShooter();
								player2.sendMessage("<Herobrine> " + HerobrineAI.getPlugin().getConfigDB().deathMessage);
							} else {
								HerobrineCore.getInstance().HerobrineHP -= (int) event.getDamage();
								HerobrineCore.getInstance().herobrineNPC.hurtAnimation();
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