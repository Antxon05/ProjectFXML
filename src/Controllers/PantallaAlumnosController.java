/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package Controllers;

import Models.Alumno;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author antxon
 */
public class PantallaAlumnosController implements Initializable {
    
    private ObservableList<Alumno> alumnos = FXCollections.observableArrayList();
    private ObservableList<String> nombres = FXCollections.observableArrayList();
    private Alert alerta;
    
    
    @FXML
    private Button b_add;

    @FXML
    private Button b_delete;

    @FXML
    private Button b_update;

    @FXML
    private TableColumn<Alumno, String> c_apellido;

    @FXML
    private TableColumn<Alumno, String> c_correo;

    @FXML
    private TableColumn<Alumno, LocalDate> c_fecha;

    @FXML
    private TableColumn<Alumno, String> c_nombre;

    @FXML
    private TableColumn<Alumno, Integer> c_tlf;

    @FXML
    private DatePicker dp_fecha;

    @FXML
    private Label label;

    @FXML
    private TableView<Alumno> t_alumnos;

    @FXML
    private TextField tf_apellido;

    @FXML
    private TextField tf_correo;

    @FXML
    private TextField tf_nombre;

    @FXML
    private TextField tf_tlf;
    
    @FXML
    private ComboBox<String> cb_apellido;

    @FXML
    private ComboBox<String> cb_nombre;
    
    @FXML
    private Button b_inicio;
    
    
    @FXML
    private void volverInicio(MouseEvent e){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PantallaPrincipal.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.show();
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
    
    @FXML
    private void eliminarFiltro(MouseEvent e){
        this.cb_nombre.setValue(null);
        this.cb_apellido.setValue(null);
    }
    
    @FXML
    private void vaciarDatos(MouseEvent e){
            tf_nombre.setText("");
            tf_apellido.setText("");
            tf_correo.setText("");
            tf_tlf.setText("");
            dp_fecha.setValue(null);
    }
    
    
    @FXML
    private void addAlumno(MouseEvent e){
        try{
            
            Alumno alumno = new Alumno();

            String nombre = tf_nombre.getText();
            String apellido = tf_apellido.getText();
            String correo = tf_correo.getText();
            if(!correo.contains("@")){
                alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Advertencia");
                alerta.setContentText("No tienes ningún dominio asignado.");
                alerta.showAndWait();

                return;
            }
            
            String telefonostr = tf_tlf.getText();

            if(telefonostr.length() > 9){
                alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Advertencia");
                alerta.setContentText("Debes insertar como máximo 9 dígitos.");
                alerta.showAndWait();

                return;
            }

            Integer telefono = null;
            try{
                telefono = Integer.parseInt(telefonostr);
            }catch(NumberFormatException exception){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setContentText("Los valores deben de ser numéricos.");
                alerta.showAndWait();
                return;
            }


            LocalDate fecha = dp_fecha.getValue();

            alumno.añadirAlumno(nombre, apellido, correo, telefono, fecha);
            

            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Se ha registrado el nuevo alumno correctamente.");
            alerta.showAndWait();

            actualizarTabla();
            actualizarComboBoxes();
            vaciarDatos(e);
            
        }catch(Exception exc){
            alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("Te falta insertar algun campo o el correo ya esta en uso.");
            alerta.showAndWait();
        }
        
            
    }
    
    @FXML
    private void updateAlumno(MouseEvent e){
        
        Alumno alumno = new Alumno();
        
        String nombre = tf_nombre.getText();
        String apellido = tf_apellido.getText();
        String correo = tf_correo.getText();
        
        //Verificación de que el correo contenga el @
        if(!correo.contains("@")){
                alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Advertencia");
                alerta.setContentText("No tienes ningún dominio asignado.");
                alerta.showAndWait();

                return;
            }
        String telefonostr = tf_tlf.getText();
        
        //Verificación de que el telefono tenga menos de 9 dígitos
            if(telefonostr.length() > 9){
                alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Advertencia");
                alerta.setContentText("Debes insertar como máximo 9 dígitos.");
                alerta.showAndWait();

                return;
            }

            //Verificación de que los valores del teléfono sean numéricos
            Integer telefono = null;
            try{
                telefono = Integer.parseInt(telefonostr);
            }catch(NumberFormatException exception){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setContentText("Los valores deben de ser numéricos.");
                alerta.showAndWait();
                return;
            }
        LocalDate fecha = dp_fecha.getValue();
        
        //Alerta de confirmación de modificación del alumno
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Actualizar datos del alumno");
        alerta.setContentText("¿Estas seguro de que quieres actualizar los datos?");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi){
            alumno.updateAlumno(nombre, apellido, correo, telefono, fecha);
            actualizarTabla();
            
            
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Los datos han sido actualizados correctamente.");
            alerta.showAndWait();
            
        }else{
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Los datos no se han actualizado.");
            alerta.showAndWait();
        }
    }
    
    
    @FXML
    private void deleteAlumno(MouseEvent e){
        
        Alumno alumno = new Alumno();
        String correo = tf_correo.getText();
        
        
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Eliminar datos del alumno.");
        alerta.setContentText("¿Estas seguro de que quieres eliminar este alumno?");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi){
            alumno.eliminarAlumno(correo);
            actualizarTabla();
            actualizarComboBoxes();
            vaciarDatos(e);
            
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("El alumno ha sido eliminado correctamente.");
            alerta.showAndWait();
            
        }else{
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("El alumno no se ha eliminado.");
            alerta.showAndWait();
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        t_alumnos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        
            seleccionarFila();
        
        });
        
        cb_nombre.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla();
        });
        
        cb_apellido.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla();
        });
        
        actualizarComboBoxes();
        actualizarTabla();
    }
    
    private void filtrarTabla() {
        String nombreSeleccionado = cb_nombre.getValue();
        String apellidoSeleccionado = cb_apellido.getValue();
        
        ObservableList<Alumno> alumnosFiltrados = FXCollections.observableArrayList();

        for (Alumno alumno : alumnos) {
            boolean coincideNombre = (nombreSeleccionado == null || alumno.getNombre().equals(nombreSeleccionado));
            boolean coincideApellido = (apellidoSeleccionado == null || alumno.getApellido().equals(apellidoSeleccionado));

            if (coincideNombre && coincideApellido) {
                alumnosFiltrados.add(alumno);
            }
        }

        t_alumnos.setItems(alumnosFiltrados);
    }


    public void seleccionarFila(){
        Alumno aSeleccionado = t_alumnos.getSelectionModel().getSelectedItem();
        
        if(aSeleccionado != null){
            
            tf_nombre.setText(aSeleccionado.getNombre());
            tf_apellido.setText(aSeleccionado.getApellido());
            tf_correo.setText(aSeleccionado.getCorreo());
            tf_tlf.setText(aSeleccionado.getTelefono().toString());
            dp_fecha.setValue(aSeleccionado.getFechaRegistro());
            
        }
    }
    
    
    public void actualizarTabla(){
        
        Alumno a = new Alumno();
        
        c_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        c_apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        c_correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        c_tlf.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        c_fecha.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        
        
        alumnos = a.getAlumnos();
        
        t_alumnos.setItems(alumnos);
    }
    
    public void actualizarComboBoxes(){
        Alumno a = new Alumno();
        
        cb_nombre.setItems(a.mostrarNombres());
        cb_apellido.setItems(a.mostrarApellidos());
    }
    
    
}
