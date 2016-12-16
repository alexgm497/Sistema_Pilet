package com.sv.udb.controlador;

import com.sv.udb.utils.UsuariosPojo;
import com.sv.udb.utils.pojos.DatosAlumnos;
import com.sv.udb.utils.pojos.DatosDocentes;
import com.sv.udb.utils.pojos.DatosUsuarios;
import com.sv.udb.utils.pojos.WSconsAlumByDoce;
import com.sv.udb.utils.pojos.WSconsDoceByAlum;
import com.sv.udb.utils.pojos.WSconsUsua;
import com.sv.udb.modelo.Usuario;
import com.sv.udb.controlador.LoginBean;
import com.sv.udb.utils.pojos.DatosUsuariosByCrit;
import com.sv.udb.utils.pojos.WSconsEmplByCodi;
import com.sv.udb.utils.pojos.WSconsEmplByCrit;
import com.sv.udb.utils.pojos.WSconsEmplByUser;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Adonay
 */
@Named(value = "webServicesBean")
@ViewScoped
public class WebServicesBean implements Serializable {
    @Inject
    private LoginBean logiBean; //Bean de session
    
    private static final long serialVersionUID = 1L;    
    
   
    
    private String filt; //Filotro de búsqueda
    private String filtApel;
    private String filtTipo;
    private WSconsUsua objeWebServ;
    //Lógica slider
    private boolean showBusc = false;
    
    //Bean de session
    @Inject
    private UsuarioBean UsuaBean;

    private WSconsAlumByDoce objeWebServAlumByDoce;
    private WSconsDoceByAlum objeWebServDoceByAlum;
    private WSconsEmplByCodi objeWebServConsEmplByCodi;
    private WSconsEmplByUser objeWebServConsEmplByUser;

    public WSconsEmplByUser getObjeWebServConsEmplByUser() {
        return objeWebServConsEmplByUser;
    }

    public void setObjeWebServConsEmplByUser(WSconsEmplByUser objeWebServConsEmplByUser) {
        this.objeWebServConsEmplByUser = objeWebServConsEmplByUser;
    }    

    public WSconsDoceByAlum getObjeWebServDoceByAlum() {
        return objeWebServDoceByAlum;
    }

    public void setObjeWebServDoceByAlum(WSconsDoceByAlum objeWebServDoceByAlum) {
        this.objeWebServDoceByAlum = objeWebServDoceByAlum;
    }

    public WSconsEmplByCodi getObjeWebServConsEmplByCodi() {
        return objeWebServConsEmplByCodi;
    }

    public void setObjeWebServConsEmplByCodi(WSconsEmplByCodi objeWebServConsEmplByCodi) {
        this.objeWebServConsEmplByCodi = objeWebServConsEmplByCodi;
    }
    
    

    public WSconsAlumByDoce getObjeWebServAlumByDoce() {
        return objeWebServAlumByDoce;
    }

    public void setObjeWebServAlumByDoce(WSconsAlumByDoce objeWebServByDoce) {
        this.objeWebServAlumByDoce = objeWebServByDoce;
    }
    
    public String getFilt() {
        return filt;
    }

    public void setFilt(String filt) {
        this.filt = filt;
    }

    public String getFiltApel() {
        return filtApel;
    }

    public void setFiltApel(String filtApel) {
        this.filtApel = filtApel;
    }

    public String getFiltTipo() {
        return filtTipo;
    }

    public void setFiltTipo(String filtTipo) {
        this.filtTipo = filtTipo;
    }

    public WSconsUsua getObjeWebServ() {
        return objeWebServ;
    }

    public void setObjeWebServ(WSconsUsua objeWebServ) {
        this.objeWebServ = objeWebServ;
    }
    
    
    /**
     * Creates a new instance of WebServicesBean
     */
    public WebServicesBean() {
        
    }

    public boolean isShowBusc() {
        return showBusc;
    }
    
