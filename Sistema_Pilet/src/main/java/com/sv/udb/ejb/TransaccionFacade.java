/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Transaccion;
import java.math.BigDecimal;
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
public class TransaccionFacade extends AbstractFacade<Transaccion> implements TransaccionFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TransaccionFacade() {
        super(Transaccion.class);
    }


    @Override
    public Transaccion findLast() {
        Query q = getEntityManager().createNativeQuery("select * from transaccion order by fech_tran DESC limit 1", Transaccion.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : (Transaccion)resu.get(0);
    }
    
    @Override
    public BigDecimal findMonto(Object id){
        String query = "SELECT SUM(mont_tran) FROM transaccion WHERE codi_dona = ?1";
        Query q = getEntityManager().createNativeQuery(query);
        q.setParameter(1, id);
        BigDecimal paga = (BigDecimal) q.getSingleResult();
        return paga;
    }
    
    @Override
    public BigDecimal findMontoEmpr(Object id){
        System.out.println("Id:" +id);
        BigDecimal montEmpr =new BigDecimal(0);
        try {
            String query = "SELECT e.mont_empr FROM empresa e WHERE e.codi_empr = ?1";
            Query q = getEntityManager().createNativeQuery(query);
            q.setParameter(1, id);
            Object  obj = q.getSingleResult();
            montEmpr = (BigDecimal) obj;
             
            System.out.println("Monto empresa: " +montEmpr);
            
        } catch (Exception e) {
            System.out.println("Errosito:" +e.getMessage());
        }
        
        return montEmpr;
    }
}
