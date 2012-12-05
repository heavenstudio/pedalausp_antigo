package br.com.lab360.totem.service;

import java.io.IOException;
import java.util.logging.Logger;
import org.apache.commons.httpclient.Credentials;
import static br.com.lab360.totem.service.Methods.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

/**
 * Serviço para chamada de Web Service no modelo REST com uso da API HttpClient
 *
 * @author wladimir
 */
public class WebServiceClient {

    private static final Logger log = Logger.getLogger(WebServiceClient.class.getName());
    private HttpClient client;
    private String uri;

    /**
     * URI do servidor com caminho do serviço web
     *
     * @param url URI completa com caminho do serviço web
     */
    public WebServiceClient(String uri) {
        this.uri = uri;
        client = new HttpClient();
    }


    /**
     * Chama um WebService de acordo com um método de requisição e um xml de parâmetro
     * @param m Método de requisição @see br.com.lab360.totem.service.Methods
     * @param xml dados enviados como parâmetro para a requisição (quando aplicável)
     * @return Retorno da chamada do WebService
     */
    public String callWebService(Methods m, String xml) {

        String result = "";
        HttpMethodBase method = null;

        switch (m) {
            case Get:
                method = new GetMethod(uri);
                break;
            case Post:
                method = new PostMethod(uri);
                break;
            case Put:
                method = new PutMethod(uri);
                break;
            case Delete:
                method = new DeleteMethod(uri);
                break;
            default:
                throw new IllegalArgumentException("Método de requisição desconhecido");
        }

        // armazena o parametro (sem nome?)
        
        method.getParams().setParameter(null, xml);
        method.addRequestHeader("Accept","application/xml");
        method.addRequestHeader("Content-Type","application/xml");

        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("287613102", "1234");
        client.getState().setCredentials(AuthScope.ANY, defaultcreds);
        // executa o método
        try {
            int statusCode = client.executeMethod(method);

            result = method.getResponseBodyAsString();
            if (statusCode != HttpStatus.SC_OK) {
                log.info("Method response: " + method.getStatusLine());
            }
        } catch (HttpException e) {
            log.severe("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.severe("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // libera conexão
            method.releaseConnection();
        }

        return result;
    }

    public String postHTTPRequest(NameValuePair nvp[]){
        String contents = "";
         try {            
            PostMethod method = new PostMethod(uri);
            method.addParameters(nvp);

            // Execute the POST method
            int statusCode = client.executeMethod(method);
            if (statusCode != -1) {
                contents = method.getResponseBodyAsString();
                method.releaseConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         return contents;
    }
}
