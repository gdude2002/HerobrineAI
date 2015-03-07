package org.jakub1221.herobrineai.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Util;
import org.jakub1221.herobrineai.AI.cores.Attack;
import org.jakub1221.herobrineai.AI.cores.Book;
import org.jakub1221.herobrineai.AI.cores.BuildStuff;
import org.jakub1221.herobrineai.AI.cores.Burn;
import org.jakub1221.herobrineai.AI.cores.BuryPlayer;
import org.jakub1221.herobrineai.AI.cores.Curse;
import org.jakub1221.herobrineai.AI.cores.DestroyTorches;
import org.jakub1221.herobrineai.AI.cores.Graveyard;
import org.jakub1221.herobrineai.AI.cores.Haunt;
import org.jakub1221.herobrineai.AI.cores.Heads;
import org.jakub1221.herobrineai.AI.cores.Pyramid;
import org.jakub1221.herobrineai.AI.cores.RandomExplosion;
import org.jakub1221.herobrineai.AI.cores.RandomSound;
import org.jakub1221.herobrineai.AI.cores.Signs;
import org.jakub1221.herobrineai.AI.cores.SoundF;
import org.jakub1221.herobrineai.AI.cores.Totem;
import org.jakub1221.herobrineai.nms.NPC.HerobrineCore;

public class AICore {

	public static final ConsoleLogger log = new ConsoleLogger();
	public static HerobrineAI plugin;
	public static Player PlayerTarget;
	public static boolean isTarget = false;
	public static int ticksToEnd = 0;
	public static boolean isDiscCalled = false;
	public static boolean isTotemCalled = false;
	public static int _ticks = 0;

	private ArrayList<Core> allCores = new ArrayList<Core>(Arrays.asList(new Core[] {
		new Attack(), new Book(), new BuildStuff(), new BuryPlayer(), new DestroyTorches(), new Graveyard(),
		new Haunt(), new Pyramid(), new Signs(), new SoundF(), new Totem(), new Heads(),
		new RandomSound(),  new RandomExplosion(), new Burn(), new Curse()
	}));
	private Core.CoreType currentCore = Core.CoreType.ANY;
	private ResetLimits resetLimits;
	private boolean BuildINT;
	private boolean MainINT;
	private boolean RandomCoreINT;
	private int MAIN_INT;
	private int BD_INT;
	private int RC_INT;

	public Core getCore(final Core.CoreType type) {
		for (final Core c : allCores) {
			if (c.getCoreType() == type) {
				return c;
			}
		}
		return null;
	}

	public AICore() {
		AICore.plugin = HerobrineAI.getPlugin();
		resetLimits = new ResetLimits();
		AICore.log.info("[HerobrineAI] Debug mode enabled!");
		findPlayer();
		startIntervals();
	}

	public Graveyard getGraveyard() {
		return (Graveyard) getCore(Core.CoreType.GRAVEYARD);
	}

	public void setCoreTypeNow(final Core.CoreType c) {
		currentCore = c;
	}

	public Core.CoreType getCoreTypeNow() {
		return currentCore;
	}

	public ResetLimits getResetLimits() {
		return resetLimits;
	}

	public void disableAll() {
		resetLimits.disable();
	}

	public void playerBedEnter(final Player player) {
		final int chance = new Random().nextInt(100);
		if (chance < 25) {
			graveyardTeleport(player);
		} else if (chance < 50) {
			setHauntTarget(player);
		}
	}

	public void findPlayer() {
		if (!AICore.isTarget) {
			final int att_chance = new Random().nextInt(100);
			if (((att_chance - (HerobrineAI.getPlugin().getConfigDB().showRate * 4)) < 55) && (Bukkit.getServer().getOnlinePlayers().size() > 0)) {
				final Player[] allOnPlayers = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
				final int playerRolled = Util.getRandomPlayerNum(allOnPlayers);
				if (allOnPlayers[playerRolled].getEntityId() != HerobrineCore.getInstance().herobrineEntityID) {
					if (HerobrineAI.getPlugin().getConfigDB().useWorlds.contains(allOnPlayers[playerRolled].getLocation().getWorld().getName()) && HerobrineCore.getInstance().canAttackPlayerNoMSG(allOnPlayers[playerRolled])) {
						cancelTarget(Core.CoreType.ANY);
						AICore.PlayerTarget = allOnPlayers[playerRolled];
						AICore.isTarget = true;
						setCoreTypeNow(Core.CoreType.START);
						startAI();
					} else {
						findPlayer();
					}
				}
			}
		}
	}

