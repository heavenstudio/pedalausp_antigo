/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lab360.pedalusp.totem.data;

import br.com.lab360.pedalusp.totem.model.Slot;
import br.com.lab360.totem.service.WebServiceClient;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class SlotData {

    static Logger logger = Logger.getLogger(SlotData.class);
    public Slot getSlotById(int slotId) {
        XStream xstream = new XStream();

        xstream.processAnnotations(Slot.class);
        xstream.omitField(Slot.class, "created-at");
        xstream.omitField(Slot.class, "updated-at");

        String url = System.getProperty("host_addr") + "totem/slots";

        NameValuePair nvp[] = new NameValuePair[1];
        nvp[0] = new NameValuePair("id", String.valueOf(slotId));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        Slot receivedSlot = (Slot) xstream.fromXML(result);
        return receivedSlot;
    }

    public Slot getSlotByPosition(int slotPosition) {
        XStream xstream = new XStream();

        xstream.processAnnotations(Slot.class);
        xstream.omitField(Slot.class, "created-at");
        xstream.omitField(Slot.class, "updated-at");

        String url = System.getProperty("host_addr") + "totem/slot_by_station_and_position";


        NameValuePair nvp[] = new NameValuePair[2];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        Slot receivedSlot = (Slot) xstream.fromXML(result);

        return receivedSlot;
    }

    public String solicitaLiberacaoServer(Slot ss) {

        String url = System.getProperty("host_addr") + "totem/request_rent";

        logger.info("SOLICITANDO LIBERACAO: Slot "+ ss.position);

        NameValuePair nvp[] = new NameValuePair[3];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(ss.position));
        nvp[2] = new NameValuePair("cyclist_id", System.getProperty("cyclist_id"));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        return result;
    }

    public String liberaSlotByPosition(int slotPosition) {
      
        String url = System.getProperty("host_addr") + "totem/rent";

        logger.info("LIBERANDO: "+ slotPosition);

        NameValuePair nvp[] = new NameValuePair[2];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        return result;
    }

    public String vazioSlotByPosition(int slotPosition) {

        String url = System.getProperty("host_addr") + "totem/set_status_by_station_and_position";

        logger.info("BAIA "+slotPosition+ ": VAZIA");

        NameValuePair nvp[] = new NameValuePair[3];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        nvp[2] = new NameValuePair("status", "Vazia");
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        return result;
    }
    
    public String devolvendoSlotByPosition(int slotPosition, String bikeRFID) {

        String url = System.getProperty("host_addr") + "totem/return_bike";

        logger.info("DEVOLVENDO RFID "+bikeRFID +", BAIA "+ slotPosition);

        NameValuePair nvp[] = new NameValuePair[4];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        nvp[2] = new NameValuePair("bike_rfid", bikeRFID);
        nvp[3] = new NameValuePair("status", "Devolvendo");
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        return result;
    }

    public String devolvidoSlotByPosition(int slotPosition, String bikeRFID) {
        
        String url = System.getProperty("host_addr") + "totem/return_bike";
        
        logger.info("DEVOLVIDO RFID "+bikeRFID +", BAIA "+ slotPosition);
        
        NameValuePair nvp[] = new NameValuePair[4];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        nvp[2] = new NameValuePair("bike_rfid", bikeRFID);
        nvp[3] = new NameValuePair("status", "Devolvido");
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        return result;
    }
     public String cancelaReservaSlotByPosition(int slotPosition) {

        String url = System.getProperty("host_addr") + "totem/set_status_by_station_and_position";
        logger.info("CANCELA RESERVA BAIA "+ slotPosition);

        NameValuePair nvp[] = new NameValuePair[3];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        nvp[2] = new NameValuePair("status", "Ocupada");
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        return result;
    }

    public String canceladoSlotByPosition(int slotPosition, String bikeRFID) {

        String url = System.getProperty("host_addr") + "totem/cancel_rent";
        logger.info("ALUGUEL CANCELADO RFID " + bikeRFID + " BAIA " + slotPosition);
 
        NameValuePair nvp[] = new NameValuePair[3];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        nvp[2] = new NameValuePair("bike_rfid", bikeRFID);
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        return result;
    }

    public String reservaSlotByPosition(int slotPosition) {

        String url = System.getProperty("host_addr") + "totem/set_status_by_station_and_position";

        logger.info("RESERVA BAIA "+ slotPosition);

        NameValuePair nvp[] = new NameValuePair[3];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        nvp[2] = new NameValuePair("status", "Reservada");
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
      
        return result;
    }

    public String bloqueiaSlot(int slotPosition){
        String url = System.getProperty("host_addr") + "totem/block_slot";
        logger.info("BLOQUEIA BAIA "+ slotPosition);

        NameValuePair nvp[] = new NameValuePair[2];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        return result;
    }
    
    public String desbloqueiaSlot(int slotPosition){
        String url = System.getProperty("host_addr") + "totem/unblock_slot";
        logger.info("DESBLOQUEIA BAIA "+ slotPosition);

        NameValuePair nvp[] = new NameValuePair[2];
        nvp[0] = new NameValuePair("station_id", System.getProperty("station_id"));
        nvp[1] = new NameValuePair("slot_position", String.valueOf(slotPosition));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        return result;
    }
    
    public boolean validaRFID(String rfid){
        String url = System.getProperty("host_addr") + "totem/validate_rfid";
        logger.info("VALIDA RFID "+ rfid);

        NameValuePair nvp[] = new NameValuePair[1];
        nvp[0] = new NameValuePair("rfid", rfid);

        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        if(result.equals("rfid_correto")){
            return true;
        }
        else
            return false;

    }
}
