/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.modelo.DetalleBeca;
import com.sv.udb.ejb.DetalleBecaFacadeLocal;
import com.sv.udb.ejb.TipoBecaFacadeLocal;
import com.sv.udb.modelo.Beca;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.modelo.TipoBeca;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eduardo
 */
@Named(value = "detalleBecaBean")
@ViewScoped
public class DetalleBecaBean implements Serializable{

    @EJB
    private TipoBecaFacadeLocal FCDETipoBeca;
    @EJB
    private DetalleBecaFacadeLocal FCDEDetaBeca;
    private DetalleBeca objeDetaBeca;
   
    private List<DetalleBeca> listDetaBeca;
    private boolean guardar;      
    

    public DetalleBeca getObjeDetaBeca() {
        return objeDetaBeca;
    }

    public void setObjeDetaBeca(DetalleBeca objeDetaBecaBeca) {
        this.objeDetaBeca = objeDetaBecaBeca;
    }

    public List<DetalleBeca> getListDetaBeca() {
        return listDetaBeca;
    }

    public void setListDetaBeca(List<DetalleBeca> listDetaBeca) {
        this.listDetaBeca = listDetaBeca;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }
    
  
    
    /**
     * Creates a new instance of DetalleBecaBean
     */
    public DetalleBecaBean() {
    }
    
    private BecasBean objeBeca;
     
    @PostConstruct
    public void init()
    {
        this.objeDetaBeca = new DetalleBeca();       
        this.guardar = true;                
        if (FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean") != null) {
            System.out.println("Entro en el init de detalle beca");
            objeBeca = (BecasBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean");
        }
        this.consTodo();
    }
    
    public void limpForm()
    {
        this.objeDetaBeca = new DetalleBeca();
        this.guardar = true;        
    }
    
    public void guar()
    {
         boolean guardarDeta;
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if (this.objeDetaBeca.getCantMese() > 0) 
            {        
                System.out.println(this.objeDetaBeca.getCodiTipoBeca().getTipoTipoBeca());
                switch (this.objeDetaBeca.getCodiTipoBeca().getTipoTipoBeca()) {
                    case 1:
                        //Matricula
                        if (this.objeDetaBeca.getCantMese() > 1) {
                            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'No se puede entregar matrícula más de una vez')");
                            guardarDeta = false;
                        } else {
                            guardarDeta = true;
                        }
                        break;
                    case 2:
                        //Mensualidad
                        if (this.objeDetaBeca.getCantMese() > 11) {
                            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'No se puede entregar más de 11 mensualidades')");
                            guardarDeta = false;
                        } else {
                            guardarDeta = true;
                        }
                        break;
                    case 3:
                        guardarDeta = true;
                        break;
                    default:
                        guardarDeta = false;
                        break;
                }
                this.objeDetaBeca.setCodiBeca(this.objeBeca.getObjeBeca());
                if (guardarDeta) {
                    int resuVali=this.FCDEDetaBeca.validar(this.objeBeca.getObjeBeca().getCodiBeca(), this.objeDetaBeca.getCodiTipoBeca().getCodiTipoBeca());                 
                    if(resuVali>= 1)
                    {
                        ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El alumno ya tiene asignado este tipo de beca.')");
                    }
                    else
                    {
                        this.objeDetaBeca.setCodiBeca(this.objeBeca.getObjeBeca());
                        this.objeDetaBeca.setEstaDetaBeca(1);
                        this.FCDEDetaBeca.create(objeDetaBeca);
                        if(this.listDetaBeca == null)
                            {
                                this.listDetaBeca = new ArrayList<>();
                            }
                        this.listDetaBeca.add(this.objeDetaBeca);
                        this.limpForm();
                        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
                        ctx.execute("$('#ModaForm').modal('hide');");
                        //log.info("Detalle Guardado");
                    }
                }
                else
                {
                    ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
                }
            }
            else 
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'La cantidad de meses no puede ser 0')");
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            System.out.println(getRootCause(ex).getMessage());
            //log.error("Error en "+ getRootCause(ex).getMessage());
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
            this.listDetaBeca.remove(this.objeDetaBeca); //Limpia el objeto viejo
            FCDEDetaBeca.edit(this.objeDetaBeca);
            this.listDetaBeca.add(this.objeDetaBeca); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            ctx.execute("$('#ModaForm').modal('hide');");
            //log.info("Detalle Modificado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            //log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeDetaBeca.setEstaDetaBeca(0);
            this.listDetaBeca.remove(this.objeDetaBeca); //Limpia el objeto viejo
            FCDEDetaBeca.edit(this.objeDetaBeca);
            this.listDetaBeca.add(this.objeDetaBeca); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            ctx.execute("$('#ModaForm').modal('hide');");
            //log.info("Detalle Modificado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            //log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    public void reActi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeDetaBeca.setEstaDetaBeca(1);
            this.listDetaBeca.remove(this.objeDetaBeca); //Limpia el objeto viejo
            FCDEDetaBeca.edit(this.objeDetaBeca);
            this.listDetaBeca.add(this.objeDetaBeca); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            ctx.execute("$('#ModaForm').modal('hide');");
            //log.info("Detalle Modificado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            //log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
     private List<TipoBeca> listTipoBeca;

    public List<TipoBeca> getListTipoBeca() {
        return listTipoBeca;
    }

    public void setListTipoBeca(List<TipoBeca> listTipoBeca) {
        this.listTipoBeca = listTipoBeca;
    }
    
    public void consTodo()
    {
        try          
        {
            System.out.println("Codigo beca: "+ objeBeca.getObjeBeca().getCodiBeca());
            this.listDetaBeca = FCDEDetaBeca.findByBeca(objeBeca.getObjeBeca().getCodiBeca());            
            listTipoBeca = FCDETipoBeca.findTipos(objeBeca.getObjeBeca().getCodiSoliBeca().getCodiGrad().getNivelGrad());
            //log.info("Detalles Consultados");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            //log.error(getRootCause(ex).getMessage());
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
            this.objeDetaBeca = FCDEDetaBeca.find(codi);
            System.out.println("Detalle:"+objeDetaBeca.getCodiTipoBeca().getCodiTipoBeca());
           System.out.println("Meses"+objeDetaBeca.getCantMese());
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeDetaBeca.getCantMese()) + "')");
            //log.info("Detalle Consultado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
            //log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
}
