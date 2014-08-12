package org.jakub1221.herobrineai.hooks;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class _WorldGuard {

	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		final WorldGuardPlugin worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		final RegionManager rm = worldGuard.getRegionManager(loc.getWorld());
		if (rm != null) {
			final Map<String, ProtectedRegion> mp = rm.getRegions();
			if (mp != null) {
				for (final Map.Entry<String, ProtectedRegion> s : mp.entrySet()) {
					if (s.getValue().contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
						return true;
					}
				}
			}
		}
		return false;
	}

}