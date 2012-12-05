/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.data;

import org.apache.commons.httpclient.NameValuePair;

import br.com.lab360.pedalusp.totem.model.Punishment;
import br.com.lab360.totem.service.WebServiceClient;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author User
 */
public class PunishmentData {

     public Punishment getPunishment(int punishmentId) {
        XStream xstream = new XStream();

        xstream.processAnnotations(Punishment.class);
        xstream.omitField(Punishment.class, "created-at");
        xstream.omitField(Punishment.class, "updated-at");

        String url = System.getProperty("host_addr") + "totem/punishment";

        NameValuePair nvp[] = new NameValuePair[1];
        nvp[0] = new NameValuePair("punishment_id", String.valueOf(punishmentId));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        Punishment punishment = (Punishment) xstream.fromXML(result);
        return punishment;
    }
}
