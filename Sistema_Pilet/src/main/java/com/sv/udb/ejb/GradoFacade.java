/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Grado;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eduardo
 */
@Stateless
public class GradoFacade extends AbstractFacade<Grado> implements GradoFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GradoFacade() {
        super(Grado.class);
    }
    
    @Override
    public BigDecimal findMatrLimit(Object id){
        String query = "SELECT MAX(g.matr_grad) FROM grado g WHERE g.nivel_grad = ?1";
        Query q = getEntityManager().createNativeQuery(query);
        q.setParameter(1, id);
        BigDecimal montMatr = (BigDecimal) q.getSingleResult();
        return montMatr;
    }
    
    @Override
    public BigDecimal findMensLimit(Object id){
        String query = "SELECT MAX(g.mens_grad) FROM grado g WHERE g.nivel_grad = ?1";
        Query q = getEntityManager().createNativeQuery(query);
        q.setParameter(1, id);
        BigDecimal montMens = (BigDecimal) q.getSingleResult();
        return montMens;
    }
    
}
