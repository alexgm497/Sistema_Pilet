package com.sv.udb.ejb;

import com.sv.udb.modelo.Cita;
import com.sv.udb.modelo.Visitante;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


/**
 * Visitantes
 * @author Control citas
 * Prototipo 2
 */
@Stateless
public class VisitanteFacade extends AbstractFacade<Visitante> implements VisitanteFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VisitanteFacade() {
        super(Visitante.class);
    }
    @Override
    public List<Visitante> findAll() {
        TypedQuery<Visitante> q = (TypedQuery<Visitante>) getEntityManager().createQuery("SELECT v FROM Visitante v WHERE v.estaVisi = 1");     
        List<Visitante> resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public Visitante findByDuiVisi(String dui) {
        TypedQuery<Visitante> q = (TypedQuery<Visitante>) getEntityManager().createQuery("SELECT v FROM Visitante v WHERE v.duiVisi = :duiVisi AND v.estaVisi = 1");     
        q.setParameter("duiVisi", dui);
        List<Visitante> resu = q.getResultList();
        return resu.isEmpty() ? null : resu.get(0);
    }
    @Override
    public List<Visitante> findByCita(Cita codiCita) {
        TypedQuery<Visitante> q = (TypedQuery<Visitante>) getEntityManager().createQuery("SELECT v FROM Visitante v, Visitantecita vc WHERE v.codiVisi = vc.codiVisi.codiVisi and vc.codiCita.codiCita = :codiCita AND v.estaVisi = 1");    
        q.setParameter("codiCita", codiCita.getCodiCita());
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Visitante> findByCarnAlum(String carnAlum) {
        TypedQuery<Visitante> q = (TypedQuery<Visitante>) getEntityManager().createQuery("SELECT v FROM Visitante v, Alumnovisitante ac WHERE v.codiVisi = ac.codiVisi.codiVisi and ac.carnAlum = :carnAlum AND v.estaVisi = 1 and ac.estaAlumVisi = 1");       
        q.setParameter("carnAlum", carnAlum);
        List resu = q.getResultList();
        return resu.isEmpty() ? new ArrayList<Visitante>() : resu;
    }
    
    @Override
    public List<Visitante> findByAllFields(String cadeText, int cantRegi, int estaRegi) {
        TypedQuery<Visitante> q = (TypedQuery<Visitante>) getEntityManager().createQuery("SELECT v FROM Visitante v WHERE (v.duiVisi LIKE :cadeText OR v.nombVisi LIKE :cadeText OR v.apelVisi LIKE :cadeText OR v.corrVisi LIKE :cadeText OR v.teleVisi LIKE :cadeText)  AND v.estaVisi = 1");       
        q.setParameter("cadeText", "%" + cadeText+ "%"); 
        q.setMaxResults(cantRegi);
        List resu = q.getResultList();
        return resu.isEmpty() ? new ArrayList<Visitante>() : resu;
    }
}
