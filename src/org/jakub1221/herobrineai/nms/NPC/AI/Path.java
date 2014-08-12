package org.jakub1221.herobrineai.nms.NPC.AI;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.jakub1221.herobrineai.HerobrineAI;

public class Path {

	private float x;
	private float z;
	private float L_x;
	private float L_z;
	private int L_t;
	private boolean xNegative;
	private boolean zNegative;
	private boolean canContinue;
	private boolean isCompleted;
	private int stepNow;
	private int maxSteps;

	public Path(final float _x, final float _z) {
		super();
		L_t = 0;
		canContinue = true;
		isCompleted = false;
		stepNow = 0;
		maxSteps = new Random().nextInt(3) + 3;
		x = _x;
		z = _z;
		L_x = _x;
		L_z = _z;
		if ((x - HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation().getX()) < 0.0) {
			xNegative = true;
		} else {
			xNegative = false;
		}
		if ((z - HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation().getZ()) < 0.0) {
			zNegative = true;
		} else {
			zNegative = false;
		}
	}

	public void update() {
		if (stepNow <= maxSteps) {
			if (!isCompleted) {
				if ((x - HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation().getX()) < 0.0) {
					xNegative = true;
				} else {
					xNegative = false;
				}
				if ((z - HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation().getZ()) < 0.0) {
					zNegative = true;
				} else {
					zNegative = false;
				}
				final Location loc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
				final World world = loc.getWorld();
				if ((loc.getBlockX() > ((int) x - 1)) && (loc.getBlockX() < ((int) x + 1)) && (loc.getBlockZ() > ((int) z - 1)) && (loc.getBlockZ() < ((int) z + 1))) {
					isCompleted = true;
					return;
				}
				final float nX = (float) loc.getX();
				final float nY = (float) loc.getY();
				final float nZ = (float) loc.getZ();
				float pre_finalX = 0.3f;
				float pre_finalZ = 0.3f;
				if (xNegative) {
					pre_finalX = -0.3f;
				}
				if (zNegative) {
					pre_finalZ = -0.3f;
				}
				boolean canGoX = true;
				boolean canGoZ = true;
				if (world.getHighestBlockYAt((int) (nX + pre_finalX), (int) nZ) > (nY + 1.0f)) {
					canGoX = false;
				}
				if (world.getHighestBlockYAt((int) nX, (int) (nZ + pre_finalZ)) > (nY + 1.0f)) {
					canGoZ = false;
				}
				if (canGoX && canGoZ) {
					if (world.getHighestBlockYAt((int) (nX + pre_finalX), (int) (nZ + pre_finalZ)) > (nY + 1.0f)) {
						canGoX = false;
						canGoZ = false;
					} else if (world.getHighestBlockYAt((int) (nX + pre_finalX), (int) (nZ + pre_finalZ)) < (nY - 2.0f)) {
						canGoX = false;
						canGoZ = false;
					}
				}
				final Location newloc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
				if (canGoX) {
					newloc.setX(newloc.getX() + pre_finalX);
				}
				if (canGoZ) {
					newloc.setZ(newloc.getZ() + pre_finalZ);
				}
				if (canGoX && !canGoZ) {
				}
				newloc.setY(world.getHighestBlockYAt(newloc) - 1);
				if (world.getBlockAt(newloc).getType().isSolid()) {
					newloc.setY(newloc.getWorld().getHighestBlockYAt(newloc) + 1.5f);
					HerobrineAI.HerobrineNPC.lookAtPoint(newloc);
					newloc.setY(newloc.getWorld().getHighestBlockYAt(newloc));
					HerobrineAI.HerobrineNPC.moveTo(newloc);
				}
				++stepNow;
			} else {
				lookAround();
			}
		} else {
			lookAround();
		}
	}

	public boolean canContinue() {
		return canContinue;
	}

	public void lookAround() {
		if (new Random().nextInt(3) == 1) {
			final Location loc = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
			if (L_t >= 10) {
				L_x = (float) loc.getX();
				L_z = (float) loc.getZ();
				L_t = 0;
			}
			final Random rand = new Random();
			L_x += rand.nextInt(20) - 10;
			L_z += rand.nextInt(20) - 10;
			++L_t;
			loc.setX(L_x);
			loc.setZ(L_z);
			HerobrineAI.HerobrineNPC.lookAtPoint(loc);
		}
	}

}