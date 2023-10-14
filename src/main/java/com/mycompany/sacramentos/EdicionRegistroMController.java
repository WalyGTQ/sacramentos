package com.mycompany.sacramentos;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

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
public class EdicionRegistroMController implements Initializable {
    //Instancia de la Clase Singleton para obtener los datos guardados
    ConsultaMatrimonio feligresDetalle = SingletonMatrimonio.getInstance().getFeligresDetalle();
       //LLamado a los elementos del FXML
    @FXML
    private TextField txtLibroM, txtFolioM, txtPartidaM, txtlugarM, txtTestigo1M, txtTestigo2M, txtNombreMM, txtApellidoMM, txtEdadMM, txtOrigenMM, txtFeligresMM, txtPadreMM, txtMadreMM, txtNombreFM, txtApellidoFM, txtEdadFM, txtOrigenFM, txtFeligresFM, txtPadreFM, txtMadreFM, txtCelebranteM;
    @FXML
    private TextArea txtAreaM;
    @FXML
    private CheckBox boxInscritoM;
    @FXML
    private DatePicker dpFechaM;
    private String check;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtLibroM.setText(String.valueOf(feligresDetalle.getLibroM()));
        txtFolioM.setText(String.valueOf(feligresDetalle.getFolioM()));
        txtPartidaM.setText(String.valueOf(feligresDetalle.getPartidaM()));
        dpFechaM.setValue(LocalDate.parse(feligresDetalle.getFechaM().toString()));
        txtlugarM.setText(feligresDetalle.getLugarM());
        txtTestigo1M.setText(feligresDetalle.getTestigoUnoM());
        txtTestigo2M.setText(feligresDetalle.getTestigoDosM());
        txtNombreMM.setText(feligresDetalle.getNombreMM());
        txtApellidoMM.setText(feligresDetalle.getApellidoMM());
        txtEdadMM.setText(String.valueOf(feligresDetalle.getEdadMM()));
        txtOrigenMM.setText(feligresDetalle.getOrigenMM());
        txtFeligresMM.setText(feligresDetalle.getFeligresMM());
        txtPadreMM.setText(feligresDetalle.getPadreMM());
        txtMadreMM.setText(feligresDetalle.getMadreMM());
        txtNombreFM.setText(feligresDetalle.getNombreFM());
        txtApellidoFM.setText(feligresDetalle.getApellidoFM());
        txtEdadFM.setText(String.valueOf(feligresDetalle.getEdadFM()));
        txtOrigenFM.setText(feligresDetalle.getOrigenFM());
        txtFeligresFM.setText(feligresDetalle.getFeligresFM());
        txtPadreFM.setText(feligresDetalle.getPadreFM());
        txtMadreFM.setText(feligresDetalle.getMadreFM());
        txtCelebranteM.setText(feligresDetalle.getCelebranteM());
        txtAreaM.setText(feligresDetalle.getObservacionM());
        if ("Si".equals(feligresDetalle.getInscritoM())) {
            boxInscritoM.setSelected(true); // Si "Si" está registrado, marca el CheckBox
        } else {
            boxInscritoM.setSelected(false); // De lo contrario, desmarca el CheckBox
        }
    }    
        @FXML
    private void _regresar() throws IOException {
        App.setRoot("matrimonios");
    }
            @FXML
    private void _actualizarM() throws IOException {
        
    }
            @FXML
    private void _eliminarM() throws IOException {
        
        
    }
    
}
