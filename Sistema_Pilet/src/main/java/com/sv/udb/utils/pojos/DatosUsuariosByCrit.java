/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils.pojos;

/**
 *
 * @author Alvin
 */
public class DatosUsuariosByCrit {
    private String nomb;
    private String apel;
    private byte[] foto;
    private String usua;
    private String tipo;
    
    public DatosUsuariosByCrit() {
        
    }

    public DatosUsuariosByCrit(String nomb, String apel, byte[] foto, String tipo) {
        this.nomb = nomb;
        this.apel = apel;
        this.foto = foto;
        this.tipo = tipo;
    }

    public String getNomb() {
        return nomb;
    }

    public void setNomb(String nomb) {
        this.nomb = nomb;
    }

    public String getApel() {
        return apel;
    }

    public void setApel(String apel) {
        this.apel = apel;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getUsua() {
        return usua;
    }

    public void setUsua(String usua) {
        this.usua = usua;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
       
}
