package org.jakub1221.herobrineai;

import java.io.InputStream;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.extensions.GraveyardWorld;
import org.jakub1221.herobrineai.commands.CmdExecutor;
import org.jakub1221.herobrineai.listeners.BlockListener;
import org.jakub1221.herobrineai.listeners.EntityListener;
import org.jakub1221.herobrineai.listeners.InventoryListener;
import org.jakub1221.herobrineai.listeners.PlayerListener;
import org.jakub1221.herobrineai.listeners.WorldListener;
import org.jakub1221.herobrineai.nms.NPC.NPCCore;
import org.jakub1221.herobrineai.nms.NPC.AI.Path;
import org.jakub1221.herobrineai.nms.NPC.AI.PathManager;
import org.jakub1221.herobrineai.nms.NPC.entity.HumanNPC;
import org.jakub1221.herobrineai.nms.entity.EntityInjector;
import org.jakub1221.herobrineai.nms.entity.EntityManager;

public class HerobrineAI extends JavaPlugin implements Listener {

	private static HerobrineAI pluginCore;
	private AICore aicore;
	private ConfigDB configdb;
	private Support support;
	private EntityManager entMng;
	private PathManager pathMng;
	public static boolean isNPCDisabled = false;
	private static int pathUpdateINT = 0;
	public static String bukkit_ver_string = "1.7.10";
	public static int HerobrineHP = 200;
	public static int HerobrineMaxHP = 200;
	public static final boolean isDebugging = false;
	public static boolean isInitDone = false;
	public static NPCCore NPCman;
	public static HumanNPC HerobrineNPC;
	public static long HerobrineEntityID;
	public static boolean AvailableWorld = false;
	public static Logger log = Bukkit.getLogger();
	public Location hbSpawnData = null;
	public boolean removeHBNextTick = false;

