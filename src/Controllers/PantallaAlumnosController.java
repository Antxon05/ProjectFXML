/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package Controllers;

import Models.Alumno;
import Models.Curso;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
// Esta clase controla la pantalla de alumnos.
public class PantallaAlumnosController implements Initializable {
    
    // Declaración de las listas para almacenar alumnos y cursos.
    private ObservableList<Alumno> alumnos = FXCollections.observableArrayList();
    private ObservableList<String> nombres = FXCollections.observableArrayList();
    private Alert alerta;  // Variable para mostrar alertas de mensajes.
    
    // Declaración de los componentes de la interfaz (botones, tabla, campos de texto, etc.)
    @FXML
    private Button b_add;
    @FXML
    private Button b_delete;
    @FXML
    private Button b_update;
    @FXML
    private TableColumn<Alumno, String> c_correo;
    @FXML
    private TableColumn<Alumno, String> c_nombre;
    @FXML
    private TableColumn<Alumno, Integer> c_tlf;
    @FXML
    private TableColumn<Curso, String> c_curso;
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
    private ComboBox<String> cb_curso;
    @FXML
    private ComboBox<String> cb_alumno;
    @FXML
    private Button b_inicio;
    @FXML
    private ComboBox<String> cb_inscripcionCurso;
    
    // Método para volver a la pantalla principal al hacer clic
    @FXML
    private void volverInicio(MouseEvent e){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PantallaPrincipal.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }catch(Exception exc){
            exc.printStackTrace();  // Si hay error, lo muestra.
        }
    }
    
    // Método para limpiar los filtros de búsqueda (alumnos y cursos)
    @FXML
    private void eliminarFiltro(MouseEvent e){
        this.cb_alumno.setValue(null);
        this.cb_curso.setValue(null);
    }
    
    // Método para vaciar los campos de datos del formulario
    @FXML
    private void vaciarDatos(MouseEvent e){
            tf_nombre.setText("");
            tf_apellido.setText("");
            tf_correo.setText("");
            tf_tlf.setText("");
            dp_fecha.setValue(null);
            cb_inscripcionCurso.setValue(null);
    }
    
    // Método para agregar un nuevo alumno
    @FXML
    private void addAlumno(MouseEvent e){
        try{
            Alumno alumno = new Alumno();

            String nombre = tf_nombre.getText();
            String apellido = tf_apellido.getText();
            String correo = tf_correo.getText();
            
            // Verificación del formato del correo
            if(!correo.contains("@")){
                alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Advertencia");
                alerta.setContentText("No tienes ningún dominio asignado.");
                alerta.showAndWait();
                return;
            }
            
            // Verificación de que el teléfono no tenga más de 9 dígitos
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
            
            // Verificación de campos vacíos
            LocalDate fecha = dp_fecha.getValue();
            String nombreCurso = cb_inscripcionCurso.getValue();
            if(nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || telefonostr.isEmpty() || nombreCurso.isEmpty()){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setContentText("Te falta insertar algun campo");
                alerta.showAndWait();
                return;
            }

            // Añadir alumno
            alumno.añadirAlumno(nombre, apellido, correo, telefono, fecha);
            alumno.añadirAlumnoInscripcion(nombre, apellido, nombreCurso);

            // Mostrar mensaje de éxito
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Se ha registrado el nuevo alumno correctamente.");
            alerta.showAndWait();

            // Actualizar la tabla y los ComboBoxes
            actualizarTabla();
            actualizarComboBoxes();
            vaciarDatos(e);  // Limpiar los campos después de agregar el alumno
            
        }catch(Exception exc){
            exc.printStackTrace();
            alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("Te falta insertar algun campo o el correo ya esta en uso.");
            alerta.showAndWait();
        }
    }
    
    // Método para actualizar los datos de un alumno
    @FXML
    private void updateAlumno(MouseEvent e) throws Exception{
        Alumno alumno = new Alumno();
        
        Integer id = obtenerIdFila();  // Obtener el ID del alumno seleccionado
        String nombre = tf_nombre.getText();
        String apellido = tf_apellido.getText();
        String correo = tf_correo.getText();
        
        // Verificación del correo y teléfono como en la función addAlumno
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
        
        // Confirmación antes de actualizar los datos
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Actualizar datos del alumno");
        alerta.setContentText("¿Estas seguro de que quieres actualizar los datos?");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi){
            // Actualizar datos
            alumno.updateAlumno(id, nombre, apellido, correo, telefono, dp_fecha.getValue(), cb_inscripcionCurso.getValue());
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
    
    // Método para eliminar un alumno
    @FXML
    private void deleteAlumno(MouseEvent e){
        Alumno alumno = new Alumno();
        Integer id = obtenerIdFila();  // Obtener ID del alumno seleccionado
        
        // Confirmación antes de eliminar
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Eliminar datos del alumno.");
        alerta.setContentText("¿Estas seguro de que quieres eliminar este alumno?");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi){
            // Eliminar alumno
            alumno.eliminarAlumno(id, cb_inscripcionCurso.getValue());
            actualizarTabla();
            actualizarComboBoxes();
            vaciarDatos(e);  // Limpiar campos después de eliminar el alumno
            
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
    
    // Método que inicializa la interfaz y se ejecuta al inicio
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        t_alumnos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            seleccionarFila();  // Llamar a la función para seleccionar fila
        });
        
        cb_alumno.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla();  // Filtrar la tabla cuando cambia el alumno
        });
        
        cb_curso.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla();  // Filtrar la tabla cuando cambia el curso
        });
        
        // Actualizar ComboBoxes y tabla al inicio
        actualizarComboBoxes();
        actualizarTabla();
    }

    // Función para filtrar los alumnos en la tabla según el alumno y curso seleccionados
    private void filtrarTabla() {
        String alumnoSeleccionado = cb_alumno.getValue();
        String cursoSeleccionado = cb_curso.getValue();
        
        ObservableList<Alumno> alumnosFiltrados = FXCollections.observableArrayList();

        for (Alumno alumno : alumnos) {
            boolean coincideAlumno = true;
            boolean coincideCurso = true;
            
            if (alumnoSeleccionado != null && !alumnoSeleccionado.isEmpty()) {
                String[] nyaAlumno = alumnoSeleccionado.split(" ");
                String alumnoNombreSeleccionado = nyaAlumno[0];
                String alumnoApellidoSeleccionado = nyaAlumno[1];
                
                coincideAlumno = alumno.getNombre().equals(alumnoNombreSeleccionado) && alumno.getApellido().equals(alumnoApellidoSeleccionado);
            }
            
            if (cursoSeleccionado != null && !cursoSeleccionado.isEmpty()) {
                coincideCurso = alumno.getCurso().equals(cursoSeleccionado);
            }
            
            if (coincideAlumno && coincideCurso) {
                alumnosFiltrados.add(alumno);  // Añadir alumno a la lista filtrada
            }
        }

        t_alumnos.setItems(alumnosFiltrados);  // Mostrar alumnos filtrados en la tabla
    }

    // Función para seleccionar la fila de la tabla y cargar sus datos en el formulario
    public void seleccionarFila(){
        Alumno aSeleccionado = t_alumnos.getSelectionModel().getSelectedItem();
        
        if(aSeleccionado != null){
            tf_nombre.setText(aSeleccionado.getNombre());
            tf_apellido.setText(aSeleccionado.getApellido());
            tf_correo.setText(aSeleccionado.getCorreo());
            tf_tlf.setText(aSeleccionado.getTelefono().toString());
            dp_fecha.setValue(aSeleccionado.getFechaRegistro());
            cb_inscripcionCurso.setValue(aSeleccionado.getCurso());
        }
    }
    
    // Función para actualizar la tabla con los alumnos
    public void actualizarTabla(){
        Alumno a = new Alumno();
       
        c_nombre.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getNombre() + " " + cellData.getValue().getApellido()));
        
        c_correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        c_tlf.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        c_curso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        
        alumnos = a.getAlumnos();
        
        t_alumnos.setItems(alumnos);  // Actualizar los alumnos en la tabla
    }

    // Función para obtener el ID del alumno seleccionado
    public Integer obtenerIdFila(){
        Alumno aSeleccionado = t_alumnos.getSelectionModel().getSelectedItem();
        Integer id = 0;
        if(aSeleccionado != null){
            id = aSeleccionado.getId_alumno();
        }
        return id;  // Devolver el ID
    }
    
    // Función para actualizar los ComboBoxes de alumnos y cursos
    public void actualizarComboBoxes(){
        Alumno a = new Alumno();
        
        cb_alumno.setItems(a.mostrarAlumnos());  // Mostrar alumnos
        cb_curso.setItems(a.mostrarCursos());    // Mostrar cursos
        cb_inscripcionCurso.setItems(a.mostrarCursos());  // Mostrar cursos para inscripción
    }
}
