/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.BecaFacadeLocal;
import com.sv.udb.ejb.DetalleBecaFacadeLocal;
import com.sv.udb.ejb.DocumentoFacadeLocal;
import com.sv.udb.ejb.GradoFacadeLocal;
import com.sv.udb.ejb.OpcionFacadeLocal;
import com.sv.udb.ejb.PreguntaFacadeLocal;
import com.sv.udb.ejb.RespuestaFacadeLocal;
import com.sv.udb.ejb.SeccionFacadeLocal;
import com.sv.udb.ejb.SeguimientoFacadeLocal;
import com.sv.udb.ejb.SolicitudBecaFacadeLocal;
import com.sv.udb.ejb.TipoBecaFacadeLocal;
import com.sv.udb.ejb.UsuarioRolFacadeLocal;
import com.sv.udb.modelo.Beca;
import com.sv.udb.modelo.DetalleBeca;
import com.sv.udb.modelo.Documento;
import com.sv.udb.modelo.Empresa;
import com.sv.udb.modelo.Grado;
import com.sv.udb.modelo.Opcion;
import com.sv.udb.modelo.OpcionRespuesta;
import com.sv.udb.modelo.Pregunta;
import com.sv.udb.modelo.Respuesta;
import com.sv.udb.modelo.Seccion;
import com.sv.udb.modelo.Seguimiento;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.modelo.TipoBeca;
import com.sv.udb.modelo.TipoEstado;
import com.sv.udb.modelo.TipoRetiro;
import com.sv.udb.modelo.UsuarioRol;
import com.sv.udb.utils.Archivo;
import com.sv.udb.utils.Conexion;
import com.sv.udb.utils.DynamicField;
import com.sv.udb.utils.pojos.DatosAlumnos;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Ariel
 */
@Named(value = "becasBean")
@ViewScoped
public class BecasBean implements Serializable{

   
    
    
    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;
    
    private static final long serialVersionUID = -5196715359527212082L;
    
    @Inject
    private GlobalAppBean globalAppBean; //Bean de aplicación (Instancia)
    private byte[] docuRepo;
    
     @EJB
     private DocumentoFacadeLocal FCDEDocu;
     @EJB
     private SeguimientoFacadeLocal FCDESegu;
    @EJB
    private GradoFacadeLocal FCDEGrado;
    @EJB
    private TipoBecaFacadeLocal FCDETipoBeca;
    @EJB
    private DetalleBecaFacadeLocal FCDEDetaBeca;
     @EJB
    private UsuarioRolFacadeLocal UsuarioRolFCDE;
    private List<TipoBeca> listTipoBeca;

    public List<TipoBeca> getListTipoBeca() {
        return listTipoBeca;
    }

    public void setListTipoBeca(List<TipoBeca> listTipoBeca) {
        this.listTipoBeca = listTipoBeca;
    }
    @EJB
    private SolicitudBecaFacadeLocal FCDESoli;
    private SolicitudBeca objeSoli;
    private SolicitudBeca objeSoli2;
    private List<SolicitudBeca> listSoli;
    private List<SolicitudBeca> listSoliH;
    private List<SolicitudBeca> listSoliActivos;
    private String carnet; //Filotro de búsqueda
    private StreamedContent fotoAlum;

    @EJB
    private BecaFacadeLocal FCDEBeca;
    private Beca objeBeca;
    private Beca objeBeca2;
    private List<Beca> listBeca;
    private List<Beca> listBecaH;
    private List<Beca> listBecaActivos;
    private List<Beca> listBecaDocu;
    
    //Para las modificaciones de las demás tablas
    private List<DetalleBeca> listDetaBeca;
    private List<Seguimiento> listSegu;
    private List<Documento> listDocu;
    
    private String buscBecaByAlum = "";//Alvin agrego esto para el buscador.

    public String getBuscBecaByAlum() {
        return buscBecaByAlum;
    }

