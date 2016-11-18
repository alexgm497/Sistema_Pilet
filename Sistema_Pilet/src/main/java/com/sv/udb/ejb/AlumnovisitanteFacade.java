package com.sv.udb.ejb;
import com.sv.udb.modelo.Alumnovisitante;
import com.sv.udb.modelo.Cita;
import com.sv.udb.modelo.Horariodisponible;
import com.sv.udb.modelo.Visitante;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Facade: Alumno visitantes
 * @author Control citas
 * Prototipo 2
 */
@Stateless
public class AlumnovisitanteFacade extends AbstractFacade<Alumnovisitante> implements AlumnovisitanteFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlumnovisitanteFacade() {
        super(Alumnovisitante.class);
    }
    
    @Override
    public List<Alumnovisitante> findAll() {
        TypedQuery<Alumnovisitante> q = (TypedQuery<Alumnovisitante>) getEntityManager().createQuery("SELECT a FROM Alumnovisitante a WHERE a.estaAlumVisi = 1"); 
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public List<Alumnovisitante> findByCodiVisiCarnAlum(Object codiVisi, Object carnAlum) {
        TypedQuery<Alumnovisitante> q = (TypedQuery<Alumnovisitante>) getEntityManager().createQuery("SELECT a FROM Alumnovisitante a WHERE a.codiVisi = :codiVisi and a.carnAlum = :carnAlum and a.estaAlumVisi = 1");        
        q.setParameter("codiVisi", codiVisi);
        q.setParameter("carnAlum", carnAlum);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Alumnovisitante> findByCarnAlum(Object carnAlum) {
        TypedQuery<Alumnovisitante> q = (TypedQuery<Alumnovisitante>) getEntityManager().createQuery("SELECT a FROM Alumnovisitante a WHERE a.carnAlum = :carnAlum and a.estaAlumVisi = 1");
        q.setParameter("carnAlum", String.valueOf(carnAlum));
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    @Override
    public List<Alumnovisitante> findByCita(Cita codiCita) {
        TypedQuery<Alumnovisitante> q = (TypedQuery<Alumnovisitante>) getEntityManager().createQuery("SELECT a FROM Alumnovisitante a, Visitantecita vs  WHERE a.codiVisi = vs.codiVisi and vs.codiCita = :codiCita and a.estaAlumVisi = 1");
        q.setParameter("codiCita", codiCita);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public Alumnovisitante findByAlumVisi(Visitante visi, String carn) {
        TypedQuery<Alumnovisitante> q = (TypedQuery<Alumnovisitante>) getEntityManager().createQuery("SELECT a FROM Alumnovisitante a  WHERE a.codiVisi = :codiVisi and a.carnAlum = :carnAlum and a.estaAlumVisi = 1").setMaxResults(1);
        if(visi==null)visi = new Visitante();
        if(carn==null)carn="null";
        q.setParameter("codiVisi", visi);
        q.setParameter("carnAlum", carn);
        Alumnovisitante resu = q.getSingleResult();
        return (resu==null)? new Alumnovisitante() : resu;
    }
}
