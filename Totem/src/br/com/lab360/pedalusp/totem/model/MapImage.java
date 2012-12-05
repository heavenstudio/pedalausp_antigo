/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.lab360.pedalusp.totem.model;

/**
 *
 * @author User
 */
public class MapImage {
    public String mapURL;
    public int numberBike;
    public int numberEmpty;
    
    public MapImage(String mURL, int nBike, int nEmpty){
        mapURL = mURL;
        numberBike = nBike;
        numberEmpty = nEmpty;
    }
}

