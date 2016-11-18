/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aleso
 */
@Entity
@Table(name = "usuario", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByCodiUsua", query = "SELECT u FROM Usuario u WHERE u.codiUsua = :codiUsua"),
    @NamedQuery(name = "Usuario.findByAcceUsua", query = "SELECT u FROM Usuario u WHERE u.acceUsua = :acceUsua"),
    @NamedQuery(name = "Usuario.findByTipoUsua", query = "SELECT u FROM Usuario u WHERE u.tipoUsua = :tipoUsua"),
    @NamedQuery(name = "Usuario.findByEstaUsua", query = "SELECT u FROM Usuario u WHERE u.estaUsua = :estaUsua")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_usua")
    private Integer codiUsua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "acce_usua")
    private String acceUsua;
    @Size(max = 50)
    @Column(name = "tipo_usua")
    private String tipoUsua;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esta_usua")
    private int estaUsua;

    public Usuario() {
    }

    public Usuario(Integer codiUsua) {
        this.codiUsua = codiUsua;
    }

    public Usuario(Integer codiUsua, String acceUsua, int estaUsua) {
        this.codiUsua = codiUsua;
        this.acceUsua = acceUsua;
        this.estaUsua = estaUsua;
    }

    public Integer getCodiUsua() {
        return codiUsua;
    }

    public void setCodiUsua(Integer codiUsua) {
        this.codiUsua = codiUsua;
    }

    public String getAcceUsua() {
        return acceUsua;
    }

    public void setAcceUsua(String acceUsua) {
        this.acceUsua = acceUsua;
    }

    public String getTipoUsua() {
        return tipoUsua;
    }

    public void setTipoUsua(String tipoUsua) {
        this.tipoUsua = tipoUsua;
    }

    public int getEstaUsua() {
        return estaUsua;
    }

    public void setEstaUsua(int estaUsua) {
        this.estaUsua = estaUsua;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiUsua != null ? codiUsua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.codiUsua == null && other.codiUsua != null) || (this.codiUsua != null && !this.codiUsua.equals(other.codiUsua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Usuario[ codiUsua=" + codiUsua + " ]";
    }
    
}
