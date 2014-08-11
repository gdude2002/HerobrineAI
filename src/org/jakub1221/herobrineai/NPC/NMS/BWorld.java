package org.jakub1221.herobrineai.NPC.NMS;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_7_R4.AxisAlignedBB;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PlayerChunkMap;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BWorld {

	private BServer server;
	private World world;
	private CraftWorld cWorld;
	private WorldServer wServer;

	public BWorld(final BServer server, final String worldName) {
		super();
		this.server = server;
		world = server.getServer().getWorld(worldName);
		try {
			cWorld = (CraftWorld) world;
			wServer = cWorld.getHandle();
		} catch (Exception ex) {
			Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
		}
	}

	public BWorld(final World world) {
		super();
		this.world = world;
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

	public void removeEntity(final String name, final Player player, final JavaPlugin plugin) {
		server.getServer().getScheduler().callSyncMethod(plugin,
			new Callable<Object>() {
				@SuppressWarnings("unchecked")
				@Override
				public Object call() throws Exception {
					final Location loc = player.getLocation();
					final CraftWorld craftWorld = (CraftWorld) player.getWorld();
					final CraftPlayer craftPlayer = (CraftPlayer) player;
					final double x = loc.getX() + 0.5;
					final double y = loc.getY() + 0.5;
					final double z = loc.getZ() + 0.5;
					final double radius = 10.0;
					final AxisAlignedBB bb = AxisAlignedBB.a(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
					List<Entity> entities = craftWorld.getHandle().getEntities(craftPlayer.getHandle(), bb);
					for (final Entity o : entities) {
						if (!(o instanceof EntityPlayer)) {
							((EntityPlayer) o).getBukkitEntity().remove();
						}
					}
					return null;
				}
			}
		);
	}

}