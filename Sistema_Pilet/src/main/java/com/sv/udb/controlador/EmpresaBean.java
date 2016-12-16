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
import com.sv.udb.modelo.Empresa;
import com.sv.udb.ejb.EmpresaFacadeLocal;
import com.sv.udb.ejb.GradoFacadeLocal;
import com.sv.udb.ejb.SeguimientoFacadeLocal;
import com.sv.udb.ejb.SolicitudBecaFacadeLocal;
import com.sv.udb.ejb.TipoBecaFacadeLocal;
import com.sv.udb.modelo.Beca;
import com.sv.udb.modelo.DetalleBeca;
import com.sv.udb.modelo.Documento;
import com.sv.udb.modelo.Seguimiento;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.modelo.TipoBeca;
import com.sv.udb.utils.Archivo;
import com.sv.udb.utils.Conexion;
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
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author ferna
 */
@Named(value = "empresaBean")
@ViewScoped
public class EmpresaBean implements Serializable{
    @EJB
    private EmpresaFacadeLocal FCDEEmpr;
    private Empresa objeEmpr;
    private List<Empresa> listEmpr;
    private boolean guardar; 
    private static final long serialVersionUID = 1L;
    @Inject
    private GlobalAppBean globalAppBean; //Bean de aplicación (Instancia)
    private byte[] docuRepo;
    private static Logger log = Logger.getLogger(EmpresaBean.class);
    public Empresa getObjeEmpr() {
        return objeEmpr;
    }

    public byte[] getDocuRepo() {
        return docuRepo;
    }
    
    public void setObjeEmpr(Empresa objeEmpr) {
        this.objeEmpr = objeEmpr;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public List<Empresa> getListEmpr() {
        return listEmpr;
    }
    
    /**
     * Creates a new instance of EmpresaBean
     */
    public EmpresaBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeEmpr = new Empresa();        
        this.guardar = true;
        this.consTodo();
        this.initDocu();
    }
    
    public void limpForm()
    {
        this.objeEmpr = new Empresa();
        this.objeEmpr.setFechEmpr(new Date());
        this.guardar = true;        
    }
    
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeEmpr.setEstaEmpr(1);
            this.objeEmpr.setMontEmpr(BigDecimal.ZERO);
            FCDEEmpr.create(this.objeEmpr);
            if(this.listEmpr == null)
            {
                this.listEmpr = new ArrayList<>();
            }
            this.listEmpr.add(this.objeEmpr);
            this.guardar = false;
            this.showEmpr = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            //log.info("Empresa Guardada");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            //log.error(getRootCause(ex).getMessage());
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
            this.listEmpr.remove(this.objeEmpr); //Limpia el objeto viejo
            FCDEEmpr.edit(this.objeEmpr);
            this.listEmpr.add(this.objeEmpr); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            //log.info("Empresa Modificada");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            //log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void elim()
    {
        System.out.println("Eliminando");
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listEmpr.remove(this.objeEmpr); //Limpia el objeto viejo
            this.objeEmpr.setEstaEmpr(0);
            FCDEEmpr.edit(this.objeEmpr);
            //this.listEmpr.add(this.objeEmpr); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            //log.info("Empresa Eliminada");
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
    
    public void consTodo()
    {
        try
        {
            this.listEmpr = FCDEEmpr.findAll();
           // log.info("Empresas Consultadas");
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
    
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeEmpr = FCDEEmpr.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeEmpr.getNombEmpr()) + "')");
            //log.info("Empresa Consultada");
             this.consTodoDocu();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
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
            String pathRepo = globalAppBean.getResourcePath("Reportes_Becas/EmpresasDonacionesActivas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }
    
    private boolean showEmpr = false;
    public boolean isShowEmpr() {
        return showEmpr;
    }
    public void toogEmpr()
    {
        this.showEmpr = !this.showEmpr;
        this.limpForm();
    }
    
    public void toogRegr()
    {
         RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.showEmpr = false;
        this.guardar = true;
        this.limpForm();
         //Recarga
            ctx.execute("window.location.reload(true);" );
    }
     /*empieza lo de documento*/
     
     
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
            if(this.objeEmpr != null)
            {
                this.objeDocu.setCodiEmpr(this.objeEmpr);
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
            limpFormDocu();
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
            

            System.out.println("Codigo emp`resa: "+this.objeEmpr.getCodiEmpr());
            this.listDocuDocu = FCDEDocu.findByEmpr(this.objeEmpr.getCodiEmpr());
           
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
                    String newPath= Path+this.carnetDocumento+"/";
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
              this.objeDocu.setRutaDocu(this.carnetDocumento + "/" + item.getSubmittedFileName());
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

     
}