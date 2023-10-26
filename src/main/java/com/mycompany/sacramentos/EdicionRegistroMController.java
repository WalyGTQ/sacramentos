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
    
     @FXML
    public void _imprimirM() throws IOException, DocumentException {
        if (showConfirmationDialog("Confirmar Impresion", "Imprimir registro", "¿Estás seguro? Se Imprimira el registro de: " + feligresDetalle.getNombreMM()+" Y "+feligresDetalle.getNombreFM())) {

            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/walyn/Downloads/Constancia_Matrimonio.pdf"));
            document.open();
            // Añadir un encabezado
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.RED);
            Font fontItalic = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 12, BaseColor.BLACK);
            Font fontItalicFecha = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 12, BaseColor.BLACK);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
            Font fontNormalTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font fontNormalRed = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.RED);

            ConsultaMatrimonio feligres = SingletonMatrimonio.getInstance().getFeligresDetalle();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy", new Locale("es", "ES"));

            String fechaSacramentoFormatted = feligres.getFechaM().format(formatter);
            String fechaActualFormatted = LocalDate.now().format(formatter);
            //Integracion de Logo Iglesia Pequeño
            Image logoP = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/logo1.png"); //  ruta de  imagen
            logoP.setAbsolutePosition(460, 630); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoP.scaleToFit(110, 110); // Ancho y alto
            document.add(logoP);
            //Integracion del Logo Bautismo
            Image logoB = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/casado.png"); //  ruta de  imagen
            logoB.setAbsolutePosition(40, 630); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoB.scaleToFit(110, 110); // Ancho y alto
            document.add(logoB);
            // Integracion del Logo Grande Del Centro
            Image logoCentro = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/logo1.png");
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

            Image linea = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/Lline.png"); //  ruta de  imagen
            linea.setAbsolutePosition(35, 40); // Coordenadas x, y (desde la esquina inferior izquierda)
            linea.scaleToFit(550, 15); // Ancho y alto
            document.add(linea);

            //Integracion de Correo
            Image correo = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/co.png"); //  ruta de  imagen
            correo.setAbsolutePosition(440, 5); // Coordenadas x, y (desde la esquina inferior izquierda)
            correo.scaleToFit(160, 50); // Ancho y alto
            document.add(correo);
            //Integracion de Whatsapp
            Image wha = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/wa.png"); //  ruta de  imagen
            wha.setAbsolutePosition(20, 5); // Coordenadas x, y (desde la esquina inferior izquierda)
            wha.scaleToFit(160, 50); // Ancho y alto
            document.add(wha);
            //Integracion de Firma
            Image co = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/firma.png"); //  ruta de  imagen
            co.setAbsolutePosition(170, 70); // Coordenadas x, y (desde la esquina inferior izquierda)
            co.scaleToFit(350, 350); // Ancho y alto
            document.add(co);

            Paragraph pHeader = new Paragraph("Parroquia Santo Hermano Pedro\nDiócesis De Sololá-Chimaltenango\n5ᵃ Avenida 4-104, Zona 1\nChimaltenango, Guatemala, C.A.\nTel: 7839-2709", fontHeader);
            pHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(pHeader);

            Paragraph pTitle = new Paragraph("CONSTANCIA DE MATRIMONIO", fontTitle);
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

            paragraph = new Paragraph("Previo a los trámites de Derecho y no habiendo impedimiento Canónico, ante la precencia de los testigos: ", fontNormalTitle);
            document.add(paragraph);
            // Crear el Chunk con los testigos
            Chunk testigosChunk = new Chunk(feligres.getTestigoUnoM() + " " + feligres.getTestigoDosM(), fontItalic);
            // Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            testigosChunk.setUnderline(0.1f, -2f);
            // Agregar el Chunk con el nombre y apellido a un Paragraph y centrarlo
            paragraph = new Paragraph(testigosChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            
            paragraph = new Paragraph("Se bendijo el matrimonio de: ", fontItalic);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            
            
            // Crear el Chunk con el nombre y apellido
            Chunk elChunk = new Chunk(feligres.getNombreMM() + " " + feligres.getApellidoMM()+ ", de "+feligres.getEdadMM()+" años de edad.", fontItalic);
            // Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            elChunk.setUnderline(0.1f, -2f);
            // Agregar el Chunk con el nombre y apellido a un Paragraph y centrarlo
            paragraph = new Paragraph(elChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            
            paragraph = new Paragraph("Hijo de: ", fontNormalTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            // Crear el Chunk con los nombres de los padres
            Chunk parentselChunk = new Chunk(feligres.getPadreMM() + " y " + feligres.getMadreMM(), fontItalic);
            // Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            parentselChunk.setUnderline(0.1f, -2f);
            // Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(parentselChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            // Añadir el Paragraph al documento
            document.add(paragraph);
            
            paragraph = new Paragraph("Con la Señorita: ", fontNormalTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            
                        // Crear el Chunk con el nombre y apellido
            Chunk ellChunk = new Chunk(feligres.getNombreFM() + " " + feligres.getApellidoFM()+ ", de "+feligres.getEdadFM()+" años de edad.", fontItalic);
            // Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            ellChunk.setUnderline(0.1f, -2f);
            // Agregar el Chunk con el nombre y apellido a un Paragraph y centrarlo
            paragraph = new Paragraph(ellChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            
            paragraph = new Paragraph("Hijo de: ", fontNormalTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            // Crear el Chunk con los nombres de los padres
            Chunk parentsellChunk = new Chunk(feligres.getPadreFM() + " y " + feligres.getMadreFM(), fontItalic);
            // Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            parentsellChunk.setUnderline(0.1f, -2f);
            // Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(parentsellChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            // Añadir el Paragraph al documento
            document.add(paragraph);


            paragraph = new Paragraph("Bendijo el Matrimonio: ", fontNormalTitle);
            document.add(paragraph);
            // Crear el Chunk con los nombres del padrino y la madrina
            Chunk godparentsChunk = new Chunk(feligres.getCelebranteM(), fontItalic);
// Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            godparentsChunk.setUnderline(0.1f, -2f);
// Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(godparentsChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
// Añadir el Paragraph al documento
            document.add(paragraph);

            paragraph = new Paragraph("\nComo consta en el libro: " + feligres.getLibroM() + ", folio " + feligres.getFolioM() + ", partida " + feligres.getPartidaM() + " de esta Parroquia.", fontNormal);
            document.add(paragraph);

            if (feligres.getObservacionM() != null && !feligres.getObservacionM().isEmpty()) {
                paragraph = new Paragraph("Observaciones: " + feligres.getObservacionM(), fontNormalRed);
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
                File myFile = new File("C:/Users/walyn/Downloads/Constancia_Matrimonio.pdf");
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(myFile);
                } else {
                    showAlert("Información", "No fue Posible abrir el Documento", "Constancia_Matrimonio.pdf", Alert.AlertType.INFORMATION);
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
