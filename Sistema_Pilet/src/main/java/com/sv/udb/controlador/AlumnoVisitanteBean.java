package com.sv.udb.controlador;

import com.sv.udb.ejb.AlumnovisitanteFacadeLocal;
import com.sv.udb.ejb.VisitanteFacadeLocal;
import com.sv.udb.modelo.Alumnovisitante;
import com.sv.udb.modelo.Visitante;
import com.sv.udb.utils.LOG4J;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;


 /**
 * La clase alumno visitante 
 * @author: ControlCitas
 * @version: Prototipo 1
 * Octubre de 2016
 */
@Named(value = "alumnoVisitanteBean")
@ViewScoped
public class AlumnoVisitanteBean implements Serializable{
   
    
    public AlumnoVisitanteBean() {
        
    }
    
    //Bean Sesion
    @Inject
    private LoginBean logiBean; 
     
    @EJB
    private AlumnovisitanteFacadeLocal FCDEAlumVisi;    
    private Alumnovisitante objeAlumVisi;
        
    private List<Alumnovisitante> listAlumVisi;
    private boolean guardar;
    
    private LOG4J<AlumnoVisitanteBean> lgs = new LOG4J<AlumnoVisitanteBean>(AlumnoVisitanteBean.class) {
    };
    private Logger log = lgs.getLog();
    // Variables para registrarse como visitante representante alumno
    @EJB
    private VisitanteFacadeLocal FCDEVisi;    
    private Visitante objeVisi;
    private boolean Disabled;
    private boolean contForm;
    private List<Alumnovisitante> listAlumVisiCarne;
    
    private String carnAlum;
    
    public void setAlumn(){
        String Carn = String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjeAlum"));
        if(Carn!=null){
            this.carnAlum = Carn;
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Alumno Seleccionado')");
        }
    }
    
    public Alumnovisitante getObjeAlumVisi() {
        return objeAlumVisi;
    }

    public void setObjeAlumVisi(Alumnovisitante objeAlumVisi) {
        this.objeAlumVisi = objeAlumVisi;
    }

    public List<Alumnovisitante> getListAlumVisi() {
        return listAlumVisi;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public AlumnovisitanteFacadeLocal getFCDEAlumVisi() {
        return FCDEAlumVisi;
    }

    public void setFCDEAlumVisi(AlumnovisitanteFacadeLocal FCDEAlumVisi) {
        this.FCDEAlumVisi = FCDEAlumVisi;
    }

    public List<Alumnovisitante> getListAlumVisiCarne() {
        return listAlumVisiCarne;
    }

    public Visitante getObjeVisi() {
        return objeVisi;
    }

    public void setObjeVisi(Visitante objeVisi) {
        this.objeVisi = objeVisi;
    }

    public boolean isDisabled() {
        return Disabled;
    }

    public void setDisabled(boolean Disabled) {
        this.Disabled = Disabled;
    }

    public boolean isContForm() {
        return contForm;
    }

    public void setContForm(boolean contForm) {
        this.contForm = contForm;
    }
    
       /**
     * Métodos
     */
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consTodo();
        this.consAlumVisi();
        if(this.listAlumVisiCarne == null) this.listAlumVisiCarne  = new ArrayList<Alumnovisitante>();
        if(this.listAlumVisi==null)this.listAlumVisi  = new ArrayList<Alumnovisitante>();
    }
    
    public void limpForm()
    {
        this.objeAlumVisi = new Alumnovisitante();
        this.objeVisi = new Visitante();
        this.guardar = true;   
        this.Disabled = true; 
        this.contForm = true;
    }
    
