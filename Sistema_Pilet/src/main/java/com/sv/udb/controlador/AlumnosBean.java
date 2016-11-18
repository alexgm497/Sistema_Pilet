package com.sv.udb.controlador;
import com.sv.udb.modelo.Alumno;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;


 /**
 * La clase alumnos
 * @author: ControlCitas
 * @version: Prototipo 1
 * Septiembre  2016
 */
    

@Named(value = "alumnosBean")
@Dependent
public class AlumnosBean {
   
    public AlumnosBean() {
    }
    public List<Alumno> consTodoAlum(){
        List<Alumno> listAlum = new ArrayList<Alumno>();
        //listAlum.add(new Alumno("Carnet","Nombre","Apellido","Nivel","Especialidad","Grupo Técnico","Grupo Académico"));
        listAlum.add(new Alumno("20130670","Kevin","Guevara","Primer Año","Informática","3-A","A-5"));
        listAlum.add(new Alumno("20130732","Alvin","Baires","Tercer Año","Informática","2-A","A-3"));
        listAlum.add(new Alumno("20130698","Keneth","Cruz","Primer Año","Arquitectura","3-b","b-2"));
        listAlum.add(new Alumno("20130735","Alba","Urrutia","Octavo Grado","","","B"));
        listAlum.add(new Alumno("20130736","Adrián","Ramírez","Septimo Grado","","","D"));
        return listAlum;
    }
    
}
