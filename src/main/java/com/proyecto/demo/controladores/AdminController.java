package com.proyecto.demo.controladores;

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
import com.proyecto.demo.servicios.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired 
        UsuarioServicio usuarioServicio;
       
	@GetMapping("/dashboard")
	public String inicioAdmin(ModelMap modelo) {
		
                
                
                
		
		return "inicioAdmin";
	}
	
	
}
