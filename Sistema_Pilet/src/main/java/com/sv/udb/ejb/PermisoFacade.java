/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Permiso;
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
public class PermisoFacade extends AbstractFacade<Permiso> implements PermisoFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermisoFacade() {
        super(Permiso.class);
    }
    
    @Override
    public List<Permiso> findAllModu() {
        String query = "SELECT * FROM permiso WHERE refe_perm = 1 AND esta_perm = 1";
        Query q = getEntityManager().createNativeQuery(query, Permiso.class);
        List resu = q.getResultList();
        return resu;
    }
    
    @Override
    public List<Permiso> findPagiByModu(Object modu) {
        String query = "SELECT * FROM permiso WHERE dire_perm LIKE '%.xhtml' AND esta_perm = 1 AND refe_perm = ?1";
        Query q = getEntityManager().createNativeQuery(query, Permiso.class);
        q.setParameter(1, modu);
        List resu = q.getResultList();
        return resu;
    }
    
    @Override
    public List<Permiso> findAcciByPagi(Object pagi) {
        String query = "SELECT * FROM permiso WHERE dire_perm NOT LIKE '%.xhtml' AND esta_perm = 1 AND refe_perm = ?1";
        Query q = getEntityManager().createNativeQuery(query, Permiso.class);
        q.setParameter(1, pagi);
        List resu = q.getResultList();
        return resu;
    }
}
