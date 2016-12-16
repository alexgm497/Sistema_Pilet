/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.EvaluacionResolucionesFacadeLocal;
import com.sv.udb.modelo.EvaluacionResoluciones;
import com.sv.udb.modelo.ResolucionSolicitudes;
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
 * Esta clase se encuentran los metodos para el manejo de los datos (CRUD) del
 * objeto objeEvalReso
 *
 * @author Alexander
 * @version 1.2
 */
@Named(value = "evaluacionResolucionesBean")
@ViewScoped
public class EvaluacionResolucionesBean implements Serializable {

    @EJB
    private EvaluacionResolucionesFacadeLocal FCDEEvalReso;
    private EvaluacionResoluciones objeEvalReso;
    private List<EvaluacionResoluciones> listEvalReso;
    private boolean guardar;

    /**
     * Función para obtener el objeto objeEvalReso
     *
     * @return EvaluacionResoluciones objeEvalReso
     */
    public EvaluacionResoluciones getObjeEvalReso() {
        return objeEvalReso;
    }

    /**
     * Función para definir el objeto objeEvalReso
     *
     * @param objeEvalReso
     */
    public void setObjeEvalReso(EvaluacionResoluciones objeEvalReso) {
        this.objeEvalReso = objeEvalReso;
    }

    /**
     * Función que retorna la lista de objetos de EvaluacionResoluciones
     *
     * @return List listEvalReso
     */
    public List<EvaluacionResoluciones> getListEvalReso() {
        return listEvalReso;
    }

    /**
     * Función para definir la lista de objetos de EvaluacionResoluciones
     *
     * @param listEvalReso
     */
    public void setListEvalReso(List<EvaluacionResoluciones> listEvalReso) {
        this.listEvalReso = listEvalReso;
    }

    /**
     * Función que retorna el valor de la variable guardar para saber si se está
     * guardando o no actualmente
     *
     * @return Boolean guardar
     */
    public boolean isGuardar() {
        return guardar;
    }

    /**
     * Función para definir si se esta guardando o no
     *
     * @param guardar
     */
    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }

    /**
     * Creates a new instance of EvaluacionResolucionesBean
     */
    public EvaluacionResolucionesBean() {
    }

    /**
     * Función que se ejecuta después de construir la clase
     */
    @PostConstruct
    public void init() {
        this.limpForm();
    }

    /**
     * Función para limpiar el formulario
     */
    public void limpForm() {
        this.objeEvalReso = new EvaluacionResoluciones();
        this.guardar = true;
    }

    /**
     * Función para guardar
     *
     * @param reso
     */
    public void guar(ResolucionSolicitudes reso, Boolean ren) {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            this.objeEvalReso.setEstaEvalReso(true);
            this.objeEvalReso.setFechEvalReso(new Date());
            this.objeEvalReso.setCodiResoSoli(reso);
            FCDEEvalReso.create(this.objeEvalReso);
            this.guardar = false;
            if (!ren) {
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            } else {                
                ctx.execute("setMessage('MESS_WARN', 'Atención', 'Datos guardados. Se reactivó la solicitud.')");
            }
            this.limpForm();
        } catch (Exception ex) {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
            ex.printStackTrace();
        }
        ctx.execute("window.setTimeout(function(){location.reload()},2500)");
    }

    public List<EvaluacionResoluciones> cons() {
        try {
            this.listEvalReso = FCDEEvalReso.findEvalUsua();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this.listEvalReso;
    }
}
