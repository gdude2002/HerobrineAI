package org.jakub1221.herobrineai.hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

public class _Residence {

	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("Residence") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		final ClaimedResidence res = Residence.getResidenceManager().getByLoc(loc);
		return res != null;
	}

}