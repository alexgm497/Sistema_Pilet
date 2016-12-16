/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.modelo.Donacion;
import com.sv.udb.ejb.DonacionFacadeLocal;
import com.sv.udb.ejb.TransaccionFacadeLocal;
import com.sv.udb.modelo.Transaccion;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Named(value = "donacionBean")
@ViewScoped
public class DonacionBean implements Serializable{

    @EJB
    private TransaccionFacadeLocal FCDETran;
    @EJB
    private DonacionFacadeLocal FCDEDona;
    private Donacion objeDona;
    private List<Donacion> listDona;
    private List<Donacion> listDonaEmpr;
    private boolean guardar;
    private boolean empresa = false;
    private boolean tipo = false;
    private boolean donacion = false;
    private static Logger log = Logger.getLogger(DonacionBean.class);

    public boolean isTipo() {
        return tipo;
    }

    public boolean isDonacion() {
        return donacion;
    }
    
    
    
    public Donacion getObjeDona() {
        return objeDona;
    }

    public boolean isEmpresa() {
        return empresa;
    }

    
    
    public void setObjeDona(Donacion objeDona) {
        this.objeDona = objeDona;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public List<Donacion> getListDona() {
        return listDona;
    }

    public List<Donacion> getListDonaEmpr() {
        return listDonaEmpr;
    }
    
    
    
    /**
     * Creates a new instance of DonacionBean
     */
    public DonacionBean() {
    }
    
    private EmpresaBean objeEmpr;
    
    @PostConstruct
    public void init()
    {
        this.objeDona = new Donacion();
        this.guardar = true;        
        if (FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("empresaBean") != null) {
            objeEmpr = (EmpresaBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("empresaBean");
        } 
        this.consTodo(); 
   }
    
    public void limpForm()
    {
        this.objeDona = new Donacion();        
        this.objeDona.setFechDona(new Date());
        this.objeDona.setMontTot(BigDecimal.ZERO);
        this.objeDona.setMontPend(BigDecimal.ZERO);
        this.objeDona.setCantCuot(BigDecimal.ZERO);        
        this.guardar = true;        
    }
    
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            BigDecimal total = this.objeDona.getCantCuot().multiply(BigDecimal.valueOf(objeDona.getPlazDona()));
            this.objeDona.setMontTot(total);
            char recaudacion=  this.objeDona.getCodiTipoDona().getRecaTipoDona();
            if ( recaudacion== 'F') {
                this.objeDona.setMontPend(total);
            } else {
                objeDona.setMontPend(BigDecimal.ZERO);
            }
            
            this.objeDona.setEstaDona(1);
            FCDEDona.create(this.objeDona);
            this.listDona.add(this.objeDona);
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
           // log.info("Donacion guardada");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
          //  log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void guar2()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            
            this.objeDona.setCodiEmpr(objeEmpr.getObjeEmpr());
            BigDecimal total = this.objeDona.getCantCuot().multiply(BigDecimal.valueOf(objeDona.getPlazDona()));
            this.objeDona.setMontTot(total);
            char recaudacion=  this.objeDona.getCodiTipoDona().getRecaTipoDona();
            if ( recaudacion== 'F') {
                this.objeDona.setMontPend(total);
            } else {
                objeDona.setMontPend(BigDecimal.ZERO);
            }
            
            this.objeDona.setEstaDona(1);
            FCDEDona.create(this.objeDona);
             if(this.listDona == null)
                    {
                        this.listDona = new ArrayList<>();
                    }
            this.listDona.add(this.objeDona);
            this.limpForm();
            this.consTodo();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
        //    log.info("Donacion guardada");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar D')");
         //   log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            BigDecimal total = this.objeDona.getCantCuot().multiply(BigDecimal.valueOf(objeDona.getPlazDona()));
            //Actualización del tipo de donacion
            char recaudacion=  this.objeDona.getCodiTipoDona().getRecaTipoDona();
            if ( recaudacion == 'F') {
                this.objeDona.setMontPend(total);
            } else {
                objeDona.setMontPend(BigDecimal.ZERO);
            }
            //Operaciones para el monto pendiente
            //Crear la variable 
            BigDecimal pagoCanc = FCDETran.findMonto(objeDona.getCodiDona());
            if (pagoCanc == null) {
                this.objeDona.setMontPend(total);
            }
            else{
                this.objeDona.setMontPend(total.subtract(pagoCanc));
            }
            this.listDona.remove(this.objeDona); //Limpia el objeto viejo
            if(this.objeDona.getMontPend().compareTo(BigDecimal.valueOf(0))!=0 ||this.objeDona.getMontPend().compareTo(BigDecimal.valueOf(0.00))!=0 )
            {
                 this.objeDona.setEstaDona(1);
            }  
            FCDEDona.edit(this.objeDona);
            this.listDona.add(this.objeDona); //Agrega el objeto modificado
            this.consTodo();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
        //    log.info("Donacion Modificada");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
         //   log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void elim()
    {
        System.out.println("He entrado a eliminar donacion");
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeDona.setEstaDona(0);
            this.listDona.remove(this.objeDona); //Limpia el objeto viejo
            FCDEDona.edit(this.objeDona);
            
            this.consTodo();
           //this.listDona.add(this.objeDona); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
       //     log.info("Donacion Eliminada");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        //    log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void consTodo()
    {
        try
        {
            //aqui va consulta personalizada
            this.listDona = FCDEDona.findAll();
            this.listDonaEmpr = FCDEDona.findDona(objeEmpr.getObjeEmpr().getCodiEmpr());
        //    log.info("Donaciones Consultadas");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error(getRootCause(ex).getMessage());
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
            this.objeDona = FCDEDona.find(codi);
            System.out.println(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeDona.getMontTot()) + "')");
        //    log.info("Donacion Consultada");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        //    log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void dona()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.donacion = !this.donacion;
        
    }
    
    public void empr()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.empresa = !this.empresa;
        this.donacion = !this.donacion;
    }
    public void tipo()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.tipo = !this.tipo;
        this.donacion = !this.donacion;
    }
    
}
