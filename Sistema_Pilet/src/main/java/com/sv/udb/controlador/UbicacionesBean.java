/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.UbicacionesFacadeLocal;
import com.sv.udb.modelo.Ubicaciones;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Kevin
 */
@Named(value = "ubicacionesBean")
@Dependent
public class UbicacionesBean {

    @EJB
    private UbicacionesFacadeLocal FCDEUbic;    
    private Ubicaciones objeUbic;
        
    private List<Ubicaciones> listUbic;
    
    
    public UbicacionesBean() {
    }

    public Ubicaciones getObjeUbic() {
        return objeUbic;
    }

    public void setObjeUbic(Ubicaciones objeUbic) {
        this.objeUbic = objeUbic;
    }

    public List<Ubicaciones> getListUbic() {
        return listUbic;
    }
    
    
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consListUbic();
    }
    
    public void limpForm()
    {
        this.listUbic = new ArrayList<Ubicaciones>();
    }
    
    public void consListUbic(){
        try
        {
            this.listUbic = FCDEUbic.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public List<Ubicaciones> consListUbicPorDispCita()
    {
        return FCDEUbic.findByDispCita();
    }
    
    public List<Ubicaciones> consListUbicPorDisEven()
    {
        return FCDEUbic.findByDispEven();
    }
    
    public void consTodo()
    {
        try
        {
            this.listUbic = FCDEUbic.findAll();
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
