package ch.pentago.cram;

import java.util.Random;

public final class Cram {
	public static String getChallenge(){
		String result;
		Random rand = new Random(System.currentTimeMillis());
		result = ""+ rand.nextDouble();
		result = SHAGenerator.getSHA(result);
		return result;
	}
	
	public static String getSignature(String challenge, String password){
		String temp = SHAGenerator.getSHA(password);
		byte[] chbytes = challenge.getBytes();
		byte[] passbytes = temp.getBytes();
		byte[] response = new byte[passbytes.length];
		for(int i = 0; i < passbytes.length; i++){
			response[i] = (byte)(chbytes[i]&passbytes[i]);
		}
		return SHAGenerator.getSHA(new String(response));
	}
	
	public static String getExpected(String challenge, String encryptedPassword){
		byte[] chbytes = challenge.getBytes();
		byte[] passbytes = encryptedPassword.getBytes();
		byte[] response = new byte[passbytes.length];
		for(int i = 0; i < passbytes.length; i++){
			response[i] = (byte)(chbytes[i]&passbytes[i]);
		}
		return SHAGenerator.getSHA(new String(response));
	}
}
