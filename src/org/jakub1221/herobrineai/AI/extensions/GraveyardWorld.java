package org.jakub1221.herobrineai.AI.extensions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.misc.StructureLoader;

public class GraveyardWorld {

	public static void create() {
		final Location loc = new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), 0.0, 3.0, 0.0);
		for (int x = -50; x <= 50; ++x) {
			for (int z = -50; z <= 50; ++z) {
				loc.getWorld().getBlockAt(x, 3, z).setType(Material.MYCEL);
			}
		}
		final int MainX = -10;
		final int MainY = 3;
		final int MainZ = -10;
		new StructureLoader(HerobrineAI.getPlugin().getInputStreamData("/res/graveyard_world.yml")).Build(loc.getWorld(), MainX, MainY, MainZ);
	}

}