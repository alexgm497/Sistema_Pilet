package com.sv.udb.controlador;

import com.sv.udb.ejb.AlumnovisitanteFacadeLocal;
import com.sv.udb.ejb.CambiocitaFacadeLocal;
import com.sv.udb.ejb.CitaFacadeLocal;
import com.sv.udb.ejb.ExcepcionhorariodisponibleFacadeLocal;
import com.sv.udb.ejb.HorariodisponibleFacadeLocal;
import com.sv.udb.ejb.UsuarioFacadeLocal;
import com.sv.udb.ejb.VisitanteFacadeLocal;
import com.sv.udb.ejb.VisitantecitaFacadeLocal;
import com.sv.udb.modelo.Alumnovisitante;
import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.modelo.Cita;
import com.sv.udb.modelo.Horariodisponible;
import com.sv.udb.modelo.Visitante;
import com.sv.udb.modelo.Visitantecita;
import com.sv.udb.utils.HorarioCitas;
import com.sv.udb.utils.LOG4J;
import com.sv.udb.utils.pojos.DatosAlumnos;
import com.sv.udb.utils.pojos.DatosUsuariosByCrit;
import com.sv.udb.utils.pojos.WSconsAlumByDoce;
import com.sv.udb.utils.pojos.WSconsDoceByAlum;
import com.sv.udb.utils.pojos.WSconsEmplByCodi;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.SessionBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;

 /**
 * La clase citas 
 * @author: ControlCitas
 * @version: Prototipo 1
 */

@Named(value = "citasBean")
@ViewScoped
public class CitasBean implements Serializable{
    
    //Bean Sesion
    @Inject
    private LoginBean logiBean; 
    
    @Inject
    private GlobalAppBean globalAppBean;
    //FACADE
    @EJB
    private VisitantecitaFacadeLocal FCDEVisiCita;
    @EJB
    private CambiocitaFacadeLocal FCDECambCita; 
    @EJB
    private HorariodisponibleFacadeLocal FCDEHoraDisp;
    @EJB
    private CitaFacadeLocal FCDECita;
    @EJB
    private AlumnovisitanteFacadeLocal FCDEAlumnoVisitante;
     @EJB
    private VisitanteFacadeLocal FCDEVisi; 
    @EJB
    private ExcepcionhorariodisponibleFacadeLocal FCDEExceHoraDisp;
    @EJB
    private UsuarioFacadeLocal FCDEUsua;
    
    //OBJETOS
    private Cita objeCita;
    private Cambiocita objeCambCita;
    private Horariodisponible horaSeleCita;
    private Alumnovisitante alumVisiSelec;
    private Alumnovisitante objeAlumVisi;
    private Visitantecita objeVisiCita = new Visitantecita();
    private Visitante objeVisi;
    
    private WSconsDoceByAlum objeAlumDepe;
    private Cambiocita objeCambCitaDepe;
    //LISTAS
    private List<DatosAlumnos> listAlumnosWS;
    private List<Visitantecita> listVisiCitaRecep;
    private List<Horariodisponible> listHoraDisp;
    private List<Cita> listCitaAlum;
    private List<Cita> listCitaAlumUsua;
    private List<Cita> listCitaVisiUsua;
    private List<Visitante> listVisi;
    private List<Horariodisponible> listHoraDispUsua;
   
    private WSconsAlumByDoce objeWebServAlumByDoce;
    private List<Alumnovisitante> listVisiTemp;
    private List<Visitantecita> listVisiCitaDepe;
    private List<Cita> listVisiUsua;
    private List<Visitante> listVisiVisiTemp;
    //variables de funcionalidad y lógica de negocio
    private boolean guardar;
    private String motivo;
    private Date fechSoliCita;
    private Date fechSoliCita2;
    private String carnAlum;
    private String FechInic;
    private String FechFina;
    
    //variables para manejar campos de la lógica de negocio
    private boolean confirmar;
    private boolean programar;
    private boolean reprogramar;
    private boolean ignoHoraDisp;
    private boolean isGrouVisi;
    private boolean LugaEven;
    private boolean motiUrge;
    private String nombProf;
    private boolean ignoExceHora;
    private boolean isUsuaDoce;
    private String buscAlum="";
    private List<HorarioCitas> listHoraCitaDoce;
    private HorarioCitas horaSeleSoliCita;
    DateFormat timef = new SimpleDateFormat("hh:mm a");
    DateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
    private boolean isVisiUsua;
    private String tipoEmpl="0";
    private String buscEmpl="";
    private int codiUsua;
    private List<DatosUsuariosByCrit> listDoceBusc;
    private List<Cambiocita> listCambCita;
    
    public List<Cambiocita> consListCambVisi()
    {
        consCambVisi();
        return listCambCita;
    }
    
    private Date fechInicBusq;
    private Date fechFinaBusq;
    private int estaCitaSele=10;

    public List<Cambiocita> consListCambCitaEmplVisi() {
        consCambCitaEmpl(3);
        return listCambCita;
    }
    
    public List<Cambiocita> consListCambCitaEmplAlum() {
        consCambCitaEmpl(1);
        return listCambCita;
    }
        
    public List<Cambiocita> consListCambCitaVisiAlum() {
        consCambCitaVisiAlum();
        return listCambCita;
    }
    
    public void setListCambCita(List<Cambiocita> listCambCita) {
        this.listCambCita = listCambCita;
    }

    public boolean isMotiUrge() {
        return motiUrge;
    }

