/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package Controllers;

import Models.Alumno;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author antxon
 */
public class PantallaAlumnosController implements Initializable {
    
    private ObservableList<Alumno> alumnos = FXCollections.observableArrayList();
    private ObservableList<String> nombres = FXCollections.observableArrayList();
    
    
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
        
        Alumno alumno = new Alumno();
        
        String nombre = tf_nombre.getText();
        String apellido = tf_apellido.getText();
        String correo = tf_correo.getText();
        String telefonostr = tf_tlf.getText();
        
        if(telefonostr.length() > 9){
            Alert mensaje = new Alert(Alert.AlertType.WARNING);
            mensaje.setTitle("Advertencia");
            mensaje.setContentText("Debes insertar como máximo 9 dígitos.");
            mensaje.showAndWait();
            
            return;
        }
        
        Integer telefono = null;
        try{
            telefono = Integer.parseInt(telefonostr);
        }catch(NumberFormatException exception){
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setTitle("Error");
            mensaje.setContentText("Los valores deben de ser numéricos.");
            mensaje.showAndWait();
            return;
        }
        
        
        LocalDate fecha = dp_fecha.getValue();
        
        alumno.añadirAlumno(nombre, apellido, correo, telefono, fecha);
        
        
        actualizarTabla();
        actualizarComboBoxes();
    }
    
    @FXML
    private void updateAlumno(MouseEvent e){
        
        Alumno alumno = new Alumno();
        
        String nombre = tf_nombre.getText();
        String apellido = tf_apellido.getText();
        String correo = tf_correo.getText();
        Integer telefono = Integer.parseInt(tf_tlf.getText());
        LocalDate fecha = dp_fecha.getValue();
        
        alumno.updateAlumno(nombre, apellido, correo, telefono, fecha);
        
        actualizarTabla();
    }
    
    
    @FXML
    private void deleteAlumno(MouseEvent e){
        
        Alumno alumno = new Alumno();
        
        String correo = tf_correo.getText();
        
        alumno.eliminarAlumno(correo);
        
        actualizarTabla();
        
        vaciarDatos(e);
    }
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        actualizarTabla();
        actualizarComboBoxes();
        
        t_alumnos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        
            seleccionarFila();
        
        });
        
        cb_nombre.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla();
        });
        
        cb_apellido.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla();
        });
        
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
