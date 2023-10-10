package com.mycompany.sacramentos;

import java.time.LocalDate;

/**
 *
 * @author walyn
 */
public class ConsultaMatrimonio {

    private Integer libroM;
    private Integer folioM;
    private Integer partidaM;
    private String lugarM;
    private LocalDate fechaM;
    private String testigoUnoM;
    private String testigoDosM;
    private String nombreMM;
    private String apellidoMM;
    private Integer edadMM;
    private String origenMM;
    private String feligresMM;
    private String padreMM;
    private String madreMM;
    private String nombreFM;
    private String apellidoFM;
    private Integer edadFM;
    private String origenFM;
    private String feligresFM;
    private String padreFM;
    private String madreFM;
    private String celebranteM;
    private String inscritoM;
    private String observacionM;
    private LocalDate registroM;

    public ConsultaMatrimonio(Integer libroM, Integer folioM, Integer partidaM, String lugarM, LocalDate fechaM,
            String testigoUnoM, String testigoDosM, String nombreMM, String apellidoMM, Integer edadMM,
            String origenMM, String feligresMM, String padreMM, String madreMM, String nombreFM,
            String apellidoFM, Integer edadFM, String origenFM, String feligresFM, String padreFM,
            String madreFM, String celebranteM, String inscritoM, String observacionM, LocalDate registroM) {
        this.libroM = libroM;
        this.folioM = folioM;
        this.partidaM = partidaM;
        this.lugarM = lugarM;
        this.fechaM = fechaM;
        this.testigoUnoM = testigoUnoM;
        this.testigoDosM = testigoDosM;
        this.nombreMM = nombreMM;
        this.apellidoMM = apellidoMM;
        this.edadMM = edadMM;
        this.origenMM = origenMM;
        this.feligresMM = feligresMM;
        this.padreMM = padreMM;
        this.madreMM = madreMM;
        this.nombreFM = nombreFM;
        this.apellidoFM = apellidoFM;
        this.edadFM = edadFM;
        this.origenFM = origenFM;
        this.feligresFM = feligresFM;
        this.padreFM = padreFM;
        this.madreFM = madreFM;
        this.celebranteM = celebranteM;
        this.inscritoM = inscritoM;
        this.observacionM = observacionM;
        this.registroM = registroM;

    }

    // Métodos getters para todos los atributos
    public Integer getLibroM() {
        return libroM;
    }

    public Integer getFolioM() {
        return folioM;
    }

    public Integer getPartidaM() {
        return partidaM;
    }

    public String getLugarM() {
        return lugarM;
    }

    public LocalDate getFechaM() {
        return fechaM;
    }

    public String getTestigoUnoM() {
        return testigoUnoM;
    }

    public String getTestigoDosM() {
        return testigoDosM;
    }

    public String getNombreMM() {
        return nombreMM;
    }

    public String getApellidoMM() {
        return apellidoMM;
    }

    public Integer getEdadMM() {
        return edadMM;
    }

    public String getOrigenMM() {
        return origenMM;
    }

    public String getFeligresMM() {
        return feligresMM;
    }

    public String getPadreMM() {
        return padreMM;
    }

    public String getMadreMM() {
        return madreMM;
    }

    public String getNombreFM() {
        return nombreFM;
    }

    public String getApellidoFM() {
        return apellidoFM;
    }

    public Integer getEdadFM() {
        return edadFM;
    }

    public String getOrigenFM() {
        return origenFM;
    }

    public String getFeligresFM() {
        return feligresFM;
    }

    public String getPadreFM() {
        return padreFM;
    }

    public String getMadreFM() {
        return madreFM;
    }

    public String getCelebranteM() {
        return celebranteM;
    }

    public String getInscritoM() {
        return inscritoM;
    }

    public String getObservacionM() {
        return observacionM;
    }

    public LocalDate getRegistroM() {
        return registroM;
    }

}
