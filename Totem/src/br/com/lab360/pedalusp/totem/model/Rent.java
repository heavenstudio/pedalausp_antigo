/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lab360.pedalusp.totem.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author User
 */
@XStreamAlias("rent")
public class Rent {

    @XStreamAlias("id")
    public int Id;
    @XStreamAlias("bike-id")
    public int bikeId;
    @XStreamAlias("origin-slot-id")
    public String originSlotId;
    @XStreamAlias("origin-datetime")
    public String originDate;
    @XStreamAlias("return-slot-id")
    public String returnSlotId;
    @XStreamAlias("return-datetime")
    public String returnDate;
    @XStreamAlias("credit")
    public String credit;
    @XStreamAlias("cyclist-id")
    public int cyclistId;
    @XStreamAlias("status")
    public String status;
    @XStreamAlias("request-origin")
    public String requestOrigin;
    @XStreamOmitField
    public Date createdAt;
    @XStreamOmitField
    public Date updatedAt;
    @XStreamAlias("return-station-name")
    public String returnStationName;
    @XStreamAlias("origin-station-name")
    public String originStationName;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
