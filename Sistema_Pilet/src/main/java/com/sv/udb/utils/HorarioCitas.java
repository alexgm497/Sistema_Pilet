/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils;

import com.sv.udb.modelo.Horariodisponible;

/**
 *
 * @author Alvin
 */
public class HorarioCitas {
    private String fecha;
    private String diaDisp;
    private String horaInic;
    private String horaFina;
    
    public HorarioCitas()
    {
        
    }

    public String getDiaDisp() {
        return diaDisp;
    }

    public void setDiaDisp(String diaDisp) {
        this.diaDisp = diaDisp;
    }
    
    

    public HorarioCitas(String fecha, String horaInic, String horaFina, String diaDisp) {
        this.fecha = fecha;
        this.horaInic = horaInic;
        this.horaFina = horaFina;
        this.diaDisp = diaDisp;
    }    
    
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInic() {
        return horaInic;
    }

    public void setHoraInic(String horaInic) {
        this.horaInic = horaInic;
    }

    public String getHoraFina() {
        return horaFina;
    }

    public void setHoraFina(String horaFina) {
        this.horaFina = horaFina;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HorarioCitas)) {
            return false;
        }
        HorarioCitas other = (HorarioCitas) object;
        if ((this.diaDisp == null && other.diaDisp != null) || (this.diaDisp != null && !this.diaDisp.equals(other.diaDisp))) {
            return false;
        }
        return true;
    }
    
    
}
