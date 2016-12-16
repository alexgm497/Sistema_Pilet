    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Beca;
import com.sv.udb.modelo.SolicitudBeca;
import java.math.BigDecimal;
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
public class SolicitudBecaFacade extends AbstractFacade<SolicitudBeca> implements SolicitudBecaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SolicitudBecaFacade() {
        super(SolicitudBeca.class);
    }
    
    @Override
    public List<SolicitudBeca> findAllActivos()
    {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM `solicitud_beca` WHERE `esta_soli_beca` = 1", SolicitudBeca.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public SolicitudBeca findLast()
    {
        Query q = getEntityManager().createNativeQuery("select * from solicitud_beca order by solicitud_beca.fech_soli_beca DESC limit 1", SolicitudBeca.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : (SolicitudBeca)resu.get(0);
    }
    @Override
    public SolicitudBeca findCarnet(String algo)
    {
        Query q = getEntityManager().createNativeQuery("select * from solicitud_beca where solicitud_beca.carn_alum ="+algo +" and  solicitud_beca.esta_soli_beca != 3 and  solicitud_beca.esta_soli_beca != 2", SolicitudBeca.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : (SolicitudBeca)resu.get(0);
    }
    
    @Override
    public List<SolicitudBeca> findAllDocu()
    {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM solicitud_beca WHERE esta_soli_beca != 3 and esta_soli_beca != 2", SolicitudBeca.class);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    
    
    
    @Override
     public void updateAll(int idNuevo,int idViejo)
     {
        String query = "update seguimiento set seguimiento.codi_soli_beca = "+idNuevo+"  where seguimiento.codi_soli_beca = "+idViejo;
        Query q = getEntityManager().createNativeQuery(query);
        q.executeUpdate();
         System.out.println("Query en soli: "+query);
        
        query = "update documento set documento.codi_soli_beca = "+idNuevo+" where documento.codi_soli_beca = "+idViejo;
        q = getEntityManager().createNativeQuery(query);
        q.executeUpdate();
       
         query = "update respuesta set respuesta.codi_soli_beca = "+idNuevo+" where respuesta.codi_soli_beca = "+idViejo;
        q = getEntityManager().createNativeQuery(query);
        q.executeUpdate();
    }
}
