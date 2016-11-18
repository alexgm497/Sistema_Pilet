/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.TipoDonacion;
import com.sv.udb.modelo.TipoRetiro;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eduardo
 */
@Stateless
public class TipoDonacionFacade extends AbstractFacade<TipoDonacion> implements TipoDonacionFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoDonacionFacade() {
        super(TipoDonacion.class);
    }
      @Override
    public List<TipoDonacion> findAllActive()  {        
        String consulta = "select * from tipo_donacion where tipo_donacion.esta_dona = 1";
        Query q = getEntityManager().createNativeQuery(consulta, TipoDonacion.class);         
                  
        List resu = q.getResultList(); 
        return resu.isEmpty() ? null : resu;
    }
}