    public void setBuscBecaByAlum(String buscBecaByAlum) {
        this.buscBecaByAlum = buscBecaByAlum;
    }
            
    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }
    
    private boolean guardar; 
    private static Logger log = Logger.getLogger(BecasBean.class);

    public SolicitudBeca getObjeSoli() {
        return objeSoli;
    }

    public void setObjeSoli(SolicitudBeca objeSoli) {
        this.objeSoli = objeSoli;
    }

    public List<SolicitudBeca> getListSoliH() {
        return listSoliH;
    }

    public void setListSoliH(List<SolicitudBeca> listSoliH) {
        this.listSoliH = listSoliH;
    }

    public List<Beca> getListBecaH() {
        return listBecaH;
    }

    public void setListBecaH(List<Beca> listBecaH) {
        this.listBecaH = listBecaH;
    }

    public List<Beca> getListBecaActivos() {
        return listBecaActivos;
    }
    public List<SolicitudBeca> getListSoli() {
        return listSoli;
    }

    public void setListSoli(List<SolicitudBeca> listSoli) {
        this.listSoli = listSoli;
    }

    public List<Beca> getListBecaDocu() {
        return listBecaDocu;
    }

    public StreamedContent getFotoAlum() {
        return fotoAlum;
    }    

    public List<DetalleBeca> getListDetaBeca() {
        return listDetaBeca;
    }

    public void setListDetaBeca(List<DetalleBeca> listDetaBeca) {
        this.listDetaBeca = listDetaBeca;
    }

    public List<Seguimiento> getListSegu() {
        return listSegu;
    }

    public void setListSegu(List<Seguimiento> listSegu) {
        this.listSegu = listSegu;
    }

    public List<Documento> getListDocu() {
        return listDocu;
    }

    public void setListDocu(List<Documento> listDocu) {
        this.listDocu = listDocu;
    }
    
    
    public Beca getObjeBeca() {
        return objeBeca;
    }

    public void setObjeBeca(Beca objeBeca) {
        this.objeBeca = objeBeca;
    }

    public List<Beca> getListBeca() {
        return listBeca;
    }

    public void setListBeca(List<Beca> listBeca) {
        this.listBeca = listBeca;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }

    public String getFilt() {
        return carnet;
    }

    public void setFilt(String carnet) {
        this.carnet = carnet;
    }

    public List<SolicitudBeca> getListSoliActivos() {
        return listSoliActivos;
    }
    
    public byte[] getDocuRepo() {
        return docuRepo;
    }
    
    /**
     * Creates a new instance of BecaSoliBean
     */
    public BecasBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeSoli = new SolicitudBeca();
        this.guardar = true;
        this.objeBeca = new Beca();
        this.consTodo();
        initDina();
        initDocu();
    }
    
    public void limpForm()
    {
        this.objeBeca = new Beca();  
        this.objeSoli = new SolicitudBeca();
        this.objeSoli.setFechSoliBeca(new Date());
        this.objeBeca.setFechBaja(new Date());
        this.guardar = true; 
        this.carnet = "";
    }
    public boolean guar()
    {        
        boolean respGuar = false;
        this.objeSoli.setFechSoliBeca(new Date());
        this.objeBeca.setFechInic(new Date());
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(objeSoli.getCarnAlum() == null || objeSoli.getNombAlum() == null)
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Busque un alumno')");
                respGuar = false;
            }
            else
            {
                    TipoEstado a = new TipoEstado();
                    a.setCodiTipoEsta(4);
                    this.objeSoli.setEstaSoliBeca(4);
                    FCDESoli.create(objeSoli);
                    this.objeSoli2 = FCDESoli.findLast();
                    this.objeBeca.setCodiSoliBeca(objeSoli2);
                    this.objeBeca.setCodiTipoEsta(a);                   
                    this.FCDEBeca.create(objeBeca);
                    this.listSoli.add(this.objeSoli);
                    if(this.listBeca == null)
                    {
                        this.listBeca = new ArrayList<>();
                    }
                    this.listBeca.add(objeBeca);
                    ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
                    //log.info("Beca Guardada");
                    this.guardar = false;
                    objeSoli =FCDESoli.findLast();
                    objeBeca=FCDEBeca.findLast();
                    respGuar = true;
                    this.toogEmpr();
            
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            //log.error(getRootCause(ex).getMessage());
            respGuar = false;
        }
        finally
        {
            
        }
        return respGuar;
    }
    
    /*ORDEN DE LAS COSAS A GUARDAR EN LOS RETIROS Y ACTUALIZACIONES DEL ESTADO
    Tipo de operación (Ej. Cambio de empresa, cambio de condiciones, cancelación de beca)
    Estado antiguo (Ej. empresa antigua, antigua condición)
    Razón del cambio
    Nuevo estado (nombre de la nueva empresa padrino, nueva condición)*/
 
    //Variables para la razón de retiro
    String tipo, anti,razon, nuev;
    
    public void modi(int num)
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            TipoEstado es = new TipoEstado();
            int tipoEsta = 0;
            razon = "["+this.objeBeca.getRetiBeca()+"]";
            //el objeto objeSoli tiene los datos de la solicitud pero modificados, entonces hay que consultar la misma solicitud
            //pero con los datos viejos.
            //Objetos viejitos
            objeSoli2 = FCDESoli.find(this.objeSoli.getCodiSoliBeca());
            this.objeBeca2 = FCDEBeca.findSoli(this.objeSoli2.getCodiSoliBeca());
            switch (num) {
                case 1:
                    tipo = "[Cambio de empresa patrocinador]";
                    //Empresa antigua
                    anti = "[" + this.objeSoli2.getCodiEmpr().getNombEmpr() + "]";
                    //Empresa nueva
                    nuev = "[" + this.objeSoli.getCodiEmpr().getNombEmpr() + "]";
                    tipoEsta = this.objeBeca2.getCodiTipoEsta().getCodiTipoEsta();
                    break;
                case 2:
                    System.out.println("Estado: " + this.objeBeca.getCodiTipoEsta().getNombTipoEsta());
                    tipoEsta = this.objeBeca.getCodiTipoEsta().getCodiTipoEsta();
                    tipo = "[Cambio de estado]";
                    //Estado antiguo
                    anti = "["+this.objeBeca2.getCodiTipoEsta().getNombTipoEsta()+"]";
                    //Nuevo estado
                    nuev = "["+this.objeBeca.getCodiTipoEsta().getNombTipoEsta()+"]";
                    break;
                default:
                    ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
                    break;
            }
            //Objetos nuevitos (El de la soli ya lo tiene el objeSoli actual)
            this.objeBeca = FCDEBeca.findSoli(this.objeSoli.getCodiSoliBeca());
            //Operaciones a objetos viejitos
            this.objeSoli2.setEstaSoliBeca(3);//3 de historial
            FCDESoli.edit(objeSoli2);//edita el registro de la solicitud en la base
            this.listSoli.add(objeSoli2);//Agrega la solicitud "vieja" a la lista
            es.setCodiTipoEsta(3);
            //Setearle la razón a la criatura
            this.objeBeca2.setRetiBeca(tipo+anti+razon+nuev);
            this.objeBeca2.setCodiTipoEsta(es);//le setea el estado de la beca de 3 de historial
            this.objeBeca2.setFechBaja(new Date());//le setea la fecha de baja
            FCDEBeca.edit(objeBeca2);//edita el registro viejo de la base

            //Cosas que no sé que hacen aquí
            this.listSoli.remove(this.objeSoli2);//quita la solicitud de la lista
            this.listBeca.remove(this.objeBeca2);//quita la beca de la lista
            
            //Operaciones a los objetos nuevitos
            this.objeSoli.setFechSoliBeca(new Date());
            FCDESoli.create(objeSoli);//crea el nuevo registro de la solicitud
            this.listSoli.add(objeSoli);//lo agrega a la lista
            this.objeSoli = FCDESoli.findLast();//busca la ultima solicitud creada
            
            this.objeBeca.setCodiSoliBeca(objeSoli);//le setea el codigo de la solicitud
            es.setCodiTipoEsta(tipoEsta);//le setea el mismo estado de la soli a la beca
            this.objeBeca.setCodiTipoEsta(es);
            this.objeBeca.setFechInic(new Date());
            FCDEBeca.create(objeBeca);//crea el nuevo registro de la beca
            this.listBeca.add(objeBeca);//lo agrega a la lista
            this.consTodo();//consulta los registros con estado diferente a 3
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
           // log.info("Solicitud Modificada");
            //Enviar al método de las modificaciones en las otra tablas

            this.cambios(objeBeca2.getCodiBeca(), 1);
            ctx.execute("$('#CambEsta').modal('hide');");
            ctx.execute("$('#CambPatr').modal('hide');");
            //Recarga
             ctx.execute("window.location.reload(true);" );
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
           // log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    
    public void reActi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            /*Busca el objeto viejo le setea el estado 3 de historial y lo modifica*/
            //this.listBeca.remove(this.objeBeca);
            this.objeSoli2 = this.objeSoli;
            this.objeSoli.setEstaSoliBeca(3);
            FCDESoli.edit(objeSoli);     
            this.objeBeca = FCDEBeca.find(objeSoli.getCodiSoliBeca());
            this.objeBeca2 = this.objeBeca;
            TipoEstado esta = new TipoEstado();
            esta.setCodiTipoEsta(3);
            this.objeBeca.setCodiTipoEsta(esta);
           objeBeca.setRetiBeca("Proceso de evaluación completado.Beca activada");
            FCDEBeca.edit(objeBeca);                          
            this.objeSoli2.setEstaSoliBeca(1);            
            TipoRetiro reti = new TipoRetiro();
            reti.setCodiReti(null);
            this.objeBeca2.setCodiReti(null);
            this.objeBeca2.setRetiBeca(null);
            esta.setCodiTipoEsta(1);
            this.objeBeca2.setCodiTipoEsta(esta);
            //objeSoli2.setBecaList(null);
            objeSoli2.setFechSoliBeca(new Date());
            FCDESoli.create(objeSoli2);
            this.objeSoli2 = FCDESoli.findLast();
            this.objeBeca2.setCodiSoliBeca(objeSoli2);
            this.objeBeca2.setFechInic(new Date());
            this.objeBeca2.setFechBaja(null);
            
            FCDEBeca.create(objeBeca2);     
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Beca Reactivada')");
            //log.info("Beca reactivada");
            this.consTodo();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
           // log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    /**
     * Función que cancela una beca por diversos motivos consultados desde la tabla tipo de retiro, así mismo debe 
     */
    public void desa()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            //Para el retiro
            String tipoCamb = "[Cancelación de beca]";
            this.objeBeca2 = FCDEBeca.findSoli(this.objeSoli.getCodiSoliBeca());
            String antiEsta = "["+this.objeBeca2.getCodiTipoEsta().getNombTipoEsta()+"]";
            String razo = "["+this.objeBeca.getRetiBeca()+"]";
            String nuevEsta = "[Beca cancelada]";
            
            this.listBeca.remove(this.objeBeca); //Limpia el objeto viejo
            TipoEstado esta = new TipoEstado();
            esta.setCodiTipoEsta(2);
            this.objeBeca.setCodiTipoEsta(esta);
            this.objeBeca.setRetiBeca(tipoCamb+antiEsta+razo+nuevEsta);
            this.objeBeca.setFechBaja(new Date());
            this.objeSoli.setEstaSoliBeca(2);
            FCDEBeca.edit(this.objeBeca);
            FCDESoli.edit(objeSoli);
            FCDEDetaBeca.desa_deta(this.objeBeca.getCodiBeca());
            this.listBeca.add(this.objeBeca); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Beca desactivada')");
            //recarga
            ctx.execute("window.location.reload(true);" );
            //log.info("Beca desactivada");
            this.consTodo();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
           // log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
   
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {           
            this.objeSoli = FCDESoli.find(codi);
            this.objeBeca = FCDEBeca.findSoli(objeSoli.getCodiSoliBeca());            
            listTipoBeca = FCDETipoBeca.findTipos(objeBeca.getCodiSoliBeca().getCodiGrad().getNivelGrad());          
            this.guardar = false;
            this.carnet = objeSoli.getCarnAlum();
            initDina();
            consTodoDocu();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeBeca.getCodiSoliBeca().getNombAlum()) + "')");
            //log.info("Beca Consultada");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
           // log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void cons(int codi)
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        //int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {      
            System.out.println("Codigo de beca: "+codi);
            this.objeSoli = FCDESoli.find(codi);
            this.objeBeca = FCDEBeca.findSoli(objeSoli.getCodiSoliBeca());            
            listTipoBeca = FCDETipoBeca.findTipos(objeBeca.getCodiSoliBeca().getCodiGrad().getNivelGrad());          
            this.guardar = false;
            this.carnet = objeSoli.getCarnAlum();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeBeca.getCodiSoliBeca().getNombAlum()) + "')");
            //log.info("Beca Consultada");
        }
        catch(Exception ex)
        {
            System.out.println("Error en consultar por codigo. Por la nueva forma de ver becas xd");
            //ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar por codigo')");
            //log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
  
    
    public boolean cons(String obje)
    {
        boolean variable=false;
        try
        {
           SolicitudBeca s1 = new SolicitudBeca();
            s1 = FCDESoli.findCarnet(obje);
            if(s1 != null)
            {variable=true;}
        }
        catch(Exception ex)
        {
            
        }
        return variable;
    }
    public void consTodo()
    {
        try
        {
            if(listSoli==null){listSoli= new ArrayList();}
            if(listBeca==null){listBeca=new ArrayList();}
             if(listSoliActivos==null){listSoliActivos= new ArrayList();}
            if(listBecaActivos==null){listBecaActivos=new ArrayList();}
             if(listBecaDocu==null){listBecaDocu= new ArrayList();}
             
            this.listSoli = FCDESoli.findAll();
            this.listBeca = FCDEBeca.findAllH();
            this.listSoliActivos = FCDESoli.findAllActivos();
            this.listBecaActivos = FCDEBeca.findAllActivos();
            this.listBecaDocu = FCDEBeca.findAllDocu();
            //log.info("Beca Consultadas");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            //log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void procVisi() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            params.put("Carnet", this.objeBeca.getCodiSoliBeca().getCarnAlum());
            String pathRepo = globalAppBean.getResourcePath("Reportes_Becas/HistorialBeca.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }
    
    public boolean consW()
    {
        boolean respFunc = false;
        RequestContext ctx = RequestContext.getCurrentInstance();
        Client client = ClientBuilder.newClient();
        String url = String.format("http://www.opensv.tk:8080/WebService/MiServicio/consAlum/%s", this.carnet.trim());        
       if(this.cons(carnet.trim()))
       {           
             ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El Alumno ya se encuentra')");
             
       }
       else
       {
           WebTarget resource = client.target(url);
            Invocation.Builder request = resource.request();
            request.accept(MediaType.APPLICATION_JSON);
            Response response = request.get();
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
            {
                DatosAlumnos resp = response.readEntity(DatosAlumnos.class); //La respuesta de captura en un pojo que esta en el paquete utils
                
                if(resp.getNomb() == null || resp.getNomb().equals(""))
                {
                    ctx.execute("setMessage('MESS_WARN', 'Atención', 'Alumno no encontrado.')");
                    respFunc = false;
                }
                else
                {                
                    if(objeSoli ==null){objeSoli=new SolicitudBeca();}
                    this.objeSoli.setCarnAlum(carnet.trim());
                    this.objeSoli.setNombAlum(resp.getNomb());;
                    Grado grad = new Grado();
                    String cortado= resp.getGrad().substring(0,1);                
                    grad=FCDEGrado.find(Integer.parseInt(cortado));   
                    this.objeSoli.setCodiGrad(grad);
                    this.objeSoli.setFotoAlum(resp.getFoto());
                    this.objeSoli.setEspeAlum(resp.getEspe());
                    this.objeSoli.setGrupAlum(resp.getGrup());
                    this.objeSoli.setSeccAcad(resp.getSeccAcad());
                    this.objeSoli.setSeccTecn(resp.getSeccTecn());
                    if(resp.getFoto() != null)
                    {
                        //String base64Image = new String(Base64.getDecoder().decode(resp.getFoto()));
                        //System.err.println("Base:" +base64Image);
                        //this.fotoAlum = new DefaultStreamedContent(base64Image, "image/jpeg", "Demo.jpg");
                    }
                    else
                    {
                        this.fotoAlum = new DefaultStreamedContent();
                    }
                    this.toogFich();
                    respFunc = true;
                }
            }
            else
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar alumno')");
                respFunc = false;
            }       
       }
       return respFunc;
    }
    
    /**
     * Funcion para actualizar los registros de las tablas seguimientos, detalle de beca y documentos cuando se haga algun cambio en la beca
     * @param codiBecaAnt codigo de la beca que se ha modificado, sus detalles deben de pasar a la nueva beca
     * @param  tipo si son modificaciones para cambiar el código de la beca o desactivación de la beca para cancelar todos los elementos relacionados
     */
    public void cambios(int codiBecaAnt, int tipo){
        RequestContext ctx = RequestContext.getCurrentInstance();
        try {
            //Nueva beca que se ha creado y a la que hay que asignarle las cosas de antes
            this.objeBeca = this.FCDEBeca.findLast();
            this.objeBeca2 = this.FCDEBeca.findBeca(codiBecaAnt);
            
            System.out.println("AAAAAAAHHHHHHH " +objeBeca.getCodiBeca() +" "+codiBecaAnt);
            FCDESoli.updateAll(objeBeca.getCodiBeca(),codiBecaAnt);
            
            
            
            //Listas de datos de las diferentes tablas
            //Detalles de beca
//            this.listDetaBeca = this.FCDEDetaBeca.findByBeca(codiBecaAnt);
//            System.out.println("Codigo de la solicitud: "+objeBeca2.getCodiSoliBeca().getCodiSoliBeca());
//            this.listSegu = this.FCDESegu.findBySoliInSpec(objeBeca2.getCodiSoliBeca().getCodiSoliBeca());
//            this.listDocu = this.FCDEDocu.findBySoli(objeBeca2.getCodiSoliBeca().getCodiSoliBeca());
//            if(listDetaBeca == null)
//                listDetaBeca = new ArrayList<>();
//                 if(listSegu == null)
//                     listSegu = new ArrayList<>();
//                     if(listDocu == null)
//                         listDocu =new ArrayList<>();
//                
//            switch (tipo) {
//                //Caso 1 son modificaciones
//                case 1:
//                    //Si cada lista no está vacia debería cambiar el codigo de la beca en todos los registros dentro de las listas
//                    if (!listDetaBeca.isEmpty()) {
//                        System.out.println("Si tiene detalles");
//                        //Para los detalles
//                        /*for (DetalleBeca temp : listDetaBeca) {
//                        temp.setCodiBeca(objeBeca);
//                        FCDEDetaBeca.edit(temp);
//                        }*/
//                    }
//                    else
//                    {
//                         System.out.println("No  tiene detalles");
//                    }
//                    if (!listSegu.isEmpty()) {
//                        System.out.println("Si tiene seguimientos");
//                        
//                        for (Seguimiento temp : listSegu) {
//                            temp.setCodiSoliBeca(this.objeBeca.getCodiSoliBeca());
//                            FCDESegu.edit(temp);
//                        }
//                    }
//                    else
//                    {
//                         System.out.println("No tiene seguimientos");
//                    }
//                    if (!listDocu.isEmpty()) {
//                        System.out.println("Si tiene documentos");
//                        /*Documentos
//                        for (Documento temp : listDocu) {
//                            temp.setCodiSoliBeca(this.objeBeca.getCodiSoliBeca());
//                            FCDEDocu.edit(temp);
//                        }  */
//                    }
//                    else
//                    {
//                         System.out.println("No tiene documentos");
//                    }
//                    break;
//                default:
//                    ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar alumno')");
//                    break;}

        }
         catch (Exception ex) {
            ex.printStackTrace();
           // log.error(getRootCause(ex).getMessage());
        }
    }
    
    
    /*-----------------------------------------------------------*/
    //Aquí abajo estan toda la logica necesaria para los barridos, slider    
    /*-----------------------------------------------------------*/
    
    //Otra variable que pertece a laogica para los barridos es guardar
    private boolean showCarn=false;
    private boolean showFich=false;
    private boolean showEmpr=false;
    private boolean showSoci=false;
    /*Explicación de como funciona:
    
    Guardar: Cuando por ejemplo guardar sea true, eso quiere decir que no se esta modificando. Eso quiere decir,que cuando sea true
    se pueden mostrar formularios que no deben mostrarse cuando se esta modificando, por ejemplo para ingresar el carnet o
    para ingresar la empresa. En ambos casos no se esta guardando, ah! pero son barridos diferentes, eso corresponde a otras
    variables. 
    
    showCarn: esta varible, solo define si se muestra el campo donde requerimos el carnet para hacer una nueva solicitud, 
    esto quiere decir que cuando esta variable sea true Y la variable de guardar sea true, solo se mostrara el campo que pida
    el carnet. Porque cuando pida el carnet, quiere decir que esta agregando una nueva solicitud y no esta modificando. 
    
    showEmpr: Es igual a showCarn, las dos variables se utilizan cuando se esta agregando una nueva solicitud, por lo tanto
    cuando guardar es igual a true , cuando no se esta modificando, lo complicado es que son dos variables, dos barridos que 
    deben activarse y desactivarse mientras guardar esta true
    
    Proceso para guardar -->            Pedir Carnet --> Mostrar Ficha --> Pedir Empresa --> Formulario para modificar
                        | Guardar         true              true            true                 false
                        | showCarnt       true              false           false                false    
                        | showFich        false             true            false                true
                        | showEmpres      false             false           true                 false
    
    
    En la tabla anterior se puede obsservar como es el comportamiento de las variables durante todo el proceso 
    se puede observar que empieza que hasta que se llega al formulario de modificar la variable de guardar va a ser true
    y segun sea el paso  o "step" de agregar solicitud las otras variablas van a ir cambiando, podes ver a "Empresa", "Ficha",
    "Carnet" como bloques, nosotros definimos que loque se muestra en cada paso, en el paso numero 1 donde pedimos el carnet
    decimos que solo muestre el bloque del carnet y así. 
    
    Ficha: este es un bloque ambiguo. Se puede interpretar de dos maneras, se puede utilizar para guardar y para modificar
    varios elementos de el se muestra y no se muestran  dependiente si se este guardando(gaurdar = true) o modificando
    (guardar = false) Por ejemplo cuando se esta guardando solo muestra la información del alumno y el boton "Agregar nueva soli"
    pero cuando se esta modificando  no  muestra ese boton si no, que muesta las tablas de documentos y detalles
      
    */

    public boolean isShowSoci() {
        return showSoci;
    }

    public void setShowSoci(boolean showSoci) {
        this.showSoci = showSoci;
    }
    
    public boolean isShowCarn() {
        return showCarn;
    }
    public boolean isShowFich() {
        return showFich;
    }
     public boolean isShowEmpr() {
        return showEmpr;
    }
    public void toogEmpr()
    {
        this.showEmpr = !this.showEmpr;
              
    }  
    public void toogFich()
    {
        this.showFich = !this.showFich;
        
    }       
    public void toogCarn()
    {
        this.showCarn = !this.showCarn;
    }
     public void toogSoci()
    {
        this.showSoci = !this.showSoci;
         showCarn=false;   
       showFich=false;
       showEmpr=false;
    }
    public void toogRegre()
    {
         RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
       showCarn=false;   
       showFich=false;
       showEmpr=false;
       this.guardar=true;
       init();
       this.limpForm();
       
       //recarga
       ctx.execute("window.location.reload(true);" );
    }
    
    /*-----------------------------------------------------------*/
    //Aquí termina  toda la logica necesaria para los barridos, slider    
    /*-----------------------------------------------------------*/
    /*-----------------------------------------------------------*/
    //Aquí empieza toda la logia del formulario dinamico  
    /*-----------------------------------------------------------*/
    
     @EJB
    private RespuestaFacadeLocal FCDEResp;
    private List<DynamicField> listCmps;
    private Map<String, Object> mapa;
    
      
    @EJB
    private OpcionFacadeLocal FCDEOpci;
     @EJB
     private PreguntaFacadeLocal FCDEPreg;
     @EJB
     private SeccionFacadeLocal FCDESecc;
     
     
    private List<Opcion> listOpci;
    private List<Pregunta> listPreg;
    private List<Seccion> listSecc;
    
     private List<Respuesta> listResp;
    
    
     //Bean de session
    @Inject
    private LoginBean logiBean; 
    
    
   
    public Map<String, Object> getMapa() {
        return mapa;
    }

    public void setMapa(Map<String, Object> mapa) {
        this.mapa = mapa;
    }
    
    public void initDina()
    {
        try {
             
            this.listCmps = new ArrayList<>();
            this.mapa = new HashMap<>();
            this.VeriRole();
            consTodoDina();          
            createMap();
            
           
            
        } catch (Exception e) {
            System.out.println("Error en init :"+e.getMessage());
        }
    }
    
    public void createMap()
    {
        try {
             for(Opcion temp : this.listOpci)
            {
                String codiDina = String.format("Dina%s", String.valueOf(temp.getCodiOpci()));
                /*codigo y respuesta xd*/
                Map<Object, Object> listOpciTemp = null;
                if(temp.getCodiEstr().getTipoEstr().equals("SELECT") || temp.getCodiEstr().getTipoEstr().equals("SELECTMANYCHECKBOX"))
                {
                    listOpciTemp = new HashMap<>();
                    for(OpcionRespuesta tempOR : temp.getOpcionRespuestaList())
                    {
                        if(tempOR.getEstaOpci() == 1)
                        {
                            listOpciTemp.put(tempOR.getCodiOpciResp(),tempOR.getDescOpci());
                        }
                    }
                   // this.mapa.put(codiDina, new Object());
                }
                    
                    if(listResp != null)
                    {
                        String checkBox="";
                        if(temp.getCodiEstr().getTipoEstr().equals("SELECTMANYCHECKBOX"))
                        {   
                                checkBox="";
                          
                               for(Respuesta tempRe : listResp)
                                {
                                    if(Objects.equals(tempRe.getCodiOpci().getCodiOpci(), temp.getCodiOpci()))
                                    {
                                        
                                        checkBox += tempRe.getCodiOpciResp().getDescOpci()+",";
                                    }
                                }
                               
                               String[] Arreglo  = checkBox.split(",");
                               String[] ids = {"1", "2"}; 
                               String[] keys = {"Descripcion 1", "Descripcion 2"}; 
                               Map<String, String> map = new HashMap<String, String>();
                                map.put("1", "Descripcion 1");	
                                map.put("2", "Descripcion 2");	
                                
//                                Map<String[], String[]> map = new HashMap<String[], String[]>();
//                                map.put(ids, keys);	
                                
//                                Map<String, Object[]> map = new HashMap<String, Object[]>();                               
//                                map.put(codiDina, keys);
                               
                               this.mapa.put(codiDina,ids);
                        }
                        else                                        
                        {
                            for(Respuesta tempRe : listResp)
                            {
                                if(tempRe.getCodiOpci().getCodiOpci()==temp.getCodiOpci())
                                {
                                    this.mapa.put(codiDina, tempRe.getDescOpci());
                                }
                            }

                        }
                    }
                    //this.mapa.put(codiDina, "Demo " + codiDina);
                
                
                this.listCmps.add(new DynamicField(temp.getTituOpci(), codiDina, listOpciTemp, temp.getCodiEstr().getTipoEstr(),temp.getCodiPreg()));
            }
        } catch (Exception e) {
            System.err.println("Error en createMap "+e.getMessage());
        }
    }
    public void consTodoDina()
    {
        try
        {
            this.listOpci= FCDEOpci.findAllActive();
            this.listPreg = FCDEPreg.findAllActive();
            this.listSecc=FCDESecc.findAllActive();
            if(objeSoli.getCodiSoliBeca() != null)
            {
                this.listResp = FCDEResp.findAll(objeSoli.getCodiSoliBeca());
                System.out.println("Este alumnazo si tiene estudio :3");
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            
        }
        finally
        {
            
        }
    }
    
    public void guarDina()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
       
        try
        {
            
            if(this.carnet==null)            
            {
               carnet=logiBean.getObjeUsua().getAcceUsua();                               
                if(this.consW())
                {
                    this.objeSoli.setCodiEmpr(new Empresa(1));
                    this.guar();
                }
            }
            /*Crear la nuva solicitud*/                      
            for(DynamicField temp:this.listCmps)
            {
                String valor = "";
                Integer codiDinaDb = Integer.parseInt(temp.getFieldKey().replace("Dina", ""));
                if(temp.getType().equals("SELECTMANYCHECKBOX"))
                {
                    String respArray = "";
                    for(Object tempResp : (Object[])this.mapa.get(temp.getFieldKey()))
                    {
                        int codigoOpcionRespuesta = Integer.parseInt(String.valueOf(tempResp));
                        respArray = respArray + "-" + tempResp;
                        Respuesta respuesta = new Respuesta( objeSoli,new Opcion(codiDinaDb),new OpcionRespuesta(codigoOpcionRespuesta),1);
                        respuesta.setDescOpci("S/R");
                        FCDEResp.create(respuesta);
                    }
                    respArray = respArray.trim();
                    valor = "id: " + codiDinaDb + " === valor: " + respArray;
                    //System.out.println(valor);
                }
                else
                {
                    String valorDb = this.mapa.get(temp.getFieldKey()).toString();                    
                    Respuesta respuesta = new Respuesta(objeSoli,new Opcion(codiDinaDb),valorDb,1);
                    FCDEResp.create(respuesta);
                    valor = "id: " + codiDinaDb + " === valor: " + this.mapa.get(temp.getFieldKey());
                    //System.out.println(valor);
                }               
            }
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            //Recarga
            ctx.execute("window.location.reload(true);" );
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            System.out.println("Error: "+ex.getMessage());
            System.out.println("Error: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
  
    
    @SuppressWarnings("cast")
    public void populateForm(ComponentSystemEvent event)
    {
        
        try  
        {
            
           HtmlForm form = (HtmlForm) event.getComponent();
            for(Seccion seccion : this.listSecc)
            {
                form.getChildren().add(this.createUIOutput("<div class=\"panel panel-primary\">"));
                form.getChildren().add(this.createUIOutput("<div class=\"panel-heading\">"));
                form.getChildren().add(this.createUIOutput(seccion.getNombSecc()));
                form.getChildren().add(this.createUIOutput("</div>"));
                form.getChildren().add(this.createUIOutput(" <div class=\"panel-body\">"));         
                form.getChildren().add(this.createUIOutput(" <h3>Indicaciones: "+seccion.getIndiSecc()+"</h3>"));       
                form.getChildren().add(this.createUIOutput("<fieldset>"));
                
                    for(Pregunta pregunta :this.listPreg)
                    {
                        if(seccion.getCodiSecc().equals(pregunta.getCodiSecc().getCodiSecc()))
                        {

                            form.getChildren().add(this.createUIOutput("<h3>"+pregunta.getDescPreg()+"</h3>"));
                            for (DynamicField field : this.listCmps) //Recorre los elementos
                            {
                                if(Objects.equals(field.getId().getCodiPreg(), pregunta.getCodiPreg()))
                                {
                                    form.getChildren().add(this.createUIOutput("<div class=\"form-group\">"));
                                    switch (field.getType())
                                    {
                                        case "TEXT":
                                            //Crea el label
                                            form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
                                            //Crea el input
                                            form.getChildren().add(this.getUIComponent(field, HtmlInputText.COMPONENT_TYPE));
                                            break;
                                        case "TEXTAREA":
                                            //Crea el label
                                            form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
                                            //Crea el input
                                            form.getChildren().add(this.getUIComponent(field, HtmlInputTextarea.COMPONENT_TYPE));
                                            break;
                                        case "SELECT":
                                            //Crea el label
                                            form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
                                            //Crea el select
                                            form.getChildren().add(this.getUIComponent(field, HtmlSelectOneMenu.COMPONENT_TYPE));
                                            break;
                                        case "SELECTMANYCHECKBOX":
                                            //Crea el label
                                            form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
                                            //Crea el select
                                            form.getChildren().add(this.getUIComponent(field, HtmlSelectManyCheckbox.COMPONENT_TYPE));
                                            break;
                                    }
                                    form.getChildren().add(this.createUIOutput("</div>"));

                                }

                            }

                        }
                    }
                form.getChildren().add(this.createUIOutput("</fieldset>"));
                form.getChildren().add(this.createUIOutput("</div>"));
                form.getChildren().add(this.createUIOutput("</div>"));
            }
            //Agregar los botones
                UIComponent btonGroup = this.getUIButtons(form);
                if(btonGroup != null)
                {
                    form.getChildren().add(btonGroup);
                }
        } 
        catch (Exception e) {
            System.out.println("Error populate:"+e.getMessage());
        }
          
    }

    private ValueExpression createValueExpression(String string, Class<String> aClass) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory()
                .createValueExpression(context.getELContext(), string, aClass);
    }
    
    private UIOutput createUIOutput(String value)
    {
        UIOutput resp = new UIOutput();
        resp.setRendererType("javax.faces.Text");
        resp.setValue(value);
        return resp;
    }
    
    private UIComponent getUIButtons(HtmlForm form)
    {
        UIComponent resp = null;
        for(UIComponent temp : form.getChildren())
        {
            if(temp.getId().equals("btonGroup"))
            {
                resp = temp;
                break;
            }
        }
        return resp;
    }
    
    private UIComponent getUIComponent(DynamicField field, String type)
    {
        UIComponent resp = null;
        Application app = FacesContext.getCurrentInstance().getApplication();
        if(type.equals(HtmlOutputLabel.COMPONENT_TYPE))
        {
            HtmlOutputLabel label = (HtmlOutputLabel)app.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
            label.setFor(field.getFieldKey());
            label.setValueExpression("value", createValueExpression(field.getLabel(), String.class));
            resp = label;
        }          
        else if(type.equals(HtmlInputText.COMPONENT_TYPE))
        {
            HtmlInputText input = (HtmlInputText)app.createComponent(HtmlInputText.COMPONENT_TYPE);
            input.setId(field.getFieldKey());
            input.setValueExpression("value", createValueExpression("#{becasBean.mapa['" + field.getFieldKey() + "']}", String.class));
            input.setStyleClass("form-control");
            resp = input;
        }
        
        else if(type.equals(HtmlInputTextarea.COMPONENT_TYPE))
        {
            HtmlInputTextarea input = (HtmlInputTextarea)app.createComponent(HtmlInputTextarea.COMPONENT_TYPE);
            input.setId(field.getFieldKey());
            input.setValueExpression("value", createValueExpression("#{becasBean.mapa['" + field.getFieldKey() + "']}", String.class));            
            input.setStyleClass("form-control");
            resp = input;
        }
         else if(type.equals(HtmlDataTable.COMPONENT_TYPE ))
        {
            
        }     
        else if(type.equals(HtmlSelectOneMenu.COMPONENT_TYPE))
        {
            HtmlSelectOneMenu input = (HtmlSelectOneMenu)app.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
            input.setId(field.getFieldKey());
            input.setValueExpression("value", createValueExpression("#{becasBean.mapa['" + field.getFieldKey() + "']}", String.class));
            input.setStyleClass("form-control");
            if(field.getFieldValue() != null)
            {
                UISelectItems objeItems = new UISelectItems();
                List<SelectItem> listItems = new ArrayList<>();
                SelectItem seleItem = new SelectItem();
                seleItem.setValue(null);
                seleItem.setLabel("Seleccione...");
                listItems.add(seleItem);
                for(Object entry : ((HashMap)field.getFieldValue()).entrySet())
                {
                    Map.Entry<Object, Object> item = (Map.Entry<Object, Object>)entry;
                    seleItem = new SelectItem();
                    seleItem.setValue(item.getKey());
                    seleItem.setLabel((String)item.getValue());
                    listItems.add(seleItem);
                }
                objeItems.setValue(listItems.toArray());
                input.getChildren().add(objeItems);
            }
            resp = input;
        }
        else if(type.equals(HtmlSelectManyCheckbox.COMPONENT_TYPE))
        {
            HtmlSelectManyCheckbox input = (HtmlSelectManyCheckbox)app.createComponent(HtmlSelectManyCheckbox.COMPONENT_TYPE);
            input.setId(field.getFieldKey());
            input.setValueExpression("value", createValueExpression("#{becasBean.mapa['" + field.getFieldKey() + "']}", String.class));
            input.setStyleClass("form-control");
            if(field.getFieldValue() != null)
            {
                UISelectItems objeItems = new UISelectItems();
                List<SelectItem> listItems = new ArrayList<>();
//                SelectItem seleItem;
                for(Object entry : ((HashMap)field.getFieldValue()).entrySet())
                {
                    Map.Entry<Object, Object> item = (Map.Entry<Object, Object>)entry;
                    listItems.add(new SelectItem(item.getKey(), (String)item.getValue()));
                }
                objeItems.setValue(listItems);
                input.getChildren().add(objeItems);
            }
            resp = input;
        }
        return resp;
    }
    
    /*Para barridos */
      private boolean TrabaSoci=false;
    private boolean Alum=false;

    public boolean isTrabaSoci() {
        return TrabaSoci;
    }

    public void setTrabaSoci(boolean TrabaSoci) {
        this.TrabaSoci = TrabaSoci;
    }

    public boolean isAlum() {
        return Alum;
    }

    public void setAlum(boolean Alum) {
        this.Alum = Alum;
    }
    
    public void VeriRole()
    {
            
             List<UsuarioRol> lis = UsuarioRolFCDE.findByUsua(logiBean.getObjeUsua());
             for(UsuarioRol temp: lis)
             {                 
                 if(temp.getCodiRole().getCodiRole() == 7)
                 {
                     /*alumnoo */
                     this.Alum=true;
                 }
                 else if(temp.getCodiRole().getCodiRole() == 8)
                 {
                     /*trabajadora social*/
                     this.TrabaSoci=false;
                 }
             }
            
    }
     public boolean consIfCarnExis()
    {   
        boolean resp=false;
        try {
             
         
                String usuario = logiBean.getObjeUsua().getAcceUsua();
                 Pattern pat = Pattern.compile("^20.*");
                    Matcher mat = pat.matcher(usuario);
                    if (mat.matches()) {
                     resp= FCDEResp.ReadIfCarnExis(usuario);  
                      if(FCDESoli.findCarnet(usuario)!=null)
                        {

                            this.carnet = logiBean.getObjeUsua().getAcceUsua();
                            this.objeSoli =FCDESoli.findCarnet(carnet);
                            this.objeBeca = FCDEBeca.findSoli(objeSoli.getCodiSoliBeca());
                        }
                        else
                        {
                            resp=false;
                        }
                    } else {
                        System.out.println("NO");
                    }
                          
               
                    
                    
           
            
        } catch (Exception e) {
            
            System.out.println("Error en consIfCarnExis" +e.getMessage());
        }
        
        return resp;
    }
     
     
     /*empieza lo de documento*/
     
     
  
    private Documento objeDocu;
    private List<Documento> listDocuDocu;
    private boolean guardarDocumento;
    private boolean imagen;
    private String rutaC;
    private byte[] esto;
    private String tokens = "";

    /*para archivos xd*/
    
    private Part file;
    private String carnetDocumento;    
    List<String> rutas;
    int DireActuInde;    
    List<Archivo> listNombFile;
    

 

    
    public String getCarnetDocumento() {
        return carnetDocumento;
    }

    public void setCarnetDocumento(String carnet) {
        this.carnetDocumento = carnet;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public List<Archivo> getListNombFile() {
        return listNombFile;
    }
    public byte[] getEsto() {
        return esto;
    }

    public boolean isImagen() {
        return imagen;
    }

    public void setImagen(boolean imagen) {
        this.imagen = imagen;
    }

    public String getTokens() {
        return tokens;
    }
    
    
    
    public Documento getObjeDocu() {
        return objeDocu;
    }

    public void setObjeDocu(Documento objeDocu) {
        this.objeDocu = objeDocu;
    }

    public boolean isGuardarDocumento() {
        return guardarDocumento;
    }

    public List<Documento> getListDocuDocu() {
        return listDocuDocu;
    }

    

      private EmpresaBean objeEmpr;
    private boolean paBeca = false;
    private boolean paEmpresa= false;

    public boolean isPaBeca() {
        return paBeca;
    }

    public void setPaBeca(boolean paBeca) {
        this.paBeca = paBeca;
    }

    public boolean isPaEmpresa() {
        return paEmpresa;
    }

    public void setPaEmpresa(boolean paEmpresa) {
        this.paEmpresa = paEmpresa;
    }
    
    
   
    public void initDocu()
    {
        this.objeDocu = new Documento();
        this.guardarDocumento = true;       
        this.objeDocu.setFechDocu(new Date());       
        this.imagen = false;
        this.tokens = "Imagen";        
       this.listNombFile = new ArrayList<>();
       this.rutas = new ArrayList<>();
       FacesContext facsCtxt = FacesContext.getCurrentInstance();            
       String ruta = facsCtxt.getExternalContext().getInitParameter("docBecas.URL"); 
       rutas.add(ruta);
       DireActuInde = 0;
       this.carnetDocumento = "";
       
        
         this.consTodoDocu();
    }
    
    public void limpFormDocu()
    {
        this.objeDocu = new Documento();
        this.guardarDocumento = true;  
        this.showImag=false;
        this.objeDocu.setFechDocu(new Date());
    }
    
    
    public void guarDocu()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {            
            if(this.objeBeca != null)
            {
                 this.objeDocu.setCodiSoliBeca(this.objeSoli);
                  this.carnetDocumento = objeDocu.getCodiSoliBeca().getCarnAlum().trim();
            }
            if(this.objeEmpr != null)
            {
                this.objeDocu.setCodiEmpr(this.objeEmpr.getObjeEmpr());
                this.carnetDocumento  = objeDocu.getCodiEmpr().getNombEmpr().trim();
            }
            this.uploFile();
            this.objeDocu.setEstaDocu(1);   
            this.FCDEDocu.create(this.objeDocu);
            if(listDocuDocu ==null) {listDocuDocu= new ArrayList();}
            this.listDocuDocu.add(this.objeDocu);
            this.limpFormDocu();
            //this.carnet = objeDocu.getCodiSoliBeca().getCarnAlum();
            //this.uploFile();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            //log.info("Documento Consultado");
        }
        catch (Exception e) 
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            //log.error(getRootCause(e).getMessage());
        }
        finally
        {
            
        }
    }
    public void elimDocu()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {          
            this.objeDocu.setEstaDocu(0);
            String ruta = this.rutas.get(0) + this.objeDocu.getRutaDocu();
            
    		File file = new File(ruta);
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
            FCDEDocu.remove(this.objeDocu);
            this.listDocuDocu.remove(this.objeDocu); //Limpia el objeto viejo
            //this.listDocu.add(this.objeDocu); //Agrega el objeto modificado
             ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos eliminados')");    
            ctx.execute("$('#ModaDocuForm').modal('hide');");
            //log.info("Documento Modificado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
            //log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    public void consTodoDocu()
    {
        try
        {
            /*
            if(this.objeBeca != null)
            {
                System.out.println("Dato sobre solis en docu: "+this.objeBeca.getObjeSoli().getCarnAlum());
                 this.objeDocu.setCodiSoliBeca(this.objeBeca.getObjeSoli());
                  this.carnet = objeDocu.getCodiSoliBeca().getCarnAlum().trim();
                  System.out.println(this.carnet);
            }
            if(this.objeEmpr != null)
            {
                this.objeDocu.setCodiEmpr(this.objeEmpr.getObjeEmpr());
                this.carnet  = objeDocu.getCodiEmpr().getNombEmpr().trim();
                System.out.println(this.carnet);
            }*/
            this.listDocuDocu = FCDEDocu.findBySoli(this.objeSoli.getCodiSoliBeca());
           
           // log.info("Documentos Consultados");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Error en cons todo en docus: " +ex.getMessage());
           // log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void consDocu()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.imagen = false;
            this.tokens = "Imagen";
            this.objeDocu = FCDEDocu.find(codi);
            String h = this.rutas.get(0);
            //System.out.println(h + this.objeDocu.getRutaDocu());
            rutaC = h + this.objeDocu.getRutaDocu();
            //this.objeDocu.setRutaDocu(h + this.objeDocu.getRutaDocu());
            this.guardarDocumento = false;
            this.showDocu = false;
            this.showImag=false;
            String ruta = this.rutas.get(0) + this.objeDocu.getRutaDocu();
            File file = new File(ruta);
            String[] tokens = file.getName().split("\\.(?=[^\\.]+$)");
            if("pdf".equals(tokens[1]))
            {
                this.imagen = !this.imagen;
                this.tokens = "Documento";
                InputStream docu = new FileInputStream(file);
                this.esto = readFully(docu);
            }
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeDocu.getRutaDocu()) + "')");
            //log.info("Documento Consultado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
           // log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
        
    public void uploFile()
    {
        String Path = this.rutas.get(DireActuInde);
        try
        {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            System.out.println(request);
            for(Part item : request.getParts())
            {
                if(this.carnetDocumento.trim().length()==0)
                {
                   moveFilePart(item, Path);
                    
                }
                else
                {
                    String newPath= Path+this.carnet+"/";
                    File theDir = new File(newPath);

                    // if the directory does not exist, create it
                    if (!theDir.exists()) {
                        
                        boolean result = false;

                        try{
                            theDir.mkdir();
                            result = true;
                           
                        } 
                        catch(SecurityException se){
                            //handle it
                        }        
                        if(result) {    
                            moveFilePart(item, newPath);
                        }
                    }
                    else
                    {
                        
                        moveFilePart(item, newPath);
                    }
                }
               
            }
            
        }
        catch(Exception ex)
        {
            System.out.println("Error en uploFile"+ex.getMessage());
        }
    }
    public static byte[] readFully(InputStream input) throws IOException
    {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
    
    
    private void moveFilePart(Part item,String path) throws IOException
    {
        try {
            if(item.getName().equals(file.getName()))
            {
                 this.listNombFile.add(new Archivo(
                        item.getSubmittedFileName(),
                        item.getInputStream(),
                        item.getContentType(),
                        readFully(item.getInputStream())
                ));
            this.processFilePart(item, String.format("%s%s",path, item.getSubmittedFileName()));                
            if(this.objeBeca != null)
            {
                 this.objeDocu.setRutaDocu(this.objeDocu.getCodiSoliBeca().getCarnAlum() + "/" + item.getSubmittedFileName());
            }
            if(this.objeEmpr != null)
            {
              this.objeDocu.setRutaDocu(this.carnet + "/" + item.getSubmittedFileName());
            }
            
            
                
             }
        } catch (Exception e) {
            System.out.println("Error en moveFilePart "+e.getMessage());
        }
        
    }
    private void processFilePart(Part part, String filename) throws IOException
    {
        int DEFAULT_BUFFER_SIZE = 2048;
        InputStream input = null;
        OutputStream output = null;
        try
        {
            input = new BufferedInputStream(part.getInputStream(), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(new FileOutputStream(filename), DEFAULT_BUFFER_SIZE);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            for (int length = 0; ((length = input.read(buffer)) > 0);)
            {
                output.write(buffer, 0, length);
            }
            
        }
        finally
        {
            if (output != null)
                try
                {
                    output.close();
                }
                catch (IOException logOrIgnore)
                {
                }
            if (input != null)
                try
                {
                    input.close();
                }
                catch (IOException logOrIgnore)
                {
                }
        }
        part.delete();
    }
    //Lógica slider para docuemntos
    private  boolean showDocu = false;

    public boolean isShowDocu() {
        return showDocu;
    }
    public void toogDocu()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.showDocu = !this.showDocu;
    }
     //Lógica slider para imagenes
    private  boolean showImag = false;

    public boolean isShowImag() {
        return showImag;
    }
    public void toogImag()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        
        this.showImag = !this.showImag;
        System.out.println(this.showImag);
        
    }

     
     
     /*termina todo lo de documento*/
     
     
     
    /*-----------------------------------------------------------*/
    //Aquí termina toda la logia del formulario dinamico  
    /*-----------------------------------------------------------*/
    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
           
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
}