	public void cancelTarget(final Core.CoreType coreType) {
		if ((coreType == currentCore) || (coreType == Core.CoreType.ANY)) {
			if (AICore.isTarget) {
				if (currentCore == Core.CoreType.ATTACK) {
					((Attack) getCore(Core.CoreType.ATTACK)).StopHandler();
				}
				if (currentCore == Core.CoreType.HAUNT) {
					((Haunt) getCore(Core.CoreType.HAUNT)).StopHandler();
				}
				AICore._ticks = 0;
				AICore.isTarget = false;
				HerobrineCore.getInstance().HerobrineHP = HerobrineCore.getInstance().HerobrineMaxHP;
				AICore.log.info("[HerobrineAI] Target cancelled.");
				final Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), 0.0, -20.0, 0.0);
				nowloc.setYaw(1.0f);
				nowloc.setPitch(1.0f);
				HerobrineCore.getInstance().herobrineNPC.moveTo(nowloc);
				currentCore = Core.CoreType.ANY;
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
					@Override
					public void run() {
						AICore.this.findPlayer();
					}
				}, (6 / HerobrineAI.getPlugin().getConfigDB().showRate) * (HerobrineAI.getPlugin().getConfigDB().showInterval * 1L));
			}
		}
	}

	public void startAI() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget && !AICore.PlayerTarget.isDead()) {
			final Object[] data = { AICore.PlayerTarget };
			final int chance = new Random().nextInt(100);
			if (chance <= 5) {
				if (HerobrineAI.getPlugin().getConfigDB().useGraveyardWorld) {
					AICore.log.info("[HerobrineAI] Teleporting target to Graveyard world.");
					getCore(Core.CoreType.GRAVEYARD).runCore(data);
				}
			} else if (chance <= 20) {
				getCore(Core.CoreType.ATTACK).runCore(data);
			} else {
				getCore(Core.CoreType.HAUNT).runCore(data);
			}
		} else {
			cancelTarget(Core.CoreType.START);
		}
	}

	public CoreResult setAttackTarget(final Player player) {
		final Object[] data = { player };
		return getCore(Core.CoreType.ATTACK).runCore(data);
	}

	public CoreResult setHauntTarget(final Player player) {
		final Object[] data = { player };
		return getCore(Core.CoreType.HAUNT).runCore(data);
	}

	public void graveyardTeleport(final Player player) {
		if (player.isOnline()) {
			cancelTarget(Core.CoreType.ANY);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
				@Override
				public void run() {
					final Object[] data = { player };
					AICore.this.getCore(Core.CoreType.GRAVEYARD).runCore(data);
				}
			}, 10L);
		}
	}

	public void playerCallTotem(final Player player) {
		final String playername = player.getName();
		final Location loc = player.getLocation();
		AICore.isTotemCalled = true;
		cancelTarget(Core.CoreType.ANY);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.cancelTarget(Core.CoreType.ANY);
				final Object[] data = { loc, playername };
				AICore.this.getCore(Core.CoreType.TOTEM).runCore(data);
			}
		}, 40L);
	}

	private void pyramidInterval() {
		if (new Random().nextBoolean() && (Bukkit.getServer().getOnlinePlayers().size() > 0)) {
			AICore.log.info("[HerobrineAI] Finding pyramid target...");
			final Player[] allOnPlayers = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
			final int playerRolled = Util.getRandomPlayerNum(allOnPlayers);
			if (HerobrineAI.getPlugin().getConfigDB().useWorlds.contains(allOnPlayers[playerRolled].getLocation().getWorld().getName())) {
				final int chance2 = new Random().nextInt(100);
				if (chance2 < 30) {
					if (HerobrineAI.getPlugin().getConfigDB().buildPyramids) {
						final Object[] data = { allOnPlayers[playerRolled] };
						getCore(Core.CoreType.PYRAMID).runCore(data);
					}
				} else if (chance2 < 70) {
					if (HerobrineAI.getPlugin().getConfigDB().buryPlayers) {
						final Object[] data = { allOnPlayers[playerRolled] };
						getCore(Core.CoreType.BURY_PLAYER).runCore(data);
					}
				} else if (HerobrineAI.getPlugin().getConfigDB().useHeads) {
					final Object[] data = { allOnPlayers[playerRolled].getName() };
					getCore(Core.CoreType.HEADS).runCore(data);
				}
			}
		}
	}

	private void buildCave() {
		if (HerobrineAI.getPlugin().getConfigDB().buildStuff && new Random().nextBoolean() && (Bukkit.getServer().getOnlinePlayers().size() > 0)) {
			final Player[] allOnPlayers = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
			final int playerRolled = Util.getRandomPlayerNum(allOnPlayers);
			if (HerobrineAI.getPlugin().getConfigDB().useWorlds.contains(allOnPlayers[playerRolled].getLocation().getWorld().getName()) && new Random().nextBoolean()) {
				final Object[] data = { allOnPlayers[playerRolled].getLocation() };
				getCore(Core.CoreType.BUILD_STUFF).runCore(data);
			}
		}
	}

	public void callByDisc(final Player player) {
		AICore.isDiscCalled = false;
		if (player.isOnline()) {
			cancelTarget(Core.CoreType.ANY);
			setHauntTarget(player);
		}
	}

	public void randomCoreINT() {
		if (new Random().nextBoolean() && (Bukkit.getServer().getOnlinePlayers().size() > 0)) {
			final Player[] allOnPlayers = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
			final int playerRolled = Util.getRandomPlayerNum(allOnPlayers);
			if ((allOnPlayers[playerRolled].getEntityId() != HerobrineCore.getInstance().herobrineEntityID)
					&& HerobrineAI.getPlugin().getConfigDB().useWorlds.contains(allOnPlayers[playerRolled].getLocation().getWorld().getName())) {
				final Object[] data = { allOnPlayers[playerRolled] };
				if (HerobrineCore.getInstance().canAttackPlayerNoMSG(allOnPlayers[playerRolled])) {
					if (new Random().nextInt(100) < 30) {
						getCore(Core.CoreType.RANDOM_SOUND).runCore(data);
					} else if (new Random().nextInt(100) < 60) {
						if (HerobrineAI.getPlugin().getConfigDB().burn) {
							getCore(Core.CoreType.BURN).runCore(data);
						}
					} else if (new Random().nextInt(100) < 80) {
						if (HerobrineAI.getPlugin().getConfigDB().curse) {
							getCore(Core.CoreType.CURSE).runCore(data);
						}
					} else {
						getCore(Core.CoreType.RANDOM_EXPLOSION).runCore(data);
					}
				}
			}
		}
	}

	public void disappearEffect() {
		final Location ploc = AICore.PlayerTarget.getLocation();
		final Location hbloc1 = HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation();
		hbloc1.setY(hbloc1.getY() + 1.0);
		final Location hbloc2 = HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation();
		hbloc2.setY(hbloc2.getY() + 0.0);
		final Location hbloc3 = HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation();
		hbloc3.setY(hbloc3.getY() + 0.5);
		final Location hbloc4 = HerobrineCore.getInstance().herobrineNPC.getBukkitEntity().getLocation();
		hbloc4.setY(hbloc4.getY() + 1.5);
		ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc1, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc2, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc3, Effect.SMOKE, 80);
		ploc.getWorld().playEffect(hbloc4, Effect.SMOKE, 80);
		ploc.setY(-20.0);
		HerobrineCore.getInstance().herobrineNPC.moveTo(ploc);
	}

	private void buildInterval() {
		if (new Random().nextInt(100) < 75) {
			pyramidInterval();
		}
		if (new Random().nextBoolean()) {
			buildCave();
		}
	}

	private void startIntervals() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.startMAIN();
				AICore.this.tartBD();
				AICore.this.startRC();
			}
		}, 5L);
	}


	public void tartBD() {
		BuildINT = true;
		BD_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.buildInterval();
			}
		}, 1L * HerobrineAI.getPlugin().getConfigDB().buildInterval, 1L * HerobrineAI.getPlugin().getConfigDB().buildInterval);
	}

	public void startMAIN() {
		MainINT = true;
		MAIN_INT = Bukkit
				.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
					@Override
					public void run() {
						AICore.this.findPlayer();
					}
				}, (6 / HerobrineAI.getPlugin().getConfigDB().showRate) * (HerobrineAI.getPlugin().getConfigDB().showInterval * 1L),
						(6 / HerobrineAI.getPlugin().getConfigDB().showRate) * (HerobrineAI.getPlugin().getConfigDB().showInterval * 1L));
	}


	public void startRC() {
		RandomCoreINT = true;
		RC_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.randomCoreINT();
			}
		}, HerobrineAI.getPlugin().getConfigDB().showInterval / 2L, HerobrineAI.getPlugin().getConfigDB().showInterval / 2L);
	}

	public void stopBD() {
		if (BuildINT) {
			BuildINT = false;
			Bukkit.getServer().getScheduler().cancelTask(BD_INT);
		}
	}

	public void stopRC() {
		if (RandomCoreINT) {
			RandomCoreINT = false;
			Bukkit.getServer().getScheduler().cancelTask(RC_INT);
		}
	}

	public void stopMAIN() {
		if (MainINT) {
			MainINT = false;
			Bukkit.getServer().getScheduler().cancelTask(MAIN_INT);
		}
	}

}