    public void consTodo()
    {
        try
        {
            this.listAlumVisi = FCDEAlumVisi.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void consAlumVisi(){
         try
        {
            this.listAlumVisiCarne = FCDEAlumVisi.findByCarnAlum(logiBean.getObjeUsua().getAcceUsua());
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
            this.objeAlumVisi = FCDEAlumVisi.find(codi);
            this.objeVisi = objeAlumVisi.getCodiVisi();
            this.guardar = false;
            this.Disabled=false;
            this.contForm = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Registro Consultado')");
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"AlumnoVisitante"+"-"+" Consultar alumno visitante: " +  objeAlumVisi.getCarnAlum());
            //por alguna razón, al consultar con cambia el select... asi que se hace manualmente....
            ctx.execute("selectedItem("+this.objeAlumVisi.getPareAlumVisi()+")");
        }
        catch(Exception ex)
        {
            log.error("Error al consultar alumno visitante");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
       /**
     * Método para encontrar el Dui del visitante
     */
    public void consPorDui()
     {
        RequestContext ctx = RequestContext.getCurrentInstance();
        try
        {   
            Visitante objVis = FCDEVisi.findByDuiVisi(this.objeVisi.getDuiVisi());
            if(objVis != null){
                    if(objVis.getDuiVisi().equals(this.objeVisi.getDuiVisi())){
                        this.objeVisi = objVis;
                        ctx.execute("setMessage('MESS_INFO', 'Atención', 'Visitante Encontrado!')");
                        ctx.execute("selectedItem("+this.objeAlumVisi.getPareAlumVisi()+")");
                        
                }
            }
            else{
                    this.Disabled = false;
                    String dui = this.objeVisi.getDuiVisi();
                    this.objeVisi = new Visitante();
                    this.objeVisi.setDuiVisi(dui);
                    ctx.execute("setMessage('MESS_INFO', 'Atención', 'Visitante no encontrado, Registrarse por favor!')");
                }
            contForm = false;
            
            
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Datos No Consultados')");
            ex.printStackTrace();
        }
    }
    
       /**
     * Método para guardar los datos del visitante
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            objeAlumVisi.setEstaAlumVisi(1);
            objeAlumVisi.setCarnAlum(this.carnAlum);
            FCDEAlumVisi.create(this.objeAlumVisi);
            this.listAlumVisi.add(this.objeAlumVisi);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados'); INIT_OBJE_TABL();");
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"AlumnoVisitante"+"-"+" Agregar alumno visitante: " +  objeAlumVisi.getCarnAlum());
            this.limpForm();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
            log.error("Error al guardar alumno visitante");
            ex.printStackTrace();
        }
    }
    
     /**
     * Método para modificar los datos del visitante
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listAlumVisi.remove(this.objeAlumVisi); //Limpia el objeto viejo
            this.objeAlumVisi.setCarnAlum(this.carnAlum);
            FCDEAlumVisi.edit(this.objeAlumVisi);
            this.listAlumVisi.add(this.objeAlumVisi); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"AlumnoVisitante"+"-"+" Modificar alumno visitante: " +  objeAlumVisi.getCarnAlum());
        }
        catch(Exception ex)
        {
            log.error("Error al modificar alumno visitante");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }
     /**
     * Método para eliminar los datos del visitante
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            objeAlumVisi.setEstaAlumVisi(0);
            FCDEAlumVisi.edit(this.objeAlumVisi);
            this.listAlumVisi.remove(this.objeAlumVisi);
            this.listAlumVisiCarne.remove(this.objeAlumVisi);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"AlumnoVisitante"+"-"+" Eliminar alumno visitante codigo: " +  objeAlumVisi.getCodiAlumVisi());
        }
        catch(Exception ex)
        {
            log.error("Error al eliminar alumno visitante");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }
     /**
     * Método para registrar los datos del visitante
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void regiVisi(){
        RequestContext ctx = RequestContext.getCurrentInstance();
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        try{
            if(!Disabled){//si aun no está registrado
                //Registramos Visitante
                    this.objeVisi.setEstaVisi(1);
                    this.objeVisi.setTipoVisi(1);
                    FCDEVisi.create(this.objeVisi);
            }
        }catch(Exception e){
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al intenar registrarse')");
            System.out.println("ERROR AL REGISTRARSE");
            e.printStackTrace();
        }                    
        asigAlumVisi();
    }
     /**
     * Método para asignar un visitante
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void asigAlumVisi(){
        try{
            RequestContext ctx = RequestContext.getCurrentInstance();
            System.out.println("ACCE CARNET ");
            objeAlumVisi.setCarnAlum(String.valueOf(logiBean.getObjeUsua().getAcceUsua()));
            //System.out.println("CODIGO VISI: "+objeVisi.getCodiVisi()+" NOMBRE VISI: "+objeVisi.getNombVisi());
            objeAlumVisi.setCodiVisi(objeVisi);
            objeAlumVisi.setEstaAlumVisi(1);
            this.listAlumVisiCarne.add(this.objeAlumVisi);
            this.guar();
        }catch(Exception e){
            System.out.println("ERROR AL ASIGNAR ALUMNO");
            e.printStackTrace();
        }
        
    }
}
