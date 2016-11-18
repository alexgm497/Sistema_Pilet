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
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 * Esta clase se encuentran los metodos para la consulta de equipos
 * @author Alexander
 * @version 1.0
 */
@Named(value = "equiposBean")
@ViewScoped
public class EquiposBean implements Serializable{

    @EJB
    private EquiposFacadeLocal FCDEEqui;
    private Equipos objeEqui;

    private List<Equipos> listEqui;

    /**
     * Funcion para otener la lista de equipos
     * @return listEqui
     */
    public List<Equipos> getListEqui() {
        return listEqui;
    }
    
    /**
     * Funcion para obtener el objeto objeEqui
     * @return objeEqui
     */
    public Equipos getObjeEqui() {
        return objeEqui;
    }

    /**
     * Función para definir el objet objeEqui
     * @param objeEqui
     */
    public void setObjeEqui(Equipos objeEqui) {
        this.objeEqui = objeEqui;
    }
    
    /**
     * Creates a new instance of EquiposBean
     */
    public EquiposBean() {
    }

    /**
     * Función que se ejecuta después de construir la clase
     */
    @PostConstruct
    public void init()
    {
        this.consTodo();
    }
    
    /**
     * Funcion para consultar todos los equipos
     */
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
    
    /**
     * Función para consultar un equipo por su id
     * @param codi
     * @return objeEqui
     */
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
