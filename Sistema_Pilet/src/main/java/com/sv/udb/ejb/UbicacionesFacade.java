/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Ubicaciones;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Kevin
 */
@Stateless
public class UbicacionesFacade extends AbstractFacade<Ubicaciones> implements UbicacionesFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UbicacionesFacade() {
        super(Ubicaciones.class);
    }
    @Override
    public List<Ubicaciones> findAll() {
        TypedQuery<Ubicaciones> q = (TypedQuery<Ubicaciones>) getEntityManager().createQuery("SELECT u FROM Ubicaciones u WHERE u.estaUbic = true"); 
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Ubicaciones> findByDispCita() {
        TypedQuery<Ubicaciones> q = (TypedQuery<Ubicaciones>) getEntityManager().createQuery("SELECT u FROM Ubicaciones u WHERE u.estaUbic = true and u.dispCita = 1"); 
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Ubicaciones> findByDispEven() {
        TypedQuery<Ubicaciones> q = (TypedQuery<Ubicaciones>) getEntityManager().createQuery("SELECT u FROM Ubicaciones u WHERE u.estaUbic = true and u.dispEvent = 1"); 
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
}
