package org.jakub1221.herobrineai.NPC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jakub1221.herobrineai.NPC.Entity.HumanEntity;
import org.jakub1221.herobrineai.NPC.Entity.HumanNPC;
import org.jakub1221.herobrineai.NPC.NMS.BServer;
import org.jakub1221.herobrineai.NPC.NMS.BWorld;
import org.jakub1221.herobrineai.NPC.Network.NetworkCore;

public class NPCCore {

	private ArrayList<HumanNPC> npcs;
	private BServer server;
	private int taskid;
	private Map<World, BWorld> bworlds;
	private NetworkCore networkCore;
	public static JavaPlugin plugin;
	private int lastID;

	public NPCCore(final JavaPlugin plugin) {
		super();
		npcs = new ArrayList<HumanNPC>();
		bworlds = new HashMap<World, BWorld>();
		lastID = 0;
		server = BServer.getInstance();
		try {
			networkCore = new NetworkCore();
		} catch (IOException e) {
			e.printStackTrace();
		}
		taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				final ArrayList<HumanNPC> toRemove = new ArrayList<HumanNPC>();
				for (final HumanNPC i : npcs) {
					final Entity j = i.getEntity();
					if (j.dead) {
						toRemove.add(i);
					}
				}
				for (final HumanNPC n : toRemove) {
					npcs.remove(n);
				}
			}
		}, 1L, 1L);
	}

	public void removeAll() {
		for (final HumanNPC humannpc : npcs) {
			if (humannpc != null) {
				humannpc.removeFromWorld();
			}
		}
		npcs.clear();
	}

	public BWorld getBWorld(final World world) {
		BWorld bworld = bworlds.get(world);
		if (bworld != null) {
			return bworld;
		}
		bworld = new BWorld(world);
		bworlds.put(world, bworld);
		return bworld;
	}

	public void DisableTask() {
		Bukkit.getServer().getScheduler().cancelTask(taskid);
	}

	public HumanNPC spawnHumanNPC(final String name, final Location l) {
		++lastID;
		final int id = lastID;
		return this.spawnHumanNPC(name, l, id);
	}

	public HumanNPC spawnHumanNPC(final String name, final Location l, final int id) {
		final BWorld world = getBWorld(l.getWorld());
		final HumanEntity humanEntity = new HumanEntity(this, world, new GameProfile(UUID.randomUUID(), name), new PlayerInteractManager(world.getWorldServer()));
		humanEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		world.getWorldServer().addEntity(humanEntity);
		final HumanNPC humannpc = new HumanNPC(humanEntity, id);
		npcs.add(humannpc);
		return humannpc;
	}

	public HumanNPC getHumanNPC(final int id) {
		for (final HumanNPC n : npcs) {
			if (n.getID() == id) {
				return n;
			}
		}
		return null;
	}

	public BServer getServer() {
		return server;
	}

	public NetworkCore getNetworkCore() {
		return networkCore;
	}

}