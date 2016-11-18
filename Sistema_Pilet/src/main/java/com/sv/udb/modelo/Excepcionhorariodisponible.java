/*
 * Modelo controlador de excepcion horario disponible
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
 * @author Sistema de citas
 * @version 1.0 13 de Octubre de 2016
 */
@Entity
@Table(name = "Excepcion_horario_disponible", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Excepcionhorariodisponible.findAll", query = "SELECT e FROM Excepcionhorariodisponible e"),
    @NamedQuery(name = "Excepcionhorariodisponible.findByCodiExceHoraDisp", query = "SELECT e FROM Excepcionhorariodisponible e WHERE e.codiExceHoraDisp = :codiExceHoraDisp"),
    @NamedQuery(name = "Excepcionhorariodisponible.findByFechExceHoraDisp", query = "SELECT e FROM Excepcionhorariodisponible e WHERE e.fechExceHoraDisp = :fechExceHoraDisp"),
    @NamedQuery(name = "Excepcionhorariodisponible.findByRazoExceHoraDisp", query = "SELECT e FROM Excepcionhorariodisponible e WHERE e.razoExceHoraDisp = :razoExceHoraDisp")})
  /**
   * Clase publica Excepcionhorariodisponible
   */
public class Excepcionhorariodisponible implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_exce_hora_disp")
    private Integer codiExceHoraDisp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fech_exce_hora_disp")
    @Temporal(TemporalType.DATE)
    private Date fechExceHoraDisp;
    @Size(max = 100)
    @Column(name = "razo_exce_hora_disp")
    private String razoExceHoraDisp;
    @JoinColumn(name = "codi_hora_disp", referencedColumnName = "codi_hora_disp")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Horariodisponible codiHoraDisp;
  /**
   * Método constructor Excepcionhorariodisponible
   */
    public Excepcionhorariodisponible() {
    }
  /**
   * Método constructor
   *  registro de la excepcion del horario disponible
   */
    public Excepcionhorariodisponible(Integer codiExceHoraDisp) {
        this.codiExceHoraDisp = codiExceHoraDisp;
    }

    public Excepcionhorariodisponible(Integer codiExceHoraDisp, Date fechExceHoraDisp) {
        this.codiExceHoraDisp = codiExceHoraDisp;
        this.fechExceHoraDisp = fechExceHoraDisp;
    }

    public Integer getCodiExceHoraDisp() {
        return codiExceHoraDisp;
    }

    public void setCodiExceHoraDisp(Integer codiExceHoraDisp) {
        this.codiExceHoraDisp = codiExceHoraDisp;
    }

    public Date getFechExceHoraDisp() {
        return fechExceHoraDisp;
    }

    public void setFechExceHoraDisp(Date fechExceHoraDisp) {
        this.fechExceHoraDisp = fechExceHoraDisp;
    }

    public String getRazoExceHoraDisp() {
        return razoExceHoraDisp;
    }

    public void setRazoExceHoraDisp(String razoExceHoraDisp) {
        this.razoExceHoraDisp = razoExceHoraDisp;
    }

    public Horariodisponible getCodiHoraDisp() {
        return codiHoraDisp;
    }

    public void setCodiHoraDisp(Horariodisponible codiHoraDisp) {
        this.codiHoraDisp = codiHoraDisp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiExceHoraDisp != null ? codiExceHoraDisp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Excepcionhorariodisponible)) {
            return false;
        }
        Excepcionhorariodisponible other = (Excepcionhorariodisponible) object;
        if ((this.codiExceHoraDisp == null && other.codiExceHoraDisp != null) || (this.codiExceHoraDisp != null && !this.codiExceHoraDisp.equals(other.codiExceHoraDisp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Excepcionhorariodisponible[ codiExceHoraDisp=" + codiExceHoraDisp + " ]";
    }
    
}
