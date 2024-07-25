/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

/**
 *
 * @author walyn
 */
public class SingletonDatosUsuario {
    // Variable estática que contiene la única instancia de la clase
    private static SingletonDatosUsuario instance;

    // Variable para almacenar la instancia de DatosUsuario
    private DatosUsuario datosUsuario;

    // Constructor privado para prevenir la creación de objetos desde fuera de la clase
    private SingletonDatosUsuario() { }

    // Método estático que proporciona acceso a la única instancia
    public static SingletonDatosUsuario getInstance() {
      
            if (instance == null) {
                instance = new SingletonDatosUsuario();
            }
        
        return instance;
    }

    // Método para obtener la instancia de DatosUsuario
    public DatosUsuario getDatosUsuario() {
        return datosUsuario;
    }

    // Método para establecer la instancia de DatosUsuario
    public void setDatosUsuario(DatosUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
    }
    
}
