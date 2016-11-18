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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kevin
 */
@Entity
@Table(name = "Alumno_visitante", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumnovisitante.findAll", query = "SELECT a FROM Alumnovisitante a ORDER BY a.codiVisi desc, a.carnAlum desc"),
    @NamedQuery(name = "Alumnovisitante.findByCodiAlumVisi", query = "SELECT a FROM Alumnovisitante a WHERE a.codiAlumVisi = :codiAlumVisi"),
    @NamedQuery(name = "Alumnovisitante.findByCarnAlum", query = "SELECT a FROM Alumnovisitante a WHERE a.carnAlum = :carnAlum"),
    @NamedQuery(name = "Alumnovisitante.findByCodiVisiCarnAlum", query = "SELECT a FROM Alumnovisitante a WHERE a.codiVisi = :codiVisi and a.carnAlum = :carnAlum"),
    @NamedQuery(name = "Alumnovisitante.findByPareAlumVisi", query = "SELECT a FROM Alumnovisitante a WHERE a.pareAlumVisi = :pareAlumVisi"),
    @NamedQuery(name = "Alumnovisitante.findByEspeAlumVisi", query = "SELECT a FROM Alumnovisitante a WHERE a.espeAlumVisi = :espeAlumVisi"),
    @NamedQuery(name = "Alumnovisitante.findByEstaAlumVisi", query = "SELECT a FROM Alumnovisitante a WHERE a.estaAlumVisi = :estaAlumVisi"),
    //PERSONALIZADAS
    @NamedQuery(name = "Alumnovisitante.findByCita", query = "SELECT a FROM Alumnovisitante a, Visitantecita vs  WHERE a.codiVisi = vs.codiVisi and vs.codiCita = :codiCita")
    })
public class Alumnovisitante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_alum_visi")
    private Integer codiAlumVisi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "carn_alum")
    private String carnAlum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pare_alum_visi")
    private int pareAlumVisi;
    @Size(max = 80)
    @Column(name = "espe_alum_visi")
    private String espeAlumVisi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esta_alum_visi")
    private int estaAlumVisi;
    @JoinColumn(name = "codi_visi", referencedColumnName = "codi_visi")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Visitante codiVisi;

    public Alumnovisitante() {
    }

    public Alumnovisitante(Integer codiAlumVisi) {
        this.codiAlumVisi = codiAlumVisi;
    }

    public Alumnovisitante(Integer codiAlumVisi, String carnAlum, int pareAlumVisi, int estaAlumVisi) {
        this.codiAlumVisi = codiAlumVisi;
        this.carnAlum = carnAlum;
        this.pareAlumVisi = pareAlumVisi;
        this.estaAlumVisi = estaAlumVisi;
    }

    public Integer getCodiAlumVisi() {
        return codiAlumVisi;
    }

    public void setCodiAlumVisi(Integer codiAlumVisi) {
        this.codiAlumVisi = codiAlumVisi;
    }

    public String getCarnAlum() {
        return carnAlum;
    }

    public void setCarnAlum(String carnAlum) {
        this.carnAlum = carnAlum;
    }

    public int getPareAlumVisi() {
        return pareAlumVisi;
    }

    public void setPareAlumVisi(int pareAlumVisi) {
        this.pareAlumVisi = pareAlumVisi;
    }

    public String getEspeAlumVisi() {
        return espeAlumVisi;
    }

    public void setEspeAlumVisi(String espeAlumVisi) {
        this.espeAlumVisi = espeAlumVisi;
    }

    public int getEstaAlumVisi() {
        return estaAlumVisi;
    }

    public void setEstaAlumVisi(int estaAlumVisi) {
        this.estaAlumVisi = estaAlumVisi;
    }

    public Visitante getCodiVisi() {
        return codiVisi;
    }

    public void setCodiVisi(Visitante codiVisi) {
        this.codiVisi = codiVisi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiAlumVisi != null ? codiAlumVisi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumnovisitante)) {
            return false;
        }
        Alumnovisitante other = (Alumnovisitante) object;
        if ((this.codiAlumVisi == null && other.codiAlumVisi != null) || (this.codiAlumVisi != null && !this.codiAlumVisi.equals(other.codiAlumVisi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Alumnovisitante[ codiAlumVisi=" + codiAlumVisi + " ]";
    }
    
}
