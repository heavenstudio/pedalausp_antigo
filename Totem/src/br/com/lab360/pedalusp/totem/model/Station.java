/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lab360.pedalusp.totem.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.lang.String;
import java.util.ArrayList;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author User
 */
@XStreamAlias("station")
public class Station {

    @XStreamAlias("number-left-slots")
    public int slot_left;
    @XStreamAlias("number-right-slots")
    public int slot_right;
    @XStreamOmitField
    public int created_at;
    @XStreamAlias("description")
    public String description;
    @XStreamAlias("address")
    public String address;
    @XStreamAlias("name")
    public String name;
    @XStreamAlias("id")
    public String id;
    @XStreamOmitField
    public int updated_at;
    @XStreamAlias("status")
    public String status;
    @XStreamAlias("password")
    public String password;
    @XStreamAlias("html")
    public String html;
    public ArrayList<Slot> slotList;


    

    @XStreamAlias("station-number")
    public int stationNumber;

    @XStreamAlias("mapX")
    public int mapX;

    @XStreamAlias("mapY")
    public int mapY;
    
    @XStreamAlias("googleMapX")
    public String googleMapX;

    @XStreamAlias("googleMapY")
    public String googleMapY;

    @XStreamAlias("num-free-slot")
    public int numFreeSlot;
    
    @XStreamAlias("number-slots")
    public int numberSlots;
    
    @XStreamAlias("num-bikes")
    public int numBikes;

    @XStreamAlias("usage")
    public String usage;




    public Station() {
    }

     @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

