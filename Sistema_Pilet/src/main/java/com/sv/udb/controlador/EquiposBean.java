/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.EquiposFacadeLocal;
import com.sv.udb.modelo.Equipos;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Alexander
 */
@Named(value = "equiposBean")
@ViewScoped
public class EquiposBean implements Serializable{

    @EJB
    private EquiposFacadeLocal FCDEEqui;

    public EquiposFacadeLocal getFCDEEqui() {
        return FCDEEqui;
    }

    public void setFCDEEqui(EquiposFacadeLocal FCDEEqui) {
        this.FCDEEqui = FCDEEqui;
    }
    private Equipos objeEqui;
    private List<Equipos> listEqui;
    private boolean guardar;

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }
    
    public List<Equipos> getListEqui() {
        return listEqui;
    }
    
    public Equipos getObjeEqui() {
        return objeEqui;
    }

    public void setObjeEqui(Equipos objeEqui) {
        this.objeEqui = objeEqui;
    }
    
    /**
     * Creates a new instance of EquiposBean
     */
    public EquiposBean() {
        
    }
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consTodo();
    }
    
    /**
     * Función para limpiar el formulario
     */
    public void limpForm()
    {
        this.objeEqui = new Equipos();
        this.guardar = true;
    }
    
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            FCDEEqui.create(this.objeEqui);
            this.listEqui.add(this.objeEqui);
            this.guardar = false;
            //this.limpForm(); //Omito para mantener los datos en la modal
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
        }
        finally
        {
            
        }
    }
    
    /**
     * Función para modificar un registro
     */
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listEqui.remove(this.objeEqui); //Limpia el objeto viejo
            FCDEEqui.edit(this.objeEqui);
            this.listEqui.add(this.objeEqui); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
        finally
        {
            
        }
    }
    
    /**
     * Función para dar de baja un registro
     */
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listEqui.remove(this.objeEqui); //Limpia el objeto viejo
            FCDEEqui.edit(this.objeEqui);
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
        finally
        {
            
        }
    }
    public void consTodo()
    {
        try
        {
            this.listEqui = FCDEEqui.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
    }
    
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiPara"));
        try
        {
            this.objeEqui = FCDEEqui.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeEqui.getDescEqui()) + "')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
        finally
        {
            
        }
    }
    
    public Equipos consEqui(int codi){
        try{
            this.objeEqui = FCDEEqui.find(codi);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return this.objeEqui;
    }
}
