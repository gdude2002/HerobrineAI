package org.jakub1221.herobrineai.nms.NPC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.PlayerInteractManager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.nms.NPC.entity.HumanEntity;
import org.jakub1221.herobrineai.nms.NPC.entity.HumanNPC;
import org.jakub1221.herobrineai.nms.NPC.network.NetworkCore;
import org.jakub1221.herobrineai.nms.NPC.utils.NMSServerAccess;
import org.jakub1221.herobrineai.nms.NPC.utils.NMSWorldAccess;

public class NPCCore {

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

	private final ArrayList<HumanNPC> npcs = new ArrayList<HumanNPC>();
	private NMSServerAccess nmsserver = NMSServerAccess.getInstance();
	private Map<World, NMSWorldAccess> nmsworlds = new HashMap<World, NMSWorldAccess>();
	private NetworkCore networkCore = new NetworkCore();
	private int lastID = 0;

	public NPCCore() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HerobrineAI.getPluginCore(), new Runnable() {
			@Override
			public void run() {
				final ArrayList<HumanNPC> toRemove = new ArrayList<HumanNPC>();
				for (final HumanNPC humanNPC : npcs) {
					final Entity entity = humanNPC.getNMSEntity();
					if (entity.dead) {
						toRemove.add(humanNPC);
					}
				}
				for (final HumanNPC n : toRemove) {
					npcs.remove(n);
				}
			}
		}, 1L, 1L);
	}

	public void removeAll() {
		for (final HumanNPC humannpc : npcs) {
			if (humannpc != null) {
				humannpc.removeFromWorld();
			}
		}
		npcs.clear();
	}

	public NMSWorldAccess getNMSWorldAccess(final World world) {
		NMSWorldAccess bworld = nmsworlds.get(world);
		if (bworld != null) {
			return bworld;
		}
		bworld = new NMSWorldAccess(world);
		nmsworlds.put(world, bworld);
		return bworld;
	}

	public HumanNPC spawnHumanNPC(final Location l) {
		++lastID;
		final int id = lastID;
		return this.spawnHumanNPC(l, id);
	}

	public HumanNPC spawnHumanNPC(final Location l, final int id) {
		final NMSWorldAccess world = getNMSWorldAccess(l.getWorld());
		final HumanEntity humanEntity = new HumanEntity(this, world, HEROBRINE_GAME_PROFILE, new PlayerInteractManager(world.getWorldServer()));
		humanEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		world.getWorldServer().addEntity(humanEntity);
		final HumanNPC humannpc = new HumanNPC(humanEntity, id);
		npcs.add(humannpc);
		return humannpc;
	}

	public HumanNPC getHumanNPC(final int id) {
		for (final HumanNPC n : npcs) {
			if (n.getID() == id) {
				return n;
			}
		}
		return null;
	}

	public NMSServerAccess getServer() {
		return nmsserver;
	}

	public NetworkCore getNetworkCore() {
		return networkCore;
	}

}