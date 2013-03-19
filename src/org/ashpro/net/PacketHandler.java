package org.ashpro.net;

import java.io.IOException;

import org.ashpro.net.Server.ServerThread;
import org.ashpro.net.packet.*;

public class PacketHandler {

	public void handlePacket(Packet packet, ServerThread thread, Server server)
			throws IOException {
		if(server != null) {
		if(packet instanceof MessagePacket)
				handleMessage((MessagePacket) packet, server);
		else if(packet instanceof HandshakePacket)
			handleHandshake((HandshakePacket) packet);
		else if(packet instanceof ExitPacket)
			handleExit((ExitPacket) packet, thread);
		}
		else {
			if(packet instanceof MessagePacket)
				handleMessage((MessagePacket) packet);
			else if(packet instanceof ExitPacket)
				handleExit((ExitPacket) packet);
		}
	}
	private void handleExit(ExitPacket packet, ServerThread thread) throws IOException {
		System.out.println("Client \"" + packet.getClientName() + "\" disconnected " +
				(packet.getData().equals("") ? "" : "with message: " + packet.getData()));
		packet.getSocket().close();
		thread.kill = true;
	}
	
	private void handleExit(ExitPacket packet) {
		System.out.println("Client \"" + packet.getClientName() + "\" disconnected " +
				(packet.getData().equals("") ? "" : "with message: " + packet.getData()));
	}
	
	private void handleHandshake(HandshakePacket packet) {
		System.out.println("Client \""+ packet.getClientName()
				+ "\" Handshake: " + packet.getData());
	}
	private void handleMessage(MessagePacket packet, Server server) {
		System.out.println(packet.getClientName() + ": " + packet.getData());
		for(ServerThread thread : server.threadList)
			server.packetSender.sendPacket(PacketId.MESSAGE.getId(), thread.socket,
					packet.getClientName(), packet.getData());
	}
	
	private void handleMessage(MessagePacket packet) {
		System.out.println(packet.getClientName() + ": " + packet.getData());
	}
}
