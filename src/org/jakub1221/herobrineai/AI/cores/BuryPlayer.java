package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class BuryPlayer extends Core {

	public Block savedBlock1;
	public Block savedBlock2;

	public BuryPlayer() {
		super(CoreType.BURY_PLAYER, AppearType.NORMAL);
		savedBlock1 = null;
		savedBlock2 = null;
	}

	@Override
	public CoreResult callCore(final Object[] data) {
		return FindPlace((Player) data[0]);
	}

	public CoreResult FindPlace(final Player player) {
		if (HerobrineAI.getPluginCore().getSupport().checkBuild(player.getLocation())) {
			final Location loc = player.getLocation();
			if (HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).getType())
					&& HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ()).getType())
					&& HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 1).getType())
					&& HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ() - 1).getType())
					&& HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 3, loc.getBlockZ()).getType())
					&& HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 3, loc.getBlockZ() - 1).getType())
					&& HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 1).getType())
					&& HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ() - 1).getType())
					&& HerobrineAI.isSolidBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ() - 2).getType())
					&& !HerobrineAI.isAllowedBlock(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 2).getType())) {
				Bury(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), player);
				return new CoreResult(true, "Player buried!");
			}
		}
		return new CoreResult(false, "Cannot find a good location!");
	}

	public void Bury(final World world, final int X, final int Y, final int Z, final Player player) {
		final Location loc = new Location(world, X, Y, Z);
		loc.getWorld().getBlockAt(X, Y - 1, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 2, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 3, Z).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 1, Z - 1).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 2, Z - 1).breakNaturally();
		loc.getWorld().getBlockAt(X, Y - 3, Z - 1).breakNaturally();
		player.teleport(new Location(world, X, Y - 3, Z));
		RegenBlocks(world, X, Y, Z, player.getName());
	}

	@SuppressWarnings("deprecation")
	public void RegenBlocks(final World world, final int X, final int Y, final int Z, final String playername) {
		final Location loc = new Location(world, X, Y, Z);
		final Location signloc = new Location(world, X, Y, Z - 2);
		final Block signblock = signloc.add(0.0, 0.0, 0.0).getBlock();
		signblock.setType(Material.SIGN_POST);
		final Sign sign = (Sign) signblock.getState();
		sign.setLine(1, playername);
		sign.update();
		loc.getWorld().getBlockAt(X, Y - 1, Z).setTypeIdAndData(98, (byte) 2, false);
		loc.getWorld().getBlockAt(X, Y - 1, Z - 1).setTypeIdAndData(98, (byte) 2, false);
	}

}