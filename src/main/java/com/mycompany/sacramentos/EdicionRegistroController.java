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
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class EdicionRegistroController implements Initializable {

    FeligresDetalle datosFeligres = SingletonData1.getInstance().getFeligresDetalle();

    //Campos para el ingreso de datos
    @FXML
    private TextField txtLibroB, txtFolioB, txtPartidaB, txtEdadB, txtLugarB, txtNombreB, txtApellidoB, txtLugarNacimientoB, txtPadreB, txtMadreB, txtPadrinoB, txtMadrinaB;
    @FXML
    private TextArea taObservacionesB;
    @FXML
    private CheckBox cbInscritoB;
    @FXML
    private DatePicker dpFechaB, dpNacimientoB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Al Inicializar la vista se muestran los datos que previamente se guardaron
        FeligresDetalle feligres = SingletonData1.getInstance().getFeligresDetalle();
        txtLibroB.setText(String.valueOf(feligres.getLibro()));
        txtFolioB.setText(String.valueOf(feligres.getFolio()));
        txtPartidaB.setText(String.valueOf(feligres.getLibro()));
        dpFechaB.setValue(LocalDate.parse(feligres.getFechaSacramento().toString()));
        txtLugarB.setText(feligres.getLugarSacramento());
        txtNombreB.setText(feligres.getNombre());
        txtApellidoB.setText(feligres.getApellido());
        txtEdadB.setText(String.valueOf(feligres.getEdad()));
        dpNacimientoB.setValue(LocalDate.parse(feligres.getNacimiento().toString()));
        txtLugarB.setText(feligres.getLugarSacramento());
        txtPadreB.setText(feligres.getPadre());
        txtMadreB.setText(feligres.getMadre());
        txtPadrinoB.setText(feligres.getPadrino());
        txtMadrinaB.setText(feligres.getMadrina());
        taObservacionesB.setText(feligres.getObservacion());
        txtLugarNacimientoB.setText(feligres.getLugarNacimiento());
        if ("Si".equals(feligres.getRegistrado())) {
            cbInscritoB.setSelected(true); // Si "Si" está registrado, marca el CheckBox
        } else {
            cbInscritoB.setSelected(false); // De lo contrario, desmarca el CheckBox
        }

    }
//Unicamente retorna a la vista Principal
    @FXML
    public void _regresar() throws IOException {
        App.setRoot("bautismosVista");
    }
//Funcion para eliminar un regitro
    @FXML
    public void _eliminarRegistroB() throws IOException, SQLException {
        // Crear ventana de diálogo de confirmación
        if (showConfirmationDialog("Confirmar eliminación", "Eliminar registro", "¿Estás seguro? Deseas eliminar el registro de: " + datosFeligres.getNombre())) {
            // El usuario ha confirmado la eliminación, Código para eliminar el registro

            Connection connection = ConexionDB.getConexion();
            try {
                // 1. Obtener el idBautismo basado en la partida y el nombre
                String queryId = "SELECT b.idBautismo, f.idFeligres "
                        + "FROM feligres f "
                        + "JOIN bautismo b ON f.idFeligres = b.idFeligres "
                        + "JOIN registrolibro r ON b.idBautismo = r.bautismo_idBautismo "
                        + "WHERE f.nombre = ? AND r.partida = ?";
                PreparedStatement stmtId = connection.prepareStatement(queryId);
                stmtId.setString(1, datosFeligres.getNombre());
                stmtId.setInt(2, datosFeligres.getPartida());
                ResultSet rs = stmtId.executeQuery();

                if (rs.next()) {
                    int idBautismo = rs.getInt("idBautismo");
                    int idFeligres = rs.getInt("idFeligres");

                    // 1. Eliminar registros asociados en las tablas secundarias
                    String deleteObservacion = "DELETE FROM observacion WHERE bautismo_idBautismo = ?";
                    PreparedStatement stmtObs = connection.prepareStatement(deleteObservacion);
                    stmtObs.setInt(1, idBautismo);
                    stmtObs.executeUpdate();

                    // 2. Eliminar registros asociados en las tablas secundarias
                    String deleteRegistro = "DELETE FROM registrolibro WHERE bautismo_idBautismo = ?";
                    PreparedStatement stmtReg = connection.prepareStatement(deleteRegistro);
                    stmtReg.setInt(1, idBautismo);
                    stmtReg.executeUpdate();

                    // 3. Eliminar el registro principal en la tabla Bautismo
                    String deleteBautismo = "DELETE FROM bautismo WHERE idBautismo = ?";
                    PreparedStatement stmtDel = connection.prepareStatement(deleteBautismo);
                    stmtDel.setInt(1, idBautismo);
                    stmtDel.executeUpdate();

                    // 4. Eliminar el registro principal en la tabla feligres
                    String deleteFeligres = "DELETE FROM feligres WHERE idFeligres = ?";
                    PreparedStatement stmtDelFel = connection.prepareStatement(deleteFeligres);
                    stmtDelFel.setInt(1, idFeligres);
                    stmtDelFel.executeUpdate();
                    // Registro no encontrado
                    showAlert("Información", "El registro fue eliminado Satisfactoriamente.", "Se Elimino: " + datosFeligres.getNombre(), Alert.AlertType.INFORMATION);
                    App.setRoot("bautismosVista");

                } else {
                    // Registro no encontrado
                    showAlert("Información", "No se encontró un registro con ese nombre y partida.", "", Alert.AlertType.INFORMATION);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Hubo un error al eliminar el registro.", "", Alert.AlertType.ERROR);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            // El usuario ha cancelado la eliminación, Cualquier acción que consideres necesario
            // Registro no encontrado
            showAlert("Información", "El Usuario a Cancelado la Operacion.", "Ninguna Modificaion Realizada", Alert.AlertType.INFORMATION);
        }
    }
    //Funcionalidad para actualizar un registro en especifico
    @FXML
    public void _actualizarB() throws IOException, SQLException {
        
        
    }
    
    
    
    
    //------------------------------------------Miselaneos
    //Para mostrar alertas mas facilmente
    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
//    Para mostrar alertas en caso de necesitar Confirmacion

    private boolean showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
