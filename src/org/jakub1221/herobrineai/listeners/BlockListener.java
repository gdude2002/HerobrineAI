package org.jakub1221.herobrineai.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.cores.Heads;

public class BlockListener implements Listener {

	@EventHandler
	public void onBlockIgnite(final BlockIgniteEvent event) {
		if (event.getBlock() != null) {
			final Block blockt = event.getBlock();
			final Location blockloc = blockt.getLocation();
			if (event.getPlayer() != null) {
				blockloc.setY(blockloc.getY() - 1.0);
				final Block block = blockloc.getWorld().getBlockAt(blockloc);
				if ((block.getType() == Material.NETHERRACK) && (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY() - 1, blockloc.getBlockZ()).getType() == Material.NETHERRACK)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX() - 1, blockloc.getBlockY() - 1, blockloc.getBlockZ()).getType() == Material.GOLD_BLOCK)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX() - 1, blockloc.getBlockY() - 1, blockloc.getBlockZ() - 1).getType() == Material.GOLD_BLOCK)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX() - 1, blockloc.getBlockY() - 1, blockloc.getBlockZ() + 1).getType() == Material.GOLD_BLOCK)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX() + 1, blockloc.getBlockY() - 1, blockloc.getBlockZ()).getType() == Material.GOLD_BLOCK)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX() + 1, blockloc.getBlockY() - 1, blockloc.getBlockZ() - 1).getType() == Material.GOLD_BLOCK)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX() + 1, blockloc.getBlockY() - 1, blockloc.getBlockZ() + 1).getType() == Material.GOLD_BLOCK)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY() - 1, blockloc.getBlockZ() - 1).getType() == Material.GOLD_BLOCK)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY() - 1, blockloc.getBlockZ() + 1).getType() == Material.GOLD_BLOCK)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY(), blockloc.getBlockZ() + 1).getType() == Material.REDSTONE_TORCH_ON)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX(), blockloc.getBlockY(), blockloc.getBlockZ() - 1).getType() == Material.REDSTONE_TORCH_ON)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX() + 1, blockloc.getBlockY(), blockloc.getBlockZ()).getType() == Material.REDSTONE_TORCH_ON)
						&& (block.getWorld().getBlockAt(blockloc.getBlockX() - 1, blockloc.getBlockY(), blockloc.getBlockZ()).getType() == Material.REDSTONE_TORCH_ON)
						&& HerobrineAI.getPlugin().getConfigDB().useTotem && !AICore.isTotemCalled) {
					HerobrineAI.getPlugin().getAICore().playerCallTotem(event.getPlayer());
				}
			}
		}
		if (event.getBlock().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event) {
		if (event.getBlock().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) {
			event.setCancelled(true);
			return;
		}
		final Heads h = (Heads) HerobrineAI.getPlugin().getAICore().getCore(Core.CoreType.HEADS);
		final ArrayList<Block> list = h.getHeadList();
		if (list.contains(event.getBlock())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event) {
		if (event.getBlock().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) {
			event.setCancelled(true);
		}
	}

}