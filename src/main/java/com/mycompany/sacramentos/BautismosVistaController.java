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
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class BautismosVistaController implements Initializable {

    private String busqueda;

    @FXML
    private TableView<FeligresDetalle> tablaFeligreses;
    //Campos para la consulta de Bautismo
    @FXML
    private TableColumn<FeligresDetalle, String> nombreColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> apellidoColumn;
    @FXML
    private TableColumn<FeligresDetalle, Integer> libroColumn;
    @FXML
    private TableColumn<FeligresDetalle, Integer> folioColumn;
    @FXML
    private TableColumn<FeligresDetalle, Integer> partidaColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> padreColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> madreColumn;
    @FXML
    private TableColumn<FeligresDetalle, LocalDate> nacimientoColumn;
    @FXML
    private TableColumn<FeligresDetalle, Integer> edadColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> lugarNacimientoColumn;
    @FXML
    private TableColumn<FeligresDetalle, LocalDate> fechaSacramentoColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> lugarSacramentoColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> padrinoColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> madrinaColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> observacionColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> registradoColumn;

    //Campos para el ingreso de datos
    @FXML
    private TextField txtLibroB, txtFolioB, txtPartidaB, txtNombreB, txtApellidoB, txtPadreB, txtMadreB, txtPadrinoB, txtMadrinaB, txtLugarBautismo, txtLugarNacimientoB;
    @FXML
    private TextArea txtAreaObservaciones;
    @FXML
    private CheckBox boxInscritoB;
    @FXML
    private DatePicker datePikerFechaB, datePikerFechaNacimientoB;
    private String check;
    //Definicion de la variable de Busqueda
    @FXML
    private TextField txtBusquedaB;

    private TabPane miTabPaneB;
    //Para Manejar las Graficas
    @FXML
    private BarChart<String, Number> bcBautismos;
    @FXML
    private ComboBox<String> cbFiltroB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatosBautismosPorFecha();
        cargarDatosDistribucionEdades();
        cargarDatosBautismosPorFecha2();

        pcBautismos.setVisible(false);
        bcBautismos.setVisible(false);
        lcBautismo.setVisible(false);
        pcBautismos2.setVisible(false);

        cbFiltroB.getItems().addAll("Bautismos por Fecha", "Bautismos Por Edades", "por Fecha Barras", "Anotados al Libro");
        //miTabPaneB.getSelectionModel().select(1);
        // Agregar el evento de cambio de selección al ComboBox
        cbFiltroB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String opcionSeleccionada = cbFiltroB.getSelectionModel().getSelectedItem();

                // Verificar la opción seleccionada y ejecutar la acción correspondiente
                if ("Bautismos por Fecha".equals(opcionSeleccionada)) {
                    pcBautismos2.setVisible(false);
                    pcBautismos.setVisible(false);
                    bcBautismos.setVisible(false);
                    lcBautismo.setVisible(true);
                    // Ejecutar el método correspondiente
                    cargarDatosBautismosPorFecha2();
                } else if ("Bautismos Por Edades".equals(opcionSeleccionada)) {
                    pcBautismos2.setVisible(false);
                    pcBautismos.setVisible(true);
                    bcBautismos.setVisible(false);
                    lcBautismo.setVisible(false);
                    cargarDatosDistribucionEdades();

                    // ...
                } else if ("por Fecha Barras".equals(opcionSeleccionada)) {
                    pcBautismos2.setVisible(false);
                    pcBautismos.setVisible(false);
                    bcBautismos.setVisible(true);
                    lcBautismo.setVisible(false);
                    cargarDatosBautismosPorFecha();
                    // Ejecutar otra acción
                    // ...

                } else if ("Anotados al Libro".equals(opcionSeleccionada)) {
                    pcBautismos.setVisible(false);
                    bcBautismos.setVisible(false);
                    lcBautismo.setVisible(false);
                    pcBautismos2.setVisible(true);
                    cargarDatosInscritosVsNoInscritos();
                }
            }
        });

    }

    @FXML
    private void _regresar() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void _mostraDatos() throws IOException {
        _consultaBautismo();

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

        // Verifica primero si los campos libro, folio o partida están vacíos
        if (txtLibroB.getText().trim().isEmpty() || txtFolioB.getText().trim().isEmpty() || txtPartidaB.getText().trim().isEmpty() || txtNombreB.getText().trim().isEmpty() || txtApellidoB.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validación de Campos");
            alert.setHeaderText(null);
            alert.setContentText("Los campos Libro, Folio y Partida no pueden quedar vacíos.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Intenta convertir las entradas a Integer y atrapa cualquier NumberFormatException
        try {
            libro = Integer.valueOf(txtLibroB.getText().trim());
            folio = Integer.valueOf(txtFolioB.getText().trim());
            partida = Integer.valueOf(txtPartidaB.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingresa valores numéricos válidos en los campos Libro, Folio y Partida.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Comprueba si alguna de las cadenas contiene números
        if (nombre.matches(".*\\d.*") || apellido.matches(".*\\d.*") || padre.matches(".*\\d.*") || madre.matches(".*\\d.*")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Los campos de nombre, apellido, padre y madre no deben contener números. Por favor, corrija e intente nuevamente.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        try {

            // Obtén la fecha de nacimiento del DatePicker
            LocalDate fechaNacimientoC = datePikerFechaNacimientoB.getValue();

            // Calcula la edad
            LocalDate hoy = LocalDate.now();
            int edad = hoy.getYear() - fechaNacimientoC.getYear();
            if (fechaNacimientoC.getDayOfYear() > hoy.getDayOfYear()) {
                edad--; // Ajusta la edad si el cumpleaños de este año aún no ha llegado.
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
            String sql = "INSERT INTO feligres (nombre, apellido, padreFeligres, madreFeligres, nacimiento, edadFeligres, lugarNacimiento) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Preparando la consulta SQL
            pstmt1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, nombre);
            pstmt1.setString(2, apellido);
            pstmt1.setString(3, padre);
            pstmt1.setString(4, madre);
            pstmt1.setDate(5, Date.valueOf(fechaNacimiento));
            pstmt1.setInt(6, edad);
            pstmt1.setString(7, lugarNacimiento);
            // Ejecutando la consulta SQL
            pstmt1.executeUpdate();

            // Obtener el ID generado
            rs = pstmt1.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                // Ahora puedes usar idGenerado para las otras consultas como clave foránea
                //Insertado a la tabla de Reistrodel Sacramento
                String sql3 = "INSERT INTO bautismo (fechaSacramento, lugarSacramento, padrino, madrina, idFeligres, fechaInscripcion) VALUES ( ?, ?, ?, ?, ?, NOW())";
                // Preparando la consulta SQL
                pstmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
                pstmt3.setDate(1, Date.valueOf(fechaBautismo));
                pstmt3.setString(2, lugarBautismo);
                pstmt3.setString(3, padrino);
                pstmt3.setString(4, madrina);
                pstmt3.setInt(5, idGenerado);
                // Ejecutando la consulta SQL
                pstmt3.executeUpdate();

                // Obtener el ID generado
                rs = pstmt3.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado1 = rs.getInt(1);
                    //Insertado a la tabla de Reistrodel Sacramento
                    String sql2 = "INSERT INTO registrolibro (libro, folio, partida, inscritoLibro, bautismo_idBautismo) VALUES (?, ?, ?, ?, ?)";
                    // Preparando la consulta SQL
                    pstmt2 = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
                    pstmt2.setInt(1, libro);
                    pstmt2.setInt(2, folio);
                    pstmt2.setInt(3, partida);
                    pstmt2.setString(4, check);
                    pstmt2.setInt(5, idGenerado1);
                    // Ejecutando la consulta SQL
                    pstmt2.executeUpdate();

                    //Insertado a la tabla de Reistrodel Sacramento
                    String sql4 = "INSERT INTO observacion (observacion, bautismo_idBautismo) VALUES (?,?)";
                    // Preparando la consulta SQL
                    pstmt4 = conn.prepareStatement(sql4);
                    pstmt4.setString(1, observaciones);
                    pstmt4.setInt(2, idGenerado1);
                    // Ejecutando la consulta SQL
                    pstmt4.executeUpdate();

                }

            }
            limpiarCampos();

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

    }

//   Nueva Consulta para Bautismo dentro de La Gestion
    private void _consultaBautismo() throws IOException {
        //Escucha el evento del doble Clic
        tablaFeligreses.setRowFactory(tv -> {
            TableRow<FeligresDetalle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    FeligresDetalle rowData = row.getItem();
                    SingletonData1.getInstance().setFeligresDetalle(rowData); // Guarda los datos en el Singleton
                    try {
                        App.setRoot("edicionRegistro");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        libroColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("libro"));
        folioColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("folio"));
        partidaColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("partida"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("nombre"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("apellido"));
        padreColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("padre"));
        madreColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("madre"));
        nacimientoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, LocalDate>("nacimiento"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("edad"));
        lugarNacimientoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("lugarNacimiento"));
        fechaSacramentoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, LocalDate>("fechaSacramento"));
        lugarSacramentoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("lugarSacramento"));
        padrinoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("padrino"));
        madrinaColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("madrina"));
        observacionColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("observacion"));
        registradoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("registrado"));
        Connection connection = ConexionDB.getConexion();
        ObservableList<FeligresDetalle> data = FXCollections.observableArrayList();

        try {
            String query = "SELECT r.libro, r.folio, r.partida, "
                    + "f.nombre, f.apellido, f.padreFeligres, f.madreFeligres, f.nacimiento, f.edadFeligres, f.lugarNacimiento, "
                    + "b.fechaSacramento, b.lugarSacramento, b.padrino, b.madrina, "
                    + "o.observacion, r.inscritoLibro  "
                    + "FROM feligres f "
                    + "JOIN bautismo b ON f.idFeligres = b.idFeligres "
                    + "JOIN registrolibro r ON b.idBautismo = r.bautismo_idBautismo "
                    + "JOIN observacion o ON b.idBautismo = o.bautismo_idBautismo ";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new FeligresDetalle(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("padreFeligres"),
                        rs.getString("madreFeligres"),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getInt("edadFeligres"),
                        rs.getString("lugarNacimiento"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getString("lugarSacramento"),
                        rs.getString("padrino"),
                        rs.getString("madrina"),
                        rs.getString("observacion"),
                        rs.getString("inscritoLibro")
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data.isEmpty()) {
                showAlert("Información", "Bautismos, No se encontraron resultados ", Alert.AlertType.INFORMATION);
                tablaFeligreses.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                return;
            }

            tablaFeligreses.setItems(data);
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

    }//Fin Consulta Bautismo

    @FXML
    private void _consultaBautismoBusqueda() throws IOException {
        // Validación para verificar si el TextField está vacío
        busqueda = txtBusquedaB.getText();
        if (busqueda.trim().isEmpty()) {
            showAlert("Error", "El campo de búsqueda no puede estar vacío.", Alert.AlertType.ERROR);
            return;
        }
        libroColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("libro"));
        folioColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("folio"));
        partidaColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("partida"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("nombre"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("apellido"));
        padreColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("padre"));
        madreColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("madre"));
        nacimientoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, LocalDate>("nacimiento"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("edad"));
        lugarNacimientoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("lugarNacimiento"));
        fechaSacramentoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, LocalDate>("fechaSacramento"));
        lugarSacramentoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("lugarSacramento"));
        padrinoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("padrino"));
        madrinaColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("madrina"));
        observacionColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("observacion"));
        registradoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("registrado"));
        Connection connection = ConexionDB.getConexion();
        ObservableList<FeligresDetalle> data = FXCollections.observableArrayList();

        try {
            String query = "SELECT r.libro, r.folio, r.partida, "
                    + "f.nombre, f.apellido, f.padreFeligres, f.madreFeligres, f.nacimiento, f.edadFeligres, f.lugarNacimiento, "
                    + "b.fechaSacramento, b.lugarSacramento, b.padrino, b.madrina, "
                    + "o.observacion, r.inscritoLibro  "
                    + "FROM feligres f "
                    + "JOIN bautismo b ON f.idFeligres = b.idFeligres "
                    + "JOIN registrolibro r ON b.idBautismo = r.bautismo_idBautismo "
                    + "JOIN observacion o ON b.idBautismo = o.bautismo_idBautismo "
                    + "WHERE nombre LIKE ? OR apellido LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + busqueda + "%");
            stmt.setString(2, "%" + busqueda + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new FeligresDetalle(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("padreFeligres"),
                        rs.getString("madreFeligres"),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getInt("edadFeligres"),
                        rs.getString("lugarNacimiento"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getString("lugarSacramento"),
                        rs.getString("padrino"),
                        rs.getString("madrina"),
                        rs.getString("observacion"),
                        rs.getString("inscritoLibro")
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data.isEmpty()) {
                showAlert("Información", "Bautismos, No se encontraron resultados para: " + busqueda, Alert.AlertType.INFORMATION);
                tablaFeligreses.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                _consultaBautismo();
                return;
            }

            tablaFeligreses.setItems(data);
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

    //Campos Miselaneos
    public void limpiarCampos() {
        // Limpiar todos los otros campos
        // Limpieza de los TextField
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

        // Limpieza del TextArea
        txtAreaObservaciones.clear();

        // Limpieza del CheckBox
        boxInscritoB.setSelected(false);

        // Limpieza de los DatePicker
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

    //Manejo de Graficas
    //Barchar Genera la Consulta pra saber cuantos Bautismos hay por fecha 
    private Map<LocalDate, Integer> obtenerBautismosPorFecha() throws SQLException {
        Map<LocalDate, Integer> resultados = new HashMap<>();

        String sql = "SELECT fechaSacramento, COUNT(*) as total FROM bautismo GROUP BY fechaSacramento ORDER BY fechaSacramento";

        try (
                Connection conn = ConexionDB.getConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resultados.put(rs.getDate("fechaSacramento").toLocalDate(), rs.getInt("total"));
            }
        }

        return resultados;
    }

    //Agrega los Datos obtenidos anteriormente y los muestra
    @FXML
    private void cargarDatosBautismosPorFecha() {
        bcBautismos.getData().clear(); // Limpia las series anteriores
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        try {
            Map<LocalDate, Integer> datos = obtenerBautismosPorFecha();

            for (Map.Entry<LocalDate, Integer> entry : datos.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }

            bcBautismos.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
            // Aquí puedes manejar el error, quizás mostrar un mensaje al usuario
        }
    }

    private Map<String, Integer> obtenerDistribucionEdadesBautismos() throws SQLException {
        Map<String, Integer> resultados = new HashMap<>();

        String sql = "SELECT \n"
                + "    CASE \n"
                + "        WHEN f.edadFeligres BETWEEN 0 AND 7 THEN '0-7 años'\n"
                + "        ELSE '8+ años'\n"
                + "    END AS rangoEdad,\n"
                + "    COUNT(*) as total \n"
                + "FROM feligres f\n"
                + "INNER JOIN bautismo b ON f.idFeligres = b.idFeligres \n"
                + "GROUP BY rangoEdad \n"
                + "ORDER BY rangoEdad;";

        try (
                Connection conn = ConexionDB.getConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String edad = rs.getString("rangoEdad");
                resultados.put(edad, rs.getInt("total"));
            }
        }

        return resultados;
    }

    @FXML
    private void cargarDistribucionEdadesBautismos() {
        bcBautismos.getData().clear(); // Limpia las series anteriores

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        try {
            Map<String, Integer> datos = obtenerDistribucionEdadesBautismos();

            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            bcBautismos.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //PieChart que muestra los bautismos por rangos de edad
    @FXML
    private PieChart pcBautismos;

    @FXML
    private void cargarDatosDistribucionEdades() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try {
            Map<String, Integer> datos = obtenerDistribucionEdadesBautismos();

            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

            pcBautismos.setData(pieChartData);

            // Si deseas mostrar la cantidad en el PieChart
            for (PieChart.Data data : pcBautismos.getData()) {
                data.nameProperty().bind(Bindings.concat(data.getName(), ": ", data.pieValueProperty().asString("%.0f")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//LineChart que muestra los bautismos por fecha
    @FXML
    private LineChart<String, Number> lcBautismo;

    @FXML
    private void cargarDatosBautismosPorFecha2() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bautismos");  // Nombre de la serie, opcional.

        try {
            Map<LocalDate, Integer> datos = obtenerBautismosPorFecha();

            for (Map.Entry<LocalDate, Integer> entry : datos.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }

            lcBautismo.getData().clear(); // Limpia los datos anteriores
            lcBautismo.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //NUevo PieCHart que muestra los registros inscritos y los que no xD ---------------------------------------
    @FXML
    private PieChart pcBautismos2;

    // 1. Consulta para obtener la cantidad de inscritos y no inscritos.
    private Map<String, Integer> obtenerInscritosVsNoInscritos() throws SQLException {
        Map<String, Integer> resultados = new HashMap<>();

        String sql = "SELECT r.inscritoLibro, COUNT(*) as total \n"
                + "FROM registrolibro r \n"
                + "INNER JOIN bautismo b ON r.bautismo_idBautismo = b.idBautismo \n"
                + "GROUP BY r.inscritoLibro;";

        try (Connection conn = ConexionDB.getConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultados.put(rs.getString("inscritoLibro"), rs.getInt("total"));
            }
        }

        return resultados;
    }

    @FXML
    private void cargarDatosInscritosVsNoInscritos() {
        try {
            Map<String, Integer> datos = obtenerInscritosVsNoInscritos();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

            pcBautismos2.setData(pieChartData);

            // Agregar leyenda a los slices del PieChart
            for (final PieChart.Data data : pcBautismos2.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                    Bounds b1 = data.getNode().getBoundsInLocal();
                    double newX = (b1.getMaxX() + b1.getMinX()) / 2;
                    double newY = (b1.getMaxY() + b1.getMinY()) / 2;
                    System.out.println(newX + " " + newY);
                    Tooltip t = new Tooltip(data.getPieValue() + "");
                    Tooltip.install(data.getNode(), t);
                });
            }
            // Si deseas mostrar la cantidad en el PieChart
            for (PieChart.Data data : pcBautismos2.getData()) {
                data.nameProperty().bind(Bindings.concat(data.getName(), ": ", data.pieValueProperty().asString("%.0f")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
