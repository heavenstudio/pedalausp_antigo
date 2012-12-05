/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author User
 */
@XStreamAlias("rents")
public class Rents {

    @XStreamImplicit(itemFieldName="rent")
    public ArrayList<Rent> rents;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

