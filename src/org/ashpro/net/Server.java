package org.ashpro.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

import org.ashpro.net.packet.Packet;
import org.ashpro.net.packet.PacketId;
import org.ashpro.net.PacketSender;

public class Server {

	public boolean listening = false;
	public ServerSocket socket = null;
	public UUID serverID = UUID.randomUUID();
	public PacketHandler packetHandler;
	public PacketSender packetSender;
	public ArrayList<ServerThread> threadList = new ArrayList<ServerThread>();
	
	public static void main(String[] args) throws IOException {
		new Server().start(4444);
	}
	
	private void start(int port) throws IOException {
        try {
            System.out.println("Starting server on port: " + port);
            socket = new ServerSocket(port);
            listening = true;
         } 
            catch (IOException e) {
               System.err.println("Could not listen on port: " + port);
               System.exit(-1);
            }
        System.out.println("Server Started sucessfully on port: " + port);
        packetHandler = new PacketHandler();
		packetSender = new PacketSender();
		new Thread(new GarbageThread()).start();
		while(listening) {
			ServerThread thread = new ServerThread(this, socket.accept());
			threadList.add(thread);
			new Thread(thread).start();
		}
        socket.close();
	}
	
	public static class GarbageThread implements Runnable
	{
		public void run() {
			while(true) {
				try {
					Thread.sleep(30000);
					System.gc();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class ServerThread implements Runnable
	{
		public Socket socket;
		private Server server;
		public boolean kill = false;
		public ServerThread(Server server, Socket socket){
			this.server = server;
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				final DataInputStream inStream = new DataInputStream(socket.getInputStream());
				while(!kill){
					int packetId = inStream.readInt();
					String clientName = Packet.readString(inStream);
					Packet packet = PacketId.getFromId(packetId);
					packet.readData(inStream);
					packet.registerPacket(clientName, socket);
					server.packetHandler.handlePacket(packet, this, server);
				}
				inStream.close();
			} catch (IOException e) {
				System.out.println("Exception thrown by client \""
						+ socket.getInetAddress().toString() + "\": " + e);
			}
			catch(Exception e) {
				System.out.println("Bad Packet Id recieved from client \""
						+ socket.getInetAddress().toString() + "\": " + e);
			}
		}
	}
}
