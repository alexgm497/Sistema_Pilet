/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.PermisoRol;
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
public class PermisoRolFacade extends AbstractFacade<PermisoRol> implements PermisoRolFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermisoRolFacade() {
        super(PermisoRol.class);
    }
 
    @Override
    public PermisoRol findByPermAndRole(Object perm, Object role) {
        String query = "SELECT * FROM permiso_rol WHERE codi_perm = ?1 AND codi_role = ?2 AND esta_perm_role = 1";
        Query q = getEntityManager().createNativeQuery(query, PermisoRol.class);
        q.setParameter(1, perm);
        q.setParameter(2, role);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : (PermisoRol)resu.get(0);
    }
}
