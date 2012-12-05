package br.com.lab360.pedalusp.totem.serial;

import br.com.lab360.pedalusp.totem.hwcontrol.BikeHWSimulator;
import br.com.lab360.pedalusp.totem.hwcontrol.DataPackage;
import br.com.lab360.pedalusp.totem.hwcontrol.DataPackageDadosStatus;
import br.com.lab360.pedalusp.totem.hwcontrol.HWController;
import java.io.*;
import java.util.*;
import javax.comm.*;
import org.apache.log4j.Logger;

public class SerialController implements Runnable, SerialPortEventListener {

    static CommPortIdentifier portId;
    static Enumeration portList;
    static InputStream inputStream;
    static OutputStream outputStream;
    static SerialPort serialPort;
    Thread readThread;
    ArrayList<Byte> byteArrayList;
    HWController hwControl;
    static Logger logger = Logger.getLogger(SerialController.class);

    //Temporario para simular o HW
    public BikeHWSimulator bHWSim;

    /*
     * Cria a conexão para recebimento de dados da porta serial
     */
    public SerialController(CommPortIdentifier portId, HWController hwc, BikeHWSimulator bHWSim) {

        this.portId = portId;
        hwControl = hwc;
        this.bHWSim = bHWSim;

        byteArrayList = new ArrayList<Byte>();
        try {
            serialPort = (SerialPort) portId.open("TotemController", 200);
        } catch (PortInUseException e) {
            logger.info(e.getMessage());
        }
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            logger.info(e.getMessage());
        }
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            logger.info(e.getMessage());
        }
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }
    }

    public void sendData(byte[] data) {
        if (serialPort == null) {
            try {
                serialPort = (SerialPort) portId.open("TotemController", 200);
            } catch (PortInUseException e) {
                logger.info(e.getMessage());
            }
        }
        try {
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            logger.info(e.getMessage());
            try {
                serialPort.setSerialPortParams(9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

            } catch (UnsupportedCommOperationException e2) {
                logger.info(e2.getMessage());
            }
        }
        try {
            //Envia dados para o hardware
            outputStream.write(data);

        } catch (IOException e) {
            logger.info("Erro ao enviar os dados: " + e.getMessage());
        }

    }
    /*
     * Trata recebimento de dados pela serial
     */

    @Override
    public void serialEvent(SerialPortEvent e) {

        int newData = 0;

        // Determina o tipo de evento
        switch (e.getEventType()) {

            //Se existir novo dado
            case SerialPortEvent.DATA_AVAILABLE:

                try {
                    //Recupera o dado
                    boolean signal = false;
                    boolean cont = true;
                    newData = inputStream.read();
                    while (newData != -1) {

                       
                        if (cont) {
                            //Insere dado no array
                            byteArrayList.add((byte) newData);

                            //Imprime bbufString
                            String bbufString = "";
                            Iterator itr = byteArrayList.iterator();
                            while (itr.hasNext()) {
                                bbufString += itr.next()+"-";
                            }
//                            logger.info(bbufString);

                            //Se o primeiro elemento for diferente de 0x02, então não é inicio de pacote
                            if (byteArrayList.size() > 0) {
                                while (byteArrayList.size() > 0 && byteArrayList.get(0) != 0x02) {
                                    byteArrayList.remove(0);
                                }
                            }

                            //logger.info("buffer size");
                            //logger.info(byteArrayList.size());

                            //Se já tem acumulado pelo menos 6 bytes (Chegou no QTD)
                            // STX | END_O | END_D | FUNCAO | QTD | DADOS | CKSUM | ETX
                            if (byteArrayList.size() > 6) {

                                //Teste para resolver o esquema de byte
                                //int qtdadeDados = (int) byteArrayList.get(4);
                                int qtdadeDados = byteArrayList.get(4).intValue();

                                //logger.info((int) byteArrayList.get(4));
                                if (qtdadeDados + 7 <= byteArrayList.size()) {
                                    //logger.info("TAMANHO BYTE ARRAY");
                                    //logger.info(byteArrayList.size());
                                    int tamanhoTotal = 7 + qtdadeDados;
                                    byte enderecoDestino = byteArrayList.get(2);
                                    //logger.info("endereco destino");
                                    //logger.info(enderecoDestino);

                                    List<Byte> pacoteCompleto = byteArrayList.subList(0, tamanhoTotal);

                                    logger.info("pacote completo");

                                    //Coloca em um array de bytes
                                    byte[] byteArray = new byte[tamanhoTotal];
                                    Iterator itrComp = pacoteCompleto.iterator();

                                    String pacoteString = "";
                                    for (int y = 0; itrComp.hasNext(); y++) {
                                        byteArray[y] = (Byte) itrComp.next();
                                        pacoteString += byteArray[y];
                                    }
                                    logger.info(pacoteString);

                                    //Apaga os dados do byteAray do pacote já lido
                                    for (int k = 0; k < tamanhoTotal; k++) {
                                        byteArrayList.remove(0);
                                    }

                                    //Verifica se o pacote termina com numero diferente de 3
                                    if (pacoteString.charAt(pacoteString.length() - 1) != '3') {
                                        //se termina com diferente: pacote com erro
                                        logger.info("pacote com erro");
                                    } else {
                                        logger.info("pacote correto");

                                        //se for para o quiosque (endereço destino 0)
                                        logger.info(String.valueOf(enderecoDestino));
                                        if (enderecoDestino == 0x00) {
                                            DataPackage dpResponse = new DataPackage();
                                            dpResponse = dpResponse.byteToPackage(byteArray);

                                            //verifica se foi o hw correto que respondeu
                                            //if (dpResponse.END_O == Integer.parseInt(System.getProperty("sendOrigin"))) {
                                            System.setProperty("receivedPackage", "true");
                                            hwControl.trataResposta(dpResponse);
                                            logger.info("HW->Totem NA SERIAL");
                                            logger.info(dpResponse.toString());
                                            if (dpResponse.DADOS instanceof DataPackageDadosStatus){
                                            	DataPackageDadosStatus dpds = (DataPackageDadosStatus) dpResponse.DADOS;
                                            	logger.info(String.format("SETANDO PROPRIEDADE RESET_RFID%s, valor: %s", String.valueOf(dpResponse.END_O), dpds.RFID));
                                            	System.setProperty("RESET_RFID" + String.valueOf(dpResponse.END_O), String.valueOf(dpds.RFID));
                                            }
                                            // } else {
                                            //    System.setProperty("receivedPackage", "error");
                                            // }

                                        } //se for para hw (endereço destino diferente de 0)
                                        //NÃO INTERESSA PARA O TOTEM.
                                        //SOMENTE PARA TESTES
                                        else {

                                            DataPackage dp = new DataPackage();
                                            dp = dp.byteToPackage(byteArray);
                                            logger.info("o que voltou");
                                            logger.info(dp.toString());

                                            //Envia para o simulador de HW
                                            DataPackage resposta = bHWSim.sendMessageToHW(dp);
                                            //hwControl.trataResposta(resposta);
                                            logger.info("Totem->HW Na serial");
                                            logger.info(dp.toString());
                                            logger.info("HW->Totem");
                                            logger.info(resposta.toString());


                                        }
                                    }
                                }
                            }
                        }
                        //pega o proximo no inputStream
                        newData = inputStream.read();
                    }
                } catch (IOException ex) {
                    logger.info(ex.getMessage());
                    return;
                }
                break;
        }

    }



}
