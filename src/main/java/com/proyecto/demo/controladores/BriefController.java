
package com.proyecto.demo.controladores;

import com.proyecto.demo.servicios.BriefServicio;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/brief")
public class BriefController {
    
    @Autowired
    private BriefServicio briefServicio;
    
    @GetMapping("/registro")
    public String vistaRegistro(){
    
    return "registroBrief.html";
    }
    
    
@PostMapping("/registro")
public String formularioRegistro(String mail,String nombreEmpresa,String nombre,String celular,String historia,String cuit,String objetivos,String objetivosCorto,String adjetivos,String servicios,String publicoObjetivo,String inversiones, String modelosAseguir,String competencia,String debilidades){
        
    try {
        // Intentamos registrar los datos
        briefServicio.registrar(mail, nombreEmpresa, nombre, celular, historia, cuit, objetivos, objetivosCorto, adjetivos, servicios, publicoObjetivo, inversiones, modelosAseguir, competencia, debilidades);
        return "registroBrief.html";
    } catch (Exception e) {
        // Imprimimos en consola para debug
        System.err.println("Error al registrar el brief: " + e.getMessage());
        
        // Podemos enviar también atributos con el error al modelo si querés mostrarlo
        return "registroBrief.html";
    }
}

    @GetMapping("/tabla")
    public String tablaBrief(ModelMap modelo){
        
        modelo.put("briefs",briefServicio.getAll());
    
    return "tablaBrief.html";
    }
    


    
}



