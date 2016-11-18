/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.TipoDocumento;
import com.sv.udb.modelo.TipoRetiro;
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
public class TipoRetiroFacade extends AbstractFacade<TipoRetiro> implements TipoRetiroFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoRetiroFacade() {
        super(TipoRetiro.class);
    }
    
     @Override
    public List<TipoRetiro> findAllActive()  {        
        String consulta = "select * from tipo_retiro where tipo_retiro.esta_reti = 1";
        Query q = getEntityManager().createNativeQuery(consulta, TipoRetiro.class);         
                  
        List resu = q.getResultList(); 
        return resu.isEmpty() ? null : resu;
    }
    
}
