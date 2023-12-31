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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class ConfirmacionController implements Initializable {

    private int total;
    //LLamamos los Graficos y ComboBox PAra Filtro

    @FXML
    private BarChart<String, Number> bcConfirmacion;
    @FXML
    private PieChart pcConfirmacion;
    @FXML
    private LineChart<String, Number> lcConfirmacion;
    @FXML
    private ComboBox<String> cbFiltroC;

    //Inicio Campo Para Consulta de Confirmacion
    @FXML
    private TableView<ConsultaConfirmacion> tvConfirmacion;
    @FXML
    private TableColumn<ConsultaConfirmacion, Integer> tcLibroCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, Integer> tcFolioCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, Integer> tcPartidaCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcNombreCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcApellidoCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, LocalDate> tcNacimientoCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, Integer> tcEdadCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcCelebranteCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, LocalDate> tcFechaCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcLugarCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcPadreCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcMadreCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcPadrinosCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcInscritoCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcObservacionesCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, LocalDate> tcRegistroCf;
    //Fin Campos Para Consulta de Confirmacion

    //LLamado a los elementos del FXML
    @FXML
    private TextField txtLibroCf, txtFolioCf, txtPartidaCf, txtNombreCf, txtApellidoCf, txtPadreCf, txtMadreCf, txtPadrino_MadrinaCf, txtLugarCf, txtCelebranteCf;
    @FXML
    private TextArea txtAreaCf;
    @FXML
    private CheckBox boxInscritoCf;
    @FXML
    private DatePicker dpFechaSacCf, dpNacimientoCf;
    private String check;

    @FXML
    private TextField txtBusqueda;
    private String busqueda;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bcConfirmacion.setVisible(false);
        lcConfirmacion.setVisible(false);
        pcConfirmacion.setVisible(false);
        try {
            _cargarGraficos();
        } catch (IOException ex) {
            Logger.getLogger(ConfirmacionController.class.getName()).log(Level.SEVERE, null, ex);
        }

        cbFiltroC.getItems().addAll("Por Edad", "Por Año", "Inscritos al Libro");
        //miTabPaneB.getSelectionModel().select(1);
        // Agregar el evento de cambio de selección al ComboBox
        cbFiltroC.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String opcionSeleccionada = cbFiltroC.getSelectionModel().getSelectedItem();

                // Verificar la opción seleccionada y ejecutar la acción correspondiente
                if ("Por Edad".equals(opcionSeleccionada)) {
                    bcConfirmacion.setVisible(true);
                    lcConfirmacion.setVisible(false);
                    pcConfirmacion.setVisible(false);
                    cargarDistribucionEdades();
                } else if ("Por Año".equals(opcionSeleccionada)) {
                    bcConfirmacion.setVisible(false);
                    lcConfirmacion.setVisible(true);
                    pcConfirmacion.setVisible(false);
                    cargarConfirmacionesPorAño();
                    // ...
                } else if ("Inscritos al Libro".equals(opcionSeleccionada)) {
                    bcConfirmacion.setVisible(false);
                    lcConfirmacion.setVisible(false);
                    pcConfirmacion.setVisible(true);
                    cargarInscritosVsNoInscritos();
                }
            }
        });
    }

    @FXML
    private void _regresar() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void _guardarCf() throws IOException {
        Integer libro, folio, partida;
        // Obteniendo los valores de los campos de texto y otros componentes
        String nombre = txtNombreCf.getText();
        String apellido = txtApellidoCf.getText();
        String padre = txtPadreCf.getText();
        String madre = txtMadreCf.getText();
        String padrino = txtPadrino_MadrinaCf.getText();
        String celebrante = txtCelebranteCf.getText();
        String lugarConfirmacion = txtLugarCf.getText();
        String observaciones = txtAreaCf.getText();
        boolean inscrito = boxInscritoCf.isSelected();
        LocalDate fechaConfirmacion = dpFechaSacCf.getValue();
        LocalDate fechaNacimiento = dpNacimientoCf.getValue();

        // Declarando variables de conexión y statement.
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        PreparedStatement pstmt5 = null;
        ResultSet rs = null;

        // Verifica primero si los campos libro, folio o partida están vacíos
        if (txtLibroCf.getText().trim().isEmpty() || txtFolioCf.getText().trim().isEmpty() || txtPartidaCf.getText().trim().isEmpty() || txtNombreCf.getText().trim().isEmpty() || txtApellidoCf.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validación de Campos");
            alert.setHeaderText(null);
            alert.setContentText("Los campos Libro, Folio, Partida, Nombre y Apellidos del Confirmante, no pueden quedar vacíos.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Intenta convertir las entradas a Integer y atrapa cualquier NumberFormatException
        try {
            libro = Integer.valueOf(txtLibroCf.getText().trim());
            folio = Integer.valueOf(txtFolioCf.getText().trim());
            partida = Integer.valueOf(txtPartidaCf.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingresa valores numéricos válidos en los campos Libro, Folio y Partida.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Comprueba si alguna de las cadenas contiene números
        if (nombre.matches(".*\\d.*") || apellido.matches(".*\\d.*") || padre.matches(".*\\d.*") || madre.matches(".*\\d.*") || padrino.matches(".*\\d.*") || celebrante.matches(".*\\d.*")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Los campos de nombre, apellido, padre y madre no deben contener números. Por favor, corrija e intente nuevamente.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }
        //Inicia la Query SQL
        try {

            // Obtén la fecha de nacimiento del DatePicker
            LocalDate fechaNacimientoC = dpNacimientoCf.getValue();
            LocalDate fechaInscripcion = dpFechaSacCf.getValue();

            // Calcula la edad
            LocalDate hoy = LocalDate.now();
            int edad = fechaInscripcion.getYear() - fechaNacimientoC.getYear();
            if (fechaNacimientoC.getDayOfYear() > fechaInscripcion.getDayOfYear()) {
                edad--; // Ajusta la edad si el cumpleaños de este año aún no ha llegado.
            }
            if (fechaNacimientoC.isAfter(hoy)) {
                showAlert("Error", "La fecha de Nacimiento no puede ser despues de hoy ", Alert.AlertType.ERROR);
                return;
            }
            if (fechaNacimientoC.isAfter(fechaInscripcion)) {
                showAlert("Error", "El Sacramento no puede ser Antes del Nacimiento", Alert.AlertType.ERROR);
                return;
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
            String sql = "INSERT INTO feligres (nombre, apellido, padreFeligres, madreFeligres, nacimiento, edadFeligres) VALUES (?, ?, ?, ?, ?, ?)";

            // Preparando la consulta SQL
            pstmt1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, nombre);
            pstmt1.setString(2, apellido);
            pstmt1.setString(3, madre);
            pstmt1.setString(4, padre);
            pstmt1.setDate(5, Date.valueOf(fechaNacimiento));
            pstmt1.setInt(6, edad);
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
                    String sql3 = "INSERT INTO confirmacion (fechaSacramento, lugarSacramento, idFeligres, celebrante_idCelebrante, padrino, fechaInscripcion) VALUES (?, ?, ?, ?, ?, NOW() )";
                    // Preparando la consulta SQL
                    pstmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
                    pstmt3.setDate(1, Date.valueOf(fechaConfirmacion));
                    pstmt3.setString(2, lugarConfirmacion);
                    pstmt3.setInt(3, idGeneradoF);
                    pstmt3.setInt(4, idGeneradoC);
                    pstmt3.setString(5, padrino);
                    // Ejecutando la consulta SQL
                    pstmt3.executeUpdate();

                    // Obtener el ID generado Celebrante
                    rs = pstmt3.getGeneratedKeys();
                    if (rs.next()) {
                        int idGeneradoS = rs.getInt(1);

                        //Insertado a la tabla de Registro del Sacramento
                        String sql2 = "INSERT INTO registrolibro (libro, folio, partida, inscritoLibro, confirmacion_idConfirmacion) VALUES (?, ?, ?, ?, ?)";
                        // Preparando la consulta SQL
                        pstmt2 = conn.prepareStatement(sql2);
                        pstmt2.setInt(1, libro);
                        pstmt2.setInt(2, folio);
                        pstmt2.setInt(3, partida);
                        pstmt2.setString(4, check);
                        pstmt2.setInt(5, idGeneradoS);
                        // Ejecutando la consulta SQL
                        pstmt2.executeUpdate();

                        //Insertado a la tabla de bbservacion del Sacramento
                        String sql4 = "INSERT INTO observacion (observacion, confirmacion_idConfirmacion) VALUES (?,?)";
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

    }//La siguiente Llave termina _guardarCf

    @FXML
    private void _busquedaAutomatica() throws IOException {
        //Escucha el evento del doble Clic
        tvConfirmacion.setRowFactory(tv -> {
            TableRow<ConsultaConfirmacion> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ConsultaConfirmacion rowData = row.getItem();
                    SingletonConfirmacion.getInstance().setFeligresDetalle(rowData); // Guarda los datos en el Singleton
                    try {
                        App.setRoot("edicionRegistroC");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        // Configuración de las columnas usando PropertyValueFactory
        tcLibroCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("libroCf"));
        tcFolioCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("folioCf"));
        tcPartidaCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("partidaCf"));
        tcNombreCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("nombreCf"));
        tcApellidoCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("apellidoCf"));
        tcNacimientoCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, LocalDate>("nacimientoCf"));
        tcEdadCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("edadCf"));
        tcCelebranteCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("celebranteCf"));
        tcFechaCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, LocalDate>("fechaCf"));
        tcLugarCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("lugarCf"));
        tcPadreCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("padreCf"));
        tcMadreCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("madreCf"));
        tcPadrinosCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("padrinosCf"));
        tcInscritoCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("inscritoCf"));
        tcObservacionesCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("observacionesCf"));
        tcRegistroCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, LocalDate>("registroCf"));

        ObservableList<ConsultaConfirmacion> data3 = FXCollections.observableArrayList();
        Connection connection = ConexionDB.getConexion();
        try {
            String query = "SELECT r.libro, r.folio, r.partida, r.inscritoLibro, "
                    + "f.nombre, f.apellido, f.nacimiento, f.edadFeligres, f.padreFeligres, f.madreFeligres, "
                    + "cf.fechaInscripcion, cf.fechaSacramento, cf.lugarSacramento, cf.padrino, "
                    + "c.nombreCelebrante,  "
                    + "o.observacion  "
                    + "FROM feligres f "
                    + "JOIN confirmacion cf ON f.idFeligres = cf.idFeligres "
                    + "JOIN celebrante c ON cf.celebrante_idCelebrante = c.idCelebrante "
                    + "JOIN registrolibro r ON cf.idConfirmacion = r.confirmacion_idConfirmacion "
                    + "JOIN observacion o ON cf.idConfirmacion = o.confirmacion_idConfirmacion ";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data3.add(new ConsultaConfirmacion(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getInt("edadFeligres"),
                        rs.getString("nombreCelebrante"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getString("lugarSacramento"),
                        rs.getString("padreFeligres"),
                        rs.getString("madreFeligres"),
                        rs.getString("padrino"),
                        rs.getString("inscritoLibro"),
                        rs.getString("observacion"),
                        rs.getDate("fechaInscripcion").toLocalDate()
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data3.isEmpty()) {
                showAlert("Información", "Confirmacion, No se encontraron resultados  ", Alert.AlertType.INFORMATION);
                tvConfirmacion.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                return;
            }

            tvConfirmacion.setItems(data3);
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

    @FXML
    private void _busquedaEspecifica() throws IOException {
        // Validación para verificar si el TextField está vacío
        busqueda = txtBusqueda.getText();
        if (busqueda.trim().isEmpty()) {
            showAlert("Error", "El campo de búsqueda no puede estar vacío.", Alert.AlertType.ERROR);
            return;
        }

        // Configuración de las columnas usando PropertyValueFactory
        tcLibroCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("libroCf"));
        tcFolioCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("folioCf"));
        tcPartidaCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("partidaCf"));
        tcNombreCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("nombreCf"));
        tcApellidoCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("apellidoCf"));
        tcNacimientoCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, LocalDate>("nacimientoCf"));
        tcEdadCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("edadCf"));
        tcCelebranteCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("celebranteCf"));
        tcFechaCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, LocalDate>("fechaCf"));
        tcLugarCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("lugarCf"));
        tcPadreCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("padreCf"));
        tcMadreCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("madreCf"));
        tcPadrinosCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("padrinosCf"));
        tcInscritoCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("inscritoCf"));
        tcObservacionesCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("observacionesCf"));
        tcRegistroCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, LocalDate>("registroCf"));

        ObservableList<ConsultaConfirmacion> data3 = FXCollections.observableArrayList();
        Connection connection = ConexionDB.getConexion();
        try {
            String query = "SELECT r.libro, r.folio, r.partida, r.inscritoLibro, "
                    + "f.nombre, f.apellido, f.nacimiento, f.edadFeligres, f.padreFeligres, f.madreFeligres, "
                    + "cf.fechaInscripcion, cf.fechaSacramento, cf.lugarSacramento, cf.padrino, "
                    + "c.nombreCelebrante,  "
                    + "o.observacion  "
                    + "FROM feligres f "
                    + "JOIN confirmacion cf ON f.idFeligres = cf.idFeligres "
                    + "JOIN celebrante c ON cf.celebrante_idCelebrante = c.idCelebrante "
                    + "JOIN registrolibro r ON cf.idConfirmacion = r.confirmacion_idConfirmacion "
                    + "JOIN observacion o ON cf.idConfirmacion = o.confirmacion_idConfirmacion "
                    + "WHERE nombre LIKE ? OR apellido LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + busqueda + "%");
            stmt.setString(2, "%" + busqueda + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data3.add(new ConsultaConfirmacion(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getInt("edadFeligres"),
                        rs.getString("nombreCelebrante"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getString("lugarSacramento"),
                        rs.getString("padreFeligres"),
                        rs.getString("madreFeligres"),
                        rs.getString("padrino"),
                        rs.getString("inscritoLibro"),
                        rs.getString("observacion"),
                        rs.getDate("fechaInscripcion").toLocalDate()
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data3.isEmpty()) {
                showAlert("Información", "Confirmacion, No se encontraron resultados  ", Alert.AlertType.INFORMATION);
                tvConfirmacion.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                return;
            }

            tvConfirmacion.setItems(data3);
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

    //Otros Codigos
    public void limpiarCampos() {
        // Limpiar todos los otros campos
        // Limpieza de los TextField
        txtLibroCf.clear();
        txtFolioCf.clear();
        txtPartidaCf.clear();
        txtNombreCf.clear();
        txtApellidoCf.clear();
        txtLugarCf.clear();
        txtPadreCf.clear();
        txtMadreCf.clear();
        txtCelebranteCf.clear();
        txtPadrino_MadrinaCf.clear();

        // Limpieza del TextArea
        txtAreaCf.clear();

        // Limpieza del CheckBox
        boxInscritoCf.setSelected(false);

        // Limpieza de los DatePicker
        dpFechaSacCf.setValue(null);
        dpNacimientoCf.setValue(null);
    }

    // Función para mostrar alertas fácilmente
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Graficas
    //-------------------LineChart Para ver cuantos confirmantes por año
    private Map<String, Integer> obtenerConfirmacionesPorAño() throws SQLException {
        Map<String, Integer> datos = new TreeMap<>(); // TreeMap mantiene las claves en orden
        String sql = "SELECT YEAR(fechaSacramento) as year, COUNT(*) as count FROM confirmacion GROUP BY year";
        try (PreparedStatement pstmt = ConexionDB.getConexion().prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.put(rs.getString("year"), rs.getInt("count"));
            }
        }
        return datos;
    }

    @FXML
    private void cargarConfirmacionesPorAño() {
        lcConfirmacion.getData().clear();
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            Map<String, Integer> datos = obtenerConfirmacionesPorAño();
            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            lcConfirmacion.getData().add(series);
            lcConfirmacion.setAnimated(true);
            series.setName("Confirmaciones por Año");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }///---------------------------------------------Fin LineChart para cuantos confirmantes por año

    //Inicio de Grafico Barchart para obtener Confirmantes por edades
    private Map<String, Integer> obtenerDistribucionEdades() throws SQLException {
        Map<String, Integer> datos = new TreeMap<>();
        String sql = "SELECT f.edadFeligres, COUNT(*) as count FROM feligres  f JOIN confirmacion c ON f.idFeligres = c.idFeligres GROUP BY edadFeligres";
        try (PreparedStatement pstmt = ConexionDB.getConexion().prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.put(String.valueOf(rs.getInt("edadFeligres")), rs.getInt("count"));
            }
        }
        return datos;
    }

    @FXML
    private void cargarDistribucionEdades() {
        bcConfirmacion.getData().clear();
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            Map<String, Integer> datos = obtenerDistribucionEdades();
            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            bcConfirmacion.getData().add(series);
            bcConfirmacion.setAnimated(true);
            series.setName("Distribución de Edades");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //---------------------------------------Fin de Grafico Barchart para obtener Confirmantes por edades

    //Inicio de Grafico PieChart que muestra cuantos registros estan inscritos al Libro
    private Map<String, Integer> obtenerInscritosVsNoInscritos() throws SQLException {
        Map<String, Integer> datos = new HashMap<>();
        String sql = "SELECT r.inscritoLibro, COUNT(*) as count FROM registrolibro r JOIN confirmacion c ON r.confirmacion_idConfirmacion = c.idConfirmacion GROUP BY inscritoLibro";
        try (PreparedStatement pstmt = ConexionDB.getConexion().prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.put(rs.getString("inscritoLibro"), rs.getInt("count"));
            }
        }
        return datos;
    }

    @FXML
    private void cargarInscritosVsNoInscritos() {
        pcConfirmacion.getData().clear();
        try {
            Map<String, Integer> datos = obtenerInscritosVsNoInscritos();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            total = 0;
            for (Integer value : datos.values()) {
                total += value;
            }

            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
                pieChartData.add(slice);
            }

            pcConfirmacion.setData(pieChartData);
            pcConfirmacion.setAnimated(true);

            // Use Platform.runLater() to delay these operations until after nodes are created and rendered.
            Platform.runLater(() -> {
                for (PieChart.Data slice : pcConfirmacion.getData()) {
                    double percentage = (slice.getPieValue() / total) * 100;

                    Tooltip tooltip = new Tooltip(slice.getName() + ": " + slice.getPieValue() + " (" + String.format("%.2f", percentage) + "%)");
                    Tooltip.install(slice.getNode(), tooltip);

                    slice.nameProperty().bind(
                            Bindings.concat(slice.getName(), " ", slice.pieValueProperty(), " (", Bindings.format("%.1f", percentage), "%)")
                    );

                    slice.getNode().setOnMouseEntered(event -> {
                        slice.getNode().setEffect(new DropShadow(7, javafx.scene.paint.Color.BLACK));
                    });
                    slice.getNode().setOnMouseExited(event -> {
                        slice.getNode().setEffect(null);
                    });
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML//Inicializa los graficos cuando sean necesarios
    private void _cargarGraficos() throws IOException {
        cargarConfirmacionesPorAño();
        cargarDistribucionEdades();
        cargarInscritosVsNoInscritos();
    }
}
