package com.mycompany.sacramentos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

/**
 * Controlador para la vista de reportes de bautismos.
 * Maneja la visualización de diferentes tipos de reportes en gráficos.
 * 
 * Autor: walyn
 */
public class VistaBautismoReporteController implements Initializable {

    @FXML private ComboBox<String> cbFiltroB;
    @FXML private BarChart<String, Number> bcBautismos;
    @FXML private PieChart pcBautismos;
    @FXML private LineChart<String, Number> lcBautismo;
    @FXML private PieChart pcBautismos2;

    /**
     * Inicializa el controlador de la clase.
     * 
     * @param url URL de la ubicación del recurso
     * @param rb  Bundle de recursos para localizar el objeto raíz
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Establecer el valor por defecto y cargar datos iniciales
        cargarDatosBautismosPorFecha();
        cargarDatosDistribucionEdades();
        cargarDatosBautismosPorFecha2();
        
        // Configurar la visibilidad inicial de los gráficos
        pcBautismos.setVisible(false);
        bcBautismos.setVisible(false);
        lcBautismo.setVisible(true);
        pcBautismos2.setVisible(false);
        
        // Agregar ítems al ComboBox y establecer valor por defecto
        cbFiltroB.getItems().addAll("Bautismos por Fecha", "Bautismos Por Edades", "por Fecha Barras", "Anotados al Libro");
        cbFiltroB.setValue("Bautismos por Fecha");

        // Agregar evento de cambio de selección al ComboBox
        cbFiltroB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String opcionSeleccionada = cbFiltroB.getSelectionModel().getSelectedItem();

                // Verificar la opción seleccionada y ejecutar la acción correspondiente
                switch (opcionSeleccionada) {
                    case "Bautismos por Fecha":
                        pcBautismos2.setVisible(false);
                        pcBautismos.setVisible(false);
                        bcBautismos.setVisible(false);
                        lcBautismo.setVisible(true);
                        cargarDatosBautismosPorFecha2();
                        break;
                    case "Bautismos Por Edades":
                        pcBautismos2.setVisible(false);
                        pcBautismos.setVisible(true);
                        bcBautismos.setVisible(false);
                        lcBautismo.setVisible(false);
                        cargarDatosDistribucionEdades();
                        break;
                    case "por Fecha Barras":
                        pcBautismos2.setVisible(false);
                        pcBautismos.setVisible(false);
                        bcBautismos.setVisible(true);
                        lcBautismo.setVisible(false);
                        cargarDatosBautismosPorFecha();
                        break;
                    case "Anotados al Libro":
                        pcBautismos.setVisible(false);
                        bcBautismos.setVisible(false);
                        lcBautismo.setVisible(false);
                        pcBautismos2.setVisible(true);
                        cargarDatosInscritosVsNoInscritos();
                        break;
                }
            }
        });
    }

    // Método para obtener la cantidad de bautismos por fecha
    private Map<LocalDate, Integer> obtenerBautismosPorFecha() throws SQLException {
        Map<LocalDate, Integer> resultados = new HashMap<>();
        String sql = "SELECT fechaSacramento, COUNT(*) as total FROM bautismo GROUP BY fechaSacramento ORDER BY fechaSacramento";
        try (Connection conn = ConexionDB.getConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resultados.put(rs.getDate("fechaSacramento").toLocalDate(), rs.getInt("total"));
            }
        }
        return resultados;
    }

    // Método para cargar los datos de bautismos por fecha en un BarChart
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
            // Manejar el error, mostrar un mensaje al usuario si es necesario
        }
    }

    // Método para obtener la distribución de edades de los bautismos
    private Map<String, Integer> obtenerDistribucionEdadesBautismos() throws SQLException {
        Map<String, Integer> resultados = new HashMap<>();
        String sql = "SELECT CASE WHEN f.edadFeligres BETWEEN 0 AND 7 THEN '0-7 años' ELSE '8+ años' END AS rangoEdad, COUNT(*) as total " +
                     "FROM feligres f INNER JOIN bautismo b ON f.idFeligres = b.idFeligres GROUP BY rangoEdad ORDER BY rangoEdad";
        try (Connection conn = ConexionDB.getConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resultados.put(rs.getString("rangoEdad"), rs.getInt("total"));
            }
        }
        return resultados;
    }

    // Método para cargar la distribución de edades en un BarChart
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

    // Método para cargar la distribución de edades en un PieChart
    @FXML
    private void cargarDatosDistribucionEdades() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            Map<String, Integer> datos = obtenerDistribucionEdadesBautismos();
            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
            pcBautismos.setData(pieChartData);
            // Mostrar la cantidad en el PieChart
            for (PieChart.Data data : pcBautismos.getData()) {
                data.nameProperty().bind(Bindings.concat(data.getName(), ": ", data.pieValueProperty().asString("%.0f")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para cargar los datos de bautismos por fecha en un LineChart
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

    // Método para obtener la cantidad de inscritos y no inscritos
    private Map<String, Integer> obtenerInscritosVsNoInscritos() throws SQLException {
        Map<String, Integer> resultados = new HashMap<>();
        String sql = "SELECT r.inscritoLibro, COUNT(*) as total FROM registrolibro r INNER JOIN bautismo b ON r.bautismo_idBautismo = b.idBautismo GROUP BY r.inscritoLibro";
        try (Connection conn = ConexionDB.getConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resultados.put(rs.getString("inscritoLibro"), rs.getInt("total"));
            }
        }
        return resultados;
    }

    // Método para cargar los datos de inscritos y no inscritos en un PieChart
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
            // Mostrar la cantidad en el PieChart
            for (PieChart.Data data : pcBautismos2.getData()) {
                data.nameProperty().bind(Bindings.concat(data.getName(), ": ", data.pieValueProperty().asString("%.0f")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para regresar a la vista anterior
    @FXML
    private void regresar() throws IOException {
        App.setRoot("vistaSacramentos");
    }
}
