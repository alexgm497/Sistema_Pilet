/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.DetalleBeca;
import java.util.Arrays;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Owner
 */
@Stateless
public class DetalleBecaFacade extends AbstractFacade<DetalleBeca> implements DetalleBecaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public List<DetalleBeca> findForCombo(Object id)  {        
        String consulta = "select d.codi_deta_beca,d.codi_beca,d.codi_tipo_beca,d.cant_mese,d.esta_deta_beca "
                + "from detalle_beca d inner JOIN beca b on  d.codi_beca = b.codi_beca "
                + "inner JOIN solicitud_beca s on  b.codi_soli_beca = s.codi_soli_beca  where s.codi_soli_beca =?1 and d.esta_deta_beca = 1";
        Query q = getEntityManager().createNativeQuery(consulta, DetalleBeca.class);           
        //Query q = getEntityManager().createNativeQuery("select * from detalle_beca", DetalleBeca.class);           
        q.setParameter(1, id);          
        List resu = q.getResultList();   
        
        
        System.out.println(Arrays.toString(resu.toArray()));
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public int validar(int codi_beca, int codi_tipo)
    {
         System.out.println("Consola beca: "+codi_tipo);
        System.out.println("Consola tipo: "+codi_beca);
        String consulta = "SELECT COUNT(codi_deta_beca) FROM `detalle_beca` WHERE `codi_tipo_beca` = ?1 and `codi_beca` = ?2";
        Query q = getEntityManager().createNativeQuery(consulta);
        q.setParameter(1, codi_tipo);
        q.setParameter(2, codi_beca);
        System.out.println("Resu: "+String.valueOf(q.getSingleResult() ));
        //Object resu = q.getSingleResult().getClass().getCanonicalName();
        return Integer.parseInt(String.valueOf(q.getSingleResult()));
    }
    
      
    @Override
    public List<DetalleBeca> findByBeca(Object id) {
        String query="select*from detalle_beca where detalle_beca.codi_beca = ?1";
         Query q = getEntityManager().createNativeQuery(query,DetalleBeca.class);
        q.setParameter(1, id);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public void desa_deta(int codi_beca)
    {
        System.out.println("Codigo beca en detalle: "+codi_beca);
        String consulta = "UPDATE `detalle_beca` SET `esta_deta_beca`= 0 WHERE `codi_beca` = 1?";
        Query q = getEntityManager().createNativeQuery(consulta);
        q.setParameter(1, codi_beca);//Y tu coso del chat?
    }

    public DetalleBecaFacade() {
        super(DetalleBeca.class);
    }
    
}
