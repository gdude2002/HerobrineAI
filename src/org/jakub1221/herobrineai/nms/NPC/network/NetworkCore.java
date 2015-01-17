package org.jakub1221.herobrineai.nms.NPC.network;

import java.io.IOException;

import net.minecraft.server.v1_8_R1.EnumProtocolDirection;
import net.minecraft.server.v1_8_R1.NetworkManager;

public class NetworkCore extends NetworkManager {

	public NetworkCore() throws IOException {
		super(EnumProtocolDirection.SERVERBOUND);
	}

	@Override
	public void a() {
	}

}