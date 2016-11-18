/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils.pojos;

import java.util.List;

/**
 *
 * @author Alvin
 */
public class WSconsEmplByUser {
    private boolean resp;
    private int codi;
    private String nomb;
    private byte[] foto;
    private String mail;
    private String tipo;
    private String grupGuia;
    private int esta;
    private List<DatosGrupos> grup;
    
    public WSconsEmplByUser() {
        
    }

    public WSconsEmplByUser(boolean resp, int codi, String nomb, byte[] foto, String mail, String tipo, String grupGuia, int esta, List<DatosGrupos> grup) {
        this.resp = resp;
        this.codi = codi;
        this.nomb = nomb;
        this.foto = foto;
        this.mail = mail;
        this.tipo = tipo;
        this.grupGuia = grupGuia;
        this.esta = esta;
        this.grup = grup;
    }
    
    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }    

    public boolean isResp() {
        return resp;
    }

    public void setResp(boolean resp) {
        this.resp = resp;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGrupGuia() {
        return grupGuia;
    }

    public void setGrupGuia(String grupGuia) {
        this.grupGuia = grupGuia;
    }

    public int getEsta() {
        return esta;
    }

    public void setEsta(int esta) {
        this.esta = esta;
    }

    public List<DatosGrupos> getGrup() {
        return grup;
    }

    public void setGrup(List<DatosGrupos> grup) {
        this.grup = grup;
    }
}
