/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Notificacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Adonay
 */
@Stateless
public class NotificacionFacade extends AbstractFacade<Notificacion> implements NotificacionFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificacionFacade() {
        super(Notificacion.class);
    }
    
    @Override
    public List<Notificacion> findByUsua(Object role) {
        Query q = getEntityManager().createQuery("SELECT n FROM Notificacion n WHERE n.codiUsua.codiUsua = :codiUsua AND n.estaNoti = 0", Notificacion.class);        
        q.setParameter("codiUsua", role);
        List resu = q.getResultList();
        return resu;
    }
    
}
