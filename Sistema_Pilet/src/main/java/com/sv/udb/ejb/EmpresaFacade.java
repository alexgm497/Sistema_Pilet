/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Donacion;
import com.sv.udb.modelo.Empresa;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eduardo
 */
@Stateless
public class EmpresaFacade extends AbstractFacade<Empresa> implements EmpresaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresaFacade() {
        super(Empresa.class);
    }
    
    @Override
    public List<Empresa> findAllActive()  {        
        String consulta = "select * from empresa where empresa.esta_empr = 1";
        Query q = getEntityManager().createNativeQuery(consulta, Empresa.class);         
                  
        List resu = q.getResultList(); 
        return resu.isEmpty() ? null : resu;
    }
    
}
