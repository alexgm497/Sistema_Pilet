/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.DepartamentosFacadeLocal;
import com.sv.udb.modelo.Departamentos;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 * Esta clase se encuentran los metodos para el manejo de los datos (CRUD) del objeto objeDepa
 * @author Gerson
 * @version 1.2
 */
@Named(value = "departamentosBean")
@ViewScoped
public class DepartamentosBean implements Serializable{
    @EJB
    private DepartamentosFacadeLocal FCDEDepa;    
    private Departamentos objeDepa;
    private List<Departamentos> listDepa;
    private boolean guardar;

    /**
     * Función para obtener  el objeto objeDepa
     * @return Departamentos objeDepa
     */
    public Departamentos getObjeDepa() {
        return objeDepa;
    }

    /**
     * Función para definir el objeto objeDepa
     * @param objeDepa
     */
    public void setObjeDepa(Departamentos objeDepa) {
        this.objeDepa = objeDepa;
    }

    /**
     * Función que retorna el valor de la variable guardar para saber si se está guardando o no actualmente
     * @return Boolean guardar
     */
    public boolean isGuardar() {
        return guardar;
    }

    /**
     * Función que retorna la lista de objetos de Departamentos
     * @return List listDepa
     */
    public List<Departamentos> getListDepa() {
        return listDepa;
    }
    
    /**
     * Creates a new instance of DepartamentosBean
     */
    
    public DepartamentosBean() {
    }
    
    /**
     * Función que se ejecuta después de construir la clase
     */
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
        this.objeDepa = new Departamentos();
        this.guardar = true;
    }
    
    /**
     * Función para guardar
     */
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeDepa.setEstaDepa(true);
            this.objeDepa.setFechIngrDepa(new Date());
            FCDEDepa.create(this.objeDepa);
            this.listDepa.add(this.objeDepa);
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
            this.listDepa.remove(this.objeDepa); //Limpia el objeto viejo
            FCDEDepa.edit(this.objeDepa);
            this.listDepa.add(this.objeDepa); //Agrega el objeto modificado
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
            this.objeDepa.setFechBajaDepa(new Date());
            this.objeDepa.setEstaDepa(false);
            this.listDepa.remove(this.objeDepa); //Limpia el objeto viejo
            FCDEDepa.edit(this.objeDepa);
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
    
    /**
     * Función para consultar todos los registros de la tabla
     */
    public void consTodo()
    {
        try
        {
            this.listDepa = FCDEDepa.findTodo();
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
     * Función para consultar un registro en específico
     */
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiPara"));
        try
        {
            this.objeDepa = FCDEDepa.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeDepa.getNombDepa()) + "')");
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
