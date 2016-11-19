/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.ProcesoSolicitudesFacadeLocal;
import com.sv.udb.modelo.ProcesoSolicitudes;
import com.sv.udb.modelo.Solicitudes;
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
 * Esta clase se encuentran los metodos para el manejo de los datos (CRUD) del objeto objeProcSoli
 * @author Mulato
 * @version 1.2
 */
@Named(value = "procesoSolicitudesBean")
@ViewScoped
public class ProcesoSolicitudesBean implements Serializable{
    @EJB
    private ProcesoSolicitudesFacadeLocal FCDEProcSoli;
    private ProcesoSolicitudes objeProcSoli;
    private List<ProcesoSolicitudes> listProcSoli;
    private boolean guardar;
    private String estado = "Resolución";
    private boolean reso = false;

    /**
     * Función para obtener  el objeto objeProcSoli
     * @return ProcesoSolicitudes objeProcSoli
     */
    public ProcesoSolicitudes getObjeProcSoli() {
        return objeProcSoli;
    }

    /**
     * Función para obtener el estado del boton
     * @return estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Función para saber si se va a realizar una reolución
     * @return reso
     */
    public boolean isReso() {
        return reso;
    }

    /**
     * Función para definir el objeto objeProcSoli
     * @param objeProcSoli
     */
    public void setObjeProcSoli(ProcesoSolicitudes objeProcSoli) {
        this.objeProcSoli = objeProcSoli;
    }

    
    /**
     * Función que retorna el valor de la variable guardar para saber si se está guardando o no actualmente
     * @return Boolean guardar
     */
    public boolean isGuardar() {
        return guardar;
    }

    /**
     * Función que retorna la lista de objetos de ProcesoSolicitudes
     * @return List listProcSoli
     */
    public List<ProcesoSolicitudes> getListProcSoli() {
        return listProcSoli;
    }
    
    /**
     * Creates a new instance of ProcesoSolicitudesBean
     */
    
    public ProcesoSolicitudesBean() {
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
        this.objeProcSoli = new ProcesoSolicitudes();
        this.guardar = true;        
    }
    
    /**
     * Función para guardar
     * @param a
     */
    public void guar(int a)
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
            {
            Solicitudes soli = new Solicitudes();
            soli.setCodiSoli(a);
            this.objeProcSoli.setCodiSoli(soli);
            this.objeProcSoli.setEstaProcSoli(true);
            this.objeProcSoli.setFechProcSoli(new Date());
            FCDEProcSoli.create(this.objeProcSoli);
            this.listProcSoli.add(this.objeProcSoli);
            this.guardar = false;
            this.limpForm();
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
     * Función para cambiar el estado del boton
     */
    public void cambEsta() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            if (this.estado.equals("Resolución")) {
                this.estado = "Procesos";
                this.reso = true;
            } else if (this.estado.equals("Procesos")) {
                this.estado = "Resolución";
                this.reso = false;
            }
        } catch (Exception ex) {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
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
            this.listProcSoli.remove(this.objeProcSoli); //Limpia el objeto viejo
            FCDEProcSoli.edit(this.objeProcSoli);
            this.listProcSoli.add(this.objeProcSoli); //Agrega el objeto modificado
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
            this.objeProcSoli.setEstaProcSoli(false);
            this.listProcSoli.remove(this.objeProcSoli); //Limpia el objeto viejo
            FCDEProcSoli.edit(this.objeProcSoli);
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
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
            this.listProcSoli = FCDEProcSoli.findTodo();
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
            this.objeProcSoli = FCDEProcSoli.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeProcSoli.getCodiProcSoli()) + "')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
        finally
        {
            
        }
    }
    
    public List<ProcesoSolicitudes> procSoli(int soli){
        try{
            this.listProcSoli = FCDEProcSoli.findProcSoli(soli);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return this.listProcSoli;
    }
}
