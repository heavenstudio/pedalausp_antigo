package br.com.lab360.pedalusp.totem.data;

import org.apache.commons.httpclient.NameValuePair;

import br.com.lab360.pedalusp.totem.model.Station;
import br.com.lab360.totem.service.WebServiceClient;

import com.thoughtworks.xstream.XStream;

/**
 * Busca os dados de uma estação
 * @author User
 */
public class StationData {

    public Station getStation(int stationId) {
        XStream xstream = new XStream();

        xstream.processAnnotations(Station.class);
        xstream.omitField(Station.class, "created-at");
        xstream.omitField(Station.class, "updated-at");

        String url = System.getProperty("host_addr") + "totem/station_by_id";

        NameValuePair nvp[] = new NameValuePair[1];
        nvp[0] = new NameValuePair("id", String.valueOf(stationId));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        Station receivedStation = (Station) xstream.fromXML(result);

        //Busca os slots da estação
        SlotsData slotList = new SlotsData();
        receivedStation.slotList = slotList.getSlots(stationId).slots;

        return receivedStation;
    }
    
    
}
