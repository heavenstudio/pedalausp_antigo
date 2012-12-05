/**
 * 
 */
package br.com.muggler.contactless;

import br.com.muggler.contactless.javafx.FXSmartcardService;
import br.com.muggler.contactless.service.ReaderService;
import br.com.muggler.contactless.service.SimpleReaderService;
import br.com.muggler.contactless.service.event.CardEventSource;
import br.com.muggler.contactless.service.handlers.USPCardHandler;
import br.com.muggler.contactless.smartcard.SmartcardPublisherClient;

/**
 * @author muggler
 *
 */
public class ServiceAssemblyHelper {
	
	private static final ReaderService smartcardReaderService = new SimpleReaderService();
	private static final CardEventSource smartcardEventSource = (CardEventSource) smartcardReaderService;
	
	private static final FXSmartcardService fxService = new FXSmartcardService(smartcardReaderService);

	private static final USPCardHandler handler = new USPCardHandler();
	
	static {
		smartcardEventSource.addEventHandler(handler);
	}
	
	public static FXSmartcardService getFxSmartCardService(){
		return fxService;
	}
	
	public static final void injectPublisher(SmartcardPublisherClient client){
		handler.addSubscriber(client);
	}
	
	public static final void clearSubscribers(){
		handler.clearSubscribers();
	}
	

}
