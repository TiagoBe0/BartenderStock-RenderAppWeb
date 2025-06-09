
package com.proyecto.demo.repositorios;

import com.proyecto.demo.entidades.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionRepositorio extends JpaRepository<Calificacion,String>{
    
}
