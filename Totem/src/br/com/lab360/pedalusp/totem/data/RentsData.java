/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.data;

import br.com.lab360.pedalusp.totem.model.Rent;
import br.com.lab360.pedalusp.totem.model.Rents;
import br.com.lab360.pedalusp.totem.model.Slots;
import br.com.lab360.totem.service.WebServiceClient;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author User
 */
public class RentsData {

    public Rents getRentsByCyclistId(int cyclistId) {
        XStream xstream = new XStream();

        xstream.processAnnotations(Rents.class);
        xstream.processAnnotations(Rent.class);
        xstream.omitField(Rent.class, "created-at");
        xstream.omitField(Rent.class, "updated-at");

        String url = System.getProperty("host_addr") + "totem/rents_by_cyclist";

        NameValuePair nvp[] = new NameValuePair[1];
        nvp[0] = new NameValuePair("id", String.valueOf(cyclistId));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        Rents receivedRents;
        if(result.equals("notfound")){
            receivedRents = new Rents();
            receivedRents.rents = new ArrayList<Rent>();
        }
        else{
            receivedRents = (Rents) xstream.fromXML(result);
        }
        return receivedRents;

        //WebServiceClient instance = new WebServiceClient(System.getProperty("host_addr")+"stations/" + stationId + "/slots.xml");
        //String result = instance.callWebService(Methods.Get, "");

        /*
        Rent r1 = new Rent();
        r1.bikeId = 1234;
        r1.credit = 10;
        r1.originDate = "20/01/2010";
        r1.returnDate = "20/01/2010";
        r1.returnStationName = "Poli USP";
        r1.originStationName = "CRUSP";

        Rent r2 = new Rent();
        r2.bikeId = 1233;
        r2.credit = 20;
        r2.originDate = "21/01/2010";
        r2.returnDate = "22/01/2010";
        r2.returnStationName = "CRUSP";
        r2.originStationName = "Poli USP";

        Rent r3 = new Rent();
        r3.bikeId = 1233;
        r3.credit = 20;
        r3.originDate = "21/01/2010";
        r3.returnDate = "22/01/2010";
        r3.returnStationName = "CRUSP";
        r3.originStationName = "Poli USP";

        Rents receivedRents = new Rents();

        receivedRents.rents = new ArrayList<Rent>();

        receivedRents.rents.add(r1);
        receivedRents.rents.add(r2);

        return receivedRents;*/
    }
}
