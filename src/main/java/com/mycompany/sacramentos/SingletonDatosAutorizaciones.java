/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

/**
 *
 * @author walyn
 */
public class SingletonDatosAutorizaciones {
    private static SingletonDatosAutorizaciones instance;
    private DatosAutorizaciones datosAutorizaciones;

    private SingletonDatosAutorizaciones() {}

    public static SingletonDatosAutorizaciones getInstance() {
        if (instance == null) {
            instance = new SingletonDatosAutorizaciones();
        }
        return instance;
    }

    public DatosAutorizaciones getDatosAutorizaciones() {
        return datosAutorizaciones;
    }

    public void setDatosAutorizaciones(DatosAutorizaciones datosAutorizaciones) {
        this.datosAutorizaciones = datosAutorizaciones;
    }
}
