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
 * Controlador para la vista de bautismos.
 * Maneja la interacción del usuario con la interfaz de bautismos.
 *
 * Autor: walyn
 */
public class BautismosVistaController implements Initializable {




    // Campos para el ingreso de datos
    @FXML private TextField txtLibroB, txtFolioB, txtPartidaB, txtNombreB, txtApellidoB, txtPadreB, txtMadreB, txtPadrinoB, txtMadrinaB, txtLugarBautismo, txtLugarNacimientoB;
    @FXML private TextArea txtAreaObservaciones;
    @FXML private CheckBox boxInscritoB;
    @FXML private DatePicker datePikerFechaB, datePikerFechaNacimientoB;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización del controlador
    }

    @FXML
    private void regresar() throws IOException {
        App.setRoot("vistaSacramentos");
    }

    @FXML
    private void _guardar() throws IOException, SQLException {
        Integer libro, folio, partida;

        // Obteniendo los valores de los campos de texto y otros componentes
        String nombre = txtNombreB.getText();
        String apellido = txtApellidoB.getText();
        String padre = txtPadreB.getText();
        String madre = txtMadreB.getText();
        String lugarNacimiento = txtLugarNacimientoB.getText();
        String padrino = txtPadrinoB.getText();
        String madrina = txtMadrinaB.getText();
        String lugarBautismo = txtLugarBautismo.getText();
        String observaciones = txtAreaObservaciones.getText();
        boolean inscrito = boxInscritoB.isSelected();
        LocalDate fechaBautismo = datePikerFechaB.getValue();
        LocalDate fechaNacimiento = datePikerFechaNacimientoB.getValue();

        // Declarando variables de conexión y statement.
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        ResultSet rs = null;

        // Verifica primero si los campos libro, folio, partida, nombre o apellido están vacíos
        if (txtLibroB.getText().trim().isEmpty() || txtFolioB.getText().trim().isEmpty() || txtPartidaB.getText().trim().isEmpty() || txtNombreB.getText().trim().isEmpty() || txtApellidoB.getText().trim().isEmpty()) {
            showAlert("Validación de Campos", "Los campos Libro, Folio y Partida no pueden quedar vacíos.", Alert.AlertType.WARNING);
            return;
        }

        // Intenta convertir las entradas a Integer y atrapa cualquier NumberFormatException
        try {
            libro = Integer.valueOf(txtLibroB.getText().trim());
            folio = Integer.valueOf(txtFolioB.getText().trim());
            partida = Integer.valueOf(txtPartidaB.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Entrada no válida", "Por favor, ingresa valores numéricos válidos en los campos Libro, Folio y Partida.", Alert.AlertType.WARNING);
            return;
        }

        // Comprueba si alguna de las cadenas contiene números
        if (nombre.matches(".*\\d.*") || apellido.matches(".*\\d.*") || padre.matches(".*\\d.*") || madre.matches(".*\\d.*")) {
            showAlert("Entrada no válida", "Los campos de nombre, apellido, padre y madre no deben contener números. Por favor, corrija e intente nuevamente.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Obtén la fecha de nacimiento del DatePicker
            LocalDate fechaNacimientoC = datePikerFechaNacimientoB.getValue();
            LocalDate fechaInscripcion = datePikerFechaB.getValue();

            // Calcula la edad
            LocalDate hoy = LocalDate.now();
            int edad = fechaInscripcion.getYear() - fechaNacimientoC.getYear();
            if (fechaNacimientoC.getDayOfYear() > fechaInscripcion.getDayOfYear()) {
                edad--; // Ajusta la edad si el cumpleaños de este año aún no ha llegado.
            }

            // Validaciones de fechas
            if (fechaNacimientoC.isAfter(hoy)) {
                showAlert("Error", "La fecha de Nacimiento no puede ser después de hoy.", Alert.AlertType.ERROR);
                return;
            }
            if (fechaNacimientoC.isAfter(fechaInscripcion)) {
                showAlert("Error", "El Sacramento no puede ser antes del nacimiento.", Alert.AlertType.ERROR);
                return;
            }

            // Validando el Combo Box
            String check = inscrito ? "Si" : "No";

            // Obteniendo la conexión utilizando la clase ConexionDB
            conn = ConexionDB.getConexion();

            // Creando la consulta SQL para insertar datos en la tabla feligres
            String sql = "INSERT INTO feligres (nombre, apellido, padreFeligres, madreFeligres, nacimiento, edadFeligres, lugarNacimiento) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, nombre);
            pstmt1.setString(2, apellido);
            pstmt1.setString(3, padre);
            pstmt1.setString(4, madre);
            pstmt1.setDate(5, Date.valueOf(fechaNacimiento));
            pstmt1.setInt(6, edad);
            pstmt1.setString(7, lugarNacimiento);
            pstmt1.executeUpdate();

            // Obtener el ID generado para insertar en las demás tablas
            rs = pstmt1.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);

                // Insertar en la tabla bautismo
                String sql3 = "INSERT INTO bautismo (fechaSacramento, lugarSacramento, padrino, madrina, idFeligres, fechaInscripcion) VALUES (?, ?, ?, ?, ?, NOW())";
                pstmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
                pstmt3.setDate(1, Date.valueOf(fechaBautismo));
                pstmt3.setString(2, lugarBautismo);
                pstmt3.setString(3, padrino);
                pstmt3.setString(4, madrina);
                pstmt3.setInt(5, idGenerado);
                pstmt3.executeUpdate();

                // Obtener el ID generado para insertar en la tabla registrolibro
                rs = pstmt3.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado1 = rs.getInt(1);

                    // Insertar en la tabla registrolibro
                    String sql2 = "INSERT INTO registrolibro (libro, folio, partida, inscritoLibro, bautismo_idBautismo) VALUES (?, ?, ?, ?, ?)";
                    pstmt2 = conn.prepareStatement(sql2);
                    pstmt2.setInt(1, libro);
                    pstmt2.setInt(2, folio);
                    pstmt2.setInt(3, partida);
                    pstmt2.setString(4, check);
                    pstmt2.setInt(5, idGenerado1);
                    pstmt2.executeUpdate();

                    // Insertar en la tabla observacion
                    String sql4 = "INSERT INTO observacion (observacion, bautismo_idBautismo) VALUES (?, ?)";
                    pstmt4 = conn.prepareStatement(sql4);
                    pstmt4.setString(1, observaciones);
                    pstmt4.setInt(2, idGenerado1);
                    pstmt4.executeUpdate();
                }
            }
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrando el PreparedStatement y Connection
            cerrarRecursos(pstmt1, pstmt2, pstmt3, pstmt4, conn, rs);
        }
    }

    // Método para limpiar los campos del formulario
    public void limpiarCampos() {
        txtLibroB.clear();
        txtFolioB.clear();
        txtPartidaB.clear();
        txtNombreB.clear();
        txtApellidoB.clear();
        txtPadreB.clear();
        txtMadreB.clear();
        txtPadrinoB.clear();
        txtMadrinaB.clear();
        txtLugarBautismo.clear();
        txtLugarNacimientoB.clear();
        txtAreaObservaciones.clear();
        boxInscritoB.setSelected(false);
        datePikerFechaB.setValue(null);
        datePikerFechaNacimientoB.setValue(null);
    }

    // Función para mostrar alertas fácilmente
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Método para cerrar recursos de base de datos
    private void cerrarRecursos(PreparedStatement pstmt1, PreparedStatement pstmt2, PreparedStatement pstmt3, PreparedStatement pstmt4, Connection conn, ResultSet rs) {
        try {
            if (pstmt1 != null) pstmt1.close();
            if (pstmt2 != null) pstmt2.close();
            if (pstmt3 != null) pstmt3.close();
            if (pstmt4 != null) pstmt4.close();
            if (conn != null) conn.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
