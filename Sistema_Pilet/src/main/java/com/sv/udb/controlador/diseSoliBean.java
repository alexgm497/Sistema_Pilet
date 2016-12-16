/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.OpcionFacadeLocal;
import com.sv.udb.ejb.OpcionRespuestaFacadeLocal;
import com.sv.udb.ejb.PreguntaFacadeLocal;
import com.sv.udb.modelo.Empresa;
import com.sv.udb.modelo.Estructura;
import com.sv.udb.modelo.Opcion;
import com.sv.udb.modelo.OpcionRespuesta;
import com.sv.udb.modelo.Pregunta;
import com.sv.udb.modelo.Seccion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eduardo
 */
@Named(value = "diseSoliBean")
@ViewScoped
public class diseSoliBean implements Serializable{

    @EJB
    private OpcionRespuestaFacadeLocal FCDEOpciResp;

    @EJB
    private OpcionFacadeLocal FCDEOpci;

    @EJB
    private PreguntaFacadeLocal FCDEPreg;
    
    private Seccion objeSecc;
    private Pregunta objePreg;
    private Opcion objeOpci;
    private int opcion;
    private boolean uno, dos;
    private OpcionRespuesta objeOpciResp;
    private List<OpcionRespuesta> listOpci;

    public OpcionRespuesta getObjeOpciResp() {
        return objeOpciResp;
    }

    public void setObjeOpciResp(OpcionRespuesta objeOpciResp) {
        this.objeOpciResp = objeOpciResp;
    }

    public boolean isUno() {
        return uno;
    }

    public void setUno(boolean uno) {
        this.uno = uno;
    }

    public boolean isDos() {
        return dos;
    }

    public void setDos(boolean dos) {
        this.dos = dos;
    }

    public int getOpcion() {
        return opcion;
    }

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }
    
    public Opcion getObjeOpci() {
        return objeOpci;
    }

    public void setObjeOpci(Opcion objeOpci) {
        this.objeOpci = objeOpci;
    }

    public Pregunta getObjePreg() {
        return objePreg;
    }

    public void setObjePreg(Pregunta objePreg) {
        this.objePreg = objePreg;
    }

    public Seccion getObjeSecc() {
        return objeSecc;
    }

    public void setObjeSecc(Seccion objeSecc) {
        this.objeSecc = objeSecc;
    }
    
    /**
     * Creates a new instance of diseSoliBean
     */
    public diseSoliBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeSecc = new Seccion();
        this.objePreg = new Pregunta();
        this.objeOpci = new Opcion();
        this.opcion = 1;
        this.uno = true;
        this.dos = true;
        this.objeOpciResp = new OpcionRespuesta(); 
    }
    
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            switch (this.opcion) {
                case 1:
                    //Seteamos estado 1 a la pregunta y opcion
                    this.objeOpci.setEstaOpci(1);
                    this.objePreg.setEstaPreg(1);
                    System.out.println(this.objeSecc.getCodiSecc());
                    this.objePreg.setCodiSecc(objeSecc);
                    this.FCDEPreg.create(objePreg);
                    this.objePreg = this.FCDEPreg.findLast();
                    this.objeOpci.setCodiPreg(objePreg);
                    Estructura es = new Estructura(); 
                    es.setCodiEstr(this.opcion);
                    this.objeOpci.setCodiEstr(es);
                    this.FCDEOpci.create(objeOpci);
                    break;
                case 2:
                    //Seteamos estado 1 a la pregunta y opcion
                    this.objeOpci.setEstaOpci(1);
                    this.objePreg.setEstaPreg(1);
                    this.objePreg.setCodiSecc(objeSecc);
                    this.FCDEPreg.create(objePreg);
                    this.objePreg = this.FCDEPreg.findLast();
                    this.objeOpci.setCodiPreg(objePreg);
                    Estructura ess = new Estructura(); 
                    ess.setCodiEstr(this.opcion);
                    this.objeOpci.setCodiEstr(ess);
                    this.FCDEOpci.create(objeOpci);
                    break;
                case 3:
                    //Seteamos estado 1 a la pregunta y opcion
                    this.objeOpci.setEstaOpci(1);
                    this.objePreg.setEstaPreg(1);
                    this.objePreg.setCodiSecc(objeSecc);
                    this.FCDEPreg.create(objePreg);
                    this.objePreg = this.FCDEPreg.findLast();
                    this.objeOpci.setCodiPreg(objePreg);
                    Estructura est = new Estructura(); 
                    est.setCodiEstr(this.opcion);
                    this.objeOpci.setCodiEstr(est);
                    this.objeOpci.setTituOpci("Seleccione: ");
                    this.objeOpci.setDescOpci("No se xd");
                    this.FCDEOpci.create(objeOpci);
                    this.objeOpci = this.FCDEOpci.findLast();
                    for(OpcionRespuesta list : listOpci)
                    {
                        list.setCodiOpci(objeOpci);
                        list.setEstaOpci(1);
                        this.FCDEOpciResp.create(list);
                    }
                    break;
                case 4:
                    //Seteamos estado 1 a la pregunta y opcion
                    this.objeOpci.setEstaOpci(1);
                    this.objePreg.setEstaPreg(1);
                    this.objePreg.setCodiSecc(objeSecc);
                    this.FCDEPreg.create(objePreg);
                    this.objePreg = this.FCDEPreg.findLast();
                    this.objeOpci.setCodiPreg(objePreg);
                    Estructura estu = new Estructura(); 
                    estu.setCodiEstr(this.opcion);
                    this.objeOpci.setCodiEstr(estu);
                    this.objeOpci.setTituOpci("Seleccione los necesarios: ");
                    this.objeOpci.setDescOpci("No se xd");
                    this.FCDEOpci.create(objeOpci);
                    this.objeOpci = this.FCDEOpci.findLast();
                    for(OpcionRespuesta list : listOpci)
                    {
                        list.setCodiOpci(objeOpci);
                        list.setEstaOpci(1);
                        this.FCDEOpciResp.create(list);
                    }
                    break;
                default:
                    break;
            }
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            this.init();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
        }
        
    }
    
    
    public void vali()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            switch (this.opcion) {
                case 1:
                    this.uno = true;
                    this.dos = true;
                    System.out.println("Simple");
                    break;
                case 2:
                    this.uno = true;
                    this.dos = false;
                    System.out.println("Area");
                    break;
                case 3:
                    this.uno = false;
                    this.dos = true;
                    System.out.println("Select");
                    break;
                case 4:
                    this.uno = false;
                    this.dos = false;
                    System.out.println("Checkbox");
                    break;
                default:
                    break;
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
        }
        
    }
    
    public void agreList()
    {
        this.objeOpciResp.setEstaOpci(1);
        if(this.listOpci == null)
            {
                this.listOpci = new ArrayList<>();
            }
        this.listOpci.add(objeOpciResp);
        this.objeOpciResp = new OpcionRespuesta();
    }
    
}
