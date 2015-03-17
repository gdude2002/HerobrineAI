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
	public CoreResult callCore(final Object[] data) {
		for (int multip = 1; multip != 7; ++multip) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HerobrineAI.getPlugin(), new Runnable() {
				@Override
				public void run() {
					HerobrineAI.getPlugin().getAICore().getCore(CoreType.SOUNDF).runCore(data);
				}
			}, multip * 30L);
		}
		return new CoreResult(true, "Starting sound play to target!");
	}

}