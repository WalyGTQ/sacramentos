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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
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
import java.sql.PreparedStatement;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author walyn
 */
public class VistaBautismoAutorizacionImpresionController implements Initializable {

    DatosAutorizaciones datos = SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones();
    @FXML
    private TextField aparroquia, parroco, padre, madre, nino, Lnacimiento, padrino, madrina;
    @FXML
    private DatePicker Fnacimiento;
    @FXML
    private TextArea obs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializamos los campos con los datos obtenidos mediante el doble click guardado en el Singleton Datos Autorizacion
        //DatosAutorizaciones datos = SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones();
        aparroquia.setText(String.valueOf(datos.getParroquia()));
        parroco.setText(String.valueOf(datos.getParroco()));
        padre.setText(String.valueOf(datos.getPadre()));
        madre.setText(String.valueOf(datos.getMadre()));
        nino.setText(String.valueOf(datos.getNino()));
        Lnacimiento.setText(String.valueOf(datos.getLnacimiento()));
        Fnacimiento.setValue(LocalDate.parse(datos.getFnacimiento().toString()));
        padrino.setText(String.valueOf(datos.getPadrino()));
        madrina.setText(String.valueOf(datos.getMadrina()));
        obs.setText(String.valueOf(datos.getObs()));

        // TODO
    }

    //Metodo para regresar
    @FXML
    private void actualizar() throws IOException, DocumentException {
        if (comparacion()) {
            System.out.println("Con Cambios");
            if (showConfirmationDialog("Confirmar eliminación", "Actualizar registro", "¿Estás seguro? Deseas Modificar la Autorizacion de: " + datos.getNino())) {
                Connection connection = ConexionDB.getConexion();
                PreparedStatement pstmt = null;

                try {
                    String sql = "UPDATE autbautismo SET "
                            + "dirigida = ?, "
                            + "aparroco = ?, "
                            + "padre = ?, "
                            + "madre = ?, "
                            + "nino = ?, "
                            + "lugarNacimiento = ?, "
                            + "fechaNacimiento = ?, "
                            + "padrino = ?, "
                            + "madrina = ?, "
                            + "observacion = ?, "
                            + "actualizacion = NOW(), "
                            + "usuarioModificador = ? "
                            + "WHERE idaut = ?";

                    pstmt = connection.prepareStatement(sql);

                    // Aquí asumiendo que los valores se obtendrán de un singleton, debes asignar cada valor:
                    pstmt.setString(1, aparroquia.getText());
                    pstmt.setString(2, parroco.getText());
                    pstmt.setString(3, padre.getText());
                    pstmt.setString(4, madre.getText());
                    pstmt.setString(5, nino.getText());
                    pstmt.setString(6, Lnacimiento.getText());
                    pstmt.setDate(7, java.sql.Date.valueOf(Fnacimiento.getValue()));
                    pstmt.setString(8, padrino.getText());
                    pstmt.setString(9, madrina.getText());
                    pstmt.setString(10, obs.getText());
                    pstmt.setString(11, SingletonDatosUsuario.getInstance().getDatosUsuario().getNombre());
                    pstmt.setInt(12, SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones().getId());

                    pstmt.executeUpdate();
                    datos.setParroquia(aparroquia.getText());
                    datos.setParroco(parroco.getText());
                    datos.setPadre(padre.getText());
                    datos.setMadre(madre.getText());
                    datos.setNino(nino.getText());
                    datos.setLnacimiento(Lnacimiento.getText());
                    datos.setFnacimiento(Fnacimiento.getValue());
                    datos.setPadrino(padrino.getText());
                    datos.setMadrina(madrina.getText());
                    datos.setObs(obs.getText());

                    showAlert("Información", "Actualización exitosa", "El registro ha sido modificado exitosamente.", Alert.AlertType.INFORMATION);
                    mostrarAlerta();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Error al actualizar", "Hubo un problema al intentar actualizar el registro.", Alert.AlertType.ERROR);
                } finally {
                    if (pstmt != null) {
                        try {
                            pstmt.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Redirigir al tab y vista deseados después de la actualización
                int tabIndexToSelect = 1;
                EstadoPane.getInstance().setSelectedTabIndex(tabIndexToSelect);
                App.setRoot("vistaBautismoAutorizacion");

            } else {
                // El usuario ha cancelado la Actualizacion
                showAlert("Información", "El Usuario ha Cancelado la Operación", "Ninguna Modificación Realizada", Alert.AlertType.INFORMATION);
                // Redirigir al tab y vista deseados
                int tabIndexToSelect = 1;
                EstadoPane.getInstance().setSelectedTabIndex(tabIndexToSelect);
                App.setRoot("vistaBautismoAutorizacion");
            }

        } else {
            System.out.println("Sin Cambios");
            showAlert("Información", "Ningún Cambio Realizado", "Ningún Campo fue Editado", Alert.AlertType.INFORMATION);
            // Redirigir al tab y vista deseados
        }
    }

    private boolean comparacion() {
        // Define un arreglo de booleanos con todas las condiciones
        boolean[] conditions = {
            !datos.getParroquia().equals(aparroquia.getText()),
            !datos.getParroco().equals(parroco.getText()),
            !datos.getPadre().equals(padre.getText()),
            !datos.getMadre().equals(madre.getText()),
            !datos.getNino().equals(nino.getText()),
            !datos.getLnacimiento().equals(Lnacimiento.getText()),
            !datos.getFnacimiento().equals(Fnacimiento.getValue()),
            !datos.getPadrino().equals(padrino.getText()),
            !datos.getMadrina().equals(madrina.getText()),
            !datos.getObs().equals(obs.getText()),};

        // Itera sobre el arreglo y retorna true si alguna condición se cumple
        for (boolean condition : conditions) {
            if (condition) {
                return true;
            }
        }
        return false;
    }

    //Metodo para regresar
    @FXML
    private void regresar() throws IOException {
        int tabIndexToSelect = 1;
        EstadoPane.getInstance().setSelectedTabIndex(tabIndexToSelect);
        App.setRoot("vistaBautismoAutorizacion");

    }

    @FXML
    private void mostrarAlerta() throws IOException, DocumentException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Registro Exitoso");
        alert.setHeaderText("La Autorización fue generada con éxito.");
        alert.setContentText("¿Desea imprimir la autorización?");

        ButtonType buttonTypeImprimir = new ButtonType("Imprimir");
        ButtonType buttonTypeNoImprimir = new ButtonType("No Imprimir");

        alert.getButtonTypes().setAll(buttonTypeImprimir, buttonTypeNoImprimir);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeImprimir) {
            // Código para manejar la impresión
            System.out.println("El usuario eligió imprimir.");
            // Aquí puedes llamar a tu método de impresión
            imprimirAutorizacion();
        } else if (result.get() == buttonTypeNoImprimir) {

            // Código para manejar la decisión de no imprimir
            System.out.println("El usuario eligió no imprimir.");
            // Aquí puedes redirigir a otra vista
            // App.setRoot("otraVista");
        }
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

    //Impresion de Documento:
    @FXML
    public void imprimirAutorizacion() throws IOException, DocumentException {
        // Registrar fuente personalizada
        FontFactory.register("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/fuentes/Cambria.ttf", "CambriaItalic");
        // Usar la fuente personalizada
        Font CambriaIta = FontFactory.getFont("CambriaItalic", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.ITALIC, BaseColor.BLACK);
        Font CambriaItaBold = FontFactory.getFont("CambriaItalic", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 13, Font.BOLDITALIC, BaseColor.BLACK);
        Font Cambria = FontFactory.getFont("CambriaItalic", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.NORMAL, BaseColor.BLACK);
        Font Cambria14Normal = FontFactory.getFont("CambriaItalic", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.BOLD, BaseColor.BLACK);

        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/walyn/Downloads/Constancia_C.pdf"));
        document.open();
        // Añadir un encabezado
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.RED);
        Font fontItalic = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 14, BaseColor.BLACK);
        Font fontItalicFecha = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 12, BaseColor.BLACK);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
        Font fontNormalTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        Font fontNormalRed = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.RED);

        datos = SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy", new Locale("es", "ES"));

        String fechaNacimientoFormatted = datos.getFnacimiento().format(formatter);
        String fechaActualFormatted = LocalDate.now().format(formatter);
        //Integracion de Logo Iglesia Pequeño
        Image logoP = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/logo1.png"); //  ruta de  imagen
        logoP.setAbsolutePosition(500, 670); // Coordenadas x, y (desde la esquina inferior izquierda)
        logoP.scaleToFit(80, 80); // Ancho y alto
        document.add(logoP);
        //Integracion del Autorizacion
        Image logoB = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/bautismo.png"); //  ruta de  imagen
        logoB.setAbsolutePosition(40, 680); // Coordenadas x, y (desde la esquina inferior izquierda)
        logoB.scaleToFit(70, 70); // Ancho y alto
        document.add(logoB);

        // Integracion del Logo Grande Del Centro
        Image logoCentro = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/logo1.png");
        logoCentro.setAbsolutePosition(110, 180);
        logoCentro.scaleToFit(400, 400);
        // Configura la opacidad usando PdfGState
        PdfGState state = new PdfGState();
        state.setFillOpacity(0.10f);  // Ajusta este valor para cambiar la opacidad
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
        //Integracion de la imagen  Whatsapp
        Image wha = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/wa.png"); //  ruta de  imagen
        wha.setAbsolutePosition(20, 5); // Coordenadas x, y (desde la esquina inferior izquierda)
        wha.scaleToFit(160, 50); // Ancho y alto
        document.add(wha);

        //Encabezado
        Paragraph pHeader = new Paragraph("Parroquia Santo Hermano Pedro\nDiócesis De Sololá-Chimaltenango\n5ᵃ Avenida 4-104, Zona 1\nChimaltenango, Guatemala, C.A.\nTel: 7839-2709", fontHeader);
        pHeader.setAlignment(Element.ALIGN_CENTER);
        document.add(pHeader);
        //Titulo del DOcumento
        Paragraph pTitle = new Paragraph("AUTORIZACION DE BAUTISMNO", fontTitle);
        pTitle.setAlignment(Element.ALIGN_CENTER);
        pTitle.setSpacingBefore(20);
        document.add(pTitle);

        // Añadir detalles de la Autorizacion
        Phrase phrase = new Phrase(); ///Phrase 1
        phrase.add(new Chunk("\nEstimado:  ", fontNormalTitle));
        phrase.add(new Chunk("Rdo. Padre, " + SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones().getParroco(), CambriaIta));
        phrase.add(new Chunk("\nParroco de:  ", fontNormalTitle));
        phrase.add(new Chunk(SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones().getParroquia(), CambriaIta));
        phrase.add(new Chunk("\n\nPor medio de la presente se concede ", Cambria));
        phrase.add(new Chunk(" AUTORIZACIÓN ", Cambria14Normal));
        phrase.add(new Chunk(" a: ", Cambria));
        document.add(phrase); /// Final Phrase 1

        Paragraph padres = new Paragraph(SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones().getPadre() + " " + "  Y  " + SingletonDatosAutorizaciones.getInstance().getDatosAutorizaciones().getMadre(), CambriaItaBold);
        padres.setAlignment(Element.ALIGN_CENTER);
        padres.setSpacingBefore(20);
        document.add(padres);

        Phrase phrase1 = new Phrase(); ///Phrase 2
        phrase1.add(new Chunk("\nFeligreses de esta Parroquia.", Cambria));
        phrase1.add(new Chunk("\nPara que puedan ", Cambria));
        phrase1.add(new Chunk(" BAUTIZAR ", Cambria14Normal));
        phrase1.add(new Chunk(" a su hijo(a): ", Cambria));
        // Añadir el Phrase al documento
        document.add(phrase1); /// Final Phrase 2

        writer.setPageEvent(new EdicionRegistroCController.FooterEvent());//Asociamos el Evento que Genera la Hora Exacta de la Extencion del Documento

        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        try {
            File myFile = new File("C:/Users/walyn/Downloads/Constancia_C.pdf");//Ruta Iglesia
            //File myFile = new File("C:/Users/walyn/Downloads/Constancia_C.pdf"); //Ruta Laptop
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(myFile);
            } else {
                showAlert("Información", "No fue Posible abrir el Documento", "Constancia_C.pdf", Alert.AlertType.INFORMATION);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
