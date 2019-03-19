package org.daisy.dotify.devtools.converters;

public class CodePointHelper {
	public enum Style {
		XML,
		COMMA
	}
	public enum Mode {
		HEX,
		DECIMAL
	}
	public enum Input {
		NAME, CODE
	}

	/**
	 * Formats a number as a zero padded hex string of a specified length.
	 * @param i the number to format
	 * @param len the length of the resulting string
	 * @return returns a string of the specified length
	 */
	public static String toHexString(int i, int len) {
		return padStr(Integer.toHexString(i), len, '0');
	}
	
	private static String padStr(String in, int len, char padding) {
		StringBuilder sb = new StringBuilder();
		for (int i=in.length(); i<len; i++) {
			sb.append(padding);
		}
		sb.append(in);
		return sb.toString();
	}
	
}