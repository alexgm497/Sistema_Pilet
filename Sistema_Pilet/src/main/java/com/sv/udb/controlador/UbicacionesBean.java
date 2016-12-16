/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.UbicacionesFacadeLocal;
import com.sv.udb.modelo.Ubicaciones;
import java.io.Serializable;
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
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Kevin
 */
@Named(value = "ubicacionesBean")
@Dependent
public class UbicacionesBean implements Serializable {

    @EJB
    private UbicacionesFacadeLocal FCDEUbic;    
    private Ubicaciones objeUbic;   
    private List<Ubicaciones> listUbic;
    private boolean guardar;

    public UbicacionesFacadeLocal getFCDEUbic() {
        return FCDEUbic;
    }

    public void setFCDEUbic(UbicacionesFacadeLocal FCDEUbic) {
        this.FCDEUbic = FCDEUbic;
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

    public void setListUbic(List<Ubicaciones> listUbic) {
        this.listUbic = listUbic;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }

    public UbicacionesBean() {
        
    }

   
    
    
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consListUbic();
        this.consTodo();
    }
    
    public void limpForm()
    {
        this.listUbic = new ArrayList<Ubicaciones>();
        this.objeUbic = new Ubicaciones();
        this.guardar = true;        
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
    
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeUbic.setEstaUbic(true);
            FCDEUbic.create(this.objeUbic);
            this.listUbic.add(this.objeUbic);
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
    
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listUbic.remove(this.objeUbic); //Limpia el objeto viejo
            FCDEUbic.edit(this.objeUbic);
            this.listUbic.add(this.objeUbic); //Agrega el objeto modificado
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
    
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeUbic.setEstaUbic(false);
            this.listUbic.remove(this.objeUbic); //Limpia el objeto viejo
            FCDEUbic.edit(this.objeUbic);
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
    
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiPara"));
        try
        {
            this.objeUbic = FCDEUbic.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeUbic.getNombUbic()) + "')");
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
