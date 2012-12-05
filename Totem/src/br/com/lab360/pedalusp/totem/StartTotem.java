package br.com.lab360.pedalusp.totem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

import org.apache.log4j.Logger;

import br.com.lab360.pedalusp.totem.data.StationData;
import br.com.lab360.pedalusp.totem.hwcontrol.HWController;
import br.com.lab360.pedalusp.totem.model.Station;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class StartTotem {

    public String parameter;
    public HWController hwcontroller;
    static Logger logger = Logger.getLogger(StartTotem.class);
  
  
    public void startTheTotem(HWController hwc) throws FileNotFoundException, IOException {

       

        hwcontroller = hwc;
        ServerListener serverListener = new ServerListener();
        serverListener.server = createWebServer(serverListener, "/controlTotem");

        //Busca arquivo de propriedades
        Properties props = new Properties();        
        InputStream in = null;
        try {
            //Busca o arquivo no Linux
            in = new FileInputStream("/home/usuario/Downloads/totem.properties");
            props.load(in);
            logger.info("Arquivo de configuração encontrado em /home/usuario/Downloads/totem.properties");
        } catch (Exception e) {
            in = null;
            logger.error("Arquivo não encontrado em /home/usuario/Downloads/totem.properties");
            try {
                //Busca o arquivo no Windows
                in = new FileInputStream("c:/totem/totem.properties");
                props.load(in);
                logger.info("Arquivo de configuração encontrado em c:/totem/totem.properties");
            } catch (Exception e2) {
                in = null;
                logger.error("Arquivo não encontrado em c:/totem/totem.properties");
            }
        }

        //Parametro para verificar se recebeu um novo pacote
        System.setProperty("receivedPackage", "false");
        System.setProperty("video_path", props.getProperty("video_path"));
        System.setProperty("host_addr", props.getProperty("host_addr"));
        System.setProperty("station_id", props.getProperty("station_id"));
        System.setProperty("serial_port", props.getProperty("serial_port"));
System.setProperty("use_to_hw_serial", props.getProperty("use_to_hw_serial"));

        StationData sd = new StationData();
        logger.info("Buscando informações da estação no servidor...");
        Station station = sd.getStation(Integer.parseInt(System.getProperty("station_id")));
        System.setProperty("station_number", String.valueOf(station.stationNumber));
        System.setProperty("number_slots", String.valueOf(station.slotList.size()));

        //SOMENTE PARA SIMULAÇÃO DO HW
        /*for (int p = 1; station.slotList.size() >= p; p++) {

            System.setProperty("BIKE".concat(String.valueOf(p)), station.slotList.get(p - 1).bikeRFID);
        }*/

        //Parametros de teste para simular o HARDWARE
        for (int t = 1; station.slotList.size() >= t; t++) {
            if (station.slotList.get(t - 1).status.equals("Ocupada")) {
                System.setProperty("HW".concat(String.valueOf(t)), "1");
            }
            if (station.slotList.get(t - 1).status.equals("Reservada")) {
                System.setProperty("HW".concat(String.valueOf(t)), "2");
            }
            if (station.slotList.get(t - 1).status.equals("Liberando")
                    || station.slotList.get(t - 1).status.equals("Solicitando Liberacao")) {
                System.setProperty("HW".concat(String.valueOf(t)), "3");
            }
            if (station.slotList.get(t - 1).status.equals("Vazia")) {
                System.setProperty("HW".concat(String.valueOf(t)), "4");
            }
            if (station.slotList.get(t - 1).status.equals("Devolvendo")) {
                System.setProperty("HW".concat(String.valueOf(t)), "5");
            }
            if (station.slotList.get(t - 1).status.equals("Bloqueada")) {
                System.setProperty("HW".concat(String.valueOf(t)), "6");
            }

           // System.out.println(System.getProperty("HW".concat(String.valueOf(t))));
        }

        //Parametros globais com status do hardware
        for (int t = 1; station.slotList.size() >= t; t++) {
            if (station.slotList.get(t - 1).status.equals("Ocupada")) {
                System.setProperty("SLOT".concat(String.valueOf(t)), "1");
            }
            if (station.slotList.get(t - 1).status.equals("Reservada")) {
                System.setProperty("SLOT".concat(String.valueOf(t)), "2");
            }
           // System.out.println(station.slotList.get(t - 1).status);
            if (station.slotList.get(t - 1).status.equals("Liberando")
                    || station.slotList.get(t - 1).status.equals("Solicitada Liberacao")) {
                System.setProperty("SLOT".concat(String.valueOf(t)), "3");
            }
            if (station.slotList.get(t - 1).status.equals("Vazia")) {
                System.setProperty("SLOT".concat(String.valueOf(t)), "4");
            }
            if (station.slotList.get(t - 1).status.equals("Devolvendo")) {
                System.setProperty("SLOT".concat(String.valueOf(t)), "5");
            }
            if (station.slotList.get(t - 1).status.equals("Bloqueada")) {
                System.setProperty("SLOT".concat(String.valueOf(t)), "6");
            }
        }
        
    }

    /**
     * Cria uma instancia de servidor para receber requisição para liberação via celular
     * @param serverClass
     * @param context
     * @return
     */
    public static HttpServer createWebServer(HttpHandler serverClass, String context) {

        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
            HttpContext ctx = server.createContext(context, serverClass);
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return server;
    }

    private class ServerListener implements HttpHandler {

        public HttpServer server;

        //método é executado quando máquina recebe uma requisição
        public void handle(HttpExchange exchange) throws IOException {

            //pega os parametros para liberacao da bike
            String userId = new String(getParam(exchange.getRequestURI().getQuery(), "userid"));
            String slotPosition = new String(getParam(exchange.getRequestURI().getQuery(), "slotposition"));
            String function = new String(getParam(exchange.getRequestURI().getQuery(), "function"));

            logger.info("Recebe requisição do Servidor");
            logger.info("IDUsuario: " + userId + " - SlotPosition: " + slotPosition + " - Function: " + function);

            StringBuffer response = new StringBuffer();


            if (function.equals("libera")) {
                logger.info("DESTRAVAMENTO VIA CELULAR");
                hwcontroller.destravamento(Integer.parseInt(slotPosition));
                //responde requisição
                response.append("<html><head></head><body>");
                response.append("Liberado");
                response.append("</body></html>");
            }
            if (function.equals("bloqueia")) {
                logger.info("BLOQUEIO VIA SERVER");
                hwcontroller.bloqueiaBaia(Integer.parseInt(slotPosition));
                //responde requisição
                response.append("<html><head></head><body>");
                response.append("Bloqueado");
                response.append("</body></html>");
            }
            if (function.equals("desbloqueia")) {
                logger.info("DESBLOQUEIO VIA SERVER");
                hwcontroller.desbloqueiaBaia(Integer.parseInt(slotPosition));
                //responde requisição
                response.append("<html><head></head><body>");
                response.append("Desbloqueado");
                response.append("</body></html>");
            }
            if (function.equals("reseta")){
            	logger.info("RESET DA PLACA VIA SERVER");
            	hwcontroller.resetaBaia(Integer.parseInt(slotPosition));
//            	try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            	hwcontroller.getStatus();
            	//responde requisição
            	boolean bike = System.getProperty("SLOT" + slotPosition).equals("1");
            	String rfid = System.getProperty("RESET_RFID" + slotPosition);
            	logger.info("Acessando propriedade RESET_RFID"+ slotPosition+" valor: " + System.getProperty("RFID" + slotPosition));
//            	String rfid = "0462F90EFD";
            	response.append(bike? "bike:"+rfid : "vazia");
            }
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = (OutputStream) exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
            exchange.close();


        }

        //Quebra a QueryString
        public String getParam(String query, String requestedParam) {
            String paramValue = null;
            String paramName = new String("");

            int i;
            boolean copy = true;
            for (i = 0; i < query.length(); i++) {

                if (requestedParam.compareTo(paramName) == 0) {
                    break;
                }

                if (query.charAt(i) == '=') {
                    copy = false;
                }

                if (copy) {
                    paramName += query.charAt(i);
                }

                if (query.charAt(i) == '&') {
                    paramName = "";
                    copy = true;
                }

            }


            if (paramName.compareTo("") == 0) {
                return null;
            } else {

                i++; //skip equal sign (=)
                paramValue = new String("");
                while (query.charAt(i) != '&') {
                    paramValue += query.charAt(i);
                    i++;
                    if (i == query.length()) {
                        break;
                    }
                }
                return paramValue;

            }
        }
    }
}
