package com.sv.udb.ejb;

import com.sv.udb.modelo.Evento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


/**
 * Facade: Eventos
 * @author Control citas
 * Prototipo 2
 */
@Stateless
public class EventoFacade extends AbstractFacade<Evento> implements EventoFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventoFacade() {
        super(Evento.class);
    }
    
    public List<Evento> findByEsta(int estaEven)
    {
        TypedQuery<Evento> q = (TypedQuery<Evento>) getEntityManager().createQuery("SELECT e FROM Evento e WHERE e.estaEven = :estaEven");      
        q.setParameter("estaEven", estaEven);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : resu;
    }
    
}
