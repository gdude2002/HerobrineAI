package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.ConfigDB;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class RandomPosition extends Core {

	private int randomTicks;
	private int randomMoveTicks;
	private boolean RandomMoveIsPlayer;

	public RandomPosition() {
		super(CoreType.RANDOM_POSITION, AppearType.APPEAR);
		randomTicks = 0;
		randomMoveTicks = 0;
		RandomMoveIsPlayer = false;
	}

	public int getRandomTicks() {
		return randomTicks;
	}

	public int getRandomMoveTicks() {
		return randomMoveTicks;
	}

	public void setRandomTicks(final int i) {
		randomTicks = i;
	}

	public void setRandomMoveTicks(final int i) {
		randomMoveTicks = i;
	}

	@Override
	public CoreResult CallCore(final Object[] data) {
		return setRandomPosition((World) data[0]);
	}

	public CoreResult setRandomPosition(final World world) {
		if (!HerobrineAI.getPluginCore().getConfigDB().UseWalkingMode) {
			return new CoreResult(false, "WalkingMode is disabled!");
		}
		if (randomTicks == 3) {
			return new CoreResult(false, "WalkingMode - Find location failed!");
		}
		++randomTicks;
		if ((HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) || AICore.isTarget) {
			return new CoreResult(false, "WalkingMode failed!");
		}
		final Location newloc = getRandomLocation(world);
		if (newloc != null) {
			HerobrineAI.HerobrineNPC.moveTo(newloc);
			newloc.setX(newloc.getX() + 2.0);
			newloc.setY(newloc.getY() + 1.5);
			HerobrineAI.HerobrineNPC.lookAtPoint(newloc);
			randomTicks = 0;
			AICore.log.info("[HerobrineAI] Herobrine is now in RandomLocation mode.");
			HerobrineAI.getPluginCore().getAICore().Start_RM();
			HerobrineAI.getPluginCore().getAICore().Start_RS();
			HerobrineAI.getPluginCore().getAICore().Start_CG();
			RandomMoveIsPlayer = false;
			return new CoreResult(true, "Herobrine is now in WalkingMode.");
		}
		AICore.log.info("[HerobrineAI] RandomPosition Failed!");
		return setRandomPosition(world);
	}

	public Location getRandomLocation(final World world) {
		int i;
		int nxtX;
		int nxtZ;
		int randx;
		int randy;
		int randz;
		int randxp;
		int randzp;
		for (i = 0, i = 0; i <= 300; ++i) {
			nxtX = HerobrineAI.getPluginCore().getConfigDB().WalkingModeXRadius;
			if (nxtX < 0) {
				nxtX = -nxtX;
			}
			nxtZ = HerobrineAI.getPluginCore().getConfigDB().WalkingModeZRadius;
			if (nxtZ < 0) {
				nxtZ = -nxtZ;
			}
			randx = new Random().nextInt(nxtX);
			randy = 0;
			randz = new Random().nextInt(nxtZ);
			randxp = new Random().nextInt(1);
			randzp = new Random().nextInt(1);
			if ((randxp == 0) && (randx != 0)) {
				randx = -randx;
			}
			if ((randzp == 0) && (randz != 0)) {
				randz = -randz;
			}
			randx += HerobrineAI.getPluginCore().getConfigDB().WalkingModeFromXRadius;
			randz += HerobrineAI.getPluginCore().getConfigDB().WalkingModeFromZRadius;
			if (world == null) {
				return null;
			}
			randy = world.getHighestBlockYAt(randx, randz);
			if ((world.getBlockAt(randx, randy, randz).getType() == Material.AIR) && (world.getBlockAt(randx, randy + 1, randz).getType() == Material.AIR)
					&& (world.getBlockAt(randx, randy - 1, randz).getType() != Material.AIR) && (world.getBlockAt(randx, randy - 1, randz).getType() != Material.WATER)
					&& (world.getBlockAt(randx, randy - 1, randz).getType() != Material.LAVA) && (world.getBlockAt(randx, randy - 1, randz).getType() != Material.GRASS)
					&& (world.getBlockAt(randx, randy - 1, randz).getType() != Material.SNOW) && (world.getBlockAt(randx, randy - 1, randz).getType() != Material.LEAVES)
					&& (world.getBlockAt(randx, randy - 1, randz).getType() != Material.WHEAT) && (world.getBlockAt(randx, randy - 1, randz).getType() != Material.TORCH)
					&& (world.getBlockAt(randx, randy - 1, randz).getType() != Material.REDSTONE_TORCH_OFF) && (world.getBlockAt(randx, randy - 1, randz).getType() != Material.REDSTONE_TORCH_ON)
					&& (world.getBlockAt(randx, randy - 1, randz).getType() != Material.REDSTONE) && (world.getBlockAt(randx, randy - 1, randz).getType() != Material.STATIONARY_WATER)
					&& (world.getBlockAt(randx, randy - 1, randz).getType() != Material.STATIONARY_LAVA)
			) {
				AICore.log.info("[HerobrineAI] RandomLocation " + world.getBlockAt(randx, randy - 1, randz).getType().toString() + " is X:" + randx + " Y:" + randy + " Z:" + randz);
				return new Location(world, randx + 0.5, randy, randz);
			}
		}
		return null;
	}

	public void RandomMove() {
		if ((HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) && !AICore.isTarget && !RandomMoveIsPlayer) {
			HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
		}
	}

	public void CheckGravity() {
		if ((HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) && !AICore.isTarget) {
			final Location hbloc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
			final World w = hbloc.getWorld();
			final ConfigDB config = HerobrineAI.getPluginCore().getConfigDB();
			if ((hbloc.getBlockX() < (config.WalkingModeXRadius + config.WalkingModeFromXRadius)) && (hbloc.getBlockX() > (-config.WalkingModeXRadius + config.WalkingModeFromXRadius))
					&& (hbloc.getBlockZ() < (config.WalkingModeZRadius + config.WalkingModeFromZRadius)) && (hbloc.getBlockZ() > (-config.WalkingModeZRadius + config.WalkingModeFromZRadius))) {
				if (w.getBlockAt(hbloc.getBlockX(), hbloc.getBlockY() - 1, hbloc.getBlockZ()).getType().isTransparent()) {
					hbloc.setY(hbloc.getY() - 1.0);
					HerobrineAI.HerobrineNPC.moveTo(hbloc);
				} else if (hbloc.getWorld().getBlockAt(hbloc).getType().isSolid()) {
					hbloc.setY(hbloc.getY() + 1.0);
					HerobrineAI.HerobrineNPC.moveTo(hbloc);
				}
			} else {
				HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.RANDOM_POSITION);
			}
		}
	}

	public void CheckPlayerPosition() {
		boolean isThere = false;
		final Location loc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		final Player[] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
		if (Bukkit.getServer().getOnlinePlayers().length > 0) {
			Location ploc;
			for (int i = 0; i <= (Bukkit.getServer().getOnlinePlayers().length - 1); ++i) {
				if (HerobrineAI.HerobrineEntityID != AllOnPlayers[i].getEntityId()) {
					ploc = AllOnPlayers[i].getLocation();
					if ((ploc.getWorld() == loc.getWorld()) && ((ploc.getX() + 7.0) > loc.getX()) && ((ploc.getX() - 7.0) < loc.getX()) && ((ploc.getZ() + 7.0) > loc.getZ()) && ((ploc.getZ() - 7.0) < loc.getZ()) && ((ploc.getY() + 7.0) > loc.getY()) && ((ploc.getY() - 7.0) < loc.getY())) {
						loc.setY(-20.0);
						HerobrineAI.HerobrineNPC.moveTo(loc);
						HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.RANDOM_POSITION);
						RandomMoveIsPlayer = false;
						HerobrineAI.getPluginCore().getAICore().setAttackTarget(AllOnPlayers[i]);
					} else if ((ploc.getWorld() == loc.getWorld()) && ((ploc.getX() + 15.0) > loc.getX()) && ((ploc.getX() - 15.0) < loc.getX()) && ((ploc.getZ() + 15.0) > loc.getZ())
							&& ((ploc.getZ() - 15.0) < loc.getZ()) && ((ploc.getY() + 15.0) > loc.getY()) && ((ploc.getY() - 15.0) < loc.getY())) {
						ploc.setY(ploc.getY() + 1.5);
						HerobrineAI.HerobrineNPC.lookAtPoint(ploc);
						HerobrineAI.getPluginCore().getPathManager().setPath(null);
						isThere = true;
					}
				}
			}
		}
		if (isThere) {
			RandomMoveIsPlayer = true;
		} else {
			RandomMoveIsPlayer = false;
		}
	}

}