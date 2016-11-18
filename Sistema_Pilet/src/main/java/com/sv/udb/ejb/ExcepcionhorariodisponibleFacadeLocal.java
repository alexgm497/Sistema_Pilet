/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Excepcionhorariodisponible;
import com.sv.udb.modelo.Horariodisponible;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Kevin
 */
@Local
public interface ExcepcionhorariodisponibleFacadeLocal {

    void create(Excepcionhorariodisponible excepcionhorariodisponible);

    void edit(Excepcionhorariodisponible excepcionhorariodisponible);

    void remove(Excepcionhorariodisponible excepcionhorariodisponible);

    Excepcionhorariodisponible find(Object id);

    List<Excepcionhorariodisponible> findAll();

    List<Excepcionhorariodisponible> findRange(int[] range);
    
    public boolean findByDispHora(Horariodisponible hora, Date fech);
    
    List<Excepcionhorariodisponible> findByCodiUsua(int codi);
    
    int count();
    
}
