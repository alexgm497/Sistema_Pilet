/*
 * Controlador eventos
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.EventoFacadeLocal;
import com.sv.udb.modelo.Evento;
import com.sv.udb.controlador.LoginBean;
import com.sv.udb.utils.LOG4J;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sistema de citas
 * @version 1.0 13 de Octubre de 2016
 */
@Named(value = "eventosBean")
@ViewScoped
public class EventosBean implements Serializable{
   
    
    public EventosBean() {
        
    }
    //Bean Sesion
    @Inject
    private LoginBean logiBean;  
    
    
    @EJB
    private EventoFacadeLocal FCDEEven;    
    private Evento objeEven;
    private List<Evento> listEven;
    private boolean guardar;
    
    
    private LOG4J<EventosBean> lgs = new LOG4J<EventosBean>(EventosBean.class) {
    };
    private Logger log = lgs.getLog();
    
    public Evento getObjeEven() {
        return objeEven;
    }

    public void setObjeEven(Evento objeEven) {
        this.objeEven = objeEven;
    }

    public List<Evento> getListEven() {
        return listEven;
    }

    public boolean isGuardar() {
        return guardar;
    }
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consTodo();
    }
    //Limpiando el formulario
    public void limpForm()
    {
        this.objeEven = new Evento();
        this.guardar = true;        
    }
    
    public void consTodo()
    {
        try
        {
            this.listEven = FCDEEven.findByEsta(1);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void cons()
    {
        //Capturo el contexto de la página
        RequestContext ctx = RequestContext.getCurrentInstance(); 
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeEven = FCDEEven.find(codi); //Encontrando el codigo
            this.guardar = false;
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Eventos"+"-"+" Consultar evento: " + objeEven.getNombEven());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Registro Consultado')");
        }
        catch(Exception ex)
        {
            log.error("Error al consultar evento");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
     /**
     * Método que guarda un objeto del tipo evento en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */   
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {            
            if(validar())
            {
                FCDEEven.create(this.objeEven);
                this.listEven.add(this.objeEven);
                log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Eventos"+"-"+" Agregado evento: " + objeEven.getNombEven());
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
                limpForm();
            }
        }
        catch(Exception ex)
        {
            log.error("Error al registar evento");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
        }
    }
    
     /**
     * Método que modifica un objeto del tipo evento en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */       
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(validar())
            {
                this.listEven.remove(this.objeEven); //Limpia el objeto viejo
                FCDEEven.edit(this.objeEven);
                this.listEven.add(this.objeEven); //Agrega el objeto modificado
                log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Eventos"+"-"+" Modificar evento: " + objeEven.getNombEven());
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            }
        }
        catch(Exception ex)
        {
            log.error("Error al modificar evento");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }
    
     /**
     * Método boleano para validar ls fecha y hora
     * Para que no se pueda colocar una fecha u hora antes de la actual.
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */  
    private boolean validar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        try
        {
            //si las fechas son correctas
            if((this.objeEven.getFechFinaEven().after(this.objeEven.getFechInicEven())))
            {
                return true;
            }
            else
            {   //si es el mismo dia pero con horas correctas
                if((this.objeEven.getFechFinaEven().equals(this.objeEven.getFechInicEven())&&formatter.parse(this.objeEven.getHoraFinaEven()).after(formatter.parse(this.objeEven.getHoraInicEven())))){
                    return true;
                //si las fechas son correctas pero las horas son incorrectas
                }else if((this.objeEven.getFechFinaEven().equals(this.objeEven.getFechInicEven()))&& !formatter.parse(this.objeEven.getHoraFinaEven()).after(formatter.parse(this.objeEven.getHoraInicEven()))){
                    FacesContext.getCurrentInstance().addMessage("FormRegi:horaInicEven", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La hora Final no puede ser antes de la Inicial",  null));
                    FacesContext.getCurrentInstance().addMessage("FormRegi:horaFinaEven", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La hora Final no puede ser antes de la Inicial",  null));
                //si las fechas son incorrectas
                }else{
                    FacesContext.getCurrentInstance().addMessage("FormRegi:fechInic", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La Fecha Final no puede ser antes de la Inicial",  null));
                    FacesContext.getCurrentInstance().addMessage("FormRegi:fechFina", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La Fecha Final no puede ser antes de la Inicial",  null));
                }
                
                return false;
            }
        }
        catch(Exception err)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Horas no válidas')");
            return false;
        }
    }
  
     /**
     * Método que elimina un objeto del tipo evento en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */    
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeEven.setEstaEven(0);
            this.listEven.remove(this.objeEven);
            FCDEEven.edit(this.objeEven);
            this.listEven.add(this.objeEven);
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Eventos"+"-"+" Eliminar evento codigo: " + objeEven.getCodiEvent());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
        }
        catch(Exception ex)
        {
            log.error("Error al eliminar evento");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }
}