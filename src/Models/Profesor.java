/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase que representa un profesor en la base de datos.
 *
 * @author antxon
 */
public class Profesor {
    
    private Integer id_profesor; // ID del profesor
    private String nombre; // Nombre del profesor
    private String apellido; // Apellido del profesor
    private String correo; // Correo electrónico del profesor

    // Instancia de la clase que maneja la conexión a la base de datos
    private ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
    private Connection conn = this.conexion.getConnection(); // Conexión a la base de datos

    // Constructor vacío
    public Profesor() {
        
    }
    
    // Constructor con parámetros
    public Profesor(Integer id_profesor, String nombre, String apellido, String correo) {
        this.id_profesor = id_profesor;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
    }

    // Getters y setters para los atributos
    public Integer getId_profesor() {
        return id_profesor;
    }

    public void setId_profesor(Integer id_profesor) {
        this.id_profesor = id_profesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Método para representar al objeto Profesor como String
    @Override
    public String toString() {
        return "Profesor{" + "id_profesor=" + id_profesor + ", nombre=" + nombre + ", apellido=" + apellido + ", correo=" + correo + '}';
    }
    
    // Método para obtener una lista de todos los profesores desde la base de datos
    public ObservableList<Profesor> getProfesores() {
        ObservableList<Profesor> obs = FXCollections.observableArrayList(); // Lista observable para los profesores
        try {
            // Consulta SQL para obtener todos los profesores
            String sql = "SELECT * FROM profesores";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            // Recorrer los resultados y añadirlos a la lista
            while (rs.next()) {
                Integer idProfesor = rs.getInt("id_profesor");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                
                // Crear un nuevo objeto Profesor con los datos obtenidos
                Profesor p = new Profesor(idProfesor, nombre, apellido, correo);

                // Añadir el profesor a la lista observable
                obs.add(p);
            }

            // Cerrar los recursos
            rs.close();
            stmt.close();
            conexion.cerrarConexion();

        } catch (SQLException ex) {
            Logger.getLogger(Profesor.class.getName()).log(Level.SEVERE, null, ex); // Log de error si ocurre
        }
        return obs;
    }
     
    // Método para obtener una lista de los nombres de los profesores
    public ObservableList<String> mostrarNombres(){
        
        ObservableList<String> nombres = FXCollections.observableArrayList(); // Lista observable para los nombres
        
        try{
            // Consulta SQL para obtener los nombres de los profesores
            String sql = "SELECT nombre FROM profesores";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta
            while(rs.next()){
                String nombre = rs.getString("nombre"); // Obtener el nombre
                
                nombres.add(nombre); // Añadir a la lista de nombres
            }
            
        }catch(Exception e){
            e.printStackTrace(); // Manejo de excepciones
        }
        
        return nombres;
    }
    
    // Método para obtener una lista de los apellidos de los profesores
    public ObservableList<String> mostrarApellidos(){
        
        ObservableList<String> apellidos = FXCollections.observableArrayList(); // Lista observable para los apellidos
        
        try{
            // Consulta SQL para obtener los apellidos de los profesores
            String sql = "SELECT apellido FROM profesores";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta
            while(rs.next()){
                String apellido = rs.getString("apellido"); // Obtener el apellido
                
                apellidos.add(apellido); // Añadir a la lista de apellidos
            }
            
        }catch(Exception e){
            e.printStackTrace(); // Manejo de excepciones
        }
        
        return apellidos;
    }
    
    // Método para añadir un nuevo profesor a la base de datos
    public void añadirProfesor(String nombre, String apellido, String correo) throws Exception{
        
        try{
            // Nueva conexión a la base de datos
            ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
            Connection conn = conexion.getConnection();
            
            // Consulta SQL para insertar un nuevo profesor
            String sql = "INSERT INTO profesores (nombre, apellido, correo) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            // Establecer los valores en la consulta
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, correo);
            
            stmt.executeUpdate(); // Ejecutar la consulta
            
            stmt.close();
            conexion.cerrarConexion(); // Cerrar la conexión
            
        }catch(SQLException e){
            if(e.getMessage().contains("Duplicate entry")){ // Verificar si el correo ya existe
                throw new Exception("El correo ya esta registrado.");
            } else{
                throw new Exception("Error al registrar el profesor.");
            }
        }
    }
    
