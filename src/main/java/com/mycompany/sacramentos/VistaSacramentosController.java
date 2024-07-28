package com.mycompany.sacramentos;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class VistaSacramentosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    //MEtodo para regresar
    @FXML
    private void regresar() throws IOException {
        App.setRoot("menuPrincipal");
    }

    //metodo para ingresar a los bautismos
    @FXML
    private void bautismos() throws IOException {
        App.setRoot("edicionRegistro");
    }

    //metodo para ingresar a los bautismos
    @FXML
    private void InsBautismo() throws IOException {
        App.setRoot("bautismosVista");
    }

    @FXML
    private void EditBautismo() throws IOException {
        App.setRoot("vistaEdicionBautismo");
    }

    @FXML
    private void reporteBautismo() throws IOException {
        App.setRoot("vistaBautismoReporte");
    }

    @FXML
    private void constanciaBautismo() throws IOException {
        App.setRoot("vistaBautismoConstancia");
    }

    @FXML
    private void autBautismo() throws IOException {
        App.setRoot("vistaBautismoAutorizacion");
    }

}
