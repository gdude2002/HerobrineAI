package org.jakub1221.herobrineai.AI.extensions;

import java.util.Random;

import org.bukkit.Location;
import org.jakub1221.herobrineai.HerobrineAI;

public class Position {

	public static Location getTeleportPosition(final Location ploc) {
		final int chance = new Random().nextInt(3);
		if (chance == 0) {
			if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() - 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() - 2).getType())) {
				ploc.setZ(ploc.getZ() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() + 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() + 2).getType())) {
				ploc.setZ(ploc.getZ() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() - 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() - 2).getType())) {
				ploc.setZ(ploc.getZ() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() + 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() + 2).getType())) {
				ploc.setZ(ploc.getZ() + 2.0);
			}
		} else if (chance == 1) {
			if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() - 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() - 2).getType())) {
				ploc.setZ(ploc.getZ() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() + 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() + 2).getType())) {
				ploc.setZ(ploc.getZ() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() - 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() - 2).getType())) {
				ploc.setZ(ploc.getZ() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() + 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() + 2).getType())) {
				ploc.setZ(ploc.getZ() + 2.0);
			}
		} else if (chance == 2) {
			if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() - 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() - 2).getType())) {
				ploc.setZ(ploc.getZ() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() + 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() + 2).getType())) {
				ploc.setZ(ploc.getZ() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() - 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() - 2).getType())) {
				ploc.setZ(ploc.getZ() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() + 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() + 2).getType())) {
				ploc.setZ(ploc.getZ() + 2.0);
			}
		}
		if (chance == 3) {
			if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() + 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() + 2).getType())) {
				ploc.setZ(ploc.getZ() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() - 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() - 2).getType())) {
				ploc.setZ(ploc.getZ() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() + 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() + 2).getType())) {
				ploc.setZ(ploc.getZ() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() + 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() + 2).getType())) {
				ploc.setZ(ploc.getZ() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() - 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() - 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY(), ploc.getBlockZ()).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX() + 2, ploc.getBlockY() + 1, ploc.getBlockZ()).getType())) {
				ploc.setX(ploc.getX() + 2.0);
			} else if (HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ() - 2).getType())
					&& HerobrineAI.isAllowedBlock(ploc.getWorld().getBlockAt(ploc.getBlockX(), ploc.getBlockY() + 1, ploc.getBlockZ() - 2).getType())) {
				ploc.setZ(ploc.getZ() - 2.0);
			}
		}
		return ploc;
	}

}