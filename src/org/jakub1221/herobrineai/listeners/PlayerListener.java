package org.jakub1221.herobrineai.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if ((event.getClickedBlock() != null) && (event.getPlayer().getItemInHand() != null) && (event.getClickedBlock().getType() == Material.JUKEBOX)) {
			final ItemStack item = event.getPlayer().getItemInHand();
			final Jukebox block = (Jukebox) event.getClickedBlock().getState();
			if (!block.isPlaying() && (item.getType() == Material.RECORD_11)) {
				HerobrineAI.getPluginCore().getAICore();
				if (!AICore.isDiscCalled) {
					final Player player = event.getPlayer();
					HerobrineAI.getPluginCore().getAICore();
					AICore.isDiscCalled = true;
					HerobrineAI.getPluginCore().getAICore().CancelTarget(Core.CoreType.ANY);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
						@Override
						public void run() {
							HerobrineAI.getPluginCore().getAICore().callByDisc(player);
						}
					}, 50L);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerEnterBed(final PlayerBedEnterEvent event) {
		if (new Random().nextInt(100) > 75) {
			final Player player = event.getPlayer();
			event.setCancelled(true);
			HerobrineAI.getPluginCore().getAICore().playerBedEnter(player);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		if (event.getPlayer().getEntityId() != HerobrineAI.HerobrineEntityID) {
			HerobrineAI.getPluginCore().getAICore();
			if ((AICore.PlayerTarget == event.getPlayer()) && (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == Core.CoreType.GRAVEYARD)
					&& (event.getPlayer().getLocation().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard"))) {
				HerobrineAI.getPluginCore().getAICore();
				if (AICore.isTarget && new Random().nextBoolean()) {
					event.getPlayer().teleport(HerobrineAI.getPluginCore().getAICore().getGraveyard().getSavedLocation());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKick(final PlayerKickEvent event) {
		if (event.getPlayer().getEntityId() == HerobrineAI.HerobrineEntityID) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		if (event.getPlayer().getEntityId() == HerobrineAI.HerobrineEntityID) {
			if (event.getFrom().getWorld() != event.getTo().getWorld()) {
				HerobrineAI.getPluginCore().HerobrineRemove();
				HerobrineAI.getPluginCore().HerobrineSpawn(event.getTo());
				event.setCancelled(true);
				return;
			}
			if ((HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == Core.CoreType.RANDOM_POSITION)
					&& (HerobrineAI.HerobrineNPC.getEntity().getBukkitEntity().getLocation().getBlockX() > HerobrineAI.getPluginCore().getConfigDB().WalkingModeXRadius)
					&& (HerobrineAI.HerobrineNPC.getEntity().getBukkitEntity().getLocation().getBlockX() < -HerobrineAI.getPluginCore().getConfigDB().WalkingModeXRadius)
					&& (HerobrineAI.HerobrineNPC.getEntity().getBukkitEntity().getLocation().getBlockZ() > HerobrineAI.getPluginCore().getConfigDB().WalkingModeZRadius)
					&& (HerobrineAI.HerobrineNPC.getEntity().getBukkitEntity().getLocation().getBlockZ() < -HerobrineAI.getPluginCore().getConfigDB().WalkingModeZRadius)) {
				HerobrineAI.getPluginCore().getAICore().CancelTarget(Core.CoreType.RANDOM_POSITION);
				HerobrineAI.HerobrineNPC.moveTo(new Location(Bukkit.getServer().getWorlds().get(0), 0.0, -20.0, 0.0));
			}
		}
	}

	@EventHandler
	public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
		if (event.getPlayer().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(final PlayerDeathEvent event) {
		if (event.getEntity().getEntityId() == HerobrineAI.HerobrineEntityID) {
			event.setDeathMessage("");
			HerobrineAI.getPluginCore().HerobrineRemove();
			final Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), 0.0, -20.0, 0.0);
			nowloc.setYaw(1.0f);
			nowloc.setPitch(1.0f);
			HerobrineAI.getPluginCore().HerobrineSpawn(nowloc);
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(final PlayerMoveEvent event) {
		if ((event.getPlayer().getEntityId() != HerobrineAI.HerobrineEntityID) && (event.getPlayer().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard"))) {
			final Player player = event.getPlayer();
			player.teleport(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49, 4.0, 10.69, -179.85f, 0.44999f));
		}
	}

}