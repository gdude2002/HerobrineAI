package org.jakub1221.herobrineai.AI;

import java.util.Random;

import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;

public class Message {

	public static void sendMessage(final Player player) {
		if (HerobrineAI.getPlugin().getConfigDB().sendMessages) {
			final int count = HerobrineAI.getPlugin().getConfigDB().useMessages.size();
			final Random randgen = new Random();
			final int randmsg = randgen.nextInt(count);
			player.sendMessage("<Herobrine> " + HerobrineAI.getPlugin().getConfigDB().useMessages.get(randmsg));
		}
	}

}