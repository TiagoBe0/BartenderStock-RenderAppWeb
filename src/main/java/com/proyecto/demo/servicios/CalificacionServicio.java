
package com.proyecto.demo.servicios;

import com.proyecto.demo.entidades.Calificacion;
import com.proyecto.demo.repositorios.CalificacionRepositorio;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalificacionServicio {
    
    
    
    @Autowired
    private CalificacionRepositorio calificacionRepositorio;
        
    
    @Transactional
    public void registrar(String nombreEmpresa,int estrellasConformidad,int estrellasAtencion,int estrellasTiempo,String comentarios){
            
        
        Calificacion c = new Calificacion();
        c.setActiva(true);
        c.setComentarios(comentarios);
        c.setEstrellasAtencion(estrellasAtencion);
        c.setEstrellasConformidad(estrellasConformidad);
        c.setEstrellasTiempo(estrellasTiempo);
        c.setNombreEmpresa(nombreEmpresa);
        
        calificacionRepositorio.save(c);
    
    }
    
    
    @Transactional
    public List<Calificacion> getAll(){
    return calificacionRepositorio.findAll();
    }
}
