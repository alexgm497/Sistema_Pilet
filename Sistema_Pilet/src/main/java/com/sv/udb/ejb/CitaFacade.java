
package com.sv.udb.ejb;

import com.sv.udb.modelo.Cita;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


/**
 * @author Control citas
 * Facade: Cita
 * Prototipo 1
 */

@Stateless
public class CitaFacade extends AbstractFacade<Cita> implements CitaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaFacade() {
        super(Cita.class);
    }
    @Override
    public List<Cita> findAll() {
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c WHERE c.estaCita != 0"); 
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Cita> findAllVisi() {
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c WHERE c.estaCita != 0 and c.tipoCita = 2"); 
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Cita> findAllCita() {
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c WHERE c.estaCita != 0 and c.tipoCita = 1"); 
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Cita> findCitaByCodiUsua(int codi) {
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c WHERE c.codiUsua = :codiUsua and c.estaCita != 0 and c.tipoCita = 1");      
        q.setParameter("codiUsua", codi);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Cita> findCitaAlumByCodiUsua(int codi) {
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c WHERE c.codiUsua = :codiUsua and c.estaCita != 0 and c.tipoCita = 1");      
        q.setParameter("codiUsua", codi);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Cita> findCitaVisiByCodiUsua(int codi) {
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c WHERE c.codiUsua = :codiUsua and c.estaCita != 0 and c.tipoCita = 3");      
        q.setParameter("codiUsua", codi);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Cita> findVisiByCodiUsua(int codi) {
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c WHERE c.codiUsua = :codiUsua and c.estaCita != 0 and c.tipoCita = 2");      
        q.setParameter("codiUsua", codi);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Cita> findCitaCale(int codiUsua) {
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c WHERE c.codiUsua = :codiUsua and c.estaCita = 2");      
        q.setParameter("codiUsua", codiUsua);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Cita> findByEstaProg() {
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c where c.estaCita = 2 ");
        List resu = q.getResultList();
        return (resu.isEmpty()) ? new ArrayList<Cita>() : resu;
    }
    @Override
    public List<Cita> findByCarnAlum(String carnAlum) {//SELECT c FROM Cita c, Visitantecita vc where c = vc.codiCita and vc.carnAlum = :carnAlum and c.estaCita != 0
        TypedQuery<Cita> q = (TypedQuery<Cita>) getEntityManager().createQuery("SELECT c FROM Cita c where c in(select vc.codiCita from Visitantecita vc where vc.carnAlum = :carnAlum) and c.estaCita != 0");
        q.setParameter("carnAlum", carnAlum);
        List resu = q.getResultList();
        return (resu.isEmpty()) ? new ArrayList<Cita>() : resu;
    }
}