	@Override
	public void onEnable() {
		boolean errorCheck = true;
		try {
			Class.forName(net.minecraft.server.v1_7_R4.Entity.class.getName());
		} catch (ClassNotFoundException e) {
			errorCheck = false;
			HerobrineAI.isInitDone = false;
		}
		if (errorCheck) {
			getCommand("hb-ai").setExecutor(new CmdExecutor(this));
			support = new Support();
			getServer().getPluginManager().registerEvents(new EntityListener(), this);
			getServer().getPluginManager().registerEvents(new BlockListener(), this);
			getServer().getPluginManager().registerEvents(new InventoryListener(), this);
			getServer().getPluginManager().registerEvents(new PlayerListener(), this);
			getServer().getPluginManager().registerEvents(new WorldListener(), this);
			HerobrineAI.isInitDone = true;
			HerobrineAI.pluginCore = this;
			HerobrineAI.NPCman = new NPCCore(this);
			configdb = new ConfigDB(HerobrineAI.log);
			pathMng = new PathManager();
			aicore = new AICore();
			entMng = new EntityManager();
			configdb.Startup();
			configdb.Reload();
			if (configdb.UseGraveyardWorld && (Bukkit.getServer().getWorld("world_herobrineai_graveyard") == null)) {
				HerobrineAI.log.info("[HerobrineAI] Creating Graveyard world...");
				final WorldCreator wc = new WorldCreator("world_herobrineai_graveyard");
				wc.generateStructures(false);
				final WorldType type = WorldType.FLAT;
				wc.type(type);
				wc.createWorld();
				GraveyardWorld.Create();
			}
			HerobrineAI.log.info("[HerobrineAI] Plugin loaded!");
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					if (HerobrineAI.this.removeHBNextTick) {
						HerobrineAI.this.HerobrineRemove();
						HerobrineAI.this.HerobrineSpawn(HerobrineAI.this.hbSpawnData);
						HerobrineAI.this.removeHBNextTick = false;
					}
				}
			}, 1L, 1L);
			final Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), 0.0, -20.0, 0.0);
			nowloc.setYaw(1.0f);
			nowloc.setPitch(1.0f);
			HerobrineSpawn(nowloc);
			HerobrineAI.HerobrineNPC.setItemInHand(configdb.ItemInHand.getItemStack());
			HerobrineAI.pathUpdateINT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					if (new Random().nextBoolean() && HerobrineAI.this.getAICore().getCoreTypeNow().equals(Core.CoreType.RANDOM_POSITION)) {
						HerobrineAI.this.getPathManager().setPath(new Path(new Random().nextInt(15) - 7.0f, new Random().nextInt(15) - 7.0f));
					}
				}
			}, 150L, 150L);
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					if (HerobrineAI.this.getAICore().getCoreTypeNow().equals(Core.CoreType.RANDOM_POSITION)) {
						HerobrineAI.this.getPathManager().update();
					}
				}
			}, 5L, 5L);
			EntityInjector.inject();
		} else {
			HerobrineAI.log.warning("[HerobrineAI] ******************ERROR******************");
			HerobrineAI.log.warning("[HerobrineAI] This version is only compatible with bukkit version " + HerobrineAI.bukkit_ver_string);
			HerobrineAI.log.warning("[HerobrineAI] *****************************************");
			setEnabled(false);
		}
	}

	@Override
	public void onDisable() {
		if (HerobrineAI.isInitDone) {
			entMng.killAllMobs();
			Bukkit.getServer().getScheduler().cancelTask(HerobrineAI.pathUpdateINT);
			HerobrineAI.NPCman.DisableTask();
			aicore.CancelTarget(Core.CoreType.ANY);
			aicore.Stop_BD();
			aicore.Stop_CG();
			aicore.Stop_MAIN();
			aicore.Stop_RC();
			aicore.Stop_RM();
			aicore.Stop_RP();
			aicore.Stop_RS();
			aicore.disableAll();
			HerobrineAI.log.info("[HerobrineAI] Plugin disabled!");
		}
	}

	public InputStream getInputStreamData(final String src) {
		return HerobrineAI.class.getResourceAsStream(src);
	}

	public AICore getAICore() {
		return aicore;
	}

	public EntityManager getEntityManager() {
		return entMng;
	}

	public static HerobrineAI getPluginCore() {
		return HerobrineAI.pluginCore;
	}

	public void HerobrineSpawn(final Location loc) {
		HerobrineAI.HerobrineNPC = HerobrineAI.NPCman.spawnHumanNPC("Herobrine", loc);
		HerobrineAI.HerobrineNPC.getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(this, true));
		HerobrineAI.HerobrineEntityID = HerobrineAI.HerobrineNPC.getBukkitEntity().getEntityId();
	}

	public void HerobrineRemove() {
		HerobrineAI.HerobrineEntityID = 0L;
		HerobrineAI.HerobrineNPC = null;
		HerobrineAI.NPCman.removeAll();
	}

	public ConfigDB getConfigDB() {
		return configdb;
	}

	public String getVersionStr() {
		return "3.4.1";
	}

	public Support getSupport() {
		return support;
	}

	public PathManager getPathManager() {
		return pathMng;
	}

	public NPCCore getNPCCore() {
		return HerobrineAI.NPCman;
	}

	public boolean canAttackPlayer(final Player player, final Player sender) {
		boolean opCheck = true;
		boolean creativeCheck = true;
		boolean ignoreCheck = true;
		if (!configdb.AttackOP && player.isOp()) {
			opCheck = false;
		}
		if (!configdb.AttackCreative && (player.getGameMode() == GameMode.CREATIVE)) {
			creativeCheck = false;
		}
		if (configdb.UseIgnorePermission && player.hasPermission("hb-ai.ignore")) {
			ignoreCheck = false;
		}
		if (opCheck && creativeCheck && ignoreCheck) {
			return true;
		}
		if (!opCheck) {
			sender.sendMessage(ChatColor.RED + "[HerobrineAI] Player is OP.");
		} else if (!creativeCheck) {
			sender.sendMessage(ChatColor.RED + "[HerobrineAI] Player is in Creative mode.");
		} else if (!ignoreCheck) {
			sender.sendMessage(ChatColor.RED + "[HerobrineAI] Player has ignore permission.");
		}
		return false;
	}

	public boolean canAttackPlayerConsole(final Player player) {
		boolean opCheck = true;
		boolean creativeCheck = true;
		boolean ignoreCheck = true;
		if (!configdb.AttackOP && player.isOp()) {
			opCheck = false;
		}
		if (!configdb.AttackCreative && (player.getGameMode() == GameMode.CREATIVE)) {
			creativeCheck = false;
		}
		if (configdb.UseIgnorePermission && player.hasPermission("hb-ai.ignore")) {
			ignoreCheck = false;
		}
		if (opCheck && creativeCheck && ignoreCheck) {
			return true;
		}
		if (!opCheck) {
			HerobrineAI.log.info("[HerobrineAI] Player is OP.");
		} else if (!creativeCheck) {
			HerobrineAI.log.info("[HerobrineAI] Player is in Creative mode.");
		} else if (!ignoreCheck) {
			HerobrineAI.log.info("[HerobrineAI] Player has ignore permission.");
		}
		return false;
	}

	public boolean canAttackPlayerNoMSG(final Player player) {
		boolean opCheck = true;
		boolean creativeCheck = true;
		boolean ignoreCheck = true;
		if (!configdb.AttackOP && player.isOp()) {
			opCheck = false;
		}
		if (!configdb.AttackCreative && (player.getGameMode() == GameMode.CREATIVE)) {
			creativeCheck = false;
		}
		if (configdb.UseIgnorePermission && player.hasPermission("hb-ai.ignore")) {
			ignoreCheck = false;
		}
		return opCheck && creativeCheck && ignoreCheck;
	}

	public String getAvailableWorldString() {
		if (HerobrineAI.AvailableWorld) {
			return "Yes";
		}
		return "No";
	}

	public static boolean isSolidBlock(final Material mat) {
		return mat.isSolid();
	}

	@SuppressWarnings("deprecation")
	public static boolean isAllowedBlock(final Material mat) {
		switch (mat.getId()) {
			case 10: {
				return false;
			}
			case 11: {
				return false;
			}
			case 8: {
				return false;
			}
			case 9: {
				return false;
			}
			default: {
				return !mat.isSolid();
			}
		}
	}

}