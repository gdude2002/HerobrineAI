package org.jakub1221.herobrineai;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jakub1221.herobrineai.hooks._Factions;
import org.jakub1221.herobrineai.hooks._GriefPrevention;
import org.jakub1221.herobrineai.hooks._PreciousStones;
import org.jakub1221.herobrineai.hooks._Residence;
import org.jakub1221.herobrineai.hooks._Towny;
import org.jakub1221.herobrineai.hooks._WorldGuard;

public class Support {
	private boolean B_Residence;
	private boolean B_GriefPrevention;
	private boolean B_Towny;
	private boolean B_WorldGuard;
	private boolean B_PreciousStones;
	private boolean B_Factions;
	private _Residence ResidenceCore;
	private _GriefPrevention GriefPreventionCore;
	private _Towny TownyCore;
	private _WorldGuard WorldGuard;
	private _PreciousStones PreciousStones;
	private _Factions Factions;

	public Support() {
		super();
		B_Residence = false;
		B_GriefPrevention = false;
		B_Towny = false;
		B_WorldGuard = false;
		B_PreciousStones = false;
		B_Factions = false;
		ResidenceCore = null;
		GriefPreventionCore = null;
		TownyCore = null;
		WorldGuard = null;
		PreciousStones = null;
		Factions = null;
		ResidenceCore = new _Residence();
		GriefPreventionCore = new _GriefPrevention();
		TownyCore = new _Towny();
		WorldGuard = new _WorldGuard();
		PreciousStones = new _PreciousStones();
		Factions = new _Factions();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HerobrineAI.getPluginCore(), new Runnable() {
			@Override
			public void run() {
				Support.this.CheckForPlugins();
			}
		}, 2L);
	}

	public boolean isPreciousStones() {
		return B_PreciousStones;
	}

	public boolean isWorldGuard() {
		return B_WorldGuard;
	}

	public boolean isResidence() {
		return B_Residence;
	}

	public boolean isGriefPrevention() {
		return B_GriefPrevention;
	}

	public boolean isTowny() {
		return B_Towny;
	}

	public boolean isFactions() {
		return B_Factions;
	}

	public void CheckForPlugins() {
		if (ResidenceCore.Check()) {
			B_Residence = true;
			HerobrineAI.log.info("[HerobrineAI] Residence plugin detected!");
		}
		if (GriefPreventionCore.Check()) {
			B_GriefPrevention = true;
			HerobrineAI.log.info("[HerobrineAI] GriefPrevention plugin detected!");
		}
		if (TownyCore.Check()) {
			B_Towny = true;
			HerobrineAI.log.info("[HerobrineAI] Towny plugin detected!");
		}
		if (WorldGuard.Check()) {
			B_WorldGuard = true;
			HerobrineAI.log.info("[HerobrineAI] WorldGuard plugin detected!");
		}
		if (PreciousStones.Check()) {
			B_PreciousStones = true;
			HerobrineAI.log.info("[HerobrineAI] PreciousStones plugin detected!");
		}
		if (Factions.Check()) {
			B_Factions = true;
			HerobrineAI.log.info("[HerobrineAI] Factions plugin detected!");
		}
	}

	public boolean isSecuredArea(final Location loc) {
		if (B_Residence) {
			return ResidenceCore.isSecuredArea(loc);
		}
		if (B_GriefPrevention) {
			return GriefPreventionCore.isSecuredArea(loc);
		}
		if (B_Towny) {
			return TownyCore.isSecuredArea(loc);
		}
		if (B_WorldGuard) {
			return WorldGuard.isSecuredArea(loc);
		}
		if (B_PreciousStones) {
			return PreciousStones.isSecuredArea(loc);
		}
		return B_Factions && Factions.isSecuredArea(loc);
	}

	public boolean checkBuild(final Location loc) {
		return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Build || !isSecuredArea(loc);
	}

	public boolean checkAttack(final Location loc) {
		return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Attack || !isSecuredArea(loc);
	}

	public boolean checkHaunt(final Location loc) {
		return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Haunt || !isSecuredArea(loc);
	}

	public boolean checkSigns(final Location loc) {
		return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Signs || !isSecuredArea(loc);
	}

	public boolean checkBooks(final Location loc) {
		return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Books || !isSecuredArea(loc);
	}

}