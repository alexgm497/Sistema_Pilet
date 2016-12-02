/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Solicitudes;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gersonfrancisco
 */
@Local
public interface SolicitudesFacadeLocal {

    void create(Solicitudes solicitudes);

    void edit(Solicitudes solicitudes);

    void remove(Solicitudes solicitudes);

    Solicitudes find(Object id);
    
    Solicitudes findSoli(int codi);
    
    List<Solicitudes> findTodo();
    
    List<Solicitudes> findEncargado();
    
    List<Solicitudes> findTecnico();

    List<Solicitudes> findVaci();
    
    List<Solicitudes> findAllAsig();
    
    List<Solicitudes> findAsig();

    List<Solicitudes> findAll();
    
    List<Solicitudes> findTodoR();
    
    List<Solicitudes> findAsigFina();

    List<Solicitudes> findRange(int[] range);
    
    void asig(int codiSoli, String prio, int codiUsua);
    
    void reso(int codiSoli);

    int count();
    
}
