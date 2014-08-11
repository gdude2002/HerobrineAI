package org.jakub1221.herobrineai.AI;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
import org.jakub1221.herobrineai.AI.cores.RandomPosition;
import org.jakub1221.herobrineai.AI.cores.RandomSound;
import org.jakub1221.herobrineai.AI.cores.Signs;
import org.jakub1221.herobrineai.AI.cores.SoundF;
import org.jakub1221.herobrineai.AI.cores.Totem;
import org.jakub1221.herobrineai.entity.MobType;
import org.jakub1221.herobrineai.misc.ItemName;

public class AICore {

	public static ConsoleLogger log;
	private ArrayList<Core> AllCores;
	private Core.CoreType CoreNow;
	public static HerobrineAI plugin;
	public static Player PlayerTarget;
	public static boolean isTarget;
	public static int ticksToEnd;
	public static boolean isDiscCalled;
	public static boolean isTotemCalled;
	public static int _ticks;
	private ResetLimits resetLimits;
	private boolean BuildINT;
	private boolean MainINT;
	private boolean RandomPositionINT;
	private boolean RandomMoveINT;
	private boolean RandomSeeINT;
	private boolean CheckGravityINT;
	private boolean RandomCoreINT;
	private int RP_INT;
	private int RM_INT;
	private int RS_INT;
	private int CG_INT;
	private int MAIN_INT;
	private int BD_INT;
	private int RC_INT;

	static {
		AICore.log = new ConsoleLogger();
		AICore.isTarget = false;
		AICore.ticksToEnd = 0;
		AICore.isDiscCalled = false;
		AICore.isTotemCalled = false;
		AICore._ticks = 0;
	}

	public Core getCore(final Core.CoreType type) {
		for (final Core c : AllCores) {
			if (c.getCoreType() == type) {
				return c;
			}
		}
		return null;
	}

	public AICore() {
		super();
		AllCores = new ArrayList<Core>();
		CoreNow = Core.CoreType.ANY;
		resetLimits = null;
		BuildINT = false;
		MainINT = false;
		RandomPositionINT = false;
		RandomMoveINT = false;
		RandomSeeINT = false;
		CheckGravityINT = false;
		RandomCoreINT = false;
		RP_INT = 0;
		RM_INT = 0;
		RS_INT = 0;
		CG_INT = 0;
		MAIN_INT = 0;
		BD_INT = 0;
		RC_INT = 0;
		AllCores.add(new Attack());
		AllCores.add(new Book());
		AllCores.add(new BuildStuff());
		AllCores.add(new BuryPlayer());
		AllCores.add(new DestroyTorches());
		AllCores.add(new Graveyard());
		AllCores.add(new Haunt());
		AllCores.add(new Pyramid());
		AllCores.add(new RandomPosition());
		AllCores.add(new Signs());
		AllCores.add(new SoundF());
		AllCores.add(new Totem());
		AllCores.add(new Heads());
		AllCores.add(new RandomSound());
		AllCores.add(new RandomExplosion());
		AllCores.add(new Burn());
		AllCores.add(new Curse());
		resetLimits = new ResetLimits();
		AICore.plugin = HerobrineAI.getPluginCore();
		AICore.log.info("[HerobrineAI] Debug mode enabled!");
		FindPlayer();
		StartIntervals();
	}

	public Graveyard getGraveyard() {
		return (Graveyard) getCore(Core.CoreType.GRAVEYARD);
	}

	public RandomPosition getRandomPosition() {
		return (RandomPosition) getCore(Core.CoreType.RANDOM_POSITION);
	}

	public void setCoreTypeNow(final Core.CoreType c) {
		CoreNow = c;
	}

	public Core.CoreType getCoreTypeNow() {
		return CoreNow;
	}

	public ResetLimits getResetLimits() {
		return resetLimits;
	}

	public void disableAll() {
		resetLimits.disable();
	}

