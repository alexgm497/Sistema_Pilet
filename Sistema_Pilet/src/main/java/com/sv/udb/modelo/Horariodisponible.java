/*
*Modelo controlador Horariodisponible
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
 * @author Sistema de citas
 * @version 1.0 13 de Octubre de 2016
 */
@Entity
@Table(name = "Horario_disponible", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Horariodisponible.findAll", query = "SELECT h FROM Horariodisponible h"),
    @NamedQuery(name = "Horariodisponible.findByCodiHoraDisp", query = "SELECT h FROM Horariodisponible h WHERE h.codiHoraDisp = :codiHoraDisp"),
    @NamedQuery(name = "Horariodisponible.findByCodiUsua", query = "SELECT h FROM Horariodisponible h WHERE h.codiUsua = :codiUsua"),
    @NamedQuery(name = "Horariodisponible.findByDiaHoraDisp", query = "SELECT h FROM Horariodisponible h WHERE h.diaHoraDisp = :diaHoraDisp"),
    @NamedQuery(name = "Horariodisponible.findByHoraInicHoraDisp", query = "SELECT h FROM Horariodisponible h WHERE h.horaInicHoraDisp = :horaInicHoraDisp"),
    @NamedQuery(name = "Horariodisponible.findByHoraFinaHoraDisp", query = "SELECT h FROM Horariodisponible h WHERE h.horaFinaHoraDisp = :horaFinaHoraDisp"),
    @NamedQuery(name = "Horariodisponible.findByAnioHoraDisp", query = "SELECT h FROM Horariodisponible h WHERE h.anioHoraDisp = :anioHoraDisp"),
    @NamedQuery(name = "Horariodisponible.findByEstaHoraDisp", query = "SELECT h FROM Horariodisponible h WHERE h.estaHoraDisp = :estaHoraDisp")})
  /**
   * Clase publica Horariodisponible
   */
public class Horariodisponible implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_hora_disp")
    private Integer codiHoraDisp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codi_usua")
    private int codiUsua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "dia_hora_disp")
    private String diaHoraDisp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "hora_inic_hora_disp")
    private String horaInicHoraDisp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "hora_fina_hora_disp")
    private String horaFinaHoraDisp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "anio_hora_disp")
    private String anioHoraDisp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esta_hora_disp")
    private int estaHoraDisp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codiHoraDisp", fetch = FetchType.EAGER)
    private List<Excepcionhorariodisponible> excepcionhorariodisponibleList;
  /**
   * Método constructor Horariodisponible
   */
    public Horariodisponible() {
    }
  /**
   * Método constructor
   * registro para el horario disponible
   */
    public Horariodisponible(Integer codiHoraDisp) {
        this.codiHoraDisp = codiHoraDisp;
    }

    public Horariodisponible(Integer codiHoraDisp, int codiUsua, String diaHoraDisp, String horaInicHoraDisp, String horaFinaHoraDisp, String anioHoraDisp, int estaHoraDisp) {
        this.codiHoraDisp = codiHoraDisp;
        this.codiUsua = codiUsua;
        this.diaHoraDisp = diaHoraDisp;
        this.horaInicHoraDisp = horaInicHoraDisp;
        this.horaFinaHoraDisp = horaFinaHoraDisp;
        this.anioHoraDisp = anioHoraDisp;
        this.estaHoraDisp = estaHoraDisp;
    }

    public Integer getCodiHoraDisp() {
        return codiHoraDisp;
    }

    public void setCodiHoraDisp(Integer codiHoraDisp) {
        this.codiHoraDisp = codiHoraDisp;
    }

    public int getCodiUsua() {
        return codiUsua;
    }

    public void setCodiUsua(int codiUsua) {
        this.codiUsua = codiUsua;
    }

    public String getDiaHoraDisp() {
        return diaHoraDisp;
    }

    public void setDiaHoraDisp(String diaHoraDisp) {
        this.diaHoraDisp = diaHoraDisp;
    }

    public String getHoraInicHoraDisp() {
        return horaInicHoraDisp;
    }

    public void setHoraInicHoraDisp(String horaInicHoraDisp) {
        this.horaInicHoraDisp = horaInicHoraDisp;
    }

    public String getHoraFinaHoraDisp() {
        return horaFinaHoraDisp;
    }

    public void setHoraFinaHoraDisp(String horaFinaHoraDisp) {
        this.horaFinaHoraDisp = horaFinaHoraDisp;
    }

    public String getAnioHoraDisp() {
        return anioHoraDisp;
    }

    public void setAnioHoraDisp(String anioHoraDisp) {
        this.anioHoraDisp = anioHoraDisp;
    }

    public int getEstaHoraDisp() {
        return estaHoraDisp;
    }

    public void setEstaHoraDisp(int estaHoraDisp) {
        this.estaHoraDisp = estaHoraDisp;
    }

    @XmlTransient
    public List<Excepcionhorariodisponible> getExcepcionhorariodisponibleList() {
        return excepcionhorariodisponibleList;
    }

    public void setExcepcionhorariodisponibleList(List<Excepcionhorariodisponible> excepcionhorariodisponibleList) {
        this.excepcionhorariodisponibleList = excepcionhorariodisponibleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiHoraDisp != null ? codiHoraDisp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horariodisponible)) {
            return false;
        }
        Horariodisponible other = (Horariodisponible) object;
        if ((this.codiHoraDisp == null && other.codiHoraDisp != null) || (this.codiHoraDisp != null && !this.codiHoraDisp.equals(other.codiHoraDisp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Horariodisponible[ codiHoraDisp=" + codiHoraDisp + " ]";
    }
    
}
