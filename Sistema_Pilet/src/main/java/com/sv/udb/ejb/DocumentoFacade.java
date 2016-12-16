/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Documento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Ariel
 */
@Stateless
public class DocumentoFacade extends AbstractFacade<Documento> implements DocumentoFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public Documento findLast() {
        Query q = getEntityManager().createNativeQuery("select * from documento order by documento.fech_docu DESC limit 1", Documento.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : (Documento)resu.get(0);
    }
    
    @Override
    public List<Documento> findBySoli(Object id) {
        String query="select * from documento where documento.codi_soli_beca = ?1";
         Query q = getEntityManager().createNativeQuery(query,Documento.class);
        q.setParameter(1, id);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public List<Documento> findByEmpr(Object id) {
        System.out.println("Codigo de la empresa :| "+id);
        String query="select * from documento where documento.codi_empr = ?1";
         Query q = getEntityManager().createNativeQuery(query,Documento.class);
        q.setParameter(1, id);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }


    public DocumentoFacade() {
        super(Documento.class);
    }
    
}
