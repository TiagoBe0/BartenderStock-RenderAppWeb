
package com.proyecto.demo.repositorios;

import com.proyecto.demo.entidades.Brief;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BriefRepositorio extends JpaRepository<Brief,String > {
    
}
