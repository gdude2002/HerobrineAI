package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Bukkit;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class RandomSound extends Core {

	public RandomSound() {
		super(CoreType.RANDOM_SOUND, AppearType.NORMAL);
	}

	@Override
	public CoreResult CallCore(final Object[] data) {
		for (int multip = 1; multip != 7; ++multip) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HerobrineAI.getPluginCore(), new Runnable() {
				@Override
				public void run() {
					HerobrineAI.getPluginCore().getAICore().getCore(CoreType.SOUNDF).RunCore(data);
				}
			}, multip * 30L);
		}
		return new CoreResult(true, "Starting sound play to target!");
	}

}