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
 * FXML Controller class para gestionar los profesores.
 * Permite añadir, eliminar, actualizar y filtrar profesores.
 * También se encarga de la interacción con la interfaz de usuario.
 * 
 * @author Antxon
 */
public class PantallaProfesoresController implements Initializable {

    // Listas observables para los profesores y los nombres de los profesores
    private ObservableList<Profesor> profesores = FXCollections.observableArrayList();
    private ObservableList<String> nombres = FXCollections.observableArrayList();
    private Alert alerta;
    
    
    // Elementos de la interfaz
    @FXML private Button b_add;
    @FXML private Button b_delete;
    @FXML private Button b_eliminarFiltros;
    @FXML private Button b_empty;
    @FXML private Button b_inicio;
    @FXML private Button b_update;
    @FXML private TableColumn<Profesor, String> c_apellido;
    @FXML private TableColumn<Profesor, String> c_correo;
    @FXML private TableColumn<Profesor, String> c_nombre;
    @FXML private ComboBox<String> cb_nombre;
    @FXML private Label label;
    @FXML private TableView<Profesor> t_profesores;
    @FXML private TextField tf_apellido;
    @FXML private TextField tf_correo;
    @FXML private TextField tf_nombre;

    /**
     * Método para volver a la pantalla principal al hacer clic en el botón de inicio.
     * @param e Evento del clic.
     */
    @FXML
    private void volverInicio(MouseEvent e){
        try{
            // Carga la pantalla principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PantallaPrincipal.fxml"));
            Parent root = loader.load();
            
            // Obtiene la ventana actual y cambia su escena
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
    
    /**
     * Elimina el filtro de búsqueda del ComboBox.
     * @param event Evento del clic.
     */
    @FXML
    void eliminarFiltro(MouseEvent event) {
        this.cb_nombre.setValue(null);  // Elimina el valor seleccionado en el ComboBox
        actualizarComboBoxes(); // Actualiza el ComboBox con los valores posibles
    }
    
    /**
     * Añade un nuevo profesor a la base de datos.
     * Realiza las validaciones antes de añadir al profesor.
     * @param e Evento del clic.
     */
    @FXML
    void addProfesor(MouseEvent e) {
        try{
            // Crea una instancia de Profesor
            Profesor profesor = new Profesor();

            // Obtiene los datos del formulario
            String nombre = tf_nombre.getText();
            String apellido = tf_apellido.getText();
            String correo = tf_correo.getText();
            
            // Validaciones de los campos
            if(nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setContentText("Te falta insertar algun campo");
                alerta.showAndWait();
                return;
            }
            
            // Verifica si el correo tiene un dominio válido
            if(!correo.contains("@")){
                alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Advertencia");
                alerta.setContentText("No tienes ningún dominio asignado.");
                alerta.showAndWait();
                return;
            }

            // Añade el profesor a la base de datos
            profesor.añadirProfesor(nombre, apellido, correo);
            
            // Muestra un mensaje de éxito
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Se ha registrado el nuevo profesor correctamente.");
            alerta.showAndWait();

            // Actualiza la tabla y los ComboBoxes
            actualizarTabla();
            actualizarComboBoxes();
            vaciarDatos(e);
            
        }catch(Exception exc){
            System.out.println("Error en el registro de profesor");
            exc.printStackTrace();
        }
    }

    /**
     * Elimina un profesor de la base de datos.
     * Solicita confirmación antes de eliminar el profesor.
     * @param e Evento del clic.
     */
    @FXML
    void deleteProfesor(MouseEvent e) {
        Profesor profesor = new Profesor();
        Integer id = obtenerIdFila(); // Obtiene el ID del profesor seleccionado
        
        // Alerta de confirmación para eliminar al profesor
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Eliminar datos del profesor.");
        alerta.setContentText("¿Estas seguro de que quieres eliminar este profesor? Se borraran todos los vinculos con el curso");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi){
            // Elimina el profesor si el usuario confirma
            profesor.eliminarProfesor(id);
            actualizarTabla();
            actualizarComboBoxes();
            vaciarDatos(e);
            
            // Muestra mensaje de éxito
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("El profesor ha sido eliminado correctamente.");
            alerta.showAndWait();
        }else{
            // Muestra mensaje si el profesor no fue eliminado
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("El profesor no se ha eliminado.");
            alerta.showAndWait();
        }
    }

    /**
     * Actualiza los datos de un profesor seleccionado.
     * Muestra una alerta para confirmar la actualización.
     * @param event Evento del clic.
     */
    @FXML
    void updateProfesor(MouseEvent event) {
        Profesor profesor = new Profesor();
        
        Integer id = obtenerIdFila();
        String nombre = tf_nombre.getText();
        String apellido = tf_apellido.getText();
        String correo = tf_correo.getText();
        
        // Validaciones de los campos
        if(nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()){
            alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("Te falta insertar algun campo");
            alerta.showAndWait();
            return;
        }
        
        // Verifica si el correo tiene un dominio válido
        if(!correo.contains("@")){
            alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setContentText("No tienes ningún dominio asignado.");
            alerta.showAndWait();
            return;
        }
        
        // Alerta de confirmación para actualizar los datos del profesor
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Actualizar datos del profesor");
        alerta.setContentText("¿Estas seguro de que quieres actualizar los datos?");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi){
            // Actualiza los datos del profesor
            profesor.updateProfesor(id, nombre, apellido, correo);
            actualizarTabla();
            
            // Muestra mensaje de éxito
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Los datos han sido actualizados correctamente.");
            alerta.showAndWait();
        }else{
            // Muestra mensaje si los datos no fueron actualizados
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Los datos no se han actualizado.");
            alerta.showAndWait();
        }
    }

