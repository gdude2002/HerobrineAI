package org.jakub1221.herobrineai.nms.NPC.network;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PlayerConnection;

import org.jakub1221.herobrineai.nms.NPC.NPCCore;

public class NetworkHandler extends PlayerConnection {

	public NetworkHandler(final NPCCore npcCore, final EntityPlayer entityPlayer) {
		super(npcCore.getServer().getMCServer(), npcCore.getNetworkCore(), entityPlayer);
	}

	@Override
	public void a(final double d0, final double d1, final double d2, final float f, final float f1) {
	}

	@Override
	public void sendPacket(final Packet packet) {
	}

}