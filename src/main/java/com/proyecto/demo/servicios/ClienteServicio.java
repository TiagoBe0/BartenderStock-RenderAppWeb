
package com.proyecto.demo.servicios;

import com.proyecto.demo.entidades.Cliente;
import com.proyecto.demo.entidades.Usuario;
import com.proyecto.demo.errores.ErrorServicio;
import com.proyecto.demo.repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    
   @Transactional 
    public void registrar(String nombre , String telefono, String descripcion,String idUsuario,int orden) throws ErrorServicio{
            
            Cliente c = new Cliente();
            c.setActiva(true);
            c.setDescripcion(descripcion);
            c.setIdUsuario(idUsuario);
            Usuario usuario = usuarioServicio.buscarPorId(idUsuario);
            List<Cliente> clientes = usuario.getClientes();
            
            c.setNumeroOrden(0);
            clientes.add(c);
            clienteRepositorio.save(c);
            usuarioServicio.repositorioSave(usuario);
    
    
    }
    
    public List<Cliente> getAll(){
    
    return clienteRepositorio.findAll();
    
    }
}
