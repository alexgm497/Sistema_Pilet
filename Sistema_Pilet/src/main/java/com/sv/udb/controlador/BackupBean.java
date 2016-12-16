/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.utils.LOG4J;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Carlos
 */
@Named(value = "backupBean")
@ViewScoped
@ManagedBean
public class BackupBean implements Serializable{
    @Inject
    private LoginBean logiBean; //Bean de session
    
    private LOG4J<BackupBean> lgs = new LOG4J<BackupBean>(BackupBean.class) {
    };
    private Logger log = lgs.getLog();
    /**
     * Creates a new instance of BackupBean
     */
    public BackupBean() {
    }
    
    /**
    * Método que se ejecuta después de haber construido la clase e inicializa las variables
    */
    @PostConstruct
    public void init()
    {
        
    }
    
    public void creaBackUp()
    {
        try 
        {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String dumpExePath = "C:\\wamp\\bin\\mysql\\mysql5.5.17\\bin\\mysqldump.exe";
            //"C:\\wamp\\bin\\mysql\\mysql5.5.16\\bin\\mysqldump.exe"
            String host = "185.144.156.103";
            String port = "3306";
            String user = "ricaldone";
            String password="@dmin123";
            String database = "sistemas_pilet";
            String basePath = ctx.getRealPath("/");
            //String backupPath = basePath + "BackupsMySQLdb//";
            String backupPath = "C:\\wampp\\bin\\mysql\\";
            Process p = null;
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String filepath = database + "-" + host + "-(" + dateFormat.format(date) + ").sql";
            String batchCommand = "";
            if (password != "") {
                batchCommand = dumpExePath + " -h " + host + " --port " + port + " -u " + user + " --password=" + password + " --add-drop-database -B " + database + " -r \"" + backupPath + "" + filepath + "\"";
            } else {
                batchCommand = dumpExePath + " -h " + host + " --port " + port + " -u " + user + " --add-drop-database -B " + database + " -r \"" + backupPath + "" + filepath + "\"";
            }
            Runtime runtime = Runtime.getRuntime();
            p = runtime.exec(batchCommand);
            int processComplete = p.waitFor();
            if (processComplete == 0) {
                log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"Backup"+"-"+"Backup created successfully for with DB " + database + " in " + host + ":" + port);
            } else {
                log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"Backup"+"-"+"Could not create the backup for with DB " + database + " in " + host + ":" + port);
            }
 
        } catch (IOException ioe) {
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"Backup"+"-"+"ERROR AL CREAR BACKUP");
        } catch (Exception e) {
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"Backup"+"-"+"ERROR AL CREAR BACKUP");
        }
    }
    /**
     * Datos extras solo de uso
     */
    private String lblMessage;
    ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    String basePath = ctx.getRealPath("/");
    // private String txtPath = "G:\\New Folder\\";
    private String txtPath = basePath + "BackupRestoreMysqldb//";
    
    /**
     * Aquí se sube el archivo, esto sirve para seleccionar la ruta
     * @param event 
     */

    public void upload(FileUploadEvent event) {




        if (event.getFile().getFileName().equals("")) {
            lblMessage = "Seleccione el archivo a recuperar";
            System.out.println(lblMessage);
        } else {
            //restoreDB("root", "root", "G:\\New Folder\\" + event.getFile().getFileName());
            RecuperarBackUp("root", "root", txtPath + event.getFile().getFileName());
            System.out.println("Algo va mal" + event.getFile().getFileName());
        }
    }
    
    /**
     * Aqui es com funciona todo
     * @param Usuario
     * @param Password
     * @param recurso
     * @return 
     */
    
    public boolean RecuperarBackUp(String Usuario, String Password, String recurso) {
        // Se ingresa la ruta donde se restaurará el archivo, con sus parametros
        //  String[] executeCmd = new String[]{"C:\\Program Files\\MySQL\\MySQL Server 5.0\\bin\\mysql ", "--user=" + dbUserName, "--password=" + dbPassword, "-e", "source " + source};
        String[] executeCmd = new String[]{"C:\\wampp\\bin\\mysql\\ ", "--user=" +Usuario, "--password=" + Password, "-e", "source " + recurso};

        Process runtimeProcess;
        try {
            //Se realiza la acción
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            //Verificamos que funcione con un waitfor, que sería la espera de resultado
            if (processComplete == 0) {
                log.info(logiBean.getObjeUsua().getCodiUsua()+"--Todo funciono");
                lblMessage = "Todo funciono";
                System.out.println(lblMessage);
                return true;
            } else {
                log.info(logiBean.getObjeUsua().getCodiUsua()+"--Nada funciono");
                lblMessage = "Que regada";
                System.out.println(lblMessage);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
