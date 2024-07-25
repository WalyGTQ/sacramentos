/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sacramentos;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class MenuPrincipalController implements Initializable {
    @FXML private Label nombreUsuario;
    
    private ConexionDB sesion1;
    private Usuario usuarioActual;
    private String actividad;

    public void setUsuario(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nombreUsuario.setText(SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre());
    }

    @FXML
    private void _about() throws IOException {
        App.setRoot("about");
    }
        @FXML
    private void sacramentos() throws IOException {
        App.setRoot("vistaSacramentos");
    }
    //Envia hacia la vista de Busqueda General de Feligreses Inscritos
    @FXML
    public void BusquedaGeneral() throws IOException {
        App.setRoot("vistaBusquedaGeneral");
    }


    @FXML
    private void _salir() throws IOException {
        actividad = "Cerrar Sesion";
        sesion1 = new ConexionDB(SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre(), SingletonDatosUsuario.getInstance().getDatosUsuario().getPass(),actividad);

        try {
            sesion1.cerrarSesion();
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("Error al Cerrar Sesion para el usuario: " + SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre());
        }
        App.setRoot("_login");
    }

}
