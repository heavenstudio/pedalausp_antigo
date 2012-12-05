/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lab360.pedalusp.totem.hwcontrol;

import java.util.ArrayList;
import java.util.Properties;

import javafx.async.RunnableFuture;

import org.apache.log4j.Logger;

import com.ctc.wstx.sr.ElemAttrs;

import br.com.lab360.pedalusp.totem.data.SlotData;
import br.com.lab360.pedalusp.totem.serial.SerialConnection;

/**
 *
 * @author User
 */
public class HWController implements RunnableFuture {

    static Logger logger = Logger.getLogger(HWController.class);
    DataPackage sendDP = new DataPackage();
    ArrayList msgList = new ArrayList();
    SlotData sData = new SlotData();
    DataPackage responsePack;
    int numberSlots = 0;
    public SerialConnection conn;
    BikeHWSimulator bkSim;

    public HWController() throws InterruptedException {
    }
    /*
     * Solicita Status dos HW
     */

    public void getStatus() {

        for (int i = 1; i <= Integer.parseInt(System.getProperty("number_slots")); i++) {

            //PEDE STATUS
            sendDP.END_O = 0x00; //O endereço do quiosque é o ZERO
            sendDP.END_D = Byte.decode(Integer.toString(i));
            sendDP.FUNCAO = 0x02;
            sendDP.QTD = 0x00;
            sendDP.updateCHECKSUM();
             //ENVIA OS DADOS PARA O HARDWARE
            if (System.getProperty("use_to_hw_serial").equals("true")) {
                conn.sendData(sendDP, 7);
                //quantidade de dados 7
            } //Dados enviados
            //System.out.println("DADOS ENVIADOS:" + sendDP.toString());
            else if (System.getProperty("use_to_hw_serial").equals("false")) {
                //Chamada para o simulador de HW
                responsePack = bkSim.sendMessageToHW(sendDP);
                //Dados recebidos
                //System.out.println("DADOS RECEBIDOS:" + responsePack.toString());
                //PARA CASOS DE TESTES SEM HW
                trataResposta(responsePack);
            }
        }
        //Envia outros pacotes de dados (DESTRAVAMENTO / CONFIRMAÇÃO DE TRAVAMENTO)
        while (msgList.size() > 0) {

            sendDP = (DataPackage) msgList.get(msgList.size() - 1);

            msgList.remove(msgList.size() - 1);

            if (System.getProperty("use_to_hw_serial").equals("true")) {
                conn.sendData(sendDP, 7);
            } else if (System.getProperty("use_to_hw_serial").equals("false")) {
                responsePack = bkSim.sendMessageToHW(sendDP);
                if (sendDP.FUNCAO != 0x04) {
                    trataResposta(responsePack);
                }
            }
        }
//        logger.info("Status Atual SLOT:" + getHWStatus());
//        logger.info("Status Atual HW:" + getHWStatusHW());P
    }

    //Envia mensagem para HW para DESTRAVAR
    public void destravamento(int HW) {
        logger.info("Solicita DESTRAVAMENTO do HW : " + HW);
        DataPackage msgDest = new DataPackage();
        msgDest.END_O = 0x00;

        msgDest.END_D = Byte.decode(Integer.toString(HW));
        msgDest.FUNCAO = 0x01;
        msgDest.QTD = 0x00;
        msgDest.updateCHECKSUM();

//        logger.info("Mensagem de DESTRAVAR: " + msgDest.toString());
        msgList.add(msgDest);

    }
    // Mensagem de confirmação de travamento

    public void travamentoOK(int HW) {

        logger.info("Solicita TRAVAMENTO do HW: " + HW);
        DataPackage msgtOK = new DataPackage();
        msgtOK.END_O = 0x00;
        msgtOK.END_D = Byte.decode(Integer.toString(HW));
        msgtOK.FUNCAO = 0x04;
        msgtOK.QTD = 0x00;
        msgtOK.updateCHECKSUM();

//        logger.info("Mensagem de TRAVAR: " + msgtOK.toString());
        msgList.add(msgtOK);
    }

    public void bloqueiaBaia(int HW) {

        logger.info("Solicita BLOQUEIO do HW: " + HW);
        DataPackage msgBlock = new DataPackage();

        msgBlock.END_O = 0x00;
        msgBlock.END_D = Byte.decode(Integer.toString(HW));
        msgBlock.FUNCAO = 0x05;
        msgBlock.QTD = 0x00;
        msgBlock.updateCHECKSUM();
//        logger.info("Mensagem de  BLOQUEAR: " + msgBlock.toString());
        msgList.add(msgBlock);
    }

    public void desbloqueiaBaia(int HW) {

        logger.info("Solicita DESBLOQUEIO do HW: " + HW);
        DataPackage msgUnblock = new DataPackage();
        //msgtOK.END_O = 0x00;
        msgUnblock.END_O = 0x00;
        msgUnblock.END_D = Byte.decode(Integer.toString(HW));
        msgUnblock.FUNCAO = 0x06;
        msgUnblock.QTD = 0x00;
        msgUnblock.updateCHECKSUM();
//        logger.info("Mensagem de DESBLOQUEAR: " + msgUnblock.toString());
        msgList.add(msgUnblock);
    }
    