    /**
     * Limpia los campos de entrada de datos del formulario.
     * @param event Evento del clic.
     */
    @FXML
    void vaciarDatos(MouseEvent event) {
        tf_nombre.setText("");
        tf_apellido.setText("");
        tf_correo.setText("");
    }
    
    
    /**
     * Método llamado al inicializar el controlador. 
     * Se encarga de actualizar la tabla y ComboBox con los datos actuales.
     * @param url URL de la vista.
     * @param rb Recursos de la vista.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarTabla(); // Actualiza la tabla con los datos de los profesores
        actualizarComboBoxes(); // Actualiza el ComboBox con los nombres de los profesores
        
        // Listener para seleccionar un profesor en la tabla
        t_profesores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            seleccionarFila();
        });
        
        // Listener para filtrar la tabla por nombre seleccionado en el ComboBox
        cb_nombre.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla();
        });
        
        actualizarTabla();
    }    
    
    /**
     * Obtiene el ID de la fila seleccionada en la tabla.
     * @return ID del profesor seleccionado.
     */
    public Integer obtenerIdFila(){
        Profesor pSeleccionado = t_profesores.getSelectionModel().getSelectedItem();
        Integer id = 0;
        if(pSeleccionado != null){
            id = pSeleccionado.getId_profesor();
        }
        
        return id;
    }
    
    /**
     * Filtra los profesores en la tabla según el nombre seleccionado en el ComboBox.
     */
    private void filtrarTabla() {
        String nombreSeleccionado = cb_nombre.getValue();
        
        ObservableList<Profesor> profesoresFiltrados = FXCollections.observableArrayList();

        for (Profesor profesor : profesores) {
            boolean coincideNombre = (nombreSeleccionado == null || profesor.getNombre().equals(nombreSeleccionado));
            if (coincideNombre) {
                profesoresFiltrados.add(profesor);
            }
        }

        t_profesores.setItems(profesoresFiltrados);
    }


    /**
     * Rellena los campos del formulario con los datos del profesor seleccionado en la tabla.
     */
    public void seleccionarFila(){
        Profesor pSeleccionado = t_profesores.getSelectionModel().getSelectedItem();
        
        if(pSeleccionado != null){
            tf_nombre.setText(pSeleccionado.getNombre());
            tf_apellido.setText(pSeleccionado.getApellido());
            tf_correo.setText(pSeleccionado.getCorreo());
        }
    }
    
    
    // Métodos para actualizar la tabla y el ComboBox con los datos de los profesores.
    
    /**
     * Actualiza la tabla de profesores con los datos actuales.
     */
    public void actualizarTabla(){
        Profesor p = new Profesor();
        
        // Establece las columnas de la tabla
        c_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        c_apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        c_correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        
        // Obtiene la lista de profesores y la asigna a la tabla
        profesores = p.getProfesores();
        t_profesores.setItems(profesores);
    }
    
    /**
     * Actualiza los elementos del ComboBox con los nombres de los profesores.
     */
    public void actualizarComboBoxes(){
        Profesor p = new Profesor();
        cb_nombre.setItems(p.mostrarNombres()); // Obtiene los nombres de los profesores
    }
}
