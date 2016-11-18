/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Horariodisponible;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Kevin
 */
@Local
public interface HorariodisponibleFacadeLocal {

    void create(Horariodisponible horariodisponible);

    void edit(Horariodisponible horariodisponible);

    void remove(Horariodisponible horariodisponible);

    Horariodisponible find(Object id);

    List<Horariodisponible> findAll();

    List<Horariodisponible> findRange(int[] range);

    List<Horariodisponible> findByCodiUsua(Object codi);
        
    int count();
    
}
