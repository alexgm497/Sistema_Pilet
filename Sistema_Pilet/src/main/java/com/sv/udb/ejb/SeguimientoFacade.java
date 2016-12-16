/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

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
public class SeguimientoFacade extends AbstractFacade<Seguimiento> implements SeguimientoFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SeguimientoFacade() {
        super(Seguimiento.class);
    }
    
    @Override
    public List<Seguimiento> findByEstaSegu() {
        TypedQuery<Seguimiento> q = (TypedQuery<Seguimiento>) getEntityManager().createQuery("SELECT s FROM Seguimiento s WHERE s.estaSegu = 1 ");
        List resu = q.getResultList();
        return (resu.isEmpty()) ? new ArrayList<Seguimiento>() : resu;
    }
    
    @Override
    public List<Seguimiento> findByAll()
    {
        Query q = getEntityManager().createNativeQuery("select * from seguimiento WHERE codi_soli_beca is null and codi_empr is null", Seguimiento.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public List<Seguimiento> findByEmpr()
    {
        Query q = getEntityManager().createNativeQuery("select * from seguimiento where codi_empr != 0", Seguimiento.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public List<Seguimiento> findBySoli()
    {
        Query q = getEntityManager().createNativeQuery("select * from seguimiento where codi_soli_beca != 0", Seguimiento.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
     @Override
    public List<Seguimiento> findByEmprInSpec(Object id)
    {
        Query q = getEntityManager().createNativeQuery("select * from seguimiento where codi_empr = ?1", Seguimiento.class);
        q.setParameter(1, id);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public List<Seguimiento> findBySoliInSpec(Object id)
    {
        System.out.println("id facade: "+id);
        Query q = getEntityManager().createNativeQuery("select * from seguimiento where codi_soli_beca = ?1", Seguimiento.class);
        q.setParameter(1, id);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public Seguimiento findByCodiSegu(Seguimiento codi) {
        TypedQuery<Seguimiento> q = (TypedQuery<Seguimiento>) getEntityManager().createQuery("SELECT s FROM Seguimiento s WHERE s.codiSegu = :codi_segu ORDER BY s.fechInicio desc, s.fechFin desc ").setMaxResults(1);    
        q.setParameter("codi_segu", codi.getCodiSegu());
        Seguimiento resu = q.getSingleResult();
        return (resu == null) ? null : resu;
    }
 
    @Override
    public List<Seguimiento> findByEmprU(int codi)
    {
        Query q = getEntityManager().createNativeQuery("select * from seguimiento where codi_empr = ?1", Seguimiento.class);
        q.setParameter(1, codi); 
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
}
