
package com.proyecto.demo.controladores;

import com.proyecto.demo.servicios.CalificacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calificacion")
public class CalificacionController {
    
    
    
    @Autowired
    private CalificacionServicio calificacionServicio;
    
    
    @GetMapping("/registro")
    public String vistaRegistro(){
   
    
    return "registroCalificacion.html";
    }
    
      @GetMapping("/tabla")
    public String tabla(ModelMap modelo){
   
    modelo.put("calificaciones", calificacionServicio.getAll());
    return "tablaCalificacion.html";
    }
    
        @PostMapping("/registro")
    public String formulario(String nombreEmpresa,int estrellasConformidad,int estrellasAtencion,int estrellasTiempo,String comentarios){
   
        calificacionServicio.registrar(nombreEmpresa, estrellasConformidad, estrellasAtencion, estrellasTiempo, comentarios);
    
    return "registroCalificacion.html";
    }
}
