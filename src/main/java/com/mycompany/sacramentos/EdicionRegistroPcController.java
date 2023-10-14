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
public class EdicionRegistroPcController implements Initializable {

    ConsultaPrimeraComunion feligres = SingletonPrimeraComunion.getInstance().getFeligresDetalle();
    //Instanciamos los campos del FXML hacia aca el controlador :)
    @FXML
    private TextField txtLibroPc, txtFolioPc, txtPartidaPc, txtNombrePc, txtApellidoPc, txtEdadPc, txtLugarBauPc, txtCelebrantePc;
    @FXML
    private TextArea txtAreaObPc;
    @FXML
    private CheckBox boxInscritoPc;
    @FXML
    private DatePicker dpFechaRealizadoPc, dpFechaNacPc;
    @FXML
    private String check;
    //Algunas variables necesarias
    private String inscrito;//para pasar un string por CheckBox

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtLibroPc.setText(String.valueOf(feligres.getLibroC()));
        txtFolioPc.setText(String.valueOf(feligres.getFolioC()));
        txtPartidaPc.setText(String.valueOf(feligres.getPartidaC()));
        dpFechaRealizadoPc.setValue(LocalDate.parse(feligres.getFechaRealizadoPc().toString()));
        txtLugarBauPc.setText(feligres.getLugarC());
        txtNombrePc.setText(feligres.getNombreC());
        txtApellidoPc.setText(feligres.getApellidoC());
        txtEdadPc.setText(String.valueOf(feligres.getEdadC()));
        dpFechaNacPc.setValue(LocalDate.parse(feligres.getFechaNacPc().toString()));
        txtCelebrantePc.setText(feligres.getCelebranteC());
        txtAreaObPc.setText(feligres.getObservacionC());
        if ("Si".equals(feligres.getAnotadoC())) {
            boxInscritoPc.setSelected(true); // Si "Si" está registrado, marca el CheckBox
        } else {
            boxInscritoPc.setSelected(false); // De lo contrario, desmarca el CheckBox
        }
    }

    @FXML
    private void _regresar() throws IOException {
        App.setRoot("primeraComunion");
    }

    @FXML
    private void _actualziarPc() throws IOException, SQLException {
        if (comparacionPc()) {
            //Cuando se detectan Cambios en algun Campo
            PreparedStatement pstmt = null;
            PreparedStatement pstmt0 = null;
            PreparedStatement pstmt1 = null;
            PreparedStatement pstmt2 = null;
            PreparedStatement pstmt3 = null;
            if (showConfirmationDialog("Confirmar Actualizacion", "Actualizar registro", "¿Estás seguro? Deseas Actualizar el registro de: " + feligres.getNombreC())) {
                Connection connection = ConexionDB.getConexion();
                try {

                    connection.setAutoCommit(false); // Start transaction
                    // 1. Obtener el idPrimeraComunion basado en la partida y el nombre
                    String queryId = "SELECT p.idComunion, p.celebrante_idCelebrante, f.idFeligres "
                            + "FROM feligres f "
                            + "JOIN comunion p ON f.idFeligres = p.idFeligres "
                            + "JOIN registrolibro r ON p.idComunion = r.comunion_idComunion "
                            + "WHERE f.nombre = ? AND r.partida = ?";
                    PreparedStatement stmtId = connection.prepareStatement(queryId);
                    stmtId.setString(1, feligres.getNombreC());
                    stmtId.setInt(2, feligres.getPartidaC());
                    ResultSet rs = stmtId.executeQuery();

                    if (rs.next()) {
                        int idComunion = rs.getInt("idComunion");
                        int idFeligres = rs.getInt("idFeligres");
                        int idCelebrante = rs.getInt("celebrante_idCelebrante");

                        // Obtén la fecha de nacimiento del DatePicker
                        LocalDate fechaNacimientoC = dpFechaNacPc.getValue();

                        // Calcula la edad
                        LocalDate hoy = LocalDate.now();
                        int edad = hoy.getYear() - fechaNacimientoC.getYear();
                        if (fechaNacimientoC.getDayOfYear() > hoy.getDayOfYear()) {
                            edad--; // Ajusta la edad si el cumpleaños de este año aún no ha llegado.
                        }

                        //Manejo del ChecBox Modificado :.(
                        boolean check = boxInscritoPc.isSelected();
                        if (check) {
                            inscrito = "Si";
                        } else {
                            inscrito = "No";
                        }

                        // Update Feligres
                        String sqlFeligres = "UPDATE feligres SET nombre = ?, apellido = ?, nacimiento = ?, edadFeligres = ? WHERE idFeligres = ?";
                        pstmt = connection.prepareStatement(sqlFeligres);
                        pstmt.setString(1, txtNombrePc.getText());
                        pstmt.setString(2, txtApellidoPc.getText());
                        pstmt.setDate(3, Date.valueOf(dpFechaNacPc.getValue()));
                        pstmt.setInt(4, edad);
                        pstmt.setInt(5, idFeligres);
                        pstmt.executeUpdate();

                        // Update Celebrante
                        String sqlCelebrante = "UPDATE celebrante SET nombreCelebrante = ? WHERE idCelebrante = ?";
                        pstmt0 = connection.prepareStatement(sqlCelebrante);
                        pstmt0.setString(1, txtCelebrantePc.getText());
                        pstmt0.setInt(2, idCelebrante);
                        pstmt0.executeUpdate();

                        // Update comunion
                        String sqlUpdateBautismo = "UPDATE comunion SET fechaSacramento = ?, lugarSacramento = ? WHERE idComunion = ?";
                        pstmt1 = connection.prepareStatement(sqlUpdateBautismo);
                        pstmt1.setDate(1, Date.valueOf(dpFechaRealizadoPc.getValue()));
                        pstmt1.setString(2, txtLugarBauPc.getText());
                        pstmt1.setInt(3, idComunion);
                        pstmt1.executeUpdate();

                        // Update RegistroLibro
                        String sqlUpdateRegistroLibro = "UPDATE registroLibro SET libro = ?, folio = ?, partida = ?, inscritoLibro = ? WHERE comunion_idComunion = ?";
                        pstmt2 = connection.prepareStatement(sqlUpdateRegistroLibro);
                        pstmt2.setInt(1, Integer.parseInt(txtLibroPc.getText()));
                        pstmt2.setInt(2, Integer.parseInt(txtFolioPc.getText()));
                        pstmt2.setInt(3, Integer.parseInt(txtPartidaPc.getText()));
                        pstmt2.setString(4, inscrito);
                        pstmt2.setInt(5, idComunion);
                        pstmt2.executeUpdate();

                        // Update Observacion
                        String sqlUpdateObservaciones = "UPDATE observacion SET observacion = ? WHERE comunion_idComunion = ?";
                        pstmt3 = connection.prepareStatement(sqlUpdateObservaciones);
                        pstmt3.setString(1, txtAreaObPc.getText());
                        pstmt3.setInt(2, idComunion);
                        pstmt3.executeUpdate();

                        // Registro no encontrado
                        showAlert("Información", "El registro fue Actualizado Satisfactoriamente.", "Se Actualizo: " + feligres.getNombreC(), Alert.AlertType.INFORMATION);
                        App.setRoot("PrimeraComunion");
                        connection.commit(); // Commit transaction
                    } else {
                        // Registro no encontrado
                        showAlert("Información", "No se encontró un registro con ese nombre y partida.", "", Alert.AlertType.INFORMATION);
                    }

                } catch (SQLException ex) {
                    connection.rollback();
                    ex.printStackTrace();
                    showAlert("Error", "Hubo un error al actualizar el registro.", "", Alert.AlertType.ERROR);
                } finally {
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                        if (pstmt != null) {
                            pstmt.close();
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
            showAlert("Informacion", "Ningun Cambio Realizado", "Ningun Campo fue Editado", Alert.AlertType.INFORMATION);
        }
    }

    
    @FXML
    private void _EliminarPc() throws IOException{
                // Crear ventana de diálogo de confirmación
        if (showConfirmationDialog("Confirmar eliminación", "Eliminar registro", "¿Estás seguro? Deseas eliminar el registro de: " + feligres.getNombreC())) {
            // El usuario ha confirmado la eliminación, Código para eliminar el registro

            Connection connection = ConexionDB.getConexion();
            try {
                // 1. Obtener el idComunion basado en la partida y el nombre
                String queryId = "SELECT p.idComunion, f.idFeligres, c.idCelebrante "
                        + "FROM feligres f "
                        + "JOIN comunion p ON f.idFeligres = p.idFeligres "
                        + "JOIN registrolibro r ON p.idComunion = r.comunion_idComunion "
                        + "JOIN celebrante c ON p.celebrante_idCelebrante = c.idCelebrante "
                        + "WHERE f.nombre = ? AND r.partida = ?";
                PreparedStatement stmtId = connection.prepareStatement(queryId);
                stmtId.setString(1, feligres.getNombreC());
                stmtId.setInt(2, feligres.getPartidaC());
                ResultSet rs = stmtId.executeQuery();

                if (rs.next()) {
                    int idComunion = rs.getInt("idComunion");
                    int idFeligres = rs.getInt("idFeligres");
                    int idCelebrante = rs.getInt("idCelebrante");

                    // 1. Eliminar registros asociados en las tablas secundarias
                    String deleteObservacion = "DELETE FROM observacion WHERE comunion_idComunion = ?";
                    PreparedStatement stmtObs = connection.prepareStatement(deleteObservacion);
                    stmtObs.setInt(1, idComunion);
                    stmtObs.executeUpdate();

                    // 2. Eliminar registros asociados en las tablas secundarias
                    String deleteRegistro = "DELETE FROM registrolibro WHERE comunion_idComunion = ?";
                    PreparedStatement stmtReg = connection.prepareStatement(deleteRegistro);
                    stmtReg.setInt(1, idComunion);
                    stmtReg.executeUpdate();

                    // 3. Eliminar el registro principal en la tabla Bautismo
                    String deleteBautismo = "DELETE FROM comunion WHERE idComunion = ?";
                    PreparedStatement stmtDel = connection.prepareStatement(deleteBautismo);
                    stmtDel.setInt(1, idComunion);
                    stmtDel.executeUpdate();

                    // 4. Eliminar el registro principal en la tabla feligres
                    String deleteFeligres = "DELETE FROM feligres WHERE idFeligres = ?";
                    PreparedStatement stmtDelFel = connection.prepareStatement(deleteFeligres);
                    stmtDelFel.setInt(1, idFeligres);
                    stmtDelFel.executeUpdate();
                    
                    // 5. Eliminar el registro del Celebrante
                    String deleteCelebrante = "DELETE FROM celebrante WHERE idCelebrante = ?";
                    PreparedStatement stmtDelCel = connection.prepareStatement(deleteCelebrante);
                    stmtDelCel.setInt(1, idCelebrante);
                    stmtDelCel.executeUpdate();
                    
                    // Registro no encontrado
                    showAlert("Información", "El registro fue eliminado Satisfactoriamente.", "Se Elimino: " + feligres.getNombreC(), Alert.AlertType.INFORMATION);
                    App.setRoot("primeraComunion");

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
    //Otros Codigos
    //Intento de devolver un boleano al comparar los datos
    private boolean comparacionPc() {
        // Validacion que sea numero los valores ingresados
        Integer libroAsInteger;
        Integer folioAsInteger;
        Integer partidaAsInteger;
        Integer edadPc;

        try {
            libroAsInteger = Integer.parseInt(txtLibroPc.getText());
            folioAsInteger = Integer.parseInt(txtFolioPc.getText());
            partidaAsInteger = Integer.parseInt(txtPartidaPc.getText());
            edadPc = Integer.parseInt(txtEdadPc.getText());
        } catch (NumberFormatException e) {
            showAlert("Advertencia", "Algun Valor Ingresado es Invalido", "Libro, Folio y Partida deben ser Números", Alert.AlertType.ERROR);
            return false; // Si hay un error, termina el método aquí.
        }

        // Simplificación en el manejo de CheckBox
        inscrito = boxInscritoPc.isSelected() ? "Si" : "No";

        // Simplifica las comparaciones usando ||
        return !feligres.getLibroC().equals(libroAsInteger)
                || !feligres.getFolioC().equals(folioAsInteger)
                || !feligres.getPartidaC().equals(partidaAsInteger)
                || !feligres.getFechaRealizadoPc().equals(dpFechaRealizadoPc.getValue())
                || !feligres.getLugarC().equals(txtLugarBauPc.getText())
                || !feligres.getNombreC().equals(txtNombrePc.getText())
                || !feligres.getApellidoC().equals(txtApellidoPc.getText())
                || !feligres.getEdadC().equals(edadPc)
                || !feligres.getFechaNacPc().equals(dpFechaNacPc.getValue())
                || !feligres.getCelebranteC().equals(txtCelebrantePc.getText())
                || !feligres.getObservacionC().equals(txtAreaObPc.getText())
                || !feligres.getAnotadoC().equals(inscrito);
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
