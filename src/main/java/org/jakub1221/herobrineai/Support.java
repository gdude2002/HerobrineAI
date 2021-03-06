package org.jakub1221.herobrineai;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jakub1221.herobrineai.hooks.FactionsHook;
import org.jakub1221.herobrineai.hooks.GriefPreventionHook;
import org.jakub1221.herobrineai.hooks.PreciousStonesHook;
import org.jakub1221.herobrineai.hooks.ResidenceHook;
import org.jakub1221.herobrineai.hooks.WorldGuardHook;

public class Support {
	private boolean B_Residence;
	private boolean B_GriefPrevention;
	private boolean B_WorldGuard;
	private boolean B_PreciousStones;
	private boolean B_Factions;
	private ResidenceHook ResidenceCore;
	private GriefPreventionHook GriefPreventionCore;
	private WorldGuardHook WorldGuard;
	private PreciousStonesHook PreciousStones;
	private FactionsHook Factions;

	public Support() {
		ResidenceCore = new ResidenceHook();
		GriefPreventionCore = new GriefPreventionHook();
		WorldGuard = new WorldGuardHook();
		PreciousStones = new PreciousStonesHook();
		Factions = new FactionsHook();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HerobrineAI.getPlugin(), new Runnable() {
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
		if (B_WorldGuard) {
			return WorldGuard.isSecuredArea(loc);
		}
		if (B_PreciousStones) {
			return PreciousStones.isSecuredArea(loc);
		}
		return B_Factions && Factions.isSecuredArea(loc);
	}

	public boolean checkBuild(final Location loc) {
		return HerobrineAI.getPlugin().getConfigDB().securedAreaBuild || !isSecuredArea(loc);
	}

	public boolean checkAttack(final Location loc) {
		return HerobrineAI.getPlugin().getConfigDB().securedAreaAttack || !isSecuredArea(loc);
	}

	public boolean checkHaunt(final Location loc) {
		return HerobrineAI.getPlugin().getConfigDB().securedAreaHaunt || !isSecuredArea(loc);
	}

	public boolean checkSigns(final Location loc) {
		return HerobrineAI.getPlugin().getConfigDB().securedAreaSigns || !isSecuredArea(loc);
	}

	public boolean checkBooks(final Location loc) {
		return HerobrineAI.getPlugin().getConfigDB().securedAreaBooks || !isSecuredArea(loc);
	}

}