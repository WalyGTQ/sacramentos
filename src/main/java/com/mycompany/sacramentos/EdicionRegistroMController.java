package com.mycompany.sacramentos;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
public class EdicionRegistroMController implements Initializable {

    //Instancia de la Clase Singleton para obtener los datos guardados
    ConsultaMatrimonio feligresDetalle = SingletonMatrimonio.getInstance().getFeligresDetalle();
    //LLamado a los elementos del FXML
    @FXML
    private TextField txtLibroM, txtFolioM, txtPartidaM, txtlugarM, txtTestigo1M, txtTestigo2M, txtNombreMM, txtApellidoMM, txtEdadMM, txtOrigenMM, txtFeligresMM, txtPadreMM, txtMadreMM, txtNombreFM, txtApellidoFM, txtEdadFM, txtOrigenFM, txtFeligresFM, txtPadreFM, txtMadreFM, txtCelebranteM;
    @FXML
    private TextArea txtAreaM;
    @FXML
    private CheckBox boxInscritoM;
    @FXML
    private DatePicker dpFechaM;
    private String check;
    private String inscrito;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtLibroM.setText(String.valueOf(feligresDetalle.getLibroM()));
        txtFolioM.setText(String.valueOf(feligresDetalle.getFolioM()));
        txtPartidaM.setText(String.valueOf(feligresDetalle.getPartidaM()));
        dpFechaM.setValue(LocalDate.parse(feligresDetalle.getFechaM().toString()));
        txtlugarM.setText(feligresDetalle.getLugarM());
        txtTestigo1M.setText(feligresDetalle.getTestigoUnoM());
        txtTestigo2M.setText(feligresDetalle.getTestigoDosM());
        txtNombreMM.setText(feligresDetalle.getNombreMM());
        txtApellidoMM.setText(feligresDetalle.getApellidoMM());
        txtEdadMM.setText(String.valueOf(feligresDetalle.getEdadMM()));
        txtOrigenMM.setText(feligresDetalle.getOrigenMM());
        txtFeligresMM.setText(feligresDetalle.getFeligresMM());
        txtPadreMM.setText(feligresDetalle.getPadreMM());
        txtMadreMM.setText(feligresDetalle.getMadreMM());
        txtNombreFM.setText(feligresDetalle.getNombreFM());
        txtApellidoFM.setText(feligresDetalle.getApellidoFM());
        txtEdadFM.setText(String.valueOf(feligresDetalle.getEdadFM()));
        txtOrigenFM.setText(feligresDetalle.getOrigenFM());
        txtFeligresFM.setText(feligresDetalle.getFeligresFM());
        txtPadreFM.setText(feligresDetalle.getPadreFM());
        txtMadreFM.setText(feligresDetalle.getMadreFM());
        txtCelebranteM.setText(feligresDetalle.getCelebranteM());
        txtAreaM.setText(feligresDetalle.getObservacionM());
        if ("Si".equals(feligresDetalle.getInscritoM())) {
            boxInscritoM.setSelected(true); // Si "Si" está registrado, marca el CheckBox
        } else {
            boxInscritoM.setSelected(false); // De lo contrario, desmarca el CheckBox
        }
    }

    @FXML
    private void _regresar() throws IOException {
        App.setRoot("matrimonios");
    }
    
    @FXML //Funcionalidad Para Actualizar un registro Especifico
    private void _actualizarM() throws IOException {
        if (comparacionM()) {
            //Codigo Cuanndo se detecta cambios en algun campo
            if (showConfirmationDialog("Confirmar Actualizacion", "Actualizar registro", "¿Estás seguro? Deseas Actualizar el registro de: " + feligresDetalle.getNombreMM() + " Y " + feligresDetalle.getNombreFM())) {

                PreparedStatement pstmtM = null;
                PreparedStatement pstmtF = null;
                PreparedStatement pstmt0 = null;
                PreparedStatement pstmt1 = null;
                PreparedStatement pstmt2 = null;
                PreparedStatement pstmt3 = null;
                Connection connection = ConexionDB.getConexion();
                int edadM = Integer.parseInt(txtEdadMM.getText());
                int edadF = Integer.parseInt(txtEdadFM.getText());
                try {

                    connection.setAutoCommit(false); // Start transaction
                    // 1. Obtener el idPrimeraComunion basado en la partida y el nombre
                    String queryId = "SELECT m.idMatrimonio, m.celebrante_idCelebrante, f1.idFeligres AS idFeligres1, f2.idFeligres AS idFeligres2 "
                            + "FROM matrimonios m "
                            + "JOIN Feligres f1 ON m.idFeligres1 = f1.idFeligres "
                            + "JOIN Feligres f2 ON m.idFeligres2 = f2.idFeligres "
                            + "JOIN registrolibro r ON m.idMatrimonio = r.matrimonio_idMatrimonio "
                            + "WHERE f1.nombre = ? AND f2.nombre = ? AND r.partida = ? ";
                    PreparedStatement stmtId = connection.prepareStatement(queryId);
                    stmtId.setString(1, feligresDetalle.getNombreMM());
                    stmtId.setString(2, feligresDetalle.getNombreFM());
                    stmtId.setInt(3, feligresDetalle.getPartidaM());
                    ResultSet rs = stmtId.executeQuery();

                    if (rs.next()) {
                        int idMatrimonio = rs.getInt("idMatrimonio");
                        int idFeligres1 = rs.getInt("idFeligres1");
                        int idFeligres2 = rs.getInt("idFeligres2");
                        int idCelebrante = rs.getInt("celebrante_idCelebrante");

                    

                        //Manejo del ChecBox Modificado :.(
                        boolean check = boxInscritoM.isSelected();
                        if (check) {
                            inscrito = "Si";
                        } else {
                            inscrito = "No";
                        }
                         //Update Feligreses :.)
                        String sqlFeligresM = "UPDATE feligres SET nombre = ?, apellido = ?, edadFeligres = ?, lugarNacimiento = ?, feligresDe = ?, madreFeligres = ?, padreFeligres = ? WHERE idFeligres = ?";
                        pstmtM = connection.prepareStatement(sqlFeligresM);
                        pstmtM.setString(1, txtNombreMM.getText());
                        pstmtM.setString(2, txtApellidoMM.getText());
                        pstmtM.setInt(3, edadM);
                        pstmtM.setString(4, txtOrigenMM.getText());
                        pstmtM.setString(5, txtFeligresMM.getText());
                        pstmtM.setString(6, txtPadreMM.getText());
                        pstmtM.setString(7, txtMadreMM.getText());
                        pstmtM.setInt(8, idFeligres1);
                        pstmtM.executeUpdate();
                        
                        String sqlFeligresF = "UPDATE feligres SET nombre = ?, apellido = ?, edadFeligres = ?, lugarNacimiento = ?, feligresDe = ?, madreFeligres = ?, padreFeligres = ? WHERE idFeligres = ?";
                        pstmtF = connection.prepareStatement(sqlFeligresF);
                        pstmtF.setString(1, txtNombreFM.getText());
                        pstmtF.setString(2, txtApellidoFM.getText());
                        pstmtF.setInt(3, edadF);
                        pstmtF.setString(4, txtOrigenFM.getText());
                        pstmtF.setString(5, txtFeligresFM.getText());
                        pstmtF.setString(6, txtPadreFM.getText());
                        pstmtF.setString(7, txtMadreFM.getText());
                        pstmtF.setInt(8, idFeligres2);
                        pstmtF.executeUpdate();

                        // Update Celebrante
                        String sqlCelebrante = "UPDATE celebrante SET nombreCelebrante = ? WHERE idCelebrante = ?";
                        pstmt0 = connection.prepareStatement(sqlCelebrante);
                        pstmt0.setString(1, txtCelebranteM.getText());
                        pstmt0.setInt(2, idCelebrante);
                        pstmt0.executeUpdate();

                        // Update comunion
                        String sqlUpdateMatrimonio = "UPDATE matrimonios SET fechaSacramento = ?, lugarSacramento = ?, testigo1 = ?, testigo2 = ? WHERE idMatrimonio = ?";
                        pstmt1 = connection.prepareStatement(sqlUpdateMatrimonio);
                        pstmt1.setDate(1, Date.valueOf(dpFechaM.getValue()));
                        pstmt1.setString(2, txtlugarM.getText());
                        pstmt1.setString(3, txtTestigo1M.getText());
                        pstmt1.setString(4, txtTestigo2M.getText());
                        pstmt1.setInt(5, idMatrimonio);
                        pstmt1.executeUpdate();

                        // Update RegistroLibro
                        String sqlUpdateRegistroLibro = "UPDATE registroLibro SET libro = ?, folio = ?, partida = ?, inscritoLibro = ? WHERE matrimonio_idMatrimonio = ?";
                        pstmt2 = connection.prepareStatement(sqlUpdateRegistroLibro);
                        pstmt2.setInt(1, Integer.parseInt(txtLibroM.getText()));
                        pstmt2.setInt(2, Integer.parseInt(txtFolioM.getText()));
                        pstmt2.setInt(3, Integer.parseInt(txtPartidaM.getText()));
                        pstmt2.setString(4, inscrito);
                        pstmt2.setInt(5, idMatrimonio);
                        pstmt2.executeUpdate();

                        // Update Observacion
                        String sqlUpdateObservaciones = "UPDATE observacion SET observacion = ? WHERE matrimonio_idMatrimonio = ?";
                        pstmt3 = connection.prepareStatement(sqlUpdateObservaciones);
                        pstmt3.setString(1, txtAreaM.getText());
                        pstmt3.setInt(2, idMatrimonio);
                        pstmt3.executeUpdate();

                        // Registro no encontrado
                        showAlert("Información", "El registro fue Actualizado Satisfactoriamente.", "Se Actualizo: " + feligresDetalle.getNombreMM() + " Y " + feligresDetalle.getNombreFM(), Alert.AlertType.INFORMATION);
                        App.setRoot("matrimonios");
                        connection.commit(); // Commit transaction
                    } else {
                        // Registro no encontrado
                        showAlert("Información", "No se encontró un registro con ese nombre y partida.", "", Alert.AlertType.INFORMATION);
                    }

                } catch (SQLException ex) {
                    //connection.rollback();
                    ex.printStackTrace();
                    showAlert("Error", "Hubo un error al actualizar el registro.", "", Alert.AlertType.ERROR);
                } finally {
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                        if (pstmtM != null) {
                            pstmtM.close();
                        }
                        if (pstmt1 != null) {
                            pstmt1.close();
                        }
                        if (pstmt2 != null) {
                            pstmt2.close();
                        }
                        if (pstmt3 != null) {
                            pstmt3.close();
                        }
                        if (pstmtF != null) {
                            pstmtF.close();
                        }
                        if (pstmt0 != null) {
                            pstmt0.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
 
                
                
                
            } else {
                // El usuario ha cancelado la Actualizacion, Acciones Competentes

                showAlert("Información", "El Usuario a Cancelado la Operacion.", "Ninguna Modificaion Realizada", Alert.AlertType.INFORMATION);
            }

        } else {
            //Codigo cuando no existe nungun cambio ningun campo
            showAlert("Informacion", "Ningun Cambio Realizado", "Ningun Campo fue Editado", Alert.AlertType.INFORMATION);
        }

    }

    @FXML //Funcionalidad Para eliminar un Registro Especifico
    private void _eliminarM() throws IOException, SQLException {
        // Crear ventana de diálogo de confirmación
        if (showConfirmationDialog("Confirmar eliminación", "Eliminar registro", "¿Estás seguro? Deseas eliminar el registro de: " + feligresDetalle.getNombreMM() + " Y " + feligresDetalle.getNombreFM())) {
            // El usuario ha confirmado la eliminación, Código para eliminar el registro

            Connection connection = ConexionDB.getConexion();
            try {
                connection.setAutoCommit(false); // Start transaction
                    // 1. Obtener el idPrimeraComunion basado en la partida y el nombre
                    String queryId = "SELECT m.idMatrimonio, m.celebrante_idCelebrante, f1.idFeligres AS idFeligres1, f2.idFeligres AS idFeligres2 "
                            + "FROM matrimonios m "
                            + "JOIN Feligres f1 ON m.idFeligres1 = f1.idFeligres "
                            + "JOIN Feligres f2 ON m.idFeligres2 = f2.idFeligres "
                            + "JOIN registrolibro r ON m.idMatrimonio = r.matrimonio_idMatrimonio "
                            + "WHERE f1.nombre = ? AND f2.nombre = ? AND r.partida = ? ";
                    PreparedStatement stmtId = connection.prepareStatement(queryId);
                    stmtId.setString(1, feligresDetalle.getNombreMM());
                    stmtId.setString(2, feligresDetalle.getNombreFM());
                    stmtId.setInt(3, feligresDetalle.getPartidaM());
                    ResultSet rs = stmtId.executeQuery();

                if (rs.next()) {
                        int idMatrimonio = rs.getInt("idMatrimonio");
                        int idFeligres1 = rs.getInt("idFeligres1");
                        int idFeligres2 = rs.getInt("idFeligres2");
                        int idCelebrante = rs.getInt("celebrante_idCelebrante");

                    // 1. Eliminar registros asociados en las tablas secundarias
                    String deleteObservacion = "DELETE FROM observacion WHERE matrimonio_idMatrimonio = ?";
                    PreparedStatement stmtObs = connection.prepareStatement(deleteObservacion);
                    stmtObs.setInt(1, idMatrimonio);
                    stmtObs.executeUpdate();

                    // 2. Eliminar registros asociados en las tablas secundarias
                    String deleteRegistro = "DELETE FROM registrolibro WHERE matrimonio_idMatrimonio = ?";
                    PreparedStatement stmtReg = connection.prepareStatement(deleteRegistro);
                    stmtReg.setInt(1, idMatrimonio);
                    stmtReg.executeUpdate();

                    // 3. Eliminar el registro principal en la tabla Matrimonio
                    String deleteMatrimonio = "DELETE FROM matrimonios WHERE IdMatrimonio = ?";
                    PreparedStatement stmtDel = connection.prepareStatement(deleteMatrimonio);
                    stmtDel.setInt(1, idMatrimonio);
                    stmtDel.executeUpdate();

                    // 4. Eliminar el registro principal en la tabla feligres 1
                    String deleteFeligres1 = "DELETE FROM feligres WHERE idFeligres = ?";
                    PreparedStatement stmtDelFel1 = connection.prepareStatement(deleteFeligres1);
                    stmtDelFel1.setInt(1, idFeligres1);
                    stmtDelFel1.executeUpdate();
                    
                    // 5. Eliminar el registro principal en la tabla feligres 2
                    String deleteFeligres2 = "DELETE FROM feligres WHERE idFeligres = ?";
                    PreparedStatement stmtDelFel2 = connection.prepareStatement(deleteFeligres2);
                    stmtDelFel2.setInt(1, idFeligres2);
                    stmtDelFel2.executeUpdate();

                    // 6. Eliminar el registro del Celebrante
                    String deleteCelebrante = "DELETE FROM celebrante WHERE idCelebrante = ?";
                    PreparedStatement stmtDelCel = connection.prepareStatement(deleteCelebrante);
                    stmtDelCel.setInt(1, idCelebrante);
                    stmtDelCel.executeUpdate();
                    connection.commit();
                    // Registro no encontrado
                    showAlert("Información", "El registro fue eliminado Satisfactoriamente.", "Se Elimino: " + feligresDetalle.getNombreMM() + " Y " + feligresDetalle.getNombreFM(), Alert.AlertType.INFORMATION);
                    App.setRoot("matrimonios");

                } else {
                    // Registro no encontrado
                    showAlert("Información", "No se encontró un registro con ese nombre y partida.", "", Alert.AlertType.INFORMATION);
                }
            } catch (SQLException ex) {
                connection.rollback();

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

    private boolean comparacionM() {
        // Validacion que sea numero los valores ingresados
// Validacion que sea número los valores ingresados
        Integer libroAsInteger;
        Integer folioAsInteger;
        Integer partidaAsInteger;
        Integer edadMMAsInteger;
        Integer edadFMAsInteger;

        try {
            libroAsInteger = Integer.parseInt(txtLibroM.getText());
            folioAsInteger = Integer.parseInt(txtFolioM.getText());
            partidaAsInteger = Integer.parseInt(txtPartidaM.getText());
            edadMMAsInteger = Integer.parseInt(txtEdadMM.getText());
            edadFMAsInteger = Integer.parseInt(txtEdadFM.getText());
        } catch (NumberFormatException e) {
            showAlert("Advertencia", "Algun Valor Ingresado es Invalido", "Libro, Folio, Partida, EdadMM y EdadFM deben ser Números", Alert.AlertType.ERROR);
            return false; // Si hay un error, termina el método aquí.
        }

        // Simplificación en el manejo de CheckBox
        inscrito = boxInscritoM.isSelected() ? "Si" : "No";

        // Comparaciones adaptadas según los nuevos campos
        return !feligresDetalle.getLibroM().equals(libroAsInteger)
                || !feligresDetalle.getFolioM().equals(folioAsInteger)
                || !feligresDetalle.getPartidaM().equals(partidaAsInteger)
                || !feligresDetalle.getFechaM().equals(dpFechaM.getValue())
                || !feligresDetalle.getLugarM().equals(txtlugarM.getText())
                || !feligresDetalle.getTestigoUnoM().equals(txtTestigo1M.getText())
                || !feligresDetalle.getTestigoDosM().equals(txtTestigo2M.getText())
                || !feligresDetalle.getNombreMM().equals(txtNombreMM.getText())
                || !feligresDetalle.getApellidoMM().equals(txtApellidoMM.getText())
                || !feligresDetalle.getEdadMM().equals(edadMMAsInteger)
                || !feligresDetalle.getOrigenMM().equals(txtOrigenMM.getText())
                || !feligresDetalle.getFeligresMM().equals(txtFeligresMM.getText())
                || !feligresDetalle.getPadreMM().equals(txtPadreMM.getText())
                || !feligresDetalle.getMadreMM().equals(txtMadreMM.getText())
                || !feligresDetalle.getNombreFM().equals(txtNombreFM.getText())
                || !feligresDetalle.getApellidoFM().equals(txtApellidoFM.getText())
                || !feligresDetalle.getEdadFM().equals(edadFMAsInteger)
                || !feligresDetalle.getOrigenFM().equals(txtOrigenFM.getText())
                || !feligresDetalle.getFeligresFM().equals(txtFeligresFM.getText())
                || !feligresDetalle.getPadreFM().equals(txtPadreFM.getText())
                || !feligresDetalle.getMadreFM().equals(txtMadreFM.getText())
                || !feligresDetalle.getCelebranteM().equals(txtCelebranteM.getText())
                || !feligresDetalle.getObservacionM().equals(txtAreaM.getText())
                || !feligresDetalle.getInscritoM().equals(inscrito);
    }

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
