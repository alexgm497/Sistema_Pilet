/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.modelo.Documento;
import com.sv.udb.ejb.DocumentoFacadeLocal;
import com.sv.udb.ejb.SolicitudBecaFacadeLocal;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.utils.Archivo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ferna
 */
@Named(value = "documentoBean")
@ViewScoped
public class DocumentoBean implements Serializable{
    
    @EJB
    private DocumentoFacadeLocal FCDEDocu;
    private Documento objeDocu;
    private List<Documento> listDocu;
    private boolean guardar;
    private boolean imagen;
    private static Logger log = Logger.getLogger(DocumentoBean.class);
    private String rutaC;
    private byte[] esto;
    private String tokens = "";

    /*para archivos xd*/
    
    private Part file;
    private String carnet;    
    List<String> rutas;
    int DireActuInde;    
    List<Archivo> listNombFile;
    
    
     private List<SolicitudBeca> listSoli;

    public List<SolicitudBeca> getListSoli() {
        return listSoli;
    }

    public void setListSoli(List<SolicitudBeca> listSoli) {
        this.listSoli = listSoli;
    }
    @EJB
    private SolicitudBecaFacadeLocal FCDESoli;
    
    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
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

    public boolean isGuardar() {
        return guardar;
    }

    public List<Documento> getListDocu() {
        return listDocu;
    }

    /**
     * Creates a new instance of DocumentoBean
     */
    public DocumentoBean() {
    }
    
   private BecasBean objeBeca;
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
    
    
    @PostConstruct
    public void init()
    {
        this.objeDocu = new Documento();
        this.guardar = true;       
        this.objeDocu.setFechDocu(new Date());       
        this.imagen = false;
        this.tokens = "Imagen";        
       this.listNombFile = new ArrayList<>();
       this.rutas = new ArrayList<>();
       FacesContext facsCtxt = FacesContext.getCurrentInstance();            
       String ruta = facsCtxt.getExternalContext().getInitParameter("docBecas.URL"); 
       rutas.add(ruta);
       DireActuInde = 0;
       this.carnet = "";
       
       if (FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean") != null) {
            objeBeca = (BecasBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean");
            this.paBeca = true;
            System.out.println("Dato en documentos: "+objeBeca.getObjeSoli().getCarnAlum());
        }
        if (FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("empresaBean") != null) {
            objeEmpr = (EmpresaBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("empresaBean");
            this.paEmpresa= false;
            System.out.println("Dato en documentos: "+objeEmpr.getObjeEmpr().getNombEmpr());
       
        }    
         this.consTodo();
    }
    
    public void limpForm()
    {
        this.objeDocu = new Documento();
        this.guardar = true;  
        this.showImag=false;
        this.objeDocu.setFechDocu(new Date());
    }
    
    
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {            
            if(this.objeBeca != null)
            {
                 this.objeDocu.setCodiSoliBeca(this.objeBeca.getObjeSoli());
                  this.carnet = objeDocu.getCodiSoliBeca().getCarnAlum().trim();
            }
            if(this.objeEmpr != null)
            {
                this.objeDocu.setCodiEmpr(this.objeEmpr.getObjeEmpr());
                this.carnet  = objeDocu.getCodiEmpr().getNombEmpr().trim();
            }
            this.uploFile();
            this.objeDocu.setEstaDocu(1);   
            this.FCDEDocu.create(this.objeDocu);
            this.listDocu.add(this.objeDocu);
            this.limpForm();
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
    public void elim()
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
            this.listDocu.remove(this.objeDocu); //Limpia el objeto viejo
            //this.listDocu.add(this.objeDocu); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
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
    public void consTodo()
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
            this.listDocu = FCDEDocu.findAll();
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
    
    public void cons()
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
            this.guardar = false;
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
                if(this.carnet.trim().length()==0)
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
}
