package com.mycompany.sacramentos;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Controlador para la vista del menú principal. Maneja la interacción del
 * usuario con la interfaz principal de la aplicación.
 *
 * Autor: walyn
 */
public class MenuPrincipalController implements Initializable {

    @FXML
    private Label nombreUsuario; // Etiqueta para mostrar el nombre del usuario

    private ConexionDB sesion1; // Objeto para manejar la conexión a la base de datos
    private Usuario usuarioActual; // Objeto para almacenar los datos del usuario actual
    private String actividad; // Variable para almacenar la actividad actual

    /**
     * Establece el usuario actual.
     *
     * @param usuarioActual Objeto Usuario con los datos del usuario actual
     */
    public void setUsuario(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    /**
     * Inicializa el controlador de la clase.
     *
     * @param url URL de la ubicación del recurso
     * @param rb Bundle de recursos para localizar el objeto raíz
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Establece el nombre del usuario en la etiqueta desde el Singleton
        nombreUsuario.setText(SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre());
    }

    /**
     * Maneja la acción del botón "About". Cambia la vista actual a la vista
     * "about".
     *
     * @throws IOException si ocurre un error al cambiar la vista
     */
    @FXML
    private void _about() throws IOException {
        App.setRoot("about");
    }

    /**
     * Maneja la acción del botón "Sacramentos". Cambia la vista actual a la
     * vista "vistaSacramentos".
     *
     * @throws IOException si ocurre un error al cambiar la vista
     */
    @FXML
    private void sacramentos() throws IOException {
        App.setRoot("vistaSacramentos");
    }

    /**
     * Maneja la acción del botón "Búsqueda General". Cambia la vista actual a
     * la vista "vistaBusquedaGeneral".
     *
     * @throws IOException si ocurre un error al cambiar la vista
     */
    @FXML
    public void BusquedaGeneral() throws IOException {
        App.setRoot("vistaBusquedaGeneral");
    }

    /**
     * Maneja la acción del botón "Salir". Cierra la sesión del usuario y cambia
     * la vista actual a la vista de login.
     *
     * @throws IOException si ocurre un error al cambiar la vista
     */
    @FXML
    private void _salir() throws IOException {
        actividad = "Cerrar Sesion";
        sesion1 = new ConexionDB(SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre(),
                SingletonDatosUsuario.getInstance().getDatosUsuario().getPass(),
                actividad);

        try {
            sesion1.cerrarSesion(); // Intenta cerrar la sesión
        } catch (SQLException e) {
            System.out.println("Error al cerrar sesión para el usuario: "
                    + SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre());
            e.printStackTrace(); // Imprime el stack trace para depuración
        }
        App.setRoot("_login"); // Cambia la vista a la vista de login
    }
}
