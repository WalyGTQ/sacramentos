/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sacramentos;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class EdicionRegistroController implements Initializable {

    //Campos para el ingreso de datos
    @FXML
    private TextField txtLibroB, txtFolioB, txtPartidaB, txtEdadB, txtLugarB, txtNombreB, txtApellidoB, txtLugarNacimientoB, txtPadreB, txtMadreB, txtPadrinoB, txtMadrinaB;
    @FXML
    private TextArea taObservacionesB;
    @FXML
    private CheckBox cbInscritoB;
    @FXML
    private DatePicker dpFechaB, dpNacimientoB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Al Inicializar la vista se muestran los datos que previamente se guardaron
        FeligresDetalle feligres = SingletonData1.getInstance().getFeligresDetalle();
        txtLibroB.setText(String.valueOf(feligres.getLibro()));
        txtFolioB.setText(String.valueOf(feligres.getFolio()));
        txtPartidaB.setText(String.valueOf(feligres.getLibro()));
        dpFechaB.setValue(LocalDate.parse(feligres.getFechaSacramento().toString()));
        txtLugarB.setText(feligres.getLugarSacramento());
        txtNombreB.setText(feligres.getNombre());
        txtApellidoB.setText(feligres.getApellido());
        txtEdadB.setText(String.valueOf(feligres.getEdad()));
        dpNacimientoB.setValue(LocalDate.parse(feligres.getNacimiento().toString()));
        txtLugarB.setText(feligres.getLugarSacramento());
        txtPadreB.setText(feligres.getPadre());
        txtMadreB.setText(feligres.getMadre());
        txtPadrinoB.setText(feligres.getPadrino());
        txtMadrinaB.setText(feligres.getMadrina());
        taObservacionesB.setText(feligres.getObservacion());
        txtLugarNacimientoB.setText(feligres.getLugarNacimiento());
        if ("Si".equals(feligres.getRegistrado())) {
            cbInscritoB.setSelected(true); // Si "Si" está registrado, marca el CheckBox
        } else {
            cbInscritoB.setSelected(false); // De lo contrario, desmarca el CheckBox
        }
        System.out.println(feligres.getRegistrado());

    }

    @FXML
    public void _regresar() throws IOException {
        App.setRoot("bautismosVista");
    }
    

}
