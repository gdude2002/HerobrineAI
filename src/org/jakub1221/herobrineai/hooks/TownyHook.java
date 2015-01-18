package org.jakub1221.herobrineai.hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.palmergames.bukkit.towny.object.TownyUniverse;

public class TownyHook {

	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("Towny") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		return TownyUniverse.getTownBlock(loc) != null;
	}

}