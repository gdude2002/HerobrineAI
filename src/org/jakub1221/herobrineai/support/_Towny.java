package org.jakub1221.herobrineai.support;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class _Towny {

	public boolean Check() {
		return Bukkit.getServer().getPluginManager().getPlugin("Towny") != null;
	}

	public boolean isSecuredArea(final Location loc) {
		final Towny towny = (Towny) Bukkit.getServer().getPluginManager().getPlugin("Towny");
		towny.getTownyUniverse();
		final TownBlock block = TownyUniverse.getTownBlock(loc);
		return block != null;
	}

}