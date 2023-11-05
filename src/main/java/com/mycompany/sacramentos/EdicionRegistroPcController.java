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
    private int edad;
            

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
                        LocalDate fechaInscripcion = dpFechaRealizadoPc.getValue();

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
                        App.setRoot("primeraComunion");
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
    private void _EliminarPc() throws IOException {
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
            libroAsInteger = Integer.valueOf(txtLibroPc.getText());
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

    @FXML
    public void _imprimirPc() throws IOException, DocumentException {
        if (showConfirmationDialog("Confirmar Impresion", "Imprimir registro", "¿Estás seguro? Se Imprimira el registro de: " + feligres.getNombreC())) {

            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/psher/Downloads/Constancia_Pc.pdf"));
            document.open();
            // Añadir un encabezado
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.RED);
            Font fontItalic = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 14, BaseColor.BLACK);
            Font fontItalicFecha = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 12, BaseColor.BLACK);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
            Font fontNormalTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font fontNormalRed = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.RED);
            
            ConsultaPrimeraComunion feligres = SingletonPrimeraComunion.getInstance().getFeligresDetalle();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy", new Locale("es", "ES"));

            String fechaSacramentoFormatted = feligres.getFechaRealizadoPc().format(formatter);
            String fechaNacimientoFormatted = feligres.getFechaNacPc().format(formatter);
            String fechaActualFormatted = LocalDate.now().format(formatter);
            //Integracion de Logo Iglesia Pequeño
            Image logoP = Image.getInstance("C:/Users/psher/OneDrive/Documentos/Sacramentos/img/logo1.png"); //  ruta de  imagen
            logoP.setAbsolutePosition(460, 630); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoP.scaleToFit(110, 110); // Ancho y alto
            document.add(logoP);
            //Integracion del Logo Bautismo
            Image logoB = Image.getInstance("C:/Users/psher/OneDrive/Documentos/Sacramentos/img/comunion.png"); //  ruta de  imagen
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
            //---------------------------------------------------Finaliza el Encabezado

            Paragraph pTitle = new Paragraph("CONSTANCIA DE PRIMERA COMUNION", fontTitle);
            pTitle.setAlignment(Element.ALIGN_CENTER);
            pTitle.setSpacingBefore(20);
            document.add(pTitle);

            // Añadir detalles del bautismo
            Paragraph paragraph;

            paragraph = new Paragraph("\nEn esta parroquia el día :", fontNormalTitle);
            document.add(paragraph);
            // Crear el Chunk con fechaSacramentoFormatted
            Chunk dateChunk = new Chunk(fechaSacramentoFormatted, fontItalic);
            // Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            dateChunk.setUnderline(0.1f, -2f);
            // Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(dateChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            // Añadir el Paragraph al documento
            document.add(paragraph);

            paragraph = new Paragraph("En la Celebracion solemne del Sacrificio Eucarístico, en esta parroquia, recibio el sacramento de la Primera Comunión: ", fontNormalTitle);
            document.add(paragraph);
            // Crear el Chunk con el nombre y apellido
            Chunk nameChunk = new Chunk(feligres.getNombreC() + " " + feligres.getApellidoC(), fontItalic);
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

            paragraph = new Paragraph("\nComo consta en el libro: " + feligres.getLibroC() + ", folio " + feligres.getFolioC() + ", partida " + feligres.getPartidaC() + " de esta Parroquia.", fontNormal);
            document.add(paragraph);

            if (feligres.getObservacionC() != null && !feligres.getObservacionC().isEmpty()) {
                paragraph = new Paragraph("Observaciones: " + feligres.getObservacionC(), fontNormalRed);
                document.add(paragraph);
            }
            //-----------------------------------------------------------------Pie de Pagina
            // Fecha y firma
            paragraph = new Paragraph("Chimaltenango, " + fechaActualFormatted, fontItalicFecha);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);

            // FIrma y Sello
            paragraph = new Paragraph("\n\n\n\n\n\n\n\n\n\nFirma del Párroco Y sello Parroquial", fontNormal);
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
                File myFile = new File("C:/Users/psher/Downloads/Constancia_Pc.pdf");
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(myFile);
                } else {
                    showAlert("Información", "No fue Posible abrir el Documento", "Constancia_Pc.pdf", Alert.AlertType.INFORMATION);
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
