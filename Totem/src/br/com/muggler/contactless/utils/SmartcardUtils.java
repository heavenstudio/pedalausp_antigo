/**
 * 
 */
package br.com.muggler.contactless.utils;

/**
 * @author muggler
 *
 */
public final class SmartcardUtils {
	
	public static String toHexString(byte bytes[]) {
	    if (bytes == null) {
	        return null;
	    }

	    StringBuffer sb = new StringBuffer();
	    for (int iter = 0; iter < bytes.length; iter++) {
	        byte high = (byte) ( (bytes[iter] & 0xf0) >> 4);
	        byte low =  (byte)   (bytes[iter] & 0x0f);
	        sb.append(nibble2char(high));
	        sb.append(nibble2char(low));
	    }

	    return sb.toString();
	}

	private static char nibble2char(byte b) {
	    byte nibble = (byte) (b & 0x0f);
	    if (nibble < 10) {
	        return (char) ('0' + nibble);
	    }
	    return (char) ('A' + nibble - 10);
	}
	
	
	public static void main(String[] args){
		testConversionToHexString();
	}
	
	private static void testConversionToHexString(){
		byte[] hexa =  {-113,-128,1,-128,79,12,-96,0,0,3,6,3,0,1,0,0,0,0,106};
		assert toHexString(hexa).equals("8F8001804F0CA000000306030001000000006A");
	}

}