    public UsuariosPojo consLogi(String acce, String cont)
    {
        UsuariosPojo resp;
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s/%s", url, "consLogi", acce, this.getSHA256Hash(cont));
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            resp = response.readEntity(UsuariosPojo.class);//La respuesta de captura en un pojo que esta en el paquete utils
            if(!resp.getTipo().equals(null)){
                if(!resp.getTipo().equals("alum" ))
                {
                    new LoginBean().setObjeWSconsEmplByAcce(consEmplByUser(acce));
                }                
            }
        }
        else
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consumir los datos')");
            resp = null;
        }
        return resp;
    }
    
    private String getSHA256Hash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    private String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
    
    public void nuev()
    {
        
    }
    
    public void abri()
    {
        
    }
    
    public void limpForm()
    {
        
    }
    
    public void consWebServHabiUsua()
    {
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s/%s/%s", url, "consUsua", this.filt.equals("") ? null : this.filt, this.filtApel.equals("") ? null : this.filtApel, this.filtTipo.equals("") ? null : this.filtTipo);
        System.out.println(url);
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"));
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            this.objeWebServ = response.readEntity(WSconsUsua.class); //La respuesta de captura en un pojo que esta en el paquete utils
            List<DatosUsuarios> lstDato = new ArrayList<DatosUsuarios>();
            for(DatosUsuarios temp : objeWebServ.getResu()){
                int cont = 0;
                if(this.UsuaBean.getListUsua() != null){
                    for(Usuario temp2 : this.UsuaBean.getListUsua()){
                        if(temp2.getAcceUsua().equals(temp.getUsua())){
                            cont = 1; 
                        }
                    }
                    if(cont == 0){
                        lstDato.add(temp);
                    }
                }
            }
            this.UsuaBean.setResuElim(lstDato);
        }
        else
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al procesar la consulta')");
        }
    }
    /*
    * Toogle buscador, cambia el valor del buscador
    */
    public void toogBusc()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.showBusc = !this.showBusc;
    }
    
    public List<DatosAlumnos> consAlumPorCrit(String nombAlum, String apelAlum, String gradAlum, String espeAlum)
    {
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s/%s/%s/%s", url, "consAlum", nombAlum,apelAlum,gradAlum,espeAlum);
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"));
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            this.objeWebServAlumByDoce = response.readEntity(WSconsAlumByDoce.class); //La respuesta de captura en un pojo que esta en el paquete utils
            for(DatosAlumnos temp : this.objeWebServAlumByDoce.getResu())
            {
                System.err.println(String.format("Carnet: %s Nombre: %s", temp.getCarn(), temp.getNomb()));
            }
        }
        else
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al procesar la consulta')");
        }
        return this.objeWebServAlumByDoce.getResu();
    }
    
    public WSconsAlumByDoce consAlumPorDoce(String codiDoce)
    {
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s", url, "consAlumByDoce", codiDoce);
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"));
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            this.objeWebServAlumByDoce = response.readEntity(WSconsAlumByDoce.class); //La respuesta de captura en un pojo que esta en el paquete utils
        }
        else
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al procesar la consulta')");
        }
        return this.objeWebServAlumByDoce;
    }
    
    
    
    public WSconsDoceByAlum consDocePorAlum(String carnAlum)
    {
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s", url, "consAlum", carnAlum);
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"));
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            this.objeWebServDoceByAlum = response.readEntity(WSconsDoceByAlum.class); //La respuesta de captura en un pojo que esta en el paquete utils
        }
        else
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al procesar la consulta')");
        }
        return this.objeWebServDoceByAlum;
    }
    
    public WSconsEmplByCodi consEmplPorCodi(String codiEmpl)
    {
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s", url, "consEmplByCodi", codiEmpl);
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"));
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            this.objeWebServConsEmplByCodi = response.readEntity(WSconsEmplByCodi.class); //La respuesta de captura en un pojo que esta en el paquete utils           
        }
        else
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al procesar la consulta')");
        }
        return this.objeWebServConsEmplByCodi;
    }
    
    public WSconsEmplByUser consEmplByUser(String user)
    {
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s", url, "consEmpl", user);
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"));
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            this.objeWebServConsEmplByUser = response.readEntity(WSconsEmplByUser.class); //La respuesta de captura en un pojo que esta en el paquete utils
            return this.objeWebServConsEmplByUser;
        }
        else
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al procesar la consulta')");
        }
        return this.objeWebServConsEmplByUser;
    }
    
    public WSconsEmplByCrit consEmplPorParam(String nombEmpl, String apelEmpl, String tipoEmpl)
    {
        /*
            alum
            doceTecn
            doceAcad
            emplAdmi
            emplRece
        */
        WSconsEmplByCrit resp = new WSconsEmplByCrit();
        if(!nombEmpl.trim().equals(""))
        {
            FacesContext facsCtxt = FacesContext.getCurrentInstance();
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            Client client = ClientBuilder.newClient();
            String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
            url = String.format("%s/%s/%s/%s/%s", url, "consUsua", nombEmpl,apelEmpl,tipoEmpl);
            WebTarget resource = client.target(url);
            Invocation.Builder request = resource.request();
            request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"));
            Response response = request.get();
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
            {
                resp = response.readEntity(WSconsEmplByCrit.class); //La respuesta de captura en un pojo que esta en el paquete utils         
            }
            else
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al procesar la consulta')");
            }                   
        }
        return resp;
    }
}
