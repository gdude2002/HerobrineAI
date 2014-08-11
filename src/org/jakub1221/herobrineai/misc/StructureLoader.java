package org.jakub1221.herobrineai.misc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class StructureLoader {

	private int current;
	private int length;
	private InputStream inp;
	private YamlConfiguration file;

	public StructureLoader(final InputStream in) {
		super();
		current = 0;
		length = 0;
		inp = in;
		file = new YamlConfiguration();
		try {
			file.load(inp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (InvalidConfigurationException e3) {
			e3.printStackTrace();
		}
		try {
			inp.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void Build(final World world, final int MainX, final int MainY, final int MainZ) {
		length = file.getInt("DATA.LENGTH") - 1;
		current = 0;
		while (current <= length) {
			world.getBlockAt(MainX + file.getInt("DATA." + current + ".X"), MainY + file.getInt("DATA." + current + ".Y"), MainZ + file.getInt("DATA." + current + ".Z"))
					.setTypeIdAndData(file.getInt("DATA." + current + ".ID"), (byte) file.getInt("DATA." + current + ".DATA"), false);
			++current;
		}
	}

}