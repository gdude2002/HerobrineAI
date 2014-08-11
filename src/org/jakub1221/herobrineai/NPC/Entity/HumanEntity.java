package org.jakub1221.herobrineai.NPC.Entity;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumGamemode;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.jakub1221.herobrineai.NPC.NPCCore;
import org.jakub1221.herobrineai.NPC.NMS.BWorld;
import org.jakub1221.herobrineai.NPC.Network.NetworkHandler;

public class HumanEntity extends EntityPlayer {

	public HumanEntity(final NPCCore npcCore, final BWorld world, final GameProfile s, final PlayerInteractManager playerInteractManager) {
		super(npcCore.getServer().getMCServer(), world.getWorldServer(), s, playerInteractManager);
		playerInteractManager.b(EnumGamemode.SURVIVAL);
		playerConnection = new NetworkHandler(npcCore, this);
		fauxSleeping = true;
	}

	public void setBukkitEntity(final org.bukkit.entity.Entity entity) {
		bukkitEntity = (CraftEntity) entity;
	}

	@Override
	public void move(final double arg0, final double arg1, final double arg2) {
		setPosition(arg0, arg1, arg2);
	}

	@Override
	public boolean a(final EntityHuman entity) {
		return super.a(entity);
	}

	@Override
	public void c(Entity entity) {
		super.c(entity);
	}

}