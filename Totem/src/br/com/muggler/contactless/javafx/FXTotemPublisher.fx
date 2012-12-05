package br.com.muggler.contactless.javafx;

import br.com.muggler.contactless.smartcard.SmartcardPublisherClient;
import br.com.muggler.contactless.smartcard.SmartcardPublisher;

/**
 * @author muggler
 */

public class FXTotemPublisher extends SmartcardPublisherClient  {
	
	var waitingForCard = ##"FXTotemPublisher.waitingForCard";
	var error = ##"FXTotemPublisher.readError";
	
	public var text: String  = waitingForCard;
	
	public var publisher : SmartcardPublisher;
	
	override function setSmartcardPublisher(pPublisher:SmartcardPublisher){
	    publisher = pPublisher;
	}
	
	override function nUspRead(nUsp:String){
	    FX.deferAction(
	   		function(): Void {
	    		text = nUsp;
    	})
	}
	
	override function readError(){
		FX.deferAction(
	    	function(): Void {
	    		text = error;
	        })
	}

}