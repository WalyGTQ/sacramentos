/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author walyn
 */
public class DatosAutorizaciones {
    

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty parroquia;
    private final SimpleStringProperty parroco;
    private final SimpleStringProperty padre;
    private final SimpleStringProperty madre;
    private final SimpleStringProperty nino;
    private final SimpleStringProperty lnacimiento;
    private final SimpleObjectProperty<LocalDate> fnacimiento;
    private final SimpleStringProperty padrino;
    private final SimpleStringProperty madrina;
    private final SimpleStringProperty obs;
    private final SimpleObjectProperty<LocalDateTime> creacion;
    private final SimpleStringProperty creador;
    private final SimpleObjectProperty<LocalDateTime> modificacion;
    private final SimpleStringProperty modificador;

    public DatosAutorizaciones(int id, String parroquia, String parroco, String padre, String madre, String nino, String lnacimiento, LocalDate fnacimiento, String padrino, String madrina, String obs, LocalDateTime creacion, String creador, LocalDateTime modificacion, String modificador) {
        this.id = new SimpleIntegerProperty(id);
        this.parroquia = new SimpleStringProperty(parroquia);
        this.parroco = new SimpleStringProperty(parroco);
        this.padre = new SimpleStringProperty(padre);
        this.madre = new SimpleStringProperty(madre);
        this.nino = new SimpleStringProperty(nino);
        this.lnacimiento = new SimpleStringProperty(lnacimiento);
        this.fnacimiento = new SimpleObjectProperty<>(fnacimiento);
        this.padrino = new SimpleStringProperty(padrino);
        this.madrina = new SimpleStringProperty(madrina);
        this.obs = new SimpleStringProperty(obs);
        this.creacion = new SimpleObjectProperty<>(creacion);
        this.creador = new SimpleStringProperty(creador);
        this.modificacion = new SimpleObjectProperty<>(modificacion);
        this.modificador = new SimpleStringProperty(modificador);
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getParroquia() {
        return parroquia.get();
    }

    public String getParroco() {
        return parroco.get();
    }

    public String getPadre() {
        return padre.get();
    }

    public String getMadre() {
        return madre.get();
    }

    public String getNino() {
        return nino.get();
    }

    public String getLnacimiento() {
        return lnacimiento.get();
    }

    public LocalDate getFnacimiento() {
        return fnacimiento.get();
    }

    public String getPadrino() {
        return padrino.get();
    }

    public String getMadrina() {
        return madrina.get();
    }

    public String getObs() {
        return obs.get();
    }

    public LocalDateTime getCreacion() {
        return creacion.get();
    }

    public String getCreador() {
        return creador.get();
    }

    public LocalDateTime getModificacion() {
        return modificacion.get();
    }

    public String getModificador() {
        return modificador.get();
    }

    // Setters
    public void setId(int value) {
        id.set(value);
    }

    public void setParroquia(String value) {
        parroquia.set(value);
    }

    public void setParroco(String value) {
        parroco.set(value);
    }

    public void setPadre(String value) {
        padre.set(value);
    }

    public void setMadre(String value) {
        madre.set(value);
    }

    public void setNino(String value) {
        nino.set(value);
    }

    public void setLnacimiento(String value) {
        lnacimiento.set(value);
    }

    public void setFnacimiento(LocalDate value) {
        fnacimiento.set(value);
    }

    public void setPadrino(String value) {
        padrino.set(value);
    }

    public void setMadrina(String value) {
        madrina.set(value);
    }

    public void setObs(String value) {
        obs.set(value);
    }

    public void setCreacion(LocalDateTime value) {
        creacion.set(value);
    }

    public void setCreador(String value) {
        creador.set(value);
    }

    public void setModificacion(LocalDateTime value) {
        modificacion.set(value);
    }

    public void setModificador(String value) {
        modificador.set(value);
    }
}
