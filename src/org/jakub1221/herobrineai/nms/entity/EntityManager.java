package org.jakub1221.herobrineai.nms.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

public class EntityManager {

	private HashMap<Integer, CustomEntity> mobList;

	public EntityManager() {
		super();
		mobList = new HashMap<Integer, CustomEntity>();
	}

	public void spawnCustomZombie(final Location loc, final MobType mbt) {
		final World world = loc.getWorld();
		final net.minecraft.server.v1_7_R4.World mcWorld = ((CraftWorld) world).getHandle();
		final CustomZombie zmb = new CustomZombie(mcWorld, loc, mbt);
		mcWorld.addEntity(zmb);
		mobList.put(new Integer(zmb.getBukkitEntity().getEntityId()), zmb);
	}

	public void spawnCustomSkeleton(final Location loc, final MobType mbt) {
		final World world = loc.getWorld();
		final net.minecraft.server.v1_7_R4.World mcWorld = ((CraftWorld) world).getHandle();
		final CustomSkeleton zmb = new CustomSkeleton(mcWorld, loc, mbt);
		mcWorld.addEntity(zmb);
		mobList.put(new Integer(zmb.getBukkitEntity().getEntityId()), zmb);
	}

	public boolean isCustomMob(final int id) {
		return mobList.containsKey(new Integer(id));
	}

	public CustomEntity getMobType(final int id) {
		return mobList.get(new Integer(id));
	}

	public void removeMob(final int id) {
		mobList.get(new Integer(id)).Kill();
		mobList.remove(new Integer(id));
	}

	public void removeAllMobs() {
		mobList.clear();
	}

	public void killAllMobs() {
		for (final Map.Entry<Integer, CustomEntity> s : mobList.entrySet()) {
			s.getValue().Kill();
		}
		removeAllMobs();
	}

}