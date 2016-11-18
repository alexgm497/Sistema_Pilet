/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.BecaFacadeLocal;
import com.sv.udb.ejb.DetalleBecaFacadeLocal;
import com.sv.udb.ejb.SolicitudBecaFacadeLocal;
import com.sv.udb.ejb.TipoBecaFacadeLocal;
import com.sv.udb.modelo.Beca;
import com.sv.udb.modelo.Grado;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.modelo.TipoBeca;
import com.sv.udb.modelo.TipoRetiro;
import com.sv.udb.modelo.TipoEstado;
import com.sv.udb.utils.AlumnosPojo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ApplicationException;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eduardo
 */
@Named(value = "becaSoliBean")
@ViewScoped
public class BecaSoliBean implements Serializable {

    @EJB
    private TipoBecaFacadeLocal FCDETipoBeca;

    @EJB
    private DetalleBecaFacadeLocal FCDEDetaBeca;
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
    private String filt; //Filotro de búsqueda

    
    
    @EJB
    private BecaFacadeLocal FCDEBeca;
    private Beca objeBeca;
    private Beca objeBeca2;
    private List<Beca> listBeca;
    private List<Beca> listBecaH;
    
    
    private boolean guardar; 
    private static Logger log = Logger.getLogger(BecaSoliBean.class);

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
    
    

    public List<SolicitudBeca> getListSoli() {
        return listSoli;
    }

