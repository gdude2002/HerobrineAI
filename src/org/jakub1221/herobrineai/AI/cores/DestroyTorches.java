package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class DestroyTorches extends Core {

	public DestroyTorches() {
		super(CoreType.DESTROY_TORCHES, AppearType.NORMAL);
	}

	@Override
	public CoreResult callCore(final Object[] data) {
		return destroyTorches((Location) data[0]);
	}

	public CoreResult destroyTorches(final Location loc) {
		if (HerobrineAI.getPluginCore().getConfigDB().destroyTorches) {
			final int x = loc.getBlockX();
			final int y = loc.getBlockY();
			final int z = loc.getBlockZ();
			final World world = loc.getWorld();
			int i = -HerobrineAI.getPluginCore().getConfigDB().destroyTorchesRadius;
			int ii = -HerobrineAI.getPluginCore().getConfigDB().destroyTorchesRadius;
			int iii = -HerobrineAI.getPluginCore().getConfigDB().destroyTorchesRadius;
			for (i = -HerobrineAI.getPluginCore().getConfigDB().destroyTorchesRadius; i <= HerobrineAI.getPluginCore().getConfigDB().destroyTorchesRadius; ++i) {
				for (ii = -HerobrineAI.getPluginCore().getConfigDB().destroyTorchesRadius; ii <= HerobrineAI.getPluginCore().getConfigDB().destroyTorchesRadius; ++ii) {
					for (iii = -HerobrineAI.getPluginCore().getConfigDB().destroyTorchesRadius; iii <= HerobrineAI.getPluginCore().getConfigDB().destroyTorchesRadius; ++iii) {
						if (world.getBlockAt(x + ii, y + i, z + iii).getType() == Material.TORCH) {
							world.getBlockAt(x + ii, y + i, z + iii).breakNaturally();
							return new CoreResult(true, "Torches destroyed!");
						}
					}
				}
			}
		}
		return new CoreResult(false, "Cannot destroy torches.");
	}

}