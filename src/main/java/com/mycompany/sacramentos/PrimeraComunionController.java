/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sacramentos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class PrimeraComunionController implements Initializable {

    @FXML
    private TextField txtLibroPc, txtFolioPc, txtPartidaPc, txtNombrePc, txtApellidoPc, txtLugarBauPc, txtCelebrantePc;
    @FXML
    private TextArea txtAreaObPc;
    @FXML
    private CheckBox boxInscritoPc;
    @FXML
    private DatePicker dpFechaRealizadoPc, dpFechaNacPc;
    @FXML
    private String check;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void _regresar() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void _guardarPc() throws IOException {
        Integer libro, folio, partida;

        // Obteniendo los valores de los campos de texto y otros componentes
        String nombre = txtNombrePc.getText();
        String apellido = txtApellidoPc.getText();
        String lugarPc = txtLugarBauPc.getText();
        String celebrante = txtCelebrantePc.getText();
        String observaciones = txtAreaObPc.getText();
        boolean inscrito = boxInscritoPc.isSelected();
        LocalDate fechaPc = dpFechaRealizadoPc.getValue();
        LocalDate fechaNacimiento = dpFechaNacPc.getValue();

        // Declarando variables de conexión y statement.
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        PreparedStatement pstmt5 = null;
        ResultSet rs = null;

        // Verifica primero si los campos libro, folio o partida están vacíos
        if (txtLibroPc.getText().trim().isEmpty() || txtFolioPc.getText().trim().isEmpty() || txtPartidaPc.getText().trim().isEmpty() || txtNombrePc.getText().trim().isEmpty() || txtApellidoPc.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validación de Campos");
            alert.setHeaderText(null);
            alert.setContentText("Los campos Libro, Folio, Partida, Nomnbre y Apellido no pueden quedar vacíos.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Intenta convertir las entradas a Integer y atrapa cualquier NumberFormatException
        try {
            libro = Integer.valueOf(txtLibroPc.getText().trim());
            folio = Integer.valueOf(txtFolioPc.getText().trim());
            partida = Integer.valueOf(txtPartidaPc.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingresa valores numéricos válidos en los campos Libro, Folio y Partida.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Comprueba si alguna de las cadenas contiene números
        if (nombre.matches(".*\\d.*") || apellido.matches(".*\\d.*") || celebrante.matches(".*\\d.*")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Los campos de Nombre, Apellido y Celebrante no deben contener números. Por favor, corrija e intente nuevamente.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }
        //Inicia la Query SQL
        try {

            // Obtén la fecha de nacimiento del DatePicker
            LocalDate fechaNacimientoC = dpFechaNacPc.getValue();

            // Calcula la edad
            LocalDate hoy = LocalDate.now();
            int edad = hoy.getYear() - fechaNacimientoC.getYear();
            if (fechaNacimientoC.getDayOfYear() > hoy.getDayOfYear()) {
                edad--; // Ajusta la edad si el cumpleaños de este año aún no ha llegado.
            }

            // Validadndo el Combo Box
            if (inscrito) {
                check = "Si";
            } else {
                check = "No";
            }
            // Obteniendo la conexión utilizando la clase ConexionDB
            conn = ConexionDB.getConexion();

            // Creando la consulta SQL para insertar datos.
            String sql = "INSERT INTO feligres (nombre, apellido, nacimiento, edadFeligres) VALUES (?, ?, ?, ?)";

            // Preparando la consulta SQL
            pstmt1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, nombre);
            pstmt1.setString(2, apellido);
            pstmt1.setDate(3, Date.valueOf(fechaNacimiento));
            pstmt1.setInt(4, edad);
            // Ejecutando la consulta SQL
            pstmt1.executeUpdate();

            // Obtener el ID generado Feligres
            rs = pstmt1.getGeneratedKeys();
            if (rs.next()) {
                int idGeneradoF = rs.getInt(1);

                //Realizamos la QUERY Pa insertar al Celebrante
                // Creando la consulta SQL para insertar datos.
                //Insertamos al Celebrante
                String sql5 = "INSERT INTO celebrante (nombreCelebrante) VALUES (?)";
                // Preparando la consulta SQL
                pstmt5 = conn.prepareStatement(sql5, Statement.RETURN_GENERATED_KEYS);
                pstmt5.setString(1, celebrante);
                // Ejecutando la consulta SQL
                pstmt5.executeUpdate();

                // Obtener el ID generado Celebrante
                rs = pstmt5.getGeneratedKeys();
                if (rs.next()) {
                    int idGeneradoC = rs.getInt(1);
                    // Ahora puedes usar idGenerado para las otras consultas como clave foránea
                    //Insertado a la tabla de Registro del Sacramento
                    String sql3 = "INSERT INTO comunion (fechaSacramento, lugarSacramento, idFeligres, celebrante_idCelebrante, fechaInscripcion) VALUES (?, ?, ?, ?, NOW() )";
                    // Preparando la consulta SQL
                    pstmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
                    pstmt3.setDate(1, Date.valueOf(fechaPc));
                    pstmt3.setString(2, lugarPc);
                    pstmt3.setInt(3, idGeneradoF);
                    pstmt3.setInt(4, idGeneradoC);
                    // Ejecutando la consulta SQL
                    pstmt3.executeUpdate();

                    // Obtener el ID generado Celebrante
                    rs = pstmt3.getGeneratedKeys();
                    if (rs.next()) {
                        int idGeneradoS = rs.getInt(1);

                        //Insertado a la tabla de Registro del Sacramento
                        String sql2 = "INSERT INTO registrolibro (libro, folio, partida, inscritoLibro, comunion_idComunion) VALUES (?, ?, ?, ?, ?)";
                        // Preparando la consulta SQL
                        pstmt2 = conn.prepareStatement(sql2);
                        pstmt2.setInt(1, libro);
                        pstmt2.setInt(2, folio);
                        pstmt2.setInt(3, partida);
                        pstmt2.setString(4, check);
                        pstmt2.setInt(5, idGeneradoS);
                        // Ejecutando la consulta SQL
                        pstmt2.executeUpdate();

                        //Insertado a la tabla de Reistrodel Sacramento
                        String sql4 = "INSERT INTO observacion (observacion, comunion_idComunion) VALUES (?,?)";
                        // Preparando la consulta SQL
                        pstmt4 = conn.prepareStatement(sql4);
                        pstmt4.setString(1, observaciones);
                        pstmt4.setInt(2, idGeneradoS);
                        // Ejecutando la consulta SQL
                        pstmt4.executeUpdate();

                    }

                }

            }
            limpiarCampos();
         //Semi Finaliza la QUERY
        } catch (SQLException e) {
            e.printStackTrace();
            // manejo de errores según necesite
        } finally {
            // Cerrando el PreparedStatement y Connection
            try {
                if (pstmt1 != null) {
                    pstmt1.close();
                }
                if (pstmt2 != null) {
                    pstmt2.close();
                }
                if (pstmt3 != null) {
                    pstmt3.close();
                }
                if (pstmt4 != null) {
                    pstmt4.close();
                }
                if (pstmt5 != null) {
                    pstmt5.close();
                }
                if (conn != null) {
                    conn.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //Aca Finaliza _guardarPc
    }

    public void limpiarCampos() {
//    this.txtLibroB.clear();
//    this.txtFolioB.clear();
        // Limpiar todos los otros campos
        // Limpieza de los TextField
        txtLibroPc.clear();
        txtFolioPc.clear();
        txtPartidaPc.clear();
        txtNombrePc.clear();
        txtApellidoPc.clear();
        txtLugarBauPc.clear();

        // Limpieza del TextArea
        txtAreaObPc.clear();

        // Limpieza del CheckBox
        boxInscritoPc.setSelected(false);

        // Limpieza de los DatePicker
        dpFechaRealizadoPc.setValue(null);
        dpFechaNacPc.setValue(null);
    }

}
