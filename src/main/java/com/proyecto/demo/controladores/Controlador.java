package com.proyecto.demo.controladores;

import com.proyecto.demo.entidades.Usuario;
import com.proyecto.demo.errores.ErrorServicio;
import com.proyecto.demo.servicios.UsuarioServicio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class Controlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
        
    
    
        @GetMapping("/")
    public String index(ModelMap modelo) {
        return "index.html";
    }
    
    
        
    
        @GetMapping("/calificanos")
    public String calificanos(){
        return "calificanos.html";
    }
    @GetMapping("/nosotros")
    public String nosotros(){
        return "nosotros.html";
    }
        @GetMapping("/servicios")
    public String servicios(){
        return "servicios.html";
    }
        @GetMapping("/brief")
    public String brief(){
        return "brief.html";
    }
      @GetMapping("/contacto")
    public String contacto(){
        return "contacto.html";
    }
    
    
     @GetMapping("/registro")
    public String registro(){
        return "render-registro.html";
    }
    
    @GetMapping("/panel-rahip")
    public String panel(){
        return "panel-rahip.html";
    }
    
    
    
    //CONTROLADORES VIEJOS DE AQUI ABAJO
    
    
    
    
    
    
    
    
    
    
    
    
    
    @GetMapping("/render")
    public String inde_test_render(){
    
    return "index_render.html";
    }
     @GetMapping("/render-index")
    public String index_test(){
    
    return "index.html";
    }
    
    
    
    


    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio-render")
    public String inicio() {
        return "inicio.html";
    }

    @GetMapping("/login-render")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "render-login.html";
    }


    
    @GetMapping("/formularioBarra")
    public String form(ModelMap modelo) {
        
        
        return "formularioBarra.html";
    }
  

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo) {
    	
    	
        return "index_app.html";
    }

    @GetMapping("/loginUsuarioModelo")
    public String loginn(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "loginUsuario1.html";
    }
     @GetMapping("/loginUsuarioControlador")
    public String loginUser() {
        
        return "loginUsuario1.html";
    }

    @GetMapping("/registrol")
    public String registrol() {
        
        
        return "registroUsuario.html";
    }

  

}
