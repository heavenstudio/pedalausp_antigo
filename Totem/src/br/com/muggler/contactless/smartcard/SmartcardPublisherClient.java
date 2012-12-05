/**
 * 
 */
package br.com.muggler.contactless.smartcard;


/**
 * @author muggler
 *
 */
public interface SmartcardPublisherClient {
	
	public void setSmartcardPublisher(SmartcardPublisher smartcardPublisher);
	
	public void nUspRead(String nUsp);
	
	public void readError();

}
