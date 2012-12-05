/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lab360.pedalusp.totem.serial;

import br.com.lab360.pedalusp.totem.StartTotem;
import br.com.lab360.pedalusp.totem.hwcontrol.BikeHWSimulator;
import br.com.lab360.pedalusp.totem.hwcontrol.DataPackage;
import br.com.lab360.pedalusp.totem.hwcontrol.HWController;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.comm.CommDriver;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class SerialConnection {

    static Enumeration portList;
    static CommPortIdentifier portId;
    static SerialPort serialPort;
    static OutputStream outputStream;
    SerialController serialController;
    HWController hwController;
    static Logger logger = Logger.getLogger(SerialConnection.class);
    public SerialConnection(HWController hwc, BikeHWSimulator bHWSim) throws IOException {
        hwController = hwc;
        //Busca arquivo de propriedades
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream("/home/usuario/Downloads/totem.properties");
            props.load(in);
        } catch (Exception e) {
            in = null;
            logger.info("Arquivo não encontrado em home/user*");
            try {
                in = new FileInputStream("c:/totem/totem.properties");
                props.load(in);
                logger.info("arquivo encontrado em c:/totem/totem.properties");
            } catch (Exception e2) {
                in = null;
                logger.info("Arquivo não encontrado em c:/totem/totem.properties");
            }
        }
        if (in == null) {
            in = StartTotem.class.getClassLoader().getResourceAsStream("br/com/lab360/pedalusp/totem/totem.properties");
            if (in == null) {
                logger.info("Arquivo nao encontrado");
            } else {
                props.load(in);
            }
        }

        //Carrega o Driver
        String driverName = props.getProperty("comm_driver");
        logger.info(driverName);
        try {
            CommDriver driver = (CommDriver) Class.forName(driverName).newInstance();
            driver.initialize();
        } catch (Throwable e) {
            logger.info(e.getStackTrace());
        }

        portList = CommPortIdentifier.getPortIdentifiers();
        logger.info("Procurando a porta...");

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getName().equals(props.getProperty("serial_port"))) {
                logger.info("Porta encontrada: "+ portId.getName());
                serialController = new SerialController(portId, hwc, bHWSim);
                serialController.run();
            }
        }
    }

    public void sendData(DataPackage dp, int qtdade) {
        if(dp.END_D!=1 || dp.END_O!=1){
        System.setProperty("sendOrigin",String.valueOf(dp.END_D));
        for (int count = 0; (System.getProperty("receivedPackage").equals("false") ||
                System.getProperty("receivedPackage").equals("error")) && count<6;  count++){
            try {
                
                //se não chegou em 600 milisegundos, envia novamente
                if (count == 0 ) {
                    serialController.sendData(dp.dataPackageToByteArray(qtdade));
                }
                if(dp.FUNCAO == 0x04){
                    System.setProperty("receivedPackage","true");
                }
                Thread.sleep(50);
                
            } catch (InterruptedException ex) {
                logger.info("erro"+ ex.getStackTrace());
            }
        }
        System.setProperty("receivedPackage","false");
        }        
    }
    //SIMULADOR
    public void sendDataWH(DataPackage dp, int qtdade) {
        serialController.sendData(dp.dataPackageToByteArray(qtdade));
    }
}
