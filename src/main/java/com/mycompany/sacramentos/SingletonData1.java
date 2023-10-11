/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

/**
 *
 * @author walyn
 */
public class SingletonData1 {
        private static SingletonData1 instance;
    private FeligresDetalle feligresDetalle;

    private SingletonData1() { }

    public static SingletonData1 getInstance() {
        if (instance == null) {
            instance = new SingletonData1();
        }
        return instance;
    }

    public FeligresDetalle getFeligresDetalle() {
        return feligresDetalle;
    }

    public void setFeligresDetalle(FeligresDetalle feligresDetalle) {
        this.feligresDetalle = feligresDetalle;
    }
}
