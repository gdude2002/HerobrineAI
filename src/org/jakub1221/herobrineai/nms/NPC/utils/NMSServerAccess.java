package org.jakub1221.herobrineai.nms.NPC.utils;

import java.util.List;
import java.util.logging.Logger;

import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;

public class NMSServerAccess {

	private static final NMSServerAccess ins = new NMSServerAccess();

	public static NMSServerAccess getInstance() {
		return ins;
	}

	private Server server = Bukkit.getServer();
	private CraftServer cServer = (CraftServer) server;
	private MinecraftServer mcServer = cServer.getServer();

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

	public MinecraftServer getMCServer() {
		return mcServer;
	}

}