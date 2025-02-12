package Controllers;

import Models.Alumno;
import Models.Curso;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * FXML Controller class para gestionar los cursos.
 * 
 * Este controlador se encarga de la lógica de la interfaz de usuario para agregar,
 * eliminar, actualizar cursos, y manejar los filtros de la tabla de cursos.
 */
public class PantallaCursosController implements Initializable {

    private ObservableList<Curso> cursos = FXCollections.observableArrayList();
    private ObservableList<String> nombres = FXCollections.observableArrayList();
    private Alert alerta;
    
    @FXML
    private Button b_add;
    @FXML
    private Button b_asignarP;
    @FXML
    private Button b_delete;
    @FXML
    private Button b_eliminarFiltros;
    @FXML
    private Button b_inicio;
    @FXML
    private Button b_inscribirA;
    @FXML
    private Button b_update;
    @FXML
    private Button b_vaciar;
    
    @FXML
    private TableColumn<Curso, String> c_descripcion;
    @FXML
    private TableColumn<Curso, String> c_duracion;
    @FXML
    private TableColumn<Curso, String> c_nombre;
    @FXML
    private TableColumn<Curso, String> c_profesor;
    
    @FXML
    private ComboBox<String> cb_curso;
    @FXML
    private ComboBox<String> cb_profesor;
    @FXML
    private TextField tf_duracion;
    @FXML
    private TextField tf_nombre;
    @FXML
    private TableView<Curso> tv_cursos;
    @FXML
    private TextArea tf_descripcion;
    @FXML
    private ComboBox<String> cb_seleccionProfesor;
    
    /**
     * Agrega un nuevo curso a la base de datos.
     * Recoge la información ingresada y la envía al modelo Curso para que sea registrada.
     * Luego actualiza la tabla y los ComboBoxes.
     */
    @FXML
    void addCurso(MouseEvent e) {
        try {
            Curso curso = new Curso();
            String nombre = tf_nombre.getText();
            String descripcion = tf_descripcion.getText();
            String duracion = tf_duracion.getText();
            String profesor = cb_seleccionProfesor.getValue();

            curso.añadirCurso(nombre, descripcion, duracion);
            curso.añadirProfesorAsignacion(profesor, nombre);
            
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Se ha registrado el nuevo curso correctamente.");
            alerta.showAndWait();

            actualizarTabla();
            actualizarComboBoxes();
            vaciarDatos(e);
            
        } catch(Exception exc) {
            alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("Te falta insertar algun campo.");
            alerta.showAndWait();
        }
    }

