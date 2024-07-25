package com.mycompany.sacramentos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Sesion {

    private String UsuarioSesion;
    private int idUsuarioSesion;
    private LocalDateTime horaInicioSesion;
    private LocalDateTime horaFinSesion;
    private String estadoSesion;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/sacramentos";
    private String username = "root";
    private String password = "Admin_123##";

    public Sesion(String UsuarioSesion, int idUsuarioSesion) {
        this.UsuarioSesion = UsuarioSesion;
        this.idUsuarioSesion = idUsuarioSesion;
    }

    public void iniciarSesion() throws SQLException {
        this.horaInicioSesion = LocalDateTime.now();
        this.estadoSesion = "activa";
        this.connection = DriverManager.getConnection(url, username, password);

        String sql = "INSERT INTO sesion_actividad (UsuarioSesion, idUsuarioSesion, horaInicioSesion, estadoSesion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, UsuarioSesion);
            preparedStatement.setInt(2, idUsuarioSesion);
            preparedStatement.setObject(3, horaInicioSesion);
            preparedStatement.setString(4, estadoSesion);
            preparedStatement.executeUpdate();
        }

        System.out.println("Sesión iniciada para el usuario: " + UsuarioSesion);
    }

    public void registrarActividad(String actividad) throws SQLException {
        this.horaInicioSesion = LocalDateTime.now();
      //  this.estadoSesion = "activa";
        this.connection = DriverManager.getConnection(url, username, password);
        String sql = "INSERT INTO sesion_actividad (UsuarioSesion, idUsuarioSesion, actividadSesion, estadoSesion, horaInicioSesion) VALUES (?, ?, ?, 'activa', ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, UsuarioSesion);
            preparedStatement.setInt(2, idUsuarioSesion);
            preparedStatement.setString(3, actividad);
            preparedStatement.setObject(4, LocalDateTime.now());
            preparedStatement.executeUpdate();
        }

        System.out.println("Actividad registrada: " + actividad);
    }

    public void cerrarSesion() throws SQLException {
        this.horaFinSesion = LocalDateTime.now();
        this.estadoSesion = "cerrada";
        Duration duracion = Duration.between(horaInicioSesion, horaFinSesion);

        String sql = "UPDATE sesion_actividad SET estadoSesion = ?, horaFinSesion = ?, duracion = ? WHERE UsuarioSesion = ? AND idUsuarioSesion = ? AND estadoSesion = 'activa'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, estadoSesion);
            preparedStatement.setObject(2, horaFinSesion);
            preparedStatement.setObject(3, duracion);
            preparedStatement.setString(4, UsuarioSesion);
            preparedStatement.setInt(5, idUsuarioSesion);
            preparedStatement.executeUpdate();
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }

        System.out.println("Sesión cerrada para el usuario: " + UsuarioSesion);
    }

    public String getUsuarioSesion() {
        return UsuarioSesion;
    }

    public int getIdUsuarioSesion() {
        return idUsuarioSesion;
    }

    public LocalDateTime getHoraInicioSesion() {
        return horaInicioSesion;
    }

    public LocalDateTime getHoraFinSesion() {
        return horaFinSesion;
    }

    public String getEstadoSesion() {
        return estadoSesion;
    }
}
