/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.demo.entidades;

import java.io.Serializable;

public class UsuarioSesionDTO implements Serializable { // Importante para la sesión
    private Long id;
    private String nombre;
    private String email;

    public UsuarioSesionDTO(Long id, String nombre, String email /*, byte[] fotoPerfilData */) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        // this.fotoPerfilData = fotoPerfilData;
    }

    // Getters y Setters
}