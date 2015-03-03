package org.jakub1221.herobrineai.nms.NPC.entity;

import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftBat;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;

public class HumanNPC {

	private EntityPlayer entity;
	private final int id;

	public HumanNPC(final HumanEntity humanEntity, final int id) {
		entity = humanEntity;
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public EntityPlayer getNMSEntity() {
		return entity;
	}

	public void armSwingAnimation() {
		((WorldServer) getNMSEntity().world).tracker.a(getNMSEntity(), new PacketPlayInArmAnimation());
	}

	public void hurtAnimation() {
		((LivingEntity) entity.getBukkitEntity()).damage(0.5);
		((LivingEntity) entity.getBukkitEntity()).setHealth(20);
	}

	public void setItemInHand(final ItemStack item) {
		if (item != null) {
			((org.bukkit.entity.HumanEntity) getNMSEntity().getBukkitEntity()).setItemInHand(item);
		}
	}

	public String getName() {
		return ((HumanEntity) getNMSEntity()).getName();
	}

	public void moveTo(final Location loc) {
		teleport(loc);
	}

	public void teleport(final Location loc) {
		if (loc.getWorld().getName().equals(getNMSEntity().world.getWorld().getName())) {
			LivingEntity bat = (LivingEntity) getBukkitEntity().getPassenger();
			if (bat != null) {
				bat.leaveVehicle();
			}
			getNMSEntity().locX = loc.getX();
			getNMSEntity().locY = loc.getY();
			getNMSEntity().locZ = loc.getZ();
			if (bat != null) {
				bat.teleport(getBukkitEntity());
			} else {
				bat = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.BAT);
			}
			bat.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 0));
			((CraftBat) bat).getHandle().mount(getNMSEntity());
		} else {
			HerobrineAI.getPluginCore().hbSpawnData = loc;
			HerobrineAI.getPluginCore().removeHBNextTick = true;
		}
	}

	public PlayerInventory getInventory() {
		return ((org.bukkit.entity.HumanEntity) getNMSEntity().getBukkitEntity()).getInventory();
	}

	public void removeFromWorld() {
		try {
			entity.world.removeEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void lookAtPoint(final Location point) {
		if (getNMSEntity().getBukkitEntity().getWorld() != point.getWorld()) {
			return;
		}
		final Location npcLoc = ((LivingEntity) getNMSEntity().getBukkitEntity()).getEyeLocation();
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
		((EntityPlayer) getNMSEntity()).yaw = (float) (newYaw - 90.0);
		((EntityPlayer) getNMSEntity()).pitch = (float) newPitch;
		((EntityPlayer) getNMSEntity()).aJ = (float) (newYaw - 90.0);
		((EntityPlayer) getNMSEntity()).aI = (float) (newYaw - 90.0);
	}

	public org.bukkit.entity.Entity getBukkitEntity() {
		return entity.getBukkitEntity();
	}

}