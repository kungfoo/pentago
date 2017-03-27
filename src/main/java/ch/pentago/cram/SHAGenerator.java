package ch.pentago.cram;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * used to encrypt the passwords stored in the reservation system
 * @author Silvio Heuberger
 *
 */
public class SHAGenerator {
	/**
	 * returns the md5 checksum for the given string
	 * @param input String to generate the md5-sum of
	 * @return String representation of the md5-checksum
	 */
	public static String getSHA(String input){
		String temp = "";
		try {
			// get digest for string
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] food = md.digest(input.getBytes("US-ASCII"));
			// dump sting to hex
			temp = HexWriter.dumpString(food);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp;
	}
}
