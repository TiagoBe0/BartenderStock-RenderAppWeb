
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
    public void registrar(String mail,String nombreEmpresa,String nombre,String celular,String historia,String cuit,String objetivos,String objetivosCorto,String adjetivos,String servicios,String publicoObjetivo,String inversiones, String modelosAseguir,String competencia,String debilidades){
        
        Brief brief = new Brief();
        brief.setMail(mail);
        brief.setCelular(celular);
        brief.setNombreEmpresa(nombreEmpresa);
        brief.setNombre(nombre);
        brief.setHistoria(historia);
        brief.setCuit(cuit);
        brief.setActivo(true);
        brief.setAdjetivos(adjetivos);
        brief.setCompetencia(competencia);
        brief.setObjetivosCorto(objetivosCorto);
        brief.setPublicoObjetivo(publicoObjetivo);
        brief.setServicios(servicios);
        brief.setDebilidades(debilidades);
        brief.setInversiones(inversiones);
        brief.setModelosAseguir(modelosAseguir);
        
        briefRepositorio.save(brief);
               
    
    }
    
    
    @Transactional
    public List<Brief> getAll(){
    
    return briefRepositorio.findAll();
    }
    
}
