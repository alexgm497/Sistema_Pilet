/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.ResolucionSolicitudesFacadeLocal;
import com.sv.udb.ejb.SolicitudesFacadeLocal;
import com.sv.udb.modelo.ResolucionSolicitudes;
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
 * Esta clase se encuentran los metodos para el manejo de los datos (CRUD) del objeto objeResoSoli
 * @author Israel
 * @version 1.2
 */
@Named(value = "resolucionSolicitudBean")
@ViewScoped
public class ResolucionSolicitudBean implements Serializable{
    @EJB
    private ResolucionSolicitudesFacadeLocal FCDEResoSoli;
    private SolicitudesFacadeLocal FCDESoli;
    private ResolucionSolicitudes objeResoSoli;
    private List<ResolucionSolicitudes> listResoSoli;
    private boolean guardar;
    private int cant = 0;

    /**
     * Función para obtener  el objeto objeResoSoli
     * @return ResolucionSolicitudes objeResoSoli
     */
    public ResolucionSolicitudes getObjeResoSoli() {
        return objeResoSoli;
    }

    /**
     * Función para definir el objeto objeResoSoli
     * @param objeResoSoli
     */
    public void setObjeResoSoli(ResolucionSolicitudes objeResoSoli) {
        this.objeResoSoli = objeResoSoli;
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
    public List<ResolucionSolicitudes> getListResoSoli() {
        return listResoSoli;
    }
    
    /**
     * Creates a new instance of ResolucionSolicitudesBean
     */
    
    public ResolucionSolicitudBean() {
    }
    
    /**
     * Función que se ejecuta después de construir la clase
     */
    @PostConstruct
    public void init()
    {
        this.limpForm();
    }
    
    /**
     * Función para limpiar el formulario
     */
    public void limpForm()
    {
        this.objeResoSoli = new ResolucionSolicitudes();
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
            Solicitudes soli = new Solicitudes(SolicitudesBean.codiSoli);
            this.objeResoSoli.setCodiSoli(soli);
            this.objeResoSoli.setFechResoSoli(new Date());
            this.objeResoSoli.setEstaResoSoli(1);
            FCDEResoSoli.create(this.objeResoSoli);
            FCDESoli.reso(SolicitudesBean.codiSoli);
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            ctx.execute("location.reload()");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
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
            this.objeResoSoli.setFechResoSoli(new Date());
            this.listResoSoli.remove(this.objeResoSoli); //Limpia el objeto viejo
            FCDEResoSoli.edit(this.objeResoSoli);
            this.listResoSoli.add(this.objeResoSoli); //Agrega el objeto modificado
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
     * Función para consultar un registro en específico
     */
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiPara"));
        try
        {
            this.objeResoSoli = FCDEResoSoli.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeResoSoli.getCodiResoSoli()) + "')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
        finally
        {
            
        }
    }
    
    /**
     * Funcion para consultar una resolución por id
     * @param codi
     * @return
     */
    public ResolucionSolicitudes consReso(int codi){
        try{
            this.objeResoSoli = FCDEResoSoli.findReso(codi);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return this.objeResoSoli;
    }
    
    /**
     * Función para consultar las ultimas resoluciones
     * @return listResoSoli
     */
    public List<ResolucionSolicitudes> consResoUsua(){
        try{
            if(this.cant == 0){
                this.cant = 25;
            }
            this.listResoSoli = FCDEResoSoli.findResoUsua(this.cant);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return this.listResoSoli;
    }
    
    /**
     * Función para obtener la cantidad de registros a consultar
     * @param cant
     */
    public void consCant(int cant){
        this.cant = cant;
    }
}
