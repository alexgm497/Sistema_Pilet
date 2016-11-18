/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.modelo;

import java.io.Serializable;
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
@Table(name = "Visitante_cita", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Visitantecita.findAll", query = "SELECT v FROM Visitantecita v"),
    @NamedQuery(name = "Visitantecita.findByCodiVisiCita", query = "SELECT v FROM Visitantecita v WHERE v.codiVisiCita = :codiVisiCita"),
    @NamedQuery(name = "Visitantecita.findByCarnAlum", query = "SELECT v FROM Visitantecita v WHERE v.carnAlum = :carnAlum"),
    @NamedQuery(name = "Visitantecita.findByFechLlegCita", query = "SELECT v FROM Visitantecita v WHERE v.fechLlegCita = :fechLlegCita"),
    @NamedQuery(name = "Visitantecita.findByHoraLlegCita", query = "SELECT v FROM Visitantecita v WHERE v.horaLlegCita = :horaLlegCita"),
    @NamedQuery(name = "Visitantecita.findByFechSaliCita", query = "SELECT v FROM Visitantecita v WHERE v.fechSaliCita = :fechSaliCita"),
    @NamedQuery(name = "Visitantecita.findByHoraSaliCita", query = "SELECT v FROM Visitantecita v WHERE v.horaSaliCita = :horaSaliCita"),
    @NamedQuery(name = "Visitantecita.findByEstaVisi", query = "SELECT v FROM Visitantecita v WHERE v.estaVisi = :estaVisi")})
public class Visitantecita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_visi_cita")
    private Integer codiVisiCita;
    @Size(max = 10)
    @Column(name = "carn_alum")
    private String carnAlum;
    @Column(name = "fech_lleg_cita")
    @Temporal(TemporalType.DATE)
    private Date fechLlegCita;
    @Size(max = 8)
    @Column(name = "hora_lleg_cita")
    private String horaLlegCita;
    @Column(name = "fech_sali_cita")
    @Temporal(TemporalType.DATE)
    private Date fechSaliCita;
    @Size(max = 8)
    @Column(name = "hora_sali_cita")
    private String horaSaliCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esta_visi")
    private int estaVisi;
    @JoinColumn(name = "codi_visi", referencedColumnName = "codi_visi")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Visitante codiVisi;
    @JoinColumn(name = "codi_cita", referencedColumnName = "codi_cita")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Cita codiCita;

    public Visitantecita() {
    }

    public Visitantecita(Integer codiVisiCita) {
        this.codiVisiCita = codiVisiCita;
    }

    public Visitantecita(Integer codiVisiCita, int estaVisi) {
        this.codiVisiCita = codiVisiCita;
        this.estaVisi = estaVisi;
    }

    public Integer getCodiVisiCita() {
        return codiVisiCita;
    }

    public void setCodiVisiCita(Integer codiVisiCita) {
        this.codiVisiCita = codiVisiCita;
    }

    public String getCarnAlum() {
        return carnAlum;
    }

    public void setCarnAlum(String carnAlum) {
        this.carnAlum = carnAlum;
    }

    public Date getFechLlegCita() {
        return fechLlegCita;
    }

    public void setFechLlegCita(Date fechLlegCita) {
        this.fechLlegCita = fechLlegCita;
    }

    public String getHoraLlegCita() {
        return horaLlegCita;
    }

    public void setHoraLlegCita(String horaLlegCita) {
        this.horaLlegCita = horaLlegCita;
    }

    public Date getFechSaliCita() {
        return fechSaliCita;
    }

    public void setFechSaliCita(Date fechSaliCita) {
        this.fechSaliCita = fechSaliCita;
    }

    public String getHoraSaliCita() {
        return horaSaliCita;
    }

    public void setHoraSaliCita(String horaSaliCita) {
        this.horaSaliCita = horaSaliCita;
    }

    public int getEstaVisi() {
        return estaVisi;
    }

    public void setEstaVisi(int estaVisi) {
        this.estaVisi = estaVisi;
    }

    public Visitante getCodiVisi() {
        return codiVisi;
    }

    public void setCodiVisi(Visitante codiVisi) {
        this.codiVisi = codiVisi;
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
        hash += (codiVisiCita != null ? codiVisiCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Visitantecita)) {
            return false;
        }
        Visitantecita other = (Visitantecita) object;
        if ((this.codiVisiCita == null && other.codiVisiCita != null) || (this.codiVisiCita != null && !this.codiVisiCita.equals(other.codiVisiCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Visitantecita[ codiVisiCita=" + codiVisiCita + " ]";
    }
    
}
