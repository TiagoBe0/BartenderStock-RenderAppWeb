package com.proyecto.demo.controladores;

import com.proyecto.demo.entidades.Barra;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;		
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.demo.entidades.Usuario;
import com.proyecto.demo.errores.ErrorServicio;
import com.proyecto.demo.servicios.BarraServicio;
import com.proyecto.demo.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.DeleteMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired 
        UsuarioServicio usuarioServicio;
        @Autowired 
        BarraServicio barraServicio;
	
	@GetMapping("/dashboard")
	public String inicioAdmin(ModelMap modelo) {
		
		List<Usuario> usuarios = usuarioServicio.todosLosUsuarios();
		List<Barra> barras = barraServicio.listarTodas();
		modelo.put("usuarios", usuarios);
                modelo.put("usuarioss", barras);
                
                
                
		
		return "render-inicioAdmin.html";
	}
	
	@GetMapping("/habilitar/{id}")
	public String habilitar(ModelMap modelo, @PathVariable String id) {
		try {
			usuarioServicio.habilitar(id);
			return "redirect:/admin/dashboard";
		}catch(Exception e) {
			modelo.put("error", "No fue posible habilitar");
			return "inicioAdmin";
		}
	}
	
	@GetMapping("/deshabilitar/{id}")
	public String deshabilitar(ModelMap modelo, @PathVariable String id) {
		try {
			usuarioServicio.deshabilitar(id);
			return "redirect:/admin/dashboard";
		}catch(Exception e) {
			modelo.put("error", "No fue posible deshabilitar");
			return "inicioAdmin";
		}
	}
	
	@GetMapping("/cambiar_rol/{id}")
	public String cambiarRol(ModelMap modelo, @PathVariable String id) {
		try {
			usuarioServicio.cambiarRol(id);
			modelo.put("exito", "Rol modificado con Exito!");
			return "redirect:/admin/dashboard";
		}catch(Exception e) {
			modelo.put("error", "No fue posible reasignar el rol");
			return "inicioAdmin";
		}
	}
        
        
        //POST BORRAR TODA LA BASE DE DATOS
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/borrar-todo/{id}")
    public String borrarTodo( ModelMap modelo,  @PathVariable String id ) throws ErrorServicio {
       
        modelo.put("perfil",usuarioServicio.buscarPorId(id));
         
         
         return "index_app_inicio.html";
      
    }
	
	
}
