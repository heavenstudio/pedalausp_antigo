/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lab360.pedalusp.totem.hwcontrol;

import br.com.lab360.pedalusp.totem.serial.SerialConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author User
 */
public class BikeHWSimulator {

    DataPackage newDP = new DataPackage();
    Timer timer;
    Timer timer2;
    public SerialConnection sConn;

    public BikeHWSimulator(){
        
    }

    public DataPackage sendMessageToHW(DataPackage dp) {
        int qtdadeTotalBytes = 0;
        //TO-DO: VALIDAR CHECKSUM
        //Caso seja verificação de status
        if (dp.FUNCAO == 0x02) {
            System.out.println("PC->HW - Recebe solicitação de status");
            newDP.END_O = dp.END_D;
            newDP.END_D = 0x00;
            newDP.FUNCAO = 0x31;

            DataPackageDadosStatus dpds = new DataPackageDadosStatus();
            dpds.MODO = Byte.parseByte(System.getProperty("HW" + dp.END_D));
            dpds.SENSORES = 0x00;
            dpds.FALHA = 0x00;
            
            if (System.getProperty("BIKE" + dp.END_D) != null && System.getProperty("BIKE" + dp.END_D) != "") {
                dpds.RFID = System.getProperty("BIKE" + dp.END_D);
            } else {
                dpds.RFID = "0000000000";
            }
            newDP.DADOS = dpds;
            newDP.QTD = 0x0D;
            qtdadeTotalBytes = 20;
            newDP.updateCHECKSUM();
        }
        //CONFIRMA DESTRAVAMENTO
        if (dp.FUNCAO == 0x01) {
            System.out.println("PC->HW - Recebe solicitação de destravamento");
            System.setProperty("HW" + dp.END_D, "3");
            System.out.println("HW" + dp.END_D);
            newDP.END_O = dp.END_D;
            newDP.END_D = 0x00;
            newDP.FUNCAO = 0x30;

            DataPackageDadosDestravamento dpdd = new DataPackageDadosDestravamento();
            dpdd.DADODESTRAVAMENTO = 0x00; //Destravamento OK
            newDP.DADOS = dpdd;

            newDP.QTD = 0x01;
            qtdadeTotalBytes = 8;
            newDP.updateCHECKSUM();
            timer = new Timer();
            TimeoutTirarBicicletaDaBaia rt = new TimeoutTirarBicicletaDaBaia();
            rt.nBaia = "HW" + dp.END_D;
            timer.schedule(rt, 50000);

        }

        //CONFIRMA TRAVAMENTO
        if (dp.FUNCAO == 0x04) {
            qtdadeTotalBytes = 8;
            System.setProperty("HW" + dp.END_D, "1");
        }

        //BLOQUEIO
        if (dp.FUNCAO == 0x05) {
            System.out.println("__________________HW RECEBEU BLOQUEIO______________");
            System.setProperty("HW" + dp.END_D, "10");
        }

        //DESBLOQUEIO - POR PADRÃO VAI PARA VAZIA
        if (dp.FUNCAO == 0x06) {
            System.out.println("__________________HW RECEBEU DESBLOQUEIO______________");
            System.out.println(dp.toString());
            System.setProperty("HW" + dp.END_D, "4");
        }


        if(dp.FUNCAO != 0x04 && dp.FUNCAO != 0x05 && dp.FUNCAO != 0x06){
            //CASO DE ENVIO PELA SERIAL
            System.out.println("saindo na serial");
            System.out.println(newDP.toString());
            System.out.println(qtdadeTotalBytes);
            sConn.sendDataWH(newDP, qtdadeTotalBytes);
        }
        //ENVIA O DADO
        /* teste da comunicação serial*/
        return newDP;
    }

    public void testeApertarBotaoDestravar(int HW) {
        System.out.println("Destravar ___________________________BOTAO");
        if (System.getProperty("HW".concat(String.valueOf(HW))).equals("3")) {
            System.out.println("Destravar ___________________________BOTAO2");
            System.setProperty("HW".concat(String.valueOf(HW)), "4");
        }

    }

    public void testeDevolverBicicleta(int HW, String bikeRFID) {
        System.setProperty("HW".concat(String.valueOf(HW)), "5");
        System.setProperty("BIKE".concat(String.valueOf(HW)), bikeRFID);
        System.out.println("____________DEVOLVER_____________");
        System.out.println(System.getProperty("BIKE".concat(String.valueOf(HW))));
    }

    public void testeReservarBicicleta(int HW) {
        System.setProperty("HW".concat(String.valueOf(HW)), "2");
        timer2 = new Timer();
        TimeoutReservaBicicleta trb = new TimeoutReservaBicicleta();
        trb.nBaia = "HW" + HW;
        timer2.schedule(trb, 10000);
    }


    class TimeoutTirarBicicletaDaBaia extends TimerTask {

        public String nBaia;

        public void run() {
            if (System.getProperty(nBaia).equals("3")) {

                //O ciclista demorou para tirar da baia
                System.setProperty(nBaia, "1");
                timer.cancel(); //Terminate the timer thread
            }
        }
    }

    class TimeoutReservaBicicleta extends TimerTask {

        public String nBaia;

        public void run() {
            if (System.getProperty(nBaia).equals("2")) {
                //O ciclista demorou escolher a bicicleta
                System.setProperty(nBaia, "1");
                timer2.cancel(); //Terminate the timer thread
            }
        }
    }
}
