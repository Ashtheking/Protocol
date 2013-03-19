package org.ashpro.net.packet;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public abstract class Packet {
	
	private String clientName;
	private Object data;
	private Socket socket;

	public abstract void writeData(DataOutputStream out) throws IOException;
	public abstract void readData(DataInputStream in) throws IOException;
		
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
    public static void writeString(String s, DataOutputStream stream) throws IOException {
    	if(s.length() > 32767)
    		throw new IOException("String too big");
    	else {
    		stream.writeShort(s.length());
    		stream.writeChars(s);
    	}
    }
       
    public static String readString(DataInputStream stream) throws IOException {
    	short word0 = stream.readShort();
    	if(word0 < 0)
    		throw new IOException("Received string length is less than zero! Weird string!");
    	String s = "";
    	for(int j = 0; j < word0; j++)
    		s += stream.readChar();          
    	return s;
    }
    
    public void registerPacket(String clientName, Socket socket) {
    	this.clientName = clientName;
    	this.socket = socket;
    }
	public String getClientName() {
		return clientName;
	}
	public Socket getSocket() {
		return socket;
	}
}
