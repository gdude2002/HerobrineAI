package org.jakub1221.herobrineai.nms.entity;

import java.util.Random;

import net.minecraft.server.v1_7_R4.EntityZombie;
import net.minecraft.server.v1_7_R4.GenericAttributes;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;

public class CustomZombie extends EntityZombie implements CustomEntity {

	private MobType mobType;

	public CustomZombie(final World world, final Location loc, final MobType mbt) {
		super(world);
		mobType = null;
		mobType = mbt;
		if (mbt == MobType.ARTIFACT_GUARDIAN) {
			spawnArtifactGuardian(loc);
		} else if (mbt == MobType.HEROBRINE_WARRIOR) {
			spawnHerobrineWarrior(loc);
		}
	}

	private void spawnArtifactGuardian(final Location loc) {
		getAttributeInstance(GenericAttributes.d).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getDouble("npc.Guardian.Speed"));
		getAttributeInstance(GenericAttributes.maxHealth).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Guardian.HP"));
		setHealth(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Guardian.HP"));
		setCustomName("Artifact Guardian");
		((Zombie) getBukkitEntity()).getEquipment().setItemInHand(new ItemStack(Material.GOLD_SWORD, 1));
		((Zombie) getBukkitEntity()).getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET, 1));
		((Zombie) getBukkitEntity()).getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
		((Zombie) getBukkitEntity()).getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
		((Zombie) getBukkitEntity()).getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
		getBukkitEntity().teleport(loc);
	}

	private void spawnHerobrineWarrior(final Location loc) {
		getAttributeInstance(GenericAttributes.d).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getDouble("npc.Warrior.Speed"));
		getAttributeInstance(GenericAttributes.maxHealth).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Warrior.HP"));
		setHealth(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Warrior.HP"));
		setCustomName("Herobrine?s Warrior");
		((Zombie) getBukkitEntity()).getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD, 1));
		((Zombie) getBukkitEntity()).getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
		((Zombie) getBukkitEntity()).getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
		((Zombie) getBukkitEntity()).getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
		((Zombie) getBukkitEntity()).getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
		getBukkitEntity().teleport(loc);
	}

	public CustomZombie(final World world) {
		super(world);
		mobType = null;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void Kill() {
		String mobS = "";
		if (mobType == MobType.ARTIFACT_GUARDIAN) {
			mobS = "Guardian";
		} else {
			mobS = "Warrior";
		}
		for (int i = 1; i <= 2500; ++i) {
			if (HerobrineAI.getPluginCore().getConfigDB().npc.contains("npc." + mobS + ".Drops." + Integer.toString(i))) {
				final int chance = new Random().nextInt(100);
				if (chance <= HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc." + mobS + ".Drops." + Integer.toString(i) + ".Chance")) {
					getBukkitEntity().getLocation().getWorld().dropItemNaturally(getBukkitEntity().getLocation(),
					new ItemStack(Material.getMaterial(i), HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc." + mobS + ".Drops." + Integer.toString(i) + ".Count")));
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