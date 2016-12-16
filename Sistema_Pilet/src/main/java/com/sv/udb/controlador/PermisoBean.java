/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.PermisoFacadeLocal;
import com.sv.udb.modelo.Permiso;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Carlos
 */
@Named(value = "permisoBean")
@ViewScoped
public class PermisoBean implements Serializable{
    @EJB
    private PermisoFacadeLocal FCDEPermiso;
    
    private Permiso objePerm;
    private List<Permiso> listPerm;
    private List<Permiso> listModu;
    private List<Permiso> listPagi;
    private List<Permiso> listAcci;
    private boolean guardar;

    public Permiso getObjePerm() {
        return objePerm;
    }

    public void setObjePerm(Permiso objePerm) {
        this.objePerm = objePerm;
    }

    public List<Permiso> getListPerm() {
        return listPerm;
    }

    public void setListPerm(List<Permiso> listPerm) {
        this.listPerm = listPerm;
    }

    public List<Permiso> getListModu() {
        return listModu;
    }

    public void setListModu(List<Permiso> listModu) {
        this.listModu = listModu;
    }

    public List<Permiso> getListPagi() {
        return listPagi;
    }

    public void setListPagi(List<Permiso> listPagi) {
        this.listPagi = listPagi;
    }

    public List<Permiso> getListAcci() {
        return listAcci;
    }

    public void setListAcci(List<Permiso> listAcci) {
        this.listAcci = listAcci;
    }
    
    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }
    
    /**
     * Creates a new instance of PermisoBean
     */
    public PermisoBean() {
    }
    
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consTodo();
    }
    
    public void limpForm()
    {
        this.objePerm =  new Permiso();
        this.guardar = true;        
    }
    
    public void consTodo()
    {
        try
        {
            this.listPerm = FCDEPermiso.findAll();
            this.listModu = FCDEPermiso.findAllModu();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
    }
    
    public void fillPagiCmbx(Permiso modu)
    {
        try
        {
            this.listPagi = FCDEPermiso.findPagiByModu(modu.getCodiPerm());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
    }
    
    public void creaAcciChkb(Permiso pagi)
    {
        try
        {
            this.listAcci = FCDEPermiso.findAcciByPagi(pagi.getCodiPerm());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
    }
   
}
