package org.ashpro.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessagePacket extends Packet {
	
	public MessagePacket() {
	}
	
	public MessagePacket(String message) {
		setData(message);
	}

	@Override
	public void writeData(DataOutputStream out) throws IOException {
		writeString((String) getData(), out);
	}

	@Override
	public void readData(DataInputStream in) throws IOException {
		setData(readString(in));
	}
}
