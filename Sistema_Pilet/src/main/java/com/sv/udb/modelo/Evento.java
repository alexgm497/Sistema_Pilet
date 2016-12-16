/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.modelo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Kevin
 */
@Entity
@Table(name = "Evento", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e"),
    @NamedQuery(name = "Evento.findByCodiEvent", query = "SELECT e FROM Evento e WHERE e.codiEvent = :codiEvent"),
    @NamedQuery(name = "Evento.findByNombEven", query = "SELECT e FROM Evento e WHERE e.nombEven = :nombEven"),
    @NamedQuery(name = "Evento.findByFechInicEven", query = "SELECT e FROM Evento e WHERE e.fechInicEven = :fechInicEven"),
    @NamedQuery(name = "Evento.findByFechFinaEven", query = "SELECT e FROM Evento e WHERE e.fechFinaEven = :fechFinaEven"),
    @NamedQuery(name = "Evento.findByHoraInicEven", query = "SELECT e FROM Evento e WHERE e.horaInicEven = :horaInicEven"),
    @NamedQuery(name = "Evento.findByHoraFinaEven", query = "SELECT e FROM Evento e WHERE e.horaFinaEven = :horaFinaEven"),
    @NamedQuery(name = "Evento.findByEstaEven", query = "SELECT e FROM Evento e WHERE e.estaEven = :estaEven")})
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_event")
    private Integer codiEvent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nomb_even")
    private String nombEven;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fech_inic_even")
    @Temporal(TemporalType.DATE)
    private Date fechInicEven;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fech_fina_even")
    @Temporal(TemporalType.DATE)
    private Date fechFinaEven;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "hora_inic_even")
    private String horaInicEven;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "hora_fina_even")
    private String horaFinaEven;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esta_even")
    private int estaEven;
    @OneToMany(mappedBy = "codiEven", fetch = FetchType.EAGER)
    private List<Cita> citaList;
    @JoinColumn(name = "codi_ubic", referencedColumnName = "codi_ubic")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Ubicaciones codiUbic;

    public Evento() {
    }

    public Evento(Integer codiEvent) {
        this.codiEvent = codiEvent;
    }

    public Evento(Integer codiEvent, String nombEven, Date fechInicEven, Date fechFinaEven, String horaInicEven, String horaFinaEven, int estaEven) {
        this.codiEvent = codiEvent;
        this.nombEven = nombEven;
        this.fechInicEven = fechInicEven;
        this.fechFinaEven = fechFinaEven;
        this.horaInicEven = horaInicEven;
        this.horaFinaEven = horaFinaEven;
        this.estaEven = estaEven;
    }

    public Integer getCodiEvent() {
        return codiEvent;
    }

    public void setCodiEvent(Integer codiEvent) {
        this.codiEvent = codiEvent;
    }

    public String getNombEven() {
        return nombEven;
    }

    public void setNombEven(String nombEven) {
        this.nombEven = nombEven;
    }

    public Date getFechInicEven() {
        return fechInicEven;
    }

    public void setFechInicEven(Date fechInicEven) {
        this.fechInicEven = fechInicEven;
    }

    public Date getFechFinaEven() {
        return fechFinaEven;
    }

    public void setFechFinaEven(Date fechFinaEven) {
        this.fechFinaEven = fechFinaEven;
    }

    public String getHoraInicEven() {
        return horaInicEven;
    }

    public void setHoraInicEven(String horaInicEven) {
        this.horaInicEven = horaInicEven;
    }

    public String getHoraFinaEven() {
        return horaFinaEven;
    }

    public void setHoraFinaEven(String horaFinaEven) {
        this.horaFinaEven = horaFinaEven;
    }

    public int getEstaEven() {
        return estaEven;
    }

    public void setEstaEven(int estaEven) {
        this.estaEven = estaEven;
    }

    @XmlTransient
    public List<Cita> getCitaList() {
        return citaList;
    }

    public void setCitaList(List<Cita> citaList) {
        this.citaList = citaList;
    }

    public Ubicaciones getCodiUbic() {
        return codiUbic;
    }

    public void setCodiUbic(Ubicaciones codiUbic) {
        this.codiUbic = codiUbic;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiEvent != null ? codiEvent.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.codiEvent == null && other.codiEvent != null) || (this.codiEvent != null && !this.codiEvent.equals(other.codiEvent))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Evento[ codiEvent=" + codiEvent + " ]";
    }
    
}
