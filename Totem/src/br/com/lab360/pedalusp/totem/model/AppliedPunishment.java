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

@XStreamAlias("applied-punishment")
public class AppliedPunishment {


    @XStreamAlias("id")
    public String Id;

    @XStreamAlias("punishment-id")
    public String punishmentId;

    @XStreamAlias("cyclist-id")
    public String cyclistId;

    @XStreamAlias("applied-at")
    public String appliedAt;

    @XStreamAlias("expires")
    public String expires;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
