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
public class DatosGrupos {
    private String grad;
    private String nomb;
    private String desc;

    public DatosGrupos()
    {
        
    }
    
    public DatosGrupos(String grad, String nomb, String desc) {
        this.grad = grad;
        this.nomb = nomb;
        this.desc = desc;
    }
   
    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getNomb() {
        return nomb;
    }

    public void setNomb(String nomb) {
        this.nomb = nomb;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
}
