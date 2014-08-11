package org.jakub1221.herobrineai.AI;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class ConsoleLogger {

	static Logger log;

	static {
		ConsoleLogger.log = Bukkit.getLogger();
	}

	public void info(final String text) {
	}

}