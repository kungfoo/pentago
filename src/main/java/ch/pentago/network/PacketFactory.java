package ch.pentago.network;

import org.jdom.Document;

public class PacketFactory{
	// different types of packets this factory builds
	
	public final static int REQUEST = 0;
	public final static int REPLY = 1;
	public final static int CHAT = 2;
	public final static int GAME = 3;
	public final static int STATUS = 4;
	public final static int ACK = 5;
	public final static int NAK = 6;
	
	
	private PacketFactory(){}
	
	public static Document createPacket(int type) throws NetworkException{
		switch(type){ 
		case REQUEST:
			return RequestPacket.getDocument();
		case REPLY:
			return ReplyPacket.getDocument();
		case CHAT:
			return ChatPacket.getDocument();
		case GAME:
			return GamePacket.getDocument();
		case STATUS:
			return StatusPacket.getDocument();
		case ACK:
			return AckPacket.getDocument();
		case NAK:
			return NakPacket.getDocument();
		default:
			throw new NetworkException("unknown packet type");
		}
	}
}
