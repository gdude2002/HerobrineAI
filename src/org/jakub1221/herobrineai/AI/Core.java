package org.jakub1221.herobrineai.AI;

import org.jakub1221.herobrineai.HerobrineAI;

public abstract class Core {

	private final AppearType Appear;
	private final CoreType coreType;
	private CoreResult nowData;

	public Core(final CoreType cp, final AppearType ap) {
		super();
		nowData = null;
		coreType = cp;
		Appear = ap;
	}

	public AppearType getAppear() {
		return Appear;
	}

	public CoreType getCoreType() {
		return coreType;
	}

	protected abstract CoreResult CallCore(final Object[] p0);

	public CoreResult RunCore(final Object[] data) {
		nowData = CallCore(data);
		if (nowData.getResult() && (Appear == AppearType.APPEAR)) {
			HerobrineAI.getPluginCore().getAICore().setCoreTypeNow(coreType);
		}
		return nowData;
	}

	public enum AppearType {
		APPEAR, NORMAL
	}

	public enum CoreType {
		ATTACK, HAUNT, BOOK, BUILD_STUFF, BURY_PLAYER, DESTROY_TORCHES, GRAVEYARD, PYRAMID,
		RANDOM_POSITION, SIGNS, SOUNDF, TOTEM, ANY, START, HEADS,
		RANDOM_SOUND, RANDOM_EXPLOSION, BURN, CURSE, STARE
	}

}