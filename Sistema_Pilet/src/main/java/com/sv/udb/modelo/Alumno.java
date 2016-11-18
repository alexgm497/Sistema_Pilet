/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.modelo;

/**
 *
 * @author Kevin
 */
public class Alumno {
    private String carnAlum;
    private String nombAlum;
    private String ApelAlum;
    private String nivAlum;
    private String espeAulm;
    private String grupTecn;
    private String grupAcad;
    
    public Alumno() {
    }

    public Alumno(String carnAlum, String nombAlum, String ApelAlum, String nivAlum, String espeAulm, String grupTecn, String grupAcad) {
        this.carnAlum = carnAlum;
        this.nombAlum = nombAlum;
        this.ApelAlum = ApelAlum;
        this.nivAlum = nivAlum;
        this.espeAulm = espeAulm;
        this.grupTecn = grupTecn;
        this.grupAcad = grupAcad;
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

    public String getApelAlum() {
        return ApelAlum;
    }

    public void setApelAlum(String ApelAlum) {
        this.ApelAlum = ApelAlum;
    }

    public String getNivAlum() {
        return nivAlum;
    }

    public void setNivAlum(String nivAlum) {
        this.nivAlum = nivAlum;
    }

    public String getEspeAulm() {
        return espeAulm;
    }

    public void setEspeAulm(String espeAulm) {
        this.espeAulm = espeAulm;
    }

    public String getGrupTecn() {
        return grupTecn;
    }

    public void setGrupTecn(String grupTecn) {
        this.grupTecn = grupTecn;
    }

    public String getGrupAcad() {
        return grupAcad;
    }

    public void setGrupAcad(String grupAcad) {
        this.grupAcad = grupAcad;
    }
    
    
    
}