    /**
     * Elimina un curso de la base de datos.
     * Pide confirmación al usuario antes de proceder con la eliminación.
     * Luego actualiza la tabla y los ComboBoxes.
     */
    @FXML
    void deleteCurso(MouseEvent e) {
        Curso curso = new Curso();
        Integer id = obtenerIdFila();
        String profesor = cb_seleccionProfesor.getValue();
        
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Eliminar datos del curso.");
        alerta.setContentText("¿Estas seguro de que quieres eliminar este curso?");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi) {
            curso.eliminarCurso(id, profesor);
            actualizarTabla();
            actualizarComboBoxes();
            vaciarDatos(e);
            
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("El curso ha sido eliminado correctamente.");
            alerta.showAndWait();
        } else {
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("El curso no se ha eliminado.");
            alerta.showAndWait();
        }
    }

    /**
     * Actualiza los datos de un curso.
     * Solicita confirmación al usuario antes de realizar la actualización.
     * Luego actualiza la tabla de cursos.
     */
    @FXML
    void updateCurso(MouseEvent e) throws Exception {
        Curso curso = new Curso();
        
        Integer id = obtenerIdFila();
        String nombre = tf_nombre.getText();
        String descripcion = tf_descripcion.getText();
        Integer duracion = Integer.parseInt(tf_duracion.getText());
        String profesor = cb_seleccionProfesor.getValue();
        
        alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Actualizar datos del curso");
        alerta.setContentText("¿Estas seguro de que quieres actualizar los datos?");
        
        ButtonType bSi = new ButtonType("Sí");
        ButtonType bNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alerta.getButtonTypes().setAll(bSi, bNo);
        
        Optional<ButtonType> respuesta = alerta.showAndWait();
        
        if(respuesta.isPresent() && respuesta.get() == bSi) {
            curso.updateCurso(id, nombre, descripcion, duracion, profesor);
            actualizarTabla();
            
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Los datos han sido actualizados correctamente.");
            alerta.showAndWait();
        } else {
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Información");
            alerta.setContentText("Los datos no se han actualizado.");
            alerta.showAndWait();
        }
    }

    /**
     * Elimina los filtros de búsqueda aplicados en los ComboBoxes.
     */
    @FXML
    void eliminarFiltro(MouseEvent e) {
        this.cb_curso.setValue(null);
        this.cb_profesor.setValue(null);
    }

    /**
     * Vacía los campos de entrada de texto en el formulario.
     */
    @FXML
    void vaciarDatos(MouseEvent e) {
        tf_nombre.setText("");
        tf_descripcion.setText("");
        tf_duracion.setText("");
        cb_seleccionProfesor.setValue("");
    }

    /**
     * Vuelve a la pantalla principal (inicio).
     * Carga la vista de la pantalla principal.
     */
    @FXML
    void volverInicio(MouseEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PantallaPrincipal.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch(Exception exc) {
            exc.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Configura los listeners para los filtros de la tabla.
        tv_cursos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            seleccionarFila();
        });
        
        cb_curso.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla();
        });
        
        cb_profesor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarTabla();
        });
        
        // Actualiza los ComboBoxes y la tabla de cursos al inicio.
        actualizarComboBoxes();
        actualizarTabla();
    }

    /**
     * Filtra los cursos mostrados en la tabla según los filtros seleccionados en los ComboBoxes.
     */
    private void filtrarTabla() {
        String nombreSeleccionado = cb_curso.getValue();
        String profesorSeleccionado = cb_profesor.getValue();
        
        ObservableList<Curso> cursosFiltrados = FXCollections.observableArrayList();

        for (Curso curso : cursos) {
            boolean coincideNombre = (nombreSeleccionado == null || curso.getNombre().equals(nombreSeleccionado));
            boolean coincideProfesor = (profesorSeleccionado == null || curso.getProfesor().equals(profesorSeleccionado));

            if (coincideNombre && coincideProfesor) {
                cursosFiltrados.add(curso);
            }
        }

        tv_cursos.setItems(cursosFiltrados);
    }

    /**
     * Rellena los campos del formulario con los datos del curso seleccionado en la tabla.
     */
    public void seleccionarFila() {
        Curso cSeleccionado = tv_cursos.getSelectionModel().getSelectedItem();
        
        if(cSeleccionado != null) {
            tf_nombre.setText(cSeleccionado.getNombre());
            tf_descripcion.setWrapText(true);
            tf_descripcion.setText(cSeleccionado.getDescripcion());
            tf_duracion.setText(Integer.toString(cSeleccionado.getDuracion_horas()));
            cb_seleccionProfesor.setValue(cSeleccionado.getProfesor());
        }
    }

    /**
     * Obtiene el ID del curso seleccionado en la tabla.
     */
    public Integer obtenerIdFila() {
        Curso cSeleccionado = tv_cursos.getSelectionModel().getSelectedItem();
        Integer id = 0;
        if(cSeleccionado != null) {
            id = cSeleccionado.getId_curso();
        }
        return id;
    }

    /**
     * Actualiza los datos de la tabla con la lista de cursos.
     */
    public void actualizarTabla() {
        Curso c = new Curso();
        
        c_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        c_duracion.setCellValueFactory(new PropertyValueFactory<>("duracion_horas"));
        c_profesor.setCellValueFactory(new PropertyValueFactory<>("profesor"));
        
        cursos = c.getCursos();
        tv_cursos.setItems(cursos);
    }

    /**
     * Actualiza los ComboBoxes con los nombres de cursos y profesores disponibles.
     */
    public void actualizarComboBoxes() {
        Curso c = new Curso();
        
        cb_curso.setItems(c.mostrarNombres());
        cb_profesor.setItems(c.mostrarNombreProfesores());
        cb_seleccionProfesor.setItems(c.mostrarNombreProfesores());
    }
}
