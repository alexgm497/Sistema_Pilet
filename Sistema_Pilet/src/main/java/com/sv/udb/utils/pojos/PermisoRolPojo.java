/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils.pojos;

import com.sv.udb.modelo.Permiso;

/**
 *
 * @author Carlos
 */
public class PermisoRolPojo {
    private Permiso codiModu;
    private Permiso codiPagi;
    
    
    public PermisoRolPojo(){
        
    }
    
    public Permiso getCodiModu() {
        return codiModu;
    }

    public void setCodiModu(Permiso codiModu) {
        this.codiModu = codiModu;
    }

    public Permiso getCodiPagi() {
        return codiPagi;
    }

    public void setCodiPagi(Permiso codiPagi) {
        this.codiPagi = codiPagi;
    }
}
