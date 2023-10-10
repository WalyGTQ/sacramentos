package com.mycompany.sacramentos;

import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author walyn
 */
public class ConsultaPrimeraComunion {

    private final IntegerProperty libroC;
    private final IntegerProperty folioC;
    private final IntegerProperty partidaC;
    private final IntegerProperty edadC;
    private final ObjectProperty<LocalDate> fechaRealizadoPc;
    private final ObjectProperty<LocalDate> fechaNacPc;
    private final ObjectProperty<LocalDate> registroC;
    private final StringProperty nombreC;
    private final StringProperty apellidoC;
    private final StringProperty lugarC;
    private final StringProperty celebranteC;
    private final StringProperty anotadoC;
    private final StringProperty observacionC;

    public ConsultaPrimeraComunion(int libroC, int folioC, int partidaC, int edadC,
            LocalDate fechaRealizadoPc, LocalDate fechaNacPc, LocalDate registroC,
            String nombreC, String apellidoC, String lugarC,
            String celebranteC, String anotadoC, String observacionC) {
        this.libroC = new SimpleIntegerProperty(libroC);
        this.folioC = new SimpleIntegerProperty(folioC);
        this.partidaC = new SimpleIntegerProperty(partidaC);
        this.edadC = new SimpleIntegerProperty(edadC);
        this.fechaRealizadoPc = new SimpleObjectProperty<>(fechaRealizadoPc);
        this.fechaNacPc = new SimpleObjectProperty<>(fechaNacPc);
        this.registroC = new SimpleObjectProperty<>(registroC);
        this.nombreC = new SimpleStringProperty(nombreC);
        this.apellidoC = new SimpleStringProperty(apellidoC);
        this.lugarC = new SimpleStringProperty(lugarC);
        this.celebranteC = new SimpleStringProperty(celebranteC);
        this.anotadoC = new SimpleStringProperty(anotadoC);
        this.observacionC = new SimpleStringProperty(observacionC);
    }

    public int getLibroC() {
        return libroC.get();
    }

    public int getFolioC() {
        return folioC.get();
    }

    public int getPartidaC() {
        return partidaC.get();
    }

    public int getEdadC() {
        return edadC.get();
    }

    public LocalDate getFechaRealizadoPc() {
        return fechaRealizadoPc.get();
    }

    public LocalDate getFechaNacPc() {
        return fechaNacPc.get();
    }

    public LocalDate getRegistroC() {
        return registroC.get();
    }

    public String getNombreC() {
        return nombreC.get();
    }

    public String getApellidoC() {
        return apellidoC.get();
    }

    public String getLugarC() {
        return lugarC.get();
    }

    public String getCelebranteC() {
        return celebranteC.get();
    }

    public String getAnotadoC() {
        return anotadoC.get();
    }

    public String getObservacionC() {
        return observacionC.get();
    }

    // Se puede agregar setters para modificar estos campos.
    // Los métodos property() para cada campo si es necesario para enlazar con JavaFX.
}
