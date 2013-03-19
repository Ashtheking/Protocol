package org.ashpro.net.packet;

import java.util.HashMap;
import java.util.Map;

public enum PacketId {
	
	HANDSHAKE(0, HandshakePacket.class),
	MESSAGE(1, MessagePacket.class),
	EXIT(2, ExitPacket.class);
	
	private final Class<? extends Packet> packet;
	private final int id;
	private static final Map<Integer, Class<? extends Packet>> lookupId =
			new HashMap<Integer, Class<? extends Packet>>();
	
	private PacketId(int id, final Class<? extends Packet> packet) {
		this.packet = packet;
		this.id = id;
	}
	public static Packet getFromId(int lookup) throws InstantiationException, IllegalAccessException {
		return lookupId.get(lookup).newInstance();
	}
	
	
	public int getId() {
		return id;
	}
	
	static {
		for(PacketId pi : values())
			lookupId.put(pi.id, pi.packet);
	}
}
