
package com.proyecto.demo.controladores;

import com.proyecto.demo.errores.ErrorServicio;
import com.proyecto.demo.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    
    @GetMapping("/registro")
    public String registro(){
    
    return "render-registroCliente.html";
    }
    
    
   
     @PostMapping("/registrar")
    public String registrar(ModelMap modelo,   String nombre , String telefono, String descripcion,String idUsuario,int orden) throws ErrorServicio {
        
        
         
            clienteServicio.registrar(nombre, telefono, descripcion, idUsuario, orden);
       
        return "render-registroCliente.html";
    }
    
}
