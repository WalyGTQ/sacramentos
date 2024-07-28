/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

public class EstadoPane {
    private static EstadoPane instance;
    private int selectedTabIndex = 0; // Índice por defecto

    private EstadoPane() {}

    public static EstadoPane getInstance() {
        if (instance == null) {
            instance = new EstadoPane();
        }
        return instance;
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }
}
