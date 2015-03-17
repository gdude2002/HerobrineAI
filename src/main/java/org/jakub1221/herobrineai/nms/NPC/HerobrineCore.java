package org.jakub1221.herobrineai.nms.NPC;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.extensions.GraveyardWorld;
import org.jakub1221.herobrineai.nms.NPC.entity.Herobrine;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class HerobrineCore {

	private static final GameProfile HEROBRINE_GAME_PROFILE = getHerobrineGameProfile();

	private static GameProfile getHerobrineGameProfile() {
		GameProfile profile = new GameProfile(UUID.fromString("f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2"), "Herobrine");
		Property textures = new Property(
			"textures",
			"eyJ0aW1lc3RhbXAiOjE0MjE0ODczMzk3MTMsInByb2ZpbGVJZCI6ImY4NGM2YTc5MGE0ZTQ1ZTA4NzliY2Q0OWViZDRjNGUyIiwicHJvZmlsZU5hbWUiOiJIZXJvYnJpbmUiLCJpc1B1YmxpYyI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk4YjdjYTNjN2QzMTRhNjFhYmVkOGZjMThkNzk3ZmMzMGI2ZWZjODQ0NTQyNWM0ZTI1MDk5N2U1MmU2Y2IifX19",
			"Edb1R3vm2NHUGyTPaOdXNQY9p5/Ez4xButUGY3tNKIJAzjJM5nQNrq54qyFhSZFVwIP6aM4Ivqmdb2AamXNeN0KgaaU/C514N+cUZNWdW5iiycPytfh7a6EsWXV4hCC9B2FoLkbXuxs/KAbKORtwNfFhQupAsmn9yP00e2c3ZQmS18LWwFg0vzFqvp4HvzJHqY/cTqUxdlSFDrQe/4rATe6Yx6v4zbZN2sHbSL+8AwlDDuP2Xr4SS6f8nABOxjSTlWMn6bToAYiymD+KUPoO0kQJ0Uw/pVXgWHYjQeM4BYf/FAxe8Bf1cP8S7VKueULkOxqIjXAp85uqKkU7dR/s4M4yHm6fhCOCLSMv6hi5ewTaFNYyhK+NXPftFqHcOxA1LbrjOe6NyphF/2FI79n90hagxJpWwNPz3/8I5rnGbYwBZPTsTnD8PszgQTNuWSuvZwGIXPIp9zb90xuU7g7VNWjzPVoOHfRNExEs7Dn9pG8CIA/m/a8koWW3pkbP/AMMWnwgHCr/peGdvF5fN+hJwVdpbfC9sJfzGwA7AgXG/6yqhl1U7YAp/aCVM9bZ94sav+kQghvN41jqOwy4F4i/swc7R4Fx2w5HFxVY3j7FChG7iuhqjUclm79YNhTG0lBQLiZbN5FmC9QgrNHRKlzgSZrXHWoG3YXFSqfn4J+Om9w="
		);
		profile.getProperties().put(textures.getName(), textures);
		return profile;
	}

	private static final HerobrineCore instance = new HerobrineCore();

	public static HerobrineCore getInstance() {
		return instance;
	}

	private NPCCore core = new NPCCore();

	public Herobrine herobrineNPC;
	public long herobrineEntityID;
	public int HerobrineHP = 200;
	public int HerobrineMaxHP = 200;
	public boolean availableWorld = false;

	public Location hbSpawnData = null;
	public boolean removeHBNextTick = false;

	public void init() {
		if (HerobrineAI.getPlugin().getConfigDB().useGraveyardWorld && (Bukkit.getServer().getWorld("world_herobrineai_graveyard") == null)) {
			HerobrineAI.log.info("[HerobrineAI] Creating Graveyard world...");
			final WorldCreator wc = new WorldCreator("world_herobrineai_graveyard");
			wc.generateStructures(false);
			final WorldType type = WorldType.FLAT;
			wc.type(type);
			wc.createWorld();
			GraveyardWorld.create();
		}
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HerobrineAI.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if (removeHBNextTick) {
					removeHerobrine();
					spawnHerobrine(hbSpawnData);
					removeHBNextTick = false;
				}
			}
		}, 1L, 1L);
		final Location nowloc = new Location(Bukkit.getServer().getWorlds().get(0), 0.0, -20.0, 0.0);
		nowloc.setYaw(1.0f);
		nowloc.setPitch(1.0f);
		spawnHerobrine(nowloc);
		herobrineNPC.setItemInHand(HerobrineAI.getPlugin().getConfigDB().itemInHand.getItemStack());
	}

	public void spawnHerobrine(final Location loc) {
		herobrineNPC = core.spawnHumanNPC(loc, HEROBRINE_GAME_PROFILE);
		herobrineNPC.getBukkitEntity().setMetadata("NPC", new FixedMetadataValue(HerobrineAI.getPlugin(), true));
		herobrineEntityID = herobrineNPC.getBukkitEntity().getEntityId();
	}

	public void removeHerobrine() {
		Entity bat = herobrineNPC.getBukkitEntity().getPassenger();
		if (bat != null) {
			bat.leaveVehicle();
			bat.remove();
		}
		herobrineEntityID = 0L;
		herobrineNPC = null;
	}

	public boolean canAttackPlayer(final Player player, final Player sender) {
		boolean opCheck = true;
		boolean creativeCheck = true;
		boolean ignoreCheck = true;
		if (!HerobrineAI.getPlugin().getConfigDB().attackOP && player.isOp()) {
			opCheck = false;
		}
		if (!HerobrineAI.getPlugin().getConfigDB().attackCreative && (player.getGameMode() == GameMode.CREATIVE)) {
			creativeCheck = false;
		}
		if (HerobrineAI.getPlugin().getConfigDB().useIgnorePermission && player.hasPermission("hb-ai.ignore")) {
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
		if (!HerobrineAI.getPlugin().getConfigDB().attackOP && player.isOp()) {
			opCheck = false;
		}
		if (!HerobrineAI.getPlugin().getConfigDB().attackCreative && (player.getGameMode() == GameMode.CREATIVE)) {
			creativeCheck = false;
		}
		if (HerobrineAI.getPlugin().getConfigDB().useIgnorePermission && player.hasPermission("hb-ai.ignore")) {
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
		if (!HerobrineAI.getPlugin().getConfigDB().attackOP && player.isOp()) {
			opCheck = false;
		}
		if (!HerobrineAI.getPlugin().getConfigDB().attackCreative && (player.getGameMode() == GameMode.CREATIVE)) {
			creativeCheck = false;
		}
		if (HerobrineAI.getPlugin().getConfigDB().useIgnorePermission && player.hasPermission("hb-ai.ignore")) {
			ignoreCheck = false;
		}
		return opCheck && creativeCheck && ignoreCheck;
	}

	public String getAvailableWorldString() {
		if (availableWorld) {
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
