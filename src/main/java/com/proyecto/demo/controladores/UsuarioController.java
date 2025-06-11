package com.proyecto.demo.controladores;


import com.proyecto.demo.entidades.Usuario;
import com.proyecto.demo.errores.ErrorServicio;
import com.proyecto.demo.servicios.CalificacionServicio;
import com.proyecto.demo.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

     @Autowired
    private CalificacionServicio calificacionServicio;
    
    
    //CONTROLADORES RAHIP WEB
    
   
       @GetMapping("/inicioUsuariosd")
    public String inicioUsuario(ModelMap modelo) {
        return  "render-inicioUsuario.html";
    }
       @GetMapping("/inicioUsuario")
    public String tabla(ModelMap modelo){
   
    modelo.put("calificaciones", calificacionServicio.getAll());
    return "tablaCalificanos.html";
    }
    
    
    
    
    
    
    
    

    //REGISTRO USUARIO ADMIN
       @PostMapping("/registrar")
    public String registrarAdmin( ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) {
        
        try {
            usuarioServicio.registrar( nombre, apellido, mail, clave1, clave2);
        } catch (ErrorServicio ex) {
           
            return "index.html";
        }
        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria");
        return "exito.html";
    } 
    
    
    
    
            //Este es el que llega a crear barra
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/tablaBrief")
    public String tablaBrief(HttpSession session, @RequestParam String id,String nombre, ModelMap model) throws ErrorServicio {
        
        

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            
            model.addAttribute("perfil", usuario);
            
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }
        return "tablaBrief.html";}
    
    
               //Este es el que llega a crear barra
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/tablaCalificanos")
    public String tablaCalificanos(HttpSession session, @RequestParam String id,String nombre, ModelMap model) throws ErrorServicio {
        
        

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            
            model.addAttribute("perfil", usuario);
            
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }
        return "tablaCalificanos.html";}
    
    
    
    
}//llave de clase
