/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;
import com.sv.udb.controlador.VisitantesBean;
import com.sv.udb.ejb.CambiocitaFacadeLocal;
import com.sv.udb.ejb.CitaFacadeLocal;
import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.modelo.Cita;
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
    private ScheduleModel objeCale;
    private List<Cita> listCita;
    private ScheduleEvent citaEven;
    private Cambiocita objeCambCita;
    private String fechForma;

   
    public String getFechForma() {
        return fechForma;
    }

    public void setFechForma(String fechForma) {
        this.fechForma = fechForma;
    }
    
    public void onEventSelect(SelectEvent selectEvent)
    {
        this.citaEven= (ScheduleEvent) selectEvent.getObject();
        this.objeCambCita = (Cambiocita) citaEven.getData();
        this.fechForma = new SimpleDateFormat("dd/MM/yyyy").format(this.objeCambCita.getFechFinCitaNuev());
    }
    
    public Cambiocita getObjeCambCita() {
        return objeCambCita;
    }

    public void setObjeCambCita(Cambiocita objeCambCita) {
        this.objeCambCita = objeCambCita;
    }
    
    
    public List<Cita> getListCita() {
        return listCita;
    }

    public void setListCita(List<Cita> listCita) {
        this.listCita = listCita;
    }

    public ScheduleEvent getCitaEven() {
        return citaEven;
    }

    public void setCitaEven(ScheduleEvent citaEven) {
        this.citaEven = citaEven;
    }
    
    @EJB
    private CitaFacadeLocal FCDECita;
    @EJB
    private CambiocitaFacadeLocal FCDECambCita;
    
    public CalendarioBean() {
    }
    @PostConstruct
    public void init()
    {
        objeCale = new DefaultScheduleModel();
        listCita = new ArrayList<Cita>();
        //this.consTodo();
        getEvenCale();
    }
    
    public ScheduleModel getObjeCale() {
        return objeCale;
    }
    
    public void conListCitaProg()
    {
        try
        {
            this.listCita = FCDECita.findCitaCale(LoginBean.getObjeWSconsEmplByAcce().getCodi());
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
     public void getEvenCale(){
        try{
            this.objeCale = new DefaultScheduleModel();
            conListCitaProg();//se consultan las citas programadas
            if(listCita == null) listCita= new ArrayList<Cita>();
            for(Cita obj: listCita){//--> recorrer lista
                Cambiocita  objeCambCita = getCambCita(obj);
                if(objeCambCita == null)objeCambCita = new Cambiocita();
                
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String FechFinaS = df.format(objeCambCita.getFechFinCitaNuev())+" "+objeCambCita.getHoraFinCitaNuev();
                String FechInicS = df.format(objeCambCita.getFechInicCitaNuev())+" "+objeCambCita.getHoraInicCitaNuev();
                DefaultScheduleEvent evt = new DefaultScheduleEvent();
                evt.setStartDate(this.getFecha(FechInicS));
                evt.setEndDate(this.getFecha(FechFinaS));
                evt.setTitle("Cita");
                evt.setData(objeCambCita);
                evt.setDescription(obj.getCodiUbic().getNombUbic());
                
                System.out.println(objeCambCita.getFechInicCitaNuev()+"");
                this.objeCale.addEvent(evt);
             }
        }catch(Exception e){
            e.printStackTrace();
        }  
     }
     
     
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
                System.out.println(fecha);
            } catch (Exception e) {
                fecha = null;
            }
        }
        return fecha;
    }
    
}
