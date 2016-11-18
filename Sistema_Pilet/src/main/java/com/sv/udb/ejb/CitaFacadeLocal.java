/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Cita;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Kevin
 */
@Local
public interface CitaFacadeLocal {

    void create(Cita cita);

    void edit(Cita cita);

    void remove(Cita cita);

    Cita find(Object id);

    List<Cita> findAll();
    
    List<Cita> findAllVisi();
    
    List<Cita> findAllCita();

    List<Cita> findRange(int[] range);

    List<Cita> findCitaByCodiUsua(int codi);
    
    List<Cita> findVisiByCodiUsua(int codi);
    
    List<Cita> findByEstaProg();
    
    List<Cita> findByCarnAlum(String carnAlum);
    
    List<Cita> findCitaCale(int codiUsua);
    
    int count();
    
}
