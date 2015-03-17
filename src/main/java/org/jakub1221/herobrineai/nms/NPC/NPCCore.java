package org.jakub1221.herobrineai.nms.NPC;

import net.minecraft.server.v1_8_R2.PlayerInteractManager;
import net.minecraft.server.v1_8_R2.WorldServer;

import com.mojang.authlib.GameProfile;

import org.bukkit.Location;
import org.bukkit.World;

import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

import org.jakub1221.herobrineai.nms.NPC.entity.HumanEntity;
import org.jakub1221.herobrineai.nms.NPC.entity.Herobrine;

public class NPCCore {

	private int lastID = 0;

	public Herobrine spawnHumanNPC(final Location l, GameProfile profile) {
		++lastID;
		final int id = lastID;
		return this.spawnHumanNPC(l, id, profile);
	}

	private Herobrine spawnHumanNPC(final Location l, final int id, GameProfile profile) {
		WorldServer worldserver = getWorldServer(l.getWorld());
		final HumanEntity humanEntity = new HumanEntity(this, worldserver, profile, new PlayerInteractManager(worldserver));
		humanEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		worldserver.addEntity(humanEntity);
		final Herobrine humannpc = new Herobrine(humanEntity, id);
		return humannpc;
	}

	private WorldServer getWorldServer(World world) {
		return ((CraftWorld) world).getHandle();
	}

}