    public void resetaBaia(int HW){
    	logger.info("Solicita RESET do HW: " + HW);
    	DataPackage msgReset = new DataPackage();
    	msgReset.END_O = 0x00;
    	msgReset.END_D = Byte.decode(Integer.toString(HW));
    	msgReset.FUNCAO = 0x07;
    	msgReset.QTD = 0x00;
    	msgReset.updateCHECKSUM();
//        logger.info("Mensagem de  RESET: " + msgReset.toString());
        msgList.add(msgReset);
    }
    /*
     * Trata as respostas recebidas do HW
     */

    public void trataResposta(DataPackage response) {
        //Confirmação de destravamento
        if (response.FUNCAO == 0x30) {
            DataPackageDadosDestravamento dpdd = (DataPackageDadosDestravamento) response.DADOS;
            if (dpdd.DADODESTRAVAMENTO == 0x00) {
//                System.out.println("SLOT DESTRAVADO");
                //Avisar servidor de destravamento completo
                sData.liberaSlotByPosition(response.END_O);
                System.setProperty("SLOT" + response.END_O, "3");
            }
        }
        //Resposta de PEDIDO DE STATUS
        if (response.FUNCAO == 0x31) {
            DataPackageDadosStatus dpds = (DataPackageDadosStatus) response.DADOS;
            switch (response.DADOS.getModo()) {

                //Aguardando retirada da Bicicleta  --- MODO 3 ---
                case 0x03:
                    if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("2")
                            || System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("1")) {
                        System.setProperty("SLOT" + String.valueOf(response.END_O), "3");
                        sData.liberaSlotByPosition(response.END_O);
                    }
                    break;

                //Status - Bicicleta ausente --- MODO 4 ---
                case 0x04:
                    if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("3")
                            || System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("4")) {

                        if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("3")) {
                            sData.vazioSlotByPosition(response.END_O);
                        }
                        //Atualiza o status
                        System.setProperty("SLOT" + String.valueOf(response.END_O), "4");
                    } else {
                        if (verifyUnblock(System.getProperty("SLOT" + String.valueOf(response.END_O)))) {
                            sData.desbloqueiaSlot(response.END_O);
                            System.setProperty("SLOT" + response.END_O, "4");
                        } else {
                            //ERRO - Bicicleta retirada sem permissão
                            sData.bloqueiaSlot(response.END_O);
                            System.setProperty("SLOT" + response.END_O, "10");
//                            System.out.println("Bicicleta Liberada sem permissão;");
                        }
                    }
                    break;


