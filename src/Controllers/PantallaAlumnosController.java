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
import javafx.scene.control.Button;
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
    
    private static ObservableList<Alumno> alumnos = FXCollections.observableArrayList();
    
    
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
    private void vaciarDatos(MouseEvent e){
            tf_nombre.setText("");
            tf_apellido.setText("");
            tf_correo.setText("");
            tf_tlf.setText("");
            dp_fecha.setValue(null);
    }
    
    
    @FXML
    private void addAlumno(MouseEvent e){
        
        
        
        
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Alumno a = new Alumno();
        
        
        c_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        c_apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        c_correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        c_tlf.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        c_fecha.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        
        alumnos = a.getAlumnos();
        
        t_alumnos.setItems(alumnos);
        
        t_alumnos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        
        seleccionarFila();
        
        });
        
       
        
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
    
}
