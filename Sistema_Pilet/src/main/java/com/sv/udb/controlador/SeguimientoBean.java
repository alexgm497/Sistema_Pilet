/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.modelo.Seguimiento;
import com.sv.udb.ejb.SeguimientoFacadeLocal;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ferna
 */
@Named(value = "seguimientoBean")
@ViewScoped
public class SeguimientoBean implements Serializable{
    @EJB
    private SeguimientoFacadeLocal FCDESegu;
    private Seguimiento objeSegu;
    private List<Seguimiento> listSegu;
    private boolean guardar;  
    private static Logger log = Logger.getLogger(SeguimientoBean.class);
    public Seguimiento getObjeSegu() {
        return objeSegu;
    }

    public void setObjeSegu(Seguimiento objeSegu) {
        this.objeSegu = objeSegu;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public List<Seguimiento> getListSegu() {
        return listSegu;
    }
    
    

    /**
     * Creates a new instance of SeguimientoBean
     */
    public SeguimientoBean() {
    }
    
    private BecasBean objeBeca;
      private EmpresaBean objeEmpr;
    
    @PostConstruct
    public void init()
    {
        
        this.objeSegu = new Seguimiento();
       this.listSegu = new ArrayList<>();                    
        this.guardar = true;
        if (FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean") != null) {
            objeBeca = (BecasBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean");
            System.out.println("Dato de seuimeinto: "+objeBeca.getObjeSoli().getCarnAlum());
        }
        if (FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("empresaBean") != null) {
            objeEmpr = (EmpresaBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("empresaBean");
        }         
        this.consTodo();        
        
    }
    
    public void limpForm()
    {
        
        this.objeSegu = new Seguimiento();
        this.objeSegu.setFechInicio(new Date());
        this.objeSegu.setFechFin(new Date());
     
        this.guardar = true;        
    }
    
    public void guar()
    {
        
            if(this.objeBeca != null)
            {
                 this.objeSegu.setCodiSoliBeca(this.objeBeca.getObjeSoli());
            }
            if(this.objeEmpr != null)
            {
               this.objeSegu.setCodiEmpr(this.objeEmpr.getObjeEmpr());
                 
            }
       
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeSegu.setFechSegu(new Date());
            this.objeSegu.setEstaSegu(1);
            this.FCDESegu.create(this.objeSegu);          
            if(this.listSegu == null)
            {
             this.listSegu = new ArrayList<>();   
            }
            this.listSegu.add(this.objeSegu);
            
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
             ctx.execute("$('#ModaSeguForm').modal('hide');");
            
          //  log.info("Seguimiento Guardado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
           // log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void modi()
    {
         if(this.objeBeca != null)
            {
                 this.objeSegu.setCodiSoliBeca(this.objeBeca.getObjeSoli());
            }
            if(this.objeEmpr != null)
            {
               this.objeSegu.setCodiEmpr(this.objeEmpr.getObjeEmpr());
                 
            }
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listSegu.remove(this.objeSegu); 
            FCDESegu.edit(this.objeSegu);
            
                this.listSegu.add(this.objeSegu);
            
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
           // log.info("Seguimiento Modificado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
          //  log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void elim()
    {
         if(this.objeBeca != null)
            {
                 this.objeSegu.setCodiSoliBeca(this.objeBeca.getObjeSoli());
            }
            if(this.objeEmpr != null)
            {
               this.objeSegu.setCodiEmpr(this.objeEmpr.getObjeEmpr());
                 
            }
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeSegu.setEstaSegu(0);
            FCDESegu.remove(this.objeSegu);
                this.listSegu.remove(this.objeSegu);
            
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
          //  log.info("Seguimiento Eliminado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
          //  log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void consTodo()
    {
        try
        {
            
            if(this.objeBeca != null)
            {
                  this.listSegu = FCDESegu.findBySoliInSpec(this.objeBeca.getObjeBeca().getCodiBeca());
            }
            if(this.objeEmpr != null)
            {
                this.listSegu = FCDESegu.findByEmprInSpec(this.objeEmpr.getObjeEmpr().getCodiEmpr());
                 
            }
            
          //  log.info("Seguimientos Consultados");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
          //  log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void cons()
    {
        
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeSegu = FCDESegu.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
            String.format("%s", this.objeSegu.getDescSegu()) + "')");
          //  log.info("Seguimiento Consultado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
         //   log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
}
