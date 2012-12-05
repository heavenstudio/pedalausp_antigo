/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author User
 */

@XStreamAlias("punishment")
public class Punishment {

    @XStreamAlias("id")
    public String Id;

    @XStreamAlias("applied-type")
    public String appliedType;

    @XStreamAlias("trigger-amount")
    public String triggerAmount;

    @XStreamAlias("applied-amount")
    public String appliedAmount;

    @XStreamAlias("description")
    public String description;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
