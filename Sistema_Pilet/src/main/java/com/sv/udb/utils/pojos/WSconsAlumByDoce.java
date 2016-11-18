/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils.pojos;

import java.util.List;

/**
 *
 * @author Mauricio
 */
public class WSconsAlumByDoce {
    private boolean resp;
    private List<DatosAlumnos> resu;

    public boolean isResp() {
        return resp;
    }

    public void setResp(boolean resp) {
        this.resp = resp;
    }

    public List<DatosAlumnos> getResu() {
        return resu;
    }

    public void setResu(List<DatosAlumnos> resu) {
        this.resu = resu;
    }
}
