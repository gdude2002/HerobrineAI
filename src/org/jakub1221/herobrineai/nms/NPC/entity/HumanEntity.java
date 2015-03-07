package org.jakub1221.herobrineai.nms.NPC.entity;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EnumGamemode;
import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.PlayerInteractManager;
import net.minecraft.server.v1_8_R1.WorldServer;

import com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.jakub1221.herobrineai.nms.NPC.NPCCore;
import org.jakub1221.herobrineai.nms.NPC.network.NetworkHandler;

public class HumanEntity extends EntityPlayer {

	public HumanEntity(final NPCCore npcCore, final WorldServer worldserver, final GameProfile s, final PlayerInteractManager playerInteractManager) {
		super(MinecraftServer.getServer(), worldserver, s, playerInteractManager);
		playerInteractManager.b(EnumGamemode.SURVIVAL);
		playerConnection = new NetworkHandler(npcCore, this);
		fauxSleeping = true;
	}

	@Override
	public void move(final double x, final double y, final double z) {
		setPosition(x, y, z);
	}

	@Override
	public boolean a(final EntityHuman entity) {
		return super.a(entity);
	}

	@Override
	public void c(Entity entity) {
		super.c(entity);
	}

	private CraftPlayer cplayer;

	@Override
	public CraftPlayer getBukkitEntity() {
		if (cplayer == null) {
			cplayer = new CraftPlayer((CraftServer) Bukkit.getServer(), this);
		}
		return cplayer;
	}

}