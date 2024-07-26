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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador para la vista de edición de bautismos.
 * Maneja la interacción del usuario con la interfaz de edición de bautismos.
 * 
 * Autor: walyn
 */
public class VistaEdicionBautismoController implements Initializable {

    @FXML private String busqueda; // Texto de búsqueda
    @FXML private TextField txtBusquedaB; // Campo de texto para la búsqueda

    @FXML private TableView<FeligresDetalle> tablaFeligreses; // Tabla para mostrar los feligreses

    // Columnas de la tabla
    @FXML private TableColumn<FeligresDetalle, String> nombreColumn;
    @FXML private TableColumn<FeligresDetalle, String> apellidoColumn;
    @FXML private TableColumn<FeligresDetalle, Integer> libroColumn;
    @FXML private TableColumn<FeligresDetalle, Integer> folioColumn;
    @FXML private TableColumn<FeligresDetalle, Integer> partidaColumn;
    @FXML private TableColumn<FeligresDetalle, String> padreColumn;
    @FXML private TableColumn<FeligresDetalle, String> madreColumn;
    @FXML private TableColumn<FeligresDetalle, LocalDate> nacimientoColumn;
    @FXML private TableColumn<FeligresDetalle, Integer> edadColumn;
    @FXML private TableColumn<FeligresDetalle, String> lugarNacimientoColumn;
    @FXML private TableColumn<FeligresDetalle, LocalDate> fechaSacramentoColumn;
    @FXML private TableColumn<FeligresDetalle, String> lugarSacramentoColumn;
    @FXML private TableColumn<FeligresDetalle, String> padrinoColumn;
    @FXML private TableColumn<FeligresDetalle, String> madrinaColumn;
    @FXML private TableColumn<FeligresDetalle, String> observacionColumn;
    @FXML private TableColumn<FeligresDetalle, String> registradoColumn;

