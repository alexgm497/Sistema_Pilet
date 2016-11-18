package com.sv.udb.controlador;
import com.sv.udb.ejb.AlumnovisitanteFacadeLocal;
import com.sv.udb.ejb.VisitanteFacadeLocal;
import com.sv.udb.modelo.Alumnovisitante;
import com.sv.udb.modelo.Visitante;
import com.sv.udb.utils.LOG4J;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.apache.log4j.Logger;

/**
 * La clase visitantes
 * @author Sistema de citas
 * @version prototipo 2
 * Octubre 2016
 */
@Named(value = "visitantesBean")
@ViewScoped
public class VisitantesBean implements Serializable{
    
    public VisitantesBean() {
        
    }
     
    @EJB
    private VisitanteFacadeLocal FCDEVisi;    
    private Visitante objeVisi;
    
    @Inject
    private LoginBean logiBean; 
    
    private List<Visitante> listVisi;
    private boolean guardar;
    
    private String cadeText="";

    public String getCadeText() {
        return cadeText;
    }

    public void setCadeText(String cadeText) {
        this.cadeText = cadeText;
    }
    
    private LOG4J<VisitantesBean> lgs = new LOG4J<VisitantesBean>(VisitantesBean.class) {
    };
    private Logger log = lgs.getLog();
    
    
    
    //variables para registro de nuevo visitante
    @Inject
    private GlobalAppBean globalAppBean;
    private AlumnoVisitanteBean alumVisiBean;
   
    public Visitante getObjeVisi() {
        return objeVisi;
    }
    

    public void setObjeVisi(Visitante objeVisi) {
        this.objeVisi = objeVisi;
    }

    public List<Visitante> getListVisi() {
        consTodo();
        return listVisi;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public AlumnoVisitanteBean getAlumVisiBean() {
        this.alumVisiBean = new AlumnoVisitanteBean();
        return alumVisiBean;
    }

    public void setAlumVisiBean(AlumnoVisitanteBean alumVisiBean) {
        this.alumVisiBean = alumVisiBean;
    }

    
    
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        
        
    }
 //Limpiando el formulario   
    public void limpForm()
    {
        this.objeVisi = new Visitante();
        this.guardar = true;   
        
    }
    
    
    public void consTodo()
    {
        try
        {
            this.listVisi = FCDEVisi.findByAllFields(cadeText, 20, 1);
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
            this.objeVisi = FCDEVisi.find(codi);
            this.guardar = false;
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+" Consultar visitante: " + objeVisi.getNombVisi() + " " + objeVisi.getApelVisi());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Registro Consultado')");
        }
        catch(Exception ex)
        {
            log.error("Error al consultar visitante");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
        finally
        {
            
        }
    }
     /**
     * Método que guarda un objeto del tipo visitante en la base de datos
     *  Objeto del tipo evento
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */       
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            objeVisi.setEstaVisi(1);
            FCDEVisi.create(this.objeVisi);
            
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+" Agregar visitante: " + objeVisi.getNombVisi() + " " + objeVisi.getApelVisi());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            if(listVisi == null)listVisi = new ArrayList<Visitante>();
            this.listVisi.add(this.objeVisi);
            limpForm();
        }
        catch(Exception ex)
        {
            log.error("Error al guardar visitante");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
        }
    }
      /**
     * Método que modifica un objeto del tipo visitante en la base de datos
     * Objeto del tipo evento
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */       
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listVisi.remove(this.objeVisi); //Limpia el objeto viejo
            FCDEVisi.edit(this.objeVisi);
            this.listVisi.add(this.objeVisi); //Agrega el objeto modificado
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+" Modificar visitante: " + objeVisi.getNombVisi() + " " + objeVisi.getApelVisi());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
        }
        catch(Exception ex)
        {
            log.error("Error al modificar visitante");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }
     /**
     * Método que elimina un objeto del tipo visitante en la base de datos
     *  Objeto del tipo evento
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */         
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            objeVisi.setEstaVisi(0);
            FCDEVisi.edit(this.objeVisi);
            this.listVisi.remove(this.objeVisi);
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+" Eliminar visitante codigo: " + objeVisi.getCodiVisi());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
            this.limpForm();
        }
        catch(Exception ex)
        {
            log.error("Error al eliminar visitante");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }
}
