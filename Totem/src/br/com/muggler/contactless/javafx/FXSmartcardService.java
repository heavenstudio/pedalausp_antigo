/**
 * 
 */

package br.com.muggler.contactless.javafx;


import javafx.async.RunnableFuture;
import br.com.muggler.contactless.service.ReaderService;


/**
 * Wrapper class to isolate the JavaFX dependency from the existing {@link ReaderService} implementations.
 * 
 * @author muggler
 */
public class FXSmartcardService implements RunnableFuture {

    private final ReaderService service;

    /**
     * Constructs a new {@link FXSmartcardService} that wraps the given {@link ReaderService}
     * 
     * @param service
     * @since
     */
    public FXSmartcardService(ReaderService service) {
        this.service = service;
    }

    public void run() throws Exception {
        this.service.start();
    }

}
