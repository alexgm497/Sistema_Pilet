/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.OpcionRespuesta;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ariel
 */
@Local
public interface OpcionRespuestaFacadeLocal {

    void create(OpcionRespuesta opcionRespuesta);

    void edit(OpcionRespuesta opcionRespuesta);

    void remove(OpcionRespuesta opcionRespuesta);

    OpcionRespuesta find(Object id);

    List<OpcionRespuesta> findAll();

    List<OpcionRespuesta> findRange(int[] range);

    int count();
    
}
