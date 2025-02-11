/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import Models.Alumno;
import Models.Profesor;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Antxon
 */
public class PantallaProfesoresController implements Initializable {

    private ObservableList<Profesor> profesores = FXCollections.observableArrayList();
    private ObservableList<String> nombres = FXCollections.observableArrayList();
    private Alert alerta;
    
    
     @FXML
    private Button b_add;

    @FXML
    private Button b_delete;

    @FXML
    private Button b_eliminarFiltros;

    @FXML
    private Button b_empty;

    @FXML
    private Button b_inicio;

    @FXML
    private Button b_update;

    @FXML
    private TableColumn<Profesor, String> c_apellido;

    @FXML
    private TableColumn<Profesor, String> c_correo;

    @FXML
    private TableColumn<Profesor, String> c_nombre;

    @FXML
    private ComboBox<String> cb_apellido;

    @FXML
    private ComboBox<String> cb_nombre;

    @FXML
    private Label label;

    @FXML
    private TableView<Profesor> t_profesores;

    @FXML
    private TextField tf_apellido;

    @FXML
    private TextField tf_correo;

    @FXML
    private TextField tf_nombre;

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
    void eliminarFiltro(MouseEvent event) {

        this.cb_nombre.setValue(null);
        this.cb_apellido.setValue(null);
        
        actualizarComboBoxes();
        
    }
    
    
    @FXML
    void addProfesor(MouseEvent e) {
        try{
            
            Profesor profesor = new Profesor();

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

            profesor.añadirProfesor(nombre, apellido, correo);
            

            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Se ha registrado el nuevo profesor correctamente.");
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
    void deleteProfesor(MouseEvent e) {
        Profesor profesor = new Profesor();
        String correo = tf_correo.getText();
        
        
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Eliminar datos del profesor.");
        alerta.setContentText("¿Estas seguro de que quieres eliminar este profesor?");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi){
            profesor.eliminarProfesor(correo);
            actualizarTabla();
            actualizarComboBoxes();
            vaciarDatos(e);
            
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("El profesor ha sido eliminado correctamente.");
            alerta.showAndWait();
            
            
        }else{
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("El profesor no se ha eliminado.");
            alerta.showAndWait();
        }
    }

    @FXML
    void updateProfesor(MouseEvent event) {
        Profesor profesor = new Profesor();
        
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
        
        
        //Alerta de confirmación de modificación del alumno
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Actualizar datos del profesor");
        alerta.setContentText("¿Estas seguro de que quieres actualizar los datos?");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi){
            profesor.updateProfesor(nombre, apellido, correo);
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
    void vaciarDatos(MouseEvent event) {
        tf_nombre.setText("");
        tf_apellido.setText("");
        tf_correo.setText("");
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarTabla();
        actualizarComboBoxes();
        
        t_profesores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        
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
        
        ObservableList<Profesor> profesoresFiltrados = FXCollections.observableArrayList();

        for (Profesor profesor : profesores) {
            boolean coincideNombre = (nombreSeleccionado == null || profesor.getNombre().equals(nombreSeleccionado));
            boolean coincideApellido = (apellidoSeleccionado == null || profesor.getApellido().equals(apellidoSeleccionado));

            if (coincideNombre && coincideApellido) {
                profesoresFiltrados.add(profesor);
            }
        }

        t_profesores.setItems(profesoresFiltrados);
    }


    public void seleccionarFila(){
        Profesor pSeleccionado = t_profesores.getSelectionModel().getSelectedItem();
        
        if(pSeleccionado != null){
            
            tf_nombre.setText(pSeleccionado.getNombre());
            tf_apellido.setText(pSeleccionado.getApellido());
            tf_correo.setText(pSeleccionado.getCorreo());
            
        }
    }
    
    
    
    //Metodos para actualizar tablas y comboboxes
    
    public void actualizarTabla(){
        
        Profesor p = new Profesor();
        
        c_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        c_apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        c_correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        
        
        profesores = p.getProfesores();
        
        t_profesores.setItems(profesores);
    }
    
    public void actualizarComboBoxes(){
        Profesor p = new Profesor();
        
        cb_nombre.setItems(p.mostrarNombres());
        cb_apellido.setItems(p.mostrarApellidos());
    }
    
    
}
