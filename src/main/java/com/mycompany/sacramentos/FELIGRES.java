package com.mycompany.sacramentos;

import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @autor walyn
 */
public class FELIGRES {

    private final IntegerProperty idFeligres;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty apellido;
    private final SimpleObjectProperty<LocalDate> nacimiento;
    private final IntegerProperty edadFeligres;
    private final SimpleStringProperty lugarNacimiento;
    private final SimpleStringProperty padreFeligres;
    private final SimpleStringProperty madreFeligres;
    private final SimpleStringProperty direccion;
    private final SimpleStringProperty telefono;
    private final SimpleStringProperty correo;
    private final SimpleStringProperty observaciones;
    private final SimpleStringProperty feligresDe;
    private final SimpleStringProperty genero;
    private final SimpleStringProperty jurisdiccion;
    private final SimpleStringProperty religion;
    private final SimpleStringProperty ocupacion;
    private final SimpleStringProperty dpi;
    private final IntegerProperty comunidad_idComunidad;

    public FELIGRES(Integer idFeligres, String nombre, String apellido, LocalDate nacimiento, Integer edadFeligres, String lugarNacimiento, String padreFeligres, String madreFeligres, String direccion, String telefono, String correo, String observaciones, String feligresDe, String genero, String jurisdiccion, String religion, String ocupacion, String dpi, Integer comunidad_idComunidad) {
        this.idFeligres = new SimpleIntegerProperty(idFeligres);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.nacimiento = new SimpleObjectProperty<>(nacimiento);
        this.edadFeligres = new SimpleIntegerProperty(edadFeligres);
        this.lugarNacimiento = new SimpleStringProperty(lugarNacimiento);
        this.padreFeligres = new SimpleStringProperty(padreFeligres);
        this.madreFeligres = new SimpleStringProperty(madreFeligres);
        this.direccion = new SimpleStringProperty(direccion);
        this.telefono = new SimpleStringProperty(telefono);
        this.correo = new SimpleStringProperty(correo);
        this.observaciones = new SimpleStringProperty(observaciones);
        this.feligresDe = new SimpleStringProperty(feligresDe);
        this.genero = new SimpleStringProperty(genero);
        this.jurisdiccion = new SimpleStringProperty(jurisdiccion);
        this.religion = new SimpleStringProperty(religion);
        this.ocupacion = new SimpleStringProperty(ocupacion);
        this.dpi = new SimpleStringProperty(dpi);
        this.comunidad_idComunidad = new SimpleIntegerProperty(comunidad_idComunidad);
    }

    // Getters
    public Integer getIdFeligres() {
        return idFeligres.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public String getApellido() {
        return apellido.get();
    }

    public LocalDate getNacimiento() {
        return nacimiento.get();
    }

    public Integer getEdadFeligres() {
        return edadFeligres.get();
    }

    public String getLugarNacimiento() {
        return lugarNacimiento.get();
    }

    public String getPadreFeligres() {
        return padreFeligres.get();
    }

    public String getMadreFeligres() {
        return madreFeligres.get();
    }

    public String getDireccion() {
        return direccion.get();
    }

    public String getTelefono() {
        return telefono.get();
    }

    public String getCorreo() {
        return correo.get();
    }

    public String getObservaciones() {
        return observaciones.get();
    }

    public String getFeligresDe() {
        return feligresDe.get();
    }

    public String getGenero() {
        return genero.get();
    }

    public String getJurisdiccion() {
        return jurisdiccion.get();
    }

    public String getReligion() {
        return religion.get();
    }

    public String getOcupacion() {
        return ocupacion.get();
    }

    public String getDpi() {
        return dpi.get();
    }

    public Integer getComunidad_idComunidad() {
        return comunidad_idComunidad.get();
    }

    // Setters
    public void setIdFeligres(Integer value) {
        idFeligres.set(value);
    }

    public void setNombre(String value) {
        nombre.set(value);
    }

    public void setApellido(String value) {
        apellido.set(value);
    }

    public void setNacimiento(LocalDate value) {
        nacimiento.set(value);
    }

    public void setEdadFeligres(Integer value) {
        edadFeligres.set(value);
    }

    public void setLugarNacimiento(String value) {
        lugarNacimiento.set(value);
    }

    public void setPadreFeligres(String value) {
        padreFeligres.set(value);
    }

    public void setMadreFeligres(String value) {
        madreFeligres.set(value);
    }

    public void setDireccion(String value) {
        direccion.set(value);
    }

    public void setTelefono(String value) {
        telefono.set(value);
    }

    public void setCorreo(String value) {
        correo.set(value);
    }

    public void setObservaciones(String value) {
        observaciones.set(value);
    }

    public void setFeligresDe(String value) {
        feligresDe.set(value);
    }

    public void setGenero(String value) {
        genero.set(value);
    }

    public void setJurisdiccion(String value) {
        jurisdiccion.set(value);
    }

    public void setReligion(String value) {
        religion.set(value);
    }

    public void setOcupacion(String value) {
        ocupacion.set(value);
    }

    public void setDpi(String value) {
        dpi.set(value);
    }

    public void setComunidad_idComunidad(Integer value) {
        comunidad_idComunidad.set(value);
    }
}
