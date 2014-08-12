package org.jakub1221.herobrineai.AI;

import java.util.Random;

import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;

public class Message {

	public static void SendMessage(final Player player) {
		if (HerobrineAI.getPluginCore().getConfigDB().SendMessages) {
			final int count = HerobrineAI.getPluginCore().getConfigDB().useMessages.size();
			final Random randgen = new Random();
			final int randmsg = randgen.nextInt(count);
			player.sendMessage("<Herobrine> " + HerobrineAI.getPluginCore().getConfigDB().useMessages.get(randmsg));
		}
	}

}