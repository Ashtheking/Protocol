package org.ashpro.net;

import java.io.DataOutputStream;
import java.net.Socket;

import org.ashpro.net.packet.Packet;
import org.ashpro.net.packet.PacketId;


public class PacketSender {
	
	public Packet sendPacket(int id, Socket socket, String clientName, Object data) {
		try {
			if(PacketId.getFromId(id) == null)
				throw new NullPointerException();
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeInt(id);
			Packet.writeString(clientName, out);
			Packet packet = PacketId.getFromId(id);
			packet.setData(data);
			packet.registerPacket(clientName, socket);
			packet.writeData(out);
			return packet;
		}
			catch(Exception e) {
				System.out.println("Bad Packet Id: " + id + "\n Exception thrown: "+ e);
				return null;
			}
	}

}
