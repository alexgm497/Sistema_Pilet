/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.GradoFacadeLocal;
import com.sv.udb.modelo.TipoBeca;
import com.sv.udb.ejb.TipoBecaFacadeLocal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
@Named(value = "tipoBecaBean")
@ViewScoped
public class TipoBecaBean implements Serializable{

    @EJB
    private GradoFacadeLocal FCDEGrado;
    @EJB
    private TipoBecaFacadeLocal FCDETipo;

    private TipoBeca objeTipo;
    private List<TipoBeca> listTipo;
    private boolean guardar;        
    private static Logger log = Logger.getLogger(TipoBecaBean.class);
    
    boolean guarTipo;
    public TipoBeca getObjeTipo() {
        return objeTipo;
    }

    public void setListTipo(List<TipoBeca> listTipo) {
        this.listTipo = listTipo;
    }

    public void setObjeTipo(TipoBeca objeTipo) {
        this.objeTipo = objeTipo;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public List<TipoBeca> getListTipo() {
        return listTipo;
    }

    /**
     * Creates a new instance of TipoBecaBean
     */
    public TipoBecaBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeTipo = new TipoBeca();
        this.guardar = true;
        this.consTodo();
        this.guarTipo = false;
    }
    
    public void limpForm()
    {
        this.objeTipo = new TipoBeca();
        this.guardar = true;        
    }
    
    /**
     * Metodo que guarda un registro en la tabla Tipo Beca
     */
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        BigDecimal matr;
        BigDecimal mens;
        try
        {
            if(this.FCDETipo.findByName(this.objeTipo.getNombTipoBeca()) == null)
            {
                if (this.objeTipo.getDescTipoBeca().compareTo(BigDecimal.ZERO) == 1) {
                    switch (this.objeTipo.getTipoTipoBeca()) {
                        case 1:
                            //Matricula
                            matr = this.FCDEGrado.findMatrLimit(this.objeTipo.getNivelTipoBeca());
                            //Objeto 1: matricula, objeto 2: monto de la beca
                            //Resultado 0 son iguales 
                            //Resultado 1 el primero es mayor que el segundo
                            if (matr.compareTo(this.objeTipo.getDescTipoBeca()) == 0 || matr.compareTo(this.objeTipo.getDescTipoBeca()) == 1) {
                                guarTipo = true;
                            } else {
                                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El monto que se desea descontar es mayor al valor de la matricula')");
                                guarTipo = false;
                            }
                            break;
                        case 2:
                            //Mensualidad
                            mens = this.FCDEGrado.findMensLimit(this.objeTipo.getNivelTipoBeca());
                            if (mens.compareTo(this.objeTipo.getDescTipoBeca()) == 0 || mens.compareTo(this.objeTipo.getDescTipoBeca()) == 1) {
                                guarTipo = true;
                            } else {
                                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El monto que se desea descontar es mayor al valor de la mensualidad')");
                                guarTipo = false;
                            }
                            break;
                        case 3:
                            this.guarTipo = true;
                            break;
                    }
                    if (guarTipo) {
                        this.objeTipo.setEstaTipoBeca(1);
                        FCDETipo.create(this.objeTipo);
                        if (this.listTipo == null) {
                            this.listTipo = new ArrayList<>();
                        }
                        this.listTipo.add(this.objeTipo);
                        this.limpForm();
                        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
                        //log.info("Tipo Beca Guardado");
                    }
                    else
                    {
                         ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Hubo un problema en guardar el tipo de beca)"); 
                    }

                } else {
                    ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El descuento debe ser mayor a 0')"); 
                }
            }
            else
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El nombre ingresado para el tipo de beca ya existe.')");
            }
            
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            //log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    /**
     * Metodo que modifia un registro de la tabla Tipo Beca
     */
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        BigDecimal matr;
        BigDecimal mens;
        try
        {
            if (this.objeTipo.getDescTipoBeca().compareTo(BigDecimal.ZERO) == 1) {
                switch (this.objeTipo.getTipoTipoBeca()) {
                    case 1:
                        //Matricula
                        matr = this.FCDEGrado.findMatrLimit(this.objeTipo.getNivelTipoBeca());
                        //Objeto 1: matricula, objeto 2: monto de la beca
                        //Resultado 0 son iguales 
                        //Resultado 1 el primero es mayor que el segundo
                        if (matr.compareTo(this.objeTipo.getDescTipoBeca()) == 0 || matr.compareTo(this.objeTipo.getDescTipoBeca()) == 1) {
                            guarTipo = true;
                        } else {
                            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El monto que se desea descontar es mayor al valor de la matricula')");
                            guarTipo = false;
                        }
                        break;
                    case 2:
                        //Mensualidad
                        mens = this.FCDEGrado.findMensLimit(this.objeTipo.getNivelTipoBeca());
                        if (mens.compareTo(this.objeTipo.getDescTipoBeca()) == 0 || mens.compareTo(this.objeTipo.getDescTipoBeca()) == 1) {
                            guarTipo = true;
                        } else {
                            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El monto que se desea descontar es mayor al valor de la mensualidad')");
                            guarTipo = false;
                        }
                        break;
                    case 3:
                        //Otro
                        break;
                }

                if (guarTipo) {
                    this.listTipo.remove(this.objeTipo); //Limpia el objeto viejo
                    FCDETipo.edit(this.objeTipo);
                    this.listTipo.add(this.objeTipo); //Agrega el objeto modificado
                    ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
                    //log.info("Tipo Beca Modificado");
                }
            }
            else{
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El descuento debe ser mayor a 0')");
            }
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
    
    /**
     * Metodo que elimina un registro de la tabla Tipo Beca
     */
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listTipo.remove(this.objeTipo); //Limpia el objeto viejo
            this.objeTipo.setEstaTipoBeca(0);
            FCDETipo.edit(this.objeTipo);
            this.listTipo.add(this.objeTipo); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
           // log.info("Tipo Beca Eliminado");
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
            this.listTipo.remove(this.objeTipo); //Limpia el objeto viejo
            this.objeTipo.setEstaTipoBeca(1);
            FCDETipo.edit(this.objeTipo);
           this.listTipo.add(this.objeTipo); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
           // log.info("Tipo Beca Eliminado");
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
    
    /**
     * Metodo que consulta todos los registros de la tabla Tipo Beca
     */
    public void consTodo()
    {
        try
        {
            this.listTipo = FCDETipo.findAll();
            //log.info("Tipos de Becas Consultados");
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
    
    /**
     * Metodo que consulta un registro de la tabla Tipo Beca
     */
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeTipo = FCDETipo.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeTipo.getNombTipoBeca()) + "')");
            //log.info("Tipo Beca Consultado");
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
