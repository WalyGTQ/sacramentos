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
    private final String UsuarioSesion;
    private final String UsuarioActividad;
    private final int idUsuarioSesion;
    private String estadoSesion;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/sacramentos";
    private static final String USER = "root";
    private static final String PASS = "Admin_123##";

    // Constructor para inicializar los parámetros de sesión
    public ConexionDB(String UsuarioSesion, int idUsuarioSesion, String UsuarioActividad) {
        this.UsuarioSesion = UsuarioSesion;
        this.idUsuarioSesion = idUsuarioSesion;
        this.UsuarioActividad = UsuarioActividad;
    }

    // Método para iniciar la sesión y registrar en la base de datos
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
                    int id = (int) generatedId;
                    System.out.println("ID generado: " + id);
                    SingletonDatosUsuario.getInstance().getDatosUsuario().setIdentidad(id);
                } else {
                    System.out.println("No se generó ningún ID.");
                }
            }
        }

        System.out.println("Sesión iniciada para el usuario: " + UsuarioSesion);
    }

    // Método para cerrar la sesión y actualizar el registro en la base de datos
    public void cerrarSesion() throws SQLException {
        this.horaFinSesion = LocalDateTime.now();
        this.estadoSesion = "cerrada";
        this.connection = DriverManager.getConnection(URL, USER, PASS);

        // Calcular la duración de la sesión
        Duration duracion = Duration.between(SingletonDatosUsuario.getInstance().getDatosUsuario().getFechaHoraRegistro(), horaFinSesion);
        long duracionEnSegundos = duracion.toSeconds();

        // Formatear la fecha y hora de inicio de sesión
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = SingletonDatosUsuario.getInstance().getDatosUsuario().getFechaHoraRegistro().format(formatter);

        String sql = "UPDATE sesion_actividad SET estadoSesion = ?, horaFinSesion = ?, duracion = ? WHERE  idUsuarioSesion = ? AND idRegistro = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, estadoSesion);
            preparedStatement.setObject(2, horaFinSesion);
            preparedStatement.setLong(3, duracionEnSegundos);
            preparedStatement.setInt(4, idUsuarioSesion);
            preparedStatement.setObject(5, SingletonDatosUsuario.getInstance().getDatosUsuario().getIdentidad());

            preparedStatement.executeUpdate();
            System.out.println(SingletonDatosUsuario.getInstance().getDatosUsuario().getFechaHoraRegistro());
            System.out.println(formattedDateTime);
        }

        // Cerrar la conexión si está abierta
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }

        System.out.println("Sesión cerrada para el usuario: " + UsuarioSesion);
    }

    // Método estático para obtener la conexión a la base de datos
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
