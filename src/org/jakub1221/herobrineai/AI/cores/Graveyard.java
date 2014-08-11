package org.jakub1221.herobrineai.AI.cores;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Graveyard extends Core {

	private List<LivingEntity> LivingEntities;
	private int ticks;
	private double savedX;
	private double savedY;
	private double savedZ;
	private World savedWorld;
	private Player savedPlayer;

	public Graveyard() {
		super(CoreType.GRAVEYARD, AppearType.APPEAR);
		ticks = 0;
		savedX = 0.0;
		savedY = 0.0;
		savedZ = 0.0;
		savedWorld = null;
		savedPlayer = null;
	}

	@Override
	public CoreResult CallCore(final Object[] data) {
		return Teleport((Player) data[0]);
	}

	public CoreResult Teleport(final Player player) {
		if (!HerobrineAI.getPluginCore().getConfigDB().UseGraveyardWorld) {
			return new CoreResult(false, "Graveyard world is not allowed!");
		}
		if (!HerobrineAI.getPluginCore().getAICore().checkAncientSword(player.getInventory())) {
			LivingEntities = Bukkit.getServer().getWorld("world_herobrineai_graveyard").getLivingEntities();
			for (int i = 0; i <= (LivingEntities.size() - 1); ++i) {
				if (!(LivingEntities.get(i) instanceof Player) && (LivingEntities.get(i).getEntityId() != HerobrineAI.HerobrineEntityID)) {
					LivingEntities.get(i).remove();
				}
			}
			Bukkit.getServer().getWorld("world_herobrineai_graveyard").setTime(15000L);
			HerobrineAI.getPluginCore().getAICore();
			AICore.PlayerTarget = player;
			final Location loc = player.getLocation();
			savedX = loc.getX();
			savedY = loc.getY();
			savedZ = loc.getZ();
			savedWorld = loc.getWorld();
			savedPlayer = player;
			loc.setWorld(Bukkit.getServer().getWorld("world_herobrineai_graveyard"));
			loc.setX(-2.49);
			loc.setY(4.0);
			loc.setZ(10.69);
			loc.setYaw(-179.85f);
			loc.setPitch(0.44999f);
			player.teleport(loc);
			Start();
			HerobrineAI.getPluginCore().getAICore();
			AICore.isTarget = true;
			Bukkit.getServer().getWorld("world_herobrineai_graveyard").setStorm(false);
			return new CoreResult(true, "Player successfully teleported!");
		}
		return new CoreResult(false, "Player has Ancient Sword.");
	}

	public void Start() {
		ticks = 0;
		HerobrineAI.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, -4.12));
		HandlerInterval();
	}

	public void HandlerInterval() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				Graveyard.this.Handler();
			}
		}, 5L);
	}

	public void Handler() {
		LivingEntities = Bukkit.getServer().getWorld("world_herobrineai_graveyard").getLivingEntities();
		for (int i = 0; i <= (LivingEntities.size() - 1); ++i) {
			if (!(LivingEntities.get(i) instanceof Player) && (LivingEntities.get(i).getEntityId() != HerobrineAI.HerobrineEntityID)) {
				LivingEntities.get(i).remove();
			}
		}
		if (!savedPlayer.isDead() && savedPlayer.isOnline() && (savedPlayer.getLocation().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) && (ticks != 90)) {
			HerobrineAI.getPluginCore().getAICore();
			if (AICore.isTarget) {
				final Location ploc = savedPlayer.getLocation();
				ploc.setY(ploc.getY() + 1.5);
				HerobrineAI.HerobrineNPC.lookAtPoint(ploc);
				if (ticks == 1) {
					HerobrineAI.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, -4.12));
				} else if (ticks == 40) {
					HerobrineAI.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, -0.5));
				} else if (ticks == 60) {
					HerobrineAI.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, 5.1));
				} else if (ticks == 84) {
					HerobrineAI.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, 7.5));
				}
				if (new Random().nextInt(4) == 1) {
					final Location newloc = new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), new Random().nextInt(400), new Random().nextInt(20) + 20.0,
							new Random().nextInt(400));
					Bukkit.getServer().getWorld("world_herobrineai_graveyard").strikeLightning(newloc);
				}
				++ticks;
				HandlerInterval();
				return;
			}
		}
		HerobrineAI.getPluginCore().getAICore();
		if (AICore.PlayerTarget == savedPlayer) {
			HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.GRAVEYARD);
		}
		savedPlayer.teleport(new Location(savedWorld, savedX, savedY, savedZ));
	}

	public Location getSavedLocation() {
		return new Location(savedWorld, savedX, savedY, savedZ);
	}

}