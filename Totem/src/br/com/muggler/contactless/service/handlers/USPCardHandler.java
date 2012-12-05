/**
 * 
 */

package br.com.muggler.contactless.service.handlers;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.smartcardio.CardException;
import br.com.muggler.contactless.service.event.CardEvent;
import br.com.muggler.contactless.service.event.CardEventHandler;
import br.com.muggler.contactless.service.event.CardEvent.EventType;
import br.com.muggler.contactless.smartcard.SmartcardPublisher;
import br.com.muggler.contactless.smartcard.SmartcardPublisherClient;
import br.com.muggler.contactless.smartcard.USPSmartCard;


/**
 * @author muggler
 *
 */
@SuppressWarnings("unused")
public class USPCardHandler implements CardEventHandler, SmartcardPublisher {

    private static final Logger LOGGER = Logger.getLogger(USPCardHandler.class.getCanonicalName());

    private final List<SmartcardPublisherClient> clients = new ArrayList<SmartcardPublisherClient>();

    /**
     * Construtor padrão.
     * 
     * @since
     */
    public USPCardHandler() {
        // nada
    }

    /**
     * Contrói um {@link USPCardHandler} adicionando os clientes da lista de argumentos.
     * 
     * @param clients
     * @since
     */
    public USPCardHandler(SmartcardPublisherClient... clients) {
        for (SmartcardPublisherClient client : clients) {
            this.clients.add(client);
        }
    }

    public void notify(CardEvent e) {
        if (e.getEventType().equals(EventType.CARD_AVAILABLE)) {
            USPSmartCard uspCard = new USPSmartCard(e.getCard());
            try {
                if (uspCard.readNusp()) {
                    String nUsp = uspCard.getnUsp();
                    for (SmartcardPublisherClient cl : this.clients) {
						if(nUsp.length() <= 5 || nUsp.length() >= 8){
							cl.readError();
						}else{
                        cl.nUspRead(nUsp);
                    }
					}
                } else {
                    for (SmartcardPublisherClient cl : this.clients) {
                        cl.readError();
                    }
                }
            } catch (CardException e1) {
            	for (SmartcardPublisherClient cl : this.clients) {
					cl.readError();
				}
                e1.printStackTrace();
            }
        }
    }
    
    public void addSubscriber(SmartcardPublisherClient cl) {
    	this.clients.add(cl);
    }

	public void clearSubscribers() {
		this.clients.clear();
	}

	public void removeSubscriber(SmartcardPublisherClient cl) {
		this.clients.remove(cl);
	}

}
