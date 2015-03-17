package org.jakub1221.herobrineai.hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.ps.PS;

public class FactionsHook {

	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("Factions") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		return !BoardColl.get().getFactionAt(PS.valueOf(loc)).getComparisonName().equalsIgnoreCase("Wilderness");
	}

}