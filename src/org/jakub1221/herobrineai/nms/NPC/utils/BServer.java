package org.jakub1221.herobrineai.nms.NPC.utils;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;

public class BServer {

	private static BServer ins;
	private MinecraftServer mcServer;
	private CraftServer cServer;
	private Server server;

	private BServer() {
		server = Bukkit.getServer();
		try {
			cServer = (CraftServer) server;
			mcServer = cServer.getServer();
		} catch (Exception ex) {
			Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
		}
	}

	public void stop() {
		mcServer.safeShutdown();
	}

	public Logger getLogger() {
		return cServer.getLogger();
	}

	public List<WorldServer> getWorldServers() {
		return mcServer.worlds;
	}

	public Server getServer() {
		return server;
	}

	public static BServer getInstance() {
		if (BServer.ins == null) {
			BServer.ins = new BServer();
		}
		return BServer.ins;
	}

	public MinecraftServer getMCServer() {
		return mcServer;
	}

}