package org.jakub1221.herobrineai.listeners;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.misc.ItemName;

public class InventoryListener implements Listener {
	Logger log;

	public InventoryListener() {
		super();
		log = Logger.getLogger("Minecraft");
	}

	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent event) {
		if (event.getInventory().getType() == InventoryType.CHEST) {
			final Object[] data = { event.getPlayer(), event.getInventory() };
			HerobrineAI.getPlugin().getAICore().getCore(Core.CoreType.BOOK).runCore(data);
			if ((new Random().nextInt(100) > 97) && HerobrineAI.getPlugin().getConfigDB().useHeads && (event.getInventory().firstEmpty() != -1)
					&& HerobrineAI.getPlugin().getAICore().getResetLimits().isHead()) {
				event.getInventory().setItem(event.getInventory().firstEmpty(), ItemName.CreateSkull(event.getPlayer().getName()));
			}
		}
	}

	@EventHandler
	public void onInventoryOpen(final InventoryOpenEvent event) {
		if (((event.getInventory().getType() == InventoryType.CHEST) || (event.getInventory().getType() == InventoryType.FURNACE) || (event.getInventory().getType() == InventoryType.WORKBENCH))
				&& HerobrineAI.getPlugin().getConfigDB().useWorlds.contains(event.getPlayer().getLocation().getWorld().getName()) && HerobrineAI.getPlugin().getConfigDB().placeSigns
				&& HerobrineAI.getPlugin().getSupport().checkSigns(event.getPlayer().getLocation()) && HerobrineAI.getPlugin().getAICore().getResetLimits().isSign()) {
			final Object[] data = { event.getPlayer().getLocation(), event.getPlayer().getLocation() };
			HerobrineAI.getPlugin().getAICore().getCore(Core.CoreType.SIGNS).runCore(data);
		}
	}
}
