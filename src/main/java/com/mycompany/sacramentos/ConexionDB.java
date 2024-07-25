package com.mycompany.sacramentos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConexionDB {

    private LocalDateTime horaInicioSesion;
    private LocalDateTime horaFinSesion;

    private String UsuarioSesion;
    private String UsuarioActividad;
    private int idUsuarioSesion;
    private String estadoSesion;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/sacramentos";
    private static final String USER = "root";
    private static final String PASS = "Admin_123##";

    public ConexionDB(String UsuarioSesion, int idUsuarioSesion, String UsuarioActividad) {
        this.UsuarioSesion = UsuarioSesion;
        this.idUsuarioSesion = idUsuarioSesion;
        this.UsuarioActividad = UsuarioActividad;
    }

    public void iniciarSesion() throws SQLException {
        this.horaInicioSesion = LocalDateTime.now();
        this.estadoSesion = "activa";
        this.connection = DriverManager.getConnection(URL, USER, PASS);

        String sql = "INSERT INTO sesion_actividad (UsuarioSesion, idUsuarioSesion, horaInicioSesion, estadoSesion, actividadSesion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, UsuarioSesion);
            preparedStatement.setInt(2, idUsuarioSesion);
            preparedStatement.setObject(3, horaInicioSesion);
            preparedStatement.setString(4, estadoSesion);
            preparedStatement.setString(5, UsuarioActividad);
            preparedStatement.executeUpdate();
            // Obtener las claves generadas
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    Integer Id = (int) generatedId;
                    System.out.println("ID generado: " + Id);
                    SingletonDatosUsuario.getInstance().getDatosUsuario().setIdentidad(Id);
                    // Puedes almacenar el ID generado si lo necesitas en la sesión u otra variable
                } else {
                    System.out.println("No se generó ningún ID.");
                }
            }
        }

        System.out.println("Sesión iniciada para el usuario: " + UsuarioSesion);
    }

    public void cerrarSesion() throws SQLException {
        this.horaFinSesion = LocalDateTime.now();
        this.estadoSesion = "cerrada";
        this.connection = DriverManager.getConnection(URL, USER, PASS);
        Duration duracion = Duration.between(SingletonDatosUsuario.getInstance().getDatosUsuario().getFechaHoraRegistro(), horaFinSesion);
        // Convertir la duración a minutos
        long duracionEnMinutos = duracion.toSeconds();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = SingletonDatosUsuario.getInstance().getDatosUsuario().getFechaHoraRegistro().format(formatter);

        String sql = "UPDATE sesion_actividad SET estadoSesion = ?, horaFinSesion = ?, duracion = ? WHERE UsuarioSesion = ? AND idUsuarioSesion = ? AND idRegistro = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, estadoSesion);
            preparedStatement.setObject(2, horaFinSesion);
            preparedStatement.setLong(3, duracionEnMinutos); // Asumiendo que duracionEnMinutos es de tipo long
            preparedStatement.setString(4, UsuarioSesion);
            preparedStatement.setInt(5, idUsuarioSesion);
            preparedStatement.setObject(6, SingletonDatosUsuario.getInstance().getDatosUsuario().getIdentidad()); // Asegúrate de que el tipo sea compatible

            preparedStatement.executeUpdate();
            System.out.println(SingletonDatosUsuario.getInstance().getDatosUsuario().getFechaHoraRegistro());
            System.out.println(formattedDateTime);
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }

        System.out.println("Sesión cerrada para el usuario: " + UsuarioSesion);
    }

    public static Connection getConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error en la conexión", e);
        }
    }
}
