package org.ashpro.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.UUID;

import org.ashpro.net.packet.Packet;
import org.ashpro.net.packet.PacketId;
import org.ashpro.net.PacketSender;

public class Client {
	
	public UUID clientId = UUID.randomUUID();
	public Socket socket = null;
	public PacketSender packetSender = new PacketSender();
	public String clientName;
	public PacketHandler packetHandler = new PacketHandler();

	public static void main(String[] args) {
		new Client().start(4444);
	}

	private void start(int port) {
		try {
				System.out.println("Connecting to localhost on port " + port);
				socket = new Socket(InetAddress.getLocalHost().getHostAddress(), port);
				clientName = InetAddress.getLocalHost().getHostName();
	    } 
	    catch (UnknownHostException e) {
	    	System.err.println("Don't know about host: localhost.");
	    	System.exit(1);
	    } 
	    catch (IOException e) {
	    	System.err.println("Couldn't get I/O for the connection to: localhost.");
	    	System.exit(1);
	    }
		System.out.println("Connected to localhost on port " + port);
		packetSender.sendPacket(PacketId.HANDSHAKE.getId(), socket, clientName, clientId);
		new Thread(new ScanThread(packetSender, socket, clientName)).start();
		new Thread(new MonitorThread(socket, this)).start();
	}
	
	public static class MonitorThread implements Runnable
	{
		
		public Socket socket;
		public Client client;
		
		public MonitorThread(Socket s, Client client) {
			socket = s;
			this.client = client;
		}
		
		public void run() {
			try {
				DataInputStream inStream = new DataInputStream(socket.getInputStream());
				while(!socket.isClosed()){
					int packetId = inStream.readInt();
					String clientName = Packet.readString(inStream);
					Packet packet = PacketId.getFromId(packetId);
					packet.readData(inStream);
					packet.registerPacket(clientName, socket);
					client.packetHandler.handlePacket(packet, null, null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class ScanThread implements Runnable
	{
		public PacketSender packetSender;
		public Socket socket;
		public String clientName;
		public Scanner scan;
		
		public ScanThread(PacketSender sender, Socket s, String client) {
			packetSender = sender;
			socket = s;
			clientName = client;
			scan = new Scanner(System.in);
		}
		
		public void run() {
			while(true)
			packetSender.sendPacket(
					PacketId.MESSAGE.getId(), socket, clientName, testCommand(scan.nextLine()));
		}
		
		public String testCommand(String cmd) {
			if(cmd.equalsIgnoreCase("/quit")) {
				packetSender.sendPacket(
						PacketId.EXIT.getId(), socket, clientName, "");
				System.exit(0);
			}
			return cmd;
		}
	}
}
