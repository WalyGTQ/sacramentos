/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

/**
 *
 * @author walyn
 */
public class SingletonPrimeraComunion {
        // Instancia única de la clase
    private static SingletonPrimeraComunion instance;

    // Aquí  ConsultaPrimeraComunion es una clase. 
    // Si no es así, reemplaza por el tipo de dato adecuado.
    private ConsultaPrimeraComunion feligresDetallePc;

    // Constructor privado para evitar que se creen nuevas instancias
    private SingletonPrimeraComunion() {}

    // Método para obtener la única instancia permitida de la clase
    public static SingletonPrimeraComunion getInstance() {
        if (instance == null) {
            instance = new SingletonPrimeraComunion();
        }
        return instance;
    }

    // Getter y setter para feligresDetalle
    public ConsultaPrimeraComunion getFeligresDetalle() {
        return feligresDetallePc;
    }

    public void setFeligresDetalle(ConsultaPrimeraComunion feligresDetalle) {
        this.feligresDetallePc = feligresDetalle;
    }
    
}
