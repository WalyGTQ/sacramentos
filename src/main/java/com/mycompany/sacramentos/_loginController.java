package com.mycompany.sacramentos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class _loginController implements Initializable {

    private LocalDateTime horaInicioSesion;
    public Usuario user;

    // Para Iniciar Sesion
    private Sesion sesion;
    private ConexionDB sesion1;
    private String actividad;

    @FXML
    private Label txtIncorrecto;

    @FXML
    private Label txtObligatorio;

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField _txtPassword;

    @Override//Inicializamos para que al presionar enter nos permita ingresar
    public void initialize(URL location, ResourceBundle resources) {
        // Añadir manejador de eventos para el campo de contraseña
        _txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    _irPrimero(); // Llamar al método de inicio de sesión
                } catch (IOException ex) {
                    Logger.getLogger(_loginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Implementación de la función para obtener el ID del usuario
    private int obtenerIdUsuario(String UsuarioSesion) {

        int idUsuario = -1; // Valor predeterminado para el ID del usuario

        String sql = "SELECT idUsuario FROM usuario WHERE nickUsuario = ? OR correoUsuario = ?";

        try (Connection conexion = ConexionDB.getConexion(); PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, UsuarioSesion);
            pstmt.setString(2, UsuarioSesion);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idUsuario = rs.getInt("idUsuario");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al obtener el ID del usuario.");
        }

        return idUsuario; // Retorna el ID obtenido o -1 si no se encontró
    }

    @FXML
    private void _irPrimero() throws IOException {
        String UsuarioSesion = txtUsuario.getText();
        int idUsuarioSesion = obtenerIdUsuario(UsuarioSesion); // Función para obtener el ID del usuario
        String usuario = txtUsuario.getText();
        String password = _txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            // Animación cuando se introduce un campo vacío
            txtObligatorio.setVisible(true);
            // Configurar la transición de desvanecimiento
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(4000), txtObligatorio);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setCycleCount(1);
            fadeTransition.setAutoReverse(false);

            // Iniciar la transición
            fadeTransition.play();

            // Configurar la transición de vibrante
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), txtObligatorio);
            translateTransition.setFromX(0);
            translateTransition.setByX(10);
            translateTransition.setCycleCount(10);
            translateTransition.setAutoReverse(true);
            translateTransition.setInterpolator(Interpolator.LINEAR);

            // Iniciar la transición
            translateTransition.play();
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
                        user = new Usuario(UsuarioSesion, idUsuarioSesion);
                        // Logueo exitoso, redirige al usuario a otra pantalla o realiza alguna acción
                        System.out.println("Login exitoso");
                        actividad = "Gestion Sesion";
                        horaInicioSesion = LocalDateTime.now();

                        //Insatanciamos la los datos del usuario y le asignamos valor, para luego guardar la clase en el singleton :)
                        DatosUsuario usuarioSesion = new DatosUsuario(UsuarioSesion, idUsuarioSesion, horaInicioSesion, 0);
                        SingletonDatosUsuario.getInstance().setDatosUsuario(usuarioSesion);
                        App.setRoot("menuPrincipal");
                        System.out.println("Datos del Singleton " + SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre() + " " + SingletonDatosUsuario.getInstance().getDatosUsuario().getPass());

                        // Iniciar la sesión y registrar la actividad
                        //sesion = new Sesion(UsuarioSesion, idUsuarioSesion);
                        sesion1 = new ConexionDB(SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre(), SingletonDatosUsuario.getInstance().getDatosUsuario().getPass(), actividad);
                        try {
                            sesion1.iniciarSesion();
                            // sesion.registrarActividad("Usuario inició sesión");

                            System.out.println(user.getNombre());
                            System.out.println(user.getPass());
                            System.out.println("Sesión iniciada y actividad registrada para el usuario: " + UsuarioSesion);

                        } catch (SQLException e) {
                            // e.printStackTrace();
                            System.out.println("Error al iniciar sesión o registrar actividad para el usuario: " + UsuarioSesion);
                        }

                    } else {
                        // Contraseña incorrecta
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

    @FXML
    private void _about() throws IOException {
        App.setRoot("about");
    }
}
