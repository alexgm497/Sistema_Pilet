package com.sv.udb.controlador;
import com.sv.udb.ejb.ExcepcionhorariodisponibleFacadeLocal;
import com.sv.udb.modelo.Excepcionhorariodisponible;
import com.sv.udb.utils.LOG4J;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.apache.log4j.Logger;

/**
 * Clase de excepcion de los horarios disponibles
 * @author Sistema de citas
 * @version prototipo 2
 * Octubre de 2016
 */
@Named(value = "excepcionHorarioDisponiblesBean")
@ViewScoped
public class ExcepcionHorarioDisponibleBean implements Serializable{
   
    
    public ExcepcionHorarioDisponibleBean() {
        
    }
     
    @EJB
    private ExcepcionhorariodisponibleFacadeLocal FCDEExceHoraDisp;    
    private Excepcionhorariodisponible objeExceHoraDisp;
    private List<Excepcionhorariodisponible> listExceHoraDisp;
    private boolean guardar;
    
     @Inject
    private LoginBean logiBean; 
     
    private LOG4J<ExcepcionHorarioDisponibleBean> lgs = new LOG4J<ExcepcionHorarioDisponibleBean>(ExcepcionHorarioDisponibleBean.class) {
    };
    private Logger log = lgs.getLog();
    
    public Excepcionhorariodisponible getObjeExceHoraDisp() {
        return objeExceHoraDisp;
    }

    public void setObjeExceHoraDisp(Excepcionhorariodisponible objeExceHoraDisp) {
        this.objeExceHoraDisp = objeExceHoraDisp;
    }

    public List<Excepcionhorariodisponible> getListExceHoraDisp() {
        return listExceHoraDisp;
    }

    public boolean isGuardar() {
        return guardar;
    }
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consTodo();
    }
    //Limpiando el formulario
    public void limpForm()
    {
        this.objeExceHoraDisp = new Excepcionhorariodisponible();
        this.guardar = true;        
    }
    
    public void consTodo()
    {
        try
        {
            this.listExceHoraDisp = FCDEExceHoraDisp.findByCodiUsua(LoginBean.getObjeWSconsEmplByAcce().getCodi());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeExceHoraDisp = FCDEExceHoraDisp.find(codi);
            this.guardar = false;
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"ExcepcionHorario"+"-"+"Se ha consultado una excepcion con codigo : " + objeExceHoraDisp.getCodiExceHoraDisp());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Registro Consultado')");
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"ExcepcionHorario"+"-"+"Error al consultar registro con codigo : " + objeExceHoraDisp.getCodiExceHoraDisp());
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
     /**
     * Obteniendo los dias de la semana       
     * @since incluido desde la version 1.0
     */
    private int getDay(String dia){
        int ndia = 0;
        String dias[] = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
        for(int i = 0; i < dias.length; i++){
            if(dia.equals(dias[i])){ndia = i+1; break;}
        }
        return ndia;
    }
         /**
     * Validando
     * Objeto del tipo excepcion horarios disponibles     
     * @since incluido desde la version 1.0
     */ 
    private boolean validar(){
        boolean val = false;
        RequestContext ctx = RequestContext.getCurrentInstance();
            int diaHoraDisp = getDay(this.objeExceHoraDisp.getCodiHoraDisp().getDiaHoraDisp());
            int diaExceHoraDisp = this.objeExceHoraDisp.getFechExceHoraDisp().getDay();
            if(diaHoraDisp == diaExceHoraDisp){
                val = true;
            }else{
                FacesContext.getCurrentInstance().addMessage("FormRegi:fech", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Esta fecha no coincide con el Horario Disponible",  null));
            }
        return val;
    }
         /**
     * Método que guarda un objeto del tipo excepcion horario en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */ 
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {   
            if(validar()){
              FCDEExceHoraDisp.create(this.objeExceHoraDisp);
              if(listExceHoraDisp == null)listExceHoraDisp= new ArrayList<Excepcionhorariodisponible>();
              log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"ExcepcionHorario"+"-"+"Se ha  Agregado una excepcion con codigo: " + objeExceHoraDisp.getCodiExceHoraDisp());
              ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
              this.listExceHoraDisp.add(this.objeExceHoraDisp);
              limpForm();
            }
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"ExcepcionHorario"+"-"+"Error al guardar registro");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
        }
    }
     /**
     * Método que modifica un objeto del tipo excepcion horario en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */ 
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(validar()){
              this.listExceHoraDisp.remove(this.objeExceHoraDisp); //Limpia el objeto viejo
              FCDEExceHoraDisp.edit(this.objeExceHoraDisp);
              this.listExceHoraDisp.add(this.objeExceHoraDisp); //Agrega el objeto modificado
              log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"ExcepcionHorario"+"-"+"Se ha  modificado una excepcion con codigo: " + objeExceHoraDisp.getCodiExceHoraDisp());
              ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
              this.listExceHoraDisp.add(this.objeExceHoraDisp); 
            }
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"ExcepcionHorario"+"-"+"Error al modificar registro con codigo:" + objeExceHoraDisp.getCodiExceHoraDisp());
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }
    /**
     * Método que elmina un objeto del tipo excepcion horario en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */   
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            FCDEExceHoraDisp.remove(this.objeExceHoraDisp);
            this.listExceHoraDisp.remove(this.objeExceHoraDisp);
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"ExcepcionHorario"+"-"+"Se ha  eliminado una excepcion con codigo: " + objeExceHoraDisp.getCodiExceHoraDisp());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"ExcepcionHorario"+"-"+"Error al eliminar registro con codigo:" + objeExceHoraDisp.getCodiExceHoraDisp());
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }
}