    // Método para actualizar los datos de un profesor
    public void updateProfesor(Integer idProfesor, String nombre, String apellido, String correo){
        try{
            // Consulta SQL para actualizar los datos del profesor
            String updateSql = "UPDATE profesores SET nombre = ?, apellido = ?, correo = ? WHERE id_profesor = ?";
            PreparedStatement pst2 = conn.prepareStatement(updateSql);
                
            pst2.setString(1, nombre);
            pst2.setString(2, apellido);
            pst2.setString(3, correo);
            pst2.setInt(4, idProfesor);
                
            pst2.executeUpdate(); // Ejecutar la actualización
                
            pst2.close();
            
            conexion.cerrarConexion(); // Cerrar la conexión
        }catch(Exception e){
            e.printStackTrace(); // Manejo de excepciones
        }
    }
    
    // Método para eliminar un profesor de la base de datos
    public void eliminarProfesor(Integer idProfesor){
        try{
            // Eliminar la asignación del profesor en otras tablas
            eliminarAsignacionProfesor(idProfesor);
            
            // Verificar si la conexión está abierta
            if (conn == null || conn.isClosed()) {
                conn = conexion.getConnection();
            }
            
            // Consulta SQL para eliminar el profesor
            String sqlDelete = "DELETE FROM profesores WHERE id_profesor = ?";
            PreparedStatement pst2 = conn.prepareStatement(sqlDelete);
            pst2.setInt(1, idProfesor);
            pst2.executeUpdate(); // Ejecutar la eliminación
            pst2.close();
                
            // Reconfigurar el auto incremento de la tabla
            String getMaxIdSql = "SELECT MAX(id_profesor) FROM profesores";
            PreparedStatement pst3 = conn.prepareStatement(getMaxIdSql);
            ResultSet rsMax = pst3.executeQuery();

            int maxId = 0;
            if (rsMax.next()) {
                maxId = rsMax.getInt(1); // Obtener el valor máximo del ID
            }
            rsMax.close();
            pst3.close();
                
            // Ajustar el auto incremento para que el próximo ID sea mayor
            String sqlAlter = "ALTER TABLE profesores AUTO_INCREMENT = ?";
            PreparedStatement pst4 = conn.prepareStatement(sqlAlter);
            pst4.setInt(1, maxId + 1);
            pst4.executeUpdate();
            pst4.close();
                
            System.out.println("Se ha eliminado correctamente el profesor con id = " + idProfesor);
        }catch(Exception e){
            e.printStackTrace(); // Manejo de excepciones
        }
    }
    
    // Método para eliminar las asignaciones de un profesor de la base de datos
    public void eliminarAsignacionProfesor(Integer idProfesor) throws Exception{
        if (conn == null || conn.isClosed()) {
            conn = conexion.getConnection();
        }
        
        try{
            // Consulta SQL para eliminar las asignaciones del profesor
            PreparedStatement stmt = null;
            ResultSet rs = null;
            
            String deleteQuery = "DELETE FROM asignaciones WHERE id_profesor = ?";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setInt(1, idProfesor);
            stmt.executeUpdate(); // Ejecutar la eliminación
            
            // Reconfigurar el auto incremento para la tabla de asignaciones
            String getMaxIdSql = "SELECT MAX(id_asignacion) FROM asignaciones";
            PreparedStatement pst3 = conn.prepareStatement(getMaxIdSql);
            ResultSet rsMax = pst3.executeQuery();

            int maxId = 0;
            if (rsMax.next()) {
                maxId = rsMax.getInt(1); // Obtener el valor máximo del ID
            }
            rsMax.close();
            pst3.close();
                
            // Ajustar el auto incremento para que el próximo ID sea mayor
            String sqlAlter = "ALTER TABLE asignaciones AUTO_INCREMENT = ?";
            PreparedStatement pst4 = conn.prepareStatement(sqlAlter);
            pst4.setInt(1, maxId + 1);
            pst4.executeUpdate();
            pst4.close();
            
            System.out.println("Profesor eliminado");
        }catch (SQLException exc){
            throw new Exception("Error al eliminar el profesor.");
        }
    }
}
