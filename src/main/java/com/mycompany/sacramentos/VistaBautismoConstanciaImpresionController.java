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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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
public class VistaBautismoConstanciaImpresionController implements Initializable {
    
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
    private int edad;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

                //Al Inicializar la vista se muestran los datos que previamente se guardaron
        FeligresDetalle feligres = SingletonData1.getInstance().getFeligresDetalle();
        txtLibroB.setText(String.valueOf(feligres.getLibro()));
        txtFolioB.setText(String.valueOf(feligres.getFolio()));
        txtPartidaB.setText(String.valueOf(feligres.getPartida()));
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
        App.setRoot("vistaSacramentos");
    }
        //Para mostrar alertas mas facilmente
    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
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
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/walyn/Downloads/Constancia_Bautismo.pdf"));
            document.open();
            // Añadir un encabezado
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.RED);
            Font fontItalic = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 14, BaseColor.BLACK);
            Font fontItalicFecha = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 12, BaseColor.BLACK);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
            Font fontNormalTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font fontNormalRed = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.RED);

            FeligresDetalle feligres = SingletonData1.getInstance().getFeligresDetalle();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy", new Locale("es", "ES"));

            String fechaSacramentoFormatted = feligres.getFechaSacramento().format(formatter);
            String fechaNacimientoFormatted = feligres.getNacimiento().format(formatter);
            String fechaActualFormatted = LocalDate.now().format(formatter);
            //Integracion de Logo Iglesia Pequeño
            Image logoP = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/logo1.png"); //  ruta de  imagen
            logoP.setAbsolutePosition(460, 630); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoP.scaleToFit(110, 110); // Ancho y alto
            document.add(logoP);
            //Integracion del Logo Bautismo
            Image logoB = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/bautismoC.png"); //  ruta de  imagen
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

            Paragraph pTitle = new Paragraph("CONSTANCIA DE BAUTIZO", fontTitle);
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

            paragraph = new Paragraph("Fue bautizado(a) solemnemente: ", fontNormalTitle);
            document.add(paragraph);
            // Crear el Chunk con el nombre y apellido
            Chunk nameChunk = new Chunk(feligres.getNombre() + " " + feligres.getApellido(), fontItalic);
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
            Chunk parentsChunk = new Chunk(feligres.getPadre() + " y " + feligres.getMadre(), fontItalic);
// Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            parentsChunk.setUnderline(0.1f, -2f);
// Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(parentsChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
// Añadir el Paragraph al documento
            document.add(paragraph);

            paragraph = new Paragraph("Fueron sus padrinos: ", fontNormalTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            // Crear el Chunk con los nombres del padrino y la madrina
            Chunk godparentsChunk = new Chunk(feligres.getPadrino() + " y " + feligres.getMadrina(), fontItalic);
// Establecer el subrayado (puedes ajustar los parámetros para cambiar el aspecto del subrayado)
            godparentsChunk.setUnderline(0.1f, -2f);
// Agregar el Chunk a un Paragraph y centrarlo
            paragraph = new Paragraph(godparentsChunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);
// Añadir el Paragraph al documento
            document.add(paragraph);

            paragraph = new Paragraph("\nComo consta en el libro: " + feligres.getLibro() + ", folio " + feligres.getFolio() + ", partida " + feligres.getPartida() + " de esta Parroquia.", fontNormal);
            document.add(paragraph);

            if (feligres.getObservacion() != null && !feligres.getObservacion().isEmpty()) {
                paragraph = new Paragraph("Observaciones: " + feligres.getObservacion(), fontNormalRed);
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

    @FXML///Este Metodo no se utiliza mas, pero se deja por si es necesario cuestionar antes de Imprimir
    public void mostrarDialogoSeleccion() {
        // Crea un Alert con opciones
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Seleccionar Documento");
        alert.setHeaderText("¿Qué documento deseas imprimir?");

        // Crear botones personalizados
        ButtonType botonConstancia = new ButtonType("Constancia");
        ButtonType botonFicha = new ButtonType("Ficha de Inscripción");
        ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Añadir botones al diálogo
        alert.getButtonTypes().setAll(botonConstancia, botonFicha, botonCancelar);

        // Mostrar el diálogo y esperar a que el usuario responda
        Optional<ButtonType> resultado = alert.showAndWait();

        // Manejar la respuesta
        resultado.ifPresent(buttonType -> {
            if (buttonType == botonConstancia) {
                try {
                    _imprimirB(); // Método existente para imprimir la constancia
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }
            } else if (buttonType == botonFicha) {
                try {
                    _imprimirFicha(); // Método para imprimir la ficha de inscripción (debes implementarlo)
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }
            }
            // Si se selecciona "Cancelar", no se hace nada
        });
    }
    @FXML
    public void _imprimirFicha() throws IOException, DocumentException {
        // Implementa la lógica para imprimir la ficha de inscripción
        if (showConfirmationDialog("Confirmar Impresion", "Imprimir FICHA", "¿Estás seguro? Se Imprimira FICHA DE: " + datosFeligres.getNombre())) {

            Document document = new Document(PageSize.LETTER);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/walyn/Downloads/Ficha_Bautismo.pdf"));
            document.open();
            // Añadir un encabezado
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font fontItalic = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 12, BaseColor.RED);
            Font fontItalicN = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 12, BaseColor.BLACK);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font fontNormalTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font fontNormalRed = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.RED);
            Font fontNormalRedOBS = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.RED);

            FeligresDetalle feligres = SingletonData1.getInstance().getFeligresDetalle();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy", new Locale("es", "ES"));

            String fechaSacramentoFormatted = feligres.getFechaSacramento().format(formatter);
            String fechaNacimientoFormatted = feligres.getNacimiento().format(formatter);
            String fechaActualFormatted = LocalDate.now().format(formatter);
            //Parte Oficina
            //Integracion de Logo Iglesia Pequeño
            Image logoP = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/logo1.png"); //  ruta de  imagen
            logoP.setAbsolutePosition(460, 680); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoP.scaleToFit(80, 80); // Ancho y alto
            document.add(logoP);
            //Integracion del Logo Bautismo
            Image logoB = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/bautismoC.png"); //  ruta de  imagen
            logoB.setAbsolutePosition(40, 680); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoB.scaleToFit(80, 80); // Ancho y alto
            document.add(logoB);
            //Parte feligres
            //Integracion de Logo Iglesia Pequeño
            Image logoP2 = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/logo1.png"); //  ruta de  imagen
            logoP2.setAbsolutePosition(460, 340); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoP2.scaleToFit(80, 80); // Ancho y alto
            document.add(logoP2);
            //Integracion del Logo Bautismo
            Image logoB2 = Image.getInstance("C:/Users/walyn/OneDrive/Documentos/NetBeansProjects/Sacramentos/src/main/resources/img/bautismoC.png"); //  ruta de  imagen
            logoB2.setAbsolutePosition(40, 340); // Coordenadas x, y (desde la esquina inferior izquierda)
            logoB2.scaleToFit(80, 80); // Ancho y alto
            document.add(logoB2);

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
            //Primera Parte Oficina
            Paragraph pHeader = new Paragraph("Parroquia Santo Hermano Pedro\nDiócesis De Sololá-Chimaltenango\n5ᵃ Avenida 4-104, Zona 1\nChimaltenango, Guatemala, C.A.\nTel: 7839-2709", fontHeader);
            pHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(pHeader);

            Paragraph pTitle = new Paragraph("Ficha de Inscripcion de Bautismo", fontNormalRed);
            pTitle.setAlignment(Element.ALIGN_CENTER);
            pTitle.setSpacingBefore(10);
            document.add(pTitle);

            // Añadir detalles del bautismo
            Paragraph paragraph;

            paragraph = new Paragraph("\nNombre del Niño(a):  " + feligres.getNombre() + " " + feligres.getApellido(), fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Fecha de Nacimiento:  " + fechaNacimientoFormatted, fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Lugar de Nacimiento:  " + feligres.getLugarNacimiento(), fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Nombre de los Padres:  " + feligres.getPadre() + "  Y  " + feligres.getMadre(), fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Padrinos:  " + feligres.getPadrino() + "  Y  " + feligres.getMadrina(), fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Fecha de Inscripcion:  " + fechaActualFormatted, fontItalicN);
            document.add(paragraph);
            if (feligres.getObservacion() != null && !feligres.getObservacion().isEmpty()) {
                paragraph = new Paragraph("Observaciones: " + feligres.getObservacion(), fontNormalRedOBS);
                document.add(paragraph);
            }
            paragraph = new Paragraph("Fecha del Sacramento", fontNormal);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            paragraph = new Paragraph(fechaSacramentoFormatted, fontItalic);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            paragraph = new Paragraph("\n-----------------------------------------------------------------------------------------------------------------------------------\n\n", fontItalicN);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            //Segunda Parte Feligres:
            Paragraph pHeader2 = new Paragraph("Parroquia Santo Hermano Pedro\nDiócesis De Sololá-Chimaltenango\n5ᵃ Avenida 4-104, Zona 1\nChimaltenango, Guatemala, C.A.\nTel: 7839-2709", fontHeader);
            pHeader2.setAlignment(Element.ALIGN_CENTER);
            document.add(pHeader2);

            Paragraph pTitle2 = new Paragraph("Ficha de Inscripcion de Bautismo", fontNormalRed);
            pTitle2.setAlignment(Element.ALIGN_CENTER);
            pTitle2.setSpacingBefore(10);
            document.add(pTitle2);

            paragraph = new Paragraph("\nNombre del Niño(a):  " + feligres.getNombre() + " " + feligres.getApellido(), fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Fecha de Nacimiento:  " + fechaNacimientoFormatted, fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Lugar de Nacimiento:  " + feligres.getLugarNacimiento(), fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Nombre de los Padres:  " + feligres.getPadre() + "  Y  " + feligres.getMadre(), fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Padrinos:  " + feligres.getPadrino() + "  Y  " + feligres.getMadrina(), fontNormal);
            document.add(paragraph);
            paragraph = new Paragraph("Fecha de Inscripcion:  " + fechaActualFormatted, fontItalicN);
            document.add(paragraph);
            if (feligres.getObservacion() != null && !feligres.getObservacion().isEmpty()) {
                paragraph = new Paragraph("Observaciones: " + feligres.getObservacion(), fontNormalRedOBS);
                document.add(paragraph);
            }
            paragraph = new Paragraph("Fecha del Sacramento", fontNormal);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            paragraph = new Paragraph(fechaSacramentoFormatted, fontItalic);
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
                File myFile = new File("C:/Users/walyn/Downloads/Ficha_Bautismo.pdf");
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
    @FXML
    private void regresar() throws IOException {
        App.setRoot("vistaBautismoConstancia");
    }
    
    
}
