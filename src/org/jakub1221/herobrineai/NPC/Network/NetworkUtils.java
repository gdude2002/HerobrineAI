package org.jakub1221.herobrineai.NPC.Network;

import net.minecraft.server.v1_7_R4.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NetworkUtils {

	public static void sendPacketNearby(final Location location, final Packet packet) {
		sendPacketNearby(location, packet, 64.0);
	}

	public static void sendPacketNearby(final Location location, final Packet packet, double radius) {
		radius *= radius;
		final World world = location.getWorld();
		Player[] onlinePlayers;
		for (int length = (onlinePlayers = Bukkit.getServer().getOnlinePlayers()).length, i = 0; i < length; ++i) {
			final Player p = onlinePlayers[i];
			if (p != null) {
				if (world == p.getWorld()) {
					if (location.distanceSquared(p.getLocation()) <= radius) {
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
					}
				}
			}
		}
	}

	public static ItemStack[] combineItemStackArrays(final Object[] a, final Object[] b) {
		final ItemStack[] c = new ItemStack[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

}