    /**
     * Inicializa el controlador de la clase.
     * 
     * @param url URL de la ubicación del recurso
     * @param rb  Bundle de recursos para localizar el objeto raíz
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            _consultaBautismo();
        } catch (IOException ex) {
            Logger.getLogger(VistaEdicionBautismoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para consultar los bautismos y llenar la tabla.
     * 
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void _consultaBautismo() throws IOException {
        // Configurar evento de doble clic en las filas de la tabla
        tablaFeligreses.setRowFactory(tv -> {
            TableRow<FeligresDetalle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    FeligresDetalle rowData = row.getItem();
                    SingletonData1.getInstance().setFeligresDetalle(rowData);
                    try {
                        App.setRoot("edicionRegistro");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        // Configurar las columnas de la tabla
        libroColumn.setCellValueFactory(new PropertyValueFactory<>("libro"));
        folioColumn.setCellValueFactory(new PropertyValueFactory<>("folio"));
        partidaColumn.setCellValueFactory(new PropertyValueFactory<>("partida"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        padreColumn.setCellValueFactory(new PropertyValueFactory<>("padre"));
        madreColumn.setCellValueFactory(new PropertyValueFactory<>("madre"));
        nacimientoColumn.setCellValueFactory(new PropertyValueFactory<>("nacimiento"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        lugarNacimientoColumn.setCellValueFactory(new PropertyValueFactory<>("lugarNacimiento"));
        fechaSacramentoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaSacramento"));
        lugarSacramentoColumn.setCellValueFactory(new PropertyValueFactory<>("lugarSacramento"));
        padrinoColumn.setCellValueFactory(new PropertyValueFactory<>("padrino"));
        madrinaColumn.setCellValueFactory(new PropertyValueFactory<>("madrina"));
        observacionColumn.setCellValueFactory(new PropertyValueFactory<>("observacion"));
        registradoColumn.setCellValueFactory(new PropertyValueFactory<>("registrado"));

        Connection connection = ConexionDB.getConexion();
        ObservableList<FeligresDetalle> data = FXCollections.observableArrayList();

        try {
            String query = "SELECT r.libro, r.folio, r.partida, f.nombre, f.apellido, f.padreFeligres, f.madreFeligres, " +
                           "f.nacimiento, f.edadFeligres, f.lugarNacimiento, b.fechaSacramento, b.lugarSacramento, " +
                           "b.padrino, b.madrina, o.observacion, r.inscritoLibro " +
                           "FROM feligres f " +
                           "JOIN bautismo b ON f.idFeligres = b.idFeligres " +
                           "JOIN registrolibro r ON b.idBautismo = r.bautismo_idBautismo " +
                           "JOIN observacion o ON b.idBautismo = o.bautismo_idBautismo";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new FeligresDetalle(
                    rs.getInt("libro"), rs.getInt("folio"), rs.getInt("partida"),
                    rs.getString("nombre"), rs.getString("apellido"),
                    rs.getString("padreFeligres"), rs.getString("madreFeligres"),
                    rs.getDate("nacimiento").toLocalDate(), rs.getInt("edadFeligres"),
                    rs.getString("lugarNacimiento"), rs.getDate("fechaSacramento").toLocalDate(),
                    rs.getString("lugarSacramento"), rs.getString("padrino"),
                    rs.getString("madrina"), rs.getString("observacion"),
                    rs.getString("inscritoLibro")
                ));
            }

            if (data.isEmpty()) {
                showAlert("Información", "Bautismos, No se encontraron resultados", Alert.AlertType.INFORMATION);
                tablaFeligreses.setItems(FXCollections.observableArrayList()); // Limpiar la tabla
            } else {
                tablaFeligreses.setItems(data);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Hubo un error al realizar la búsqueda.", Alert.AlertType.ERROR);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para consultar bautismos basados en una búsqueda.
     * 
     * @throws IOException si ocurre un error de entrada/salida
     */
    @FXML
    private void _consultaBautismoBusqueda() throws IOException {
        busqueda = txtBusquedaB.getText();
        if (busqueda.trim().isEmpty()) {
            showAlert("Error", "El campo de búsqueda no puede estar vacío.", Alert.AlertType.ERROR);
            return;
        }

        // Configurar las columnas de la tabla
        libroColumn.setCellValueFactory(new PropertyValueFactory<>("libro"));
        folioColumn.setCellValueFactory(new PropertyValueFactory<>("folio"));
        partidaColumn.setCellValueFactory(new PropertyValueFactory<>("partida"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        padreColumn.setCellValueFactory(new PropertyValueFactory<>("padre"));
        madreColumn.setCellValueFactory(new PropertyValueFactory<>("madre"));
        nacimientoColumn.setCellValueFactory(new PropertyValueFactory<>("nacimiento"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        lugarNacimientoColumn.setCellValueFactory(new PropertyValueFactory<>("lugarNacimiento"));
        fechaSacramentoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaSacramento"));
        lugarSacramentoColumn.setCellValueFactory(new PropertyValueFactory<>("lugarSacramento"));
        padrinoColumn.setCellValueFactory(new PropertyValueFactory<>("padrino"));
        madrinaColumn.setCellValueFactory(new PropertyValueFactory<>("madrina"));
        observacionColumn.setCellValueFactory(new PropertyValueFactory<>("observacion"));
        registradoColumn.setCellValueFactory(new PropertyValueFactory<>("registrado"));

        Connection connection = ConexionDB.getConexion();
        ObservableList<FeligresDetalle> data = FXCollections.observableArrayList();

        try {
            String query = "SELECT r.libro, r.folio, r.partida, f.nombre, f.apellido, f.padreFeligres, f.madreFeligres, " +
                           "f.nacimiento, f.edadFeligres, f.lugarNacimiento, b.fechaSacramento, b.lugarSacramento, " +
                           "b.padrino, b.madrina, o.observacion, r.inscritoLibro " +
                           "FROM feligres f " +
                           "JOIN bautismo b ON f.idFeligres = b.idFeligres " +
                           "JOIN registrolibro r ON b.idBautismo = r.bautismo_idBautismo " +
                           "JOIN observacion o ON b.idBautismo = o.bautismo_idBautismo " +
                           "WHERE nombre LIKE ? OR apellido LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + busqueda + "%");
            stmt.setString(2, "%" + busqueda + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new FeligresDetalle(
                    rs.getInt("libro"), rs.getInt("folio"), rs.getInt("partida"),
                    rs.getString("nombre"), rs.getString("apellido"),
                    rs.getString("padreFeligres"), rs.getString("madreFeligres"),
                    rs.getDate("nacimiento").toLocalDate(), rs.getInt("edadFeligres"),
                    rs.getString("lugarNacimiento"), rs.getDate("fechaSacramento").toLocalDate(),
                    rs.getString("lugarSacramento"), rs.getString("padrino"),
                    rs.getString("madrina"), rs.getString("observacion"),
                    rs.getString("inscritoLibro")
                ));
            }

            if (data.isEmpty()) {
                showAlert("Información", "Bautismos, No se encontraron resultados para: " + busqueda, Alert.AlertType.INFORMATION);
                tablaFeligreses.setItems(FXCollections.observableArrayList()); // Limpiar la tabla
                _consultaBautismo();
            } else {
                tablaFeligreses.setItems(data);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Hubo un error al realizar la búsqueda.", Alert.AlertType.ERROR);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para regresar a la vista anterior.
     * 
     * @throws IOException si ocurre un error de entrada/salida
     */
    @FXML
    private void regresar() throws IOException {
        App.setRoot("vistaSacramentos");
    }

    /**
     * Función para mostrar alertas fácilmente.
     * 
     * @param title Título de la alerta
     * @param content Contenido de la alerta
     * @param alertType Tipo de alerta
     */
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
