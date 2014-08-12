package org.jakub1221.herobrineai.hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.mcore.ps.PS;

public class _Factions {

	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("Factions") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		return !BoardColls.get().getFactionAt(PS.valueOf(loc)).getComparisonName().equalsIgnoreCase("Wilderness");
	}

}