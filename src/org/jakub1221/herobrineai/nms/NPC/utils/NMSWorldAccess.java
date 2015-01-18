package org.jakub1221.herobrineai.nms.NPC.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_8_R1.PlayerChunkMap;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;

public class NMSWorldAccess {

	private CraftWorld cWorld;
	private WorldServer wServer;

	public NMSWorldAccess(final World world) {
		try {
			cWorld = (CraftWorld) world;
			wServer = cWorld.getHandle();
		} catch (Exception ex) {
			Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
		}
	}

	public PlayerChunkMap getPlayerManager() {
		return wServer.getPlayerChunkMap();
	}

	public CraftWorld getCraftWorld() {
		return cWorld;
	}

	public WorldServer getWorldServer() {
		return wServer;
	}

}