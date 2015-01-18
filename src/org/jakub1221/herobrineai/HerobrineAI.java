package org.jakub1221.herobrineai;

import java.io.InputStream;
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
import org.jakub1221.herobrineai.nms.NPC.entity.HumanNPC;
import org.jakub1221.herobrineai.nms.entity.EntityInjector;
import org.jakub1221.herobrineai.nms.entity.EntityManager;

public class HerobrineAI extends JavaPlugin implements Listener {

	public static final Logger log = Bukkit.getLogger();

	private static HerobrineAI pluginCore;

	public static HerobrineAI getPluginCore() {
		return HerobrineAI.pluginCore;
	}

	private AICore aicore;
	private ConfigDB configdb;
	private Support support;
	private EntityManager entMng;
	private NPCCore NPCCore;

	public static HumanNPC herobrineNPC;
	public static long herobrineEntityID;
	public static int HerobrineHP = 200;
	public static int HerobrineMaxHP = 200;
	public static boolean availableWorld = false;
	public static boolean isNPCDisabled = false;

	public Location hbSpawnData = null;
	public boolean removeHBNextTick = false;

	@Override
	public void onEnable() {
		HerobrineAI.pluginCore = this;
		configdb = new ConfigDB();
		aicore = new AICore();
		support = new Support();
		entMng = new EntityManager();
		NPCCore = new NPCCore();
		configdb.reload();
		getCommand("hb-ai").setExecutor(new CmdExecutor(this));
		getServer().getPluginManager().registerEvents(new EntityListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new WorldListener(), this);
		initHerobrine();
	}

	private void initHerobrine() {
		if (configdb.useGraveyardWorld && (Bukkit.getServer().getWorld("world_herobrineai_graveyard") == null)) {
			HerobrineAI.log.info("[HerobrineAI] Creating Graveyard world...");
			final WorldCreator wc = new WorldCreator("world_herobrineai_graveyard");
			wc.generateStructures(false);
			final WorldType type = WorldType.FLAT;
			wc.type(type);
			wc.createWorld();
			GraveyardWorld.create();
		}
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				if (HerobrineAI.this.removeHBNextTick) {
					HerobrineAI.this.removeHerobrine();
					HerobrineAI.this.spawnHerobrine(HerobrineAI.this.hbSpawnData);
					HerobrineAI.this.removeHBNextTick = false;
				}
			}
		}, 1L, 1L);
		final Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), 0.0, -20.0, 0.0);
		nowloc.setYaw(1.0f);
		nowloc.setPitch(1.0f);
		spawnHerobrine(nowloc);
		HerobrineAI.herobrineNPC.setItemInHand(configdb.itemInHand.getItemStack());
		EntityInjector.inject();
	}

	@Override
	public void onDisable() {
		entMng.killAllMobs();
		aicore.cancelTarget(Core.CoreType.ANY);
		aicore.stopBD();
		aicore.stopMAIN();
		aicore.stopRC();
		aicore.disableAll();
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

	public ConfigDB getConfigDB() {
		return configdb;
	}

	public Support getSupport() {
		return support;
	}

	public NPCCore getNPCCore() {
		return NPCCore;
	}


	public void spawnHerobrine(final Location loc) {
		HerobrineAI.herobrineNPC = NPCCore.spawnHumanNPC(loc);
		HerobrineAI.herobrineNPC.getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(this, true));
		HerobrineAI.herobrineEntityID = HerobrineAI.herobrineNPC.getBukkitEntity().getEntityId();
	}

	public void removeHerobrine() {
		HerobrineAI.herobrineEntityID = 0L;
		HerobrineAI.herobrineNPC = null;
		NPCCore.removeAll();
	}

	public boolean canAttackPlayer(final Player player, final Player sender) {
		boolean opCheck = true;
		boolean creativeCheck = true;
		boolean ignoreCheck = true;
		if (!configdb.attackOP && player.isOp()) {
			opCheck = false;
		}
		if (!configdb.attackCreative && (player.getGameMode() == GameMode.CREATIVE)) {
			creativeCheck = false;
		}
		if (configdb.useIgnorePermission && player.hasPermission("hb-ai.ignore")) {
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
		if (!configdb.attackOP && player.isOp()) {
			opCheck = false;
		}
		if (!configdb.attackCreative && (player.getGameMode() == GameMode.CREATIVE)) {
			creativeCheck = false;
		}
		if (configdb.useIgnorePermission && player.hasPermission("hb-ai.ignore")) {
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
		if (!configdb.attackOP && player.isOp()) {
			opCheck = false;
		}
		if (!configdb.attackCreative && (player.getGameMode() == GameMode.CREATIVE)) {
			creativeCheck = false;
		}
		if (configdb.useIgnorePermission && player.hasPermission("hb-ai.ignore")) {
			ignoreCheck = false;
		}
		return opCheck && creativeCheck && ignoreCheck;
	}

	public String getAvailableWorldString() {
		if (HerobrineAI.availableWorld) {
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