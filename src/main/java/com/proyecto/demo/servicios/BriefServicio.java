
package com.proyecto.demo.servicios;

import com.proyecto.demo.entidades.Brief;
import com.proyecto.demo.repositorios.BriefRepositorio;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class BriefServicio {
    
    @Autowired
    private BriefRepositorio briefRepositorio;
    
    
    @Transactional
    public void registrar(String mail,String nombreEmpresa,String nombre,String celular,String historia,String cuit,String objetivos){
        
        Brief brief = new Brief();
        brief.setMail(mail);
        brief.setCelular(celular);
        brief.setNombreEmpresa(nombreEmpresa);
        brief.setNombre(nombre);
        brief.setHistoria(historia);
        brief.setCuit(cuit);
        brief.setActivo(true);
        briefRepositorio.save(brief);
               
    
    }
    
    
    @Transactional
    public List<Brief> getAll(){
    
    return briefRepositorio.findAll();
    }
    
}
