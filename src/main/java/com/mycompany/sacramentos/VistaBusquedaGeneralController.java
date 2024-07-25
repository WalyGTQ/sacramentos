/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sacramentos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class VistaBusquedaGeneralController implements Initializable {

    @FXML
    private Label nombreUsuario; //Asignacion Label Usuario
    private String busqueda;//Asignacion del texto en eltext field a variable

    @FXML
    private TableView<FELIGRES> tbFeligreses;//Asignacion tabla
    //Inicio Asignacion de columnas tbFeligreses
    @FXML
    private TableColumn<FELIGRES, Integer> id;
    @FXML
    private TableColumn<FELIGRES, String> nombre;
    @FXML
    private TableColumn<FELIGRES, String> apellido;
    @FXML
    private TableColumn<FELIGRES, LocalDate> nacimiento;
    @FXML
    private TableColumn<FELIGRES, Integer> edadFeligres;
    @FXML
    private TableColumn<FELIGRES, String> lugarNacimiento;
    @FXML
    private TableColumn<FELIGRES, String> padreFeligres;
    @FXML
    private TableColumn<FELIGRES, String> madreFeligres;
    @FXML
    private TableColumn<FELIGRES, String> direccion;
    @FXML
    private TableColumn<FELIGRES, String> telefono;
    @FXML
    private TableColumn<FELIGRES, String> correo;
    @FXML
    private TableColumn<FELIGRES, String> observaciones;
    @FXML
    private TableColumn<FELIGRES, String> feligresDe;
    @FXML
    private TableColumn<FELIGRES, String> genero;
    @FXML
    private TableColumn<FELIGRES, String> jurisdiccion;
    @FXML
    private TableColumn<FELIGRES, String> religion;
    @FXML
    private TableColumn<FELIGRES, String> ocupacion;
    @FXML
    private TableColumn<FELIGRES, String> dpi;
    @FXML
    private TableColumn<FELIGRES, Integer> comunidad_idComunidad;
    //Fin  Asignacion de columnas Tabla tbFeligreses

    @FXML
    private TextField txtBusquedaRapida;//Asignacion de txt para busqueda rapida

    //Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nombreUsuario.setText(SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre());
            consulta2();
            // TODO
        } catch (IOException ex) {
            Logger.getLogger(VistaBusquedaGeneralController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VistaBusquedaGeneralController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Inicia el metodo principa para la busqueda de los datos en la base de datos
    @FXML
    public void buscar() throws IOException, SQLException {
        // Validación para verificar si el TextField está vacío
        busqueda = txtBusquedaRapida.getText();
        if (busqueda.trim().isEmpty()) {
            showAlert("Error", "El campo de búsqueda no puede estar vacío.", Alert.AlertType.ERROR);
        }

        consulta();

    }

// Método que realiza una consulta a la base de datos y pasa los datos a la tabla
    private void consulta() throws IOException, SQLException {
        // Configurar las columnas de la tabla con las propiedades del objeto FELIGRES
        id.setCellValueFactory(new PropertyValueFactory<>("idFeligres"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        nacimiento.setCellValueFactory(new PropertyValueFactory<>("nacimiento"));
        edadFeligres.setCellValueFactory(new PropertyValueFactory<>("edadFeligres"));
        lugarNacimiento.setCellValueFactory(new PropertyValueFactory<>("lugarNacimiento"));
        padreFeligres.setCellValueFactory(new PropertyValueFactory<>("padreFeligres"));
        madreFeligres.setCellValueFactory(new PropertyValueFactory<>("madreFeligres"));
        direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        observaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        feligresDe.setCellValueFactory(new PropertyValueFactory<>("feligresDe"));
        genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        jurisdiccion.setCellValueFactory(new PropertyValueFactory<>("jurisdiccion"));
        religion.setCellValueFactory(new PropertyValueFactory<>("religion"));
        ocupacion.setCellValueFactory(new PropertyValueFactory<>("ocupacion"));
        dpi.setCellValueFactory(new PropertyValueFactory<>("dpi"));
        comunidad_idComunidad.setCellValueFactory(new PropertyValueFactory<>("comunidad_idComunidad"));

        Connection connection = null; // Declarar la conexión fuera del bloque try para asegurar el cierre en finally
        ObservableList<FELIGRES> data = FXCollections.observableArrayList(); // Crear una lista observable para almacenar los datos

        try {
            connection = ConexionDB.getConexion(); // Obtener la conexión a la base de datos
            String query = "SELECT idFeligres, nombre, apellido, nacimiento, edadFeligres, lugarNacimiento, "
                    + "padreFeligres, madreFeligres, direccion, telefono, correo, observaciones, "
                    + "feligresDe, genero, jurisdiccion, religion, ocupacion, dpi, comunidad_idComunidad "
                    + "FROM feligres "
                    + "WHERE nombre LIKE ? OR apellido LIKE ?";

            PreparedStatement stmt = connection.prepareStatement(query); // Preparar la consulta SQL
            stmt.setString(1, "%" + busqueda + "%"); // Establecer el primer parámetro de la consulta
            stmt.setString(2, "%" + busqueda + "%"); // Establecer el segundo parámetro de la consulta
            ResultSet rs = stmt.executeQuery(); // Ejecutar la consulta y obtener los resultados

            while (rs.next()) {
                LocalDate nacimiento = null; // Inicializar la variable de fecha de nacimiento
                if (rs.getDate("nacimiento") != null) {
                    nacimiento = rs.getDate("nacimiento").toLocalDate(); // Convertir java.sql.Date a java.time.LocalDate
                }

                // Crear un nuevo objeto FELIGRES con los datos obtenidos
                FELIGRES feligres = new FELIGRES(
                        rs.getInt("idFeligres"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        nacimiento,
                        rs.getInt("edadFeligres"),
                        rs.getString("lugarNacimiento"),
                        rs.getString("padreFeligres"),
                        rs.getString("madreFeligres"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("observaciones"),
                        rs.getString("feligresDe"),
                        rs.getString("genero"),
                        rs.getString("jurisdiccion"),
                        rs.getString("religion"),
                        rs.getString("ocupacion"),
                        rs.getString("dpi"),
                        rs.getInt("comunidad_idComunidad")
                );
                data.add(feligres); // Agregar el objeto a la lista observable
            }

            // Verificar si no se encontraron resultados
            if (data.isEmpty()) {
                showAlert("Información", "No se encontraron resultados para: " + busqueda, Alert.AlertType.INFORMATION);
                tbFeligreses.setItems(FXCollections.observableArrayList()); // Limpiar la tabla
            } else {
                tbFeligreses.setItems(data); // Establecer los datos en la tabla
            }

        } catch (SQLException ex) {
            // Mostrar una alerta en caso de error en la búsqueda
            showAlert("Error", "Hubo un error al realizar la búsqueda.", Alert.AlertType.ERROR);
        } finally {
            // Asegurar que la conexión se cierra
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Imprimir el stack trace en caso de error al cerrar la conexión
                }
            }
        }
    }

    @FXML // Método que realiza una consulta a la base de datos y pasa los datos a la tabla
    private void consulta2() throws IOException, SQLException {
        // Configurar las columnas de la tabla con las propiedades del objeto FELIGRES
        id.setCellValueFactory(new PropertyValueFactory<>("idFeligres"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        nacimiento.setCellValueFactory(new PropertyValueFactory<>("nacimiento"));
        edadFeligres.setCellValueFactory(new PropertyValueFactory<>("edadFeligres"));
        lugarNacimiento.setCellValueFactory(new PropertyValueFactory<>("lugarNacimiento"));
        padreFeligres.setCellValueFactory(new PropertyValueFactory<>("padreFeligres"));
        madreFeligres.setCellValueFactory(new PropertyValueFactory<>("madreFeligres"));
        direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        observaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        feligresDe.setCellValueFactory(new PropertyValueFactory<>("feligresDe"));
        genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        jurisdiccion.setCellValueFactory(new PropertyValueFactory<>("jurisdiccion"));
        religion.setCellValueFactory(new PropertyValueFactory<>("religion"));
        ocupacion.setCellValueFactory(new PropertyValueFactory<>("ocupacion"));
        dpi.setCellValueFactory(new PropertyValueFactory<>("dpi"));
        comunidad_idComunidad.setCellValueFactory(new PropertyValueFactory<>("comunidad_idComunidad"));

        Connection connection = null; // Declarar la conexión fuera del bloque try para asegurar el cierre en finally
        ObservableList<FELIGRES> data = FXCollections.observableArrayList(); // Crear una lista observable para almacenar los datos

        try {
            connection = ConexionDB.getConexion(); // Obtener la conexión a la base de datos
            String query = "SELECT idFeligres, nombre, apellido, nacimiento, edadFeligres, lugarNacimiento, "
                    + "padreFeligres, madreFeligres, direccion, telefono, correo, observaciones, "
                    + "feligresDe, genero, jurisdiccion, religion, ocupacion, dpi, comunidad_idComunidad "
                    + "FROM feligres"; // Consulta sin cláusula WHERE

            PreparedStatement stmt = connection.prepareStatement(query); // Preparar la consulta SQL
            ResultSet rs = stmt.executeQuery(); // Ejecutar la consulta y obtener los resultados

            while (rs.next()) {
                LocalDate nacimiento = null; // Inicializar la variable de fecha de nacimiento
                if (rs.getDate("nacimiento") != null) {
                    nacimiento = rs.getDate("nacimiento").toLocalDate(); // Convertir java.sql.Date a java.time.LocalDate
                }

                // Crear un nuevo objeto FELIGRES con los datos obtenidos
                FELIGRES feligres = new FELIGRES(
                        rs.getInt("idFeligres"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        nacimiento,
                        rs.getInt("edadFeligres"),
                        rs.getString("lugarNacimiento"),
                        rs.getString("padreFeligres"),
                        rs.getString("madreFeligres"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("observaciones"),
                        rs.getString("feligresDe"),
                        rs.getString("genero"),
                        rs.getString("jurisdiccion"),
                        rs.getString("religion"),
                        rs.getString("ocupacion"),
                        rs.getString("dpi"),
                        rs.getInt("comunidad_idComunidad")
                );
                data.add(feligres); // Agregar el objeto a la lista observable
            }

            // Verificar si no se encontraron resultados
            if (data.isEmpty()) {
                showAlert("Información", "No se encontraron resultados.", Alert.AlertType.INFORMATION);
                tbFeligreses.setItems(FXCollections.observableArrayList()); // Limpiar la tabla
            } else {
                tbFeligreses.setItems(data); // Establecer los datos en la tabla
            }

        } catch (SQLException ex) {
            // Mostrar una alerta en caso de error en la búsqueda
            showAlert("Error", "Hubo un error al realizar la búsqueda.", Alert.AlertType.ERROR);
        } finally {
            // Asegurar que la conexión se cierra
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Imprimir el stack trace en caso de error al cerrar la conexión
                }
            }
        }
    }

    //Unicamente retorna a la vista Principal
    @FXML
    public void regresar() throws IOException {
        App.setRoot("menuPrincipal");
    }
    // Función para mostrar alertas fácilmente

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
