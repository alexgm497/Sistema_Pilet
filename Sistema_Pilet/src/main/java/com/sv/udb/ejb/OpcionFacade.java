/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Opcion;
import com.sv.udb.modelo.Seccion;
import com.sv.udb.modelo.Seguimiento;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author eduardo
 */
@Stateless
public class OpcionFacade extends AbstractFacade<Opcion> implements OpcionFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OpcionFacade() {
        super(Opcion.class);
    }

    @Override
    public Opcion findLast()
    {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM `opcion` ORDER BY codi_opci DESC LIMIT 1", Opcion.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : (Opcion)resu.get(0);
    }
    
    @Override
    public List<Opcion> findAllActive() {
        
        Query q = getEntityManager().createNativeQuery("select * from opcion where opcion.esta_opci =  1", Opcion.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
}
