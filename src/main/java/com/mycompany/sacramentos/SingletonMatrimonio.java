/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

/**
 *
 * @author walyn
 */
public class SingletonMatrimonio {
    
    // Instancia única de SingletonMatrimonio
    private static SingletonMatrimonio instance;

    // Datos que deseas guardar
    private ConsultaMatrimonio feligresDetalle;

    // Constructor privado para prevenir la instanciación desde clases externas
    private SingletonMatrimonio() { }

    // Método público para obtener la instancia única
    public static SingletonMatrimonio getInstance() {
        if (instance == null) {
            instance = new SingletonMatrimonio();
        }
        return instance;
    }

    // Getter y Setter para feligresDetalle
    public ConsultaMatrimonio getFeligresDetalle() {
        return feligresDetalle;
    }

    public void setFeligresDetalle(ConsultaMatrimonio feligresDetalle) {
        this.feligresDetalle = feligresDetalle;
    }
}
