/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kevin
 */
@Entity
@Table(name = "Cambio_cita", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cambiocita.findAll", query = "SELECT c FROM Cambiocita c"),
    @NamedQuery(name = "Cambiocita.findByCodiCambCita", query = "SELECT c FROM Cambiocita c WHERE c.codiCambCita = :codiCambCita"),
    @NamedQuery(name = "Cambiocita.findByFechCambCita", query = "SELECT c FROM Cambiocita c WHERE c.fechCambCita = :fechCambCita"),
    @NamedQuery(name = "Cambiocita.findByHoraCambCita", query = "SELECT c FROM Cambiocita c WHERE c.horaCambCita = :horaCambCita"),
    @NamedQuery(name = "Cambiocita.findByFechInicCitaNuev", query = "SELECT c FROM Cambiocita c WHERE c.fechInicCitaNuev = :fechInicCitaNuev"),
    @NamedQuery(name = "Cambiocita.findByHoraInicCitaNuev", query = "SELECT c FROM Cambiocita c WHERE c.horaInicCitaNuev = :horaInicCitaNuev"),
    @NamedQuery(name = "Cambiocita.findByFechFinCitaNuev", query = "SELECT c FROM Cambiocita c WHERE c.fechFinCitaNuev = :fechFinCitaNuev"),
    @NamedQuery(name = "Cambiocita.findByHoraFinCitaNuev", query = "SELECT c FROM Cambiocita c WHERE c.horaFinCitaNuev = :horaFinCitaNuev"),
    @NamedQuery(name = "Cambiocita.findByMotiCambCita", query = "SELECT c FROM Cambiocita c WHERE c.motiCambCita = :motiCambCita"),
    @NamedQuery(name = "Cambiocita.findByEstaCambCita", query = "SELECT c FROM Cambiocita c WHERE c.estaCambCita = :estaCambCita"),
    //CUSTOM
    @NamedQuery(name = "Cambiocita.findByCodiCita", query = "SELECT c FROM Cambiocita c WHERE c.codiCita = :codiCita ORDER BY c.fechCambCita desc, c.horaCambCita desc")})
public class Cambiocita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_camb_cita")
    private Integer codiCambCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fech_camb_cita")
    @Temporal(TemporalType.DATE)
    private Date fechCambCita;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "hora_camb_cita")
    private String horaCambCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fech_inic_cita_nuev")
    @Temporal(TemporalType.DATE)
    private Date fechInicCitaNuev;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "hora_inic_cita_nuev")
    private String horaInicCitaNuev;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fech_fin_cita_nuev")
    @Temporal(TemporalType.DATE)
    private Date fechFinCitaNuev;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "hora_fin_cita_nuev")
    private String horaFinCitaNuev;
    @Size(max = 500)
    @Column(name = "moti_camb_cita")
    private String motiCambCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esta_camb_cita")
    private int estaCambCita;
    @JoinColumn(name = "codi_cita", referencedColumnName = "codi_cita")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Cita codiCita;

    public Cambiocita() {
    }

    public Cambiocita(Integer codiCambCita) {
        this.codiCambCita = codiCambCita;
    }

    public Cambiocita(Integer codiCambCita, Date fechCambCita, String horaCambCita, Date fechInicCitaNuev, String horaInicCitaNuev, Date fechFinCitaNuev, String horaFinCitaNuev, int estaCambCita) {
        this.codiCambCita = codiCambCita;
        this.fechCambCita = fechCambCita;
        this.horaCambCita = horaCambCita;
        this.fechInicCitaNuev = fechInicCitaNuev;
        this.horaInicCitaNuev = horaInicCitaNuev;
        this.fechFinCitaNuev = fechFinCitaNuev;
        this.horaFinCitaNuev = horaFinCitaNuev;
        this.estaCambCita = estaCambCita;
    }

    public Integer getCodiCambCita() {
        return codiCambCita;
    }

    public void setCodiCambCita(Integer codiCambCita) {
        this.codiCambCita = codiCambCita;
    }

    public Date getFechCambCita() {
        return fechCambCita;
    }

    public void setFechCambCita(Date fechCambCita) {
        this.fechCambCita = fechCambCita;
    }

    public String getHoraCambCita() {
        return horaCambCita;
    }

    public void setHoraCambCita(String horaCambCita) {
        this.horaCambCita = horaCambCita;
    }

    public Date getFechInicCitaNuev() {
        return fechInicCitaNuev;
    }

    
    public void setFechInicCitaNuev(Date fechInicCitaNuev) {
        this.fechInicCitaNuev = fechInicCitaNuev;
    }

    public String getHoraInicCitaNuev() {
        return horaInicCitaNuev;
    }

    public void setHoraInicCitaNuev(String horaInicCitaNuev) {
        this.horaInicCitaNuev = horaInicCitaNuev;
    }

    public Date getFechFinCitaNuev() {
        return fechFinCitaNuev;
    }

    public void setFechFinCitaNuev(Date fechFinCitaNuev) {
        this.fechFinCitaNuev = fechFinCitaNuev;
    }

    public String getHoraFinCitaNuev() {
        return horaFinCitaNuev;
    }

    public void setHoraFinCitaNuev(String horaFinCitaNuev) {
        this.horaFinCitaNuev = horaFinCitaNuev;
    }

    public String getMotiCambCita() {
        return motiCambCita;
    }

    public void setMotiCambCita(String motiCambCita) {
        this.motiCambCita = motiCambCita;
    }

    public int getEstaCambCita() {
        return estaCambCita;
    }

    public void setEstaCambCita(int estaCambCita) {
        this.estaCambCita = estaCambCita;
    }

    public Cita getCodiCita() {
        return codiCita;
    }

    public void setCodiCita(Cita codiCita) {
        this.codiCita = codiCita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiCambCita != null ? codiCambCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cambiocita)) {
            return false;
        }
        Cambiocita other = (Cambiocita) object;
        if ((this.codiCambCita == null && other.codiCambCita != null) || (this.codiCambCita != null && !this.codiCambCita.equals(other.codiCambCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Cambiocita[ codiCambCita=" + codiCambCita + " ]";
    }
    
}
