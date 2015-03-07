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
import org.jakub1221.herobrineai.nms.NPC.HerobrineCore;

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
	public CoreResult callCore(final Object[] data) {
		return Teleport((Player) data[0]);
	}

	public CoreResult Teleport(final Player player) {
		if (!HerobrineAI.getPlugin().getConfigDB().useGraveyardWorld) {
			return new CoreResult(false, "Graveyard world is not allowed!");
		}
		LivingEntities = Bukkit.getServer().getWorld("world_herobrineai_graveyard").getLivingEntities();
		for (int i = 0; i <= (LivingEntities.size() - 1); ++i) {
			if (!(LivingEntities.get(i) instanceof Player) && (LivingEntities.get(i).getEntityId() != HerobrineCore.getInstance().herobrineEntityID)) {
				LivingEntities.get(i).remove();
			}
		}
		Bukkit.getServer().getWorld("world_herobrineai_graveyard").setTime(15000L);
		HerobrineAI.getPlugin().getAICore();
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
		HerobrineAI.getPlugin().getAICore();
		AICore.isTarget = true;
		Bukkit.getServer().getWorld("world_herobrineai_graveyard").setStorm(false);
		return new CoreResult(true, "Player successfully teleported!");
	}

	public void Start() {
		ticks = 0;
		HerobrineCore.getInstance().herobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, -4.12));
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
			if (!(LivingEntities.get(i) instanceof Player) && (LivingEntities.get(i).getEntityId() != HerobrineCore.getInstance().herobrineEntityID)) {
				LivingEntities.get(i).remove();
			}
		}
		if (!savedPlayer.isDead() && savedPlayer.isOnline() && (savedPlayer.getLocation().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) && (ticks != 90)) {
			HerobrineAI.getPlugin().getAICore();
			if (AICore.isTarget) {
				final Location ploc = savedPlayer.getLocation();
				ploc.setY(ploc.getY() + 1.5);
				HerobrineCore.getInstance().herobrineNPC.lookAtPoint(ploc);
				if (ticks == 1) {
					HerobrineCore.getInstance().herobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, -4.12));
				} else if (ticks == 40) {
					HerobrineCore.getInstance().herobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, -0.5));
				} else if (ticks == 60) {
					HerobrineCore.getInstance().herobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, 5.1));
				} else if (ticks == 84) {
					HerobrineCore.getInstance().herobrineNPC.moveTo(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, 7.5));
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
		HerobrineAI.getPlugin().getAICore();
		if (AICore.PlayerTarget == savedPlayer) {
			HerobrineAI.getPlugin().getAICore().cancelTarget(CoreType.GRAVEYARD);
		}
		savedPlayer.teleport(new Location(savedWorld, savedX, savedY, savedZ));
	}

	public Location getSavedLocation() {
		return new Location(savedWorld, savedX, savedY, savedZ);
	}

}