package org.jakub1221.herobrineai.nms.NPC.network;

import net.minecraft.server.v1_8_R2.EnumProtocolDirection;
import net.minecraft.server.v1_8_R2.NetworkManager;

public class NetworkCore extends NetworkManager {

	public NetworkCore() {
		super(EnumProtocolDirection.SERVERBOUND);
	}

	@Override
	public void a() {
	}

}