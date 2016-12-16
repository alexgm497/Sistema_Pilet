package com.sv.udb.controlador;
import com.sv.udb.ejb.HorariodisponibleFacadeLocal;
import com.sv.udb.modelo.Horariodisponible;
import com.sv.udb.utils.LOG4J;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * Clase de horario disponible 
 * @author Sistema de citas
 * @version prototipo 2
 * Octubre de 2016
 */
@Named(value = "horarioDisponibleBean")
@ViewScoped
public class HorarioDisponibleBean implements Serializable{
    
    //Bean Sesion
    @Inject
    private LoginBean logiBean; 
   
    
    public HorarioDisponibleBean() {
        
    }
     
    @EJB
    private HorariodisponibleFacadeLocal FCDEHoraDisp;    
    private Horariodisponible objeHoraDisp;
    private List<Horariodisponible> listHoraDisp = new ArrayList<Horariodisponible>();
    private List<Horariodisponible> listHoraDispTodo = new ArrayList<Horariodisponible>();
    private boolean guardar;
    
    private LOG4J<HorarioDisponibleBean> lgs = new LOG4J<HorarioDisponibleBean>(HorarioDisponibleBean.class) {
    };
    private Logger log = lgs.getLog();
    
    
    public List<Horariodisponible> getListHoraDispTodo() {
        consTodo();
        return listHoraDispTodo;
    }

    public void setListHoraDispTodo(List<Horariodisponible> listHoraDispTodo) {
        this.listHoraDispTodo = listHoraDispTodo;
    }

    public Horariodisponible getObjeHoraDisp() {
        return objeHoraDisp;
    }

    public void setObjeHoraDisp(Horariodisponible objeHoraDisp) {
        this.objeHoraDisp = objeHoraDisp;
    }

    public List<Horariodisponible> getListHoraDisp() {
        consPorUsua();
        return listHoraDisp;
    }

    public boolean isGuardar() {
        return guardar;
    }
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
    }
    //Limpiar el formulario
    public void limpForm()
    {
        this.objeHoraDisp = new Horariodisponible();
        this.guardar = true; 
        
    }
    
    public void consPorUsua()
    {
        try
        {
            this.listHoraDisp = FCDEHoraDisp.findByCodiUsua(new LoginBean().getObjeWSconsEmplByAcce().getCodi());
            if(listHoraDisp == null)listHoraDisp = new ArrayList<Horariodisponible>();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void consTodo()
    {
        try
        {
            this.listHoraDispTodo = FCDEHoraDisp.findByCodiUsua(LoginBean.getObjeWSconsEmplByAcce().getCodi());
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
            this.objeHoraDisp = FCDEHoraDisp.find(codi);
            this.guardar = false;
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"HorarioDisponible"+"-"+"Se ha consultado un horario con codigo: " + objeHoraDisp.getCodiHoraDisp());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Registro Consultado')");
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"HorarioDisponible"+"-"+"Error al consultar registro con codigo: " + objeHoraDisp.getCodiHoraDisp());
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
     /**
     * Método que guarda un objeto del tipo horario disponible en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */ 
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(validar()){
                this.objeHoraDisp.setCodiUsua(LoginBean.getObjeWSconsEmplByAcce().getCodi());
                objeHoraDisp.setEstaHoraDisp(1);
                FCDEHoraDisp.create(this.objeHoraDisp);
                log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"HorarioDisponible"+"-"+"Se ha agregado un horario con codigo: " + objeHoraDisp.getCodiHoraDisp());
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
                if(this.listHoraDisp.isEmpty()) this.listHoraDisp = new ArrayList<Horariodisponible>();
                this.listHoraDisp.add(objeHoraDisp);
                limpForm();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"HorarioDisponible"+"-"+"Error al guardar registro");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
        }
    }
     /**
     * Método que modifica un objeto del tipo horario disponible en la base de datos
     * objeHoraDisp Objeto del tipo horario disponible
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */     
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(validar()){
                this.objeHoraDisp.setCodiUsua(LoginBean.getObjeWSconsEmplByAcce().getCodi());
                this.listHoraDisp.remove(this.objeHoraDisp); //Limpia el objeto viejo
                FCDEHoraDisp.edit(this.objeHoraDisp);
                log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"HorarioDisponible"+"-"+"Se ha modificado un horario con codigo: " + objeHoraDisp.getCodiHoraDisp());
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
                this.listHoraDisp.add(objeHoraDisp);
            }
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"HorarioDisponible"+"-"+"Error al modificar registro con codigo: " + objeHoraDisp.getCodiHoraDisp());
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }
     /**
     * Método que elimina un objeto del tipo horario disponible en la base de datos
     *  Objeto del tipo horario disponible
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */ 
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            objeHoraDisp.setEstaHoraDisp(0);
            FCDEHoraDisp.edit(this.objeHoraDisp);
            this.listHoraDisp.remove(this.objeHoraDisp);
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"HorarioDisponible"+"-"+"Se ha eliminado un horario con codigo: " + objeHoraDisp.getCodiHoraDisp());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"HorarioDisponible"+"-"+"Error al eliminar registro con codigo: " + objeHoraDisp.getCodiHoraDisp());
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }
         /**
     * Método que valida la fecha y hora del tipo horario disponible en la base de datos
     * objeHoraDisp Objeto del tipo horario disponible para obtener fechas
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */ 
    private boolean validar(){
        boolean val = false;
        RequestContext ctx = RequestContext.getCurrentInstance();
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        try
        {
          
            if(formatter.parse(this.objeHoraDisp.getHoraFinaHoraDisp()).after(formatter.parse(this.objeHoraDisp.getHoraInicHoraDisp()))){
                val = true;
            }
            else
            {
                FacesContext.getCurrentInstance().addMessage("FormRegi:horaInicHoraDisp", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La hora Final no puede ser antes de la Inicial",  null));
                FacesContext.getCurrentInstance().addMessage("FormRegi:horaFinaHoraDisp", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La hora Final no puede ser antes de la Inicial",  null));
            }
        }
        catch(Exception err)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Horas no válidas')");
            return false;
        }
        return val;
    }
}
