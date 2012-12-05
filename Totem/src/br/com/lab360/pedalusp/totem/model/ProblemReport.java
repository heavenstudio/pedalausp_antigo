/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lab360.pedalusp.totem.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.Date;

/**
 *
 * @author User
 */
@XStreamAlias("problem-report")
public class ProblemReport {

    @XStreamAlias("problem-id")
    public int problem_id;
    @XStreamAlias("cyclist-id")
    public int cyclist_id;
    @XStreamAlias("station-id")
    public int station_id;
    @XStreamAlias("number-reported")
    public int number_reported;
    @XStreamAlias("created-at")
    public Date createAt;
    @XStreamAlias("updated-at")
    public Date updatedAt;
    @XStreamAlias("id")
    public int id;
}
