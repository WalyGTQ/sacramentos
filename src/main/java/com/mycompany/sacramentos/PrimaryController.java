package com.mycompany.sacramentos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController {

    private String busqueda;

    //Campos para la consulta de Bautismo
    @FXML
    private TableView<FeligresDetalle> tablaFeligreses;
    @FXML
    private TableColumn<FeligresDetalle, String> nombreColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> apellidoColumn;
    @FXML
    private TableColumn<FeligresDetalle, Integer> libroColumn;
    @FXML
    private TableColumn<FeligresDetalle, Integer> folioColumn;
    @FXML
    private TableColumn<FeligresDetalle, Integer> partidaColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> padreColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> madreColumn;
    @FXML
    private TableColumn<FeligresDetalle, LocalDate> nacimientoColumn;
    @FXML
    private TableColumn<FeligresDetalle, Integer> edadColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> lugarNacimientoColumn;
    @FXML
    private TableColumn<FeligresDetalle, LocalDate> fechaSacramentoColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> lugarSacramentoColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> padrinoColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> madrinaColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> observacionColumn;
    @FXML
    private TableColumn<FeligresDetalle, String> registradoColumn;
    //Fin de Campos para la Consulta Bautismo
    //Inicio Campor Para Consulta de Primera Comunion
    @FXML
    private TableView<ConsultaPrimeraComunion> tvComunion;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, Integer> tcLibroC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, Integer> tcFolioC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, Integer> tcPartidaC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, Integer> tcEdadC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, LocalDate> tcFechaC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, LocalDate> tcNacimientoC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, LocalDate> tcRegistroC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcNombreC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcApellidoC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcLugarC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcCelebranteC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcAnotadoC;
    @FXML
    private TableColumn<ConsultaPrimeraComunion, String> tcObservacionC;
    //Fin Campos Para Consulta de Primera Comunion

    //Inicio Campor Para Consulta de Confirmacion
    @FXML
    private TableView<ConsultaConfirmacion> tvConfirmacion;
    @FXML
    private TableColumn<ConsultaConfirmacion, Integer> tcLibroCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, Integer> tcFolioCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, Integer> tcPartidaCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcNombreCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcApellidoCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, LocalDate> tcNacimientoCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, Integer> tcEdadCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcCelebranteCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, LocalDate> tcFechaCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcLugarCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcPadreCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcMadreCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcPadrinosCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcInscritoCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, String> tcObservacionesCf;
    @FXML
    private TableColumn<ConsultaConfirmacion, LocalDate> tcRegistroCf;
    //Fin Campos Para Consulta de Confirmacion

    //Inicio Campor Para Consulta de Matrimonios
    @FXML
    private TableView<ConsultaMatrimonio> tvMatrimonio;
    @FXML
    private TableColumn<ConsultaMatrimonio, Integer> tcLibroM;
    @FXML
    private TableColumn<ConsultaMatrimonio, Integer> tcFolioM;
    @FXML
    private TableColumn<ConsultaMatrimonio, Integer> tcPartidaM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcLugarM;
    @FXML
    private TableColumn<ConsultaMatrimonio, LocalDate> tcFechaM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcTestigoUnoM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcTestigoDosM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcNombreMM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcApellidoMM;
    @FXML
    private TableColumn<ConsultaMatrimonio, Integer> tcEdadMM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcOrigenMM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcFeligresMM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcPadreMM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcMadreMM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcNOmbreFM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcApellidoFM;
    @FXML
    private TableColumn<ConsultaMatrimonio, Integer> tcEdadFM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcOrigenFM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcFeligresFM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcPadreFM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcMadreFM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcCelebranteM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcInscritoM;
    @FXML
    private TableColumn<ConsultaMatrimonio, String> tcObservacionM;
    @FXML
    private TableColumn<ConsultaMatrimonio, LocalDate> tcRegistroM;
    //Fin Campos Para Consulta de Matrimonios

    @FXML
    private TextField txtBusquedaRapida;

    @FXML
    private void _bautismos() throws IOException {
        App.setRoot("bautismosVista");
    }

    @FXML
    private void _primeraComunion() throws IOException {
        App.setRoot("primeraComunion");
    }

    @FXML
    private void _confirmacion() throws IOException {
        App.setRoot("confirmacion");
    }

    @FXML
    private void _matrimonio() throws IOException {
        App.setRoot("matrimonios");
    }

    @FXML
    private void _reportes() throws IOException {
        App.setRoot("reportes");
    }

    @FXML
    private void _salir() throws IOException {
        App.setRoot("_login");
    }

    @FXML
    private void _configuracion() throws IOException {
        App.setRoot("configuracion");
    }

    @FXML
    private void _buscar() throws IOException {
        tablaFeligreses.setRowFactory(tv -> {
            TableRow<FeligresDetalle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    FeligresDetalle rowData = row.getItem();
                    SingletonData1.getInstance().setFeligresDetalle(rowData); // Guarda los datos en el Singleton
                    try {
                        App.setRoot("edicionRegistro");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        // Validación para verificar si el TextField está vacío
        busqueda = txtBusquedaRapida.getText();
        if (busqueda.trim().isEmpty()) {
            showAlert("Error", "El campo de búsqueda no puede estar vacío.", Alert.AlertType.ERROR);
            return;
        }

        _consultaBautismo();
        _consultaComunion();
        _consultaConfirmacion();
        _consultaMatrimonios();

    }

    private void _consultaBautismo() throws IOException {
        libroColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("libro"));
        folioColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("folio"));
        partidaColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("partida"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("nombre"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("apellido"));
        padreColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("padre"));
        madreColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("madre"));
        nacimientoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, LocalDate>("nacimiento"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, Integer>("edad"));
        lugarNacimientoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("lugarNacimiento"));
        fechaSacramentoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, LocalDate>("fechaSacramento"));
        lugarSacramentoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("lugarSacramento"));
        padrinoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("padrino"));
        madrinaColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("madrina"));
        observacionColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("observacion"));
        registradoColumn.setCellValueFactory(new PropertyValueFactory<FeligresDetalle, String>("registrado"));
        Connection connection = ConexionDB.getConexion();
        ObservableList<FeligresDetalle> data = FXCollections.observableArrayList();

        try {
            String query = "SELECT r.libro, r.folio, r.partida, "
                    + "f.nombre, f.apellido, f.padreFeligres, f.madreFeligres, f.nacimiento, f.edadFeligres, f.lugarNacimiento, "
                    + "b.fechaSacramento, b.lugarSacramento, b.padrino, b.madrina, "
                    + "o.observacion, r.inscritoLibro  "
                    + "FROM feligres f "
                    + "JOIN bautismo b ON f.idFeligres = b.idFeligres "
                    + "JOIN registrolibro r ON b.idBautismo = r.bautismo_idBautismo "
                    + "JOIN observacion o ON b.idBautismo = o.bautismo_idBautismo "
                    + "WHERE nombre LIKE ? OR apellido LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + busqueda + "%");
            stmt.setString(2, "%" + busqueda + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data.add(new FeligresDetalle(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("padreFeligres"),
                        rs.getString("madreFeligres"),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getInt("edadFeligres"),
                        rs.getString("lugarNacimiento"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getString("lugarSacramento"),
                        rs.getString("padrino"),
                        rs.getString("madrina"),
                        rs.getString("observacion"),
                        rs.getString("inscritoLibro")
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data.isEmpty()) {
                showAlert("Información", "Bautismos, No se encontraron resultados para: " + busqueda, Alert.AlertType.INFORMATION);
                tablaFeligreses.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                return;
            }

            tablaFeligreses.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Hubo un error al realizar la búsqueda.", Alert.AlertType.ERROR);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }//Fin Consulta Bautismo
    
    private void _consultaComunion() throws IOException {
        // Configuración de las columnas usando PropertyValueFactory
        tcLibroC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("libroC"));
        tcFolioC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("folioC"));
        tcPartidaC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("partidaC"));
        tcEdadC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, Integer>("edadC"));
        tcFechaC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, LocalDate>("fechaRealizadoPc"));
        tcNacimientoC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, LocalDate>("fechaNacPc"));
        tcRegistroC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, LocalDate>("registroC"));
        tcNombreC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("nombreC"));
        tcApellidoC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("apellidoC"));
        tcLugarC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("lugarC"));
        tcCelebranteC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("celebranteC"));
        tcAnotadoC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("anotadoC"));
        tcObservacionC.setCellValueFactory(new PropertyValueFactory<ConsultaPrimeraComunion, String>("observacionC"));
        ObservableList<ConsultaPrimeraComunion> data2 = FXCollections.observableArrayList();
        Connection connection = ConexionDB.getConexion();
        try {
            String query = "SELECT r.libro, r.folio, r.partida, r.inscritoLibro, "
                    + "f.nombre, f.apellido, f.nacimiento, f.edadFeligres, "
                    + "p.fechaInscripcion, p.fechaSacramento, p.lugarSacramento, "
                    + "c.nombreCelebrante,  "
                    + "o.observacion  "
                    + "FROM feligres f "
                    + "JOIN comunion p ON f.idFeligres = p.idFeligres "
                    + "JOIN celebrante c ON p.celebrante_idCelebrante = c.idCelebrante "
                    + "JOIN registrolibro r ON p.idComunion = r.comunion_idComunion "
                    + "JOIN observacion o ON p.idComunion = o.comunion_idComunion "
                    + "WHERE nombre LIKE ? OR apellido LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + busqueda + "%");
            stmt.setString(2, "%" + busqueda + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data2.add(new ConsultaPrimeraComunion(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getInt("edadFeligres"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getDate("fechaInscripcion").toLocalDate(),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("lugarSacramento"),
                        rs.getString("nombreCelebrante"),
                        rs.getString("inscritoLibro"),
                        rs.getString("observacion")
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data2.isEmpty()) {
                showAlert("Información", "Primera Comunion, No se encontraron resultados para: " + busqueda, Alert.AlertType.INFORMATION);
                tvComunion.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                return;
            }

            tvComunion.setItems(data2);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Hubo un error al realizar la búsqueda.", Alert.AlertType.ERROR);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }//finaliza _consultaComunion

    private void _consultaConfirmacion() throws IOException {
        // Configuración de las columnas usando PropertyValueFactory

        tcLibroCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("libroCf"));
        tcFolioCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("folioCf"));
        tcPartidaCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("partidaCf"));
        tcNombreCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("nombreCf"));
        tcApellidoCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("apellidoCf"));
        tcNacimientoCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, LocalDate>("nacimientoCf"));
        tcEdadCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, Integer>("edadCf"));
        tcCelebranteCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("celebranteCf"));
        tcFechaCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, LocalDate>("fechaCf"));
        tcLugarCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("lugarCf"));
        tcPadreCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("padreCf"));
        tcMadreCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("madreCf"));
        tcPadrinosCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("padrinosCf"));
        tcInscritoCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("inscritoCf"));
        tcObservacionesCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, String>("observacionesCf"));
        tcRegistroCf.setCellValueFactory(new PropertyValueFactory<ConsultaConfirmacion, LocalDate>("registroCf"));

        ObservableList<ConsultaConfirmacion> data3 = FXCollections.observableArrayList();
        Connection connection = ConexionDB.getConexion();
        try {
            String query = "SELECT r.libro, r.folio, r.partida, r.inscritoLibro, "
                    + "f.nombre, f.apellido, f.nacimiento, f.edadFeligres, f.padreFeligres, f.madreFeligres, "
                    + "cf.fechaInscripcion, cf.fechaSacramento, cf.lugarSacramento, cf.padrino, "
                    + "c.nombreCelebrante,  "
                    + "o.observacion  "
                    + "FROM feligres f "
                    + "JOIN confirmacion cf ON f.idFeligres = cf.idFeligres "
                    + "JOIN celebrante c ON cf.celebrante_idCelebrante = c.idCelebrante "
                    + "JOIN registrolibro r ON cf.idConfirmacion = r.confirmacion_idConfirmacion "
                    + "JOIN observacion o ON cf.idConfirmacion = o.confirmacion_idConfirmacion "
                    + "WHERE nombre LIKE ? OR apellido LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + busqueda + "%");
            stmt.setString(2, "%" + busqueda + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data3.add(new ConsultaConfirmacion(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getInt("edadFeligres"),
                        rs.getString("nombreCelebrante"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getString("lugarSacramento"),
                        rs.getString("padreFeligres"),
                        rs.getString("madreFeligres"),
                        rs.getString("padrino"),
                        rs.getString("inscritoLibro"),
                        rs.getString("observacion"),
                        rs.getDate("fechaInscripcion").toLocalDate()
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data3.isEmpty()) {
                showAlert("Información", "Confirmacion, No se encontraron resultados para: " + busqueda, Alert.AlertType.INFORMATION);
                tvConfirmacion.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                return;
            }

            tvConfirmacion.setItems(data3);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Hubo un error al realizar la búsqueda.", Alert.AlertType.ERROR);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//finaliza _confirmacion

    private void _consultaMatrimonios() throws IOException {
        tcLibroM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, Integer>("libroM"));
        tcFolioM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, Integer>("folioM"));
        tcPartidaM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, Integer>("partidaM"));
        tcLugarM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("lugarM"));
        tcFechaM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, LocalDate>("fechaM"));
        tcTestigoUnoM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("testigoUnoM"));
        tcTestigoDosM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("testigoDosM"));
        tcNombreMM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("nombreMM"));
        tcApellidoMM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("apellidoMM"));
        tcEdadMM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, Integer>("edadMM"));
        tcOrigenMM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("origenMM"));
        tcFeligresMM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("feligresMM"));
        tcPadreMM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("padreMM"));
        tcMadreMM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("madreMM"));
        tcNOmbreFM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("nombreFM"));
        tcApellidoFM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("apellidoFM"));
        tcEdadFM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, Integer>("edadFM"));
        tcOrigenFM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("origenFM"));
        tcFeligresFM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("feligresFM"));
        tcPadreFM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("padreFM"));
        tcMadreFM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("madreFM"));
        tcCelebranteM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("celebranteM"));
        tcInscritoM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("inscritoM"));
        tcObservacionM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, String>("observacionM"));
        tcRegistroM.setCellValueFactory(new PropertyValueFactory<ConsultaMatrimonio, LocalDate>("registroM"));
        ObservableList<ConsultaMatrimonio> data4 = FXCollections.observableArrayList();
        Connection connection = ConexionDB.getConexion();

        try {
            String query = "SELECT r.libro, r.folio, r.partida, "
                    + "m.lugarSacramento, m.fechaSacramento, m.testigo1, m.testigo2, "
                    + " f1.nombre AS nombreMM, f1.apellido AS apellidoMM, f1.edadFeligres AS edadMM, f1.lugarNacimiento AS origenMM, f1.feligresDe AS feligresMM, f1.padreFeligres AS padreMM, f1.madreFeligres AS madreMM, "
                    + "f2.nombre AS nombreFM, f2.apellido AS apellidoFM, f2.edadFeligres AS edadFM, f2.lugarNacimiento AS origenFM, f2.feligresDe AS feligresFM, f2.padreFeligres AS padreFM, f2.madreFeligres AS madreFM,  "
                    + "c.nombreCelebrante, r.inscritoLibro, o.observacion, m.fechaInscripcion  "
                    + "FROM Matrimonios m "
                    + "JOIN Feligres f1 ON m.idFeligres1 = f1.idFeligres "
                    + "JOIN Feligres f2 ON m.idFeligres2 = f2.idFeligres "
                    + "JOIN RegistroLibro r ON m.idMatrimonio = r.matrimonio_idMatrimonio "
                    + "JOIN Celebrante c ON m.celebrante_idCelebrante = c.idCelebrante "
                    + "JOIN Observacion o ON m.idMatrimonio = o.matrimonio_idMatrimonio "
                    + "WHERE f1.nombre LIKE ? OR f1.apellido LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + busqueda + "%");
            stmt.setString(2, "%" + busqueda + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                data4.add(new ConsultaMatrimonio(
                        rs.getInt("libro"),
                        rs.getInt("folio"),
                        rs.getInt("partida"),
                        rs.getString("lugarSacramento"),
                        rs.getDate("fechaSacramento").toLocalDate(),
                        rs.getString("testigo1"),
                        rs.getString("testigo2"),
                        rs.getString("nombreMM"),
                        rs.getString("apellidoMM"),
                        rs.getInt("edadMM"),
                        rs.getString("origenMM"),
                        rs.getString("feligresMM"),
                        rs.getString("padreMM"),
                        rs.getString("madreMM"),
                        rs.getString("nombreFM"),
                        rs.getString("apellidoFM"),
                        rs.getInt("edadFM"),
                        rs.getString("origenFM"),
                        rs.getString("feligresFM"),
                        rs.getString("padreFM"),
                        rs.getString("madreFM"),
                        rs.getString("nombreCelebrante"),
                        rs.getString("inscritoLibro"),
                        rs.getString("observacion"),
                        rs.getDate("fechaInscripcion").toLocalDate()
                ));
            }

            // Validación para verificar si no se encontraron resultados
            if (data4.isEmpty()) {
                showAlert("Información", "Matrimonio, No se encontraron resultados para: " + busqueda, Alert.AlertType.INFORMATION);
                tvMatrimonio.setItems(FXCollections.observableArrayList()); // Limpia la tabla
                return;
            }

            tvMatrimonio.setItems(data4);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Hubo un error al realizar la búsqueda.", Alert.AlertType.ERROR);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }// Finaliza _consultaMatrimonios

    //Codigo Miselaneo
    // Función para mostrar alertas fácilmente
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
}
