/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.model;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 *
 * @author User
 */


@XStreamAlias("slot")
public class Slot {


    @XStreamAlias("id")
    public String Id;

    @XStreamAlias("bike-id")
    public String bikeId;

    @XStreamAlias("bike-rfid")
    public String bikeRFID;

    @XStreamAlias("station-id")
    public String stationId;

    @XStreamOmitField
    public Date createdAt;

        @XStreamOmitField
    public Date updatedAt;

    @XStreamAlias("position")
    public int position;

    @XStreamAlias("status")
    public String status;


             @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
