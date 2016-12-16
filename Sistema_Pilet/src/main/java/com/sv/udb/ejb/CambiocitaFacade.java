package com.sv.udb.ejb;

import com.sv.udb.modelo.Alumnovisitante;
import com.sv.udb.modelo.Beca_;
import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.modelo.Cita;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public List<Cambiocita> findCambioCitaByCarnAlum(Date fechaInicial, Date fechaFinal, String carnAlum)
    {
        List<Cambiocita> resu;
        String sql = "SELECT cc.* FROM cambio_cita cc, cita c WHERE c.codi_cita in (select codi_cita from visitante_cita where carn_alum = ?) and cc.codi_cita = c.codi_cita and  cc.codi_camb_cita in(SELECT MAX(codi_camb_cita) from cambio_cita, cita where cambio_cita.codi_cita = cita.codi_cita and ((fech_camb_cita between ? and ?) or (fech_inic_cita_nuev between ? and ?) or (fech_fin_cita_nuev between ? and ?)) group by cita.codi_cita) ORDER BY cc.codi_camb_cita DESC ";
        Query query = em.createNativeQuery(sql, Cambiocita.class);
        query.setParameter(1, carnAlum);
        query.setParameter(2, fechaInicial);
        query.setParameter(3, fechaFinal);
        query.setParameter(4, fechaInicial);
        query.setParameter(5, fechaFinal);
        query.setParameter(6, fechaInicial);
        query.setParameter(7, fechaFinal);
        resu =(List<Cambiocita>) query.getResultList();
        return resu;
    }
    
    @Override
    public List<Cambiocita> findCambioCitaCale(Date fechaInicial, Date fechaFinal, Integer codiUsua)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<Cambiocita> resu;
        String sql = "SELECT cc.* FROM cambio_cita cc, cita c WHERE c.tipo_cita != 2 AND cc.esta_camb_cita= 2 AND c.codi_cita = cc.codi_cita AND cc.codi_camb_cita in(SELECT MAX(codi_camb_cita) from cambio_cita, cita where cambio_cita.codi_cita = cita.codi_cita and codi_usua = ? and (fech_fin_cita_nuev between ? and ?) group by cita.codi_cita)  ORDER BY cc.codi_camb_cita DESC";
        Query query = em.createNativeQuery(sql, Cambiocita.class);
        query.setParameter(1, codiUsua);
        query.setParameter(2, df.format(fechaInicial));
        query.setParameter(3, df.format(fechaFinal));
        resu =(List<Cambiocita>) query.getResultList();
        return resu;
    }
    
    @Override
    public List<Cambiocita> findCambioCitaByFechaAndUsua(Date fechaInicial, Date fechaFinal, Integer codiUsua, Integer estaCita, int tipoCita)
    {
        List<Cambiocita> resu;
        if(estaCita==10)
        {
            String sql = "SELECT cc.* FROM cambio_cita cc, cita c WHERE c.tipo_cita = ? AND c.codi_cita = cc.codi_cita AND cc.codi_camb_cita in(SELECT MAX(codi_camb_cita) from cambio_cita, cita where cambio_cita.codi_cita = cita.codi_cita and codi_usua = ? and ((fech_camb_cita between ? and ?) or (fech_inic_cita_nuev between ? and ?) or (fech_fin_cita_nuev between ? and ?)) group by cita.codi_cita)  ORDER BY cc.codi_camb_cita DESC";
            Query query = em.createNativeQuery(sql, Cambiocita.class);
            query.setParameter(1, tipoCita);
            query.setParameter(2, codiUsua);
            query.setParameter(3, fechaInicial);
            query.setParameter(4, fechaFinal);
            query.setParameter(5, fechaInicial);
            query.setParameter(6, fechaFinal);
            query.setParameter(7, fechaInicial);
            query.setParameter(8, fechaFinal);
            resu =(List<Cambiocita>) query.getResultList();
        }
        else
        {
            String sql = "SELECT cc.* FROM cambio_cita cc, cita c WHERE c.tipo_cita = ? and c.codi_cita = cc.codi_cita AND cc.esta_camb_cita=? AND cc.codi_camb_cita in(SELECT MAX(codi_camb_cita) from cambio_cita, cita where cambio_cita.codi_cita = cita.codi_cita and codi_usua = ?  group by cita.codi_cita) and ((cc.fech_camb_cita between ? and ?) or (cc.fech_inic_cita_nuev between ? and ?) or (cc.fech_fin_cita_nuev between ? and ?)) ORDER BY cc.codi_camb_cita DESC";
            Query query = em.createNativeQuery(sql, Cambiocita.class);
            query.setParameter(1, tipoCita);
            query.setParameter(2, estaCita);
            query.setParameter(3, codiUsua);
            query.setParameter(4, fechaInicial);
            query.setParameter(5, fechaFinal);
            query.setParameter(6, fechaInicial);
            query.setParameter(7, fechaFinal);
            query.setParameter(8, fechaInicial);
            query.setParameter(9, fechaFinal);
            resu =(List<Cambiocita>) query.getResultList();
        }
        return resu;
    }
    
    
    
    @Override
    public boolean findCambioCitaByParams(String fecha, String horaInic, String horaFin, Integer codiUsua)
    {
        String sql = "SELECT * FROM cambio_cita cc WHERE cc.codi_camb_cita in (select max(codi_camb_cita) from cambio_cita, cita where cambio_cita.codi_cita = cita.codi_cita and cita.codi_usua = ?  group by cambio_cita.codi_cita) and esta_camb_cita = 2 and ((STR_TO_DATE(?, '%h:%i %p') between STR_TO_DATE(cc.hora_inic_cita_nuev, '%h:%i %p') and STR_TO_DATE(cc.hora_fin_cita_nuev, '%h:%i %p')) or (STR_TO_DATE(?, '%h:%i %p') between STR_TO_DATE(cc.hora_inic_cita_nuev, '%h:%i %p') and STR_TO_DATE(hora_fin_cita_nuev, '%h:%i %p'))) and fech_inic_cita_nuev = ? ";
        Query query = em.createNativeQuery(sql, Cambiocita.class);
        query.setParameter(1, codiUsua);
        query.setParameter(2, horaInic);
        query.setParameter(3, horaFin);
        query.setParameter(4, fecha);
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
    
    @Override
    public List<Cambiocita> findCambioVisiByFech(Date fechaInicial, Date fechaFinal)
    {
        List<Cambiocita> resu;
        String sql = "SELECT cc.* FROM cambio_cita cc, cita c WHERE c.tipo_cita = ? AND c.codi_cita = cc.codi_cita AND cc.codi_camb_cita in(SELECT MAX(codi_camb_cita) from cambio_cita, cita where cambio_cita.codi_cita = cita.codi_cita  and ((fech_camb_cita between ? and ?) or (fech_inic_cita_nuev between ? and ?) or (fech_fin_cita_nuev between ? and ?)) group by cita.codi_cita)  ORDER BY cc.codi_camb_cita DESC";
        Query query = em.createNativeQuery(sql, Cambiocita.class);
        query.setParameter(1, 2);
        query.setParameter(2, fechaInicial);
        query.setParameter(3, fechaFinal);
        query.setParameter(4, fechaInicial);
        query.setParameter(5, fechaFinal);
        query.setParameter(6, fechaInicial);
        query.setParameter(7, fechaFinal);
        resu =(List<Cambiocita>) query.getResultList();
        return resu;
    }
}
