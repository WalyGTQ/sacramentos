/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author walyn
 */
public class DatosUsuario {

    private final SimpleStringProperty nombre;
    private final IntegerProperty pass;
    private final IntegerProperty identidad;
    private final ObjectProperty<LocalDateTime> fechaHoraRegistro;

    public DatosUsuario(String nombre, Integer pass, LocalDateTime fechaHoraRegistro, Integer id) {
        this.nombre = new SimpleStringProperty(nombre);
        this.pass = new SimpleIntegerProperty(pass);
        this.fechaHoraRegistro = new SimpleObjectProperty<>(fechaHoraRegistro);
        this.identidad = new SimpleIntegerProperty(id);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String value) {
        nombre.set(value);
    }

    public Integer getPass() {
        return pass.get();
    }

    public void setPass(Integer value) {
        pass.set(value);
     }
    
    
    public Integer getIdentidad() {
        return identidad.get();
    }

    public void setIdentidad(Integer value) {
        identidad.set(value);
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro.get();
    }

    public void setFechaHoraRegistro(LocalDateTime value) {
        fechaHoraRegistro.set(value);
    }

    public ObjectProperty<LocalDateTime> fechaHoraRegistroProperty() {
        return fechaHoraRegistro;
    }

}
