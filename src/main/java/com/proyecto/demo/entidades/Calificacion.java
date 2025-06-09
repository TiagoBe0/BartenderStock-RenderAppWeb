
package com.proyecto.demo.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Calificacion {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombreEmpresa;
    private int estrellasConformidad;
    private int estrellasTiempo;
    private int estrellasAtencion;
    private String comentarios;
    private boolean activa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public int getEstrellasConformidad() {
        return estrellasConformidad;
    }

    public void setEstrellasConformidad(int estrellasConformidad) {
        this.estrellasConformidad = estrellasConformidad;
    }

    public int getEstrellasTiempo() {
        return estrellasTiempo;
    }

    public void setEstrellasTiempo(int estrellasTiempo) {
        this.estrellasTiempo = estrellasTiempo;
    }

    public int getEstrellasAtencion() {
        return estrellasAtencion;
    }

    public void setEstrellasAtencion(int estrellasAtencion) {
        this.estrellasAtencion = estrellasAtencion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
    
    
    
    
}
