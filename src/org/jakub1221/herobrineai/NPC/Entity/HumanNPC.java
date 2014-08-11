package org.jakub1221.herobrineai.NPC.Entity;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jakub1221.herobrineai.HerobrineAI;

public class HumanNPC {

	private Entity entity;
	private final int id;

	public HumanNPC(final HumanEntity humanEntity, final int id) {
		super();
		entity = humanEntity;
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public Entity getEntity() {
		return entity;
	}

	public void ArmSwingAnimation() {
		((WorldServer) getEntity().world).tracker.a(getEntity(), new PacketPlayInArmAnimation());
	}

	public void HurtAnimation() {
		((LivingEntity) entity.getBukkitEntity()).damage(0.5);
		((LivingEntity) entity.getBukkitEntity()).setHealth(20);
	}

	public void setItemInHand(final ItemStack item) {
		if (item != null) {
			((org.bukkit.entity.HumanEntity) getEntity().getBukkitEntity()).setItemInHand(item);
		}
	}

	public String getName() {
		return ((HumanEntity) getEntity()).getName();
	}

	public void setPitch(final float pitch) {
		((HumanEntity) getEntity()).pitch = pitch;
	}

	public void moveTo(final Location loc) {
		Teleport(loc);
	}

	public void Teleport(final Location loc) {
		if (loc.getWorld().getName().equals(getEntity().world.getWorld().getName())) {
			getEntity().locX = loc.getX();
			getEntity().locY = loc.getY();
			getEntity().locZ = loc.getZ();
		} else {
			HerobrineAI.getPluginCore().hbSpawnData = loc;
			HerobrineAI.getPluginCore().removeHBNextTick = true;
		}
	}

	public PlayerInventory getInventory() {
		return ((org.bukkit.entity.HumanEntity) getEntity().getBukkitEntity()).getInventory();
	}

	public void removeFromWorld() {
		try {
			entity.world.removeEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setYaw(final float yaw) {
		((EntityPlayer) getEntity()).yaw = yaw;
		((EntityPlayer) getEntity()).aO = yaw;
		((EntityPlayer) getEntity()).aP = yaw;
	}

	public void lookAtPoint(final Location point) {
		if (getEntity().getBukkitEntity().getWorld() != point.getWorld()) {
			return;
		}
		final Location npcLoc = ((LivingEntity) getEntity().getBukkitEntity()).getEyeLocation();
		final double xDiff = point.getX() - npcLoc.getX();
		final double yDiff = point.getY() - npcLoc.getY();
		final double zDiff = point.getZ() - npcLoc.getZ();
		final double DistanceXZ = Math.sqrt((xDiff * xDiff) + (zDiff * zDiff));
		final double DistanceY = Math.sqrt((DistanceXZ * DistanceXZ) + (yDiff * yDiff));
		double newYaw = (Math.acos(xDiff / DistanceXZ) * 180.0) / 3.141592653589793;
		final double newPitch = ((Math.acos(yDiff / DistanceY) * 180.0) / 3.141592653589793) - 90.0;
		if (zDiff < 0.0) {
			newYaw += Math.abs(180.0 - newYaw) * 2.0;
		}
		((EntityPlayer) getEntity()).yaw = (float) (newYaw - 90.0);
		((EntityPlayer) getEntity()).pitch = (float) newPitch;
		((EntityPlayer) getEntity()).aP = (float) (newYaw - 90.0);
		((EntityPlayer) getEntity()).aO = (float) (newYaw - 90.0);
	}

	public org.bukkit.entity.Entity getBukkitEntity() {
		return entity.getBukkitEntity();
	}

}