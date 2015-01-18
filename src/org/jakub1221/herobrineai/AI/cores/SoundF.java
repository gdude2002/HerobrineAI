package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class SoundF extends Core {

	public SoundF() {
		super(CoreType.SOUNDF, AppearType.NORMAL);
	}

	@Override
	public CoreResult callCore(final Object[] data) {
		return playRandom((Player) data[0]);
	}

	public CoreResult playRandom(final Player player) {
		if (!HerobrineAI.getPluginCore().getConfigDB().useSound) {
			return new CoreResult(false, "Sound is disabled!");
		}
		final Sound[] sounds = { Sound.STEP_STONE, Sound.STEP_WOOD, Sound.STEP_GRASS, Sound.STEP_SAND, Sound.STEP_GRAVEL, Sound.WITHER_HURT, Sound.WITHER_HURT, Sound.WITHER_HURT, Sound.WITHER_HURT,
				Sound.DOOR_OPEN, Sound.DOOR_CLOSE, Sound.GHAST_SCREAM, Sound.GHAST_SCREAM2, Sound.WITHER_DEATH, Sound.WITHER_HURT };
		final int chance = new Random().nextInt(14);
		int randx = new Random().nextInt(3);
		int randz = new Random().nextInt(3);
		final int randxp = new Random().nextInt(1);
		final int randzp = new Random().nextInt(1);
		if ((randxp == 0) && (randx != 0)) {
			randx = -randx;
		}
		if ((randzp == 0) && (randz != 0)) {
			randz = -randz;
		}
		player.playSound(player.getLocation(), sounds[chance], 0.75f, 0.75f);
		return new CoreResult(true, "SoundF is starting!");
	}

}