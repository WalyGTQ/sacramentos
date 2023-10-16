/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sacramentos;

import java.awt.Color;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
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
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class PrimeraComunionController implements Initializable {

    //LLamamos los Graficos y ComboBox PAra Filtro
    @FXML
    private BarChart<String, Number> bcPrimeraComunion;
    @FXML
    private PieChart pcPrimeraComunion;
    @FXML
    private LineChart<String, Number> lcPrimeraComunion;
    @FXML
    private ComboBox<String> cbFiltroP;

    //Para La Busqueda
    private String busqueda;
    @FXML
    private TextField txtBusquedaPc;
    //Inicio Campor Para Consulta de Primera Comunion
    @FXML
    private TableView<ConsultaPrimeraComunion> tvComunion;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, Integer> tcLibroC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, Integer> tcFolioC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, Integer> tcPartidaC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, Integer> tcEdadC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, LocalDate> tcFechaC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, LocalDate> tcNacimientoC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, LocalDate> tcRegistroC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcNombreC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcApellidoC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcLugarC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcCelebranteC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcAnotadoC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcObservacionC;
    //Fin Campos Para Consulta de Primera Comunion
    @FXML
    private TextField txtLibroPc, txtFolioPc, txtPartidaPc, txtNombrePc, txtApellidoPc, txtLugarBauPc, txtCelebrantePc;
    @FXML
    private TextArea txtAreaObPc;
    @FXML
    private CheckBox boxInscritoPc;
    @FXML
    private DatePicker dpFechaRealizadoPc, dpFechaNacPc;
    @FXML
    private String check;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bcPrimeraComunion.setVisible(false);
        lcPrimeraComunion.setVisible(false);
        pcPrimeraComunion.setVisible(false);
        try {
            _cargarGraficos();
        } catch (IOException ex) {
            Logger.getLogger(PrimeraComunionController.class.getName()).log(Level.SEVERE, null, ex);
        }

        cbFiltroP.getItems().addAll("Por Edad", "Por Fecha", "Inscritos al Libro");
        //miTabPaneB.getSelectionModel().select(1);
        // Agregar el evento de cambio de selección al ComboBox
        cbFiltroP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String opcionSeleccionada = cbFiltroP.getSelectionModel().getSelectedItem();

                // Verificar la opción seleccionada y ejecutar la acción correspondiente
                if ("Por Edad".equals(opcionSeleccionada)) {
                    bcPrimeraComunion.setVisible(true);
                    lcPrimeraComunion.setVisible(false);
                    pcPrimeraComunion.setVisible(false);
                    cargarDatosFeligresesPorEdad();

                } else if ("Por Fecha".equals(opcionSeleccionada)) {
                    bcPrimeraComunion.setVisible(false);
                    lcPrimeraComunion.setVisible(true);
                    pcPrimeraComunion.setVisible(false);
                    cargarDatosComunionesPorFecha();
                    // ...
                } else if ("Inscritos al Libro".equals(opcionSeleccionada)) {
                    bcPrimeraComunion.setVisible(false);
                    lcPrimeraComunion.setVisible(false);
                    pcPrimeraComunion.setVisible(true);
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
    private void _guardarPc() throws IOException {
        Integer libro, folio, partida;

        // Obteniendo los valores de los campos de texto y otros componentes
        String nombre = txtNombrePc.getText();
        String apellido = txtApellidoPc.getText();
        String lugarPc = txtLugarBauPc.getText();
        String celebrante = txtCelebrantePc.getText();
        String observaciones = txtAreaObPc.getText();
        boolean inscrito = boxInscritoPc.isSelected();
        LocalDate fechaPc = dpFechaRealizadoPc.getValue();
        LocalDate fechaNacimiento = dpFechaNacPc.getValue();

        // Declarando variables de conexión y statement.
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        PreparedStatement pstmt5 = null;
        ResultSet rs = null;

        // Verifica primero si los campos libro, folio o partida están vacíos
        if (txtLibroPc.getText().trim().isEmpty() || txtFolioPc.getText().trim().isEmpty() || txtPartidaPc.getText().trim().isEmpty() || txtNombrePc.getText().trim().isEmpty() || txtApellidoPc.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validación de Campos");
            alert.setHeaderText(null);
            alert.setContentText("Los campos Libro, Folio, Partida, Nomnbre y Apellido no pueden quedar vacíos.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Intenta convertir las entradas a Integer y atrapa cualquier NumberFormatException
        try {
            libro = Integer.valueOf(txtLibroPc.getText().trim());
            folio = Integer.valueOf(txtFolioPc.getText().trim());
            partida = Integer.valueOf(txtPartidaPc.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingresa valores numéricos válidos en los campos Libro, Folio y Partida.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }

        // Comprueba si alguna de las cadenas contiene números
        if (nombre.matches(".*\\d.*") || apellido.matches(".*\\d.*") || celebrante.matches(".*\\d.*")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Entrada no válida");
            alert.setHeaderText(null);
            alert.setContentText("Los campos de Nombre, Apellido y Celebrante no deben contener números. Por favor, corrija e intente nuevamente.");
            alert.showAndWait();
            return; // Termina la ejecución del método para no continuar con el proceso
        }
        //Inicia la Query SQL
        try {

            // Obtén la fecha de nacimiento del DatePicker
            LocalDate fechaNacimientoC = dpFechaNacPc.getValue();

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
            String sql = "INSERT INTO feligres (nombre, apellido, nacimiento, edadFeligres) VALUES (?, ?, ?, ?)";

            // Preparando la consulta SQL
            pstmt1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, nombre);
            pstmt1.setString(2, apellido);
            pstmt1.setDate(3, Date.valueOf(fechaNacimiento));
            pstmt1.setInt(4, edad);
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
                    String sql3 = "INSERT INTO comunion (fechaSacramento, lugarSacramento, idFeligres, celebrante_idCelebrante, fechaInscripcion) VALUES (?, ?, ?, ?, NOW() )";
                    // Preparando la consulta SQL
                    pstmt3 = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
                    pstmt3.setDate(1, Date.valueOf(fechaPc));
                    pstmt3.setString(2, lugarPc);
                    pstmt3.setInt(3, idGeneradoF);
                    pstmt3.setInt(4, idGeneradoC);
                    // Ejecutando la consulta SQL
                    pstmt3.executeUpdate();
                    System.out.println(idGeneradoF + idGeneradoC);

                    // Obtener el ID generado Celebrante
                    rs = pstmt3.getGeneratedKeys();
                    if (rs.next()) {
                        int idGeneradoS = rs.getInt(1);

                        //Insertado a la tabla de Registro del Sacramento
                        String sql2 = "INSERT INTO registrolibro (libro, folio, partida, inscritoLibro, comunion_idComunion) VALUES (?, ?, ?, ?, ?)";
                        // Preparando la consulta SQL
                        pstmt2 = conn.prepareStatement(sql2);
                        pstmt2.setInt(1, libro);
                        pstmt2.setInt(2, folio);
                        pstmt2.setInt(3, partida);
                        pstmt2.setString(4, check);
                        pstmt2.setInt(5, idGeneradoS);
                        // Ejecutando la consulta SQL
                        pstmt2.executeUpdate();
                        System.out.println(libro + " " + folio + "  " + partida);
                        //Insertado a la tabla de Reistrodel Sacramento
                        String sql4 = "INSERT INTO observacion (observacion, comunion_idComunion) VALUES (?,?)";
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

        //Aca Finaliza _guardarPc
    }

    @FXML
    private void _busquedaAutomatica() throws IOException {
        //Escucha el evento del doble Clic
        tvComunion.setRowFactory(tv -> {
            TableRow<ConsultaPrimeraComunion> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ConsultaPrimeraComunion rowData = row.getItem();
                    SingletonPrimeraComunion.getInstance().setFeligresDetalle(rowData); // Guarda los datos en el Singleton
                    try {
                        App.setRoot("edicionRegistroPc");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        // Configuración de las columnas usando PropertyValueFactory
        tcLibroC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("libroC"));
        tcFolioC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("folioC"));
        tcPartidaC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("partidaC"));
        tcEdadC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("edadC"));
        tcFechaC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, LocalDate>("fechaRealizadoPc"));
        tcNacimientoC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, LocalDate>("fechaNacPc"));
        tcRegistroC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, LocalDate>("registroC"));
        tcNombreC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("nombreC"));
        tcApellidoC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("apellidoC"));
        tcLugarC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("lugarC"));
        tcCelebranteC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("celebranteC"));
        tcAnotadoC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("anotadoC"));
        tcObservacionC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("observacionC"));
        ObservableList<ConsultaPrimeraComunion> data2 = FXCollections.observableArrayList();
        Connection connection = ConexionDB.getConexion();
        try {
            String query = "SELECT r.libro, r.folio, r.partida, r.inscritoLibro, "
                    + "f.nombre, f.apellido, f.nacimiento, f.edadFeligres, "
                    + "p.fechaInscripcion, p.fechaSacramento, p.lugarSacramento, "
                    + "c.nombreCelebrante,  "
                    + "o.observacion  "
                    + "FROM feligres f "
                    + "JOIN comunion p ON f.idFeligres = p.idFeligres "
                    + "JOIN celebrante c ON p.celebrante_idCelebrante = c.idCelebrante "
                    + "JOIN registrolibro r ON p.idComunion = r.comunion_idComunion "
                    + "JOIN observacion o ON p.idComunion = o.comunion_idComunion ";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data2.add(new ConsultaPrimeraComunion(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getInt("edadFeligres"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getDate("fechaInscripcion").toLocalDate(),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("lugarSacramento"),
                        rs.getString("nombreCelebrante"),
                        rs.getString("inscritoLibro"),
                        rs.getString("observacion")
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data2.isEmpty()) {
                showAlert("Información", "Primera Comunion, No se encontraron resultados: ", Alert.AlertType.INFORMATION);
                tvComunion.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                return;
            }

            tvComunion.setItems(data2);
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
    private void _busquedaEspecifica() throws IOException {//Realiza una busqueda mediante entradas de Texto
        // Validación para verificar si el TextField está vacío
        busqueda = txtBusquedaPc.getText();
        if (busqueda.trim().isEmpty()) {
            showAlert("Error", "El campo de búsqueda no puede estar vacío.", Alert.AlertType.ERROR);
            return;
        }
        // Configuración de las columnas usando PropertyValueFactory
        tcLibroC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("libroC"));
        tcFolioC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("folioC"));
        tcPartidaC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("partidaC"));
        tcEdadC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("edadC"));
        tcFechaC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, LocalDate>("fechaRealizadoPc"));
        tcNacimientoC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, LocalDate>("fechaNacPc"));
        tcRegistroC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, LocalDate>("registroC"));
        tcNombreC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("nombreC"));
        tcApellidoC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("apellidoC"));
        tcLugarC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("lugarC"));
        tcCelebranteC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("celebranteC"));
        tcAnotadoC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("anotadoC"));
        tcObservacionC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("observacionC"));
        ObservableList<ConsultaPrimeraComunion> data2 = FXCollections.observableArrayList();
        Connection connection = ConexionDB.getConexion();
        try {
            String query = "SELECT r.libro, r.folio, r.partida, r.inscritoLibro, "
                    + "f.nombre, f.apellido, f.nacimiento, f.edadFeligres, "
                    + "p.fechaInscripcion, p.fechaSacramento, p.lugarSacramento, "
                    + "c.nombreCelebrante,  "
                    + "o.observacion  "
                    + "FROM feligres f "
                    + "JOIN comunion p ON f.idFeligres = p.idFeligres "
                    + "JOIN celebrante c ON p.celebrante_idCelebrante = c.idCelebrante "
                    + "JOIN registrolibro r ON p.idComunion = r.comunion_idComunion "
                    + "JOIN observacion o ON p.idComunion = o.comunion_idComunion "
                    + "WHERE nombre LIKE ? OR apellido LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + busqueda + "%");
            stmt.setString(2, "%" + busqueda + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data2.add(new ConsultaPrimeraComunion(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getInt("edadFeligres"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getDate("fechaInscripcion").toLocalDate(),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("lugarSacramento"),
                        rs.getString("nombreCelebrante"),
                        rs.getString("inscritoLibro"),
                        rs.getString("observacion")
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data2.isEmpty()) {
                showAlert("Información", "Primera Comunion, No se encontraron resultados para: " + busqueda, Alert.AlertType.INFORMATION);
                tvComunion.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                _busquedaAutomatica();
                return;
            }

            tvComunion.setItems(data2);
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

    //Codigo Miselaneo
    public void limpiarCampos() {
//    this.txtLibroB.clear();
//    this.txtFolioB.clear();
        // Limpiar todos los otros campos
        // Limpieza de los TextField
        txtLibroPc.clear();
        txtFolioPc.clear();
        txtPartidaPc.clear();
        txtNombrePc.clear();
        txtApellidoPc.clear();
        txtLugarBauPc.clear();

        // Limpieza del TextArea
        txtAreaObPc.clear();

        // Limpieza del CheckBox
        boxInscritoPc.setSelected(false);

        // Limpieza de los DatePicker
        dpFechaRealizadoPc.setValue(null);
        dpFechaNacPc.setValue(null);
    }

    // Función para mostrar alertas fácilmente
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //----------------Manejo de Graficas----------------------------------------
    //Codigo Para Grafica BarChar que muestra las primeras comuniones por edad:
    public Map<String, Integer> obtenerFeligresesPorEdad() throws SQLException {
        Map<String, Integer> resultados = new HashMap<>();

        String sql = "SELECT "
                + "CASE "
                + "    WHEN edadFeligres BETWEEN 0 AND 8 THEN '0-8 años' "
                + "    WHEN edadFeligres BETWEEN 9 AND 11 THEN '9-11 años' "
                + "    WHEN edadFeligres BETWEEN 12 AND 15 THEN '12-15 años' "
                + "    ELSE '16+ años' "
                + "END AS rangoEdad, "
                + "COUNT(*) as total "
                + "FROM feligres "
                + "GROUP BY rangoEdad "
                + "ORDER BY FIELD(rangoEdad, '0-8 años', '9-11 años', '12-15 años', '16+ años');";

        try (Connection conn = ConexionDB.getConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultados.put(rs.getString("rangoEdad"), rs.getInt("total"));
            }
        }

        return resultados;
    }

    @FXML
    private void cargarDatosFeligresesPorEdad() {
        // Limpia los datos antiguos del gráfico
        bcPrimeraComunion.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        try {
            Map<String, Integer> datos = obtenerFeligresesPorEdad();

            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
                series.getData().add(data);

                // Para mostrar la cantidad exacta encima de la barra
                data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        Node node = newValue;
                        Text text = new Text(String.valueOf(data.getYValue()));

                        node.parentProperty().addListener((obs, oldParent, newParent) -> {
                            if (newParent != null) {
                                Group parentGroup = (Group) newParent;
                                parentGroup.getChildren().add(text);
                                text.setLayoutY(node.getBoundsInParent().getMinY() - 5);
                                text.setLayoutX(node.getBoundsInParent().getMinX() + (node.getBoundsInParent().getWidth() / 2) - (text.getLayoutBounds().getWidth() / 2));
                            }
                        });
                    }
                });

            }

            bcPrimeraComunion.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//---------------------------------- Finaliza Grafica BarChar

    // Grafico tipo LineBar que muestra cuantas primera comuniones se han realizado en el tiempo
    public Map<LocalDate, Integer> obtenerComunionesPorFecha() throws SQLException {
        Map<LocalDate, Integer> resultados = new HashMap<>();

        String sql = "SELECT fechaSacramento, COUNT(*) as total FROM comunion GROUP BY fechaSacramento ORDER BY fechaSacramento";

        try (
                Connection conn = ConexionDB.getConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resultados.put(rs.getDate("fechaSacramento").toLocalDate(), rs.getInt("total"));
            }
        }

        return resultados;
    }

    @FXML
    private void cargarDatosComunionesPorFecha() {
        lcPrimeraComunion.getData().clear(); // Limpiamos datos antiguos

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        try {
            Map<LocalDate, Integer> datos = obtenerComunionesPorFecha();

            for (Map.Entry<LocalDate, Integer> entry : datos.entrySet()) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey().toString(), entry.getValue());
                series.getData().add(data);
            }

            lcPrimeraComunion.getData().add(series);

            // Efecto: Suavizar las líneas y puntos
            lcPrimeraComunion.setCreateSymbols(true);
            lcPrimeraComunion.setAnimated(true);

            Platform.runLater(() -> {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    Node node = data.getNode();

                    if (node == null) {
                        data.nodeProperty().addListener(new ChangeListener<Node>() {
                            @Override
                            public void changed(ObservableValue<? extends Node> obs, Node oldNode, Node newNode) {
                                if (newNode != null) {
                                    Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nTotal: " + data.getYValue());
                                    Tooltip.install(newNode, tooltip);
                                    newNode.setOnMouseEntered(event -> newNode.setStyle("-fx-background-color: #ff5733;"));
                                    newNode.setOnMouseExited(event -> newNode.setStyle("-fx-background-color: #f3622d;"));
                                    data.nodeProperty().removeListener(this);  // Importante: eliminar el listener una vez que se ha utilizado
                                }
                            }
                        });
                    } else {
                        Tooltip tooltip = new Tooltip("Fecha: " + data.getXValue() + "\nTotal: " + data.getYValue());
                        Tooltip.install(node, tooltip);
                        node.setOnMouseEntered(event -> node.setStyle("-fx-background-color: #ff5733;"));
                        node.setOnMouseExited(event -> node.setStyle("-fx-background-color: #f3622d;"));
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //---------------------------------------------Finaliza la LineBarr

    //Grafico de PieCHart donde se muestra quienes han sido inscritos y quienes NO
    public Map<String, Integer> obtenerInscritosVsNoInscritos() throws SQLException {
        Map<String, Integer> resultados = new HashMap<>();

        // Usamos la lógica que determina la presencia o ausencia en la tabla Comunion
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM comunion WHERE idComunion IN (SELECT comunion_idComunion FROM registrolibro WHERE inscritoLibro = 'Si')) AS inscritos, "
                + "(SELECT COUNT(*) FROM comunion WHERE idComunion IN (SELECT comunion_idComunion FROM registrolibro WHERE inscritoLibro = 'No')) AS noInscritos;";

        try (Connection conn = ConexionDB.getConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                resultados.put("Inscritos", rs.getInt("inscritos"));
                resultados.put("No Inscritos", rs.getInt("noInscritos"));
            }
        }

        return resultados;
    }

    @FXML
    private void cargarDatosInscritosVsNoInscritos() {
        pcPrimeraComunion.getData().clear(); // Limpiamos datos antiguos

        try {
            Map<String, Integer> datos = obtenerInscritosVsNoInscritos();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            int total = 0;
            for (Integer value : datos.values()) {
                total += value;
            }

            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
                pieChartData.add(slice);

                // Efecto: mostrar el valor exacto cuando se pasa el mouse encima de la porción
                double percentage = (entry.getValue() / (double) total) * 100;
                Tooltip tooltip = new Tooltip(entry.getKey() + ": " + entry.getValue() + " (" + String.format("%.2f", percentage) + "%)");
                Tooltip.install(slice.getNode(), tooltip);

                // Mostrar el número exacto (porcentaje) en la porción
                slice.nameProperty().bind(
                        Bindings.concat(slice.getName(), " ", slice.pieValueProperty(), " (", Bindings.format("%.1f", percentage), "%)")
                );
            }

            pcPrimeraComunion.setData(pieChartData);
            pcPrimeraComunion.setAnimated(true);

            // Coloca esto después de establecer los datos en el PieChart
            Platform.runLater(() -> {
                for (PieChart.Data slice : pcPrimeraComunion.getData()) {
                    // Añadir un efecto de sombra cuando el mouse pasa sobre una porción
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

    //Fin Grafico PieCHart
    @FXML
    private void _cargarGraficos() throws IOException {
        cargarDatosFeligresesPorEdad();
        cargarDatosComunionesPorFecha();
        cargarDatosInscritosVsNoInscritos();

    }
}
