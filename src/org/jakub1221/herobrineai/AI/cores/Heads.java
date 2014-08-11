package org.jakub1221.herobrineai.AI.cores;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.misc.BlockChanger;

public class Heads extends Core {

	private boolean isCalled;
	private ArrayList<Block> headList;

	public Heads() {
		super(CoreType.HEADS, AppearType.NORMAL);
		isCalled = false;
		headList = new ArrayList<Block>();
	}

	@Override
	public CoreResult CallCore(final Object[] data) {
		if (isCalled) {
			return new CoreResult(false, "There are already heads! Wait until they disappear.");
		}
		if (!Bukkit.getPlayer((String) data[0]).isOnline()) {
			return new CoreResult(false, "Player is offline.");
		}
		final Player player = Bukkit.getServer().getPlayer((String) data[0]);
		if (!HerobrineAI.getPluginCore().getSupport().checkBuild(player.getLocation())) {
			return new CoreResult(false, "Player is in secure area!");
		}
		if (HerobrineAI.getPluginCore().getConfigDB().UseHeads) {
			final Location loc = player.getLocation();
			final int px = loc.getBlockX();
			final int pz = loc.getBlockZ();
			int y = 0;
			int x = -7;
			int z = -7;
			for (x = -7; x <= 7; ++x) {
				for (z = -7; z <= 7; ++z) {
					if (new Random().nextInt(7) == new Random().nextInt(7)) {
						if (HerobrineAI.isAllowedBlock(loc.getWorld().getHighestBlockAt(px + x, pz + z).getType())) {
							y = loc.getWorld().getHighestBlockYAt(px + x, pz + z);
						} else {
							y = loc.getWorld().getHighestBlockYAt(px + x, pz + z) + 1;
						}
						final Block block = loc.getWorld().getBlockAt(px + x, y, pz + z);
						BlockChanger.PlaceSkull(block.getLocation(), "Herobrine");
						headList.add(block);
					}
				}
			}
			isCalled = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
				@Override
				public void run() {
					Heads.this.RemoveHeads();
				}
			}, 100L);
			return new CoreResult(true, "Spawned some heads near " + player.getName() + "!");
		}
		return new CoreResult(false, "Heads are disabled!");
	}

	public void RemoveHeads() {
		for (final Block h : headList) {
			h.setType(Material.AIR);
		}
		headList.clear();
		isCalled = false;
	}

	public ArrayList<Block> getHeadList() {
		return headList;
	}

}