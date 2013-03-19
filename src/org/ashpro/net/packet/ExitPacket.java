package org.ashpro.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.ashpro.net.packet.Packet;

public class ExitPacket extends Packet {

	public ExitPacket() {
	}
	
	public ExitPacket(String bye) {
		setData(bye);
	}

	@Override
	public void writeData(DataOutputStream out) throws IOException {
		writeString((String)getData(), out);
	}

	@Override
	public void readData(DataInputStream in) throws IOException {
		setData(readString(in));
	}

}
