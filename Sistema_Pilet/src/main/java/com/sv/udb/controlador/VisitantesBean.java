package com.sv.udb.controlador;
import com.sv.udb.ejb.VisitanteFacadeLocal;
import com.sv.udb.modelo.Visitante;
import com.sv.udb.utils.LOG4J;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.apache.log4j.Logger;
//LIBRERIAS PARA LEER EXCEL
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.util.Iterator;  
import javax.faces.application.FacesMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
  
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.ss.usermodel.Sheet;  
import org.apache.poi.ss.usermodel.Workbook;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  
import org.primefaces.model.UploadedFile;

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
    private List<Visitante> listVisiExce;
    private boolean guardar;
    
    private String cadeText="";
    private Part file;
    private LOG4J<VisitantesBean> lgs = new LOG4J<VisitantesBean>(VisitantesBean.class) {
    };
    private Logger log = lgs.getLog();
    
    public String getCadeText() {
        return cadeText;
    }

    public void setCadeText(String cadeText) {
        this.cadeText = cadeText;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public List<Visitante> getListVisiExce() {
        return listVisiExce;
    }
    
    private boolean valiDocuExce(){
        boolean resp = false;
        
        return resp;
    }
    
    public void impoListExce(int guarListVisiCita)
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        if(this.listVisiExce==null || this.listVisiExce.isEmpty() || this.listVisiExce.size()==0)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'No hay nada que importar')");
        }
        else
        {
            int contGuar = 0;
            for(Visitante objeVisiTemp: this.listVisiExce)
            {
                if(FCDEVisi.findByDuiVisi(objeVisiTemp.getDuiVisi())==null)
                {
                    FCDEVisi.create(objeVisiTemp);
                    contGuar++;
                    this.listVisi.add(objeVisi);
                }
            }
            this.listVisiExce=new ArrayList<Visitante>();
            if(contGuar==0) ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Ningún Visitante Guardado')");
            if(contGuar == 1) ctx.execute("setMessage('MESS_SUCC', 'Atención', '1 Visitante Guardado')");
            if(contGuar > 1) ctx.execute("setMessage('MESS_SUCC', 'Atención', '"+contGuar+" Visitantes Guardados')");
        }
    }
    
    public void setListVisiExce(){
        RequestContext ctx = RequestContext.getCurrentInstance();
        Visitante objeVisiTempExce = new Visitante();
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            for(Part item : request.getParts())
            {
                if(item.getName().equals(file.getName()))
                {
                    //FileInputStream inputStream = new FileInputStream(file);
                    String ext = file.getSubmittedFileName().trim();
                    ext = ext.substring(ext.length()-4,ext.length());
                    if(ext.equals("xlsx")){
                        Workbook workbook = new XSSFWorkbook(file.getInputStream());
                        Sheet sheet = workbook.getSheetAt(0);  
                        Iterator<Row> iterator = sheet.iterator(); 
                        int colums = sheet.getRow(0).getPhysicalNumberOfCells();
                        if(sheet.getWorkbook().getNumberOfSheets() == 1){
                            if(sheet.getPhysicalNumberOfRows() != 0){
                                if(colums == 5){
                                    while (iterator.hasNext()) {  
                                        Row nextRow = iterator.next();
                                        Iterator<Cell> cellIterator = nextRow.cellIterator();
                                        while (cellIterator.hasNext()) {  
                                            Cell cell = cellIterator.next();  
                                            int columnIndex=cell.getColumnIndex();  
                                            switch(columnIndex){
                                                case 0://nombre
                                                    objeVisiTempExce.setNombVisi(cell.getStringCellValue());
                                                break;
                                                case 1://apellido
                                                    objeVisiTempExce.setApelVisi(cell.getStringCellValue());
                                                break;
                                                case 2://dui
                                                    objeVisiTempExce.setDuiVisi(cell.getStringCellValue());
                                                break;
                                                case 3://telefono
                                                    objeVisiTempExce.setTeleVisi(cell.getStringCellValue());
                                                break;
                                                case 4://correo
                                                    objeVisiTempExce.setCorrVisi(cell.getStringCellValue());
                                                    objeVisiTempExce.setTipoVisi(2);
                                                    objeVisiTempExce.setEstaVisi(1);
                                                    if(listVisiExce == null)listVisiExce = new ArrayList<Visitante>();
                                                    listVisiExce.add(objeVisiTempExce);
                                                break;

                                            }
                                        }
                                        objeVisiTempExce = new Visitante();
                                    }
                                    ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Documento Cargado con Éxito')");
                                }else{
                                    //validar 5 columnas
                                     FacesContext.getCurrentInstance().addMessage("FormImpoExce:file", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Formato del archivo incorrecto",  null));
                                }
                            }else{
                                //validar que la pagina no este vacía
                                FacesContext.getCurrentInstance().addMessage("FormImpoExce:file", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La primera página de este archivo está vacía",  null));
                            }
                        }else{
                            //validar una pagina
                            FacesContext.getCurrentInstance().addMessage("FormImpoExce:file", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Solo se puede importar la primera hoja del archivo",  null));
                        }
                        // inputStream.close(); 
                        workbook.close();
                    }else{
                        //validar que sea xlsx
                        if(ext.equals(".xls")){
                            FacesContext.getCurrentInstance().addMessage("FormImpoExce:file", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Versión antigua no soportada, se requiere archivo .xlsx",  null));
                        }else{
                            FacesContext.getCurrentInstance().addMessage("FormImpoExce:file", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo de documento incorrecto",  null));
                        }
                        
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(VisitantesBean.class.getName()).log(Level.SEVERE, null, ex);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar Documento')");
        } catch (IOException ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(VisitantesBean.class.getName()).log(Level.SEVERE, null, ex);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar Documento')");
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(VisitantesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
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
        
        listVisiExce = new ArrayList<Visitante>();
        this.limpForm();
        
    }
 //Limpiando el formulario   
    public void limpForm()
    {
        this.objeVisi = new Visitante();
        listVisiExce=new ArrayList<Visitante>();
        this.guardar = true;   
        
    }
    
    public void dropVisiDocuExce(Visitante obje){
        if(listVisiExce == null) listVisiExce = new ArrayList<Visitante>();
        if(listVisiExce.contains(obje)){
            listVisiExce.remove(obje);
            RequestContext ctx = RequestContext.getCurrentInstance();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Visitante Eliminado la lista')");
        }
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
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+"Se ha consultado un visitante con codigo: " + objeVisi.getCodiVisi());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Registro Consultado')");
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+"Error al consultar registro con codigo: " + objeVisi.getCodiVisi());
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
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+"Se ha agregado un visitante con codigo: " + objeVisi.getCodiVisi());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            if(listVisi == null)listVisi = new ArrayList<Visitante>();
            this.listVisi.add(this.objeVisi);
            limpForm();
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+"Error al agregar registro");
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
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+"Se ha modificado un visitante con codigo: " + objeVisi.getCodiVisi());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+"Error al modificar registro con codigo: " + objeVisi.getCodiVisi());
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
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+"Se ha eliminado un visitante con codigo: " + objeVisi.getCodiVisi());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
            this.limpForm();
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Visitantes"+"-"+"Error al eliminar registro con codigo: " + objeVisi.getCodiVisi());
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }
    
    
}
