package org.jakub1221.herobrineai.misc;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemName {

	public static ItemMeta meta;
	public static SkullMeta skullmeta;

	static {
		ItemName.meta = null;
		ItemName.skullmeta = null;
	}

	public static ItemStack colorLeatherArmor(final ItemStack i, final Color color) {
		final LeatherArmorMeta la_meta = (LeatherArmorMeta) i.getItemMeta();
		la_meta.setColor(color);
		i.setItemMeta(la_meta);
		return i;
	}

	public static ItemStack setName(final ItemStack item, final String name) {
		(ItemName.meta = item.getItemMeta()).setDisplayName(name);
		item.setItemMeta(ItemName.meta);
		return item;
	}

	public static ItemStack setLore(final ItemStack item, final ArrayList<String> lore) {
		(ItemName.meta = item.getItemMeta()).setLore(lore);
		item.setItemMeta(ItemName.meta);
		return item;
	}

	public static ItemStack setNameAndLore(final ItemStack item, final String name, final ArrayList<String> lore) {
		(ItemName.meta = item.getItemMeta()).setDisplayName(name);
		ItemName.meta.setLore(lore);
		item.setItemMeta(ItemName.meta);
		return item;
	}

	public static ArrayList<String> getLore(final ItemStack item) {
		return (ArrayList<String>) item.getItemMeta().getLore();
	}

	public static String getName(final ItemStack item) {
		return item.getItemMeta().getDisplayName();
	}

	public static ItemStack CreateSkull(final String data) {
		final ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		final SkullMeta skullmeta = (SkullMeta) skull.getItemMeta();
		skullmeta.setOwner(data);
		skullmeta.setDisplayName(ChatColor.RESET + data);
		skull.setItemMeta(skullmeta);
		return skull;
	}

}