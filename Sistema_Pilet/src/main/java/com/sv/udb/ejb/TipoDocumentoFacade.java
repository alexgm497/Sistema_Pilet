/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.TipoBeca;
import com.sv.udb.modelo.TipoDocumento;
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
public class TipoDocumentoFacade extends AbstractFacade<TipoDocumento> implements TipoDocumentoFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoDocumentoFacade() {
        super(TipoDocumento.class);
    }
    
    @Override
    public List<TipoDocumento> findAllActive()  {        
        String consulta = "select * from tipo_documento where tipo_documento.esta_tipo_docu = 1";
        Query q = getEntityManager().createNativeQuery(consulta, TipoDocumento.class);         
                  
        List resu = q.getResultList(); 
        return resu.isEmpty() ? null : resu;
    }
    
}
