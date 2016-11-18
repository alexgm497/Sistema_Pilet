package com.sv.udb.ejb;
import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.modelo.Cita;
import com.sv.udb.modelo.Visitante;
import com.sv.udb.modelo.Visitantecita;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/**
 * Facade: Citas de los visitantes
 * @author Control citas
 * Prototipo 1
 */
@Stateless
public class VisitantecitaFacade extends AbstractFacade<Visitantecita> implements VisitantecitaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VisitantecitaFacade() {
        super(Visitantecita.class);
    }
    
    @Override
    public List<Visitantecita> findAll() {
        TypedQuery<Visitantecita> q = (TypedQuery<Visitantecita>) getEntityManager().createQuery("SELECT v FROM Visitantecita v WHERE v.estaVisi != -1");
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public List<Visitantecita> findByCarnAlum(String codi) {
        TypedQuery<Visitantecita> q = (TypedQuery<Visitantecita>) getEntityManager().createQuery("SELECT v FROM Visitantecita v WHERE v.carnAlum = :carnAlum and v.estaVisi != -1");
        q.setParameter("carnAlum", codi);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Visitantecita> findByCodiUsua(int codi) {
        TypedQuery<Visitantecita> q = (TypedQuery<Visitantecita>) getEntityManager().createQuery("SELECT v FROM Visitantecita v WHERE v.codiCita.codiUsua = :codiUsua and v.estaVisi != -1");
        q.setParameter("codiUsua", Integer.parseInt(String.valueOf(codi)));
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Visitantecita> findByCodiCita(Cita codi) {
        TypedQuery<Visitantecita> q = (TypedQuery<Visitantecita>) getEntityManager().createQuery("SELECT v FROM Visitantecita v WHERE v.codiCita = :codiCita and v.estaVisi != -1");      
        q.setParameter("codiCita", codi);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public Visitantecita findByCodiCitaCarnAlum(Cita cita, String carn) {
        TypedQuery<Visitantecita> q = (TypedQuery<Visitantecita>) getEntityManager().createQuery("SELECT v FROM Visitantecita v WHERE v.codiCita = :codiCita and v.carnAlum = :carnAlum and v.estaVisi != -1");      
        q.setParameter("codiCita", cita);
        q.setParameter("carnAlum", carn);
        List<Visitantecita> resu = q.getResultList();
        return (resu.isEmpty()) ? null : resu.get(0);
    }
    
}