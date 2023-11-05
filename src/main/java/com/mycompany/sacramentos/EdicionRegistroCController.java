package com.mycompany.sacramentos;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
public class EdicionRegistroCController implements Initializable {

    //Instancia de la Clase Singleton para obtener los datos guardados
    ConsultaConfirmacion feligresDetalle = SingletonConfirmacion.getInstance().getFeligresDetalle();

    //LLamado a los elementos del FXML
    @FXML
    private TextField txtLibroCf, txtFolioCf, txtPartidaCf, txtNombreCf, txtApellidoCf, txtEdadCf, txtPadreCf, txtMadreCf, txtPadrino_MadrinaCf, txtLugarCf, txtCelebranteCf;
    @FXML
    private TextArea txtAreaCf;
    @FXML
    private CheckBox boxInscritoCf;
    @FXML
    private DatePicker dpFechaSacCf, dpNacimientoCf;
    private String check;
    private String inscrito;//para pasar un string por CheckBox
    private int edad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        txtLibroCf.setText(String.valueOf(feligresDetalle.getLibroCf()));
        txtFolioCf.setText(String.valueOf(feligresDetalle.getFolioCf()));
        txtPartidaCf.setText(String.valueOf(feligresDetalle.getPartidaCf()));
        dpFechaSacCf.setValue(LocalDate.parse(feligresDetalle.getFechaCf().toString()));
        txtLugarCf.setText(feligresDetalle.getLugarCf());
        txtNombreCf.setText(feligresDetalle.getNombreCf());
        txtApellidoCf.setText(feligresDetalle.getApellidoCf());
        txtEdadCf.setText(String.valueOf(feligresDetalle.getEdadCf()));
        dpNacimientoCf.setValue(LocalDate.parse(feligresDetalle.getNacimientoCf().toString()));
        txtCelebranteCf.setText(feligresDetalle.getCelebranteCf());
        txtPadreCf.setText(feligresDetalle.getPadreCf());
        txtMadreCf.setText(feligresDetalle.getMadreCf());
        txtPadrino_MadrinaCf.setText(feligresDetalle.getPadrinosCf());
        txtAreaCf.setText(feligresDetalle.getObservacionesCf());
        if ("Si".equals(feligresDetalle.getInscritoCf())) {
            boxInscritoCf.setSelected(true); // Si "Si" está registrado, marca el CheckBox
        } else {
            boxInscritoCf.setSelected(false); // De lo contrario, desmarca el CheckBox
        }
    }

    @FXML
    private void _regresar() throws IOException {
        App.setRoot("confirmacion");
    }

    @FXML
    private void _actualizarC() throws IOException, SQLException {
        if (comparacionC()) {
            //Cuando se detectan Cambios en algun Campo

            if (showConfirmationDialog("Confirmar Actualizacion", "Actualizar registro", "¿Estás seguro? Deseas Actualizar el registro de: " + feligresDetalle.getNombreCf())) {
                PreparedStatement pstmt = null;
                PreparedStatement pstmt0 = null;
                PreparedStatement pstmt1 = null;
                PreparedStatement pstmt2 = null;
                PreparedStatement pstmt3 = null;
                Connection connection = ConexionDB.getConexion();
                try {

                    connection.setAutoCommit(false); // Start transaction
                    // 1. Obtener el idPrimeraComunion basado en la partida y el nombre
                    String queryId = "SELECT c.idConfirmacion, c.celebrante_idCelebrante, f.idFeligres "
                            + "FROM feligres f "
                            + "JOIN confirmacion c ON f.idFeligres = c.idFeligres "
                            + "JOIN registrolibro r ON c.idConfirmacion = r.confirmacion_idConfirmacion "
                            + "WHERE f.nombre = ? AND r.partida = ?";
                    PreparedStatement stmtId = connection.prepareStatement(queryId);
                    stmtId.setString(1, feligresDetalle.getNombreCf());
                    stmtId.setInt(2, feligresDetalle.getPartidaCf());
                    ResultSet rs = stmtId.executeQuery();

                    if (rs.next()) {
                        int idConfirmacion = rs.getInt("idConfirmacion");
                        int idFeligres = rs.getInt("idFeligres");
                        int idCelebrante = rs.getInt("celebrante_idCelebrante");

                        // Obtén la fecha de nacimiento del DatePicker
                        LocalDate fechaNacimientoC = dpNacimientoCf.getValue();
                        LocalDate fechaInscripcion = dpFechaSacCf.getValue();

                        // Calcula la edad
                        LocalDate hoy = LocalDate.now();
                        edad = fechaInscripcion.getYear() - fechaNacimientoC.getYear();
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
                        boolean check = boxInscritoCf.isSelected();
                        if (check) {
                            inscrito = "Si";
                        } else {
                            inscrito = "No";
                        }

                        // Update Feligres
                        String sqlFeligres = "UPDATE feligres SET nombre = ?, apellido = ?, nacimiento = ?, edadFeligres = ?, madreFeligres = ?, padreFeligres = ? WHERE idFeligres = ?";
                        pstmt = connection.prepareStatement(sqlFeligres);
                        pstmt.setString(1, txtNombreCf.getText());
                        pstmt.setString(2, txtApellidoCf.getText());
                        pstmt.setDate(3, Date.valueOf(dpNacimientoCf.getValue()));
                        pstmt.setInt(4, edad);
                        pstmt.setString(5, txtPadreCf.getText());
                        pstmt.setString(6, txtMadreCf.getText());
                        pstmt.setInt(7, idFeligres);
                        pstmt.executeUpdate();

                        // Update Celebrante
                        String sqlCelebrante = "UPDATE celebrante SET nombreCelebrante = ? WHERE idCelebrante = ?";
                        pstmt0 = connection.prepareStatement(sqlCelebrante);
                        pstmt0.setString(1, txtCelebranteCf.getText());
                        pstmt0.setInt(2, idCelebrante);
                        pstmt0.executeUpdate();

                        // Update comunion
                        String sqlUpdateConfirmacion = "UPDATE confirmacion SET fechaSacramento = ?, lugarSacramento = ?, padrino = ? WHERE idConfirmacion = ?";
                        pstmt1 = connection.prepareStatement(sqlUpdateConfirmacion);
                        pstmt1.setDate(1, Date.valueOf(dpFechaSacCf.getValue()));
                        pstmt1.setString(2, txtLugarCf.getText());
                        pstmt1.setString(3, txtPadrino_MadrinaCf.getText());
                        pstmt1.setInt(4, idConfirmacion);
                        pstmt1.executeUpdate();

                        // Update RegistroLibro
                        String sqlUpdateRegistroLibro = "UPDATE registroLibro SET libro = ?, folio = ?, partida = ?, inscritoLibro = ? WHERE confirmacion_idConfirmacion = ?";
                        pstmt2 = connection.prepareStatement(sqlUpdateRegistroLibro);
                        pstmt2.setInt(1, Integer.parseInt(txtLibroCf.getText()));
                        pstmt2.setInt(2, Integer.parseInt(txtFolioCf.getText()));
                        pstmt2.setInt(3, Integer.parseInt(txtPartidaCf.getText()));
                        pstmt2.setString(4, inscrito);
                        pstmt2.setInt(5, idConfirmacion);
                        pstmt2.executeUpdate();

                        // Update Observacion
                        String sqlUpdateObservaciones = "UPDATE observacion SET observacion = ? WHERE confirmacion_idConfirmacion = ?";
                        pstmt3 = connection.prepareStatement(sqlUpdateObservaciones);
                        pstmt3.setString(1, txtAreaCf.getText());
                        pstmt3.setInt(2, idConfirmacion);
                        pstmt3.executeUpdate();

                        // Registro no encontrado
                        showAlert("Información", "El registro fue Actualizado Satisfactoriamente.", "Se Actualizo: " + feligresDetalle.getNombreCf(), Alert.AlertType.INFORMATION);
                        App.setRoot("confirmacion");
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
        // App.setRoot("confirmacion");
    }

    @FXML
    private void _eliminarC() throws IOException {
        // Crear ventana de diálogo de confirmación
        if (showConfirmationDialog("Confirmar eliminación", "Eliminar registro", "¿Estás seguro? Deseas eliminar el registro de: " + feligresDetalle.getNombreCf())) {
            // El usuario ha confirmado la eliminación, Código para eliminar el registro

            Connection connection = ConexionDB.getConexion();
            try {
                // 1. Obtener el idPrimeraComunion basado en la partida y el nombre
                String queryId = "SELECT c.idConfirmacion, c.celebrante_idCelebrante, f.idFeligres "
                        + "FROM feligres f "
                        + "JOIN confirmacion c ON f.idFeligres = c.idFeligres "
                        + "JOIN registrolibro r ON c.idConfirmacion = r.confirmacion_idConfirmacion "
                        + "WHERE f.nombre = ? AND r.partida = ?";
                PreparedStatement stmtId = connection.prepareStatement(queryId);
                stmtId.setString(1, feligresDetalle.getNombreCf());
                stmtId.setInt(2, feligresDetalle.getPartidaCf());
                ResultSet rs = stmtId.executeQuery();

                if (rs.next()) {
                    int idConfirmacion = rs.getInt("idConfirmacion");
                    int idFeligres = rs.getInt("idFeligres");
                    int idCelebrante = rs.getInt("celebrante_idCelebrante");

                    // 1. Eliminar registros asociados en las tablas secundarias
                    String deleteObservacion = "DELETE FROM observacion WHERE confirmacion_idConfirmacion = ?";
                    PreparedStatement stmtObs = connection.prepareStatement(deleteObservacion);
                    stmtObs.setInt(1, idConfirmacion);
                    stmtObs.executeUpdate();

                    // 2. Eliminar registros asociados en las tablas secundarias
                    String deleteRegistro = "DELETE FROM registrolibro WHERE confirmacion_idConfirmacion = ?";
                    PreparedStatement stmtReg = connection.prepareStatement(deleteRegistro);
                    stmtReg.setInt(1, idConfirmacion);
                    stmtReg.executeUpdate();

                    // 3. Eliminar el registro principal en la tabla confirmacion
                    String deleteConfirmacion = "DELETE FROM confirmacion WHERE idConfirmacion = ?";
                    PreparedStatement stmtDel = connection.prepareStatement(deleteConfirmacion);
                    stmtDel.setInt(1, idConfirmacion);
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
                    showAlert("Información", "El registro fue eliminado Satisfactoriamente.", "Se Elimino: " + feligresDetalle.getNombreCf(), Alert.AlertType.INFORMATION);
                    App.setRoot("confirmacion");

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

    private boolean comparacionC() {
        // Validacion que sea numero los valores ingresados
        Integer libroAsInteger;
        Integer folioAsInteger;
        Integer partidaAsInteger;
        Integer edadC;

        try {
            libroAsInteger = Integer.parseInt(txtLibroCf.getText());
            folioAsInteger = Integer.parseInt(txtFolioCf.getText());
            partidaAsInteger = Integer.parseInt(txtPartidaCf.getText());
            edadC = Integer.parseInt(txtEdadCf.getText());
        } catch (NumberFormatException e) {
            showAlert("Advertencia", "Algun Valor Ingresado es Invalido", "Libro, Folio y Partida deben ser Números", Alert.AlertType.ERROR);
            return false; // Si hay un error, termina el método aquí.
        }

        // Simplificación en el manejo de CheckBox
        inscrito = boxInscritoCf.isSelected() ? "Si" : "No";

        // Simplifica las comparaciones usando ||
        return !feligresDetalle.getLibroCf().equals(libroAsInteger)
                || !feligresDetalle.getFolioCf().equals(folioAsInteger)
                || !feligresDetalle.getPartidaCf().equals(partidaAsInteger)
                || !feligresDetalle.getFechaCf().equals(dpFechaSacCf.getValue())
                || !feligresDetalle.getLugarCf().equals(txtLugarCf.getText())
                || !feligresDetalle.getNombreCf().equals(txtNombreCf.getText())
                || !feligresDetalle.getApellidoCf().equals(txtApellidoCf.getText())
                || !feligresDetalle.getEdadCf().equals(edadC)
                || !feligresDetalle.getNacimientoCf().equals(dpNacimientoCf.getValue())
                || !feligresDetalle.getCelebranteCf().equals(txtCelebranteCf.getText())
                || !feligresDetalle.getPadreCf().equals(txtPadreCf.getText())
                || !feligresDetalle.getMadreCf().equals(txtMadreCf.getText())
                || !feligresDetalle.getPadrinosCf().equals(txtPadrino_MadrinaCf.getText())
                || !feligresDetalle.getObservacionesCf().equals(txtAreaCf.getText())
                || !feligresDetalle.getInscritoCf().equals(boxInscritoCf.isSelected() ? "Si" : "No");

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

    private static boolean safeEquals(Object a, Object b) {
        if (a == null) {
            return b == null; // Devuelve true si ambos son nulos, de lo contrario, devuelve false.
        }
        return a.equals(b); // Si 'a' no es nulo, compara con 'b' (que puede ser nulo).
    }
    
        @FXML
    public void _imprimirC() throws IOException, DocumentException {
        if (showConfirmationDialog("Confirmar Impresion", "Imprimir registro", "¿Estás seguro? Se Imprimira el registro de: " + feligresDetalle.getNombreCf())) {

            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/psher/Downloads/Constancia_C.pdf"));
            document.open();
            // Añadir un encabezado
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.RED);
            Font fontItalic = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 14, BaseColor.BLACK);
            Font fontItalicFecha = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 12, BaseColor.BLACK);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
            Font fontNormalTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font fontNormalRed = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.RED);

            ConsultaConfirmacion feligres = SingletonConfirmacion.getInstance().getFeligresDetalle();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy", new Locale("es", "ES"));

            String fechaSacramentoFormatted = feligres.getFechaCf().format(formatter);
            String fechaNacimientoFormatted = feligres.getNacimientoCf().format(formatter);
            String fechaActualFormatted = LocalDate.now().format(formatter);
            //Integracion de Logo Iglesia Pequeño
            Image logoP = Image.getInstance("C:/Users/psher/OneDrive/Documentos/Sacramentos/img/logo1.png"); //  ruta de  imagen
            logoP.setAbsolutePosition(460, 630); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoP.scaleToFit(110, 110); // Ancho y alto
            document.add(logoP);
            //Integracion del Logo Bautismo
            Image logoB = Image.getInstance("C:/Users/psher/OneDrive/Documentos/Sacramentos/img/espiritu-santo.png"); //  ruta de  imagen
            logoB.setAbsolutePosition(40, 630); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoB.scaleToFit(110, 110); // Ancho y alto
            document.add(logoB);
            // Integracion del Logo Grande Del Centro
            Image logoCentro = Image.getInstance("C:/Users/psher/OneDrive/Documentos/Sacramentos/img/logo1.png");
            logoCentro.setAbsolutePosition(110, 180);
            logoCentro.scaleToFit(400, 400);
// Configura la opacidad usando PdfGState
            PdfGState state = new PdfGState();
            state.setFillOpacity(0.15f);  // Ajusta este valor para cambiar la opacidad
// Agrega la imagen como marca de agua
            PdfContentByte under = writer.getDirectContentUnder();
            under.saveState();
            under.setGState(state);
            under.addImage(logoCentro);
            under.restoreState();

            Image linea = Image.getInstance("C:/Users/psher/OneDrive/Documentos/Sacramentos/img/Lline.png"); //  ruta de  imagen
            linea.setAbsolutePosition(35, 40); // Coordenadas x, y (desde la esquina inferior izquierda)
            linea.scaleToFit(550, 15); // Ancho y alto
            document.add(linea);

            //Integracion de Correo
            Image correo = Image.getInstance("C:/Users/psher/OneDrive/Documentos/Sacramentos/img/co.png"); //  ruta de  imagen
            correo.setAbsolutePosition(440, 5); // Coordenadas x, y (desde la esquina inferior izquierda)
            correo.scaleToFit(160, 50); // Ancho y alto
            document.add(correo);
            //Integracion de Whatsapp
            Image wha = Image.getInstance("C:/Users/psher/OneDrive/Documentos/Sacramentos/img/wa.png"); //  ruta de  imagen
            wha.setAbsolutePosition(20, 5); // Coordenadas x, y (desde la esquina inferior izquierda)
            wha.scaleToFit(160, 50); // Ancho y alto
            document.add(wha);
            //Integracion de Firma
            Image co = Image.getInstance("C:/Users/psher/OneDrive/Documentos/Sacramentos/img/firma.png"); //  ruta de  imagen
            co.setAbsolutePosition(170, 70); // Coordenadas x, y (desde la esquina inferior izquierda)
            co.scaleToFit(350, 350); // Ancho y alto
            document.add(co);

            Paragraph pHeader = new Paragraph("Parroquia Santo Hermano Pedro\nDiócesis De Sololá-Chimaltenango\n5ᵃ Avenida 4-104, Zona 1\nChimaltenango, Guatemala, C.A.\nTel: 7839-2709", fontHeader);
            pHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(pHeader);

            Paragraph pTitle = new Paragraph("CONSTANCIA DE CONFIRMACION", fontTitle);
            pTitle.setAlignment(Element.ALIGN_CENTER);
            pTitle.setSpacingBefore(20);
            document.add(pTitle);

            // Añadir detalles del bautismo
            Paragraph paragraph;

            paragraph = new Paragraph("\nEn esta parroquia el día :", fontNormalTitle);
            document.add(paragraph);
// Crear el Chunk con fechaSacramentoFormatted
            Chunk dateChunk = new Chunk(fechaSacramentoFormatted + ", a la edad de "+ feligres.getEdadCf()+" años.", fontItalic);
// Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            dateChunk.setUnderline(0.1f, -2f);
// Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(dateChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
// Añadir el Paragraph al documento
            document.add(paragraph);

            paragraph = new Paragraph("Por la imposición de manos del Obispo, "+ feligres.getCelebranteCf()+", recibio el Sacramento de la Confirmacion: ", fontNormalTitle);
            document.add(paragraph);
            // Crear el Chunk con el nombre y apellido
            Chunk nameChunk = new Chunk(feligres.getNombreCf() + " " + feligres.getApellidoCf(), fontItalic);
            // Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            nameChunk.setUnderline(0.1f, -2f);
            // Agregar el Chunk con el nombre y apellido a un Paragraph y centrarlo
            paragraph = new Paragraph(nameChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            paragraph = new Paragraph("Quien nació el: ", fontNormalTitle);
            document.add(paragraph);
// Crear el Chunk con fechaNacimientoFormatted
            Chunk birthDateChunk = new Chunk(fechaNacimientoFormatted, fontItalic);
// Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            birthDateChunk.setUnderline(0.1f, -2f);
// Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(birthDateChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
// Añadir el Paragraph al documento
            document.add(paragraph);

            paragraph = new Paragraph("Hijo(a) de: ", fontNormalTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
// Crear el Chunk con los nombres de los padres
            Chunk parentsChunk = new Chunk(feligres.getPadreCf() + " y " + feligres.getMadreCf(), fontItalic);
// Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            parentsChunk.setUnderline(0.1f, -2f);
// Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(parentsChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
// Añadir el Paragraph al documento
            document.add(paragraph);

            paragraph = new Paragraph("Padrino/Madrina: ", fontNormalTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            // Crear el Chunk con los nombres del padrino y la madrina
            Chunk godparentsChunk = new Chunk(feligres.getPadrinosCf(), fontItalic);
// Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            godparentsChunk.setUnderline(0.1f, -2f);
// Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(godparentsChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
// Añadir el Paragraph al documento
            document.add(paragraph);

            paragraph = new Paragraph("\nComo consta en el libro: " + feligres.getLibroCf() + ", folio " + feligres.getFolioCf() + ", partida " + feligres.getPartidaCf() + " de esta Parroquia.", fontNormal);
            document.add(paragraph);

            if (feligres.getObservacionesCf() != null && !feligres.getObservacionesCf().isEmpty()) {
                paragraph = new Paragraph("Observaciones: " + feligres.getObservacionesCf(), fontNormalRed);
                document.add(paragraph);
            }

            // Fecha y firma
            paragraph = new Paragraph("Chimaltenango, " + fechaActualFormatted, fontItalicFecha);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);

            // FIrma y Sello
            paragraph = new Paragraph("\n\n\n\n\n\n\nFirma del Párroco Y sello Parroquial", fontNormal);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            // Nombre del Parroco
            paragraph = new Paragraph("P. José Rolando Cúmez Tuyuc", fontItalic);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            writer.setPageEvent(new FooterEvent());//Asociamos el Evento que Genera la Hora Exacta de la Extencion del Documento

            try {

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                document.close();
            }
            try {
                File myFile = new File("C:/Users/psher/Downloads/Constancia_C.pdf");//Ruta Iglesia
                //File myFile = new File("C:/Users/walyn/Downloads/Constancia_C.pdf"); //Ruta Laptop
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(myFile);
                } else {
                    showAlert("Información", "No fue Posible abrir el Documento", "Constancia_C.pdf", Alert.AlertType.INFORMATION);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Información", "Se Cancelo la Impresion", "Cancelado por Usuario", Alert.AlertType.INFORMATION);
        }
    }

    static class FooterEvent extends PdfPageEventHelper {

        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            // Obtener la hora actual y formatearla
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
            String currentTime = now.format(formatter);

            // Añadir la hora al pie de página
            Phrase footerPhrase = new Phrase(currentTime, footerFont);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                    footerPhrase, (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 18, 0); // Puedes ajustar los valores de posición si lo necesitas
        }
    }

    
    
}
