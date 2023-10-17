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
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class ReportesController implements Initializable {

    @FXML
    private StackedAreaChart<String, Number> graficaGeneral;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();

    }

    @FXML
    private void _regresar() throws IOException {
        App.setRoot("primary");
    }

    //Grafica:
    private Map<LocalDate, Map<String, Integer>> obtenerDatos() throws SQLException {
        Map<LocalDate, Map<String, Integer>> datosUnificados = new TreeMap<>();

        // Cargar bautismos
        Map<LocalDate, Integer> bautismos = obtenerBautismosPorFecha();
        for (Map.Entry<LocalDate, Integer> entry : bautismos.entrySet()) {
            datosUnificados.computeIfAbsent(entry.getKey(), k -> new HashMap<>()).put("Bautismos", entry.getValue());
        }

        // Cargar comuniones
        Map<LocalDate, Integer> comuniones = obtenerComunionesPorFecha();
        for (Map.Entry<LocalDate, Integer> entry : comuniones.entrySet()) {
            datosUnificados.computeIfAbsent(entry.getKey(), k -> new HashMap<>()).put("Comuniones", entry.getValue());
        }

        // Nota: Las siguientes consultas devuelven años, así que convertiremos eso en fechas
        // Cargar confirmaciones
        Map<String, Integer> confirmaciones = obtenerConfirmacionesPorAño();
        for (Map.Entry<String, Integer> entry : confirmaciones.entrySet()) {
            LocalDate fecha = LocalDate.of(Integer.parseInt(entry.getKey()), 1, 1);  // Usamos el primer día del año
            datosUnificados.computeIfAbsent(fecha, k -> new HashMap<>()).put("Confirmaciones", entry.getValue());
        }

        // Cargar matrimonios
        Map<String, Integer> matrimonios = obtenerMatrimoniosPorAño();
        for (Map.Entry<String, Integer> entry : matrimonios.entrySet()) {
            LocalDate fecha = LocalDate.of(Integer.parseInt(entry.getKey()), 1, 1);  // Usamos el primer día del año
            datosUnificados.computeIfAbsent(fecha, k -> new HashMap<>()).put("Matrimonios", entry.getValue());
        }

        return datosUnificados;
    }

    @FXML
    public void cargarDatos() {
        Platform.runLater(() -> {
            try {
                graficaGeneral.getData().clear(); // Limpiar datos previos.

                Map<LocalDate, Map<String, Integer>> datos = obtenerDatos();

                // Cambiar el tipo del mapa de series a <String, Number>
                Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

                for (Map.Entry<LocalDate, Map<String, Integer>> dateEntry : datos.entrySet()) {
                    // Convertir la fecha a una cadena
                    String fechaString = dateEntry.getKey().toString();

                    for (Map.Entry<String, Integer> dataEntry : dateEntry.getValue().entrySet()) {
                        String tipo = dataEntry.getKey();
                        Integer valor = dataEntry.getValue();

                        // Si no existe una serie para este tipo, la creamos
                        if (!seriesMap.containsKey(tipo)) {
                            XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
                            newSeries.setName(tipo);
                            seriesMap.put(tipo, newSeries);
                        }

                        // Agregamos el dato a la serie correspondiente usando fechaString
                        seriesMap.get(tipo).getData().add(new XYChart.Data<>(fechaString, valor));
                    }
                }

                // Agregar todas las series al gráfico
                for (XYChart.Series<String, Number> serie : seriesMap.values()) {
                    graficaGeneral.getData().add(serie);
                }

            } catch (SQLException e) {
                System.out.println("Error al obtener los datos unificados: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Se encontró un valor nulo: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        });
    }

    //--------------------------------------------------------Consultas Previas
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

    private Map<String, Integer> obtenerMatrimoniosPorAño() throws SQLException {
        Map<String, Integer> datos = new TreeMap<>(); // TreeMap mantiene las claves en orden

        String sql = "SELECT YEAR(fechaSacramento) as year, COUNT(*) as count "
                + "FROM matrimonios "
                + "GROUP BY YEAR(fechaSacramento)";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                datos.put(rs.getString("year"), rs.getInt("count"));
            }
        }
        return datos;
    }

}
