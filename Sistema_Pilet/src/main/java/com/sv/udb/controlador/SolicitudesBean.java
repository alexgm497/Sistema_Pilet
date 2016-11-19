/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.SolicitudesFacadeLocal;
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
 * Esta clase se encuentran los metodos para el manejo de los datos (CRUD) del objeto objeSoli
 * @author Oscar
 * @version 1.2
 */
@Named(value = "solicitudesBean")
@ViewScoped
public class SolicitudesBean implements Serializable {

    @EJB
    private SolicitudesFacadeLocal FCDESoli;
    private Solicitudes objeSoli;
    private List<Solicitudes> listSoli;
    private List<Solicitudes> listSoliEnca;
    private List<Solicitudes> listSoliTecn;
    private List<Solicitudes> listSoliVaci;
    private boolean guardar;
    private String estado = "Sin Asignación";
    private boolean estaB = true;

    /**
     * Función para obtener el objeto objeSoli     *
     * @return Solicitudes objeSoli
     */
    public Solicitudes getObjeSoli() {
        return objeSoli;
    }

    /**
     * Función para definir el objeto objeSoli
     * @param objeSoli
     */
    public void setObjeSoli(Solicitudes objeSoli) {
        this.objeSoli = objeSoli;
    }

    /**
     * Función que retorna el valor de la variable guardar para saber si se está guardando o no actualmente
     * @return Boolean guardar
     */
    public boolean isGuardar() {
        return guardar;
    }

    /**
     * Función que retorna la lista de objetos de Solicitudes
     * @return List listSoli
     */
    public List<Solicitudes> getListSoli() {
        return listSoli;
    }

    /**
     * Función que retorna la lista de objetos de Solicitudes por Encargado
     * @return List listSoliEnca
     */
    public List<Solicitudes> getListSoliEnca() {
        return listSoliEnca;
    }

    /**
     * Función que retorna la lista de objetos de Solicitudes por Técnico
     * @return List listSoliTecn
     */
    public List<Solicitudes> getListSoliTecn() {
        return listSoliTecn;
    }

    /**
     * Función que retorna la lista de objetos de Solicitudes sin asignar
     * @return List listSoliVaci
     */
    public List<Solicitudes> getListSoliVaci() {
        return listSoliVaci;
    }

    /**
     * Función que retorna el estado de las muestras
     * @return List listSoliVaci
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Creates a new instance of SolicitudesBean
     */
    public SolicitudesBean() {
    }

    /**
     * Función que se ejecuta después de construir la clase
     */
    @PostConstruct
    public void init() {
        this.limpForm();
        this.consTodo();
        this.consEncargado();
        this.consTecnico();
    }

    /**
     * Función para limpiar el formulario
     */
    public void limpForm() {
        this.objeSoli = new Solicitudes();
        this.guardar = true;
    }

    /**
     * Función para guardar
     */
    public void guar() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            LoginBean login = new LoginBean();
            this.objeSoli.setCodiUsua(login.getObjeUsua().getCodiUsua());
            this.objeSoli.setEstaSoli(1);
            this.objeSoli.setFechHoraSoli(new Date());
            FCDESoli.create(this.objeSoli);
            this.listSoli.add(this.objeSoli);
            this.guardar = false;
            //this.limpForm(); //Omito para mantener los datos en la modal
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
        } catch (Exception ex) {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
        } finally {

        }
    }

    /**
     * Función para modificar un registro
     */
    public void modi() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            this.listSoli.remove(this.objeSoli); //Limpia el objeto viejo
            FCDESoli.edit(this.objeSoli);
            this.listSoli.add(this.objeSoli); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
        } catch (Exception ex) {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        } finally {

        }
    }

    /**
     * Función para consultar solicitudes en estado 3
     * @return listSoliVaci
     */
    public List<Solicitudes> consAsigFina()
    {
        try
        {
            this.listSoliVaci = FCDESoli.findAsigFina();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return listSoliVaci;
    }
    
    /**
     * Funcion para cambiar la función del boton
     */
    public void cambEsta() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            if (this.estado.equals("Sin Asignación")) {
                this.estado = "Asignados";
                this.estaB = false;
            } else if (this.estado.equals("Asignados")) {
                this.estado = "Sin Asignación";
                this.estaB = true;
            }
        } catch (Exception ex) {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }

    /**
     * Funcion para asignar solicitud a un técnico
     * @param Soli
     * @param codiUsua
     */
    public void asig(Solicitudes Soli, int codiUsua) {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            this.listSoli.remove(this.objeSoli); //Limpia el objeto viejo
            FCDESoli.asig(Soli.getCodiSoli(), Soli.getPrioSoli(), codiUsua);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Solicitud asignada')");
        } catch (Exception ex) {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        } finally {

        }
    }

    /**
     * Función para dar de baja un registro
     */
    public void elim() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            this.listSoli.remove(this.objeSoli); //Limpia el objeto viejo
            FCDESoli.remove(this.objeSoli);
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
        } catch (Exception ex) {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        } finally {

        }
    }

    /**
     * Función para consultar todos los registros de la tabla
     */
    public void consTodo() {
        try {
            this.listSoli = FCDESoli.findTodo();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    /**
     * Función para consultar las solicitudes asignadas a los Encargados
     */
    public void consEncargado() {
        try {
            this.listSoliEnca = FCDESoli.findEncargado();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    /**
     * Función para consultar las solicitudes asignadas a los Técnicos
     */
    public void consTecnico() {
        try {
            this.listSoliTecn = FCDESoli.findTecnico();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    /**
     * Función para consultar las solicitudes sin asignar
     * @return List listSoliVaci
     */
    public List<Solicitudes> consVaci() {
        if (estaB) {
            try {
                this.listSoliVaci = FCDESoli.findVaci();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                this.listSoliVaci = FCDESoli.findAllAsig();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listSoliVaci;
    }

    /**
     * Funcion para consultar solicitudes asignadas
     * @return listSoliVaci
     */
    public List<Solicitudes> consAsig() {
        try {
            this.listSoliVaci = FCDESoli.findAsig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listSoliVaci;
    }

    /**
     * Función para re-asignar la solicitud a un Encargado
     */
    public void modiAsigEnca() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            this.objeSoli.setEstaSoli(2);
            this.listSoli.remove(this.objeSoli); //Limpia el objeto viejo
            FCDESoli.edit(this.objeSoli);
            this.consEncargado();
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
        } catch (Exception ex) {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        } finally {

        }
    }

    /**
     * Función para consultar un registro en específico
     */
    public void cons() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiPara"));
        try {
            this.objeSoli = FCDESoli.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a "
                    + String.format("%s", this.objeSoli.getCodiSoli()) + "')");
        } catch (Exception ex) {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        } finally {

        }
    }
}
