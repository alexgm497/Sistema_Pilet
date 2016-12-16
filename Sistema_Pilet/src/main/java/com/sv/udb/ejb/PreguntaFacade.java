/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Opcion;
import com.sv.udb.modelo.Pregunta;
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
public class PreguntaFacade extends AbstractFacade<Pregunta> implements PreguntaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public Pregunta findLast()
    {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM `pregunta` ORDER BY codi_preg DESC LIMIT 1", Pregunta.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : (Pregunta)resu.get(0);
    }
       
    
    @Override
    public List<Pregunta> findAllActive() {
        
        Query q = getEntityManager().createNativeQuery("select * from pregunta  where pregunta.esta_preg =  1", Pregunta.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    public PreguntaFacade() {
        super(Pregunta.class);
    }
    
}
