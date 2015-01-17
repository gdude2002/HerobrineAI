package org.jakub1221.herobrineai.nms.entity;

import java.util.Random;

import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.misc.ItemName;

public class CustomSkeleton extends EntitySkeleton implements CustomEntity {
	private MobType mobType;

	public CustomSkeleton(final World world, final Location loc, final MobType mbt) {
		super(world);
		mobType = null;
		mobType = mbt;
		if (mbt == MobType.DEMON) {
			spawnDemon(loc);
		}
	}

	public void spawnDemon(final Location loc) {
		getAttributeInstance(GenericAttributes.d).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getDouble("npc.Demon.Speed"));
		getAttributeInstance(GenericAttributes.maxHealth).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.HP"));
		setHealth(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.HP"));
		setCustomName("Demon");
		((Skeleton) getBukkitEntity()).getEquipment().setItemInHand(new ItemStack(Material.GOLDEN_APPLE, 1));
		((Skeleton) getBukkitEntity()).getEquipment().setHelmet(ItemName.colorLeatherArmor(new ItemStack(Material.LEATHER_HELMET, 1), Color.RED));
		((Skeleton) getBukkitEntity()).getEquipment().setChestplate(ItemName.colorLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE, 1), Color.RED));
		((Skeleton) getBukkitEntity()).getEquipment().setLeggings(ItemName.colorLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS, 1), Color.RED));
		((Skeleton) getBukkitEntity()).getEquipment().setBoots(ItemName.colorLeatherArmor(new ItemStack(Material.LEATHER_BOOTS, 1), Color.RED));
		getBukkitEntity().teleport(loc);
	}

	public CustomSkeleton(final World world) {
		super(world);
		mobType = null;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void Kill() {
		for (int i = 1; i <= 2500; ++i) {
			if (HerobrineAI.getPluginCore().getConfigDB().npc.contains("npc.Demon.Drops." + Integer.toString(i))) {
				final int chance = new Random().nextInt(100);
				if (chance <= HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.Drops." + Integer.toString(i) + ".Chance")) {
					getBukkitEntity().getLocation().getWorld().dropItemNaturally(getBukkitEntity().getLocation(),
					new ItemStack(Material.getMaterial(i), HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Demon.Drops." + Integer.toString(i) + ".Count")));
				}
			}
		}
		setHealth(0.0f);
	}

	@Override
	public MobType getMobType() {
		return mobType;
	}

}
