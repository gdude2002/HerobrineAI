package org.jakub1221.herobrineai.hooks;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GriefPreventionHook {

	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		return GriefPrevention.instance.dataStore.getClaimAt(loc, false, (Claim) null) != null;
	}

}