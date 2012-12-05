/**
 * 
 */

package br.com.muggler.contactless.service.event;


import javax.smartcardio.Card;


/**
 * Immutable object that encapsulates the information about a smartcard event.
 * 
 * @author muggler
 */
public class CardEvent {

    private final Card card;
    private final EventType type;

    /**
     * A new CardEvent with a specific and type of event (card insertion, removal, etc).
     * 
     * @param card
     * @param type
     * @since
     */
    public CardEvent(Card card, EventType type) {
        this.type = type;
        this.card = card;
    }

    /**
     * Indicates the nature of a smartcard event (card insertion, removal, etc).
     * 
     * @author muggler
     * @since
     */
    public enum EventType {
        /** card is available. */
        CARD_AVAILABLE,
        /** card was removed. */
        CARD_REMOVED;
    }

    /**
     * @return the {@link Card} that originated the event.
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * @return a {@link EventType} element describing the event.
     */
    public EventType getEventType() {
        return this.type;
    }

}
