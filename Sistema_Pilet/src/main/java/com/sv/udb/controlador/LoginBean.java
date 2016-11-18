/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.NotificacionFacadeLocal;
import com.sv.udb.ejb.UsuarioFacadeLocal;
import com.sv.udb.modelo.Usuario;
import com.sv.udb.modelo.Notificacion;
import com.sv.udb.utils.LOG4J;
import com.sv.udb.utils.UsuariosPojo;
import com.sv.udb.utils.pojos.WSconsEmplByCodi;
import com.sv.udb.utils.pojos.WSconsEmplByUser;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

 /**
 * Esta clase se encarga de manejar lo relacionado con los inicios/cierres de sesión
 * @author: AGAV Team
 * @version: Prototipo 1
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    
    //Campos de la clase
    @EJB
    private UsuarioFacadeLocal FCDEUsua;
    
    @EJB
    private NotificacionFacadeLocal FCDENoti;

    private static final long serialVersionUID = 5074501358281220977L;
       
    @Inject
    private GlobalAppBean globalAppBean; //Bean de aplicación
    
    private static Usuario objeUsua;
    private UsuariosPojo usuaPojo;
    private boolean loge;
    private String usua;
    private String cont;
    private String imagPerf;
    private List<Notificacion> listNoti;//Lista de Notificaciones
    private static WSconsEmplByUser objeWSconsEmplByAcce;

    public static WSconsEmplByUser getObjeWSconsEmplByAcce() {
        return objeWSconsEmplByAcce;
    }

    public void setObjeWSconsEmplByAcce(WSconsEmplByUser objeWSconsEmplByAcce) {
        this.objeWSconsEmplByAcce = objeWSconsEmplByAcce;
    }
    
    private LOG4J<LoginBean> lgs = new LOG4J<LoginBean>(LoginBean.class) {
    };
    private Logger log = lgs.getLog();
    
    public LoginBean() {
    }
    
    @PostConstruct
    public void init()
    {
        
    }
    
    //Encapsulamiento de los campos de la clase

    public Usuario getObjeUsua() {
        return objeUsua;
    }

    public void setObjeUsua(Usuario objeUsua) {
        this.objeUsua = objeUsua;
    }
    
    public boolean isLoge() {
        return loge;
    }

    public String getUsua() {
        return usua;
    }

    public void setUsua(String usua) {
        this.usua = usua;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getImagPerf() {
        return imagPerf;
    }

    public List<Notificacion> getListNoti() {
        return listNoti;
    }

    public UsuariosPojo getUsuaPojo() {
        return usuaPojo;
    }

    public void setUsuaPojo(UsuariosPojo usuaPojo) {
        this.usuaPojo = usuaPojo;
    }


    /**
     * Método que crea la sesión
     */
    public void creaSess()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        try
        {
            this.loge = false;
            this.objeUsua = FCDEUsua.findByAcce(this.usua);
            if(this.objeUsua != null)
            {
                WebServicesBean ws = new WebServicesBean();
                usuaPojo = ws.consLogi(usua, cont);
                if(usuaPojo.getNomb() != null){
                    log.info(this.objeUsua.getCodiUsua()+"-"+"Login"+"-"+"Incio de Sesion");
                    ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Bienvenido)"); //No se muestra porque redirecciona
                    this.loge = true;
                    //Cargar una imagen de usuario (Puede ser de una BD)
                    this.imagPerf = "images/userDemo.png";
                    //Llenar lista de notif icaciones.... puede salir de la DB
                    this.listNoti = FCDENoti.findByUsua(this.objeUsua.getCodiUsua());
                    //Redireccionar
                    
                    facsCtxt.getExternalContext().redirect(globalAppBean.getUrl("index.xhtml"));
                }
                else{
                    ctx.execute("setMessage('MESS_WARN', 'Atención', 'Ingreso Incorrecto')");
                }
            }
            else
            {
                ctx.execute("setMessage('MESS_WARN', 'Atención', 'Ingreso Incorrecto')");
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al logear')");
        }
        finally
        {
            
        }        
    }
    
    /**
     * Método que cierra la sesión
     */
    public void cerrSess()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        try
        {
            facsCtxt.getExternalContext().invalidateSession();
            facsCtxt.getExternalContext().redirect(globalAppBean.getUrl("login.xhtml")); 
            log.info(this.objeUsua.getCodiUsua()+"-"+"Login"+"-"+"Fin de Sesion");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al finalizar la sesión')");
            log.error(this.objeUsua.getCodiUsua()+"-"+"Login"+"-"+"Error cerrando Sesion: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }       
    }
}
