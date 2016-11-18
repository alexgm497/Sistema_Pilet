/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.CambiocitaFacadeLocal;
import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.utils.LOG4J;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;


 /**
 * La clase cambio de cita 
 * @author: ControlCitas
 * @version: Prototipo 1
 * Septiembre 2016
 */
@Named(value = "cambioCitaBean")
@ViewScoped
public class CambioCitaBean implements Serializable{
   
    
    public CambioCitaBean() {
        
    }
    
    @Inject
    private LoginBean logiBean;
    
    @EJB
    private CambiocitaFacadeLocal FCDECambCita;    
    private Cambiocita objeCambCita;
    private List<Cambiocita> listCambCita;
    private boolean guardar;
    
    private LOG4J<CambioCitaBean> lgs = new LOG4J<CambioCitaBean>(CambioCitaBean.class) {
    };
    private Logger log = lgs.getLog();
    
    public Cambiocita getObjeCambCita() {
        return objeCambCita;
    }

    public void setObjeCambCita(Cambiocita objeCambCita) {
        this.objeCambCita = objeCambCita;
    }

    public List<Cambiocita> getListCambCita() {
        return listCambCita;
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
    
    public void limpForm()
    {
        this.objeCambCita = new Cambiocita();
        this.guardar = true;        
    }
    
    public void consTodo()
    {
        try
        {
            this.listCambCita = FCDECambCita.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeCambCita = FCDECambCita.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Registro Consultado)");
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"CambioCita"+"-"+" Consultar cambio cita codigo: " + objeCambCita.getCodiCambCita());
        }
        catch(Exception ex)
        {
            log.error("Error al consultar cambio cita", ex);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
        finally
        {
            
        }
    }
    
    
 /**
 * Metodo para guardar los datos de cambio de cita
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            FCDECambCita.create(this.objeCambCita);
            this.listCambCita.add(this.objeCambCita);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"CambioCita"+"-"+" Agregar cambio cita codigo: " + objeCambCita.getCodiCambCita());
            this.limpForm();
        }
        catch(Exception ex)
        {
            log.error("Error al guardar cambio cita", ex);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
        }
    }
    
     /**
 * Metodo para modificar los datos de cambio de cita
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listCambCita.remove(this.objeCambCita); //Limpia el objeto viejo
            FCDECambCita.edit(this.objeCambCita);
            this.listCambCita.add(this.objeCambCita); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"CambioCita"+"-"+" Modificar cambio cita codigo: " + objeCambCita.getCodiCambCita());
            this.limpForm();
        }
        catch(Exception ex)
        {
            log.error("Error al modificar cambio cita", ex);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }
 /**
 * Metodo para eliminar los datos de cambio de cita
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            FCDECambCita.remove(this.objeCambCita);
            this.listCambCita.remove(this.objeCambCita);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"CambioCita"+"-"+" Eliminar cambio cita codigo: " + objeCambCita.getCodiCambCita());
            this.limpForm();
        }
        catch(Exception ex)
        {
            log.error("Error al eliminar cambio cita", ex);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }
}
