/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.modelo.Cita;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Kevin
 */
@Local
public interface CambiocitaFacadeLocal {

    void create(Cambiocita cambiocita);

    void edit(Cambiocita cambiocita);

    void remove(Cambiocita cambiocita);

    Cambiocita find(Object id);

    List<Cambiocita> findAll();

    List<Cambiocita> findRange(int[] range);
    
    Cambiocita findByCodiCita(Cita codi);
    
    Cambiocita findByCita(Cita codi);
    
    boolean findCambioCitaByParams(String fecha, String horaInic, String horaFin, Integer codiUsua);
    
    List<Cambiocita> findCambioCitaByFechaAndUsua(Date fechaInicial, Date fechaFinal, Integer codiUsua, Integer estaCita, int tipoCita);
    
    List<Cambiocita> findCambioCitaCale(Date fechaInicial, Date fechaFinal, Integer codiUsua);
    
    List<Cambiocita> findCambioVisiByFech(Date fechaInicial, Date fechaFinal);
    
    List<Cambiocita> findCambioCitaByCarnAlum(Date fechaInicial, Date fechaFinal, String carnAlum);
    
    int count();
    
}
