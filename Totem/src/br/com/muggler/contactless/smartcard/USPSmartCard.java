/**
 * 
 */
package br.com.muggler.contactless.smartcard;

import java.nio.ByteBuffer;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;

/**
 * @author muggler
 *
 */
public class USPSmartCard extends SmartCard{
	
	private String nUsp = "";

	/**
	 * @param card
	 */
	public USPSmartCard(Card card) {
		super(card);
	}
	
	public boolean readNusp() throws CardException{
		CardChannel channel = getCard().getBasicChannel();
		
		byte[] apdu_loadKey = {(byte) 0xFF,(byte) 0x82, 0x20, 0x01, 0x06, (byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF};
		if(!validateResponse(transmitCommand(channel, apdu_loadKey))){
			return false;
		}
		byte[] apdu_authenticate = {(byte) 0xFF,(byte) 0x88, 0x00, 0x01, 0x60, 0x01};
		if(!validateResponse(transmitCommand(channel, apdu_authenticate))){
			return false;
		}
		byte[] apdu_readBinary = {(byte) 0xFF,(byte) 0xB0, 0x00, 0x01, 0x10};
		byte[] block01 = transmitCommand(channel, apdu_readBinary);
		if(!validateResponse(block01)){
			return false;
		}
		
		getCard().disconnect(true);
		
		nUsp = retrieveNusp(block01);
		return true;

	}
	
	private String retrieveNusp(byte[] block01) {
		StringBuilder sb = new StringBuilder();
		byte[] nUsp = new byte[10];
		System.arraycopy(block01, 0, nUsp, 0, 10);
		for (byte b : nUsp) {
			int digit = b ^ 0x30;
			sb.append(String.format("%d", digit));
		}
		while(sb.charAt(0) == '0'){sb.deleteCharAt(0);}
		return sb.toString();
	}

	private byte[] transmitCommand(CardChannel channel, byte[] commandApdu) throws CardException {
		ByteBuffer sendBuf = ByteBuffer.wrap(commandApdu);
		ByteBuffer recBuf = ByteBuffer.wrap(new byte[258]);
		int reLe = channel.transmit(sendBuf, recBuf);
		byte[] respApdu = new byte[reLe];
		System.arraycopy(recBuf.array(), 0, respApdu, 0, reLe);
		return respApdu;
	}
	
	private boolean validateResponse(byte[] responseApdu){
		byte sw1_success = -0x70;
		byte sw2_success = 0x00;
		byte sw1 = responseApdu[responseApdu.length - 2];
		byte sw2 = responseApdu[responseApdu.length - 1];
		if(sw1 != sw1_success || sw2 != sw2_success){
			return false;
		}
		return true;
	}
	
	public String getnUsp() {
		String temp = nUsp;
		nUsp = "";
		return temp;
	}
	
} // end of class
