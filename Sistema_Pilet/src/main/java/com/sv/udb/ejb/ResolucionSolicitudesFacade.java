/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.controlador.LoginBean;
import com.sv.udb.modelo.ResolucionSolicitudes;
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
public class ResolucionSolicitudesFacade extends AbstractFacade<ResolucionSolicitudes> implements ResolucionSolicitudesFacadeLocal {
    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResolucionSolicitudesFacade() {
        super(ResolucionSolicitudes.class);
    }
    
    @Override
    public ResolucionSolicitudes findReso(int codi) {
        Query q = getEntityManager().createQuery("SELECT u FROM ResolucionSolicitudes u WHERE u.codiSoli.codiSoli = :codiSoli", ResolucionSolicitudes.class);
        q.setParameter("codiSoli", codi);
        ResolucionSolicitudes resu = (ResolucionSolicitudes)q.getSingleResult();
        return resu;
    }
    
    @Override
    public List<ResolucionSolicitudes> findResoUsua(int cant) {
        Query q = getEntityManager().createQuery("SELECT rs FROM ResolucionSolicitudes rs INNER JOIN Solicitudes s ON rs.codiSoli.codiSoli = s.codiSoli WHERE s.codiEnca = :codiEnca ORDER BY rs.codiResoSoli DESC", ResolucionSolicitudes.class);
        LoginBean login = new LoginBean();
        q.setParameter("codiEnca", login.getObjeUsua().getCodiUsua());
        List resu = q.setMaxResults(cant).getResultList();
        return resu;
    }
}
