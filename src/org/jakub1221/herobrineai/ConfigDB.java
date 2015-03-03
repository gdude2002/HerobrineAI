package org.jakub1221.herobrineai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jakub1221.herobrineai.misc.CustomID;

public class ConfigDB {

	public YamlConfiguration config = new YamlConfiguration();

	public int showRate = 2;
	public boolean hitPlayer = true;
	public boolean sendMessages = true;
	public boolean lighting = true;
	public boolean destroyTorches = true;
	public int destroyTorchesRadius = 5;
	public int showInterval = 144000;
	public boolean totemExplodes = true;
	public boolean buildStuff = true;
	public boolean placeSigns = true;
	public boolean useTotem = true;
	public boolean writeBooks = true;
	public boolean killable= false;
	public boolean usePotionEffects = true;
	public int caveChance = 40;
	public int bookChance = 5;
	public int signChance = 5;
	public String deathMessage = "You cannot kill me!";
	public List<String> useWorlds = new ArrayList<String>();
	public List<String> useMessages = new ArrayList<String>();
	public List<String> useSignMessages = new ArrayList<String>();
	public List<String> useBookMessages = new ArrayList<String>();
	public boolean buildPyramids = true;
	public boolean useGraveyardWorld = true;
	public boolean buryPlayers = true;
	public boolean spawnWolves = true;
	public boolean spawnBats = true;
	public boolean attackCreative = true;
	public boolean attackOP = true;
	public boolean securedAreaBuild = true;
	public boolean securedAreaAttack = true;
	public boolean securedAreaHaunt = true;
	public boolean securedAreaSigns = true;
	public boolean securedAreaBooks = true;
	public int herobrineHP = 150;
	public int buildInterval = 72000;
	public boolean useHeads = true;
	public CustomID itemInHand = null;
	public boolean explosions = true;
	public boolean burn = true;
	public boolean curse = true;
	public int maxBooks = 1;
	public int maxSigns = 1;
	public int maxHeads = 1;
	public boolean useIgnorePermission = true;
	public boolean useSound = true;
	public File configF = new File("plugins/HerobrineAI/config.yml");

	public void reload() {
		try {
			config.load(configF);
		} catch (Exception e) {
			e.printStackTrace();
		}
		showInterval = config.getInt("config.ShowInterval", showInterval);
		showRate = config.getInt("config.ShowRate", showRate);
		hitPlayer = config.getBoolean("config.HitPlayer", hitPlayer);
		sendMessages = config.getBoolean("config.SendMessages", sendMessages);
		lighting = config.getBoolean("config.Lighting", lighting);
		destroyTorches = config.getBoolean("config.DestroyTorches", destroyTorches);
		destroyTorchesRadius = config.getInt("config.DestroyTorchesRadius", destroyTorchesRadius);
		useWorlds = config.getStringList("config.Worlds");
		totemExplodes = config.getBoolean("config.TotemExplodes", totemExplodes);
		buildStuff = config.getBoolean("config.BuildStuff", buildStuff);
		placeSigns = config.getBoolean("config.PlaceSigns", placeSigns);
		useTotem = config.getBoolean("config.UseTotem", useTotem);
		writeBooks = config.getBoolean("config.WriteBooks", writeBooks);
		killable = config.getBoolean("config.Killable", killable);
		usePotionEffects = config.getBoolean("config.UsePotionEffects", usePotionEffects);
		caveChance = config.getInt("config.CaveChance", caveChance);
		bookChance = config.getInt("config.BookChance", bookChance);
		signChance = config.getInt("config.SignChance", signChance);
		deathMessage = config.getString("config.DeathMessage", deathMessage);
		useMessages = config.getStringList("config.Messages");
		useSignMessages = config.getStringList("config.SignMessages");
		useBookMessages = config.getStringList("config.BookMessages");
		buildPyramids = config.getBoolean("config.BuildPyramids", buildPyramids);
		useGraveyardWorld = config.getBoolean("config.UseGraveyardWorld", useGraveyardWorld);
		buryPlayers = config.getBoolean("config.BuryPlayers", buryPlayers);
		spawnWolves = config.getBoolean("config.SpawnWolves", spawnWolves);
		spawnBats = config.getBoolean("config.SpawnBats", spawnBats);
		buildInterval = config.getInt("config.BuildInterval", buildInterval);
		herobrineHP = config.getInt("config.HerobrineHP", herobrineHP);
		attackCreative = config.getBoolean("config.AttackCreative", attackCreative);
		attackOP = config.getBoolean("config.AttackOP", attackOP);
		securedAreaBuild = config.getBoolean("config.SecuredArea.Build", securedAreaBuild);
		securedAreaAttack = config.getBoolean("config.SecuredArea.Attack", securedAreaAttack);
		securedAreaHaunt = config.getBoolean("config.SecuredArea.Haunt", securedAreaHaunt);
		securedAreaSigns = config.getBoolean("config.SecuredArea.Signs", securedAreaSigns);
		securedAreaBooks = config.getBoolean("config.SecuredArea.Books", securedAreaBooks);
		useHeads = config.getBoolean("config.UseHeads", useHeads);
		itemInHand = new CustomID(config.getString("config.ItemInHand"));
		explosions = config.getBoolean("config.Explosions", explosions);
		burn = config.getBoolean("config.Burn", burn);
		curse = config.getBoolean("config.Curse", curse);
		maxBooks = config.getInt("config.Limit.Books", maxBooks);
		maxSigns = config.getInt("config.Limit.Signs", maxSigns);
		maxHeads = config.getInt("config.Limit.Heads", maxHeads);
		useIgnorePermission = config.getBoolean("config.UseIgnorePermission", useIgnorePermission);
		useSound = config.getBoolean("config.UseHauntSound", useSound);
		HerobrineAI.HerobrineMaxHP = herobrineHP;
		HerobrineAI.getPluginCore().getAICore().stopMAIN();
		HerobrineAI.getPluginCore().getAICore().startMAIN();
		HerobrineAI.getPluginCore().getAICore().stopBD();
		HerobrineAI.getPluginCore().getAICore().tartBD();
		HerobrineAI.getPluginCore().getAICore().stopRC();
		HerobrineAI.getPluginCore().getAICore().startRC();
		HerobrineAI.availableWorld = false;
		HerobrineAI.getPluginCore().getAICore().getResetLimits().updateFromConfig();
		if (HerobrineAI.herobrineNPC != null) {
			HerobrineAI.herobrineNPC.setItemInHand(itemInHand.getItemStack());
		}
	}

	public void addAllWorlds() {
		final ArrayList<String> allWorlds = new ArrayList<String>();
		final List<World> worlds_ = Bukkit.getWorlds();
		for (int i = 0; i <= (worlds_.size() - 1); ++i) {
			if (!worlds_.get(i).getName().equalsIgnoreCase("world_herobrineai_graveyard")) {
				allWorlds.add(worlds_.get(i).getName());
			}
		}
		try {
			config.load(configF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (InvalidConfigurationException e3) {
			e3.printStackTrace();
		}
		config.set("config.Worlds", allWorlds);
		try {
			config.save(configF);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		reload();
	}

}