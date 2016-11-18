/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.UsuarioFacadeLocal;
import com.sv.udb.modelo.Usuario;
import com.sv.udb.utils.LOG4J;
import com.sv.udb.utils.pojos.DatosUsuarios;
import com.sv.udb.utils.pojos.WSconsUsua;
import com.sv.udb.utils.UsuariosPojo;
import java.io.Serializable;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Carlos
 */
@Named(value = "usuarioBean")
@ViewScoped
@ManagedBean
public class UsuarioBean implements Serializable{
    //Campos de la clase
    @EJB
    private UsuarioFacadeLocal FCDEUsuario;
   
    //Bean de session
    @Inject
    private LoginBean logiBean; 
    
    private List<Usuario> listUsua;
    private Usuario objeUsua;
    private boolean guardar;
    private List<DatosUsuarios> resuElim;
    private LOG4J<UsuarioBean> lgs = new LOG4J<UsuarioBean>(UsuarioBean.class) {
    };
    private Logger log = lgs.getLog();
    /**
     * Creates a new instance of UsuarioBean
     */
    
    public List<Usuario> getListUsua() {
        return listUsua;
    }

    public void setListUsua(List<Usuario> listUsua) {
        this.listUsua = listUsua;
    }

    public Usuario getObjeUsua() {
        return objeUsua;
    }

    public void setObjeUsua(Usuario objeUsua) {
        this.objeUsua = objeUsua;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }
    
    public List<DatosUsuarios> getResuElim() {
        return resuElim;
    }

    public void setResuElim(List<DatosUsuarios> resuElim) {
        this.resuElim = resuElim;
    }
    
    public UsuarioBean() {
    }
    
    /**
    * Método que se ejecuta después de haber construido la clase e inicializa las variables
    */
    @PostConstruct
    public void init()
    {
        this.consTodo();
        //log = new LOG4J();
        //log.debug("Se inicializa el modelo de Usuarios");
    }
    
    /**
     * Método que consulta la información de la base de datos
     */
    public void consTodo()
    {
        try
        {
            this.listUsua = FCDEUsuario.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
    }
    
    public List<Usuario> consTecn(){
        try
        {
            this.listUsua = FCDEUsuario.findTecn();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return this.listUsua;
    }
    
    public String consNomb(String usua)
    {
        UsuariosPojo resp;
        Client client = ClientBuilder.newClient();
        String url = String.format("http://www.opensv.tk:8080/WebService/MiServicio/consUsua/%s", usua);
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            resp = response.readEntity(UsuariosPojo.class); //La respuesta de captura en un pojo que esta en el paquete utils
        }
        else
        {
            resp = null;
        }
        return resp.getNomb() + " " + resp.getApel();
    }
    
    public String consAcce(int codi){
        try{
            this.objeUsua = FCDEUsuario.find(codi);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return this.objeUsua.getAcceUsua();
    }
    
    public String cons(int codi){
        try
        {
            this.objeUsua = FCDEUsuario.find(codi);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return this.objeUsua.getAcceUsua();
    }
    
    public List<Usuario> cons()
    {
        List<Usuario> usua = null;
        try
        {
            usua = FCDEUsuario.findAll();
        }
        catch(Exception ex)
        {
            ex.getMessage();
        }
        finally
        {
            
        }
        return usua;
    }
    
     public void guar(DatosUsuarios obje)
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeUsua = new Usuario();            
            this.objeUsua.setAcceUsua(obje.getUsua());
            this.objeUsua.setTipoUsua(obje.getTipo());
            this.objeUsua.setEstaUsua(1);
            FCDEUsuario.create(this.objeUsua);
            this.resuElim.remove(obje);
            this.guardar = false;
            log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"Usuario"+"-"+"Usuario habilitado: "+this.objeUsua.getAcceUsua());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"Usuarios"+"-"+"Error habilitando Usuario: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
}