    public void setMotiUrge(boolean motiUrge) {
        this.motiUrge = motiUrge;
    }
    
    
    public void consCambCitaVisiAlum()
    {
        if(this.getFechInicBusq().after(this.getFechFinaBusq()))
        {
            RequestContext ctx = RequestContext.getCurrentInstance();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Fechas Incorrectas')");
        }
        else
        {
            try
            {
                this.listCambCita = FCDECambCita.findCambioCitaByCarnAlum(this.fechInicBusq, this.fechFinaBusq, logiBean.getObjeUsua().getAcceUsua());
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    public void consCambVisi()
    {
        if(this.getFechInicBusq().after(this.getFechFinaBusq()))
        {
            RequestContext ctx = RequestContext.getCurrentInstance();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Fechas Incorrectas')");
        }
        else
        {
            try
            {
                this.listCambCita = FCDECambCita.findCambioVisiByFech(this.fechInicBusq, this.fechFinaBusq);
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    public void consCambCitaEmpl(int tipoCita)
    {
        if(this.getFechInicBusq().after(this.getFechFinaBusq()))
        {
            RequestContext ctx = RequestContext.getCurrentInstance();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Fechas Incorrectas')");
        }
        else
        {
            try
            {
                this.listCambCita = FCDECambCita.findCambioCitaByFechaAndUsua(this.fechInicBusq, this.fechFinaBusq, new LoginBean().getObjeWSconsEmplByAcce().getCodi(),estaCitaSele, tipoCita);
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public int getEstaCitaSele() {
        return estaCitaSele;
    }

    public void setEstaCitaSele(int estaCitaSele) {
        this.estaCitaSele = estaCitaSele;
    }    

    public Date getFechInicBusq() {
        if(fechInicBusq == null)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -15);
            fechInicBusq = c.getTime();
        }
        return fechInicBusq;
    }

    public void setFechInicBusq(Date fechInicBusq) {
        this.fechInicBusq = fechInicBusq;
    }

    public Date getFechFinaBusq() {
        if(fechFinaBusq == null)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 15);
            fechFinaBusq = c.getTime();
        }
        return fechFinaBusq;
    }

    public void setFechFinaBusq(Date fechFinaBusq) {
        this.fechFinaBusq = fechFinaBusq;
    }
    
    //log4j
    private LOG4J<CitasBean> lgs = new LOG4J<CitasBean>(CitasBean.class) {
    };
    private org.apache.log4j.Logger log = lgs.getLog();
    
    
    public List<DatosUsuariosByCrit> getListDoceBusc() {
        consUsuaByCrit();
        return listDoceBusc;
    }

    public void setListDoceBusc(List<DatosUsuariosByCrit> listDoceBusc) {
        this.listDoceBusc = listDoceBusc;
    }
    
    public String getBuscEmpl() {
        return buscEmpl;
    }

    public void setBuscEmpl(String buscEmpl) {
        this.buscEmpl = buscEmpl;
    }
    
    public String getTipoEmpl() {
        return tipoEmpl;
    }

    public void setTipoEmpl(String tipoEmpl) {
        this.tipoEmpl = tipoEmpl;
    }
        
    public HorarioCitas getHoraSeleSoliCita() {
        return horaSeleSoliCita;
    }

    public void setHoraSeleSoliCita(HorarioCitas horaSeleSoliCita) {
        this.horaSeleSoliCita = horaSeleSoliCita;
    }
    

    public List<HorarioCitas> getListHoraCitaDoce() {
        consHoraCitaDoce();
        return listHoraCitaDoce;
    }

    public void setListHoraCitaDoce(List<HorarioCitas> listHoraCitaDoce) {
        this.listHoraCitaDoce = listHoraCitaDoce;
    }
    
    public String getBuscAlum() {
        return buscAlum;
    }

    public void setBuscAlum(String buscAlum) {
        this.buscAlum = buscAlum;
    }
    
    //Switch para formularios
    private boolean switFormCita=true;
    private boolean subSwitFormCita=false;
    
    public boolean getSwitFormCita() {
        return switFormCita;
    }

    public void setSwitFormCita(boolean switFormCita) {
        this.switFormCita = switFormCita;
    }

    public boolean isSubSwitFormCita() {
        return subSwitFormCita;
    }

    public void setSubSwitFormCita(boolean subSwitFormCita) {
        this.subSwitFormCita = subSwitFormCita;
    }
    
    
    
    public void toggSwitFormCita()
    {
        this.switFormCita = !this.switFormCita;
        /*RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        ctx.execute("INIT_OBJE_TABL()");*/
    }
    public void toggSubSwitFormCita()
    {
        this.subSwitFormCita = !this.subSwitFormCita;
        /*RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        ctx.execute("INIT_OBJE_TABL()");*/
    }
    
    public CitasBean() {
        
    }
    //Encapsulamientos
    
   
    public List<Alumnovisitante> getListVisiTemp() {
        return listVisiTemp;
    }

    public Visitante getObjeVisi() {
        return objeVisi;
    }

    public void setObjeVisi(Visitante objeVisi) {
        this.objeVisi = objeVisi;
    }

    public Visitantecita getObjeVisiCita() {
        return objeVisiCita;
    }

    public void setObjeVisiCita(Visitantecita objeVisiCita) {
        this.objeVisiCita = objeVisiCita;
    }
    
    public List<Cita> getListCitaAlum() {
        this.consListCitaAlum();
        return listCitaAlum;
    }

    public Date getFechSoliCita() {
        return fechSoliCita;
    }

    public void setFechSoliCita(Date fechSoliCita) {
        this.fechSoliCita = fechSoliCita;
    }
    public Date getFechSoliCita2() {
        return fechSoliCita2;
    }

    public void setFechSoliCita2(Date fechSoliCita2) {
        this.fechSoliCita2 = fechSoliCita2;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Alumnovisitante getAlumVisiSelec() {
        return alumVisiSelec;
    }

    public void setAlumVisiSelec(Alumnovisitante alumVisiSelec) {
        this.alumVisiSelec = alumVisiSelec;
    }
      
    public List<Horariodisponible> getListHoraDisp() {
        return listHoraDisp;
    }

    public void setListHoraDisp(List<Horariodisponible> listHoraDisp) {
        this.listHoraDisp = listHoraDisp;
    }

    public Horariodisponible getHoraSeleCita() {
        return horaSeleCita;
    }

    public void setHoraSeleCita(Horariodisponible horaSeleCita) {
        this.horaSeleCita = horaSeleCita;
    }
        
    public Cita getObjeCita() {
        return objeCita;
    }

    public void setObjeCita(Cita objeCita) {
        this.objeCita = objeCita;
    }

    public List<Visitantecita> getListVisiCitaRecep() {
        consListVisCitaRecep();
        return listVisiCitaRecep;
    }

    
    

    public boolean isGuardar() {
        return guardar;
    }


    public Cambiocita getObjeCambCita() {
        return objeCambCita;
    }

    public void setObjeCambCita(Cambiocita objeCambCita) {
        this.objeCambCita = objeCambCita;
    }

    public List<Visitante> getListVisi() {
        return listVisi;
    }

    public String getCarnAlum() {
        return carnAlum;
    }

    public void setCarnAlum(String carnAlum) {
        this.carnAlum = carnAlum;
    }

    public boolean isConfirmar() {
        return confirmar;
    }

    public void setConfirmar(boolean confirmar) {
        this.confirmar = confirmar;
    }

    public boolean isProgramar() {
        return programar;
    }

    public void setProgramar(boolean programar) {
        this.programar = programar;
    }
    /* Metodo para saber si el usuario es un docente       
     * @since incluido desde la version 1.0
     */
    public boolean isIsUsuaDoce() {
        if(LoginBean.getObjeWSconsEmplByAcce().getTipo().equals("doceTecn")||LoginBean.getObjeWSconsEmplByAcce().getTipo().equals("doceAcad"))
        {
            this.isUsuaDoce = true;
        }
        else
        {
            this.isUsuaDoce = false;
        }
        return isUsuaDoce;
    }

    public void setIsUsuaDoce(boolean isUsuaDoce) {
        this.isUsuaDoce = isUsuaDoce;
    }

    

    public boolean isReprogramar() {
        return reprogramar;
    }

    public void setReprogramar(boolean reprogramar) {
        this.reprogramar = reprogramar;
    }

    public List<Horariodisponible> getListHoraDispUsua() {
        this.consListHoraDispUsua();
        return listHoraDispUsua;
    }

    public Alumnovisitante getObjeAlumVisi() {
        return objeAlumVisi;
    }

    public void setObjeAlumVisi(Alumnovisitante objeAlumVisi) {
        this.objeAlumVisi = objeAlumVisi;
    }

    public List<DatosAlumnos> getListAlumnosWS() {
        consAlumWS();
        return listAlumnosWS;
    }

    public void setListAlumnosWS(List<DatosAlumnos> listAlumnosWS) {
        this.listAlumnosWS = listAlumnosWS;
    }

    /* Metodo para consultar los alumnos del webservice
     */
    private void consAlumWS()
    {
        if(isUsuaDoce)
        {
            this.objeWebServAlumByDoce = new WebServicesBean().consAlumPorDoce(String.valueOf(LoginBean.getObjeWSconsEmplByAcce().getCodi()));
            this.listAlumnosWS = this.objeWebServAlumByDoce.getResu();
        }  
        else
        {
            if(!(this.buscAlum.equals(null)||this.buscAlum.equals("")))
            {
                this.listAlumnosWS = new WebServicesBean().consAlumPorCrit(this.buscAlum, this.buscAlum, this.buscAlum, this.buscAlum);
            }
        }
    }

    public boolean isIgnoHoraDisp() {
        return ignoHoraDisp;
    }

    public void setIgnoHoraDisp(boolean ignoHoraDisp) {
        this.ignoHoraDisp = ignoHoraDisp;
    }

    public String getFechInic() {
        return FechInic;
    }

    public void setFechInic(String FechInic) {
        this.FechInic = FechInic;
    }

    public String getFechFina() {
        return FechFina;
    }

    public void setFechFina(String FechFina) {
        this.FechFina = FechFina;
    }
    
    
    
    public WSconsDoceByAlum getObjeAlumDepe() {
        return objeAlumDepe;
    }

    public void setObjeAlumDepe(WSconsDoceByAlum objeAlumDepe) {
        this.objeAlumDepe = objeAlumDepe;
    }

    

    public Cambiocita getObjeCambCitaDepe() {
        return objeCambCitaDepe;
    }

    public void setObjeCambCitaDepe(Cambiocita objeCambCitaDepe) {
        this.objeCambCitaDepe = objeCambCitaDepe;
    }

    public List<Visitantecita> getListVisiCitaDepe() {
        return listVisiCitaDepe;
    }

    public boolean isIsGrouVisi() {
        return isGrouVisi;
    }

    public void setIsGrouVisi(boolean isGrouVisi) {
        this.isGrouVisi = isGrouVisi;
    }

    public List<Visitante> getListVisiVisiTemp() {
        return listVisiVisiTemp;
    }

    public boolean isLugaEven() {
        return LugaEven;
    }

    public void setLugaEven(boolean LugaEven) {
        this.LugaEven = LugaEven;
    }

    public List<Cita> getListVisiUsua() {
        consListVisiUsua();
        return listVisiUsua;
    }

    public List<Cita> getListCitaAlumUsua() {
        consListCitaAlumUsua();
        return listCitaAlumUsua;
    }

    public List<Cita> getListCitaVisiUsua() {
        consListCitaVisiUsua();
        return listCitaVisiUsua;
    }

    public String getNombProf() {
        return nombProf;
    }

    public void setNombProf(String nombProf) {
        this.nombProf = nombProf;
    }

    public boolean isIgnoExceHora() {
        return ignoExceHora;
    }

    public void setIgnoExceHora(boolean ignoExceHora) {
        this.ignoExceHora = ignoExceHora;
    }

    public DateFormat getTimef() {
        return timef;
    }

    public void setTimef(DateFormat timef) {
        this.timef = timef;
    }

    public DateFormat getDatef() {
        return datef;
    }

    public void setDatef(DateFormat datef) {
        this.datef = datef;
    }

    public boolean isIsVisiUsua() {
        return isVisiUsua;
    }

    public void setIsVisiUsua(boolean isVisiUsua) {
        this.isVisiUsua = isVisiUsua;
    }

    

    
        
    
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
    }
    
    
    public void limpForm()
    {
        this.objeCita = new Cita();
        this.objeCita.setCodiUbic(null);
        this.fechSoliCita=null;
        this.fechSoliCita2=null;
        this.guardar = true;
        this.objeVisi = new Visitante();
        this.listVisi = new ArrayList<Visitante>();
        listVisi.clear();
        this.objeVisiCita = new Visitantecita();
        listVisiTemp = new ArrayList<Alumnovisitante>();
        listVisiTemp.clear();
        this.carnAlum = "";
        this.confirmar = false;
        this.reprogramar = false;
        this.programar = true;
        this.switFormCita=true;
        this.ignoHoraDisp = false;
        this.horaSeleCita = null;
        this.motivo=null;
        this.objeCambCita  = new Cambiocita();
        this.listVisiVisiTemp = new ArrayList<Visitante>();
        this.LugaEven = true;
        this.nombProf = null;
        isVisiUsua = true;
    }
    //1 es visitante, 2 es usuario
    public void switVisiUsua(int frag){
        this.switFormCita = false;
        switch(frag){
            case 1:
                this.isVisiUsua = true;
            break;
            case 2:
                this.isVisiUsua = false;
            break;
        }
    }
    
     private void consHoraCitaDoce()
    {
        consHorarios(); 
        this.listHoraCitaDoce = new ArrayList<HorarioCitas>();
        for(int x=2; x<=7; x++)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat ndf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, x);            
            if(this.listHoraDisp!=null)
            {
                
                for(Horariodisponible obje : this.listHoraDisp )
                {
                    if(getDay(obje.getDiaHoraDisp())==c.getTime().getDay())
                    {
                        if(FCDEExceHoraDisp.findByDispHora(obje,c.getTime())){
                            if(FCDECambCita.findCambioCitaByParams(ndf.format(c.getTime()), obje.getHoraInicHoraDisp(), obje.getHoraFinaHoraDisp(), this.objeCita.getCodiUsua()))
                            {
                                //System.out.println(obje.getDiaHoraDisp() + " " +c.getTime() + "( " +  obje.getHoraInicHoraDisp()+ " - "+obje.getHoraFinaHoraDisp()+ ") Disponible");
                                this.listHoraCitaDoce.add(new HorarioCitas(sdf.format(c.getTime()),obje.getHoraInicHoraDisp(),obje.getHoraFinaHoraDisp(),obje.getDiaHoraDisp()));
                            }
                            else
                            {
                                //System.out.println(obje.getDiaHoraDisp() + " " +c.getTime() + "( " +  obje.getHoraInicHoraDisp()+ " - "+obje.getHoraFinaHoraDisp()+ ") No Disponible");
                            }                            
                        }
                        else
                        {
                            //System.out.println(obje.getDiaHoraDisp() + " " +c.getTime().getDay() + "( " +  obje.getHoraInicHoraDisp()+ " - "+obje.getHoraFinaHoraDisp()+ ") No Disponible");
                        }
                    }
                }
            }
        }
        if(listHoraCitaDoce.isEmpty() && (this.objeCita.getCodiUsua()!= null && this.objeCita.getCodiUsua()!=0)){
            FacesContext.getCurrentInstance().addMessage("FormRegi:moti", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron Horarios Disponibles, Especificár la urgencia y sugerir un horario para la cita",  null));
            FacesContext.getCurrentInstance().addMessage("FormSoliCita:moti", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron Horarios Disponibles, Especificár la urgencia y sugerir un horario para la cita",  null));
        }
    }
     
    public void consHorarios()
    {
        try
        {
            this.listHoraDisp = FCDEHoraDisp.findByCodiUsua(this.objeCita.getCodiUsua());
            setUrgeCita();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void consListHoraDispProf(int codi)
    {
        try
        {
            this.listHoraDisp = FCDEHoraDisp.findByCodiUsua(codi);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    
    /* SECCIÓN DESTINADA A LA PROGRAMACIÓN DE CITAS PARA VISITANTES */
    
    public void consObjeCitaAlum()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeCita = FCDECita.find(codi);
            this.objeCambCita = FCDECambCita.findByCita(objeCita);
            fechSoliCita = objeCambCita.getFechInicCitaNuev();
            this.listVisiTemp = FCDEAlumnoVisitante.findByCarnAlum(logiBean.getObjeUsua().getAcceUsua());//--> Variable session carnet alumno
            alumVisiSelec= listVisiTemp.get(0);
            consListHoraDispProf(objeCita.getCodiUsua());
            this.nombProf = new WebServicesBean().consEmplPorCodi(String.valueOf(objeCita.getCodiUsua())).getNomb();
            estaCita();
            //si la cita involucra otro alumno, no se podrá solicitar reprogramación
            List<Visitantecita> listVisiCitaTemp = FCDEVisiCita.findByCodiCita(objeCita);
            for(Visitantecita obje: listVisiCitaTemp){
                if(!obje.getCarnAlum().equals(String.valueOf(logiBean.getObjeUsua().getAcceUsua())))reprogramar = false;
            }
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Consultada')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
    
     public void consListCitaAlum()
    {
        try
        {
            this.listCitaAlum = FCDECita.findByCarnAlum(String.valueOf(logiBean.getObjeUsua().getAcceUsua()));
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
     
     public void setUrgeCita(){
         if(motiUrge){
             FacesContext.getCurrentInstance().addMessage("FormRegi:moti", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Especificar Urgencia, Motivo, y Horario Solicitado",  null));
         }
     }
     
    //LOG: cita solicitada por visitante
      /**
 * Metodo para solicitar visitas 
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void soliCitaVisi()
    {
        try{
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            if(valiDatoCitaVisi(1))
            {
                //objeCita = new Cita();
                this.objeCita.setTipoCita(1);
                this.objeCita.setTipoVisi(2);
                this.objeCita.setTipoDura(2);
                this.objeCita.setEstaCita(1);
                this.objeCita.setDescCita(this.motivo);
                FCDECita.create(this.objeCita);  
                if(listCitaAlum == null)listCitaAlum = new ArrayList<Cita>();
                this.listCitaAlum.add(this.objeCita);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat df = new SimpleDateFormat("hh:mm a");
                objeCambCita = new Cambiocita();
                objeCambCita.setCodiCita(this.objeCita);
                objeCambCita.setFechCambCita(new Date());
                objeCambCita.setHoraCambCita(df.format(new Date()));
                if(!listHoraCitaDoce.isEmpty() && horaSeleSoliCita != null){
                    objeCambCita.setFechInicCitaNuev(sdf.parse(this.horaSeleSoliCita.getFecha()));
                    objeCambCita.setFechFinCitaNuev(sdf.parse(this.horaSeleSoliCita.getFecha()));
                    objeCambCita.setHoraInicCitaNuev(this.horaSeleSoliCita.getHoraInic());
                    objeCambCita.setHoraFinCitaNuev(this.horaSeleSoliCita.getHoraFina());
                }
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());
                FCDECambCita.create(objeCambCita);
                objeVisiCita.setCodiCita(this.objeCita);
                objeVisiCita.setCodiVisi(alumVisiSelec.getCodiVisi());
                objeVisiCita.setCarnAlum(String.valueOf(logiBean.getObjeUsua().getAcceUsua()));
                FCDEVisiCita.create(objeVisiCita);
                globalAppBean.addNotificacion(this.logiBean.getObjeUsua().getCodiUsua(), "SE HA GUARDADO UN EVENTO"  , "Modulo citas", "");
                log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha solicitado la cita, espere por la respuesta");
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Se ha solicitado la cita, espere por la respuesta.')");
                
                SimpleDateFormat sdfCita = new SimpleDateFormat("dd/MM/yyyy");
                try
                {
                    new EnvioCorreosBean().sendMail(new WebServicesBean().consEmplPorCodi(objeVisiCita.getCodiCita().getCodiUsua()+"").getMail(),
                    "Solicitud de Cita", "Se ha solicitado una cita para el día: " 
                            + sdfCita.format(objeCambCita.getFechInicCitaNuev()) + " a las " 
                            + objeCambCita.getHoraInicCitaNuev() + " en: " 
                            + objeCambCita.getCodiCita().getCodiUbic().getNombUbic() +
                            " con el visitante: " + objeVisiCita.getCodiVisi().getNombVisi() + " " + objeVisiCita.getCodiVisi().getApelVisi()
                            + " Motivo: " + objeCita.getDescCita());
                }
                catch(Exception err){System.out.println("Error al enviar correo");}
                try
                {
                    String acceUsua = new WebServicesBean().consEmplPorCodi(objeCambCita.getCodiCita().getCodiUsua()+"").getMail();
                    int codiUsuaCons = FCDEUsua.findByAcce(acceUsua).getCodiUsua();
                    globalAppBean.addNotificacion(codiUsuaCons, "Se ha solicitado una cita para el día: " 
                            + sdfCita.format(objeCambCita.getFechInicCitaNuev()) + " a las " 
                            + objeCambCita.getHoraInicCitaNuev() + " en: " 
                            + objeCambCita.getCodiCita().getCodiUbic().getNombUbic() 
                            + " con el visitante: " + objeVisiCita.getCodiVisi().getNombVisi() + " " + objeVisiCita.getCodiVisi().getApelVisi() +
                            " Motivo: " + objeCita.getDescCita(), "Citas y Visitas", null);
                }
                catch(Exception err) {System.out.println("Error al enviar notificación");} 
                
                this.limpForm();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
     /**
     * Metodo para solicitar un cambio de cita
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    
    //LOG: Cambio de cita agregado
    //solicitar Reprogramación(1), Solicitar Cancelación(2)
    public void soliCambCita(int acci){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            if(valiDatoCitaVisi(acci) || acci == 2){
                switch(acci){
                    case 0:
                        objeCita.setEstaCita(0);
                    break;
                    case 1:
                        objeCambCita.setFechInicCitaNuev(sdf.parse(this.horaSeleSoliCita.getFecha()));
                        objeCambCita.setFechFinCitaNuev(sdf.parse(this.horaSeleSoliCita.getFecha()));
                        DateFormat df = new SimpleDateFormat("hh:mm a");
                        objeCambCita.setHoraCambCita(df.format(new Date()));
                        objeCambCita.setHoraInicCitaNuev(this.horaSeleSoliCita.getHoraInic());
                        objeCambCita.setHoraFinCitaNuev(this.horaSeleSoliCita.getHoraFina());
                        objeCita.setEstaCita(4);
                    break;
                    case 2:
                        objeCita.setEstaCita(5);
                    break;
                }
                listCitaAlum.remove(objeCita);

                objeCambCita.setCodiCita(objeCita);
                objeCambCita.setMotiCambCita(motivo);
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());
                objeCambCita.setFechCambCita(new Date());
                DateFormat df = new SimpleDateFormat("hh:mm a");
                objeCambCita.setHoraCambCita(df.format(new Date()));
                FCDECita.edit(objeCita);
                FCDECambCita.create(objeCambCita);

                listCitaAlum.add(objeCita);

                switch(acci){
                    case 0:
                        log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha cancelado la solicitud ");
                        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Solicitud Cancelada'); $('#ModaFormRegi').modal('hide');");
                        this.limpForm();
                    break;
                    case 1:
                        log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha solicitado reprogramacion ");
                        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reprogramación Solicitada'); $('#ModaFormRegi').modal('hide');");
                        this.limpForm();
                    break;
                    case 2:
                        log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha solicitado cancelacion ");
                        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cancelación Solicitada');");
                    break;
                }
                estaCita();
                
                limpForm();
            }
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Error al realiza la accion ");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al realizar la acción')");
            ex.printStackTrace();
        }
    }
    //1 solicitar, 2 soli repro
     /**
 * Metodo para validar las fechas y horas en las citas
 */
    private boolean valiDatoCitaVisi(int acci){
        boolean val = false;
            if(this.horaSeleSoliCita== null || listHoraCitaDoce.isEmpty())
            {
                switch(acci){
                    case 1:
                        if(!listHoraCitaDoce.isEmpty() && !motiUrge){
                            FacesContext.getCurrentInstance().addMessage("FormRegi:hora", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe Seleccionar una horario Disponible",  null));
                        }else{
                            val = true;
                        }
                        
                    break;
                    case 2:
                        val = true;
                    break;
                }
            }
            else
            {
                val=true;
            }
        return val;
    }
    private int getDay(String dia){
        int ndia = 0;
        String dias[] = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
        for(int i = 0; i < dias.length; i++){
            if(dia.equals(dias[i])){ndia = i+1; break;}
        }
        return ndia;
    }
    
    
   
    
    
    /* TERMINA SECCIÓN DESTINADA A LA PROGRAMACIÓN DE CITAS PARA VISITANTES */
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*SECCIÓN DESTINADA A LA PROGRAMACIÓN DE REGISTRO DE VISITAS (CITAS DE TIPO 2), PARA LLEVAR CONTROL DE VISITANTES*/
    
    
    
    
    public void consListVisCitaRecep()
    {
        try
        {
            this.listVisiCitaRecep = FCDEVisiCita.findByNow();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void consObjeVisiCitaRecep(){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            limpForm();
            this.objeVisiCita = FCDEVisiCita.find(codi);
            if(objeVisiCita.getCodiCita().getTipoCita() == 1){
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Consultada')");
            }else{
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Visita Consultada')");
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
    
    //1(registrar entrada), 2 (registrar Salida)
    /* Metodo para cambiar la cita de un visitante
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void cambVisiCitaRecep(int estado){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            listVisiCitaRecep.remove(objeVisiCita);
            objeVisiCita.setEstaVisi(estado);
            
            DateFormat dfh = new SimpleDateFormat("hh:mm a");
            switch(estado){
                case 1:
                    objeVisiCita.setFechLlegCita(new Date());
                    objeVisiCita.setHoraLlegCita(dfh.format(new Date()));
                break;
                case 2:
                    objeVisiCita.setFechSaliCita(new Date());
                    objeVisiCita.setHoraSaliCita(dfh.format(new Date()));
                break;
            }
            FCDEVisiCita.edit(objeVisiCita);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cambio Realizado'); $('#ModaFormRegi').modal('hide');");
            listVisiCitaRecep.add(objeVisiCita);
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al realizar la acción')");
            ex.printStackTrace();
        }
    }
    
    //setear visitante al ser seleccionado desde una tabla
    public void setVisiRecep(){
        try{
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjeVisi"));
            this.objeVisi = FCDEVisi.find(codi);
            if(objeVisi == null) objeVisi = new Visitante();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Visitante Seleccionado')");
            this.switFormCita = true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    //setear visitante al ser seleccionado desde una tabla
    public void setEmplRecep(){
        try{
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjeEmpl"));
            this.objeCita.setCodiUsua(codi);
            WSconsEmplByCodi objeProf  = new WebServicesBean().consEmplPorCodi(String.valueOf(codi));
            this.nombProf = objeProf.getNomb();
            System.out.println("USUARIO: "+objeCita.getCodiUsua());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Personal de la Institución Seleccionado')");
            this.switFormCita = true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    //Solicitar Citas especificando citador y citado
    //LOG: Recepcionista solicitó cita
    public void soliCitaRecep()
    {
        try{
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            if(valiDatoCitaVisi(1))
            {
                //objeCita = new Cita();
                System.out.println("USUARIO: "+objeCita.getCodiUsua());
                this.objeCita.setTipoCita(1);
                this.objeCita.setTipoVisi(2);
                this.objeCita.setTipoDura(2);
                this.objeCita.setEstaCita(1);
                this.objeCita.setDescCita(this.motivo);
                FCDECita.create(this.objeCita);  
                if(listCitaAlum == null)listCitaAlum = new ArrayList<Cita>();
                this.listCitaAlum.add(this.objeCita);
                objeCambCita = new Cambiocita();
                objeCambCita.setCodiCita(this.objeCita);
                
                
                objeCambCita.setFechCambCita(new Date());
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat df = new SimpleDateFormat("hh:mm a");
                if(!listHoraCitaDoce.isEmpty() && horaSeleSoliCita != null){
                    objeCambCita.setFechInicCitaNuev(sdf.parse(this.horaSeleSoliCita.getFecha()));
                    objeCambCita.setFechFinCitaNuev(sdf.parse(this.horaSeleSoliCita.getFecha()));
                    objeCambCita.setHoraInicCitaNuev(this.horaSeleSoliCita.getHoraInic());
                    objeCambCita.setHoraFinCitaNuev(this.horaSeleSoliCita.getHoraFina());
                }
                
                objeCambCita.setHoraCambCita(df.format(new Date()));
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());
                FCDECambCita.create(objeCambCita);
                objeVisiCita.setCodiCita(this.objeCita);
                objeVisiCita.setCodiVisi(this.objeVisi);
                FCDEVisiCita.create(objeVisiCita);
                log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha solicitado la cita, espere por la respuesta");
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Se ha solicitado la cita, espere por la respuesta.')");
                this.limpForm();
            }
        }catch(Exception e){
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Error al solicitar cita");
            e.printStackTrace();
        }
        
    }
    
    //setear visitante en visita en recepcionista
    public void setVisiVisi(){
        try{
            int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjeVisiCita"));
            this.objeVisi = FCDEVisi.find(codi);
            if(objeVisi == null) objeVisi = new Visitante();
            if(listVisiVisiTemp == null)listVisiVisiTemp = new ArrayList<Visitante>();
            
                RequestContext ctx = RequestContext.getCurrentInstance();
            if((isGrouVisi && listVisiVisiTemp.size() < 1) || !isGrouVisi){
                listVisiVisiTemp.add(objeVisi);
                //eliminamos posibles cambioCita duplicados
                HashSet<Visitante> hashSet = new HashSet<Visitante>(listVisiVisiTemp);
                listVisiVisiTemp.clear();
                listVisiVisiTemp.addAll(hashSet); 
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Visitante Agregado')");
            }else{
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Este grupo ya tiene un encargado')");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    //registrar visita recepcionista
    //LOG: el recepcionista registró una visita
    public void progVisiRecep(){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        DateFormat df = new SimpleDateFormat("hh:mm a");
        fechSoliCita = new Date();
        fechSoliCita2 = fechSoliCita;
        FechInic = df.format(new Date());
        System.out.println(FechInic+" "+FechFina);
        
    
        try
        {
            if(valiDatoProgVisi()){
                objeCita.setEstaCita(2);
                objeCita.setDescCita(motivo);
                objeCita.setTipoCita(2);
                objeCita.setTipoDura(2);
                objeCita.setTipoVisi(2);
                //crear el objeto cita
                FCDECita.create(objeCita);

                objeCambCita.setCodiCita(objeCita);
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());
                objeCambCita.setFechCambCita(new Date());

                objeCambCita.setFechInicCitaNuev(fechSoliCita);
                objeCambCita.setFechFinCitaNuev(fechSoliCita2);
                objeCambCita.setHoraCambCita(df.format(new Date()));
                objeCambCita.setHoraInicCitaNuev(FechInic);
                objeCambCita.setHoraFinCitaNuev(FechFina);
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());

                //crear el objeto cambio Cita
                FCDECambCita.create(objeCambCita);
                
                if(objeVisiCita == null)objeVisiCita = new Visitantecita();
                //registrando visitantes Cita
                for(Visitante visi : listVisiVisiTemp){
                    objeVisiCita.setCodiCita(objeCita);
                    objeVisiCita.setCodiVisi(visi);
                    objeVisiCita.setFechLlegCita(new Date());
                    objeVisiCita.setFechSaliCita(new Date());
                    objeVisiCita.setEstaVisi(1);
                    objeVisiCita.setHoraLlegCita(FechInic);
                    FCDEVisiCita.create(objeVisiCita);
                    
                }
                if(this.listVisiUsua ==  null)this.listVisiUsua = new ArrayList<Cita>();
                this.listVisiUsua.add(objeCita);
                this.limpForm();
                log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Cita programada");
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Programada');");
            }            
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Error al programar cita ");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al Programar')");
            ex.printStackTrace();
        }
    }
    
    
    
    /*SECCIÓN TERMINADA DE VISITAS (CITAS DE TIPO 2), PARA LLEVAR CONTROL DE VISITANTES*/
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
                                    /*SECCIÓN PARA CITAS (DOCENTE Y PERSONAL ADMIN.)*/
    /* Metodo para consultar el ultimo objeto "cambio cita" a partir de un objeto "cita"
    * establecer booleanos segun el estado de la cita
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    
    private void estaCita(){
        switch(this.objeCita.getEstaCita()){
            //cuando una cita está por ser programada
            case 0:
                this.reprogramar = true;
                this.confirmar = false;
                this.programar = false;
                break;
            //cuando una cita está solicitada
            case 1:
                this.reprogramar = false;
                this.confirmar = true;
                this.programar = false;
            break;
            //cuando una cita está confirmada
            case 2:
                this.reprogramar = true;
                this.confirmar = false;
                this.programar = false;
            break;
            //cuando una cita está rechazada
            case 3:
                this.reprogramar = true;
                this.confirmar = false;
                this.programar = false;
            break;
            //cuando se solicitó una reprogramación
            case 4:
                this.reprogramar = false;
                this.confirmar = true;
                this.programar = false;
            break;
            //cuando se solicitó una cancelación
            case 5:
                this.reprogramar = false;
                this.confirmar = true;
                this.programar = false;
            break;
        }
    }
     
    /* Método para retornar parentesco, dependiendo del código se le pase
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public String getParen(Visitante visi){
        String parentesco = "";
        Alumnovisitante obje = consObjeAlumVisi(visi);
        switch(obje.getPareAlumVisi()){
            case 1:
                parentesco = "Padre/Madre";
            break;
            case 2:
                parentesco = "Tio/Tia";
            break;
            case 3:
                parentesco = "Abuelo/Abuela";
            break;
            case 4:
                parentesco = "Hermano/Hermana";
            break;
            case 5:
               parentesco = "Primo/Prima";
            break;
            case 6:
                parentesco = obje.getEspeAlumVisi();
            break;
            
        }
        return parentesco;
    }
    //consultar el ultimo cambio de la cita (para mostrar en la tabla)

    
    /* Metodo para consultar el ultimo cambio de la cita (para mostrar en la tabla)
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public Visitante consObjeVisi(int codi){
        Visitante objecons = null; 
        try
        {
             objecons = FCDEVisi.find(codi);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return objecons;
    }
    
    //consultar un alumno visitante a partir de un visitante y el carné del alumno seleccionado (para mostrar en tabla)
    //se tuvo que haber seleccionado el visitante en combobox primero
    public Alumnovisitante consObjeAlumVisi(Visitante visi){
        try
        {
            this.objeAlumVisi = FCDEAlumnoVisitante.findByAlumVisi(visi, carnAlum);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return objeAlumVisi;
    }
    
    //consultar un onbjeto de tipo alumno de la lista hipotética del web service
    // considerar consultar un solo registro del web service en el futuro....
    public WSconsDoceByAlum consObjeAlumno(String carn){
        return new WebServicesBean().consDocePorAlum(carn);
    }
    
    public WSconsDoceByAlum consDoceAlum(){
        return new WebServicesBean().consDocePorAlum(logiBean.getObjeUsua().getAcceUsua());
    }
    
    /* Metodo para consultar las citas del usuario
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void consListCitaAlumUsua()
    {
        try
        {
            this.listCitaAlumUsua = FCDECita.findCitaAlumByCodiUsua(LoginBean.getObjeWSconsEmplByAcce().getCodi());
            if(this.listCitaAlumUsua == null)this.listCitaAlumUsua = new ArrayList<Cita>();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    /* Metodo para consultar las citas del usuario
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void consListCitaVisiUsua()
    {
        try
        {
            this.listCitaVisiUsua = FCDECita.findCitaVisiByCodiUsua(LoginBean.getObjeWSconsEmplByAcce().getCodi());
            if(this.listCitaVisiUsua == null)this.listCitaVisiUsua = new ArrayList<Cita>();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
        
    /* Metodo para consultar los encargados de un alumno
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void consListVisiAlum(){
        try
        {
            if(carnAlum!=null){
                listVisi = FCDEVisi.findByCarnAlum(carnAlum);
            }else{
                listVisi = new ArrayList<Visitante>();
            }
            consVisiCita(true);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
     /* Metodo para consultar los horariso disponibles del usuario 
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    private void consListHoraDispUsua(){
        try
        {
            this.listHoraDispUsua = FCDEHoraDisp.findByCodiUsua(LoginBean.getObjeWSconsEmplByAcce().getCodi());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    /* Metodo para consultar visitantes de una cita y los guardamos en una lista temporal
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void consVisiCita(boolean padre){
        try{
            if(objeCita==null)objeCita=new Cita();
            if(padre){
                if(listVisiTemp==null){
                    listVisiTemp =FCDEAlumnoVisitante.findByCita(objeCita);
                    if(listVisiTemp==null)listVisiTemp = new ArrayList<Alumnovisitante>();
                }else{
                    //tomamos registros anteriores en una nueva lista
                    List<Alumnovisitante> listTransac = listVisiTemp; 
                    listVisiTemp=FCDEAlumnoVisitante.findByCita(objeCita);
                    if(listVisiTemp==null )listVisiTemp = new ArrayList<Alumnovisitante>();
                    //agregamos los anteriores a los nuevos
                    listVisiTemp.addAll(listTransac);
                    //..eliminamos repetidos
                    HashSet<Alumnovisitante> hashSet = new HashSet<Alumnovisitante>(listVisiTemp);
                    listVisiTemp.clear();
                    listVisiTemp.addAll(hashSet);
                }
                if(listVisi==null)listVisi=new ArrayList<Visitante>();
                for(Alumnovisitante visi : listVisiTemp){
                    for(Visitante visi2 : listVisi){
                        if(Objects.equals(visi.getCodiVisi(), visi2)){
                            listVisi.remove(visi2);//quitamos los visitantes que ya estan en la cita, del combobox (lista combobox)
                        }
                    }
                }
            }else{
            listVisiVisiTemp = FCDEVisi.findByCita(objeCita);
            if(listVisiVisiTemp == null)listVisiVisiTemp = new ArrayList<Visitante>();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void consDepeListCitaUsua(Cita cita){
        try
        {
            //consultamos la lista de visitante_cita
            this.listVisiCitaDepe = FCDEVisiCita.findByCodiCita(cita);
            if(listVisiCitaDepe == null)listVisiCitaDepe = new ArrayList<Visitantecita>();
            //consultamos el ultimo objeto cambio_cita
            this.objeCambCitaDepe = FCDECambCita.findByCita(cita);
            if(objeCambCitaDepe==null)objeCambCitaDepe = new Cambiocita();
            //consultamos el alumno
            if(listVisiCitaDepe.get(0).getCarnAlum() != null && listVisiCitaDepe.get(0).getCarnAlum().trim()!=""){
            this.objeAlumDepe = consObjeAlumno(listVisiCitaDepe.get(0).getCarnAlum());
            }
            
            if(objeAlumDepe==null)objeAlumDepe= new WSconsDoceByAlum();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public Visitantecita consFirtObjeVisiCitaDepe(){
        return listVisiCitaDepe.get(0);
    } 
    /* Metodo usado para consultar los encargados de un alumno, al seleccionar un alumno desde una tabla
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void setAlumn(){
        String Carn = String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjeAlum"));
        if(Carn!=null){
            this.carnAlum = Carn;
            consListVisiAlum();
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Encargados Consultados')");
        }
    }
    
    
    /* Metodo para consultar una cita del usuario (de los que estan en la tabla de datos)
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void consObjeCitaUsua(boolean padre){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            limpForm();
            this.objeCita = FCDECita.find(codi);
            this.objeCambCita = FCDECambCita.findByCita(objeCita);//consultamos el ultimo cambio de cita
            if(objeCambCita == null) objeCambCita = new Cambiocita();
            this.fechSoliCita = objeCambCita.getFechInicCitaNuev();
            listVisiTemp = null;
            consVisiCita(padre);
            this.guardar = false;
            estaCita();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Consultada')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
        
    }
   
    
    /* Metodo para agregar un visitante a una cita a una lista temporal
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void addVisiCita(){
        try
        {
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            if(listVisiTemp == null)listVisiTemp = new ArrayList<Alumnovisitante>();
            consVisiCita(true);//consultamos si ya hay visitantes en la cita
            this.listVisiTemp.add(consObjeAlumVisi(objeVisi));//i agregamos al nuevo
            this.listVisi.remove(objeVisi);
            ctx.execute("setMessage('MESS_INFO', 'Atención', 'Visitante Agregado')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
    /* Metodo para quitar un visitante en la cita de la lista temporal
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    public void dropVisiCita(Alumnovisitante obje){
        try
        {
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            this.listVisiTemp.remove(obje);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Visitante Eliminado de la Cita')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    //confirmar(1), rechazar(2), Reprogramar(3) cita
    private boolean valiDatoCambCita(int acci) throws ParseException{
        boolean vali = false;
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        //si no es nulo ó se esta solicitando cancelación ó se esta rechazando ó cancelando
        if(objeCita.getCodiUbic() != null || (acci == 1 && objeCita.getEstaCita() == 5) || acci == 2){
            //si no es nulo ó se esta solicitando cancelación  ó se esta rechazando
            if((motivo != null && !motivo.trim().equals("")) || (acci == 1 && objeCita.getEstaCita() == 5) || acci == 2){
                if(ignoHoraDisp){
                    if(formatter.parse(FechFina).after(formatter.parse(FechInic))){
                        vali = true;
                    }else{
                        FacesContext.getCurrentInstance().addMessage("FormRegi:horaInicCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La hora de inicio no puede ser despues de la hora de fin",  null));
                        FacesContext.getCurrentInstance().addMessage("FormRegi:horaFinaCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La hora de inicio no puede ser despues de la hora de fin",  null));
                    }
                }else{
                    vali = true;
                }
            }else{
                FacesContext.getCurrentInstance().addMessage("FormRegi:descCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe Especificar El Motivo",  null));
            }
        }else{
            FacesContext.getCurrentInstance().addMessage("FormRegi:ubicCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe Seleccionar un Lugar",  null));
        }
        
        return vali;
    }
    
    //LOG: cambio de cita realizado
    //confirmar(1), rechazar(2), Reprogramar(3), Cancelar(0) cita
    public void cambCita(int acci, boolean padre){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        List <Visitantecita> listVisiCitaLoca = FCDEVisiCita.findByCodiCita(objeCita);
        String tipoCambio="";
        try
        {
            
            if(valiDatoCambCita(acci) || acci == 0){
                switch(acci){
                    case 0:
                        objeCita.setEstaCita(0);
                    break;
                    case 1:
                        tipoCambio=" confirmada ";
                        //confirmar cancelación
                        if(objeCita.getEstaCita() == 5){
                            objeCita.setEstaCita(0);
                        }else{
                            //confirmar reprogramación, confirmar cita etc...
                            objeCita.setEstaCita(2);
                            if(objeCambCita.getFechInicCitaNuev()== null || objeCambCita.getFechFinCitaNuev()== null){
                                objeCambCita.setFechInicCitaNuev(fechSoliCita);
                                objeCambCita.setFechFinCitaNuev(fechSoliCita);
                                DateFormat df = new SimpleDateFormat("hh:mm a");
                                objeCambCita.setHoraCambCita(df.format(new Date()));
                                if(ignoHoraDisp){
                                    objeCambCita.setHoraInicCitaNuev(FechInic);
                                    objeCambCita.setHoraFinCitaNuev(FechFina);
                                }else{
                                    objeCambCita.setHoraInicCitaNuev(horaSeleCita.getHoraInicHoraDisp());
                                    objeCambCita.setHoraFinCitaNuev(horaSeleCita.getHoraFinaHoraDisp());
                                }
                            }
                        }
                    break;
                    case 2:
                        tipoCambio=" rechazada ";
                        objeCita.setEstaCita(3);
                    break;
                    case 3:
                        tipoCambio=" reprogramada ";
                        objeCambCita.setFechInicCitaNuev(fechSoliCita);
                        objeCambCita.setFechFinCitaNuev(fechSoliCita);
                        DateFormat df = new SimpleDateFormat("hh:mm a");
                        objeCambCita.setHoraCambCita(df.format(new Date()));
                        if(ignoHoraDisp){
                            objeCambCita.setHoraInicCitaNuev(FechInic);
                            objeCambCita.setHoraFinCitaNuev(FechFina);
                        }else{
                            objeCambCita.setHoraInicCitaNuev(horaSeleCita.getHoraInicHoraDisp());
                            objeCambCita.setHoraFinCitaNuev(horaSeleCita.getHoraFinaHoraDisp());
                        }
                        objeCita.setEstaCita(2);
                    break;
                }
                /*if(padre){
                    listCitaAlumUsua.remove(objeCita);
                }else{
                    listCitaVisiUsua.remove(objeCita);
                }*/
                objeCambCita.setCodiCita(objeCita);
                objeCambCita.setMotiCambCita(motivo);
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());
                objeCambCita.setFechCambCita(new Date());
                DateFormat df = new SimpleDateFormat("hh:mm:a");
                objeCambCita.setHoraCambCita(df.format(new Date()));
                FCDECita.edit(objeCita);
                FCDECambCita.create(objeCambCita);
                objeCambCita.setCodiCita(objeCita);
                /*if(objeCita.getEstaCita() != 0){
                    if(padre){
                        listCitaAlumUsua.add(objeCita);
                    }else{
                        listCitaVisiUsua.add(objeCita);
                    }
                }
                */
                switch(acci){
                    case 0:
                        log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha cancelado la cita ");
                        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Cancelada'); $('#ModaFormRegi').modal('hide');");
                        this.limpForm();
                    break;
                    case 1:
                        log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha confirmado cita ");
                        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Confirmada'); $('#ModaFormRegi').modal('hide');");
                        this.limpForm();
                    break;
                    case 2:
                        log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha rechazado cita, proceda a reprogramar");
                        ctx.execute("setMessage('MESS_INFO', 'Atención', 'Cita Rechazada, Proceda a Reprogramar');");
                    break;
                    case 3:{
                        log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha reprogramado una cita con codigo:" + objeCita.getCodiCita());
                        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Reprogramada'); $('#ModaFormRegi').modal('hide');");
                    }
                }
                estaCita();
                if(padre)
                {
                    consCambCitaEmpl(1);  
                }
                else
                {
                    consCambCitaEmpl(3); 
                }   
                
                for(Visitantecita objeVisiCitaLoca : listVisiCitaLoca)
                {
                    SimpleDateFormat sdfCita = new SimpleDateFormat("dd/MM/yyyy");
                    try
                    {

                        new EnvioCorreosBean().sendMail(objeVisiCitaLoca.getCodiVisi().getCorrVisi(),
                        "Cambio de Cita", "Se ha"+ tipoCambio +"una cita para el día: " 
                                + sdfCita.format(objeCambCita.getFechInicCitaNuev()) + " a las " 
                                + objeCambCita.getHoraInicCitaNuev() + " en: " 
                                + objeCambCita.getCodiCita().getCodiUbic().getNombUbic() +
                                " con el empleado: " + new WebServicesBean().consEmplPorCodi(""+objeCita.getCodiUsua()).getNomb()
                                + " Motivo: " + objeCita.getDescCita());
                    }
                    catch(Exception err){System.out.println("Error al enviar correo");}
                    try
                    {
                        int codiUsuaCons = FCDEUsua.findByAcce(objeVisiCitaLoca.getCarnAlum()).getCodiUsua();
                        globalAppBean.addNotificacion(codiUsuaCons, "Se ha programado una cita para el día: " 
                                + sdfCita.format(objeCambCita.getFechInicCitaNuev()) + " a las " 
                                + objeCambCita.getHoraInicCitaNuev() + " en: " 
                                + objeCambCita.getCodiCita().getCodiUbic().getNombUbic() 
                                + " con el empleado: " + new WebServicesBean().consEmplPorCodi(""+objeCita.getCodiUsua()).getNomb()+
                                " Motivo: " + objeCita.getDescCita(), "Citas y Visitas", null);
                    }
                    catch(Exception err) {System.out.println("Error al enviar notificación");}    
                }
            }
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Error al realizar la accion ");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al realizar la acción')");
            ex.printStackTrace();
        }
    }
    
    private boolean valiDatoProgVisiUsua() throws ParseException{
        boolean vali = false;
        //si la fecha no es null y ( si el horario no es null ó se esta ignorando el horario)
        if((horaSeleCita != null || ignoHoraDisp) && fechSoliCita!= null){
            int diaHoraDisp = (horaSeleCita == null)? 0 : getDay(this.horaSeleCita.getDiaHoraDisp());
            int diaExceHoraDisp = this.fechSoliCita.getDay();
            //si la fecha = horario
            if(diaHoraDisp == diaExceHoraDisp || ignoHoraDisp){
                //si hay visitantes en la cita
                if(listVisiTemp.size() > 0 || listVisiVisiTemp.size() > 0){
                    DateFormat formatter = new SimpleDateFormat("hh:mm a");
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    //si la fecha es despues de "hoy"
                    if(!fechSoliCita.before(new Date()) || df.format(fechSoliCita).equals(df.format(new Date()))){
                        //si la ubicacion != null
                        if(objeCita.getCodiUbic() != null){
                            //si el motivo != null
                            if((motivo != null && !motivo.trim().equals(""))){
                                //fecha fin despues de fecha inicio y se ignora 
                                if(ignoHoraDisp){
                                    if(formatter.parse(FechFina).after(formatter.parse(FechInic)) && ignoHoraDisp){
                                        vali = true;
                                    }else{
                                        FacesContext.getCurrentInstance().addMessage("FormRegi:horaInicCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La hora de inicio no puede ser despues de la hora de fin",  null));
                                        FacesContext.getCurrentInstance().addMessage("FormRegi:horaFinaCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La hora de inicio no puede ser despues de la hora de fin",  null));
                                    }
                                }else{
                                    vali = true;
                                }
                            }else{
                                FacesContext.getCurrentInstance().addMessage("FormRegi:descCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe Especificar El Motivo",  null));
                            }
                        }else{
                            FacesContext.getCurrentInstance().addMessage("FormRegi:ubicCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe Seleccionar un Lugar",  null));
                        }
                    }else{
                        FacesContext.getCurrentInstance().addMessage("FormRegi:fech", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede programar una cita en una fecha que ya pasó",  null));
                    }
                }else{
                    FacesContext.getCurrentInstance().addMessage("frmMensGlob:mensTabl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ningun Visitante Agregado a la Cita",  null));
                }
            }else{
                FacesContext.getCurrentInstance().addMessage("FormRegi:hora", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El dia de la fecha no coincide con el horario disponible",  null));
                FacesContext.getCurrentInstance().addMessage("FormRegi:fech", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El dia de la fecha no coincide con el horario disponible",  null));
            }
        }else{
            if(ignoHoraDisp){
                FacesContext.getCurrentInstance().addMessage("FormRegi:horaInicCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe Registrar una Hora de Inicio",  null));
                FacesContext.getCurrentInstance().addMessage("FormRegi:horaFinaCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe egistrar una Hora de Finalización",  null));
            }else{
                FacesContext.getCurrentInstance().addMessage("FormRegi:hora", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe Seleccionar un Horario Disponible",  null));
            }
            FacesContext.getCurrentInstance().addMessage("FormRegi:fech", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe Registrar la Fecha de la Cita",  null));
        }
        return vali;
    }
    //LOG: cita programada por el usuario (personal de la institución)
    //programar cita, variable "padre" =  "true" -> padre de familia, "padre" = "false-> visitante particular
    public void progCitaUsua(boolean padre){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        
        try
        {
            if(valiDatoProgVisiUsua()){
                objeCita.setEstaCita(2);
                objeCita.setDescCita(motivo);
                if(padre){
                    objeCita.setTipoCita(1);
                }else{
                    objeCita.setTipoCita(3);
                }
                
                objeCita.setTipoDura(2);
                objeCita.setTipoVisi(2);
                objeCita.setCodiUsua(LoginBean.getObjeWSconsEmplByAcce().getCodi());
                //crear el objeto cita
                FCDECita.create(objeCita);

                objeCambCita.setCodiCita(objeCita);
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());
                objeCambCita.setFechCambCita(new Date());
                
                objeCambCita.setFechInicCitaNuev(fechSoliCita);
                objeCambCita.setFechFinCitaNuev(fechSoliCita);
                DateFormat df = new SimpleDateFormat("hh:mm a");
                objeCambCita.setHoraCambCita(df.format(new Date()));
                if(ignoHoraDisp){
                    objeCambCita.setHoraInicCitaNuev(FechInic);
                    objeCambCita.setHoraFinCitaNuev(FechFina);
                }else{
                    objeCambCita.setHoraInicCitaNuev(horaSeleCita.getHoraInicHoraDisp());
                    objeCambCita.setHoraFinCitaNuev(horaSeleCita.getHoraFinaHoraDisp());
                }
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());

                //crear el objeto cambio Cita
                FCDECambCita.create(objeCambCita);
                SimpleDateFormat sdfCita = new SimpleDateFormat("dd/MM/yyyy");
                if(padre){
                    //registrando visitantes desde lista AlumnoVisitante
                    for(Alumnovisitante visi : listVisiTemp){
                        objeVisiCita.setCodiCita(objeCita);
                        objeVisiCita.setCarnAlum(visi.getCarnAlum());
                        objeVisiCita.setCodiVisi(visi.getCodiVisi());
                        FCDEVisiCita.create(objeVisiCita);                        
                        try
                        {
                            
                            new EnvioCorreosBean().sendMail(visi.getCodiVisi().getCorrVisi(),
                            "Programación de Cita", "Se ha programado una cita para el día: " 
                                    + sdfCita.format(objeCambCita.getFechInicCitaNuev()) + " a las " 
                                    + objeCambCita.getHoraInicCitaNuev() + " en: " 
                                    + objeCambCita.getCodiCita().getCodiUbic().getNombUbic() +
                                    " con el empleado: " + new WebServicesBean().consEmplPorCodi(""+objeCita.getCodiUsua()).getNomb()
                                    + " Motivo: " + objeCita.getDescCita());
                        }
                        catch(Exception err){System.out.println("Error al enviar correo");}
                        try
                        {
                            int codiUsuaCons = FCDEUsua.findByAcce(visi.getCarnAlum()).getCodiUsua();
                            globalAppBean.addNotificacion(codiUsuaCons, "Se ha programado una cita para el día: " 
                                    + sdfCita.format(objeCambCita.getFechInicCitaNuev()) + " a las " 
                                    + objeCambCita.getHoraInicCitaNuev() + " en: " 
                                    + objeCambCita.getCodiCita().getCodiUbic().getNombUbic() 
                                    + " con el empleado: " + new WebServicesBean().consEmplPorCodi(""+objeCita.getCodiUsua()).getNomb()+
                                    " Motivo: " + objeCita.getDescCita(), "Citas y Visitas", null);
                        }
                        catch(Exception err) {System.out.println("Error al enviar notificación");}                        
                    }
                }else{
                    //registrando visitantes lista Visitante
                    for(Visitante visi : listVisiVisiTemp){
                        objeVisiCita.setCodiCita(objeCita);
                        objeVisiCita.setCodiVisi(visi);
                        FCDEVisiCita.create(objeVisiCita);
                        try
                        {
                            
                            new EnvioCorreosBean().sendMail(visi.getCorrVisi(),
                            "Programación de Cita", "Se ha programado una cita para el día: " 
                                    + sdfCita.format(objeCambCita.getFechInicCitaNuev()) + " a las " 
                                    + objeCambCita.getHoraInicCitaNuev() + " en: " 
                                    + objeCambCita.getCodiCita().getCodiUbic().getNombUbic() +
                                    " con el empleado: " + new WebServicesBean().consEmplPorCodi(""+objeCita.getCodiUsua()).getNomb()
                                    + " Motivo: " + objeCita.getDescCita());
                        }
                        catch(Exception err){System.out.println("Error al enviar correo");}
                    }
                }
                
                if(padre){
                    if(this.listCitaAlumUsua ==  null)this.listCitaAlumUsua = new ArrayList<Cita>();
                    this.listCitaAlumUsua.add(objeCita);
                }else{
                    if(this.listCitaVisiUsua ==  null)this.listCitaVisiUsua = new ArrayList<Cita>();
                    this.listCitaVisiUsua.add(objeCita);
                }
                this.limpForm();
                log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha programado una cita con visitante particulares");
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Programada');");
            }
            
        }
        catch(Exception ex)
        {
            log.error(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Error al programar cita ");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al Programar')");
            ex.printStackTrace();
        }
    }
    
    /*TERMINA SECCIÓN PARA CITAS (DOCENTE Y PERSONAL ADMIN.)*/
    
    
    
    /*SECCIÓN DESTINADA  PARA LA GESTIÓN DE VISITAS*/
    
    //consultar una lisata de visitas (las visitas no se hacen a usuarios)
    public void consListVisiUsua(){
        try{
            listVisiUsua = FCDECita.findAllVisi();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void consObjeVisiUsua(){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            limpForm();
            this.objeCita = FCDECita.find(codi);
            this.objeCambCita = FCDECambCita.findByCita(objeCita);//consultamos el ultimo cambio de cita
            if(objeCambCita == null) objeCambCita = new Cambiocita();
            this.fechSoliCita = objeCambCita.getFechInicCitaNuev();
            this.fechSoliCita2 = objeCambCita.getFechFinCitaNuev();
            this.FechInic = objeCambCita.getHoraInicCitaNuev();
            this.FechFina = objeCambCita.getHoraFinCitaNuev();
            consVisiCita(false);
            if(listVisiVisiTemp.size() == 1 && objeCita.getCantGrupCita() != null){
                isGrouVisi = true;
            }else {
                isGrouVisi = false;
            }
            if(objeCita.getCodiEven() == null){
                this.LugaEven = true;
            }else{
                this.LugaEven = false;
            }
            
            estaCita();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Consultada')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
    
    
    
    //usado para consultar un visitante en una cita, al seleccionar un AlumnoVisitante desde una tabla
    public void setVisi(){
        try{
            int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjeVisi"));
            this.objeVisi = FCDEVisi.find(codi);
            if(objeVisi == null) objeVisi = new Visitante();
            if(listVisiVisiTemp == null)listVisiVisiTemp = new ArrayList<Visitante>();
            
                RequestContext ctx = RequestContext.getCurrentInstance();
            if((isGrouVisi && listVisiVisiTemp.size() < 1) || !isGrouVisi){
                listVisiVisiTemp.add(objeVisi);
                //eliminamos posibles cambioCita duplicados
                HashSet<Visitante> hashSet = new HashSet<Visitante>(listVisiVisiTemp);
                listVisiVisiTemp.clear();
                listVisiVisiTemp.addAll(hashSet); 
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Visitante Agregado')");
            }else{
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Este grupo ya tiene un encargado')");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    //quitar un visitante de la lista temporal
    public void dropVisiVisi(Visitante obje){
        try
        {
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            this.listVisiVisiTemp.remove(obje);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Visitante Eliminado de la Cita')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    /* Metodo para validar los datos (fechas y horas)de la programacion de visitas
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    private boolean valiDatoProgVisi(){
        boolean vali = false;
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
       
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        try
        {
            if(listVisiVisiTemp.size() > 0){
                System.out.println(fechSoliCita+" = "+fechSoliCita2);
                //ambas fechas correctas
                if(this.fechSoliCita2.after(this.fechSoliCita)){
                    vali = true;
                //el mismo dia
                }else if(this.fechSoliCita2.equals(this.fechSoliCita)){
                    //es el mismo dia, pero con hora correcta
                    if(formatter.parse(this.FechFina).after(formatter.parse(this.FechInic))){
                        vali = true;
                    //mismo dia con hora incorrecta
                    }else{
                        FacesContext.getCurrentInstance().addMessage("FormRegi:horaInicCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hora Final no debe ser antes de la Inicial",  null));
                        FacesContext.getCurrentInstance().addMessage("FormRegi:horaFinaCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hora Final no debe ser antes de la Inicial",  null));
                        FacesContext.getCurrentInstance().addMessage("FormRegiVisiCita:horaFinaCita", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Esa hora ya pasó",  null));
                    }
                //ambas fechas incorrectas
                }else if(this.fechSoliCita2.before(this.fechSoliCita)){
                    FacesContext.getCurrentInstance().addMessage("FormRegi:fechInic", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fecha Final no debe ser antes de la Inicial",  null));
                    FacesContext.getCurrentInstance().addMessage("FormRegi:fechFina", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fecha Final no debe ser antes de la Inicial",  null));
                }else{
                    System.out.println("FUERA DE MI PODER");
                }
            }else{
                FacesContext.getCurrentInstance().addMessage("frmMensGlob:mensTabl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se a Registrado ningun visitante",  null));
            }
        }
        catch(Exception err)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Horas no válidas')");
            return false;
        }
        
        return vali;
    }
 
     /**
 * Metodo para programar visitas
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */
    //LOG: visita programada por un administrador
    public void progVisi(){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        
        try
        {
            if(valiDatoProgVisi()){
                objeCita.setEstaCita(2);
                objeCita.setDescCita(motivo);
                objeCita.setTipoCita(2);
                objeCita.setTipoDura(2);
                objeCita.setTipoVisi(2);
                //crear el objeto cita
                FCDECita.create(objeCita);

                objeCambCita.setCodiCita(objeCita);
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());
                objeCambCita.setFechCambCita(new Date());

                objeCambCita.setFechInicCitaNuev(fechSoliCita);
                objeCambCita.setFechFinCitaNuev(fechSoliCita2);
                DateFormat df = new SimpleDateFormat("hh:mm:a");
                objeCambCita.setHoraCambCita(df.format(new Date()));
                objeCambCita.setHoraInicCitaNuev(FechInic);
                objeCambCita.setHoraFinCitaNuev(FechFina);
                objeCambCita.setEstaCambCita(objeCita.getEstaCita());

                //crear el objeto cambio Cita
                FCDECambCita.create(objeCambCita);
                
                if(objeVisiCita == null)objeVisiCita = new Visitantecita();
                //registrando visitantes Cita
                for(Visitante visi : listVisiVisiTemp){
                    objeVisiCita.setCodiCita(objeCita);
                    objeVisiCita.setCodiVisi(visi);
                    FCDEVisiCita.create(objeVisiCita);
                    SimpleDateFormat sdfCita = new SimpleDateFormat("dd/MM/yyyy");
                    try
                    {
                        new EnvioCorreosBean().sendMail(visi.getCorrVisi(),
                        "Cambio de Cita", "Se ha programda una visita para el día: " 
                                + sdfCita.format(objeCambCita.getFechInicCitaNuev()) + " a las " 
                                + objeCambCita.getHoraInicCitaNuev() + " en: " 
                                + objeCambCita.getCodiCita().getCodiUbic().getNombUbic() +
                                " con el empleado: " + new WebServicesBean().consEmplPorCodi(""+objeCita.getCodiUsua()).getNomb()
                                + " Motivo: " + objeCita.getDescCita());
                    }
                    catch(Exception err){System.out.println("No se envio correo");}
                }
                if(this.listVisiUsua ==  null)this.listVisiUsua = new ArrayList<Cita>();
                this.listVisiUsua.add(objeCita);
                this.limpForm();
                log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha programado una cita ");
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Programada');");
            }
            
        }
        catch(Exception ex)
        {
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Error al programar una cita");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al Programar')");
            ex.printStackTrace();
        }
    }
    //cambios cita 
     /**
 * En este metodo se hara el cambio de visita (Por fechas y horas)
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     * @param acci cambio que se realiza (reprogramar ó cancelar)
     */
    //LOG: cambio a visita realizado (reprogramada)
    // esta: (1) reprogramar, (2) cancelar
    public void cambVisi(int acci){
        RequestContext ctx = RequestContext.getCurrentInstance();
        
        try
        {
            List<Visitantecita> listVisiCitaLoca = FCDEVisiCita.findByCodiCita(objeCita);
            String accion="";
            if(valiDatoProgVisi() || acci == 2){
                    switch(acci){
                        case 1:
                            accion=" reprogramada " ;
                            objeCita.setEstaCita(2);
                        break;
                        case 2:
                            accion=" cancelada " ;
                            objeCita.setEstaCita(0);
                        break;
                    }
                    //listCambCita.remove(objeCambCita);
                    listVisiUsua.remove(objeCita);

                    DateFormat df = new SimpleDateFormat("hh:mm a");
                    objeCambCita.setHoraCambCita(df.format(new Date()));
                    
                    objeCambCita.setFechCambCita(new Date());
                    if(acci ==1){
                        objeCambCita.setFechInicCitaNuev(fechSoliCita);
                        objeCambCita.setFechFinCitaNuev(fechSoliCita2);
                        objeCambCita.setHoraInicCitaNuev(FechInic);
                        objeCambCita.setHoraFinCitaNuev(FechFina);
                        objeCambCita.setMotiCambCita(motivo);
                    }
                    objeCambCita.setCodiCita(objeCita);
                    objeCambCita.setEstaCambCita(objeCita.getEstaCita());
                    FCDECita.edit(objeCita);
                    FCDECambCita.create(objeCambCita);
                    listVisiUsua.add(objeCita);
                    log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Se ha reprogramada la cita ");
                    switch(acci){
                        case 1:
                            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Visita Reprogramada'); $('#ModaFormRegi').modal('hide');");
                        break;
                        case 2:
                            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Visita Cancelada'); $('#ModaFormRegi').modal('hide');");
                        break;
                    }
                    for(Visitantecita objeVisiCitaLoca : listVisiCitaLoca)
                    {
                        SimpleDateFormat sdfCita = new SimpleDateFormat("dd/MM/yyyy");
                        try
                        {
                            if(acci==1)
                            {
                                new EnvioCorreosBean().sendMail(objeVisiCitaLoca.getCodiVisi().getCorrVisi(),
                                "Cambio de Cita", "Se ha reprogramado una visita para el día: " 
                                        + sdfCita.format(objeCambCita.getFechInicCitaNuev()) + " a las " 
                                        + objeCambCita.getHoraInicCitaNuev() + " en: " 
                                        + objeCambCita.getCodiCita().getCodiUbic().getNombUbic() +
                                        " con el empleado: " + new WebServicesBean().consEmplPorCodi(""+objeCita.getCodiUsua()).getNomb()
                                        + " Motivo: " + objeCita.getDescCita());
                            }
                        }
                        catch(Exception err){System.out.println("No se envio correo");}
                    }
                    estaCita();
                    limpForm();
            }
            
        }
        catch(Exception ex)
        {
            log.info(this.logiBean.getObjeUsua().getCodiUsua()+"-"+"Citas"+"-"+"Error al realizar la accion ");
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al realizar la acción')");
            ex.printStackTrace();
        }
    }
    
    
    /*TERMINA SECCIÓN DESTINADA  PARA LA GESTIÓN DE VISITAS*/
    public void prueba(){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
       
        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'MENSAJE');");
        
    }
    
    private void consUsuaByCrit()
    {
        this.listDoceBusc = new ArrayList<DatosUsuariosByCrit>();
        List<DatosUsuariosByCrit> temp = new ArrayList<DatosUsuariosByCrit>();
        temp = new WebServicesBean().consEmplPorParam(this.buscEmpl, this.buscEmpl, this.tipoEmpl).getResu();
        if(temp!=null)
        {            
            for(DatosUsuariosByCrit dato : temp)
            {
                if(!dato.getTipo().equals("emplRece") && !dato.getTipo().equals("alum"))
                {
                    this.listDoceBusc.add(dato);
                }
            }
        }
    }
    
    public String consCodiUsuaByAcce(String acce)
    {
        return String.valueOf(new WebServicesBean().consEmplByUser(acce).getCodi());
    }
    
    private List<Visitante> listVisiExce;
    private Part file;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
    

    public List<Visitante> getListVisiExce() {
        return listVisiExce;
    }

    public void setListVisiExce(List<Visitante> listVisiExce) {
        this.listVisiExce = listVisiExce;
    }
    
    public void impoListExce()
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
                    this.listVisiVisiTemp.add(objeVisiTemp);
                }
                else
                {
                    contGuar++;
                    this.listVisiVisiTemp.add(FCDEVisi.findByDuiVisi(objeVisiTemp.getDuiVisi()));
                }
            }
            this.toggSubSwitFormCita();
            this.listVisiExce=new ArrayList<Visitante>();
            if(contGuar==0) ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Ningún Visitante Asignado')");
            if(contGuar == 1) ctx.execute("setMessage('MESS_SUCC', 'Atención', '1 Visitante Asignado')");
            if(contGuar > 1) ctx.execute("setMessage('MESS_SUCC', 'Atención', '"+contGuar+" Visitantes Asignados')");
        }
    }
    
    public void dropVisiDocuExce(Visitante obje){
        if(this.listVisiExce == null) this.listVisiExce = new ArrayList<Visitante>();
        if(this.listVisiExce.contains(obje)){
            this.listVisiExce.remove(obje);
            RequestContext ctx = RequestContext.getCurrentInstance();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Visitante Eliminado la lista')");
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
}
