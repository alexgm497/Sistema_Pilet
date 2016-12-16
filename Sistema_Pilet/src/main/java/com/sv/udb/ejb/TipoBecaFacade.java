/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Empresa;
import com.sv.udb.modelo.TipoBeca;
import java.util.Arrays;
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
public class TipoBecaFacade extends AbstractFacade<TipoBeca> implements TipoBecaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoBecaFacade() {
        super(TipoBeca.class);
    }
    @Override
    public List<TipoBeca> findAllActive()  {        
        String consulta = "select * from tipo_beca  where tipo_beca.esta_tipo_beca = 1";
        Query q = getEntityManager().createNativeQuery(consulta, TipoBeca.class);         
                  
        List resu = q.getResultList(); 
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public List<TipoBeca> findTipos(Object id)  {        
        String consulta = "SELECT t.codi_tipo_beca, t.nomb_tipo_beca, t.desc_tipo_beca, t.esta_tipo_beca, t.tipo_tipo_beca, t.nivel_tipo_beca FROM tipo_beca t, grado g WHERE t.nivel_tipo_beca = ?1 AND t.nivel_tipo_beca = g.nivel_grad GROUP BY t.codi_tipo_beca";
        Query q = getEntityManager().createNativeQuery(consulta, TipoBeca.class);           
        //Query q = getEntityManager().createNativeQuery("select * from detalle_beca", DetalleBeca.class);           
        q.setParameter(1, id);
        List resu = q.getResultList();
        System.out.println(Arrays.toString(resu.toArray()));
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public TipoBeca findByName(String nombre)
    {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM `tipo_beca` WHERE `nomb_tipo_beca` = ?1", TipoBeca.class);
        q.setParameter(1, nombre);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : (TipoBeca)resu.get(0);
    }
}
