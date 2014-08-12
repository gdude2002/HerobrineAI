package org.jakub1221.herobrineai.nms.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.server.v1_7_R4.EntityTypes;

import org.jakub1221.herobrineai.HerobrineAI;

public class EntityInjector {

	@SuppressWarnings("unchecked")
	public static void inject() {
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
		} catch (Throwable t) {
			t.printStackTrace();
			HerobrineAI.isNPCDisabled = true;
			HerobrineAI.log.warning("[HerobrineAI] Custom NPCs have been disabled. (Incompatibility error!)");
		}
	}

}
