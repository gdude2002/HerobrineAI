package org.jakub1221.herobrineai;

import java.util.Random;

import org.bukkit.entity.Player;

public class Util {

	public static Player getRandomPlayer(Player[] players) {
		final int playerRolled = new Random().nextInt(players.length);
		if (playerRolled <= (players.length - 1)) {
			return players[playerRolled];
		}
		return null;
	}

	public static int getRandomPlayerNum(Player[] players) {
		final int playerRolled = new Random().nextInt(players.length);
		if (playerRolled <= (players.length - 1)) {
			return playerRolled;
		}
		return 0;
	}

}