/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.NotificacionFacadeLocal;
import com.sv.udb.modelo.Notificacion;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author aleso
 */
@Named(value = "notificacionBean")
@ViewScoped
public class NotificacionBean implements Serializable {
    @Inject
    private LoginBean logiBean; //Bean de session
    
    @EJB
    private NotificacionFacadeLocal FCDENotificacion;
    private Notificacion objeNoti;
    private boolean guardar;

    public Notificacion getObjeNoti() {
        return objeNoti;
    }

    public void setObjeNoti(Notificacion objeNoti) {
        this.objeNoti = objeNoti;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }

    
    /**
     * Creates a new instance of NotificacionBean
     */
    public NotificacionBean() {
    }
    
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeNoti = FCDENotificacion.find(codi);
            logiBean.getListNoti().remove(objeNoti);
            this.objeNoti.setEstaNoti(1);
            logiBean.getListNoti().add(objeNoti);
            FCDENotificacion.edit(this.objeNoti);
            logiBean.consNoti();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
        finally
        {
            
        }
    }
}
