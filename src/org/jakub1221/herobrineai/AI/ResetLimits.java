package org.jakub1221.herobrineai.AI;

import org.bukkit.Bukkit;
import org.jakub1221.herobrineai.HerobrineAI;

public class ResetLimits {

	private int taskID;
	private int books;
	private int signs;
	private int heads;
	public int maxBooks;
	public int maxSigns;
	public int maxHeads;

	public ResetLimits() {
		super();
		taskID = 0;
		books = 0;
		signs = 0;
		heads = 0;
		maxBooks = 1;
		maxSigns = 1;
		maxHeads = 1;
		taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HerobrineAI.getPluginCore(), new Runnable() {
			@Override
			public void run() {
				ResetLimits.this.resetAll();
			}
		}, 72000L, 72000L);
	}

	public void disable() {
		Bukkit.getServer().getScheduler().cancelTask(taskID);
	}

	public boolean isBook() {
		if (books < maxBooks) {
			++books;
			return true;
		}
		return false;
	}

	public boolean isSign() {
		if (signs < maxSigns) {
			++signs;
			return true;
		}
		return false;
	}

	public boolean isHead() {
		if (heads < maxHeads) {
			++heads;
			return true;
		}
		return false;
	}

	public void resetAll() {
		books = 0;
		signs = 0;
		heads = 0;
	}

	public void updateFromConfig() {
		maxBooks = HerobrineAI.getPluginCore().getConfigDB().maxBooks;
		maxSigns = HerobrineAI.getPluginCore().getConfigDB().maxSigns;
		maxHeads = HerobrineAI.getPluginCore().getConfigDB().maxHeads;
	}

}