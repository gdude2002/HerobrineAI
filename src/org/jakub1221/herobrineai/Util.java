package org.jakub1221.herobrineai;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Util {

	public static Player getRandomPlayer() {
		final Player[] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
		final int player_rolled = new Random().nextInt(AllOnPlayers.length);
		if (player_rolled <= (AllOnPlayers.length - 1)) {
			return AllOnPlayers[player_rolled];
		}
		return null;
	}

	public static int getRandomPlayerNum() {
		final Player[] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
		final int player_rolled = new Random().nextInt(AllOnPlayers.length);
		if (player_rolled <= (AllOnPlayers.length - 1)) {
			return player_rolled;
		}
		return 0;
	}

}