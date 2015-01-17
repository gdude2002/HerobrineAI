package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Haunt extends Core {

	private int _ticks;
	private int spawnedWolves;
	private int spawnedBats;
	private int KL_INT;
	private int PS_INT;
	private boolean isHandler;
	private boolean isFirst;

	public Haunt() {
		super(CoreType.HAUNT, AppearType.APPEAR);
		_ticks = 0;
		spawnedWolves = 0;
		spawnedBats = 0;
		KL_INT = 0;
		PS_INT = 0;
		isHandler = false;
		isFirst = true;
	}

	@Override
	public CoreResult CallCore(final Object[] data) {
		return setHauntTarget((Player) data[0]);
	}

	public CoreResult setHauntTarget(final Player player) {
		if (!HerobrineAI.getPluginCore().getSupport().checkHaunt(player.getLocation())) {
			return new CoreResult(false, "Player is in secure area!");
		}
		if (!HerobrineAI.getPluginCore().canAttackPlayerNoMSG(player)) {
			return new CoreResult(false, "This player is protected.");
		}
		spawnedWolves = 0;
		spawnedBats = 0;
		_ticks = 0;
		isFirst = true;
		AICore.isTarget = true;
		AICore.PlayerTarget = player;
		AICore.log.info("[HerobrineAI] Hauntig player!");
		final Location loc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		loc.setY(-20.0);
		HerobrineAI.HerobrineNPC.moveTo(loc);
		StartHandler();
		return new CoreResult(true, "Herobrine haunts " + player.getName() + "!");
	}

	public void PlaySounds() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.HAUNT)) {
			if (!AICore.PlayerTarget.isDead()) {
				if (_ticks > 290) {
					HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);
				} else {
					final Object[] data = { AICore.PlayerTarget };
					HerobrineAI.getPluginCore().getAICore().getCore(CoreType.SOUNDF).RunCore(data);
					final Location ploc = AICore.PlayerTarget.getLocation();
					final Random randxgen = new Random();
					final int randx = randxgen.nextInt(100);
					if (randx >= 70) {
						if ((randx < 80) && (spawnedBats < 2)) {
							if (HerobrineAI.getPluginCore().getConfigDB().SpawnBats) {
								ploc.getWorld().spawnEntity(ploc, EntityType.BAT);
								++spawnedBats;
							}
						} else if ((randx < 90) && (spawnedWolves < 1) && HerobrineAI.getPluginCore().getConfigDB().SpawnWolves) {
							final Wolf wolf = (Wolf) ploc.getWorld().spawnEntity(ploc, EntityType.WOLF);
							wolf.setAdult();
							wolf.setAngry(true);
							++spawnedWolves;
						}
					}
					if (HerobrineAI.getPluginCore().getConfigDB().Lighting) {
						final int lchance = new Random().nextInt(100);
						if (lchance > 75) {
							final Location newloc = ploc;
							final int randz = new Random().nextInt(50);
							final int randxp = new Random().nextInt(1);
							final int randzp = new Random().nextInt(1);
							if (randxp == 1) {
								newloc.setX(newloc.getX() + randx);
							} else {
								newloc.setX(newloc.getX() - randx);
							}
							if (randzp == 1) {
								newloc.setZ(newloc.getZ() + randz);
							} else {
								newloc.setZ(newloc.getZ() - randz);
							}
							newloc.setY(250.0);
							newloc.getWorld().strikeLightning(newloc);
						}
					}
					if (isFirst) {
						final Object[] data2 = { AICore.PlayerTarget.getLocation() };
						HerobrineAI.getPluginCore().getAICore().getCore(CoreType.BUILD_STUFF).RunCore(data2);
					}
					isFirst = false;
				}
			} else {
				HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);
			}
		} else {
			HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);
		}
	}

	public void KeepLookingHaunt() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.HAUNT)) {
			if (!AICore.PlayerTarget.isDead()) {
				Location loc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getEntityId() != HerobrineAI.HerobrineEntityID) {
						Location ploc = player.getLocation();
						if (
							(ploc.getWorld().equals(loc.getWorld())) &&
							((ploc.getX() + 5.0) > loc.getX()) &&
							((ploc.getX() - 5.0) < loc.getX()) &&
							((ploc.getZ() + 5.0) > loc.getZ()) &&
							((ploc.getZ() - 5.0) < loc.getZ()) &&
							((ploc.getY() + 5.0) > loc.getY()) &&
							((ploc.getY() - 5.0) < loc.getY())
						) {
							HerobrineAI.getPluginCore().getAICore().DisappearEffect();
						}
					}
				}
				HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
				loc = AICore.PlayerTarget.getLocation();
				loc.setY(loc.getY() + 1.5);
				HerobrineAI.HerobrineNPC.lookAtPoint(loc);
				++_ticks;
				final AICore _aicore = HerobrineAI.getPluginCore().getAICore();
				switch (_ticks) {
					case 0: {
						HauntTP();
						break;
					}
					case 20: {
						_aicore.DisappearEffect();
						break;
					}
					case 30: {
						HauntTP();
						break;
					}
					case 50: {
						_aicore.DisappearEffect();
						break;
					}
					case 60: {
						HauntTP();
						break;
					}
					case 80: {
						_aicore.DisappearEffect();
						break;
					}
					case 90: {
						HauntTP();
						break;
					}
					case 110: {
						_aicore.DisappearEffect();
						break;
					}
					case 120: {
						HauntTP();
						break;
					}
					case 140: {
						_aicore.DisappearEffect();
						break;
					}
					case 150: {
						HauntTP();
						break;
					}
					case 170: {
						_aicore.DisappearEffect();
						break;
					}
					case 180: {
						HauntTP();
						break;
					}
					case 200: {
						_aicore.DisappearEffect();
						break;
					}
					case 210: {
						HauntTP();
						break;
					}
					case 230: {
						_aicore.DisappearEffect();
						break;
					}
					case 240: {
						HauntTP();
						break;
					}
					case 260: {
						_aicore.DisappearEffect();
						break;
					}
					case 270: {
						HauntTP();
						break;
					}
					case 290: {
						_aicore.DisappearEffect();
						break;
					}
				}
			} else {
				HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);
			}
		} else {
			HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);
		}
	}

	public void HauntTP() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.HAUNT)) {
			if (!AICore.PlayerTarget.isDead()) {
				if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AICore.PlayerTarget.getWorld().getName())) {
					FindPlace(AICore.PlayerTarget);
					final Location ploc = AICore.PlayerTarget.getLocation();
					ploc.setY(ploc.getY() + 1.5);
					HerobrineAI.HerobrineNPC.lookAtPoint(ploc);
				} else {
					HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);
				}
			} else {
				HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);
			}
		} else {
			HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.HAUNT);
		}
	}

	public boolean FindPlace(final Player player) {
		final Location loc = player.getLocation();
		int x = 0;
		int z = 0;
		int y = 0;
		int xMax = new Random().nextInt(10) + 10;
		int zMax = new Random().nextInt(10) + 10;
		final int randY = new Random().nextInt(5) + 5;
		xMax = (new Random().nextBoolean() ? (-xMax) : xMax);
		zMax = (new Random().nextBoolean() ? (-zMax) : zMax);
		for (y = -randY; y <= randY; ++y) {
			x = -xMax;
			while (true) {
				if (xMax <= 0) {
					if (x < xMax) {
						break;
					}
				} else if (x > xMax) {
					break;
				}
				z = -zMax;
				while (true) {
					if (zMax <= 0) {
						if (z < zMax) {
							break;
						}
					} else if (z > zMax) {
						break;
					}
					if (((x < -4) || (x > 4) || (z < -4) || (z > 4)) && HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(x + loc.getBlockX(), (y + loc.getBlockY()) - 1, z + loc.getBlockZ()).getType())
							&& HerobrineAI.isAllowedBlock(loc.getWorld().getBlockAt(x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ()).getType())
							&& HerobrineAI.isAllowedBlock(loc.getWorld().getBlockAt(x + loc.getBlockX(), y + loc.getBlockY() + 1, z + loc.getBlockZ()).getType())) {
						Teleport(loc.getWorld(), x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ());
					}
					z += ((zMax <= 0) ? -1 : 1);
				}
				x += ((xMax <= 0) ? -1 : 1);
			}
		}
		return false;
	}

	public void Teleport(final World world, final int X, final int Y, final int Z) {
		final Location loc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		loc.setWorld(world);
		loc.setX(X);
		loc.setY(Y);
		loc.setZ(Z);
		HerobrineAI.HerobrineNPC.moveTo(loc);
	}

	public void StartHandler() {
		isHandler = true;
		KL_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				Haunt.this.KeepLookingHaunt();
			}
		}, 5L, 5L);
		PS_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				Haunt.this.PlaySounds();
			}
		}, 35L, 35L);
	}

	public void StopHandler() {
		if (isHandler) {
			isHandler = false;
			Bukkit.getScheduler().cancelTask(KL_INT);
			Bukkit.getScheduler().cancelTask(PS_INT);
		}
	}

}