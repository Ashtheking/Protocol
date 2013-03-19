package org.ashpro.net.packet;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

public class HandshakePacket extends Packet {
	
	public HandshakePacket() {
	}
	
	public HandshakePacket(UUID clientId) {
		setData(clientId);
	}
	@Override
	public void writeData(DataOutputStream out) throws IOException {
		writeString(getData().toString(), out);
	}

	@Override
	public void readData(DataInputStream in) throws IOException {
		String str = readString(in);
		setData(UUID.fromString(str));
	}
}
