package br.com.lab360.pedalusp.totem.data;

import br.com.lab360.pedalusp.totem.model.Slot;
import br.com.lab360.pedalusp.totem.model.Slots;
import br.com.lab360.totem.service.Methods;
import br.com.lab360.totem.service.WebServiceClient;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author User
 */
public class SlotsData {
    /**
     * Resgata as baias de uma estação
     * @param stationId ID da estação
     * @return Lista de Slots
     */
    public Slots getSlots(int stationId) {
        XStream xstream = new XStream();

        xstream.processAnnotations(Slots.class);
        xstream.processAnnotations(Slot.class);
        xstream.omitField(Slot.class, "created-at");
        xstream.omitField(Slot.class, "updated-at");        
                
        String url = System.getProperty("host_addr") + "totem/slots_by_station";

        NameValuePair nvp[] = new NameValuePair[1];
        nvp[0] = new NameValuePair("id", String.valueOf(stationId));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        
        Slots receivedSlots = (Slots) xstream.fromXML(result);
        return receivedSlots;
    }
}
