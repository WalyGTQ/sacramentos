/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sacramentos;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    //Boton para imprimir la Constancia
    @FXML
    private Button btnImprimirB;

    //Campos para el ingreso de datos
    @FXML
    private TextField txtLibroB, txtFolioB, txtPartidaB, txtEdadB, txtLugarB, txtNombreB, txtApellidoB, txtLugarNacimientoB, txtPadreB, txtMadreB, txtPadrinoB, txtMadrinaB;
    @FXML
    private TextArea taObservacionesB;
    @FXML
    private CheckBox cbInscritoB;
    @FXML
    private DatePicker dpFechaB, dpNacimientoB;

    //Para validar el Check
    private String inscrito;
    private Integer idBautismo;

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
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        if (comparacionB()) {
            // Actualiza la base de datos

            // Crear ventana de diálogo de confirmación
            if (showConfirmationDialog("Confirmar Actualizacion", "Actualizar registro", "¿Estás seguro? Deseas Actualizar el registro de: " + datosFeligres.getNombre())) {
                // El usuario ha confirmado la eliminación, Código para eliminar el registro

                Connection connection = ConexionDB.getConexion();
                try {

                    connection.setAutoCommit(false); // Start transaction
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
                        idBautismo = rs.getInt("idBautismo");
                        int idFeligres = rs.getInt("idFeligres");

                        // Obtén la fecha de nacimiento del DatePicker
                        // Obtén la fecha de nacimiento del DatePicker
                        LocalDate fechaNacimientoC = dpNacimientoB.getValue();
                        LocalDate fechaInscripcion = dpFechaB.getValue();

                        // Calcula la edad
                        LocalDate hoy = LocalDate.now();
                        int edad = fechaInscripcion.getYear() - fechaNacimientoC.getYear();
                        if (fechaNacimientoC.getDayOfYear() > fechaInscripcion.getDayOfYear()) {
                            edad--; // Ajusta la edad si el cumpleaños de este año aún no ha llegado.
                        }

                        if (fechaNacimientoC.isAfter(hoy)) {
                            showAlert("Error", "La fecha de Nacimiento no puede ser despues de hoy ", "", Alert.AlertType.ERROR);
                            return;
                        }
                        if (fechaNacimientoC.isAfter(fechaInscripcion)) {
                            showAlert("Error", "El Sacramento no puede ser Antes del Nacimiento", "", Alert.AlertType.ERROR);
                            return;
                        }

                        //Manejo del ChecBox Modificado :.(
                        boolean check = cbInscritoB.isSelected();
                        if (check) {
                            inscrito = "Si";
                        } else {
                            inscrito = "No";
                        }

                        // Update Feligres
                        String sqlFeligres = "UPDATE feligres SET nombre = ?, apellido = ?, nacimiento = ?, edadFeligres = ?, lugarNacimiento = ?, padreFeligres = ?, madreFeligres = ? WHERE idFeligres = ?";

                        pstmt = connection.prepareStatement(sqlFeligres);
                        pstmt.setString(1, txtNombreB.getText());
                        pstmt.setString(2, txtApellidoB.getText());
                        pstmt.setDate(3, Date.valueOf(dpNacimientoB.getValue()));
                        pstmt.setInt(4, edad);
                        pstmt.setString(5, txtLugarNacimientoB.getText());
                        pstmt.setString(6, txtPadreB.getText());
                        pstmt.setString(7, txtMadreB.getText());
                        pstmt.setInt(8, idFeligres);
                        pstmt.executeUpdate();

                        // Update Bautismo
                        String sqlUpdateBautismo = "UPDATE bautismo SET fechaSacramento = ?, lugarSacramento = ?, padrino = ?, madrina = ? WHERE idFeligres = ?";
                        pstmt1 = connection.prepareStatement(sqlUpdateBautismo);
                        pstmt1.setDate(1, Date.valueOf(dpFechaB.getValue()));
                        pstmt1.setString(2, txtLugarNacimientoB.getText());
                        pstmt1.setString(3, txtPadrinoB.getText());
                        pstmt1.setString(4, txtMadrinaB.getText());
                        pstmt1.setInt(5, idFeligres);
                        pstmt1.executeUpdate();

                        // Update RegistroLibro
                        String sqlUpdateRegistroLibro = "UPDATE registroLibro SET libro = ?, folio = ?, partida = ?, inscritoLibro = ? WHERE bautismo_idBautismo = ?";
                        pstmt2 = connection.prepareStatement(sqlUpdateRegistroLibro);
                        pstmt2.setInt(1, Integer.parseInt(txtLibroB.getText()));
                        pstmt2.setInt(2, Integer.parseInt(txtFolioB.getText()));
                        pstmt2.setInt(3, Integer.parseInt(txtPartidaB.getText()));
                        pstmt2.setString(4, inscrito);
                        pstmt2.setInt(5, idBautismo);
                        pstmt2.executeUpdate();

                        // Update RegistroLibro
                        String sqlUpdateObservaciones = "UPDATE observacion SET observacion = ? WHERE bautismo_idBautismo = ?";
                        pstmt3 = connection.prepareStatement(sqlUpdateObservaciones);
                        pstmt3.setString(1, taObservacionesB.getText());
                        pstmt3.setInt(2, idBautismo);
                        pstmt3.executeUpdate();

                        // Registro no encontrado
                        showAlert("Información", "El registro fue Actualizado Satisfactoriamente.", "Se Actualizo: " + datosFeligres.getNombre(), Alert.AlertType.INFORMATION);
                        App.setRoot("bautismosVista");
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
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                // El usuario ha cancelado la eliminación, Cualquier acción que consideres necesario
                // Registro no encontrado
                showAlert("Información", "El Usuario a Cancelado la Operacion.", "Ninguna Modificaion Realizada", Alert.AlertType.INFORMATION);
            }

            System.out.println("Cambios detectados");

        } else {
            // No hagas nada, ya que no hubo cambios
            System.out.println("Ningun cambio xD");
        }

    }

    //------------------------------------------Miselaneos
    //Intento de devolver un boleano al comparar los datos
    private boolean comparacionB() {
        // Validacion que sea numero los valores ingresados
        Integer libroAsInteger = null;
        Integer folioAsInteger = null;
        Integer partidaAsInteger = null;

        try {
            libroAsInteger = Integer.parseInt(txtLibroB.getText());
            folioAsInteger = Integer.parseInt(txtFolioB.getText());
            partidaAsInteger = Integer.parseInt(txtPartidaB.getText());
        } catch (NumberFormatException e) {
            // Registro no encontrado
            showAlert("Advertencia", "Algun Valor Ingresado es Invalido", "Revisa que sean Numeros", Alert.AlertType.ERROR);
            return false;
        }
        //Manejo del ChecBox :.(
        boolean check = cbInscritoB.isSelected();
        if (check) {
            inscrito = "Si";
        } else {
            inscrito = "No";
        }

        // Compara cada campo con los datos originales
        if (!datosFeligres.getLibro().equals(libroAsInteger)) {
            return true;
        }
        if (!datosFeligres.getFolio().equals(folioAsInteger)) {
            return true;
        }
        if (!datosFeligres.getPartida().equals(partidaAsInteger)) {
            return true;
        }
        if (!datosFeligres.getFechaSacramento().equals(dpFechaB.getValue())) {
            return true;
        }
        if (!datosFeligres.getNacimiento().equals(dpNacimientoB.getValue())) {
            return true;
        }
        if (!datosFeligres.getLugarSacramento().equals(txtLugarB.getText())) {
            return true;
        }
        if (!datosFeligres.getNombre().equals(txtNombreB.getText())) {
            return true;
        }
        if (!datosFeligres.getApellido().equals(txtApellidoB.getText())) {
            return true;
        }
        if (!datosFeligres.getLugarNacimiento().equals(txtLugarNacimientoB.getText())) {
            return true;
        }
        if (!datosFeligres.getPadre().equals(txtPadreB.getText())) {
            return true;
        }
        if (!datosFeligres.getMadre().equals(txtMadreB.getText())) {
            return true;
        }
        if (!datosFeligres.getPadrino().equals(txtPadrinoB.getText())) {
            return true;
        }
        if (!datosFeligres.getMadrina().equals(txtMadrinaB.getText())) {
            return true;
        }
        if (!datosFeligres.getObservacion().equals(taObservacionesB.getText())) {
            return true;
        }
        if (!datosFeligres.getRegistrado().equals(inscrito)) {
            return true;
        }
        return false;
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

    @FXML
    public void _imprimirB() throws IOException, DocumentException {
        if (showConfirmationDialog("Confirmar Impresion", "Imprimir registro", "¿Estás seguro? Se Imprimira el registro de: " + datosFeligres.getNombre())) {

            Document document = new Document(PageSize.LETTER);
            PdfWriter.getInstance(document, new FileOutputStream("C:/Users/walyn/Downloads/Constancia_Bautismo.pdf"));
            document.open();
            // Añadir un encabezado
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.RED);
            Font fontItalic = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 14, BaseColor.BLACK);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
            Font fontNormalRed = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.RED);

            FeligresDetalle feligres = SingletonData1.getInstance().getFeligresDetalle();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy", new Locale("es", "ES"));

            String fechaSacramentoFormatted = feligres.getFechaSacramento().format(formatter);
            String fechaNacimientoFormatted = feligres.getNacimiento().format(formatter);
            String fechaActualFormatted = LocalDate.now().format(formatter);

            Image img = Image.getInstance("src/main/resources/img/logo1.png"); //  ruta de  imagen
            img.setAbsolutePosition(50, 750); // Coordenadas x, y (desde la esquina inferior izquierda)
            img.scaleToFit(200, 100); // Ancho y alto
            document.add(img);

            Paragraph pHeader = new Paragraph("Parroquia Santo Hermano Pedro\nDiócesis De Sololá-Chimaltenango\n5ᵃ Avenida 4-104, Zona 1\nChimaltenango, Guatemala, C.A.\nTel: 7839-2709", fontHeader);
            pHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(pHeader);

            Paragraph pTitle = new Paragraph("CONSTANCIA DE BAUTIZO", fontTitle);
            pTitle.setAlignment(Element.ALIGN_CENTER);
            pTitle.setSpacingBefore(20);
            document.add(pTitle);

            // Añadir detalles del bautismo
            Paragraph paragraph;

            paragraph = new Paragraph("\nEn esta parroquia el día :", fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("" + fechaSacramentoFormatted, fontItalic);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            paragraph = new Paragraph("\nFue bautizado(a) solemnemente: ", fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph(feligres.getNombre() + " " + feligres.getApellido(), fontItalic);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            paragraph = new Paragraph("\nQuien nació el: ", fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph(fechaNacimientoFormatted, fontItalic);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            paragraph = new Paragraph("\nHijo(a) de: ", fontNormal);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            paragraph = new Paragraph(feligres.getPadre() + " y " + feligres.getMadre(), fontItalic);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            paragraph = new Paragraph("\nFueron sus padrinos: ", fontNormal);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            paragraph = new Paragraph(feligres.getPadrino() + " y " + feligres.getMadrina(), fontItalic);
            paragraph.setAlignment(Element.ALIGN_CENTER);

            document.add(paragraph);

            paragraph = new Paragraph("\nComo consta en el libro: " + feligres.getLibro() + ", folio " + feligres.getFolio() + ", partida " + feligres.getPartida() + " de esta Parroquia.", fontNormal);
            document.add(paragraph);

            if (feligres.getObservacion() != null && !feligres.getObservacion().isEmpty()) {
                paragraph = new Paragraph("\nObservaciones: " + feligres.getObservacion(), fontNormalRed);
                document.add(paragraph);
            }

            // Fecha y firma
            paragraph = new Paragraph("\nChimaltenango, " + fechaActualFormatted, fontNormal);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);

            try {

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                document.close();
            }
            try {
                File myFile = new File("C:/Users/walyn/Downloads/Constancia_Bautismo.pdf");
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(myFile);
                } else {
                    showAlert("Información", "No fue Posible abrir el Documento", "Constancia_Bautismo.pdf", Alert.AlertType.INFORMATION);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Información", "Se Cancelo la Impresion", "Cancelado por Usuario", Alert.AlertType.INFORMATION);
        }
    }

}
