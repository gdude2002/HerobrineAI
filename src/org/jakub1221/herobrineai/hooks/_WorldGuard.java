package org.jakub1221.herobrineai.hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class _WorldGuard {

	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		final WorldGuardPlugin worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		final RegionManager rm = worldGuard.getRegionManager(loc.getWorld());
		if (rm != null) {
			return rm.getApplicableRegions(loc).size() != 0;
		}
		return false;
	}

}