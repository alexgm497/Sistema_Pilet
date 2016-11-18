/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author Kevin
 */
@Entity
@Table(name = "Visitante", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Visitante.findAll", query = "SELECT v FROM Visitante v"),
    @NamedQuery(name = "Visitante.findByCodiVisi", query = "SELECT v FROM Visitante v WHERE v.codiVisi = :codiVisi"),
    @NamedQuery(name = "Visitante.findByDuiVisi", query = "SELECT v FROM Visitante v WHERE v.duiVisi = :duiVisi"),
    @NamedQuery(name = "Visitante.findByNombVisi", query = "SELECT v FROM Visitante v WHERE v.nombVisi = :nombVisi"),
    @NamedQuery(name = "Visitante.findByApelVisi", query = "SELECT v FROM Visitante v WHERE v.apelVisi = :apelVisi"),
    @NamedQuery(name = "Visitante.findByCorrVisi", query = "SELECT v FROM Visitante v WHERE v.corrVisi = :corrVisi"),
    @NamedQuery(name = "Visitante.findByTeleVisi", query = "SELECT v FROM Visitante v WHERE v.teleVisi = :teleVisi"),
    @NamedQuery(name = "Visitante.findByTipoVisi", query = "SELECT v FROM Visitante v WHERE v.tipoVisi = :tipoVisi"),
    @NamedQuery(name = "Visitante.findByEstaVisi", query = "SELECT v FROM Visitante v WHERE v.estaVisi = :estaVisi")})
public class Visitante implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codiVisi", fetch = FetchType.EAGER)
    private List<Visitantecita> visitantecitaList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_visi")
    private Integer codiVisi;
    @Size(max = 10)
    @Column(name = "dui_visi")
    private String duiVisi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nomb_visi")
    private String nombVisi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "apel_visi")
    private String apelVisi;
    @Size(max = 100)
    @Column(name = "corr_visi")
    private String corrVisi;
    @Size(max = 10)
    @Column(name = "tele_visi")
    private String teleVisi;
    @Column(name = "tipo_visi")
    private Integer tipoVisi;
    @Column(name = "esta_visi")
    private Integer estaVisi;

    public Visitante() {
    }

    public Visitante(Integer codiVisi) {
        this.codiVisi = codiVisi;
    }

    public Visitante(Integer codiVisi, String nombVisi, String apelVisi) {
        this.codiVisi = codiVisi;
        this.nombVisi = nombVisi;
        this.apelVisi = apelVisi;
    }

    public Integer getCodiVisi() {
        return codiVisi;
    }

    public void setCodiVisi(Integer codiVisi) {
        this.codiVisi = codiVisi;
    }

    public String getDuiVisi() {
        return duiVisi;
    }

    public void setDuiVisi(String duiVisi) {
        this.duiVisi = duiVisi;
    }

    public String getNombVisi() {
        return nombVisi;
    }

    public void setNombVisi(String nombVisi) {
        this.nombVisi = nombVisi;
    }

    public String getApelVisi() {
        return apelVisi;
    }

    public void setApelVisi(String apelVisi) {
        this.apelVisi = apelVisi;
    }

    public String getCorrVisi() {
        return corrVisi;
    }

    public void setCorrVisi(String corrVisi) {
        this.corrVisi = corrVisi;
    }

    public String getTeleVisi() {
        return teleVisi;
    }

    public void setTeleVisi(String teleVisi) {
        this.teleVisi = teleVisi;
    }

    public Integer getTipoVisi() {
        return tipoVisi;
    }

    public void setTipoVisi(Integer tipoVisi) {
        this.tipoVisi = tipoVisi;
    }

    public Integer getEstaVisi() {
        return estaVisi;
    }

    public void setEstaVisi(Integer estaVisi) {
        this.estaVisi = estaVisi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiVisi != null ? codiVisi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Visitante)) {
            return false;
        }
        Visitante other = (Visitante) object;
        if ((this.codiVisi == null && other.codiVisi != null) || (this.codiVisi != null && !this.codiVisi.equals(other.codiVisi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Visitante[ codiVisi=" + codiVisi + " ]";
    }

    @XmlTransient
    public List<Visitantecita> getVisitantecitaList() {
        return visitantecitaList;
    }

    public void setVisitantecitaList(List<Visitantecita> visitantecitaList) {
        this.visitantecitaList = visitantecitaList;
    }
    
}
