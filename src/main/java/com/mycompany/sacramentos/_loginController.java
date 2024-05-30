/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sacramentos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 *
 * @author walyn
 */
public class _loginController {

    @FXML
    private Label txtIncorrecto;

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField _txtPassword;

    @FXML
    private void _irPrimero() throws IOException {
        String usuario = txtUsuario.getText();
        String password = _txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Usuario y/o contraseña no pueden estar vacíos.");
            return;
        }

        // Abre una conexión a la base de datos
        try (Connection conexion = ConexionDB.getConexion()) {
            // Consulta SQL para buscar el usuario en la base de datos
            String sql = "SELECT contraUsuario FROM usuario WHERE nickUsuario = ? OR correoUsuario = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, usuario);
                pstmt.setString(2, usuario);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Usuario existe, verifica la contraseña
                    String passwordDB = rs.getString("contraUsuario");
                    if (password.equals(passwordDB)) {
                        // Logueo exitoso, redirige al usuario a otra pantalla o realiza alguna acción
                        System.out.println("Login exitoso");
                        App.setRoot("primary");
                    } else {
                        // Contraseña incorrecta
                        //showAlert(Alert.AlertType.ERROR, "Error", "Contraseña incorrecta."); // Sustituido por Una Animacion mas Bonita
                        txtIncorrecto.setVisible(true);
                        // Configurar la transición de desvanecimiento
                        FadeTransition fadeTransition = new FadeTransition(Duration.millis(4000), txtIncorrecto);
                        fadeTransition.setFromValue(1.0);
                        fadeTransition.setToValue(0.0);
                        fadeTransition.setCycleCount(1);
                        fadeTransition.setAutoReverse(false);

                        // Iniciar la transición
                        fadeTransition.play();

                        // Configurar la transición de vibrante
                        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), txtIncorrecto);
                        translateTransition.setFromX(0);
                        translateTransition.setByX(10);
                        translateTransition.setCycleCount(10);
                        translateTransition.setAutoReverse(true);
                        translateTransition.setInterpolator(Interpolator.LINEAR);

                        // Iniciar la transición
                        translateTransition.play();

                    }
                } else {
                    // Usuario no existe
                    showAlert(Alert.AlertType.ERROR, "Error", "Usuario no existe.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al acceder a la base de datos.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();

    }

    @FXML
    private void _general() throws IOException {

        App.setRoot("_general");
    }

}
