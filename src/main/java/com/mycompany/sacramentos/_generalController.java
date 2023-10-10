package com.mycompany.sacramentos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author walyn
 * 
 */

public class _generalController implements Initializable {
    

    @FXML
    private TextField nombreAdmin;
    @FXML
    private TextField apellidoAdmin;
    @FXML
    private TextField usuarioAdmin;
    @FXML
    private DatePicker fechaAdmin;
    @FXML
    private TextField correoAdmin;
    @FXML
    private PasswordField contra1Admin;
    @FXML
    private PasswordField contra2Admin;
    @FXML
    private TextField celAdmin;
    private String Pass;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    

    @FXML
    private void _cancelarRegistro() throws IOException {

        App.setRoot("_login");
    }

    @FXML
    private void _registoAdministrador() throws IOException {
        String password1 = contra1Admin.getText();
        String password2 = contra2Admin.getText();

        // Verifica si las contraseñas son iguales
        if (!password1.equals(password2)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Contraseñas No Coinciden");
            alert.setContentText("Las contraseñas ingresadas no son iguales. Por favor, inténtalo de nuevo.");
            alert.showAndWait();
            return; // Retorna para evitar ejecutar el resto del método.
        }

        try (Connection conexion = ConexionDB.getConexion()) {

            // Verifica si ya existe un usuario con el mismo correo o nick
            String verificaSql = "SELECT * FROM usuario WHERE correoUsuario = ? OR nickUsuario = ?";
            try (PreparedStatement verificaPstmt = conexion.prepareStatement(verificaSql)) {
                verificaPstmt.setString(1, correoAdmin.getText());
                verificaPstmt.setString(2, usuarioAdmin.getText());

                ResultSet rs = verificaPstmt.executeQuery();

                if (rs.next()) {
                    // Mostrar alerta ya que se encontró un usuario existente
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Registro Duplicado");
                    alert.setContentText("Ya existe un usuario con este correo o nombre de usuario.");
                    alert.showAndWait();
                    return;
                }
            }

            // Si no se encontró un usuario existente, procede con la inserción.
            String sql = "INSERT INTO usuario (nombreUsuario, apellidoUsuario, nacUsuario, correoUsuario, contraUsuario, celUsuario, nickUsuario, registroUsuario) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nombreAdmin.getText());
                pstmt.setString(2, apellidoAdmin.getText());
                pstmt.setDate(3, java.sql.Date.valueOf(fechaAdmin.getValue()));
                pstmt.setString(4, correoAdmin.getText());
                pstmt.setString(5, password2);
                pstmt.setString(6, celAdmin.getText());
                pstmt.setString(7, usuarioAdmin.getText());

                pstmt.executeUpdate();

                // Si deseas, puedes mostrar una alerta de éxito aquí.
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText("Registro Exitoso");
                alert.setContentText("El administrador ha sido registrado exitosamente.");
                alert.showAndWait();
                App.setRoot("_login");
            }

        } catch (SQLException e) {
            e.printStackTrace();

            // Mostrar una alerta en caso de otro error SQL
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error SQL");
            alert.setContentText("Hubo un error al realizar la operación en la base de datos. Por favor, inténtelo de nuevo.");
            alert.showAndWait();
        }
    }
}
