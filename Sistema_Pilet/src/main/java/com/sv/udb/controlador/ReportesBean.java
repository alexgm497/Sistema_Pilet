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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.ManagedBean;

/**
 *
 * @author Mauricio
 */
@Named(value = "reportesBean")
@ViewScoped
public class ReportesBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private GlobalAppBean globalAppBean; //Bean de aplicación (Instancia)
    private byte[] docuRepo;
    //Estadistica de citas
    private Date fechNuevCita;
    private Date fechFinCita;
    //Estadistica de visitas
    private Date fechNuevVisi;
    private Date fechFinVisi;

    /**
     * Creates a new instance of ReportesBean
     */
    public ReportesBean() {
    }

    public byte[] getDocuRepo() {
        return docuRepo;
    }

    public Date getFechNuevCita() {
        if(this.fechNuevCita == null)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -15);
            this.fechNuevCita = c.getTime();
        }
        return fechNuevCita;
    }

    public void setFechNuevCita(Date fechNuevCita) {
        this.fechNuevCita = fechNuevCita;
    }

    public Date getFechFinCita() {
        if(this.fechFinCita == null)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 15);
            this.fechFinCita = c.getTime();
        }
        return fechFinCita;
    }

    public void setFechFinCita(Date fechFinCita) {
        this.fechFinCita = fechFinCita;
    }

    public Date getFechNuevVisi() {
        if(this.fechNuevVisi == null)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -15);
            this.fechNuevVisi = c.getTime();
        }
        return fechNuevVisi;
    }

    public void setFechNuevVisi(Date fechNuevVisi) {
        this.fechNuevVisi = fechNuevVisi;
    }

    public Date getFechFinVisi() {
        if(this.fechFinVisi == null)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 15);
            this.fechFinVisi = c.getTime();
        }
        return fechFinVisi;
    }

    public void setFechFinVisi(Date fechFinVisi) {
        this.fechFinVisi = fechFinVisi;
    }

    public void procCita() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            System.out.println(fechNuevCita + " - " + fechFinCita);
            params.put("fech_inic_cita_nuev", df.format(fechNuevCita));
            params.put("fech_fin_cita_nuev", df.format(fechFinCita)); 
            String pathRepo = globalAppBean.getResourcePath("reportes_citas/EstadisticasCitas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }
    
    public void procVisi() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            System.out.println(fechNuevVisi + " - " + fechFinVisi);
            params.put("fech_inic_cita_nuev", df.format(fechNuevVisi));
            params.put("fech_fin_cita_nuev", df.format(fechFinVisi));
            String pathRepo = globalAppBean.getResourcePath("reportes_citas/EstadisticaVisitas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }

    public void procSegu(int codiCita) {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros

            params.put("codi_cita", codiCita); //Para este ejemplo no es necesario
            System.out.println(codiCita);
            String pathRepo = globalAppBean.getResourcePath("reportes_citas/SeguimientoCita.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }

    public void procAsis(int codiCita) {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros

            params.put("codi_cita", codiCita); //Para este ejemplo no es necesario
            System.out.println(codiCita);
            String pathRepo = globalAppBean.getResourcePath("reportes_citas/AsistenciaCitas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }
    
    public void procAsisVisi(int codiVisi) {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros

            params.put("codi_visi", codiVisi); //Para este ejemplo no es necesario
            System.out.println(codiVisi);
            String pathRepo = globalAppBean.getResourcePath("reportes_citas/VisitanteAsistencia.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }
}
