/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import com.sv.udb.utils.Conexion;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JasperRunManager;
import org.primefaces.context.RequestContext;

import com.sv.udb.ejb.CambiocitaFacadeLocal;
import com.sv.udb.modelo.Cambiocita;

/**
 *
 * @author Mauricio
 */
@Named(value = "reportesBean")
@ViewScoped
public class ReportesBean implements Serializable{
    private static final long serialVersionUID = 1L;
    @Inject
    private GlobalAppBean globalAppBean; //Bean de aplicación (Instancia)
    private byte[] docuRepo; 
    private String anioCitas="";
    private String anioVisi="";
    /**
     * Creates a new instance of ReportesBean
     */
    public ReportesBean() {
    }

    public String getAnioVisi() {
        return anioVisi;
    }

    public void setAnioVisi(String anioVisi) {
        this.anioVisi = anioVisi;
    }

    public String getanioCitas() {
        return anioCitas;
    }

    public void setanioCitas(String anioCitas) {
        this.anioCitas = anioCitas;
    }

    public byte[] getDocuRepo() {
        return docuRepo;
    }
    
   
    
    //Procesar reporte
    public void procCita()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("anioCitas", anioCitas); //Para este ejemplo no es necesario
            System.out.println(anioCitas);
            String pathRepo = globalAppBean.getResourcePath("reportes_citas/EstadisticasCitas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");            
        }
    }
    
    public void procVisi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("anioVisi", anioVisi); //Para este ejemplo no es necesario
            System.out.println(anioVisi);
            String pathRepo = globalAppBean.getResourcePath("reportes_citas/EstadisticaVisitas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");            
        }
    }
}
