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
public class DataPackageDadosDestravamento implements DataPackageDados {

    public byte DADODESTRAVAMENTO;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int sum() {
        return DADODESTRAVAMENTO;
    }

    @Override
    public int getModo() {
        return 0;
    }

    @Override
    public byte[] getBytes() {
        byte[] bA = {DADODESTRAVAMENTO};
        return bA;
    }


}
