package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.AI.Message;
import org.jakub1221.herobrineai.AI.extensions.Position;

public class Attack extends Core {
	private int ticksToEnd;
	private int HandlerINT;
	private boolean isHandler;

	public Attack() {
		super(CoreType.ATTACK, AppearType.APPEAR);
		ticksToEnd = 0;
		HandlerINT = 0;
		isHandler = false;
	}

	@Override
	public CoreResult callCore(final Object[] data) {
		return setAttackTarget((Player) data[0]);
	}

	public CoreResult setAttackTarget(final Player player) {
		if (HerobrineAI.getPluginCore().getSupport().checkAttack(player.getLocation())) {
			HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
			ticksToEnd = 0;
			AICore.PlayerTarget = player;
			AICore.isTarget = true;
			AICore.log.info("[HerobrineAI] Teleporting to target. (" + AICore.PlayerTarget.getName() + ")");
			final Location ploc = AICore.PlayerTarget.getLocation();
			final Object[] data = { ploc };
			HerobrineAI.getPluginCore().getAICore().getCore(CoreType.DESTROY_TORCHES).runCore(data);
			if (HerobrineAI.getPluginCore().getConfigDB().usePotionEffects) {
				AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
				AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
				AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
			}
			final Location tploc = Position.getTeleportPosition(ploc);
			HerobrineAI.herobrineNPC.moveTo(tploc);
			Message.sendMessage(AICore.PlayerTarget);
			StartHandler();
			return new CoreResult(true, "Herobrine attacks " + player.getName() + "!");
		}
		return new CoreResult(false, "Player is in secure area.");
	}

	public void StopHandler() {
		if (isHandler) {
			Bukkit.getScheduler().cancelTask(HandlerINT);
			isHandler = false;
		}
	}

	public void StartHandler() {
		KeepLooking();
		FollowHideRepeat();
		isHandler = true;
		HandlerINT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				Attack.this.Handler();
			}
		}, 5L, 5L);
	}

	private void Handler() {
		KeepLooking();
		if ((ticksToEnd == 1) || (ticksToEnd == 16) || (ticksToEnd == 32) || (ticksToEnd == 48) || (ticksToEnd == 64) || (ticksToEnd == 80) || (ticksToEnd == 96)
				|| (ticksToEnd == 112) || (ticksToEnd == 128) || (ticksToEnd == 144)) {
			FollowHideRepeat();
		}
	}

	public void KeepLooking() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.ATTACK)) {
			if (!AICore.PlayerTarget.isDead()) {
				if (ticksToEnd == 160) {
					HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
				} else {
					++ticksToEnd;
					final Location ploc = AICore.PlayerTarget.getLocation();
					ploc.setY(ploc.getY() + 1.5);
					HerobrineAI.herobrineNPC.lookAtPoint(ploc);
					if (HerobrineAI.getPluginCore().getConfigDB().lighting) {
						final int lchance = new Random().nextInt(100);
						if (lchance > 75) {
							final Location newloc = ploc;
							final int randx = new Random().nextInt(50);
							final int randz = new Random().nextInt(50);
							if (new Random().nextBoolean()) {
								newloc.setX(newloc.getX() + randx);
							} else {
								newloc.setX(newloc.getX() - randx);
							}
							if (new Random().nextBoolean()) {
								newloc.setZ(newloc.getZ() + randz);
							} else {
								newloc.setZ(newloc.getZ() - randz);
							}
							newloc.setY(250.0);
							newloc.getWorld().strikeLightning(newloc);
						}
					}
				}
			} else {
				HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
			}
		} else {
			HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
		}
	}

	public void Follow() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.ATTACK)) {
			if (!AICore.PlayerTarget.isDead()) {
				if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AICore.PlayerTarget.getWorld().getName())
						&& HerobrineAI.getPluginCore().getSupport().checkAttack(AICore.PlayerTarget.getLocation())) {
					HerobrineAI.herobrineNPC.moveTo(Position.getTeleportPosition(AICore.PlayerTarget.getLocation()));
					final Location ploc = AICore.PlayerTarget.getLocation();
					ploc.setY(ploc.getY() + 1.5);
					HerobrineAI.herobrineNPC.lookAtPoint(ploc);
					AICore.PlayerTarget.playSound(AICore.PlayerTarget.getLocation(), Sound.WITHER_HURT, 0.75f, 0.75f);
					if (HerobrineAI.getPluginCore().getConfigDB().hitPlayer) {
						final int hitchance = new Random().nextInt(100);
						if (hitchance < 55) {
							AICore.PlayerTarget.playSound(AICore.PlayerTarget.getLocation(), Sound.HURT_FLESH, 0.75f, 0.75f);
							AICore.PlayerTarget.damage(4.0F);
						}
					}
				} else {
					HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
				}
			} else {
				HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
			}
		} else {
			HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
		}
	}

	public void Hide() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.ATTACK)) {
			if (!AICore.PlayerTarget.isDead()) {
				final Location ploc = AICore.PlayerTarget.getLocation();
				ploc.setY(-20.0);
				final Location hbloc1 = HerobrineAI.herobrineNPC.getBukkitEntity().getLocation();
				hbloc1.setY(hbloc1.getY() + 1.0);
				final Location hbloc2 = HerobrineAI.herobrineNPC.getBukkitEntity().getLocation();
				hbloc2.setY(hbloc2.getY() + 0.0);
				final Location hbloc3 = HerobrineAI.herobrineNPC.getBukkitEntity().getLocation();
				hbloc3.setY(hbloc3.getY() + 0.5);
				final Location hbloc4 = HerobrineAI.herobrineNPC.getBukkitEntity().getLocation();
				hbloc4.setY(hbloc4.getY() + 1.5);
				ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
				ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
				if (HerobrineAI.getPluginCore().getConfigDB().spawnBats) {
					final int cc = new Random().nextInt(3);
					if (cc == 0) {
						ploc.getWorld().spawnEntity(hbloc1, EntityType.BAT);
						ploc.getWorld().spawnEntity(hbloc1, EntityType.BAT);
					} else if (cc == 1) {
						ploc.getWorld().spawnEntity(hbloc1, EntityType.BAT);
					}
				}
				HerobrineAI.herobrineNPC.moveTo(ploc);
			} else {
				HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
			}
		} else {
			HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
		}
	}

	public void FollowHideRepeat() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.ATTACK)) {
			if (!AICore.PlayerTarget.isDead()) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
					@Override
					public void run() {
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
							@Override
							public void run() {
								Attack.this.Hide();
							}
						}, 30L);
						Attack.this.Follow();
					}
				}, 45L);
			} else {
				HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
			}
		} else {
			HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.ATTACK);
		}
	}

}