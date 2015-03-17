package org.jakub1221.herobrineai.nms.NPC.network;

import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.MinecraftServer;
import net.minecraft.server.v1_8_R2.Packet;
import net.minecraft.server.v1_8_R2.PlayerConnection;

import org.jakub1221.herobrineai.nms.NPC.NPCCore;

public class NetworkHandler extends PlayerConnection {

	public NetworkHandler(final NPCCore npcCore, final EntityPlayer entityPlayer) {
		super(MinecraftServer.getServer(), new NetworkCore(), entityPlayer);
	}

	@Override
	public void a(final double d0, final double d1, final double d2, final float f, final float f1) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void sendPacket(final Packet packet) {
	}

}