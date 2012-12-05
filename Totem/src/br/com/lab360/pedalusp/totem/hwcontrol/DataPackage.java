/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lab360.pedalusp.totem.hwcontrol;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class DataPackage {
	
	private static final Logger logger = Logger.getLogger(DataPackage.class);

    public byte STX = 0x02;
    public byte END_O;
    public byte END_D;
    public byte FUNCAO;
    public byte QTD;
    public DataPackageDados DADOS;
    public byte CKSUM;
    public byte ETX = 0x03;

    public void updateCHECKSUM() {
        if (FUNCAO == 0x01 || FUNCAO == 0x02 || FUNCAO == 0x04 || FUNCAO == 0x05|| FUNCAO == 0x06 || FUNCAO == 0x07) {
            CKSUM = (byte) (STX + END_O + END_D + FUNCAO + QTD);            
        } else{
            CKSUM = (byte) (STX + END_O + END_D + FUNCAO + QTD + DADOS.sum());
        }
    }

    public byte[] dataPackageToByteArray(int number){
        byte[] b = new byte[number];
         b[0] = STX;
         b[1] = END_O;
         b[2] = END_D;
         b[3] = FUNCAO;
         b[4] = QTD;
         int count = 5;

         if(DADOS!= null){             
             if(DADOS.getBytes().length>1){
                 byte[] tempDados = DADOS.getBytes();
                 int tamanho = tempDados.length;
                 for (int u =0; u< tamanho ; u++){

                     b[count] = tempDados[u];
                     count++;
                 }
             }
             //Caso de R_DESTRAVAMENTO
             else{
                 b[count] = DADOS.getBytes()[0];
                 count++;
             }
         }
         b[count] = CKSUM;
         count++;
         b[count] = ETX;
        return b;
    }

    /*
     * Transforma um array de bytes em um objeto DataPackage
     */
    public DataPackage byteToPackage(byte[] bArray){
        this.STX = bArray[0];
        this.END_O = bArray[1];
        this.END_D = bArray[2];
        this.FUNCAO = bArray[3];
        this.QTD = bArray[4];
        int count = 5;
        
        //Caso de DESTRAVAMENTO
        if(this.FUNCAO == 0x30){
            DataPackageDadosDestravamento dpdd = new DataPackageDadosDestravamento();
            dpdd.DADODESTRAVAMENTO = bArray[5];
            this.DADOS = dpdd;
            count++;
        }
        //Caso de STATUS
        if(this.FUNCAO == 0x31){
            DataPackageDadosStatus dpds = new DataPackageDadosStatus();
            dpds.MODO = bArray[count];
            count++;
            dpds.SENSORES = bArray[count];
            count++;
            dpds.FALHA = bArray[count];
            count++;
            byte[] bTemp = {bArray[count],bArray[count+1],bArray[count+2],bArray[count+3],
            bArray[count+4],bArray[count+5],bArray[count+6],bArray[count+7],bArray[count+8],bArray[count+9]};
            dpds.RFID = new String(bTemp);
            count= count+10;
            this.DADOS = dpds;
        }
        this.CKSUM = bArray[count];
        count++;
        this.ETX = bArray[count];
        return this;
    }
    
//Transforma 2 bytes em inteiro
public static int byteArrayToLong(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 8; i++) {
            int shift = (8 - 1 - i) * 8;
            value += (b[i + offset] & 0x00000000000000FF) << shift;
        }
        return value;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

