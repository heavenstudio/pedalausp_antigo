/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lab360.pedalusp.totem.data;

import br.com.lab360.totem.service.WebServiceClient;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author User
 */
public class LoginData {

    public String verificaLogin(String nusp, String password) {

        String url = System.getProperty("host_addr") + "totem/login";

        NameValuePair nvp[] = new NameValuePair[2];
        nvp[0] = new NameValuePair("nusp", nusp);
        nvp[1] = new NameValuePair("password", password);
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);

        return result;
    }
}
