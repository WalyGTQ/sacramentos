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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class VistaBautismoAutorizacionImpresionController implements Initializable {

    DatosAutorizaciones datos = SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones();
    @FXML
    private TextField aparroquia, parroco, padre, madre, nino, Lnacimiento, padrino, madrina;
    @FXML
    private DatePicker Fnacimiento;
    @FXML
    private TextArea obs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializamos los campos con los datos obtenidos mediante el doble click guardado en el Singleton Datos Autorizacion
        //DatosAutorizaciones datos = SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones();
        aparroquia.setText(String.valueOf(datos.getParroquia()));
        parroco.setText(String.valueOf(datos.getParroco()));
        padre.setText(String.valueOf(datos.getPadre()));
        madre.setText(String.valueOf(datos.getMadre()));
        nino.setText(String.valueOf(datos.getNino()));
        Lnacimiento.setText(String.valueOf(datos.getLnacimiento()));
        Fnacimiento.setValue(LocalDate.parse(datos.getFnacimiento().toString()));
        padrino.setText(String.valueOf(datos.getPadrino()));
        madrina.setText(String.valueOf(datos.getMadrina()));
        obs.setText(String.valueOf(datos.getObs()));

        // TODO
    }
    
        //Metodo para regresar
    @FXML
    private void actualizar() throws IOException {
        if (comparacion()) {
            System.out.println("Cabios detectados");
        } else {
            System.out.println("Sin Cambios");
        }

    }

    private boolean comparacion() {
        // Define un arreglo de booleanos con todas las condiciones
        boolean[] conditions = {
            !datos.getParroquia().equals(aparroquia.getText()),
            !datos.getParroco().equals(parroco.getText()),
            !datos.getPadre().equals(padre.getText()),
            !datos.getMadre().equals(madre.getText()),
            !datos.getNino().equals(nino.getText()),
            !datos.getLnacimiento().equals(Lnacimiento.getText()),
            !datos.getFnacimiento().equals(Fnacimiento.getValue()),
            !datos.getPadrino().equals(padrino.getText()),
            !datos.getMadrina().equals(madrina.getText()),
            !datos.getObs().equals(obs.getText()),};

        // Itera sobre el arreglo y retorna true si alguna condición se cumple
        for (boolean condition : conditions) {
            if (condition) {
                return true;
            }
        }
        return false;
    }

    //Metodo para regresar
    @FXML
    private void regresar() throws IOException {
        int tabIndexToSelect = 1;
        EstadoPane.getInstance().setSelectedTabIndex(tabIndexToSelect);
        App.setRoot("vistaBautismoAutorizacion");

    }
}
