package org.jakub1221.herobrineai.nms.NPC.entity;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EnumGamemode;
import net.minecraft.server.v1_8_R1.PlayerInteractManager;
import com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.jakub1221.herobrineai.nms.NPC.NPCCore;
import org.jakub1221.herobrineai.nms.NPC.network.NetworkHandler;
import org.jakub1221.herobrineai.nms.NPC.utils.NMSWorldAccess;

public class HumanEntity extends EntityPlayer {

	public HumanEntity(final NPCCore npcCore, final NMSWorldAccess world, final GameProfile s, final PlayerInteractManager playerInteractManager) {
		super(npcCore.getServer().getMCServer(), world.getWorldServer(), s, playerInteractManager);
		playerInteractManager.b(EnumGamemode.SURVIVAL);
		playerConnection = new NetworkHandler(npcCore, this);
		fauxSleeping = true;
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

	private CraftPlayer cplayer;

	@Override
	public CraftPlayer getBukkitEntity() {
		if (cplayer == null) {
			cplayer = new CraftPlayer((CraftServer) Bukkit.getServer(), this);
		}
		return cplayer;
	}

}