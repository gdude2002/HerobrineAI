package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.misc.BlockChanger;
import org.jakub1221.herobrineai.nms.NPC.HerobrineCore;

public class Signs extends Core {

	public Signs() {
		super(CoreType.SIGNS, AppearType.NORMAL);
	}

	@Override
	public CoreResult callCore(final Object[] data) {
		return placeSign((Location) data[0], (Location) data[1]);
	}

	public CoreResult placeSign(final Location loc, final Location ploc) {
		boolean status = false;
		AICore.log.info("Generating sign location...");
		if ((loc.getWorld().getBlockAt(loc.getBlockX() + 2, loc.getBlockY(), loc.getBlockZ()).getType() == Material.AIR)
				&& (loc.getWorld().getBlockAt(loc.getBlockX() + 2, loc.getBlockY() - 1, loc.getBlockZ()).getType() != Material.AIR)) {
			loc.setX(loc.getBlockX() + 2);
			createSign(loc, ploc);
			status = true;
		} else if ((loc.getWorld().getBlockAt(loc.getBlockX() - 2, loc.getBlockY(), loc.getBlockZ()).getType() == Material.AIR)
				&& (loc.getWorld().getBlockAt(loc.getBlockX() - 2, loc.getBlockY() - 1, loc.getBlockZ()).getType() != Material.AIR)) {
			loc.setX(loc.getBlockX() - 2);
			createSign(loc, ploc);
			status = true;
		} else if ((loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + 2).getType() == Material.AIR)
				&& (loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() + 2).getType() != Material.AIR)) {
			loc.setZ(loc.getBlockZ() + 2);
			createSign(loc, ploc);
			status = true;
		} else if ((loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 2).getType() == Material.AIR)
				&& (loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 2).getType() != Material.AIR)) {
			loc.setZ(loc.getBlockZ() - 2);
			createSign(loc, ploc);
			status = true;
		}
		if (status) {
			return new CoreResult(true, "Sign placed!");
		}
		return new CoreResult(false, "Cannot place a sign!");
	}

	@SuppressWarnings("deprecation")
	public void createSign(final Location loc, final Location ploc) {
		final Random randcgen = new Random();
		final int chance = randcgen.nextInt(100);
		if (chance > (100 - HerobrineAI.getPlugin().getConfigDB().signChance)) {
			final Random randgen = new Random();
			final int count = HerobrineAI.getPlugin().getConfigDB().useSignMessages.size();
			final int randmsg = randgen.nextInt(count);
			final Block signblock = loc.add(0.0, 0.0, 0.0).getBlock();
			final Block undersignblock = signblock.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
			if (HerobrineCore.isAllowedBlock(signblock.getType()) && !HerobrineCore.isAllowedBlock(undersignblock.getType())) {
				signblock.setType(Material.SIGN_POST);
				final Sign sign = (Sign) signblock.getState();
				sign.setLine(1, HerobrineAI.getPlugin().getConfigDB().useSignMessages.get(randmsg));
				sign.setRawData((byte) BlockChanger.getPlayerBlockFace(ploc).ordinal());
				sign.update();
			}
		}
	}

}