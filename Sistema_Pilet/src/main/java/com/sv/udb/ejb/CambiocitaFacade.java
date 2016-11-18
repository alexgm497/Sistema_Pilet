package com.sv.udb.ejb;

import com.sv.udb.modelo.Alumnovisitante;
import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.modelo.Cita;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * @author Control citas
 * Facade: Cambio de la cita
 * Prototipo 2
 */
@Stateless
public class CambiocitaFacade extends AbstractFacade<Cambiocita> implements CambiocitaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CambiocitaFacade() {
        super(Cambiocita.class);
    }
    
    @Override
    public Cambiocita findByCodiCita(Cita codi) {
        TypedQuery<Cambiocita> q = (TypedQuery<Cambiocita>) getEntityManager().createQuery("SELECT c FROM Cambiocita c WHERE c.codiCita = :codiCita ORDER BY c.fechCambCita desc, c.horaCambCita desc ").setMaxResults(1);    
        q.setParameter("codiCita", codi);
        Cambiocita resu = q.getSingleResult();
        return (resu == null) ? null : resu;
    }
    
    @Override
    public Cambiocita findByCita(Cita codi) {
        TypedQuery<Cambiocita> q = (TypedQuery<Cambiocita>) getEntityManager().createQuery("SELECT c FROM Cambiocita c WHERE c.codiCita = :codiCita ORDER BY c.fechCambCita desc, c.horaCambCita desc").setMaxResults(1);     
        q.setParameter("codiCita", codi);
        Cambiocita resu = q.getSingleResult();
        return (resu == null) ? null : resu;
    }
    
    @Override
    public boolean findCambioCitaByParams(String fecha, String horaInic, String horaFin, Integer codiUsua)
    {
        String sql = "SELECT * FROM cambio_cita cc WHERE cc.codi_camb_cita in (select max(codi_camb_cita) from cambio_cita, cita where cambio_cita.codi_cita = cita.codi_cita and cita.codi_usua = ?  group by cambio_cita.codi_cita) and esta_camb_cita = 2 and ((STR_TO_DATE(?, '%h:%i %p') between STR_TO_DATE(cc.hora_inic_cita_nuev, '%h:%i %p') and STR_TO_DATE(cc.hora_fin_cita_nuev, '%h:%i %p')) or (STR_TO_DATE(?, '%h:%i %p') between STR_TO_DATE(cc.hora_inic_cita_nuev, '%h:%i %p') and STR_TO_DATE(hora_fin_cita_nuev, '%h:%i %p'))) and fech_inic_cita_nuev = ?";
        Query query = em.createNativeQuery(sql, Cambiocita.class);
        query.setParameter(1, codiUsua);
        query.setParameter(2, horaInic);
        query.setParameter(3, horaFin);
        query.setParameter(4, fecha);
        
        
        /*q.setParameter("codiUsua", codiUsua);
        q.setParameter("fecha", codiUsua);
        q.setParameter("horaInic", horaInic);
        q.setParameter("horaFina", horaFin);*/
        List<Cambiocita> resu =(List<Cambiocita>) query.getResultList();
        if(resu.size()==0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
