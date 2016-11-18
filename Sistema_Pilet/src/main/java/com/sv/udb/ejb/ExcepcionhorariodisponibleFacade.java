package com.sv.udb.ejb;
import com.sv.udb.modelo.Excepcionhorariodisponible;
import com.sv.udb.modelo.Horariodisponible;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


/**
 * @author Control citas
 * Facade: Excepcion de horarios disponibles 
 * Prototipo 1
 */
@Stateless
public class ExcepcionhorariodisponibleFacade extends AbstractFacade<Excepcionhorariodisponible> implements ExcepcionhorariodisponibleFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExcepcionhorariodisponibleFacade() {
        super(Excepcionhorariodisponible.class);
    }
    @Override
    public boolean findByDispHora(Horariodisponible hora, Date fech) {
        TypedQuery<Horariodisponible> q = (TypedQuery<Horariodisponible>) getEntityManager().createQuery("SELECT e FROM Excepcionhorariodisponible e WHERE e.codiHoraDisp in(select h from Horariodisponible h where h = :horaDisp and h.estaHoraDisp = 1) and e.fechExceHoraDisp = :fech");       
        q.setParameter("fech", fech);
        q.setParameter("horaDisp", hora);
        List resu = q.getResultList();
        return resu.isEmpty();
    }
    @Override
    public List<Excepcionhorariodisponible> findByCodiUsua(int codiUsua) {
        TypedQuery<Excepcionhorariodisponible> q = (TypedQuery<Excepcionhorariodisponible>) getEntityManager().createQuery("SELECT e FROM Excepcionhorariodisponible e WHERE e.codiHoraDisp in(Select h from Horariodisponible h where h.codiUsua = :codiUsua and h.estaHoraDisp = 1)");       
        q.setParameter("codiUsua", codiUsua);
        List resu = q.getResultList();
        return resu.isEmpty() ? null: resu;
    }
    
}
