/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

/**
 *
 * @author walyn
 */
public class SingletonConfirmacion {
    
    
    private static SingletonConfirmacion instance; // Instancia única de la clase

    private ConsultaConfirmacion feligresDetalle;  // Atributo para guardar el objeto ConsultaConfirmacion

    // Constructor privado para asegurar una única instancia
    private SingletonConfirmacion() {}

    // Método para obtener la única instancia de la clase
    public static SingletonConfirmacion getInstance() {
        if (instance == null) {
            instance = new SingletonConfirmacion();
        }
        return instance;
    }

    // Getter y setter para el objeto ConsultaConfirmacion
    public ConsultaConfirmacion getFeligresDetalle() {
        return feligresDetalle;
    }

    public void setFeligresDetalle(ConsultaConfirmacion feligresDetalle) {
        this.feligresDetalle = feligresDetalle;
    }
    
}
