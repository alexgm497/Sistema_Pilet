/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ariel
 */
@Entity
@Table(name = "opcion_respuesta", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpcionRespuesta.findAll", query = "SELECT o FROM OpcionRespuesta o"),
    @NamedQuery(name = "OpcionRespuesta.findByCodiOpciResp", query = "SELECT o FROM OpcionRespuesta o WHERE o.codiOpciResp = :codiOpciResp"),
    @NamedQuery(name = "OpcionRespuesta.findByTituOpci", query = "SELECT o FROM OpcionRespuesta o WHERE o.tituOpci = :tituOpci"),
    @NamedQuery(name = "OpcionRespuesta.findByDescOpci", query = "SELECT o FROM OpcionRespuesta o WHERE o.descOpci = :descOpci"),
    @NamedQuery(name = "OpcionRespuesta.findByEstaOpci", query = "SELECT o FROM OpcionRespuesta o WHERE o.estaOpci = :estaOpci")})
public class OpcionRespuesta implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "esta_opci")
    private int estaOpci;
    @OneToMany(mappedBy = "codiOpciResp", fetch = FetchType.EAGER)
    private List<Respuesta> respuestaList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_opci_resp")
    private Integer codiOpciResp;
    @Size(max = 255)
    @Column(name = "titu_opci")
    private String tituOpci;
    @Size(max = 50)
    @Column(name = "desc_opci")
    private String descOpci;
    @JoinColumn(name = "codi_opci", referencedColumnName = "codi_opci")
    @ManyToOne(fetch = FetchType.EAGER)
    private Opcion codiOpci;
    
    

    public OpcionRespuesta() {
    }

    public OpcionRespuesta(Integer codiOpciResp) {
        this.codiOpciResp = codiOpciResp;
    }

    public Integer getCodiOpciResp() {
        return codiOpciResp;
    }

    public void setCodiOpciResp(Integer codiOpciResp) {
        this.codiOpciResp = codiOpciResp;
    }

    public String getTituOpci() {
        return tituOpci;
    }

    public void setTituOpci(String tituOpci) {
        this.tituOpci = tituOpci;
    }

    public String getDescOpci() {
        return descOpci;
    }

    public void setDescOpci(String descOpci) {
        this.descOpci = descOpci;
    }

    public Integer getEstaOpci() {
        return estaOpci;
    }

    public void setEstaOpci(Integer estaOpci) {
        this.estaOpci = estaOpci;
    }

    public Opcion getCodiOpci() {
        return codiOpci;
    }

    public void setCodiOpci(Opcion codiOpci) {
        this.codiOpci = codiOpci;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiOpciResp != null ? codiOpciResp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OpcionRespuesta)) {
            return false;
        }
        OpcionRespuesta other = (OpcionRespuesta) object;
        if ((this.codiOpciResp == null && other.codiOpciResp != null) || (this.codiOpciResp != null && !this.codiOpciResp.equals(other.codiOpciResp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.OpcionRespuesta[ codiOpciResp=" + codiOpciResp + " ]";
    }


    public void setEstaOpci(int estaOpci) {
        this.estaOpci = estaOpci;
    }

    @XmlTransient
    public List<Respuesta> getRespuestaList() {
        return respuestaList;
    }

    public void setRespuestaList(List<Respuesta> respuestaList) {
        this.respuestaList = respuestaList;
    }
    
}
