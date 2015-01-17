package org.jakub1221.herobrineai.nms.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.server.v1_8_R1.EntityTypes;

import org.jakub1221.herobrineai.HerobrineAI;

public class EntityInjector {

	@SuppressWarnings("unchecked")
	public static void inject() {
		try {
			final Field idMapField = EntityTypes.class.getDeclaredField("e");
			idMapField.setAccessible(true);
			Map<Integer, Class<?>> idMap = (Map<Integer, Class<?>>) idMapField.get(null);
			final Field nameMapField = EntityTypes.class.getDeclaredField("c");
			nameMapField.setAccessible(true);
			Map<String, Class<?>> nameMap = (Map<String, Class<?>>) nameMapField.get(null);
			nameMap.remove("Zombie");
			nameMap.remove("Skeleton");
			idMap.remove(54);
			idMap.remove(51);
			final Method a = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
			a.setAccessible(true);
			a.invoke(null, CustomZombie.class, "Zombie", 54);
			a.invoke(null, CustomSkeleton.class, "Skeleton", 51);
		} catch (Throwable t) {
			t.printStackTrace();
			HerobrineAI.isNPCDisabled = true;
			HerobrineAI.log.warning("[HerobrineAI] Custom NPCs have been disabled. (Incompatibility error!)");
		}
	}

}
