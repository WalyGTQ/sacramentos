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
public class MatrimoniosController implements Initializable {

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
        // TODO
    }

    @FXML
    private void _regresar() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void _guardarM() throws IOException {
        Integer libro, folio, partida, edadEl, EdadElla;
        // Obteniendo los valores de los campos de texto y otros componentes

        String lugarMatrimonio = txtlugarM.getText();
        String testigoUno = txtTestigo1M.getText();
        String testigoDos = txtTestigo2M.getText();
        String nombreEl = txtNombreMM.getText();
        String apellidoEl = txtApellidoMM.getText();
        String origenEl = txtOrigenMM.getText();
        String feligresEl = txtFeligresMM.getText();
        String padreEl = txtPadreMM.getText();
        String madreEl = txtMadreMM.getText();
        String nombreElla = txtNombreFM.getText();
        String apellidoElla = txtApellidoFM.getText();
        String origenElla = txtOrigenFM.getText();
        String feligresElla = txtFeligresFM.getText();
        String padreElla = txtPadreFM.getText();
        String madreElla = txtMadreFM.getText();
        String celebrante = txtCelebranteM.getText();
        LocalDate fechaMatrimonio = dpFechaM.getValue();
        String observaciones = txtAreaM.getText();
        boolean inscrito = boxInscritoM.isSelected();
        String strEdadEl = txtEdadMM.getText().trim();
        String strEdadElla = txtEdadFM.getText().trim();

        // Declarando variables de conexión y statement.
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        PreparedStatement pstmt5 = null;
        PreparedStatement pstmt6 = null;
        ResultSet rs = null;

        // Verifica primero si los campos libro, folio, contrayentes o partida están vacíos
        if (txtLibroM.getText().trim().isEmpty() || txtFolioM.getText().trim().isEmpty() || txtPartidaM.getText().trim().isEmpty() || txtNombreMM.getText().trim().isEmpty() || txtApellidoMM.getText().trim().isEmpty() || txtNombreFM.getText().trim().isEmpty() || txtApellidoFM.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validación de Campos");
            alert.setHeaderText(null);
            alert.setContentText("Los campos Libro, Folio, Partida y Contrayentes, no pueden quedar vacíos.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Intenta convertir las entradas a Integer y atrapa cualquier NumberFormatException
        try {
            libro = Integer.valueOf(txtLibroM.getText().trim());
            folio = Integer.valueOf(txtFolioM.getText().trim());
            partida = Integer.valueOf(txtPartidaM.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingresa valores numéricos válidos en los campos Libro, Folio y Partida.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Comprueba si alguna de las cadenas contiene números
        if (testigoUno.matches(".*\\d.*") || testigoDos.matches(".*\\d.*") || nombreEl.matches(".*\\d.*") || apellidoEl.matches(".*\\d.*") || padreEl.matches(".*\\d.*") || madreEl.matches(".*\\d.*") || nombreElla.matches(".*\\d.*") || apellidoElla.matches(".*\\d.*") || padreElla.matches(".*\\d.*") || madreElla.matches(".*\\d.*") || celebrante.matches(".*\\d.*")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Los campos de nombre, apellido, padre y madre no deben contener números. Por favor, corrija e intente nuevamente.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }
        // Comprobar si los campos de Ambas edades están vacíos
        if (strEdadEl.isEmpty() || strEdadElla.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Los campos de edad no pueden estar vacíos. Por favor, ingrese un valor.");
            alert.showAndWait();
            return;
        }
        try {
            //Conversion de String A Entero
            edadEl = Integer.valueOf(strEdadEl);
            EdadElla = Integer.valueOf(strEdadElla);

            // Comprobar si la edad está dentro de un rango válido
            if (edadEl < 15 || edadEl > 100 || EdadElla < 15 || EdadElla > 100) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Entrada no válida");
                alert.setHeaderText(null);
                alert.setContentText("La edad introducida no está en un rango válido. ");
                alert.showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingresa valores numéricos válidos en los campos de edad.");
            alert.showAndWait();
            return;
        }
        //Inicia la Query SQL
        try {
            // Validadndo el Combo Box
            if (inscrito) {
                check = "Si";
            } else {
                check = "No";
            }
            // Obteniendo la conexión utilizando la clase ConexionDB
            conn = ConexionDB.getConexion();

            // Creando la consulta SQL para insertar datos.
            String sql = "INSERT INTO feligres (nombre, apellido, edadFeligres, lugarNacimiento, padreFeligres, madreFeligres, feligresDe ) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Preparando la consulta SQL
            pstmt1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, nombreEl);
            pstmt1.setString(2, apellidoEl);
            pstmt1.setInt(3, edadEl);
            pstmt1.setString(4, origenEl);
            pstmt1.setString(5, padreEl);
            pstmt1.setString(6, madreEl);
            pstmt1.setString(7, feligresEl);
            // Ejecutando la consulta SQL
            pstmt1.executeUpdate();

            // Obtener el ID generado Feligres1
            rs = pstmt1.getGeneratedKeys();
            if (rs.next()) {
                int idGeneradoF1 = rs.getInt(1);

                // Creando la consulta SQL para insertar datos segundo Feligres.
                String sql1 = "INSERT INTO feligres (nombre, apellido, edadFeligres, lugarNacimiento, padreFeligres, madreFeligres, feligresDe ) VALUES (?, ?, ?, ?, ?, ?, ?)";

                // Preparando la consulta SQL
                pstmt2 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
                pstmt2.setString(1, nombreElla);
                pstmt2.setString(2, apellidoElla);
                pstmt2.setInt(3, EdadElla);
                pstmt2.setString(4, origenElla);
                pstmt2.setString(5, padreElla);
                pstmt2.setString(6, madreElla);
                pstmt2.setString(7, feligresElla);
                // Ejecutando la consulta SQL
                pstmt2.executeUpdate();

                // Obtener el ID generado Feligres2
                rs = pstmt2.getGeneratedKeys();
                if (rs.next()) {
                    int idGeneradoF2 = rs.getInt(1);

                    //Realizamos la QUERY Pa insertar al Celebrante
                    // Creando la consulta SQL para insertar datos.
                    //Insertamos al Celebrante
                    String sql2 = "INSERT INTO celebrante (nombreCelebrante) VALUES (?)";
                    // Preparando la consulta SQL
                    pstmt3 = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
                    pstmt3.setString(1, celebrante);
                    // Ejecutando la consulta SQL
                    pstmt3.executeUpdate();

                    // Obtener el ID generado Celebrante
                    rs = pstmt3.getGeneratedKeys();
                    if (rs.next()) {
                        int idGeneradoC = rs.getInt(1);
                        // Ahora puedes usar idGenerado para las otras consultas como clave foránea
                        //Insertado a la tabla de Registro del Sacramento

                        String sql3 = "INSERT INTO matrimonios (lugarSacramento, fechaSacramento,  testigo1, testigo2, idFeligres1, idFeligres2, celebrante_idCelebrante, fechaInscripcion) VALUES (?, ?, ?, ?, ?, ?, ?, NOW() )";
                        // Preparando la consulta SQL
                        pstmt4 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
                        pstmt4.setString(1, lugarMatrimonio);
                        pstmt4.setDate(2, Date.valueOf(fechaMatrimonio));
                        pstmt4.setString(3, testigoUno);
                        pstmt4.setString(4, testigoDos);
                        pstmt4.setInt(5, idGeneradoF1);
                        pstmt4.setInt(6, idGeneradoF2);
                        pstmt4.setInt(7, idGeneradoC);
                        // Ejecutando la consulta SQL
                        pstmt4.executeUpdate();

                        // Obtener el ID generado del Sacramento
                        rs = pstmt4.getGeneratedKeys();
                        if (rs.next()) {
                            int idGeneradoS = rs.getInt(1);

                            //Insertado a la tabla de Registro del Sacramento
                            String sql4 = "INSERT INTO registrolibro (libro, folio, partida, inscritoLibro, matrimonio_idMatrimonio) VALUES (?, ?, ?, ?, ?)";
                            // Preparando la consulta SQL
                            pstmt5 = conn.prepareStatement(sql4);
                            pstmt5.setInt(1, libro);
                            pstmt5.setInt(2, folio);
                            pstmt5.setInt(3, partida);
                            pstmt5.setString(4, check);
                            pstmt5.setInt(5, idGeneradoS);
                            // Ejecutando la consulta SQL
                            pstmt5.executeUpdate();

                            //Insertado a la tabla de bbservacion del Sacramento
                            String sql5 = "INSERT INTO observacion (observacion, matrimonio_idMatrimonio) VALUES (?,?)";
                            // Preparando la consulta SQL
                            pstmt6 = conn.prepareStatement(sql5);
                            pstmt6.setString(1, observaciones);
                            pstmt6.setInt(2, idGeneradoS);
                            // Ejecutando la consulta SQL
                            pstmt6.executeUpdate();

                        }//Finaliza el If de los Sacramentos

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
                if (pstmt6 != null) {
                    pstmt6.close();
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
        }//Finaliza la Consulta SQL

    }//Finaliza el Metodo _guardarM

    public void limpiarCampos() {
        // Limpiar todos los otros campos
        // Limpieza de los TextField
        txtLibroM.clear();
        txtFolioM.clear();
        txtPartidaM.clear();
        txtlugarM.clear();
        txtTestigo1M.clear();
        txtTestigo2M.clear();
        txtNombreMM.clear();
        txtApellidoMM.clear();
        txtEdadMM.clear();
        txtOrigenMM.clear();
        txtFeligresMM.clear();
        txtPadreMM.clear();
        txtMadreMM.clear();
        txtNombreFM.clear();
        txtApellidoFM.clear();
        txtEdadFM.clear();
        txtOrigenFM.clear();
        txtFeligresFM.clear();
        txtPadreFM.clear();
        txtMadreFM.clear();
        txtCelebranteM.clear();

        // Limpieza del TextArea
        txtAreaM.clear();

        // Limpieza del CheckBox
        boxInscritoM.setSelected(false);

        // Limpieza de los DatePicker
        dpFechaM.setValue(null);
    }

}
