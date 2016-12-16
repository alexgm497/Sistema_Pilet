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
 * @author Kevin
 */
@Entity
@Table(name = "Cita", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c"),
    @NamedQuery(name = "Cita.findByCodiCita", query = "SELECT c FROM Cita c WHERE c.codiCita = :codiCita"),
    @NamedQuery(name = "Cita.findByCodiUsua", query = "SELECT c FROM Cita c WHERE c.codiUsua = :codiUsua"),
    @NamedQuery(name = "Cita.findByTipoCita", query = "SELECT c FROM Cita c WHERE c.tipoCita = :tipoCita"),
    @NamedQuery(name = "Cita.findByTipoVisi", query = "SELECT c FROM Cita c WHERE c.tipoVisi = :tipoVisi"),
    @NamedQuery(name = "Cita.findByTipoDura", query = "SELECT c FROM Cita c WHERE c.tipoDura = :tipoDura"),
    @NamedQuery(name = "Cita.findByDescCita", query = "SELECT c FROM Cita c WHERE c.descCita = :descCita"),
    @NamedQuery(name = "Cita.findByEstaCita", query = "SELECT c FROM Cita c WHERE c.estaCita = :estaCita"),
    @NamedQuery(name = "Cita.findByNombGrupCita", query = "SELECT c FROM Cita c WHERE c.nombGrupCita = :nombGrupCita"),
    @NamedQuery(name = "Cita.findByCantGrupCita", query = "SELECT c FROM Cita c WHERE c.cantGrupCita = :cantGrupCita")})
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_cita")
    private Integer codiCita;
    @Column(name = "codi_usua")
    private Integer codiUsua;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_cita")
    private int tipoCita;
    @Column(name = "tipo_visi")
    private Integer tipoVisi;
    @Column(name = "tipo_dura")
    private Integer tipoDura;
    @Size(max = 500)
    @Column(name = "desc_cita")
    private String descCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esta_cita")
    private int estaCita;
    @Size(max = 100)
    @Column(name = "nomb_grup_cita")
    private String nombGrupCita;
    @Column(name = "cant_grup_cita")
    private Integer cantGrupCita;
    @JoinColumn(name = "codi_even", referencedColumnName = "codi_event")
    @ManyToOne(fetch = FetchType.EAGER)
    private Evento codiEven;
    @JoinColumn(name = "codi_ubic", referencedColumnName = "codi_ubic")
    @ManyToOne(fetch = FetchType.EAGER)
    private Ubicaciones codiUbic;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codiCita", fetch = FetchType.EAGER)
    private List<Cambiocita> cambiocitaList;

    public Cita() {
    }

    public Cita(Integer codiCita) {
        this.codiCita = codiCita;
    }

    public Cita(Integer codiCita, int tipoCita, int estaCita) {
        this.codiCita = codiCita;
        this.tipoCita = tipoCita;
        this.estaCita = estaCita;
    }

    public Integer getCodiCita() {
        return codiCita;
    }

    public void setCodiCita(Integer codiCita) {
        this.codiCita = codiCita;
    }

    public Integer getCodiUsua() {
        return codiUsua;
    }

    public void setCodiUsua(Integer codiUsua) {
        this.codiUsua = codiUsua;
    }

    public int getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(int tipoCita) {
        this.tipoCita = tipoCita;
    }

    public Integer getTipoVisi() {
        return tipoVisi;
    }

    public void setTipoVisi(Integer tipoVisi) {
        this.tipoVisi = tipoVisi;
    }

    public Integer getTipoDura() {
        return tipoDura;
    }

    public void setTipoDura(Integer tipoDura) {
        this.tipoDura = tipoDura;
    }

    public String getDescCita() {
        return descCita;
    }

    public void setDescCita(String descCita) {
        this.descCita = descCita;
    }

    public int getEstaCita() {
        return estaCita;
    }

    public void setEstaCita(int estaCita) {
        this.estaCita = estaCita;
    }

    public String getNombGrupCita() {
        return nombGrupCita;
    }

    public void setNombGrupCita(String nombGrupCita) {
        this.nombGrupCita = nombGrupCita;
    }

    public Integer getCantGrupCita() {
        return cantGrupCita;
    }

    public void setCantGrupCita(Integer cantGrupCita) {
        this.cantGrupCita = cantGrupCita;
    }

    public Evento getCodiEven() {
        return codiEven;
    }

    public void setCodiEven(Evento codiEven) {
        this.codiEven = codiEven;
    }

    public Ubicaciones getCodiUbic() {
        return codiUbic;
    }

    public void setCodiUbic(Ubicaciones codiUbic) {
        this.codiUbic = codiUbic;
    }

    @XmlTransient
    public List<Cambiocita> getCambiocitaList() {
        return cambiocitaList;
    }

    public void setCambiocitaList(List<Cambiocita> cambiocitaList) {
        this.cambiocitaList = cambiocitaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiCita != null ? codiCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cita)) {
            return false;
        }
        Cita other = (Cita) object;
        if ((this.codiCita == null && other.codiCita != null) || (this.codiCita != null && !this.codiCita.equals(other.codiCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Cita[ codiCita=" + codiCita + " ]";
    }
    
}
