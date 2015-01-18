package org.jakub1221.herobrineai.listeners;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.nms.entity.MobType;

public class EntityListener implements Listener {

	@EventHandler
	public void onCreatureSpawn(final CreatureSpawnEvent event) {
		if (!HerobrineAI.isNPCDisabled && HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(event.getEntity().getLocation().getWorld().getName())) {
			final Entity entity = event.getEntity();
			final EntityType creatureType = event.getEntityType();
			if (event.isCancelled()) {
				return;
			}
			if (creatureType == EntityType.ZOMBIE) {
				if (HerobrineAI.getPluginCore().getConfigDB().useNPCWarrior && (new Random().nextInt(100) < HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Warrior.SpawnChance"))
						&& !HerobrineAI.getPluginCore().getEntityManager().isCustomMob(entity.getEntityId())) {
					final LivingEntity ent = (LivingEntity) entity;
					ent.setHealth(0);
					HerobrineAI.getPluginCore().getEntityManager().spawnCustomZombie(event.getLocation(), MobType.HEROBRINE_WARRIOR);
				}
			} else if ((creatureType == EntityType.SKELETON) && HerobrineAI.getPluginCore().getConfigDB().useNPCDemon
					&& (new Random().nextInt(100) < HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.SpawnChance"))
					&& !HerobrineAI.getPluginCore().getEntityManager().isCustomMob(entity.getEntityId())) {
				final LivingEntity ent = (LivingEntity) entity;
				ent.setHealth(0);
				HerobrineAI.getPluginCore().getEntityManager().spawnCustomSkeleton(event.getLocation(), MobType.DEMON);
			}
		}
	}

	@EventHandler
	public void onEntityDeathEvent(final EntityDeathEvent event) {
		if (HerobrineAI.getPluginCore().getEntityManager().isCustomMob(event.getEntity().getEntityId())) {
			HerobrineAI.getPluginCore().getEntityManager().removeMob(event.getEntity().getEntityId());
		}
	}

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
							int i;
							Random randgen;
							int chance;
							for (i = 1, i = 1; i <= 2500; ++i) {
								if (HerobrineAI.getPluginCore().getConfigDB().config.contains("config.Drops." + Integer.toString(i))) {
									randgen = new Random();
									chance = randgen.nextInt(100);
									if (chance <= HerobrineAI.getPluginCore().getConfigDB().config.getInt("config.Drops." + Integer.toString(i) + ".chance")) {
										HerobrineAI.herobrineNPC.getBukkitEntity().getLocation().getWorld().dropItemNaturally(
											HerobrineAI.herobrineNPC.getBukkitEntity().getLocation(),
											new ItemStack(Material.getMaterial(i), HerobrineAI.getPluginCore().getConfigDB().config.getInt("config.Drops." + Integer.toString(i) + ".count"))
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