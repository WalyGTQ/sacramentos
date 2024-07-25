/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

/**
 *
 * @author walyn
 */
public class Usuario {

public String nombre;
public int pass;

    // Constructor
    public Usuario(String nombre, int pass) {
        this.nombre = nombre;
        this.pass = pass;
    }

    // Getter para 'nombre'
    public String getNombre() {
        return nombre;
    }

    // Setter para 'nombre'
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
        // Getter para 'pass'
    public int getPass() {
        return pass;
    }

    // Setter para 'pass'
    public void setPass(int pass) {
        this.pass = pass;
    }
    
    
}
