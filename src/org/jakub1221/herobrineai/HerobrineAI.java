package org.jakub1221.herobrineai;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.server.v1_7_R4.EntityTypes;

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
import org.jakub1221.herobrineai.NPC.NPCCore;
import org.jakub1221.herobrineai.NPC.AI.Path;
import org.jakub1221.herobrineai.NPC.AI.PathManager;
import org.jakub1221.herobrineai.NPC.Entity.HumanNPC;
import org.jakub1221.herobrineai.commands.CmdExecutor;
import org.jakub1221.herobrineai.entity.CustomSkeleton;
import org.jakub1221.herobrineai.entity.CustomZombie;
import org.jakub1221.herobrineai.entity.EntityManager;
import org.jakub1221.herobrineai.listeners.BlockListener;
import org.jakub1221.herobrineai.listeners.EntityListener;
import org.jakub1221.herobrineai.listeners.InventoryListener;
import org.jakub1221.herobrineai.listeners.PlayerListener;
import org.jakub1221.herobrineai.listeners.WorldListener;

public class HerobrineAI extends JavaPlugin implements Listener {
	private static HerobrineAI pluginCore;
	private AICore aicore;
	private ConfigDB configdb;
	private Support support;
	private EntityManager entMng;
	private PathManager pathMng;
	public static boolean isNPCDisabled;
	private static int pathUpdateINT;
	public static String bukkit_ver_string;
	public static int HerobrineHP;
	public static int HerobrineMaxHP;
	public static final boolean isDebugging = false;
	public static boolean isInitDone;
	public static NPCCore NPCman;
	public static HumanNPC HerobrineNPC;
	public static long HerobrineEntityID;
	public static boolean AvailableWorld;
	public Map<Player, Long> PlayerApple;
	public static Logger log;
	public Location hbSpawnData;
	public boolean removeHBNextTick;

	static {
		HerobrineAI.isNPCDisabled = false;
		HerobrineAI.pathUpdateINT = 0;
		HerobrineAI.bukkit_ver_string = "1.7.10";
		HerobrineAI.HerobrineHP = 200;
		HerobrineAI.HerobrineMaxHP = 200;
		HerobrineAI.isInitDone = false;
		HerobrineAI.AvailableWorld = false;
		HerobrineAI.log = Bukkit.getLogger();
	}

	public HerobrineAI() {
		super();
		PlayerApple = new HashMap<Player, Long>();
		hbSpawnData = null;
		removeHBNextTick = false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onEnable() {
		boolean errorCheck = true;
		try {
			Class.forName(net.minecraft.server.v1_7_R4.Entity.class.getName());
		} catch (ClassNotFoundException e6) {
			errorCheck = false;
			HerobrineAI.isInitDone = false;
		}
		if (errorCheck) {
			HerobrineAI.isInitDone = true;
			HerobrineAI.pluginCore = this;
			HerobrineAI.NPCman = new NPCCore(this);
			configdb = new ConfigDB(HerobrineAI.log);
			getServer().getPluginManager().registerEvents(new EntityListener(), this);
			getServer().getPluginManager().registerEvents(new BlockListener(), this);
			getServer().getPluginManager().registerEvents(new InventoryListener(), this);
			getServer().getPluginManager().registerEvents(new PlayerListener(), this);
			getServer().getPluginManager().registerEvents(new WorldListener(), this);
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
					if (HerobrineAI.getPluginCore().removeHBNextTick) {
						HerobrineAI.getPluginCore().HerobrineRemove();
						HerobrineAI.getPluginCore().HerobrineSpawn(HerobrineAI.getPluginCore().hbSpawnData);
						HerobrineAI.getPluginCore().removeHBNextTick = false;
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
					if (new Random().nextBoolean() && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow().equals(Core.CoreType.RANDOM_POSITION)) {
						HerobrineAI.getPluginCore().getPathManager().setPath(new Path(new Random().nextInt(15) - 7.0f, new Random().nextInt(15) - 7.0f));
					}
				}
			}, 150L, 150L);
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow().equals(Core.CoreType.RANDOM_POSITION)) {
						HerobrineAI.getPluginCore().getPathManager().update();
					}
				}
			}, 5L, 5L);
			getCommand("hb-ai").setExecutor(new CmdExecutor(this));
			support = new Support();
			try {
				Map<String, Class<?>> nameMap = null;
				Map<Integer, Class<?>> idMap = null;
				try {
					final Field field1 = EntityTypes.class.getDeclaredField("e");
					field1.setAccessible(true);
					idMap = (Map<Integer, Class<?>>) field1.get(idMap);
					final Field field2 = EntityTypes.class.getDeclaredField("c");
					field2.setAccessible(true);
					nameMap = (Map<String, Class<?>>) field2.get(nameMap);
					nameMap.remove("Zombie");
					nameMap.remove("Skeleton");
					idMap.remove(54);
					idMap.remove(51);
					field1.set(null, idMap);
					field2.set(null, nameMap);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
				final Method a = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
				a.setAccessible(true);
				a.invoke(null, CustomZombie.class, "Zombie", 54);
				a.invoke(null, CustomSkeleton.class, "Skeleton", 51);
			} catch (NoSuchMethodException e7) {
				HerobrineAI.isNPCDisabled = true;
			} catch (SecurityException e8) {
				HerobrineAI.isNPCDisabled = true;
			} catch (IllegalAccessException e2) {
				e2.printStackTrace();
			} catch (IllegalArgumentException e3) {
				e3.printStackTrace();
			} catch (InvocationTargetException e4) {
				e4.printStackTrace();
			}
			if (!HerobrineAI.isNPCDisabled) {
				try {
					Map<String, Class<?>> nameMap = null;
					Map<Integer, Class<?>> idMap = null;
					try {
						final Field field1 = EntityTypes.class.getDeclaredField("e");
						field1.setAccessible(true);
						idMap = (Map<Integer, Class<?>>) field1.get(idMap);
						final Field field2 = EntityTypes.class.getDeclaredField("c");
						field2.setAccessible(true);
						nameMap = (Map<String, Class<?>>) field2.get(nameMap);
						nameMap.remove("Zombie");
						nameMap.remove("Skeleton");
						idMap.remove(54);
						idMap.remove(51);
						field1.set(null, idMap);
						field2.set(null, nameMap);
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}
					final Method a = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
					a.setAccessible(true);
					a.invoke(null, CustomZombie.class, "Zombie", 54);
					a.invoke(null, CustomSkeleton.class, "Skeleton", 51);
				} catch (Exception e5) {
					e5.printStackTrace();
					setEnabled(false);
				}
			} else {
				HerobrineAI.log.warning("[HerobrineAI] Custom NPCs have been disabled. (Incompatibility error!)");
			}
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