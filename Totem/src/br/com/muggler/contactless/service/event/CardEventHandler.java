/**
 * 
 */

package br.com.muggler.contactless.service.event;


/**
 * Interface for clientes interested in listening for card events.
 * 
 * @author muggler
 */
public interface CardEventHandler {

    /**
     * Callback for receiving a {@link CardEvent}
     * 
     * @param e
     * @since
     */
    public void notify(CardEvent e);

}
