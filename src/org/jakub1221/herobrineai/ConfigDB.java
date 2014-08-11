package org.jakub1221.herobrineai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jakub1221.herobrineai.misc.CustomID;

public class ConfigDB {

	private Logger log;
	public YamlConfiguration config;
	public YamlConfiguration npc;
	public int ShowRate;
	public boolean HitPlayer;
	public boolean SendMessages;
	public boolean Lighting;
	public boolean DestroyTorches;
	public int DestroyTorchesRadius;
	public int ShowInterval;
	public boolean TotemExplodes;
	public boolean OnlyWalkingMode;
	public boolean BuildStuff;
	public boolean PlaceSigns;
	public boolean UseTotem;
	public boolean WriteBooks;
	public boolean Killable;
	public boolean UsePotionEffects;
	public int CaveChance;
	public int BookChance;
	public int SignChance;
	public String DeathMessage;
	public List<String> useWorlds;
	public List<String> useMessages;
	public List<String> useSignMessages;
	public List<String> useBookMessages;
	public boolean BuildPyramids;
	public boolean UseGraveyardWorld;
	public boolean BuryPlayers;
	public boolean SpawnWolves;
	public boolean SpawnBats;
	public boolean UseWalkingMode;
	public int WalkingModeXRadius;
	public int WalkingModeZRadius;
	public int WalkingModeFromXRadius;
	public int WalkingModeFromZRadius;
	public boolean AttackCreative;
	public boolean AttackOP;
	public boolean SecuredArea_Build;
	public boolean SecuredArea_Attack;
	public boolean SecuredArea_Haunt;
	public boolean SecuredArea_Signs;
	public boolean SecuredArea_Books;
	public int HerobrineHP;
	public int BuildInterval;
	public boolean UseHeads;
	public boolean UseNPC_Guardian;
	public boolean UseNPC_Warrior;
	public boolean UseNPC_Demon;
	public CustomID ItemInHand;
	public boolean Explosions;
	public boolean Burn;
	public boolean Curse;
	public int maxBooks;
	public int maxSigns;
	public int maxHeads;
	public boolean UseIgnorePermission;
	public String HerobrineName;
	public boolean UseSound;
	private boolean isStartupDone;
	public File configF;
	public File npcF;

	public ConfigDB(final Logger l) {
		super();
		ShowRate = 2;
		HitPlayer = true;
		SendMessages = true;
		Lighting = true;
		DestroyTorches = true;
		DestroyTorchesRadius = 5;
		ShowInterval = 144000;
		TotemExplodes = true;
		OnlyWalkingMode = false;
		BuildStuff = true;
		PlaceSigns = true;
		UseTotem = true;
		WriteBooks = true;
		Killable = false;
		UsePotionEffects = true;
		CaveChance = 40;
		BookChance = 5;
		SignChance = 5;
		DeathMessage = "You cannot kill me!";
		useWorlds = new ArrayList<String>();
		useMessages = new ArrayList<String>();
		useSignMessages = new ArrayList<String>();
		useBookMessages = new ArrayList<String>();
		BuildPyramids = true;
		UseGraveyardWorld = true;
		BuryPlayers = true;
		SpawnWolves = true;
		SpawnBats = true;
		UseWalkingMode = true;
		WalkingModeXRadius = 1000;
		WalkingModeZRadius = 1000;
		WalkingModeFromXRadius = 0;
		WalkingModeFromZRadius = 0;
		AttackCreative = true;
		AttackOP = true;
		SecuredArea_Build = true;
		SecuredArea_Attack = true;
		SecuredArea_Haunt = true;
		SecuredArea_Signs = true;
		SecuredArea_Books = true;
		HerobrineHP = 150;
		BuildInterval = 72000;
		UseHeads = true;
		UseNPC_Guardian = true;
		UseNPC_Warrior = true;
		UseNPC_Demon = true;
		ItemInHand = null;
		Explosions = true;
		Burn = true;
		Curse = true;
		maxBooks = 1;
		maxSigns = 1;
		maxHeads = 1;
		UseIgnorePermission = true;
		HerobrineName = "Herobrine";
		UseSound = true;
		isStartupDone = false;
		configF = new File("plugins/HerobrineAI/config.yml");
		npcF = new File("plugins/HerobrineAI/npc.yml");
		log = l;
	}

