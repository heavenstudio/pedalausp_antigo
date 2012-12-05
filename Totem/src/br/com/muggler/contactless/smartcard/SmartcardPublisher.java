/**
 * 
 */
package br.com.muggler.contactless.smartcard;

/**
 * @author muggler
 *
 */
public interface SmartcardPublisher {
	public void addSubscriber(SmartcardPublisherClient cl);
	public void removeSubscriber(SmartcardPublisherClient cl);
	public void clearSubscribers();
}
