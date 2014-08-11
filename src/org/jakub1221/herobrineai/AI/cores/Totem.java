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
	public CoreResult CallCore(final Object[] data) {
		return TotemCall((Location) data[0], (String) data[1]);
	}

	public CoreResult TotemCall(final Location loc, final String caller) {
		AICore.isTotemCalled = false;
		loc.getWorld().strikeLightning(loc);
		if (HerobrineAI.getPluginCore().getConfigDB().TotemExplodes) {
			loc.getWorld().createExplosion(loc, 5.0f);
		}
		if (Bukkit.getServer().getPlayer(caller) != null) {
			if (Bukkit.getServer().getPlayer(caller).isOnline()) {
				HerobrineAI.getPluginCore().getAICore().setCoreTypeNow(CoreType.TOTEM);
				HerobrineAI.getPluginCore().getAICore().setAttackTarget(Bukkit.getServer().getPlayer(caller));
				final Player player = Bukkit.getServer().getPlayer(caller);
				final Player[] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
				if (Bukkit.getServer().getOnlinePlayers().length > 0) {
					Location ploc;
					for (int i = 0; i <= (Bukkit.getServer().getOnlinePlayers().length - 1); ++i) {
						ploc = AllOnPlayers[i].getLocation();
						if ((AllOnPlayers[i].getName() != player.getName()) && ((ploc.getX() + 10.0) > loc.getX()) && ((ploc.getX() - 10.0) < loc.getX()) && ((ploc.getZ() + 10.0) > loc.getZ())
								&& ((ploc.getZ() - 10.0) < loc.getZ())) {
							Message.SendMessage(AllOnPlayers[i]);
							if (HerobrineAI.getPluginCore().getConfigDB().UsePotionEffects) {
								AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
								AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
								AllOnPlayers[i].addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
							}
						}
					}
				}
			} else {
				boolean hasTarget = false;
				Player target = null;
				final Player[] AllOnPlayers2 = Bukkit.getServer().getOnlinePlayers();
				if (Bukkit.getServer().getOnlinePlayers().length > 0) {
					Location ploc2;
					for (int j = 0; j <= (Bukkit.getServer().getOnlinePlayers().length - 1); ++j) {
						if (!hasTarget) {
							ploc2 = AllOnPlayers2[j].getLocation();
							if (((ploc2.getX() + 10.0) > loc.getX()) && ((ploc2.getX() - 10.0) < loc.getX()) && ((ploc2.getZ() + 10.0) > loc.getZ()) && ((ploc2.getZ() - 10.0) < loc.getZ())) {
								hasTarget = true;
								target = AllOnPlayers2[j];
							}
						}
					}
				}
				if (hasTarget) {
					HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.TOTEM);
					HerobrineAI.getPluginCore().getAICore().setAttackTarget(target);
					final Player player2 = target;
					if (Bukkit.getServer().getOnlinePlayers().length > 0) {
						Location ploc3;
						for (int k = 0; k <= (Bukkit.getServer().getOnlinePlayers().length - 1); ++k) {
							ploc3 = AllOnPlayers2[k].getLocation();
							if ((AllOnPlayers2[k].getName() != player2.getName()) && ((ploc3.getX() + 20.0) > loc.getX()) && ((ploc3.getX() - 20.0) < loc.getX()) && ((ploc3.getZ() + 20.0) > loc.getZ())
									&& ((ploc3.getZ() - 20.0) < loc.getZ())) {
								Message.SendMessage(AllOnPlayers2[k]);
								AllOnPlayers2[k].addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
								AllOnPlayers2[k].addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
								AllOnPlayers2[k].addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
							}
						}
					}
				}
			}
		} else {
			boolean hasTarget = false;
			Player target = null;
			final Player[] AllOnPlayers2 = Bukkit.getServer().getOnlinePlayers();
			if (Bukkit.getServer().getOnlinePlayers().length > 0) {
				Location ploc2;
				for (int j = 0; j <= (Bukkit.getServer().getOnlinePlayers().length - 1); ++j) {
					if (!hasTarget) {
						ploc2 = AllOnPlayers2[j].getLocation();
						if (((ploc2.getX() + 20.0) > loc.getX()) && ((ploc2.getX() - 20.0) < loc.getX()) && ((ploc2.getZ() + 20.0) > loc.getZ()) && ((ploc2.getZ() - 20.0) < loc.getZ())) {
							hasTarget = true;
							target = AllOnPlayers2[j];
						}
					}
				}
			}
			if (hasTarget) {
				HerobrineAI.getPluginCore().getAICore().CancelTarget(CoreType.TOTEM);
				HerobrineAI.getPluginCore().getAICore().setAttackTarget(target);
				final Player player2 = target;
				if (Bukkit.getServer().getOnlinePlayers().length > 0) {
					Location ploc3;
					for (int k = 0; k <= (Bukkit.getServer().getOnlinePlayers().length - 1); ++k) {
						if (AllOnPlayers2[k].getEntityId() != HerobrineAI.HerobrineEntityID) {
							ploc3 = AllOnPlayers2[k].getLocation();
							if ((AllOnPlayers2[k].getName() != player2.getName()) && ((ploc3.getX() + 20.0) > loc.getX()) && ((ploc3.getX() - 20.0) < loc.getX()) && ((ploc3.getZ() + 20.0) > loc.getZ())
									&& ((ploc3.getZ() - 20.0) < loc.getZ())) {
								Message.SendMessage(AllOnPlayers2[k]);
								AllOnPlayers2[k].addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
								AllOnPlayers2[k].addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
								AllOnPlayers2[k].addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
							}
						}
					}
				}
			}
		}
		return new CoreResult(false, "Totem called!");
	}

}