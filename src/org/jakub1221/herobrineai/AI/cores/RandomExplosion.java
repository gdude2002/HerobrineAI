package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class RandomExplosion extends Core {

	public RandomExplosion() {
		super(CoreType.RANDOM_EXPLOSION, AppearType.NORMAL);
	}

	@Override
	public CoreResult callCore(final Object[] data) {
		final Player player = (Player) data[0];
		if (!HerobrineAI.getPlugin().getConfigDB().explosions) {
			return new CoreResult(true, "Explosions are not allowed!");
		}
		if (HerobrineAI.getPlugin().getSupport().checkBuild(player.getLocation())) {
			final Location loc = player.getLocation();
			final int x = loc.getBlockX() + (new Random().nextInt(16) - 8);
			final int y = loc.getBlockY();
			final int z = loc.getBlockZ() + (new Random().nextInt(16) - 8);
			loc.getWorld().createExplosion(new Location(loc.getWorld(), x, y, z), 1.0f);
			return new CoreResult(true, "Explosion near the player!");
		}
		return new CoreResult(true, "Player is in secure area!");
	}

}