/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Alumnovisitante;
import com.sv.udb.modelo.Cita;
import com.sv.udb.modelo.Visitante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Kevin
 */
@Local
public interface AlumnovisitanteFacadeLocal {

    void create(Alumnovisitante alumnovisitante);

    void edit(Alumnovisitante alumnovisitante);

    void remove(Alumnovisitante alumnovisitante);

    Alumnovisitante find(Object id);

    List<Alumnovisitante> findAll();

    List<Alumnovisitante> findRange(int[] range);

    List<Alumnovisitante> findByCodiVisiCarnAlum(Object codiVisi, Object carnAlum);
    
    List<Alumnovisitante> findByCarnAlum(Object carnAlum);
    
    List<Alumnovisitante> findByCita(Cita cita);
    
    Alumnovisitante findByAlumVisi(Visitante visi, String carn);
    
    int count();
    
}
