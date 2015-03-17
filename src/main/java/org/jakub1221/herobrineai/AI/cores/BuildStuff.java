package org.jakub1221.herobrineai.AI.cores;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class BuildStuff extends Core {

	public BuildStuff() {
		super(CoreType.BUILD_STUFF, AppearType.NORMAL);
	}

	@Override
	public CoreResult callCore(final Object[] data) {
		if (data.length == 2) {
			return this.BuildCave((Location) data[0], true);
		}
		return this.BuildCave((Location) data[0]);
	}

	public CoreResult BuildCave(final Location loc) {
		if (!HerobrineAI.getPlugin().getConfigDB().buildStuff) {
			return new CoreResult(false, "Player is in secure location.");
		}
		if (!HerobrineAI.getPlugin().getSupport().checkBuild(loc)) {
			return new CoreResult(false, "Cannot build stuff.");
		}
		if (loc.getBlockY() >= 60) {
			return new CoreResult(false, "Location must be under 60 of Y.");
		}
		final int chance = new Random().nextInt(100);
		if (chance > (100 - HerobrineAI.getPlugin().getConfigDB().caveChance)) {
			AICore.log.info("Creating cave...");
			GenerateCave(loc);
			return new CoreResult(false, "Cave created!");
		}
		return new CoreResult(false, "Roll failed!");
	}

	public CoreResult BuildCave(final Location loc, final boolean cmd) {
		if (!HerobrineAI.getPlugin().getSupport().checkBuild(loc)) {
			return new CoreResult(false, "Player is in secure location.");
		}
		if (loc.getBlockY() < 60) {
			AICore.log.info("Creating cave...");
			GenerateCave(loc);
			return new CoreResult(false, "Cave created!");
		}
		return new CoreResult(false, "Location must be under 60 of Y.");
	}

	public void GenerateCave(final Location loc) {
		if (HerobrineAI.getPlugin().getSupport().checkBuild(loc)) {
			final ArrayList<Location> redstoneTorchList = new ArrayList<Location>();
			boolean goByX = new Random().nextBoolean();
			boolean goNegative = new Random().nextBoolean();
			int baseX = loc.getBlockX();
			int baseZ = loc.getBlockZ();
			final int baseY = loc.getBlockY();
			int finalX = 0;
			int finalZ = 0;
			final int maxL = new Random().nextInt(10) + 4;
			final int iR = new Random().nextInt(3) + 4;
			int iNow = 0;
			while (iNow != iR) {
				++iNow;
				goByX = new Random().nextBoolean();
				goNegative = new Random().nextBoolean();
				int i;
				for (i = 0, i = 0; i <= maxL; ++i) {
					finalX = 0;
					finalZ = 0;
					if (goNegative) {
						if (goByX) {
							finalX = -1;
						} else {
							finalZ = -1;
						}
					} else if (goByX) {
						finalX = 1;
					} else {
						finalZ = 1;
					}
					baseX += finalX;
					baseZ += finalZ;
					loc.getWorld().getBlockAt(baseX, baseY, baseZ).breakNaturally((ItemStack) null);
					loc.getWorld().getBlockAt(baseX, baseY + 1, baseZ).breakNaturally((ItemStack) null);
					if (new Random().nextBoolean()) {
						redstoneTorchList.add(new Location(loc.getWorld(), baseX, baseY + 1, baseZ));
					}
				}
			}
			for (final Location _loc : redstoneTorchList) {
				PlaceRedstoneTorch(_loc.getWorld(), _loc.getBlockX(), _loc.getBlockY(), _loc.getBlockZ());
			}
			AICore.log.info("Cave created!");
		}
	}

	public void PlaceRedstoneTorch(final World world, final int x, final int y, final int z) {
		final Random randgen = new Random();
		final int chance = randgen.nextInt(100);
		if (chance > 70) {
			world.getBlockAt(x, y, z).setType(Material.REDSTONE_TORCH_ON);
		}
	}

}