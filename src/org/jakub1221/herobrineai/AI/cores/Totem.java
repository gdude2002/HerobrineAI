package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.AI.Message;

public class Totem extends Core {

	public Totem() {
		super(CoreType.TOTEM, AppearType.APPEAR);
	}

	@Override
	public CoreResult callCore(final Object[] data) {
		return TotemCall((Location) data[0], (String) data[1]);
	}

	public CoreResult TotemCall(final Location loc, final String caller) {
		AICore.isTotemCalled = false;
		loc.getWorld().strikeLightning(loc);
		if (HerobrineAI.getPluginCore().getConfigDB().totemExplodes) {
			loc.getWorld().createExplosion(loc, 5.0f);
		}
		if (Bukkit.getServer().getPlayer(caller) != null) {
			if (Bukkit.getServer().getPlayer(caller).isOnline()) {
				HerobrineAI.getPluginCore().getAICore().setCoreTypeNow(CoreType.TOTEM);
				HerobrineAI.getPluginCore().getAICore().setAttackTarget(Bukkit.getServer().getPlayer(caller));
				final Player player = Bukkit.getServer().getPlayer(caller);
				for (Player oplayer : Bukkit.getOnlinePlayers()) {
					Location ploc = oplayer.getLocation();
					if (
						(oplayer.getName() != player.getName()) &&
						((ploc.getX() + 10.0) > loc.getX()) &&
						((ploc.getX() - 10.0) < loc.getX()) &&
						((ploc.getZ() + 10.0) > loc.getZ()) &&
						((ploc.getZ() - 10.0) < loc.getZ())
					) {
						Message.sendMessage(oplayer);
						if (HerobrineAI.getPluginCore().getConfigDB().usePotionEffects) {
							oplayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
							oplayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
							oplayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
						}
					}
				}
			} else {
				boolean hasTarget = false;
				Player target = null;
				for (Player oplayer : Bukkit.getOnlinePlayers()) {
					if (!hasTarget) {
						Location ploc = oplayer.getLocation();
						if (((ploc.getX() + 10.0) > loc.getX()) && ((ploc.getX() - 10.0) < loc.getX()) && ((ploc.getZ() + 10.0) > loc.getZ()) && ((ploc.getZ() - 10.0) < loc.getZ())) {
							hasTarget = true;
							target = oplayer;
						}
					}
				}
				if (hasTarget) {
					HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.TOTEM);
					HerobrineAI.getPluginCore().getAICore().setAttackTarget(target);
					final Player player = target;
					for (Player oplayer : Bukkit.getOnlinePlayers()) {
						Location ploc = oplayer.getLocation();
						if ((oplayer.getName() != player.getName()) && ((ploc.getX() + 20.0) > loc.getX()) && ((ploc.getX() - 20.0) < loc.getX()) && ((ploc.getZ() + 20.0) > loc.getZ())
								&& ((ploc.getZ() - 20.0) < loc.getZ())) {
							Message.sendMessage(oplayer);
							oplayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
							oplayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
							oplayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
						}
					}
				}
			}
		} else {
			boolean hasTarget = false;
			Player target = null;
			for (Player oplayer : Bukkit.getOnlinePlayers()) {
				if (!hasTarget) {
					Location ploc = oplayer.getLocation();
					if (((ploc.getX() + 20.0) > loc.getX()) && ((ploc.getX() - 20.0) < loc.getX()) && ((ploc.getZ() + 20.0) > loc.getZ()) && ((ploc.getZ() - 20.0) < loc.getZ())) {
						hasTarget = true;
						target = oplayer;
					}
				}
			}
			if (hasTarget) {
				HerobrineAI.getPluginCore().getAICore().cancelTarget(CoreType.TOTEM);
				HerobrineAI.getPluginCore().getAICore().setAttackTarget(target);
				final Player player = target;
				for (Player oplayer : Bukkit.getOnlinePlayers()) {
					if (oplayer.getEntityId() != HerobrineAI.herobrineEntityID) {
						Location ploc = oplayer.getLocation();
						if ((oplayer.getName() != player.getName()) && ((ploc.getX() + 20.0) > loc.getX()) && ((ploc.getX() - 20.0) < loc.getX()) && ((ploc.getZ() + 20.0) > loc.getZ())
								&& ((ploc.getZ() - 20.0) < loc.getZ())) {
							Message.sendMessage(oplayer);
							oplayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
							oplayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
							oplayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
						}
					}
				}
			}
		}
		return new CoreResult(false, "Totem called!");
	}

}