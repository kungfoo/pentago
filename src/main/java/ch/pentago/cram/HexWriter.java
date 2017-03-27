package ch.pentago.cram;
/**
 * class used to write out byte array returned by the MD5Generator Class to a hex-formatted string
 * @author Silvio Heuberger
 *
 */
public class HexWriter {
	/** Character array containing all valid hex digits. */
	private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	/**
	 * dumps the given byte array in hex-string form
	 * @param data the byte[] to dump
	 * @return dumped String
	 */
	static public String dumpString(byte[] data) {
		if (data == null) {
			return "null";
		}

		StringBuffer sb = new StringBuffer();

		int dataLen = data.length;
		if (dataLen < 4) {
			dataLen = 4;
		}
		for (int offset = 0; offset < dataLen; offset += 32) {
			for (int i = 0; i < 32 && offset + i < dataLen; i++) {
				sb.append(byteToString(data[offset + i]));
			}
		}
		return sb.toString();
	}

	static private String byteToString(int n) {
		char[] buf = { hexDigits[(n >>> 4) & 0xF], hexDigits[n & 0xF], };
		return new String(buf);
	}
}