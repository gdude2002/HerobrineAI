package org.jakub1221.herobrineai.AI;

public class CoreResult {

	private final boolean bo;
	private final String text;

	public CoreResult(final boolean b, final String t) {
		super();
		bo = b;
		text = t;
	}

	public boolean getResult() {
		return bo;
	}

	public String getResultString() {
		return text;
	}

}