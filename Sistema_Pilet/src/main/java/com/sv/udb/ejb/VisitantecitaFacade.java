package com.sv.udb.ejb;
import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.modelo.Cita;
import com.sv.udb.modelo.Visitante;
import com.sv.udb.modelo.Visitantecita;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    @Override
    public List<Visitantecita> findByNow() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //consultamos los codigos de todas las citas que tengan una programación para hoy
        String query = "SELECT c.codi_cita from cambio_cita c where c.fech_inic_cita_nuev = '"+df.format(new Date())+"' or c.fech_fin_cita_nuev = '"+df.format(new Date())+"'";
        Query q = getEntityManager().createNativeQuery(query);
        List<Object> codiCitaNow = q.getResultList();
        List<Object> nowCodiVali = new ArrayList<Object>();
        //consultamos el ultimo cambio de las citas consultadas, para validar que ese sea el ultimo cambio
        for (Object codi : codiCitaNow) {
            TypedQuery<Cambiocita> qcc = (TypedQuery<Cambiocita>) getEntityManager().createQuery("SELECT c FROM Cambiocita c WHERE c.codiCita.codiCita = :codiCita ORDER BY c.fechCambCita desc, c.horaCambCita desc");
            qcc.setParameter("codiCita", Integer.parseInt(String.valueOf(codi)));
            Cambiocita ccTemp = new Cambiocita();
            if(!qcc.getResultList().isEmpty()) ccTemp = qcc.getResultList().get(0);
            //si el ultimo cambio de la cita consultada es "HOY", y está programada
            if(ccTemp.getCodiCita().getEstaCita() == 2 && (df.format(ccTemp.getFechFinCitaNuev()).equals(df.format(new Date())) || df.format(ccTemp.getFechFinCitaNuev()).equals(df.format(new Date())))){
                //se crea una lista de los codigo cita que realmente son para este dia
                nowCodiVali.add(ccTemp.getCodiCita().getCodiCita());
            }
        }
         List<Visitantecita> lstVisi = new ArrayList<Visitantecita>();
         //ahora consultamos las citas que ya validamos que si son para "HOY"
        for (Object codi : nowCodiVali) {
            TypedQuery<Visitantecita> qv = (TypedQuery<Visitantecita>) getEntityManager().createQuery("SELECT vc FROM Visitantecita vc WHERE vc.codiCita.codiCita = :codiCita");
            qv.setParameter("codiCita", Integer.parseInt(String.valueOf(codi)));
            //consultamos todos los visitante_cita de todas las citas que sean para hoy
            if(!qv.getResultList().isEmpty())lstVisi.addAll(qv.getResultList());
        }
        return (lstVisi.isEmpty()) ? new ArrayList<Visitantecita>() : lstVisi;
    }
    
}