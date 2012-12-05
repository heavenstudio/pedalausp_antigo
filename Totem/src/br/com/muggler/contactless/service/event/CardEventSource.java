
package br.com.muggler.contactless.service.event;


/**
 * Interface for generating smartcard events.
 * 
 * @author muggler
 */
public interface CardEventSource {

    /**
     * Adds a new handler of smartcarthat d events.
     * 
     * @param handler
     *            a {@link CardEventHandler} that wants to be notified of events in this {@link CardEventSource}
     * @return true if the listener was successfully added; false otherwise
     */
    public Boolean addEventHandler(CardEventHandler handler);

    /**
     * Removes an object from the list of event handlers.
     * 
     * @param handler
     *            a {@link CardEventHandler} that no longer wants to be notified of events in this
     *            {@link CardEventSource}
     * @return true if the listener was successfully removed; false otherwise
     */
    public Boolean removeEventHandler(CardEventHandler handler);

    /**
     * Notifies the ocurrence of a new {@link CardEvent} to all registered handlers.
     * 
     * @param e
     */
    public void notifyEvent(CardEvent e);

}
