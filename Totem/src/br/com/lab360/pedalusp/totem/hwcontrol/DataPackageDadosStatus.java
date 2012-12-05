/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.hwcontrol;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author User
 */
public class DataPackageDadosStatus implements DataPackageDados{
    public byte MODO;
    public byte SENSORES;
    public byte FALHA;
    public String RFID;

     @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int sum() {
        byte[] bRF = RFID.getBytes();
        return FALHA + MODO + bRF[0]+bRF[1]+ bRF[2]+bRF[3]+bRF[4]+bRF[5]+bRF[6]+bRF[7]+bRF[8]+bRF[9] + SENSORES;
    }

    @Override
    public int getModo() {
        return MODO;
    }

    @Override
    public byte[] getBytes() {
        byte[] bRF = RFID.getBytes();
        byte[] bA = {MODO,SENSORES,FALHA,  bRF[0],bRF[1], bRF[2],bRF[3],bRF[4],bRF[5],bRF[6],bRF[7],bRF[8],bRF[9]};
        return bA;
    }
/*
     public static byte[] longToEightBytes(long i, boolean bigEndian) {
        if (bigEndian) {
           byte[] data = new byte[8];
            data[7] = (byte) (i & 0xFF);
            data[6] = (byte) ((i >> 8) & 0xFF);
            data[5] = (byte) ((i >> 16) & 0xFF);
            data[4] = (byte) ((i >> 24) & 0xFF);
            data[3] = (byte) ((i >> 32) & 0xFF);
            data[2] = (byte) ((i >> 40) & 0xFF);
            data[1] = (byte) ((i >> 48) & 0xFF);
            data[0] = (byte) ((i >> 56) & 0xFF);
            return data;

       } else {
           byte[] data = new byte[8];
           data[0] = (byte) (i & 0xFF);
           data[1] = (byte) ((i >> 8) & 0xFF);
           data[2] = (byte) ((i >> 16) & 0xFF);
           data[3] = (byte) ((i >> 24) & 0xFF);
           data[4] = (byte) ((i >> 32) & 0xFF);
           data[5] = (byte) ((i >> 40) & 0xFF);
           data[6] = (byte) ((i >> 48) & 0xFF);
           data[7] = (byte) ((i >> 56) & 0xFF);
           return data;
       }

   }*/

}
