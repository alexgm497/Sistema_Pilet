/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Departamentos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author gersonfrancisco
 */
@Stateless
public class DepartamentosFacade extends AbstractFacade<Departamentos> implements DepartamentosFacadeLocal {
    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartamentosFacade() {
        super(Departamentos.class);
    }
    
    @Override
    public List<Departamentos> findTodo() {
        Query q = getEntityManager().createQuery("SELECT u FROM Departamentos u WHERE u.estaDepa ="+true, Departamentos.class);
        List resu = q.getResultList();
        return resu;
    }
    
}
