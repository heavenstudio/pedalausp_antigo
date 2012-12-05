/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.data;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import br.com.lab360.pedalusp.totem.model.Cyclist;
import br.com.lab360.totem.service.WebServiceClient;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author User
 */
public class CyclistData {

	static Logger logger = Logger.getLogger(CyclistData.class);
	public Cyclist getCyclistData() {
		XStream xstream = new XStream();

		xstream.processAnnotations(Cyclist.class);
		xstream.processAnnotations(Cyclist.class);
		xstream.omitField(Cyclist.class, "created-at");
		xstream.omitField(Cyclist.class, "updated-at");

		String url = System.getProperty("host_addr") + "totem/cyclists";

		NameValuePair nvp[] = new NameValuePair[3];
		nvp[0] = new NameValuePair("id", System.getProperty("cyclist_id"));
		nvp[1] = new NameValuePair("nusp", System.getProperty("cyclist_nusp"));
		nvp[2] = new NameValuePair("password", System
				.getProperty("cyclist_password"));
		WebServiceClient instance = new WebServiceClient(url);
		String result = instance.postHTTPRequest(nvp);

		// System.out.println(System.getProperty("host_addr")+"cyclist/"++".xml");
		// WebServiceClient instance = new
		// WebServiceClient(System.getProperty("host_addr")+"cyclists/"+System.getProperty("cyclist_id")+".xml");
		// String result = instance.callWebService(Methods.Get, "");
		Cyclist receivedCyclist = (Cyclist) xstream.fromXML(result);
		return receivedCyclist;
	}

	public String verificaPermissaoCiclista() {

		String url = System.getProperty("host_addr") + "totem/check_cyclist_permission";

		logger
				.info("VERIFICA PERMISSAO CICLISTA: "+ System
						.getProperty("cyclist_id"));

		NameValuePair nvp[] = new NameValuePair[1];
		nvp[0] = new NameValuePair("cyclist_id", System
				.getProperty("cyclist_id"));
		WebServiceClient instance = new WebServiceClient(url);
		String result = instance.postHTTPRequest(nvp);

		return result;
	}

}
