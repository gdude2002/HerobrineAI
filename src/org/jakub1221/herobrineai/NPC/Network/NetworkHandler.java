package org.jakub1221.herobrineai.NPC.Network;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.jakub1221.herobrineai.NPC.NPCCore;

public class NetworkHandler extends PlayerConnection {

	public NetworkHandler(final NPCCore npcCore, final EntityPlayer entityPlayer) {
		super(npcCore.getServer().getMCServer(), npcCore.getNetworkCore(), entityPlayer);
	}

	@Override
	public CraftPlayer getPlayer() {
		return new CraftPlayer((CraftServer) Bukkit.getServer(), player);
	}

	@Override
	public void a(final double d0, final double d1, final double d2, final float f, final float f1) {
	}

	@Override
	public void sendPacket(final Packet packet) {
	}

}