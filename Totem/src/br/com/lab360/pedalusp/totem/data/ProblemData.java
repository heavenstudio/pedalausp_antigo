/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.data;

import org.apache.commons.httpclient.NameValuePair;

import br.com.lab360.pedalusp.totem.model.Problem;
import br.com.lab360.pedalusp.totem.model.ProblemReport;
import br.com.lab360.pedalusp.totem.model.Problems;
import br.com.lab360.totem.service.Methods;
import br.com.lab360.totem.service.WebServiceClient;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author User
 */
public class ProblemData {
    
    public Problems getProblemsList() {
        XStream xstream = new XStream();

        xstream.processAnnotations(Problems.class);
        xstream.processAnnotations(Problem.class);
        xstream.omitField(Problem.class, "created-at");
        xstream.omitField(Problem.class, "updated-at");

        //WebServiceClient instance = new WebServiceClient(System.getProperty("host_addr")+"problems.xml");
        //String result = instance.callWebService(Methods.Get, "");

        String url = System.getProperty("host_addr") + "totem/problems";
        
        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.callWebService(Methods.Get, "");

        Problems receivedProblems = (Problems) xstream.fromXML(result);
        return receivedProblems;
    }

    public String reportProblem(int problem_id, int cyclist_id, int number){

        ProblemReport pr = new ProblemReport();
        pr.cyclist_id = cyclist_id;
        pr.problem_id = problem_id;
        pr.station_id = Integer.parseInt(System.getProperty("station_id"));
        pr.number_reported = number;
       
        String url = System.getProperty("host_addr") + "totem/report_problem";

        NameValuePair nvp[] = new NameValuePair[4];
        nvp[0] = new NameValuePair("station_id", String.valueOf(pr.station_id));
        nvp[1] = new NameValuePair("cyclist_id", String.valueOf(pr.cyclist_id));
        nvp[2] = new NameValuePair("problem_id", String.valueOf(pr.problem_id));
        nvp[3] = new NameValuePair("number_reported", String.valueOf(pr.number_reported));

        WebServiceClient instance = new WebServiceClient(url);
        String result = instance.postHTTPRequest(nvp);
        
        if(result.equals("notsaved")){
            return "error";
        }
        else{
            return "OK";
        }
    }
}
