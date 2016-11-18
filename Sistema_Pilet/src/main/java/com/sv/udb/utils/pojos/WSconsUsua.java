/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils.pojos;

import java.util.List;

/**
 *
 * @author aleso
 */
public class WSconsUsua {
    private boolean resp;
    private List<DatosUsuarios> resu;
    private List<DatosUsuarios> resuElim;

    public boolean isResp() {
        return resp;
    }

    public void setResp(boolean resp) {
        this.resp = resp;
    }

    public List<DatosUsuarios> getResu() {
        return resu;
    }

    public void setResu(List<DatosUsuarios> resu) {
        this.resu = resu;
    }
    
}
