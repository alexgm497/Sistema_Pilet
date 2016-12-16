/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.PermisoRolFacadeLocal;
import com.sv.udb.ejb.RolFacadeLocal;
import com.sv.udb.modelo.Permiso;
import com.sv.udb.modelo.PermisoRol;
import com.sv.udb.modelo.Rol;
import com.sv.udb.utils.LOG4J;
import java.io.Serializable;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import com.sv.udb.utils.MailSender;

/**
 * Esta clase se encarga de manejar lo relacionado al CRUD de roles
 * @author: AGAV Team
 * @version: Prototipo 1
 */
@Named(value = "rolBean")
@ViewScoped
@ManagedBean
public class RolBean implements Serializable {

    //Bean de session
    @Inject
    private LoginBean logiBean;
    
    @Inject
    private EnvioCorreosBean correosBean;
    //Campos de la clase
    @EJB
    private RolFacadeLocal FCDERoles;
   @EJB
    private PermisoRolFacadeLocal FCDPermisoRol;
   
    private List<Rol> listRole;
    private Rol objeRole;
    private boolean guardar;
    
    private LOG4J<RolBean> lgs = new LOG4J<RolBean>(RolBean.class) {
    };
    private Logger log = lgs.getLog();
    
    //Encapsulamiento de los campos de la clase
    
    public RolFacadeLocal getFCDERoles() {
        return FCDERoles;
    }

    public void setFCDERoles(RolFacadeLocal FCDERoles) {
        this.FCDERoles = FCDERoles;
    }

    public List<Rol> getListRole() {
        return listRole;
    }

    public void setListRole(List<Rol> listRole) {
        this.listRole = listRole;
    }

    public Rol getObjeRole() {
        return objeRole;
    }

    public void setObjeRole(Rol objeRole) {
        this.objeRole = objeRole;
    }

    public boolean isGuardar() {
        return guardar;
    }
    
    /**
     * Constructor de la clase RolBean
    */
    public RolBean() {
    }
    
    /**
    * Método que se ejecuta después de haber construido la clase e inicializa las variables
    */
    @PostConstruct
    public void init()
    {
        this.listRole = FCDERoles.findAll();
        this.limpForm();
        //log = new LOG4J();
        //log.debug("Se inicializa el modelo de Rol");
    }
    
    /**
     * Método que limpia el formulario reiniciando las variables
     */
    public void limpForm()
    {
        this.objeRole = new Rol();
        this.guardar = true;        
    }
   
    /**
     * Método que guarda la información en la base de datos
     */
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            FCDERoles.create(this.objeRole);
            PermisoRol objePermRole = new PermisoRol();
            Permiso objePerm = new Permiso();
            objePermRole.setCodiRole(objeRole);
            objePerm.setCodiPerm(1);
            objePermRole.setCodiPerm(objePerm);
            objePermRole.setEstaPermRole(1);
            FCDPermisoRol.create(objePermRole);
            this.listRole.add(this.objeRole);
            this.guardar = false;
            log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"Roles"+"-"+"Rol creado: "+this.objeRole.getNombRole());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"Roles"+"-"+"Error creando rol: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    /**
     * Método que modifica la información en la base de datos
     */
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listRole.remove(this.objeRole); //Limpia el objeto viejo
            FCDERoles.edit(this.objeRole);
            this.listRole.add(this.objeRole); //Agrega el objeto modificado
            log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"Roles"+"-"+"Rol modificado: "+this.objeRole.getCodiRole());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"Roles"+"-"+"Error modificando rol: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    /**
     * Método que consulta la información de la base de datos
     */
    public void consTodo()
    {
        try
        {
            this.listRole = FCDERoles.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
    }
    
    /**
     * Método que consulta la información de un registro específico de la base de datos
     */
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeRole = FCDERoles.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " +this.objeRole.getNombRole()+ "')");
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
