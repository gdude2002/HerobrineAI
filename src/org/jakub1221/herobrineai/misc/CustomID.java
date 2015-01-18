package org.jakub1221.herobrineai.misc;

import org.bukkit.inventory.ItemStack;

public class CustomID {

	private int id = 0;
	private int data = 0;

	public CustomID(final String _data) {
		if (_data != null && !_data.equals("0") && (_data != null) && (_data.length() > 0)) {
			final String[] both = _data.split("[:]");
			id = Integer.parseInt(both[0]);
			if (both.length > 1) {
				data = Integer.parseInt(both[1]);
			}
		}
	}

	public int getId() {
		return id;
	}

	public int getData() {
		return data;
	}

	public boolean hasData() {
		return data > 0;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getItemStack() {
		ItemStack item = null;
		if (id != 0) {
			if (data > 0) {
				item = new ItemStack(id, 1, (byte) data);
			} else {
				item = new ItemStack(id);
			}
		}
		return item;
	}

}