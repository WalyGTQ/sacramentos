package com.mycompany.sacramentos;

import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author walyn
 */
public class FeligresDetalle {

    private final IntegerProperty libro;
    private final IntegerProperty folio;
    private final IntegerProperty partida;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty apellido;
    private final SimpleStringProperty padre;
    private final SimpleStringProperty madre;
    private final SimpleObjectProperty<LocalDate> nacimiento;
    private final IntegerProperty edad;
    private final SimpleStringProperty lugarNacimiento;
    private final SimpleObjectProperty<LocalDate> fechaSacramento;
    private final SimpleStringProperty lugarSacramento;
    private final SimpleStringProperty padrino;
    private final SimpleStringProperty madrina;
    private final SimpleStringProperty observacion;
    private final SimpleStringProperty registrado;

    public FeligresDetalle(Integer libro, Integer folio, Integer partida, String nombre, String apellido, String padre, String madre, LocalDate nacimiento, Integer edad, String lugarNacimiento, LocalDate fechaSacramento, String lugarSacramento, String padrino, String madrina, String observacion, String registrado) {

        this.libro = new SimpleIntegerProperty(libro);
        this.folio = new SimpleIntegerProperty(folio);
        this.partida = new SimpleIntegerProperty(partida);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.padre = new SimpleStringProperty(padre);
        this.madre = new SimpleStringProperty(madre);
        this.nacimiento = new SimpleObjectProperty<>(nacimiento);
        this.edad = new SimpleIntegerProperty(edad);
        this.lugarNacimiento = new SimpleStringProperty(lugarNacimiento);
        this.fechaSacramento = new SimpleObjectProperty<>(fechaSacramento);
        this.lugarSacramento = new SimpleStringProperty(lugarSacramento);
        this.padrino = new SimpleStringProperty(padrino);
        this.madrina = new SimpleStringProperty(madrina);
        this.observacion = new SimpleStringProperty(observacion);
        this.registrado = new SimpleStringProperty(registrado);

    }

    public Integer getLibro() {
        return libro.get();
    }

    public Integer getFolio() {
        return folio.get();
    }

    public Integer getPartida() {
        return partida.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public String getApellido() {
        return apellido.get();
    }

    public String getPadre() {
        return padre.get();
    }

    public String getMadre() {
        return madre.get();
    }

    public LocalDate getNacimiento() {
        return nacimiento.get();
    }

    public Integer getEdad() {
        return edad.get();
    }

    public String getLugarNacimiento() {
        return lugarNacimiento.get();
    }

    public LocalDate getFechaSacramento() {
        return fechaSacramento.get();
    }

    public String getLugarSacramento() {
        return lugarSacramento.get();
    }

    public String getPadrino() {
        return padrino.get();
    }

    public String getMadrina() {
        return madrina.get();
    }

    public String getObservacion() {
        return observacion.get();
    }

    public String getRegistrado() {
        return registrado.get();
    }

    // Si se necesita establecer valores después de haberlos creado
    public void setLibro(Integer value) {
        libro.set(value);
    }

    public void setNombre(String value) {
        nombre.set(value);
    }

    public void setApellido(String value) {
        apellido.set(value);
    }
}