	public static String getStringWalkingMode() {
		String result = "";
		if (HerobrineAI.getPluginCore().getAICore().getCoreTypeNow() == Core.CoreType.RANDOM_POSITION) {
			result = "Yes";
		} else {
			result = "No";
		}
		return result;
	}

	public void playerBedEnter(final Player player) {
		final int chance = new Random().nextInt(100);
		if (chance < 25) {
			GraveyardTeleport(player);
		} else if (chance < 50) {
			setHauntTarget(player);
		} else if (HerobrineAI.getPluginCore().getConfigDB().UseNPC_Demon && !HerobrineAI.isNPCDisabled) {
			HerobrineAI.getPluginCore().getEntityManager().spawnCustomSkeleton(player.getLocation(), MobType.DEMON);
		}
	}

	public void FindPlayer() {
		if (!HerobrineAI.getPluginCore().getConfigDB().OnlyWalkingMode && !AICore.isTarget) {
			final int att_chance = new Random().nextInt(100);
			AICore.log.info("[HerobrineAI] Generating find chance...");
			if (((att_chance - (HerobrineAI.getPluginCore().getConfigDB().ShowRate * 4)) < 55) && (Bukkit.getServer().getOnlinePlayers().length > 0)) {
				AICore.log.info("[HerobrineAI] Finding target...");
				final Player[] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
				final int player_rolled = Util.getRandomPlayerNum();
				if (AllOnPlayers[player_rolled].getEntityId() != HerobrineAI.HerobrineEntityID) {
					if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AllOnPlayers[player_rolled].getLocation().getWorld().getName())
							&& HerobrineAI.getPluginCore().canAttackPlayerNoMSG(AllOnPlayers[player_rolled])) {
						CancelTarget(Core.CoreType.ANY);
						AICore.PlayerTarget = AllOnPlayers[player_rolled];
						AICore.isTarget = true;
						AICore.log.info("[HerobrineAI] Target founded, starting AI now! (" + AICore.PlayerTarget.getName() + ")");
						setCoreTypeNow(Core.CoreType.START);
						StartAI();
					} else {
						AICore.log.info("[HerobrineAI] Target is in the safe world! (" + AllOnPlayers[player_rolled].getLocation().getWorld().getName() + ")");
						FindPlayer();
					}
				}
			}
		}
	}

	public void CancelTarget(final Core.CoreType coreType) {
		if ((coreType == CoreNow) || (coreType == Core.CoreType.ANY)) {
			if (CoreNow == Core.CoreType.RANDOM_POSITION) {
				Stop_RM();
				Stop_RS();
				Stop_CG();
				final Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), 0.0, -20.0, 0.0);
				nowloc.setYaw(1.0f);
				nowloc.setPitch(1.0f);
				HerobrineAI.HerobrineNPC.moveTo(nowloc);
				CoreNow = Core.CoreType.ANY;
				HerobrineAI.getPluginCore().getPathManager().setPath(null);
			}
			if (AICore.isTarget) {
				if (CoreNow == Core.CoreType.ATTACK) {
					((Attack) getCore(Core.CoreType.ATTACK)).StopHandler();
				}
				if (CoreNow == Core.CoreType.HAUNT) {
					((Haunt) getCore(Core.CoreType.HAUNT)).StopHandler();
				}
				AICore._ticks = 0;
				AICore.isTarget = false;
				HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
				AICore.log.info("[HerobrineAI] Target cancelled.");
				final Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), 0.0, -20.0, 0.0);
				nowloc.setYaw(1.0f);
				nowloc.setPitch(1.0f);
				HerobrineAI.HerobrineNPC.moveTo(nowloc);
				CoreNow = Core.CoreType.ANY;
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
					@Override
					public void run() {
						AICore.this.FindPlayer();
					}
				}, (6 / HerobrineAI.getPluginCore().getConfigDB().ShowRate) * (HerobrineAI.getPluginCore().getConfigDB().ShowInterval * 1L));
			}
		}
	}

	public void StartAI() {
		if (AICore.PlayerTarget.isOnline() && AICore.isTarget) {
			if (!AICore.PlayerTarget.isDead()) {
				final Object[] data = { AICore.PlayerTarget };
				final int chance = new Random().nextInt(100);
				if (chance <= 10) {
					if (HerobrineAI.getPluginCore().getConfigDB().UseGraveyardWorld) {
						AICore.log.info("[HerobrineAI] Teleporting target to Graveyard world.");
						getCore(Core.CoreType.GRAVEYARD).RunCore(data);
					}
				} else if (chance <= 25) {
					getCore(Core.CoreType.ATTACK).RunCore(data);
				} else {
					getCore(Core.CoreType.HAUNT).RunCore(data);
				}
			} else {
				CancelTarget(Core.CoreType.START);
			}
		} else {
			CancelTarget(Core.CoreType.START);
		}
	}

	public CoreResult setAttackTarget(final Player player) {
		final Object[] data = { player };
		return getCore(Core.CoreType.ATTACK).RunCore(data);
	}

	public CoreResult setHauntTarget(final Player player) {
		final Object[] data = { player };
		return getCore(Core.CoreType.HAUNT).RunCore(data);
	}

	public void GraveyardTeleport(final Player player) {
		if (player.isOnline()) {
			CancelTarget(Core.CoreType.ANY);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
				@Override
				public void run() {
					final Object[] data = { player };
					AICore.this.getCore(Core.CoreType.GRAVEYARD).RunCore(data);
				}
			}, 10L);
		}
	}

	public void PlayerCallTotem(final Player player) {
		final String playername = player.getName();
		final Location loc = player.getLocation();
		AICore.isTotemCalled = true;
		CancelTarget(Core.CoreType.ANY);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.CancelTarget(Core.CoreType.ANY);
				final Object[] data = { loc, playername };
				AICore.this.getCore(Core.CoreType.TOTEM).RunCore(data);
			}
		}, 40L);
	}

	private void RandomPositionInterval() {
		if (CoreNow == Core.CoreType.ANY) {
			((RandomPosition) getCore(Core.CoreType.RANDOM_POSITION)).setRandomTicks(0);
			final int count = HerobrineAI.getPluginCore().getConfigDB().useWorlds.size();
			final int chance = new Random().nextInt(count);
			final Object[] data = { Bukkit.getServer().getWorld(HerobrineAI.getPluginCore().getConfigDB().useWorlds.get(chance)) };
			getCore(Core.CoreType.RANDOM_POSITION).RunCore(data);
		}
	}

	private void CheckGravityInterval() {
		if (CoreNow == Core.CoreType.RANDOM_POSITION) {
			((RandomPosition) getCore(Core.CoreType.RANDOM_POSITION)).CheckGravity();
		}
	}

	private void RandomMoveInterval() {
		((RandomPosition) getCore(Core.CoreType.RANDOM_POSITION)).RandomMove();
	}

	private void RandomSeeInterval() {
		if (CoreNow == Core.CoreType.RANDOM_POSITION) {
			((RandomPosition) getCore(Core.CoreType.RANDOM_POSITION)).CheckPlayerPosition();
		}
	}

	private void PyramidInterval() {
		if (new Random().nextBoolean() && (Bukkit.getServer().getOnlinePlayers().length > 0)) {
			AICore.log.info("[HerobrineAI] Finding pyramid target...");
			final Player[] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
			final int player_rolled = Util.getRandomPlayerNum();
			if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AllOnPlayers[player_rolled].getLocation().getWorld().getName())) {
				final int chance2 = new Random().nextInt(100);
				if (chance2 < 30) {
					if (HerobrineAI.getPluginCore().getConfigDB().BuildPyramids) {
						final Object[] data = { AllOnPlayers[player_rolled] };
						getCore(Core.CoreType.PYRAMID).RunCore(data);
					}
				} else if (chance2 < 70) {
					if (HerobrineAI.getPluginCore().getConfigDB().BuryPlayers) {
						final Object[] data = { AllOnPlayers[player_rolled] };
						getCore(Core.CoreType.BURY_PLAYER).RunCore(data);
					}
				} else if (HerobrineAI.getPluginCore().getConfigDB().UseHeads) {
					final Object[] data = { AllOnPlayers[player_rolled].getName() };
					getCore(Core.CoreType.HEADS).RunCore(data);
				}
			}
		}
	}

	private void BuildCave() {
		if (HerobrineAI.getPluginCore().getConfigDB().BuildStuff && new Random().nextBoolean() && (Bukkit.getServer().getOnlinePlayers().length > 0)) {
			final Player[] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
			final int player_rolled = Util.getRandomPlayerNum();
			if (HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AllOnPlayers[player_rolled].getLocation().getWorld().getName()) && new Random().nextBoolean()) {
				final Object[] data = { AllOnPlayers[player_rolled].getLocation() };
				getCore(Core.CoreType.BUILD_STUFF).RunCore(data);
			}
		}
	}

	public void callByDisc(final Player player) {
		AICore.isDiscCalled = false;
		if (player.isOnline()) {
			CancelTarget(Core.CoreType.ANY);
			setHauntTarget(player);
		}
	}

	public void RandomCoreINT() {
		if (new Random().nextBoolean() && (Bukkit.getServer().getOnlinePlayers().length > 0)) {
			final Player[] AllOnPlayers = Bukkit.getServer().getOnlinePlayers();
			final int player_rolled = Util.getRandomPlayerNum();
			if ((AllOnPlayers[player_rolled].getEntityId() != HerobrineAI.HerobrineEntityID)
					&& HerobrineAI.getPluginCore().getConfigDB().useWorlds.contains(AllOnPlayers[player_rolled].getLocation().getWorld().getName())) {
				final Object[] data = { AllOnPlayers[player_rolled] };
				if (HerobrineAI.getPluginCore().canAttackPlayerNoMSG(AllOnPlayers[player_rolled])) {
					if (new Random().nextInt(100) < 30) {
						getCore(Core.CoreType.RANDOM_SOUND).RunCore(data);
					} else if (new Random().nextInt(100) < 60) {
						if (HerobrineAI.getPluginCore().getConfigDB().Burn) {
							getCore(Core.CoreType.BURN).RunCore(data);
						}
					} else if (new Random().nextInt(100) < 80) {
						if (HerobrineAI.getPluginCore().getConfigDB().Curse) {
							getCore(Core.CoreType.CURSE).RunCore(data);
						}
					} else {
						getCore(Core.CoreType.RANDOM_EXPLOSION).RunCore(data);
					}
				}
			}
		}
	}

	public void DisappearEffect() {
		final Location ploc = AICore.PlayerTarget.getLocation();
		final Location hbloc1 = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		hbloc1.setY(hbloc1.getY() + 1.0);
		final Location hbloc2 = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		hbloc2.setY(hbloc2.getY() + 0.0);
		final Location hbloc3 = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
		hbloc3.setY(hbloc3.getY() + 0.5);
		final Location hbloc4 = HerobrineAI.HerobrineNPC.getBukkitEntity().getLocation();
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
		HerobrineAI.HerobrineNPC.moveTo(ploc);
	}

	private void BuildInterval() {
		if (new Random().nextInt(100) < 75) {
			PyramidInterval();
		}
		if (new Random().nextBoolean()) {
			BuildCave();
		}
	}

	private void StartIntervals() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.Start_RP();
				AICore.this.Start_MAIN();
				AICore.this.Start_BD();
				AICore.this.Start_RC();
			}
		}, 5L);
	}

	public void Start_RP() {
		RandomPositionINT = true;
		RP_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.RandomPositionInterval();
			}
		}, 300L, 300L);
	}

	public void Start_BD() {
		BuildINT = true;
		BD_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.BuildInterval();
			}
		}, 1L * HerobrineAI.getPluginCore().getConfigDB().BuildInterval, 1L * HerobrineAI.getPluginCore().getConfigDB().BuildInterval);
	}

	public void Start_MAIN() {
		MainINT = true;
		MAIN_INT = Bukkit
				.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
					@Override
					public void run() {
						AICore.this.FindPlayer();
					}
				}, (6 / HerobrineAI.getPluginCore().getConfigDB().ShowRate) * (HerobrineAI.getPluginCore().getConfigDB().ShowInterval * 1L),
						(6 / HerobrineAI.getPluginCore().getConfigDB().ShowRate) * (HerobrineAI.getPluginCore().getConfigDB().ShowInterval * 1L));
	}

	public void Start_RM() {
		RandomMoveINT = true;
		RM_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.RandomMoveInterval();
			}
		}, 200L, 200L);
	}

	public void Start_RS() {
		RandomSeeINT = true;
		RS_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.RandomSeeInterval();
			}
		}, 15L, 15L);
	}

	public void Start_RC() {
		RandomCoreINT = true;
		RC_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.RandomCoreINT();
			}
		}, HerobrineAI.getPluginCore().getConfigDB().ShowInterval / 2L, HerobrineAI.getPluginCore().getConfigDB().ShowInterval / 2L);
	}

	public void Start_CG() {
		CheckGravityINT = true;
		CG_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
			@Override
			public void run() {
				AICore.this.CheckGravityInterval();
			}
		}, 10L, 10L);
	}

	public void Stop_RP() {
		if (RandomPositionINT) {
			RandomPositionINT = false;
			Bukkit.getServer().getScheduler().cancelTask(RP_INT);
		}
	}

	public void Stop_BD() {
		if (BuildINT) {
			BuildINT = false;
			Bukkit.getServer().getScheduler().cancelTask(BD_INT);
		}
	}

	public void Stop_RS() {
		if (RandomSeeINT) {
			RandomSeeINT = false;
			Bukkit.getServer().getScheduler().cancelTask(RS_INT);
		}
	}

	public void Stop_RM() {
		if (RandomMoveINT) {
			RandomMoveINT = false;
			Bukkit.getServer().getScheduler().cancelTask(RM_INT);
		}
	}

	public void Stop_RC() {
		if (RandomCoreINT) {
			RandomCoreINT = false;
			Bukkit.getServer().getScheduler().cancelTask(RC_INT);
		}
	}

	public void Stop_CG() {
		if (CheckGravityINT) {
			CheckGravityINT = false;
			Bukkit.getServer().getScheduler().cancelTask(CG_INT);
		}
	}

	public void Stop_MAIN() {
		if (MainINT) {
			MainINT = false;
			Bukkit.getServer().getScheduler().cancelTask(MAIN_INT);
		}
	}

	public ItemStack createAncientSword() {
		ItemStack item = new ItemStack(Material.GOLD_SWORD);
		final String name = "Ancient Sword";
		final ArrayList<String> lore = new ArrayList<String>();
		lore.add("AncientSword");
		lore.add("Very old and mysterious sword.");
		lore.add("This will protect you aganist Herobrine.");
		item = ItemName.setNameAndLore(item, name, lore);
		return item;
	}

	public boolean isAncientSword(final ItemStack item) {
		final ArrayList<String> lore = new ArrayList<String>();
		lore.add("AncientSword");
		lore.add("Very old and mysterious sword.");
		lore.add("This will protect you aganist Herobrine.");
		if ((item != null) && (item.getItemMeta() != null) && (item.getItemMeta().getLore() != null)) {
			final ArrayList<String> ilore = (ArrayList<String>) item.getItemMeta().getLore();
			if (ilore.containsAll(lore)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkAncientSword(final Inventory inv) {
		final ItemStack[] itemlist = inv.getContents();
		ItemStack item = null;
		int i;
		for (i = 0, i = 0; i <= (itemlist.length - 1); ++i) {
			item = itemlist[i];
			if (isAncientSword(item)) {
				return true;
			}
		}
		return false;
	}

}