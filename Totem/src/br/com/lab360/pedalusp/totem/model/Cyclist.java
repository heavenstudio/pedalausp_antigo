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
@XStreamAlias("cyclist")
public class Cyclist {

    @XStreamAlias("id")
    public int Id;
    @XStreamAlias("email")
    public String email;
    @XStreamAlias("password")
    public int password;
    @XStreamAlias("active")
    public boolean active;
    @XStreamAlias("status")
    public String status;
    @XStreamAlias("name")
    public String name;
    @XStreamAlias("nusp")
    public int nusp;
    @XStreamAlias("cpf")
    public String cpf;
    @XStreamAlias("ddd-mobile")
    public String ddd_mobile;
    @XStreamAlias("mobile-number")
    public String mobile_number;
    @XStreamAlias("max-bikes")
    public int max_bikes;
    @XStreamAlias("gender")
    public String gender;
    @XStreamAlias("rfid")
    public String rfid;
    @XStreamAlias("ddd-Phone")
    public String ddd_Phone;
    @XStreamAlias("phone-number")
    public String phone_number;
    @XStreamAlias("credit")
    public int credit;
    @XStreamAlias("adr-logradouro")
    public String adr_logradouro;
    @XStreamAlias("adr-number")
    public String adr_number;
    @XStreamAlias("adr-city")
    public String adr_city;
    @XStreamAlias("adr-state")
    public String adr_state;
    @XStreamAlias("adr-cep")
    public String adr_cep;
    @XStreamAlias("birthday")
    public String birthday;
    @XStreamAlias("college")
    public String college;
    @XStreamAlias("status-USP")
    public String status_USP;
    @XStreamAlias("vinculo")
    public String vinculo;
    @XStreamAlias("adr-complemento")
    public String adr_complemento;

      @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
