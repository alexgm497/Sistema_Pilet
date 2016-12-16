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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
 * @author Ariel
 */
@Entity
@Table(name = "solicitud_beca", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitudBeca.findAll", query = "SELECT s FROM SolicitudBeca s"),
    @NamedQuery(name = "SolicitudBeca.findByCodiSoliBeca", query = "SELECT s FROM SolicitudBeca s WHERE s.codiSoliBeca = :codiSoliBeca"),
    @NamedQuery(name = "SolicitudBeca.findByCarnAlum", query = "SELECT s FROM SolicitudBeca s WHERE s.carnAlum = :carnAlum"),
    @NamedQuery(name = "SolicitudBeca.findByNombAlum", query = "SELECT s FROM SolicitudBeca s WHERE s.nombAlum = :nombAlum"),
    @NamedQuery(name = "SolicitudBeca.findByEspeAlum", query = "SELECT s FROM SolicitudBeca s WHERE s.espeAlum = :espeAlum"),
    @NamedQuery(name = "SolicitudBeca.findByGrupAlum", query = "SELECT s FROM SolicitudBeca s WHERE s.grupAlum = :grupAlum"),
    @NamedQuery(name = "SolicitudBeca.findBySeccAcad", query = "SELECT s FROM SolicitudBeca s WHERE s.seccAcad = :seccAcad"),
    @NamedQuery(name = "SolicitudBeca.findBySeccTecn", query = "SELECT s FROM SolicitudBeca s WHERE s.seccTecn = :seccTecn"),
    @NamedQuery(name = "SolicitudBeca.findByFechSoliBeca", query = "SELECT s FROM SolicitudBeca s WHERE s.fechSoliBeca = :fechSoliBeca"),
    @NamedQuery(name = "SolicitudBeca.findByEstaSoliBeca", query = "SELECT s FROM SolicitudBeca s WHERE s.estaSoliBeca = :estaSoliBeca")})
public class SolicitudBeca implements Serializable {

    @Lob
    @Column(name = "foto_alum")
    private byte[] fotoAlum;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_soli_beca")
    private Integer codiSoliBeca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "carn_alum")
    private String carnAlum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nomb_alum")
    private String nombAlum;
    @Size(max = 100)
    @Column(name = "espe_alum")
    private String espeAlum;
    @Size(max = 50)
    @Column(name = "grup_alum")
    private String grupAlum;
    @Size(max = 100)
    @Column(name = "secc_acad")
    private String seccAcad;
    @Size(max = 100)
    @Column(name = "secc_tecn")
    private String seccTecn;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fech_soli_beca")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechSoliBeca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esta_soli_beca")
    private int estaSoliBeca;
    @JoinColumn(name = "codi_empr", referencedColumnName = "codi_empr")
    @ManyToOne(fetch = FetchType.EAGER)
    private Empresa codiEmpr;
    @JoinColumn(name = "codi_grad", referencedColumnName = "codi_grad")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Grado codiGrad;
    
    
    public SolicitudBeca() {
    }

    public SolicitudBeca(Integer codiSoliBeca) {
        this.codiSoliBeca = codiSoliBeca;
    }

    public SolicitudBeca(Integer codiSoliBeca, String carnAlum, String nombAlum, Date fechSoliBeca, int estaSoliBeca) {
        this.codiSoliBeca = codiSoliBeca;
        this.carnAlum = carnAlum;
        this.nombAlum = nombAlum;
        this.fechSoliBeca = fechSoliBeca;
        this.estaSoliBeca = estaSoliBeca;
    }

    public Integer getCodiSoliBeca() {
        return codiSoliBeca;
    }

    public void setCodiSoliBeca(Integer codiSoliBeca) {
        this.codiSoliBeca = codiSoliBeca;
    }

    public String getCarnAlum() {
        return carnAlum;
    }

    public void setCarnAlum(String carnAlum) {
        this.carnAlum = carnAlum;
    }

    public String getNombAlum() {
        return nombAlum;
    }

    public void setNombAlum(String nombAlum) {
        this.nombAlum = nombAlum;
    }

    public byte[] getFotoAlum() {
        return fotoAlum;
    }

    public void setFotoAlum(byte[] fotoAlum) {
        this.fotoAlum = fotoAlum;
    }

    public String getEspeAlum() {
        return espeAlum;
    }

    public void setEspeAlum(String espeAlum) {
        this.espeAlum = espeAlum;
    }

    public String getGrupAlum() {
        return grupAlum;
    }

    public void setGrupAlum(String grupAlum) {
        this.grupAlum = grupAlum;
    }

    public String getSeccAcad() {
        return seccAcad;
    }

    public void setSeccAcad(String seccAcad) {
        this.seccAcad = seccAcad;
    }

    public String getSeccTecn() {
        return seccTecn;
    }

    public void setSeccTecn(String seccTecn) {
        this.seccTecn = seccTecn;
    }

    public Date getFechSoliBeca() {
        return fechSoliBeca;
    }

    public void setFechSoliBeca(Date fechSoliBeca) {
        this.fechSoliBeca = fechSoliBeca;
    }

    public int getEstaSoliBeca() {
        return estaSoliBeca;
    }

    public void setEstaSoliBeca(int estaSoliBeca) {
        this.estaSoliBeca = estaSoliBeca;
    }

    public Empresa getCodiEmpr() {
        return codiEmpr;
    }

    public void setCodiEmpr(Empresa codiEmpr) {
        this.codiEmpr = codiEmpr;
    }

    public Grado getCodiGrad() {
        return codiGrad;
    }

    public void setCodiGrad(Grado codiGrad) {
        this.codiGrad = codiGrad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiSoliBeca != null ? codiSoliBeca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitudBeca)) {
            return false;
        }
        SolicitudBeca other = (SolicitudBeca) object;
        if ((this.codiSoliBeca == null && other.codiSoliBeca != null) || (this.codiSoliBeca != null && !this.codiSoliBeca.equals(other.codiSoliBeca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.SolicitudBeca[ codiSoliBeca=" + codiSoliBeca + " ]";
    }

   
    
}