    public void setListSoli(List<SolicitudBeca> listSoli) {
        this.listSoli = listSoli;
    }
    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

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
        return filt;
    }

    public void setFilt(String filt) {
        this.filt = filt;
    }
    
    /**
     * Creates a new instance of BecaSoliBean
     */
    public BecaSoliBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeSoli = new SolicitudBeca();
        this.guardar = true;
        this.objeBeca = new Beca();
        this.consTodo();
        this.consTodoH();
        if (this.objeSoli.getCodiSoliBeca() != null) {
            DetalleBecaBean asd = (DetalleBecaBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("detalleBecaBean");
            /*asd.setObjeCombPadr(objeSoli);
            asd.onAlumBecaSelect();*/
        }
        
    }
    
    public void limpForm()
    {
        this.objeBeca = new Beca();  
        this.objeSoli = new SolicitudBeca();
        this.objeSoli.setFechSoliBeca(new Date());
        this.objeBeca.setFechBaja(new Date());
        this.guardar = true; 
        this.falso();
        this.filt = "";
    }
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(objeSoli.getCarnAlum() == null || objeSoli.getNombAlum() == null)
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Busque un alumno')");
            }
            else
            {
                    TipoEstado a = new TipoEstado();
                    a.setCodiTipoEsta(1);
                    this.objeSoli.setEstaSoliBeca(0);
                    FCDESoli.create(objeSoli);
                    this.objeSoli2 = FCDESoli.findLast();
                    System.out.println("AQUIIII: "+this.objeSoli2);
                    this.objeBeca.setCodiSoliBeca(objeSoli2);
                    this.objeBeca.setCodiTipoEsta(a);
                    this.objeBeca.setFechInic(objeBeca.getFechBaja());
                    this.objeBeca.setFechBaja(null);
                    System.out.println(objeBeca);
                    this.FCDEBeca.create(objeBeca);
                    this.listSoli.add(this.objeSoli);
                   if(this.listBeca == null)
                    {
                        this.listBeca = new ArrayList<>();
                    }
                    System.out.println(this.objeBeca);
                    this.listBeca.add(objeBeca);
                    this.limpForm();
                    ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
                    log.info("Beca Guardada");
            
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            log.error(getRootCause(ex).getMessage());
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
            //this.listSoli.remove(this.objeSoli); //Limpia el objeto viejo
            System.out.println(this.objeSoli);
            System.out.println(this.objeSoli.getNombAlum());
            objeSoli2 = FCDESoli.find(this.objeSoli.getCodiSoliBeca());
            System.out.println(this.objeSoli2.getNombAlum());
            this.objeBeca = FCDEBeca.findSoli(this.objeSoli.getCodiSoliBeca());
            this.objeBeca2 = FCDEBeca.findSoli(this.objeSoli2.getCodiSoliBeca());
            this.listSoli.remove(this.objeSoli);
            this.objeSoli2.setEstaSoliBeca(3);
            objeSoli.setBecaList(null);
            FCDESoli.edit(objeSoli2);
            this.listSoli.add(objeSoli2);
            this.objeSoli.setEstaSoliBeca(1);
            FCDESoli.create(objeSoli);
            this.listSoli.add(objeSoli);
            this.listBeca.remove(this.objeBeca2);
            TipoEstado es = new TipoEstado();
            es.setCodiTipoEsta(3);
            this.objeBeca2.setCodiTipoEsta(es);
            this.objeBeca2.setFechBaja(new Date());
            FCDEBeca.edit(objeBeca2);
            //this.listBeca.add(objeBeca2);
            this.objeSoli = FCDESoli.findLast();
            System.out.println(this.objeSoli.getCodiSoliBeca());
            this.objeBeca.setCodiSoliBeca(objeSoli);
            FCDEBeca.create(objeBeca);
            this.listBeca.add(objeBeca);
            //FCDESoli.edit(this.objeSoli);
            //this.listSoli.add(this.objeSoli); //Agrega el objeto modificado
            this.consTodoH();
            this.consTodo();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info("Solicitud Modificada");
            
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            log.error(getRootCause(ex).getMessage());
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
            FCDESoli.edit(objeSoli);              //edita
            this.objeBeca2 = this.objeBeca;
            TipoEstado esta = new TipoEstado();
            esta.setCodiTipoEsta(3);
            this.objeBeca.setCodiTipoEsta(esta);
            FCDEBeca.edit(objeBeca);                          
            this.objeSoli2.setEstaSoliBeca(1);            
            TipoRetiro reti = new TipoRetiro();
            reti.setCodiReti(null);
            this.objeBeca2.setCodiReti(null);
            this.objeBeca2.setRetiBeca(null);
            esta.setCodiTipoEsta(1);
            this.objeBeca2.setCodiTipoEsta(esta);
            objeSoli2.setBecaList(null);
            FCDESoli.create(objeSoli2);
            this.objeSoli2 = FCDESoli.findLast();
            this.objeBeca2.setCodiSoliBeca(objeSoli2);
            this.objeBeca2.setFechBaja(null);
            FCDEBeca.create(objeBeca2);     
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Beca Reactivada')");
            log.info("Beca reactivada");
            this.consTodo();
            this.consTodoH();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            log.error(getRootCause(ex).getMessage());
            System.out.println("AQUI "+ex);
        }
        finally
        {
            
        }
    }
    
    public void desa()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listBeca.remove(this.objeBeca); //Limpia el objeto viejo
            TipoEstado esta = new TipoEstado();
            esta.setCodiTipoEsta(2);
            this.objeBeca.setCodiTipoEsta(esta);
            this.objeBeca.setFechBaja(new Date());
            this.objeSoli.setEstaSoliBeca(2);
            FCDEBeca.edit(this.objeBeca);
            FCDESoli.edit(objeSoli);
            this.listBeca.add(this.objeBeca); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Beca Reactivada')");
            log.info("Beca reactivada");
            this.consTodo();
            this.showBusc = false;
        }
        catch(Exception ex)
        {
                        ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            log.error(getRootCause(ex).getMessage());
            System.out.println("AQUI "+ex);
            System.out.println("AQUI "+ex.getMessage());
            System.out.println("AQUI "+ex.getCause());
            System.out.println("AQUI "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void falso()
    {
        showBusc = false;
        empresa = false;
        beca = false;
        elim = false;
        estado = false;
        detalle = false;
        grado = false;
        historia = false;
    }
    
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.showBusc = false;
            this.objeSoli = FCDESoli.find(codi);
            this.objeBeca = FCDEBeca.findSoli(objeSoli.getCodiSoliBeca());
            this.guardar = false;
            this.falso();
            this.filt = objeSoli.getCarnAlum();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeBeca.getCodiSoliBeca().getNombAlum()) + "')");
            log.info("Beca Consultada");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    public boolean cons(SolicitudBeca obje)
    {
        boolean variable=false;
        try
        {
           SolicitudBeca s = new SolicitudBeca();
            s = FCDESoli.find(obje);
            if(s!=null)
            {variable=true;}
           
        }
        catch(Exception ex)
        {
            
        }
        return variable;
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
            this.listSoli = FCDESoli.findAll();
            this.listBeca = FCDEBeca.findAllH();
            log.info("Beca Consultadas");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void consTodoH()
    {
        try
        {
            this.listSoliH = FCDESoli.findAll();
            this.listBecaH = FCDEBeca.findAllHisto();
            log.info("Beca Consultadas");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    public void consW()
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        Client client = ClientBuilder.newClient();
        String url = String.format("http://www.opensv.tk:8080/WebService/MiServicio/consAlum/%s", this.filt.trim());
        
       if(this.cons(filt.trim()))
       {
           
         ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El Alumno ya se encuentra')");
       }
       else
       {
           WebTarget resource = client.target(url);
            Builder request = resource.request();
            request.accept(MediaType.APPLICATION_JSON);
            Response response = request.get();
            if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL)
            {
                AlumnosPojo resp = response.readEntity(AlumnosPojo.class); //La respuesta de captura en un pojo que esta en el paquete utils
                this.objeSoli.setCarnAlum(filt.trim());
                this.objeSoli.setNombAlum(resp.getNomb());
                System.out.println(this.objeSoli.getNombAlum());
                Grado grad = new Grado();
                String cortado= resp.getGrad().substring(0,1);
                grad.setCodiGrad(Integer.parseInt(cortado));
                this.objeSoli.setCodiGrad(grad);
                System.out.println(grad.getCodiGrad());
                if(resp.getNomb() == null || resp.getNomb().equals(""))
                {
                    ctx.execute("setMessage('MESS_WARN', 'Atención', 'Alumno no encontrado.')");
                }
            }
            else
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar alumno')");
            }
       
       }
                
                
                
        
    }
    
    //Lógica slider
    private boolean showBusc = false;
    private boolean empresa = false;
    private boolean beca = false;
    private boolean elim = false;
    private boolean estado = false;
    private boolean detalle = false;
    private boolean grado = false;
    private boolean historia = false;

    public boolean isGrado() {
        return grado;
    }

    public boolean isHistoria() {
        return historia;
    }
    
    public boolean isDetalle() {
        return detalle;
    }
    
    public boolean isEstado() {
        return estado;
    }
    

    public boolean isBeca() {
        return beca;
    }

    public boolean isElim() {
        return elim;
    }

    public boolean isEmpresa() {
        return empresa;
    }
    
    public boolean isShowBusc() {
        return showBusc;
    }
    
    public void toogBusc()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.showBusc = !this.showBusc;
    }
    
    public void empr()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        EmpresaBean asd = (EmpresaBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("empresaBean");
        asd.limpForm();
        this.empresa = !this.empresa;
        this.beca = !this.beca;
    }
    
    public void esta()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.estado = !this.estado;
        this.beca = !this.beca;
    }
    
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.elim = !this.elim;
        this.beca = !this.beca;
    }
    
    public void beca()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.beca = !this.beca;
    }
    
    public void deta()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.detalle = !this.detalle;
        this.beca = !this.beca;
        listTipoBeca = FCDETipoBeca.findTipos(this.objeSoli.getCodiGrad().getNivelGrad());
    }
    
    public void grad()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.grado = !this.grado;
        this.beca = !this.beca;
    }
    
    public void hist()
    {
        System.out.println("historial"+this.historia);
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.historia = !this.historia;
        System.out.println("Hostiral: "+this.historia);
    }

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