	public void Startup() {
		new File("plugins/HerobrineAI").mkdirs();
		if (!configF.exists()) {
			try {
				configF.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = new YamlConfiguration();
		if (!npcF.exists()) {
			try {
				npcF.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		npc = new YamlConfiguration();
		try {
			config.load(configF);
			npc.load(npcF);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e3) {
			e3.printStackTrace();
		}
		if (!npc.contains("npc.Guardian")) {
			npc.set("npc.Guardian.SpawnCount", 1);
			npc.set("npc.Guardian.HP", 40);
			npc.set("npc.Guardian.Speed", 0.3);
			npc.set("npc.Guardian.Drops.283.Chance", 40);
			npc.set("npc.Guardian.Drops.283.Count", 1);
			npc.set("npc.Guardian.Drops.286.Chance", 30);
			npc.set("npc.Guardian.Drops.286.Count", 1);
			npc.set("npc.Warrior.SpawnChance", 4);
			npc.set("npc.Warrior.HP", 40);
			npc.set("npc.Warrior.Speed", 0.3);
			npc.set("npc.Warrior.Drops.307.Chance", 25);
			npc.set("npc.Warrior.Drops.307.Count", 1);
			npc.set("npc.Warrior.Drops.306.Chance", 20);
			npc.set("npc.Warrior.Drops.306.Count", 1);
			npc.set("npc.Demon.SpawnChance", 4);
			npc.set("npc.Demon.HP", 40);
			npc.set("npc.Demon.Speed", 0.3);
			npc.set("npc.Demon.Drops.322.Chance", 40);
			npc.set("npc.Demon.Drops.322.Count", 1);
			npc.set("npc.Demon.Drops.397.Chance", 20);
			npc.set("npc.Demon.Drops.397.Count", 1);
			try {
				npc.save(npcF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!config.contains("config.ShowRate")) {
			useWorlds.add("world");
			useMessages.add("Even Notch can?t save you now!");
			useMessages.add("Fear me!");
			useMessages.add("Welcome to my world!");
			useMessages.add("I am your death!");
			useMessages.add("Grave awaits you!");
			useSignMessages.add("I?m watching.");
			useSignMessages.add("Death...");
			useSignMessages.add("Eyes in dark...");
			useBookMessages.add("White eyes in dark...");
			useBookMessages.add("... was last what I saw ...");
			useBookMessages.add("... before i was dead.");
			log.info("[HerobrineAI] Creating new Config ...");
			config.set("config.ShowInterval", 144000);
			config.set("config.ShowRate", 2);
			config.set("config.HitPlayer", true);
			config.set("config.SendMessages", true);
			config.set("config.Lighting", false);
			config.set("config.DestroyTorches", true);
			config.set("config.DestroyTorchesRadius", 5);
			config.set("config.Worlds", useWorlds);
			config.set("config.TotemExplodes", true);
			config.set("config.OnlyWalkingMode", false);
			config.set("config.BuildStuff", true);
			config.set("config.PlaceSigns", true);
			config.set("config.UseTotem", true);
			config.set("config.WriteBooks", true);
			config.set("config.Killable", false);
			config.set("config.UsePotionEffects", true);
			config.set("config.CaveChance", 40);
			config.set("config.BookChance", 5);
			config.set("config.SignChance", 5);
			config.set("config.DeathMessage", "You cannot kill me!");
			config.set("config.Messages", useMessages);
			config.set("config.SignMessages", useSignMessages);
			config.set("config.BookMessages", useBookMessages);
			config.set("config.Drops.264.count", 1);
			config.set("config.Drops.264.chance", 20);
			config.set("config.BuildPyramids", true);
			config.set("config.UseGraveyardWorld", true);
			config.set("config.BuryPlayers", true);
			config.set("config.SpawnWolves", true);
			config.set("config.SpawnBats", true);
			config.set("config.UseWalkingMode", true);
			config.set("config.WalkingModeRadius.X", 1000);
			config.set("config.WalkingModeRadius.Z", 1000);
			config.set("config.WalkingModeRadius.FromX", 0);
			config.set("config.WalkingModeRadius.FromZ", 0);
			config.set("config.BuildInterval", 72000);
			config.set("config.BuildTemples", true);
			config.set("config.UseArtifacts.Bow", true);
			config.set("config.UseArtifacts.Sword", true);
			config.set("config.UseArtifacts.Apple", true);
			config.set("config.HerobrineHP", 300);
			config.set("config.AttackCreative", true);
			config.set("config.AttackOP", true);
			config.set("config.SecuredArea.Build", true);
			config.set("config.SecuredArea.Attack", true);
			config.set("config.SecuredArea.Haunt", true);
			config.set("config.SecuredArea.Signs", true);
			config.set("config.SecuredArea.Books", true);
			config.set("config.UseHeads", true);
			config.set("config.UseCustomItems", false);
			config.set("config.UseAncientSword", true);
			config.set("config.UseNPC.Guardian", true);
			config.set("config.UseNPC.Warrior", true);
			config.set("config.UseNPC.Demon", true);
			config.set("config.ItemInHand", "0");
			config.set("config.Explosions", true);
			config.set("config.Burn", true);
			config.set("config.Curse", true);
			config.set("config.Limit.Books", 1);
			config.set("config.Limit.Signs", 1);
			config.set("config.Limit.Heads", 1);
			config.set("config.UseIgnorePermission", false);
			config.set("config.Name", "Herobrine");
			config.set("config.UseHauntSound", true);
			try {
				config.save(configF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			boolean hasUpdated = false;
			if (!config.contains("config.Worlds")) {
				hasUpdated = true;
				log.info("[HerobrineAI] Updating old config...");
				config.set("config.Worlds", useWorlds);
			}
			if (!config.contains("config.TotemExplodes")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.TotemExplodes", true);
			}
			if (!config.contains("config.OnlyWalkingMode")) {
				if (!hasUpdated) {
					hasUpdated = true;
					log.info("[HerobrineAI] Updating old config...");
				}
				config.set("config.OnlyWalkingMode", false);
			}
			if (!config.contains("config.BuildStuff")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				useMessages.add("Even Notch can?t save you now!");
				useMessages.add("Fear me!");
				useMessages.add("Welcome to my world!");
				useMessages.add("I am your death!");
				useMessages.add("Grave awaits you!");
				useSignMessages.add("I?m watching.");
				useSignMessages.add("Death...");
				useSignMessages.add("Eyes in dark...");
				useBookMessages.add("White eyes in dark...");
				useBookMessages.add("... was last what I saw ...");
				useBookMessages.add("... before i was dead.");
				config.set("config.BuildStuff", true);
				config.set("config.PlaceSigns", true);
				config.set("config.UseTotem", true);
				config.set("config.WriteBooks", true);
				config.set("config.Killable", false);
				config.set("config.Messages", useMessages);
				config.set("config.SignMessages", useSignMessages);
				config.set("config.BookMessages", useBookMessages);
				config.set("config.Drops.264.count", 1);
				config.set("config.Drops.264.chance", 20);
			}
			if (!config.contains("config.UsePotionEffects")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.UsePotionEffects", true);
				config.set("config.CaveChance", 40);
				config.set("config.BookChance", 5);
				config.set("config.SignChance", 5);
			}
			if (!config.contains("config.DeathMessage")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.DeathMessage", "You cannot kill me!");
			}
			if (!config.contains("config.BuildPyramids")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.BuildPyramids", true);
			}
			if (!config.contains("config.UseGraveyardWorld")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.UseGraveyardWorld", true);
				config.set("config.BuryPlayers", true);
				config.set("config.SpawnWolves", true);
				config.set("config.SpawnBats", true);
				if (config.contains("config.UsePoitonEffects")) {
					config.set("config.UsePoitonEffects", (Object) null);
				}
			}
			if (!config.contains("config.DeathMessage")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.DeathMessage", "You cannot kill me!");
			}
			if (!config.contains("config.UseWalkingMode")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.UseWalkingMode", true);
				config.set("config.WalkingModeRadius.X", 1000);
				config.set("config.WalkingModeRadius.Z", 1000);
			}
			if (!config.contains("config.BuildTemples")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.BuildInterval", 45000);
				config.set("config.BuildTemples", true);
				config.set("config.UseArtifacts.Bow", true);
				config.set("config.UseArtifacts.Sword", true);
				config.set("config.UseArtifacts.Apple", true);
				config.set("config.HerobrineHP", 200);
				config.set("config.AttackCreative", true);
				config.set("config.AttackOP", true);
				config.set("config.SecuredArea.Build", true);
				config.set("config.SecuredArea.Attack", true);
				config.set("config.SecuredArea.Haunt", true);
				config.set("config.SecuredArea.Signs", true);
				config.set("config.SecuredArea.Books", true);
			}
			if (!config.contains("config.UseHeads")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.UseHeads", true);
			}
			if (!config.contains("config.UseAncientSword")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.UseAncientSword", true);
				config.set("config.UseNPC.Guardian", true);
				config.set("config.UseNPC.Warrior", true);
				config.set("config.ItemInHand", "0");
				config.set("config.WalkingModeRadius.FromX", 0);
				config.set("config.WalkingModeRadius.FromZ", 0);
			}
			if (!config.contains("config.Explosions")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.Explosions", true);
			}
			if (!config.contains("config.UseNPC.Demon")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.UseNPC.Demon", true);
				config.set("config.Burn", true);
				config.set("config.Curse", true);
			}
			if (!npc.contains("npc.Warrior.Speed")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				npc.set("npc.Warrior.Speed", 0.3);
				npc.set("npc.Guardian.Speed", 0.3);
				npc.set("npc.Demon.Speed", 0.3);
			}
			if (!config.contains("config.Limit.Books")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.Limit.Books", 1);
				config.set("config.Limit.Signs", 1);
				config.set("config.Limit.Heads", 1);
			}
			if (!config.contains("config.UseIgnorePermission")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.UseIgnorePermission", false);
			}
			if (!config.contains("config.Name")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.Name", "Herobrine");
			}
			if (!config.contains("config.UseHauntSound")) {
				if (!hasUpdated) {
					log.info("[HerobrineAI] Updating old config...");
				}
				hasUpdated = true;
				config.set("config.UseHauntSound", true);
			}
			if (hasUpdated) {
				try {
					config.save(configF);
					npc.save(npcF);
				} catch (IOException e4) {
					e4.printStackTrace();
				}
				log.info("[HerobrineAI] Config was updated to v" + HerobrineAI.getPluginCore().getVersionStr() + "!");
			}
			Reload();
		}
	}

	public void Reload() {
		try {
			config.load(configF);
			npc.load(npcF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (InvalidConfigurationException e3) {
			e3.printStackTrace();
		}
		ShowInterval = config.getInt("config.ShowInterval");
		ShowRate = config.getInt("config.ShowRate");
		HitPlayer = config.getBoolean("config.HitPlayer");
		SendMessages = config.getBoolean("config.SendMessages");
		Lighting = config.getBoolean("config.Lighting");
		DestroyTorches = config.getBoolean("config.DestroyTorches");
		DestroyTorchesRadius = config.getInt("config.DestroyTorchesRadius");
		useWorlds = config.getStringList("config.Worlds");
		TotemExplodes = config.getBoolean("config.TotemExplodes");
		OnlyWalkingMode = config.getBoolean("config.OnlyWalkingMode");
		BuildStuff = config.getBoolean("config.BuildStuff");
		PlaceSigns = config.getBoolean("config.PlaceSigns");
		UseTotem = config.getBoolean("config.UseTotem");
		WriteBooks = config.getBoolean("config.WriteBooks");
		Killable = config.getBoolean("config.Killable");
		UsePotionEffects = config.getBoolean("config.UsePotionEffects");
		CaveChance = config.getInt("config.CaveChance");
		BookChance = config.getInt("config.BookChance");
		SignChance = config.getInt("config.SignChance");
		DeathMessage = config.getString("config.DeathMessage");
		useMessages = config.getStringList("config.Messages");
		useSignMessages = config.getStringList("config.SignMessages");
		useBookMessages = config.getStringList("config.BookMessages");
		BuildPyramids = config.getBoolean("config.BuildPyramids");
		UseGraveyardWorld = config.getBoolean("config.UseGraveyardWorld");
		BuryPlayers = config.getBoolean("config.BuryPlayers");
		SpawnWolves = config.getBoolean("config.SpawnWolves");
		SpawnBats = config.getBoolean("config.SpawnBats");
		UseWalkingMode = config.getBoolean("config.UseWalkingMode");
		WalkingModeXRadius = config.getInt("config.WalkingModeRadius.X");
		WalkingModeZRadius = config.getInt("config.WalkingModeRadius.Z");
		WalkingModeFromXRadius = config.getInt("config.WalkingModeRadius.FromX");
		WalkingModeFromZRadius = config.getInt("config.WalkingModeRadius.FromZ");
		BuildInterval = config.getInt("config.BuildInterval");
		HerobrineHP = config.getInt("config.HerobrineHP");
		AttackCreative = config.getBoolean("config.AttackCreative");
		AttackOP = config.getBoolean("config.AttackOP");
		SecuredArea_Build = config.getBoolean("config.SecuredArea.Build");
		SecuredArea_Attack = config.getBoolean("config.SecuredArea.Attack");
		SecuredArea_Haunt = config.getBoolean("config.SecuredArea.Haunt");
		SecuredArea_Signs = config.getBoolean("config.SecuredArea.Signs");
		SecuredArea_Books = config.getBoolean("config.SecuredArea.Books");
		UseHeads = config.getBoolean("config.UseHeads");
		UseNPC_Guardian = config.getBoolean("config.UseNPC.Guardian");
		UseNPC_Warrior = config.getBoolean("config.UseNPC.Warrior");
		UseNPC_Demon = config.getBoolean("config.UseNPC.Demon");
		ItemInHand = new CustomID(config.getString("config.ItemInHand"));
		Explosions = config.getBoolean("config.Explosions");
		Burn = config.getBoolean("config.Burn");
		Curse = config.getBoolean("config.Curse");
		maxBooks = config.getInt("config.Limit.Books");
		maxSigns = config.getInt("config.Limit.Signs");
		maxHeads = config.getInt("config.Limit.Heads");
		UseIgnorePermission = config.getBoolean("config.UseIgnorePermission");
		HerobrineName = config.getString("config.Name");
		UseSound = config.getBoolean("config.UseHauntSound");
		HerobrineAI.HerobrineMaxHP = HerobrineHP;
		HerobrineAI.getPluginCore().getAICore().Stop_MAIN();
		HerobrineAI.getPluginCore().getAICore().Start_MAIN();
		HerobrineAI.getPluginCore().getAICore().Stop_BD();
		HerobrineAI.getPluginCore().getAICore().Start_BD();
		HerobrineAI.getPluginCore().getAICore().Stop_RC();
		HerobrineAI.getPluginCore().getAICore().Start_RC();
		HerobrineAI.AvailableWorld = false;
		HerobrineAI.getPluginCore().getAICore().getResetLimits().updateFromConfig();
		if (HerobrineAI.HerobrineNPC != null) {
			HerobrineAI.HerobrineNPC.setItemInHand(ItemInHand.getItemStack());
		}
		if (isStartupDone) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HerobrineAI.getPluginCore(), new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i <= (useWorlds.size() - 1); ++i) {
						if (Bukkit.getServer().getWorlds().contains(Bukkit.getServer().getWorld(useWorlds.get(i)))) {
							HerobrineAI.AvailableWorld = true;
						}
					}
					if (!HerobrineAI.AvailableWorld) {
						log.warning("**********************************************************");
						log.warning("[HerobrineAI] There are no available worlds for Herobrine!");
						log.warning("**********************************************************");
					} else {
						log.info("**********************************************************");
						log.info("[HerobrineAI] No problems detected.");
						log.info("**********************************************************");
					}
				}
			}, 1L);
		}
		isStartupDone = true;
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
		Reload();
	}

}