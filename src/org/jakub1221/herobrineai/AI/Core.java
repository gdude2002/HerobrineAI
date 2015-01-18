package org.jakub1221.herobrineai.AI;

import org.jakub1221.herobrineai.HerobrineAI;

public abstract class Core {

	private final AppearType appear;
	private final CoreType coreType;

	private CoreResult nowData;

	public Core(final CoreType cp, final AppearType ap) {
		coreType = cp;
		appear = ap;
	}

	public AppearType getAppear() {
		return appear;
	}

	public CoreType getCoreType() {
		return coreType;
	}

	protected abstract CoreResult callCore(final Object[] p0);

	public CoreResult runCore(final Object[] data) {
		nowData = callCore(data);
		if (nowData.getResult() && (appear == AppearType.APPEAR)) {
			HerobrineAI.getPluginCore().getAICore().setCoreTypeNow(coreType);
		}
		return nowData;
	}

	public enum AppearType {
		APPEAR, NORMAL
	}

	public enum CoreType {
		ATTACK, HAUNT, BOOK, BUILD_STUFF, BURY_PLAYER,
		DESTROY_TORCHES, GRAVEYARD, PYRAMID,
		SIGNS, SOUNDF, TOTEM, ANY, START, HEADS,
		RANDOM_SOUND, RANDOM_EXPLOSION, BURN, CURSE
	}

}