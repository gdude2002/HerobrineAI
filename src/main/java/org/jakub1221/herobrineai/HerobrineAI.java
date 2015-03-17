package org.jakub1221.herobrineai;

import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.commands.CmdExecutor;
import org.jakub1221.herobrineai.listeners.BlockListener;
import org.jakub1221.herobrineai.listeners.EntityListener;
import org.jakub1221.herobrineai.listeners.InventoryListener;
import org.jakub1221.herobrineai.listeners.PlayerListener;
import org.jakub1221.herobrineai.listeners.WorldListener;
import org.jakub1221.herobrineai.nms.NPC.HerobrineCore;

public class HerobrineAI extends JavaPlugin implements Listener {

	public static final Logger log = Bukkit.getLogger();

	private static HerobrineAI pluginCore;

	public static HerobrineAI getPlugin() {
		return HerobrineAI.pluginCore;
	}

	private AICore aicore;
	private ConfigDB configdb;
	private Support support;

	@Override
	public void onEnable() {
		HerobrineAI.pluginCore = this;
		configdb = new ConfigDB();
		aicore = new AICore();
		support = new Support();
		configdb.reload();
		getCommand("hb-ai").setExecutor(new CmdExecutor(this));
		getServer().getPluginManager().registerEvents(new EntityListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new WorldListener(), this);
		HerobrineCore.getInstance().init();
	}

	@Override
	public void onDisable() {
		aicore.cancelTarget(Core.CoreType.ANY);
		aicore.stopBD();
		aicore.stopMAIN();
		aicore.stopRC();
		aicore.disableAll();
		HerobrineCore.getInstance().removeHerobrine();
	}

	public InputStream getInputStreamData(final String src) {
		return HerobrineAI.class.getResourceAsStream(src);
	}

	public AICore getAICore() {
		return aicore;
	}

	public ConfigDB getConfigDB() {
		return configdb;
	}

	public Support getSupport() {
		return support;
	}

}