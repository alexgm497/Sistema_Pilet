/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;
import com.sv.udb.controlador.VisitantesBean;
import com.sv.udb.ejb.CambiocitaFacadeLocal;
import com.sv.udb.ejb.CitaFacadeLocal;
import com.sv.udb.ejb.VisitanteFacadeLocal;
import com.sv.udb.ejb.VisitantecitaFacadeLocal;
import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.modelo.Cita;
import com.sv.udb.modelo.Visitante;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

 /**
 * La clase calendario para citas
 * @author: ControlCitas
 * @version: Prototipo 2
 */
@Named(value = "calendarioBean")
@ViewScoped
public class CalendarioBean implements Serializable{

    /**
     * Creaando una nueva instancia para la clase Calendario
     */
    
    private static final long serialVersionUID = 6527333208194203406L;
    private List<Cambiocita> listCambioCita;
    private ScheduleEvent citaEven;
    private Cambiocita objeCambCita;
    private String fechForma;

   private ScheduleModel lazyEventModel;

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }
    
    public String getFechForma() {
        return fechForma;
    }

    public void setFechForma(String fechForma) {
        this.fechForma = fechForma;
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
        this.objeCambCita = (Cambiocita) event.getData();
    }
    
    public Cambiocita getObjeCambCita() {
        return objeCambCita;
    }

    public void setObjeCambCita(Cambiocita objeCambCita) {
        this.objeCambCita = objeCambCita;
    }

    public List<Cambiocita> getListCambioCita() {
        return listCambioCita;
    }

    public void setListCambioCita(List<Cambiocita> listCambioCita) {
        this.listCambioCita = listCambioCita;
    }
    
    public ScheduleEvent getCitaEven() {
        return citaEven;
    }

    public void setCitaEven(ScheduleEvent citaEven) {
        this.citaEven = citaEven;
    }
    
    @EJB
    private CambiocitaFacadeLocal FCDECambCita;
    @EJB
    private VisitanteFacadeLocal FCDEVisitante;
    
    public CalendarioBean() {
    }
    @PostConstruct
    public void init()
    {
        this.lazyEventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                try
                {
                    conListCambioCitaProg(start,end);
                    for(Cambiocita obj: listCambioCita){//--> recorrer lista
                    objeCambCita=obj;
                    if(objeCambCita == null)objeCambCita = new Cambiocita();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String FechFinaS = df.format(objeCambCita.getFechFinCitaNuev())+" "+objeCambCita.getHoraFinCitaNuev();
                    String FechInicS = df.format(objeCambCita.getFechInicCitaNuev())+" "+objeCambCita.getHoraInicCitaNuev();
                    DefaultScheduleEvent evt = new DefaultScheduleEvent();
                    evt.setStartDate(getFecha(FechInicS));
                    evt.setEndDate(getFecha(FechFinaS));
                    evt.setTitle("Cita");
                    evt.setData(obj);
                    evt.setDescription(obj.getCodiCita().getCodiUbic().getNombUbic());
                    this.addEvent(evt);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }  
            }   
        };
        listCambioCita = new ArrayList<Cambiocita>();
    }
    
    
    public void conListCambioCitaProg(Date fechInic, Date fechFina)
    {
        try
        {
            this.listCambioCita = FCDECambCita.findCambioCitaCale(fechInic,fechFina,LoginBean.getObjeWSconsEmplByAcce().getCodi());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
   
    /**
     * Metodo para llamar los datos y mostrar en el calendario esos datos.
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
     
     
    /**
     * Metodo de cambio de cita
     */
    public Cambiocita getCambCita(Cita cita)
    {
        Cambiocita objeCambCita = null;
        try
        {
            objeCambCita = FCDECambCita.findByCita(cita);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return objeCambCita;
    }
    
    private List<Visitante> ListVisi;

    public List<Visitante> getListVisi() {
        if(this.objeCambCita!=null)
        {
            this.ListVisi = new ArrayList<Visitante>();
            ListVisi = FCDEVisitante.findByCita(this.objeCambCita.getCodiCita());
        }
        return ListVisi;
    }

    public void setListVisi(List<Visitante> ListVisi) {
        this.ListVisi = ListVisi;
    }
       
    /**
     * Mètodo para obtener la fecha
     */
    private Date getFecha(String date) 
    {
        
        Date fecha = null;
        if (date != null){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                fecha = sdf.parse(date);
            } catch (Exception e) {
                fecha = null;
            }
        }
        return fecha;
    }
    
}
