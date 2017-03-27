package ch.pentago.network;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
/**
 * send and receive XML packets (jdom) to/from a network stream
 * protocol is human readable
 * @author kungfoo
 *
 */
public class XMLStream {
	private OutputStream outputstream;
	private BufferedReader inputreader;
	private InputStream inputstream;
	private int inpacketcount = 0;
	private int outpacketcount = 0;
	private Socket socket;
	
	// stuff used to get and send packets
	private Document packet;
	private XMLOutputter outputter;
	private SAXBuilder docbuilder;
	
	// locks for mutual exclusion
	Lock readLock = new ReentrantLock();
	Lock writeLock = new ReentrantLock();
	
	/**
	 * sets up a new stream to send packets with
	 * @param socket socket to communicate with
	 * @throws NetworkException
	 */
	public XMLStream(Socket socket) throws NetworkException{
		try{
			this.socket = socket;
			outputstream = socket.getOutputStream();
			inputstream = socket.getInputStream();
			inputreader = new BufferedReader(new InputStreamReader(inputstream,"UTF-8"));
			
			outputter = new XMLOutputter(Format.getPrettyFormat());
			docbuilder = new SAXBuilder();
		}
		catch(IOException e){
			throw new NetworkException("socket broken");
		}
	}
	
	/**
	 * receive a packet from the network stream
	 * blocks until a packet is available
	 * @return A network packet as a XML Document (jdom)
	 * @throws NetworkException
	 */
	public Document getPacket() throws NetworkException{
		String xml = "";
		String read;
		// try to read up to '.' on separate line
		try{
			readLock.lock();
			boolean reading = true;
			while(reading){
				try{
					read = inputreader.readLine();
					if(read == null){
						reading = false;
					}
					if(read != null && read.equals("")){
						// ignore empty line feed
					}
					else{
						if(read.charAt(0) != '.'){
								xml += read;
						}
						else{
							reading = false;
						}
					}
					
				}
				catch(IOException e){
					// socket broke, quit reading and signal user as offline
					reading = false;
				}
			}
			InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			packet = docbuilder.build(in);
		}
		catch(JDOMException e){
			// malformed document or no xml document at all, close socket
			return null;
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		catch (IOException e) {
			throw new NetworkException("Document could not be built....");
		}
		finally{
			readLock.unlock();
		}
		inpacketcount++;
		return packet;
	}
	
	/**
	 * send a packet over this network xml stream
	 * @param packet the packet to send
	 * @throws NetworkException
	 */
	public void sendPacket(Document packet) throws NetworkException{
		try{
			writeLock.lock();
			String message = outputter.outputString(packet) + ".\n";
			outputstream.write(message.getBytes("UTF-8"));
			outputstream.flush();
			outpacketcount++;
		}
		catch(IOException e){
			throw new NetworkException("could not send packet, stream broken?");
		}
		finally{
			writeLock.unlock();
		}
	}
	
	public Socket getSocket(){
		return socket;
	}
}
