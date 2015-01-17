package org.jakub1221.herobrineai.hooks;

import net.t00thpick1.residence.api.ResidenceAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class _Residence {

	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("Residence") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		return ResidenceAPI.getResidenceManager().getByLocation(loc) != null;
	}

}