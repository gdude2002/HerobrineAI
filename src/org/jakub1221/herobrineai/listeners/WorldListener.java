package org.jakub1221.herobrineai.listeners;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;

public class WorldListener implements Listener {

	@EventHandler
	public void onChunkLoad(final ChunkLoadEvent event) {
		if (event.isNewChunk()) {
			final World world = event.getWorld();
			if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(world.getName())) {
				if (HerobrineAI.getPluginCore().getConfigDB().buildPyramids && (new Random().nextInt(15) == 1)) {
					final Object[] data = { event.getChunk() };
					HerobrineAI.getPluginCore().getAICore().getCore(Core.CoreType.PYRAMID).runCore(data);
				}
			}
		}
	}

}