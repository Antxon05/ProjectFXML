/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antxon
 */
public class PantallaPrincipalController implements Initializable {

    @FXML
    private Button b_pCursos;

    @FXML
    private Button b_pEstudiantes;

    @FXML
    private Button b_pProfesores;
    
    //Nos conduce hacia la pantalla de Estudiantes
    @FXML
    private void ventanaEstudiantes(MouseEvent e){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PantallaAlumnos.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.show();
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
    
    //Nos conduce hacia la pantalla de Profesores
    @FXML
    private void ventanaProfesores(MouseEvent e){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PantallaProfesores.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.show();
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
    
    //Nos conduce hacia la pantalla de Cursos
    @FXML
    private void ventanaCursos(MouseEvent e){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PantallaCursos.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.show();
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }    
    
}
