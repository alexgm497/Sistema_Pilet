/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils.pojos;

/**
 *
 * @author Mauricio
 */
public class DatosAlumnos {
    private String carn;
    private String nomb;
    private byte[] foto;
    private String grad;
    private String espe;
    private String grup;
    private String seccAcad;
    private String seccTecn;
    private int esta;

    public String getCarn() {
        return carn;
    }

    public void setCarn(String carn) {
        this.carn = carn;
    }

    public String getNomb() {
        return nomb;
    }

    public void setNomb(String nomb) {
        this.nomb = nomb;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getEspe() {
        return espe;
    }

    public void setEspe(String espe) {
        this.espe = espe;
    }

    public String getGrup() {
        return grup;
    }

    public void setGrup(String grup) {
        this.grup = grup;
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

    public int getEsta() {
        return esta;
    }

    public void setEsta(int esta) {
        this.esta = esta;
    }
}
