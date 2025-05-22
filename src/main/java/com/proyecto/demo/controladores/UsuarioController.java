package com.proyecto.demo.controladores;

import com.proyecto.demo.errores.ErrorServicio;
import com.proyecto.demo.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/usuario")
public class UsuarioController {

   
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    
    
    
    //registro usuario 
    
     @PostMapping("/registrar")
    public String registrar( ModelMap modelo,MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) {
       
        try {
            usuarioServicio.registrar(archivo, nombre, apellido, mail, clave1, clave2);
        } catch (ErrorServicio ex) {
           
            return "index.html";
        }
        modelo.put("titulo", "Bienvenido a Tinder de Mascotas");
        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria");
        return "exito.html";
    } 
    
    
     public String inicioUsuario(ModelMap modelo) {
        
        
        return  "inicioUsuario.html";
    }
    
    
}//llave de clase
