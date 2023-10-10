
package com.mycompany.sacramentos;

import java.time.LocalDate;

/**
 *
 * @author walyn
 */
public class ConsultaConfirmacion {
    
    private Integer libroCf;
    private Integer folioCf;
    private Integer partidaCf;
    private String nombreCf;
    private String apellidoCf;
    private LocalDate nacimientoCf;
    private Integer edadCf;
    private String celebranteCf;
    private LocalDate fechaCf;
    private String lugarCf;
    private String padreCf;
    private String madreCf;
    private String padrinosCf;
    private String inscritoCf;
    private String observacionesCf;
    private LocalDate registroCf;

    public ConsultaConfirmacion(Integer libroCf, Integer folioCf, Integer partidaCf, String nombreCf, String apellidoCf,
                                LocalDate nacimientoCf, Integer edadCf, String celebranteCf, LocalDate fechaCf, String lugarCf,
                                String padreCf, String madreCf, String padrinosCf, String inscritoCf, String observacionesCf,
                                LocalDate registroCf) {
        this.libroCf = libroCf;
        this.folioCf = folioCf;
        this.partidaCf = partidaCf;
        this.nombreCf = nombreCf;
        this.apellidoCf = apellidoCf;
        this.nacimientoCf = nacimientoCf;
        this.edadCf = edadCf;
        this.celebranteCf = celebranteCf;
        this.fechaCf = fechaCf;
        this.lugarCf = lugarCf;
        this.padreCf = padreCf;
        this.madreCf = madreCf;
        this.padrinosCf = padrinosCf;
        this.inscritoCf = inscritoCf;
        this.observacionesCf = observacionesCf;
        this.registroCf = registroCf;
    }

    public Integer getLibroCf() {
        return libroCf;
    }

    public Integer getFolioCf() {
        return folioCf;
    }

    public Integer getPartidaCf() {
        return partidaCf;
    }

    public String getNombreCf() {
        return nombreCf;
    }

    public String getApellidoCf() {
        return apellidoCf;
    }

    public LocalDate getNacimientoCf() {
        return nacimientoCf;
    }

    public Integer getEdadCf() {
        return edadCf;
    }

    public String getCelebranteCf() {
        return celebranteCf;
    }

    public LocalDate getFechaCf() {
        return fechaCf;
    }

    public String getLugarCf() {
        return lugarCf;
    }

    public String getPadreCf() {
        return padreCf;
    }

    public String getMadreCf() {
        return madreCf;
    }

    public String getPadrinosCf() {
        return padrinosCf;
    }

    public String getInscritoCf() {
        return inscritoCf;
    }

    public String getObservacionesCf() {
        return observacionesCf;
    }

    public LocalDate getRegistroCf() {
        return registroCf;
    }
    
    
}
