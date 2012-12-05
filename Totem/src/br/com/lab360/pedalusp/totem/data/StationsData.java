/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.data;

import br.com.lab360.pedalusp.totem.model.Slots;
import br.com.lab360.pedalusp.totem.model.Station;
import br.com.lab360.pedalusp.totem.model.Stations;
import br.com.lab360.totem.service.WebServiceClient;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author User
 */
public class StationsData {
    public Stations getStations() {
        XStream xstream = new XStream();

        xstream.processAnnotations(Stations.class);
        xstream.processAnnotations(Station.class);
        xstream.omitField(Station.class, "created-at");
        xstream.omitField(Station.class, "updated-at");

        //WebServiceClient instance = new WebServiceClient(System.getProperty("host_addr")+"stations/" + stationId + "/slots.xml");
        //String result = instance.callWebService(Methods.Get, "");

        //Slots receivedSlots = (Slots) xstream.fromXML(result);
        String url = System.getProperty("host_addr") + "totem/get_all_stations";

        NameValuePair nvp[] = new NameValuePair[0];
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        Stations receivedStations = (Stations) xstream.fromXML(result);
        return receivedStations;
        
        /*
        Stations receivedStations = new Stations();

        Station s1 = new Station();
        s1.mapX = 250;
        s1.mapY = 100;
        s1.usage = "normal";
        s1.stationNumber = 1;
        s1.name = "Estacao FEA";
        s1.numFreeSlot = 10;
        s1.numBikes = 2;


        Station s2 = new Station();
        s2.mapX = 300;
        s2.mapY = 170;
        s2.usage = "vazia";
        s2.stationNumber = 2;
        s2.name = "Estacao CRUSP";
        s2.numFreeSlot = 12;
        s2.numBikes = 0;

        Station s3 = new Station();
        s3.mapX = 250;
        s3.mapY = 180;
        s3.name = "Estacao Poli";
        s3.usage = "lotada";
        s3.stationNumber = 3;
        s3.numFreeSlot = 0;
        s3.numBikes = 12;
        receivedStations.stations = new ArrayList<Station>();
        receivedStations.stations.add(s1);
        receivedStations.stations.add(s2);
        receivedStations.stations.add(s3);
        return receivedStations;
         * 
         */
    }
}
