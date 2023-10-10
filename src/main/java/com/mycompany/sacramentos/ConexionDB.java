
package com.mycompany.sacramentos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
 private static final String URL = "jdbc:mysql://localhost:3306/sacramentos";
    private static final String USER = "root";
    private static final String PASS = "Admin_123##";
    
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
