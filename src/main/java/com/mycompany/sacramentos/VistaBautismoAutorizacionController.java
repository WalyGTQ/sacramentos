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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class VistaBautismoAutorizacionController implements Initializable {
    // Campos para el ingreso de datos

    @FXML
    private TextField aparroquia, parroco, padre, madre, nino, Lnacimiento, padrino, madrina;
    @FXML
    private TextArea obs;
    @FXML
    private DatePicker Fnacimiento;
    @FXML
    private Label _v1, _v2, _v3, _v4, _v5, _v6, _v7;
    @FXML
    private TabPane tabpane;

    @FXML
    private String busqueda; // Texto de búsqueda
    @FXML
    private TextField txtBusqueda; // Campo de texto para la búsqueda
    @FXML
    private TableView<DatosAutorizaciones> tabla; // Tabla para mostrar los registros de autorizaciones
    //Declaraciones de cada una de las columnas
    @FXML
    private TableColumn<DatosAutorizaciones, Integer> _id;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _parroquia;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _parroco;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _padre;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _madre;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _nino;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _Lnacimiento;
    @FXML
    private TableColumn<DatosAutorizaciones, LocalDate> _Fnacimiento;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _padrino;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _madrina;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _obs;
    @FXML
    private TableColumn<DatosAutorizaciones, LocalDateTime> _creacion;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _creador;
    @FXML
    private TableColumn<DatosAutorizaciones, LocalDateTime> _modificacion;
    @FXML
    private TableColumn<DatosAutorizaciones, String> _modificador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         int tabIndexToSelect = EstadoPane.getInstance().getSelectedTabIndex();
         tabpane.getSelectionModel().select(tabIndexToSelect);
        // TODO
    }
    
    @FXML
    private void consulta() throws IOException {
                // Configurar evento de doble clic en las filas de la tabla
        tabla.setRowFactory(tv -> {
            TableRow<DatosAutorizaciones> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    DatosAutorizaciones rowData = row.getItem();
                    SingletonDatosAutorizaciones.getInstance().setDatosAutorizaciones(rowData);
                    try {
                        App.setRoot("vistaBautismoAutorizacionImpresion");
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });


        _id.setCellValueFactory(new PropertyValueFactory<>("id"));
        _parroquia.setCellValueFactory(new PropertyValueFactory<>("parroquia"));
        _parroco.setCellValueFactory(new PropertyValueFactory<>("parroco"));
        _padre.setCellValueFactory(new PropertyValueFactory<>("padre"));
        _madre.setCellValueFactory(new PropertyValueFactory<>("madre"));
        _nino.setCellValueFactory(new PropertyValueFactory<>("nino"));
        _Lnacimiento.setCellValueFactory(new PropertyValueFactory<>("lnacimiento"));
        _Fnacimiento.setCellValueFactory(new PropertyValueFactory<>("fnacimiento"));
        _padrino.setCellValueFactory(new PropertyValueFactory<>("padrino"));
        _madrina.setCellValueFactory(new PropertyValueFactory<>("madrina"));
        _obs.setCellValueFactory(new PropertyValueFactory<>("obs"));
        _creacion.setCellValueFactory(new PropertyValueFactory<>("creacion"));
        _creador.setCellValueFactory(new PropertyValueFactory<>("creador"));
        _modificacion.setCellValueFactory(new PropertyValueFactory<>("modificacion"));
        _modificador.setCellValueFactory(new PropertyValueFactory<>("modificador"));

        ObservableList<DatosAutorizaciones> data = FXCollections.observableArrayList();
        Connection connection = ConexionDB.getConexion();
        // Consulta SQL para seleccionar los campos específicos de la tabla autbautismo
        String sql = "SELECT idaut, dirigida, aparroco, padre, madre, nino, lugarNacimiento, "
                + "fechaNacimiento, padrino, madrina, observacion, fechaSolicitud, "
                + "usuarioCreador, actualizacion, usuarioModificador "
                + "FROM autbautismo";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new DatosAutorizaciones(
                        rs.getInt("idaut"), 
                        rs.getString("dirigida"), 
                        rs.getString("aparroco"),
                        rs.getString("padre"), 
                        rs.getString("madre"), 
                        rs.getString("nino"), 
                        rs.getString("lugarNacimiento"), 
                        rs.getDate("fechaNacimiento").toLocalDate(), 
                        rs.getString("padrino"), 
                        rs.getString("madrina"),
                        rs.getString("observacion"),
                        rs.getTimestamp("fechaSolicitud").toLocalDateTime(),
                        rs.getString("usuarioCreador"), 
                        rs.getTimestamp("actualizacion").toLocalDateTime(),
                        rs.getString("usuarioModificador")
                ));
            }

            if (data.isEmpty()) {
                showAlert("Información", "Bautismos, No se encontraron resultados", Alert.AlertType.INFORMATION);
                tabla.setItems(FXCollections.observableArrayList()); // Limpiar la tabla
            } else {
                tabla.setItems(data);
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

    //Metodo para regresar
    @FXML
    private void regresar() throws IOException {
       int tabIndexToSelect = 0; 
        EstadoPane.getInstance().setSelectedTabIndex(tabIndexToSelect);
        App.setRoot("vistaSacramentos");
    }

    @FXML
    private void guardar() throws IOException, SQLException {
        String Parroquia = aparroquia.getText();
        String Parroco = parroco.getText();
        String Padre = padre.getText();
        String Madre = madre.getText();
        String Nino = nino.getText();
        String LNacimiento = Lnacimiento.getText();
        String Padrino = padrino.getText();
        String Madrina = madrina.getText();
        String Obs = obs.getText();
        LocalDate FNacimiento = Fnacimiento.getValue();

        // Verifica que campos son obligatorios de llenar
        if (aparroquia.getText().trim().isEmpty() || padre.getText().trim().isEmpty() || madre.getText().trim().isEmpty() || nino.getText().trim().isEmpty() || Fnacimiento.getValue().toString().trim().isEmpty() || padrino.getText().trim().isEmpty() || madrina.getText().trim().isEmpty()) {
            showAlert("Validación de Campos", "Los campos en Rojo no pueden quedar vacíos.", Alert.AlertType.WARNING);
            _v1.setStyle("-fx-text-fill: red;");
            _v2.setStyle("-fx-text-fill: red;");
            _v3.setStyle("-fx-text-fill: red;");
            _v4.setStyle("-fx-text-fill: red;");
            _v5.setStyle("-fx-text-fill: red;");
            _v6.setStyle("-fx-text-fill: red;");
            _v7.setStyle("-fx-text-fill: red;");

            return;
        }
        // Comprueba si alguna de las cadenas contiene números
        if (Padre.matches(".*\\d.*") || Madre.matches(".*\\d.*") || Nino.matches(".*\\d.*") || Padrino.matches(".*\\d.*") || Madrina.matches(".*\\d.*")) {
            showAlert("Entrada no válida", "Algunos campos Contienen Valores Numericos.", Alert.AlertType.WARNING);
            return;
        }
        // Declarando variables de conexión y statement.
        Connection conn = null;
        //PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Obtén la fecha de nacimiento del DatePicker
//            LocalDate fechaacimiento = datePikerFechaNacimientoB.getValue();
//            LocalDate fechaInscripcion = datePikerFechaB.getValue();

            // Calcula la edad
            LocalDate hoy = LocalDate.now();

            // Validaciones de fechas
            if (FNacimiento.isAfter(hoy)) {
                showAlert("Error", "La fecha de Nacimiento no puede ser después de hoy.", Alert.AlertType.ERROR);
                return;
            }

            // Obteniendo la conexión utilizando la clase ConexionDB
            conn = ConexionDB.getConexion();

            // Creando la consulta SQL para insertar datos en la tabla feligres
            // Definir la consulta SQL de inserción
            String sql = "INSERT INTO autBautismo ("
                    + "dirigida, aparroco, padre, madre, nino, lugarNacimiento, fechaNacimiento, "
                    + "padrino, madrina, observacion, fechaSolicitud, usuarioCreador, actualizacion, usuarioModificador"
                    + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Crear un PreparedStatement
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Establecer los valores para la inserción
            pstmt.setString(1, Parroquia);
            pstmt.setString(2, Parroco);
            pstmt.setString(3, Padre);
            pstmt.setString(4, Madre);
            pstmt.setString(5, Nino);
            pstmt.setString(6, LNacimiento);
            pstmt.setDate(7, Date.valueOf(FNacimiento)); // Convertir LocalDate a java.sql.Date
            pstmt.setString(8, Padrino);
            pstmt.setString(9, Madrina);
            pstmt.setString(10, Obs);
            pstmt.setObject(11, LocalDateTime.now()); // Usar LocalDateTime para fecha y hora actuales
            pstmt.setString(12, SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre());
            pstmt.setObject(13, LocalDateTime.now()); // Usar LocalDateTime para la actualización actual
            pstmt.setString(14, SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre());
            pstmt.executeUpdate();
            limpiarCampos();
            mostrarAlerta();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    private void mostrarAlerta() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Registro Exitoso");
        alert.setHeaderText("La Autorización fue generada con éxito.");
        alert.setContentText("¿Desea imprimir la autorización?");

        ButtonType buttonTypeImprimir = new ButtonType("Imprimir");
        ButtonType buttonTypeNoImprimir = new ButtonType("No Imprimir");

        alert.getButtonTypes().setAll(buttonTypeImprimir, buttonTypeNoImprimir);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeImprimir) {
            // Código para manejar la impresión
            System.out.println("El usuario eligió imprimir.");
            // Aquí puedes llamar a tu método de impresión
        } else if (result.get() == buttonTypeNoImprimir) {
            tabpane.getSelectionModel().select(1);
            // Código para manejar la decisión de no imprimir
            System.out.println("El usuario eligió no imprimir.");
            // Aquí puedes redirigir a otra vista
            // App.setRoot("otraVista");
        }
    }

    // Método para limpiar los campos del formulario
    public void limpiarCampos() {
        aparroquia.clear();
        parroco.clear();
        padre.clear();
        madre.clear();
        nino.clear();
        Lnacimiento.clear();
        padrino.clear();
        madrina.clear();
        obs.clear();
        Fnacimiento.setValue(null);
    }

    // Función para mostrar alertas fácilmente
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
