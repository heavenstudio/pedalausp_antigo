/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.data;

import java.util.ArrayList;

import org.apache.commons.httpclient.NameValuePair;

import br.com.lab360.pedalusp.totem.model.AppliedPunishment;
import br.com.lab360.pedalusp.totem.model.AppliedPunishments;
import br.com.lab360.totem.service.WebServiceClient;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author User
 */
public class AppliedPunishmentsData {

     public AppliedPunishments getAppliedPunishments(int cyclistId) {
        XStream xstream = new XStream();

        xstream.processAnnotations(AppliedPunishments.class);
        xstream.processAnnotations(AppliedPunishment.class);
        xstream.omitField(AppliedPunishment.class, "created-at");
        xstream.omitField(AppliedPunishment.class, "updated-at");

        String url = System.getProperty("host_addr") + "totem/punishments_by_cyclist";

        NameValuePair nvp[] = new NameValuePair[3];
        nvp[0] = new NameValuePair("id", String.valueOf(cyclistId));
        nvp[1] = new NameValuePair("nusp", System.getProperty("cyclist_nusp"));
        nvp[2] = new NameValuePair("password", System.getProperty("cyclist_password"));
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        AppliedPunishments receivedPunishments;
        if(result.equals("notfound")){
            receivedPunishments =  new AppliedPunishments();
            receivedPunishments.appliedPunishments = new ArrayList<AppliedPunishment>();
        }
        else{
            receivedPunishments = (AppliedPunishments) xstream.fromXML(result);
        }
        return receivedPunishments;
    }
}