                //Bicicleta presente e aguardando confirmação de travamento --- MODO 5 ---
                case 0x05:
                    //Verifica se o RFID é valido, caso contrario bloqueia
                    if (sData.validaRFID(String.valueOf(dpds.RFID))) {

                        if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("4")
                                || System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("5")) {
                            //Atualiza o status
                            System.setProperty("SLOT" + response.END_O, "5");

//                            System.out.println("__________________ CONFIRMA DEVOLUÇÃO________________");
//                            System.out.println(dpds.RFID);

                            sData.devolvendoSlotByPosition(response.END_O, String.valueOf(dpds.RFID));

                            //Envia para o HW confirmação de travamento
                            travamentoOK(response.END_O);
                        } else {
                            if (verifyUnblock(System.getProperty("SLOT" + String.valueOf(response.END_O)))) {
                                System.setProperty("SLOT" + response.END_O, "1");
                                sData.desbloqueiaSlot(response.END_O);

                            } else {
                                if (verifyUnblock(System.getProperty("SLOT" + String.valueOf(response.END_O)))) {
                                    System.setProperty("SLOT" + response.END_O, "1");
                                    sData.desbloqueiaSlot(response.END_O);

                                } else {
                                    sData.bloqueiaSlot(response.END_O);
                                    System.setProperty("SLOT" + response.END_O, "10");
//                                    System.out.println("Bicicleta nova na baia com erro");
                                }
                            }
                        }
                    } else {
                        sData.bloqueiaSlot(response.END_O);
                        System.setProperty("SLOT" + response.END_O, "10");
//                        System.out.println("Bicicleta nova na baia com erro");
                    }

                    break;

                //Status - Bicicleta Presente --- MODO 1 ---
                case 0x01:
                    //Pode vir após modos 1, 2, 3, 5;
                    if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("1")) {
                        System.setProperty("SLOT" + response.END_O, "1");
                        logger.info(String.format("SETANDO PROPRIEDADE RESET_RFID%s, valor: %s", String.valueOf(response.END_O), dpds.RFID));
                        System.setProperty("RESET_RFID" + String.valueOf(response.END_O), String.valueOf(dpds.RFID));
                    } else {
                        if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("5")) {
                            sData.devolvidoSlotByPosition(response.END_O, String.valueOf(dpds.RFID));
                            System.setProperty("SLOT" + String.valueOf(response.END_O), "1");
                            logger.info(String.format("SETANDO PROPRIEDADE RESET_RFID%s, valor: %s", String.valueOf(response.END_O), dpds.RFID));
                            System.setProperty("RESET_RFID" + String.valueOf(response.END_O), String.valueOf(dpds.RFID));

                        } else {
                            if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("2")) {
                                sData.cancelaReservaSlotByPosition(response.END_O);
                                System.setProperty("SLOT" + String.valueOf(response.END_O), "1");
                                logger.info(String.format("SETANDO PROPRIEDADE RESET_RFID%s, valor: %s", String.valueOf(response.END_O), dpds.RFID));
                                System.setProperty("RESET_RFID" + String.valueOf(response.END_O), String.valueOf(dpds.RFID));
                            } else {
                                if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("3")) {
                                    sData.canceladoSlotByPosition(response.END_O, String.valueOf(dpds.RFID));
                                    System.setProperty("SLOT" + String.valueOf(response.END_O), "1");
                                    logger.info(String.format("SETANDO PROPRIEDADE RESET_RFID%s, valor: %s", String.valueOf(response.END_O), dpds.RFID));
                                    System.setProperty("RESET_RFID" + String.valueOf(response.END_O), String.valueOf(dpds.RFID));
                                } else {
                                    //É um desbloqueio
                                    if (verifyUnblock(System.getProperty("SLOT" + String.valueOf(response.END_O)))) {
                                        System.setProperty("SLOT" + response.END_O, "1");
                                        sData.desbloqueiaSlot(response.END_O);
                                        logger.info(String.format("SETANDO PROPRIEDADE RESET_RFID%s, valor: %s", String.valueOf(response.END_O), dpds.RFID));
                                        System.setProperty("RESET_RFID" + String.valueOf(response.END_O), String.valueOf(dpds.RFID));

                                    } else {
                                        sData.bloqueiaSlot(response.END_O);
                                        System.setProperty("SLOT" + response.END_O, "10");
//                                        System.out.println("Bicicleta nova sem dados anteriores");
                                    }
                                }
                            }
                        }
                    }

                    break;

                //Bicicleta Reservada
                case 0x02:
                    if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("1")
                            || System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("2")) {
                        System.setProperty("SLOT" + response.END_O, "2");
                        sData.reservaSlotByPosition(response.END_O);
                    } else {
                        if (verifyUnblock(System.getProperty("SLOT" + String.valueOf(response.END_O)))) {
                            System.setProperty("SLOT" + response.END_O, "1");
                            logger.info(String.format("SETANDO PROPRIEDADE RESET_RFID%s, valor: %s", String.valueOf(response.END_O), dpds.RFID));
                            System.setProperty("RESET_RFID" + String.valueOf(response.END_O), String.valueOf(dpds.RFID));

                        } else {
                            sData.bloqueiaSlot(response.END_O);
                            System.setProperty("SLOT" + response.END_O, "10");
//                            System.out.println("Erro com biclicleta reservada");
                        }

                    }
                    break;
                case 0x11:
                case 0x12:
                case 0x13:
                case 0x14:
                case 0x15:
		    //Se existia emprestimo iniciado, cancela
                    if (System.getProperty("SLOT" + String.valueOf(response.END_O)).equals("3")) {
                      sData.canceladoSlotByPosition(response.END_O, String.valueOf(dpds.RFID));
                      System.setProperty("SLOT" + String.valueOf(response.END_O), "1");
                      logger.info(String.format("SETANDO PROPRIEDADE RESET_RFID%s, valor: %s", String.valueOf(response.END_O), dpds.RFID));
                      System.setProperty("RESET_RFID" + String.valueOf(response.END_O), String.valueOf(dpds.RFID));
  	            }
                    //Avisa o servidor que está bloqueado
                    sData.bloqueiaSlot(response.END_O);
                    System.setProperty("SLOT" + response.END_O, String.valueOf(response.DADOS.getModo()));
//                    System.out.println("Bicicleta bloqueada: Erro" + response.DADOS.getModo());

                    break;
            }
        }

    }

    public boolean verifyUnblock(String originStatus) {
        if (originStatus.equals("10") || originStatus.equals("17") || originStatus.equals("18") || originStatus.equals("19") || originStatus.equals("20")
                || originStatus.equals("21")) {
            return true;
        } else {
            return false;
        }
    }

    public String getHWStatus() {
        String texto = "";

        for (int i = 1; i
                <= (Integer.parseInt(System.getProperty("number_slots"))); i++) {
            texto = texto + System.getProperty("SLOT" + i) + " - ";
        }
        return texto;
    }

    public String getHWStatusHW() {
        String texto = "";
        for (int i = 1; i
                <= (Integer.parseInt(System.getProperty("number_slots"))); i++) {
            texto = texto + System.getProperty("HW" + i) + " - ";
        }
        return texto;
    }

    @Override
    public void run() {
        while (1 == 1) {
            try {
                Thread.sleep(3000);
                getStatus();


            } catch (InterruptedException e) {
            }
        }
    }
}
