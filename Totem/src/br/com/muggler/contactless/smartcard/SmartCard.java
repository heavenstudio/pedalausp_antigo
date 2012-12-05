/**
 * 
 */
package br.com.muggler.contactless.smartcard;

import javax.smartcardio.Card;

/**
 * @author muggler
 *
 */
public abstract class SmartCard {
	
	private final Card card;

	public SmartCard(Card card){
		this.card = card;
	}
	
	public Card getCard() {
		return card;
	}
	
}
