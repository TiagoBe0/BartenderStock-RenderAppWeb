package com.proyecto.demo.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Brief {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombreEmpresa;
    private String nombre;
    private String celular;
    private String historia;
    private String cuit;
    private String mail;
    private String objetivos;
    private boolean activo;
    private String objetivosCorto;
    private String adjetivos;
    private String servicios;
    private String publicoObjetivo;
    private String inversiones;
    private String modelosAseguir;
    private String competencia;
    private String debilidades;

    public String getObjetivosCorto() {
        return objetivosCorto;
    }

    public void setObjetivosCorto(String objetivosCorto) {
        this.objetivosCorto = objetivosCorto;
    }

    public String getAdjetivos() {
        return adjetivos;
    }

    public void setAdjetivos(String adjetivos) {
        this.adjetivos = adjetivos;
    }

    public String getServicios() {
        return servicios;
    }

    public void setServicios(String servicios) {
        this.servicios = servicios;
    }

    public String getPublicoObjetivo() {
        return publicoObjetivo;
    }

    public void setPublicoObjetivo(String publicoObjetivo) {
        this.publicoObjetivo = publicoObjetivo;
    }

    public String getInversiones() {
        return inversiones;
    }

    public void setInversiones(String inversiones) {
        this.inversiones = inversiones;
    }

    public String getModelosAseguir() {
        return modelosAseguir;
    }

    public void setModelosAseguir(String modelosAseguir) {
        this.modelosAseguir = modelosAseguir;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public String getDebilidades() {
        return debilidades;
    }

    public void setDebilidades(String debilidades) {
        this.debilidades = debilidades;
    }
    
    
    

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    
    
    
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

}
