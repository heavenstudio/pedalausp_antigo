/**
 * 
 */

package br.com.muggler.contactless.service;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;
import br.com.muggler.contactless.service.event.CardEvent;
import br.com.muggler.contactless.service.event.CardEventHandler;
import br.com.muggler.contactless.service.event.CardEventSource;
import br.com.muggler.contactless.service.event.CardEvent.EventType;
import br.com.muggler.contactless.service.handlers.USPCardHandler;
import br.com.muggler.contactless.smartcard.ParsedATR;
import br.com.muggler.contactless.smartcard.SmartcardPublisher;
import br.com.muggler.contactless.smartcard.SmartcardPublisherClient;
import br.com.muggler.contactless.utils.SmartcardUtils;


/**
 * @author muggler
 */
public class SimpleReaderService implements CardEventSource, ReaderService {
	private static final Logger LOGGER = Logger.getLogger(SimpleReaderService.class.getCanonicalName());
	
    private static final String TERMINAL_TYPE_PCSC = "PC/SC";
    @SuppressWarnings("unused")
    private static final String PROTOCOL_T1 = "T=1";
    private static final String PROTOCOL_ANY = "*";
    private static final long WAIT_TIMEOUT_MS = 10000;

    private final List<CardEventHandler> handlers = new ArrayList<CardEventHandler>();

    private CardTerminals terminals = null;
    private Card currentCard = null;
    private boolean interrompido = false;

    public void start() {

        TerminalFactory factory;
        try {
            factory = TerminalFactory.getInstance(TERMINAL_TYPE_PCSC, null);
        } catch (NoSuchAlgorithmException e1) {
            throw new RuntimeException("Impossível estabelecer um  contexto; o serviço PCSC (pcscd) está executando?",
                    e1);

        }
        this.terminals = factory.terminals();
        factory = null;

        try {
            this.terminals.list();
        } catch (CardException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        try {
            while (!this.interrompido) {
                this.terminals.waitForChange(WAIT_TIMEOUT_MS);
                if (this.terminals.list(CardTerminals.State.CARD_INSERTION).size() > 0) {
                	LOGGER.fine("cartao detectado.");
                    CardTerminal terminal = this.terminals.list(CardTerminals.State.CARD_INSERTION).get(0);
                    this.currentCard = terminal.connect(PROTOCOL_ANY);
                    this.notifyEvent(new CardEvent(this.currentCard, EventType.CARD_AVAILABLE));
                }
                if (this.terminals.list(CardTerminals.State.CARD_REMOVAL).size() > 0) {
                	LOGGER.fine("cartao removido.");
                    this.currentCard = null;
                    this.notifyEvent(new CardEvent(this.currentCard, EventType.CARD_REMOVED));
                }
            }
        } catch (CardException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /*
     * @see br.com.muggler.contactless.service.ReaderService#stop()
     */
    public void stop() {
        this.interrompido = true;
    }

    public void notifyEvent(final CardEvent e) {
        for (final CardEventHandler listener : this.handlers) {
            Runnable notifier = new Runnable() {

                public void run() {
                    listener.notify(e);
                }
            };
            new Thread(notifier, "CardEvent notifier thread").run();
        }
    }

    public Boolean addEventHandler(final CardEventHandler handler) {
        if (handler != null) {
            return new Boolean(this.handlers.add(handler));
        }
        return Boolean.FALSE;
    }

    public Boolean removeEventHandler(final CardEventHandler handler) {
        return new Boolean(this.handlers.remove(handler));
    }

    /**
     * Teste
     * 
     * @param args
     * @throws InterruptedException
     * @since
     */
    public static void main(String[] args) throws InterruptedException {
        final SimpleReaderService service = new SimpleReaderService();
        CardEventHandler listener = new CardEventHandler() {

            public void notify(CardEvent e) {
                if (e.getEventType().equals(EventType.CARD_AVAILABLE)) {
                    String atr = SmartcardUtils.toHexString(e.getCard().getATR().getBytes());
                    System.out.println("Card connected. " + e.getCard().getATR() + ", " + atr);
                    System.out.printf("Parsed ATR with the following results: %s\n", ParsedATR.parseATR(atr));
                } else {
                    System.out.println("Card disconnected.");
                }
            }
        };
        service.addEventHandler(listener);
        service.addEventHandler(new USPCardHandler(new SmartcardPublisherClient() {

            public void setSmartcardPublisher(SmartcardPublisher smartcardPublisher) {
                //
            }

            public void readError() {
                System.out.println("Erro de leitura!");
            }

            public void nUspRead(String nUsp) {
                System.out.println("nUsp: " + nUsp);
            }
        }));

        new Thread(new Runnable() {

            public void run() {
                service.start();

            }
        }).start();
    }

} // end of class
