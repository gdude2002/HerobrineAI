package org.jakub1221.herobrineai.nms.NPC.network;

import net.minecraft.server.v1_8_R1.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NetworkUtils {

	public static void sendPacketNearby(final Location location, final Packet packet) {
		sendPacketNearby(location, packet, 64.0);
	}

	public static void sendPacketNearby(final Location location, final Packet packet, double radius) {
		radius *= radius;
		final World world = location.getWorld();
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (world == player.getWorld()) {
				if (location.distanceSquared(player.getLocation()) <= radius) {
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
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