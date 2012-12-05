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
@XStreamAlias("problem")
public class Problem {

    @XStreamAlias("id")
    public int Id;

    @XStreamAlias("problem-number")
    public String problemNumber;

    @XStreamAlias("description")
    public String description;

    @XStreamAlias("title")
    public String title;

    @XStreamAlias("with-number")
    public boolean withNumber;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
