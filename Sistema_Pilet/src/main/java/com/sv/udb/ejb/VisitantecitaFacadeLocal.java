/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Cita;
import com.sv.udb.modelo.Visitante;
import com.sv.udb.modelo.Visitantecita;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Kevin
 */
@Local
public interface VisitantecitaFacadeLocal {

    void create(Visitantecita visitantecita);

    void edit(Visitantecita visitantecita);

    void remove(Visitantecita visitantecita);

    Visitantecita find(Object id);

    List<Visitantecita> findAll();

    List<Visitantecita> findRange(int[] range);
    
    List<Visitantecita> findByCarnAlum(String codi);
    
    List<Visitantecita> findByCodiUsua(int codi);
    
    List<Visitantecita> findByCodiCita(Cita codi);

    Visitantecita findByCodiCitaCarnAlum(Cita cita, String carn);
    
    int count();
    
}
