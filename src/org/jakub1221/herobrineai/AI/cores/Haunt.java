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
import org.jakub1221.herobrineai.nms.NPC.HerobrineCore;

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
	public CoreResult callCore(final Object[] data) {
		return setHauntTarget((Player) data[0]);
	}

	public CoreResult setHauntTarget(final Player player) {
		if (!HerobrineAI.getPlugin().getSupport().checkHaunt(player.getLocation())) {
			return new CoreResult(false, "Player is in secure area!");
		}
		if (!HerobrineCore.getInstance().canAttackPlayerNoMSG(player)) {
			return new CoreResult(false, "This player is protected.");
		}
		spawnedWolves = 0;
		spawnedBats = 0;
		_ticks = 0;
		isFirst = true;
		AICore.isTarget = true;
		AICore.PlayerTarget = player;
		AICore.log.info("[HerobrineAI] Hauntig player!");
		final Location loc = HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation();
		loc.setY(-20.0);
		HerobrineCore.getInstance().herobrineNPC.moveTo(loc);
		StartHandler();
		return new CoreResult(true, "Herobrine haunts " + player.getName() + "!");
	}

	public void playSounds() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPlugin().getAICore().getCoreTypeNow() == CoreType.HAUNT)) {
			if (!AICore.PlayerTarget.isDead()) {
				if (_ticks > 290) {
					HerobrineAI.getPlugin().getAICore().cancelTarget(CoreType.HAUNT);
				} else {
					final Object[] data = { AICore.PlayerTarget };
					HerobrineAI.getPlugin().getAICore().getCore(CoreType.SOUNDF).runCore(data);
					final Location ploc = AICore.PlayerTarget.getLocation();
					final Random randxgen = new Random();
					final int randx = randxgen.nextInt(100);
					if (randx >= 70) {
						if ((randx < 80) && (spawnedBats < 2)) {
							if (HerobrineAI.getPlugin().getConfigDB().spawnBats) {
								ploc.getWorld().spawnEntity(ploc, EntityType.BAT);
								++spawnedBats;
							}
						} else if ((randx < 90) && (spawnedWolves < 1) && HerobrineAI.getPlugin().getConfigDB().spawnWolves) {
							final Wolf wolf = (Wolf) ploc.getWorld().spawnEntity(ploc, EntityType.WOLF);
							wolf.setAdult();
							wolf.setAngry(true);
							++spawnedWolves;
						}
					}
					if (HerobrineAI.getPlugin().getConfigDB().lighting) {
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
						HerobrineAI.getPlugin().getAICore().getCore(CoreType.BUILD_STUFF).runCore(data2);
					}
					isFirst = false;
				}
			} else {
				HerobrineAI.getPlugin().getAICore().cancelTarget(CoreType.HAUNT);
			}
		} else {
			HerobrineAI.getPlugin().getAICore().cancelTarget(CoreType.HAUNT);
		}
	}

	public void keepLookingHaunt() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPlugin().getAICore().getCoreTypeNow() == CoreType.HAUNT)) {
			if (!AICore.PlayerTarget.isDead()) {
				Location loc = HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation();
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getEntityId() != HerobrineCore.getInstance().herobrineEntityID) {
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
							HerobrineAI.getPlugin().getAICore().disappearEffect();
						}
					}
				}
				HerobrineCore.getInstance().HerobrineHP = HerobrineCore.getInstance().HerobrineMaxHP;
				loc = AICore.PlayerTarget.getLocation();
				loc.setY(loc.getY() + 1.5);
				HerobrineCore.getInstance().herobrineNPC.lookAtPoint(loc);
				++_ticks;
				final AICore _aicore = HerobrineAI.getPlugin().getAICore();
				switch (_ticks) {
					case 0: {
						hauntTP();
						break;
					}
					case 20: {
						_aicore.disappearEffect();
						break;
					}
					case 30: {
						hauntTP();
						break;
					}
					case 50: {
						_aicore.disappearEffect();
						break;
					}
					case 60: {
						hauntTP();
						break;
					}
					case 80: {
						_aicore.disappearEffect();
						break;
					}
					case 90: {
						hauntTP();
						break;
					}
					case 110: {
						_aicore.disappearEffect();
						break;
					}
					case 120: {
						hauntTP();
						break;
					}
					case 140: {
						_aicore.disappearEffect();
						break;
					}
					case 150: {
						hauntTP();
						break;
					}
					case 170: {
						_aicore.disappearEffect();
						break;
					}
					case 180: {
						hauntTP();
						break;
					}
					case 200: {
						_aicore.disappearEffect();
						break;
					}
					case 210: {
						hauntTP();
						break;
					}
					case 230: {
						_aicore.disappearEffect();
						break;
					}
					case 240: {
						hauntTP();
						break;
					}
					case 260: {
						_aicore.disappearEffect();
						break;
					}
					case 270: {
						hauntTP();
						break;
					}
					case 290: {
						_aicore.disappearEffect();
						break;
					}
				}
			} else {
				HerobrineAI.getPlugin().getAICore().cancelTarget(CoreType.HAUNT);
			}
		} else {
			HerobrineAI.getPlugin().getAICore().cancelTarget(CoreType.HAUNT);
		}
	}

	public void hauntTP() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && (HerobrineAI.getPlugin().getAICore().getCoreTypeNow() == CoreType.HAUNT)) {
			if (!AICore.PlayerTarget.isDead()) {
				if (HerobrineAI.getPlugin().getConfigDB().useWorlds.contains(AICore.PlayerTarget.getWorld().getName())) {
					findPlace(AICore.PlayerTarget);
					final Location ploc = AICore.PlayerTarget.getLocation();
					ploc.setY(ploc.getY() + 1.5);
					HerobrineCore.getInstance().herobrineNPC.lookAtPoint(ploc);
				} else {
					HerobrineAI.getPlugin().getAICore().cancelTarget(CoreType.HAUNT);
				}
			} else {
				HerobrineAI.getPlugin().getAICore().cancelTarget(CoreType.HAUNT);
			}
		} else {
			HerobrineAI.getPlugin().getAICore().cancelTarget(CoreType.HAUNT);
		}
	}

	public boolean findPlace(final Player player) {
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
					if (((x < -4) || (x > 4) || (z < -4) || (z > 4)) && HerobrineCore.isSolidBlock(loc.getWorld().getBlockAt(x + loc.getBlockX(), (y + loc.getBlockY()) - 1, z + loc.getBlockZ()).getType())
							&& HerobrineCore.isAllowedBlock(loc.getWorld().getBlockAt(x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ()).getType())
							&& HerobrineCore.isAllowedBlock(loc.getWorld().getBlockAt(x + loc.getBlockX(), y + loc.getBlockY() + 1, z + loc.getBlockZ()).getType())) {
						teleport(loc.getWorld(), x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ());
					}
					z += ((zMax <= 0) ? -1 : 1);
				}
				x += ((xMax <= 0) ? -1 : 1);
			}
		}
		return false;
	}

	public void teleport(final World world, final int X, final int Y, final int Z) {
		final Location loc = HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation();
		loc.setWorld(world);
		loc.setX(X);
		loc.setY(Y);
		loc.setZ(Z);
		HerobrineCore.getInstance().herobrineNPC.moveTo(loc);
	}

	public void StartHandler() {
		isHandler = true;
		KL_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				Haunt.this.keepLookingHaunt();
			}
		}, 5L, 5L);
		PS_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				Haunt.this.playSounds();
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