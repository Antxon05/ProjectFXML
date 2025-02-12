/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author antxon
 */
public class Curso {
    
    private Integer id_curso;
    private String nombre;
    private String descripcion;
    private Integer duracion_horas;
    private String profesor;
    
    private ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
    private Connection conn = this.conexion.getConnection();

    public Curso() {
    }

    public Curso(Integer id_curso, String nombre, String descripcion, Integer duracion_horas, String profesor) {
        this.id_curso = id_curso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion_horas = duracion_horas;
        this.profesor = profesor;
    }

    

    public Integer getId_curso() {
        return id_curso;
    }

    public void setId_curso(Integer id_curso) {
        this.id_curso = id_curso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getDuracion_horas() {
        return duracion_horas;
    }

    public void setDuracion_horas(Integer duracion_horas) {
        this.duracion_horas = duracion_horas;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    @Override
    public String toString() {
        return "Curso{" + "id_curso=" + id_curso + ", nombre=" + nombre + ", descripcion=" + descripcion + ", duracion_horas=" + duracion_horas + ", profesor=" + profesor + '}';
    }
    
    public void añadirCurso(String nombre, String descripcion, String duracion) throws Exception{
        
        try{
            
            if (conn == null || conn.isClosed()) {
                conn = conexion.getConnection();
            }
            
            String sql = "INSERT INTO cursos (nombre, descripcion, duracion_horas) VALUES (?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setInt(3, Integer.parseInt(duracion));
            
            
            stmt.executeUpdate();
            
            stmt.close();
            
        }catch(SQLException e){
           throw new Exception("Error al registrar el curso.");
        }
    }
    
    public void añadirProfesorAsignacion(String profesor, String nombreCurso)throws Exception{
        
        if (conn == null || conn.isClosed()) {
            conn = conexion.getConnection();
        }
        
        String[] tablaProfesor = profesor.split(" ");
        String nombre = tablaProfesor[0];
        String apellido = tablaProfesor[1];
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try{
            
            //Obtenemos el id del profesor para luego asignarselo a la tabla de asignaciones
            String consulta1 = "SELECT id_profesor FROM profesores WHERE nombre = ? AND apellido = ?";
            stmt = conn.prepareStatement(consulta1);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            
            rs = stmt.executeQuery();
            int profesorId = -1;
            
            if(rs.next()){
                profesorId = rs.getInt("id_profesor");
            }
            
            rs.close();
            stmt.close();
            
            //Obtenemos el id del curso para asignarlo cunto a la del profesor en la tabla dfe asignaciones
            String consulta2 = "SELECT id_curso FROM cursos WHERE nombre = ?";
            stmt = conn.prepareStatement(consulta2);
            stmt.setString(1, nombreCurso);
            
            rs = stmt.executeQuery();
            int cursoId = -1;
            
            if(rs.next()){
                cursoId = rs.getInt("id_curso");
            }
            
            rs.close();
            stmt.close();
            
            
            //Insertamos los datos en la tabla de asignaciones
            String insertQuery = "INSERT INTO asignaciones (id_profesor, id_curso) VALUES (?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setInt(1, profesorId);
            stmt.setInt(2, cursoId);
            
            stmt.executeUpdate();
            
            System.out.println("Profesor asignado al curso");
            
            
        }catch (SQLException exc){
            throw new Exception("Error al asignar el profesor.");
        }
    }
    
    public void eliminarCurso(String nombre, String profesor){
        
        try{
            
            eliminarAsignacion(profesor, nombre);
            
            if (conn == null || conn.isClosed()) {
                conn = conexion.getConnection();
            }
            
            String consulta = "SELECT id_curso FROM cursos WHERE nombre = ?";
            PreparedStatement pst1 = conn.prepareStatement(consulta);
            pst1.setString(1, nombre);
            ResultSet rs = pst1.executeQuery();
            
            if(rs.next()){
                int idCurso = rs.getInt("id_curso");
                
                String sqlDelete = "DELETE FROM cursos WHERE id_curso = ?";
                PreparedStatement pst2 = conn.prepareStatement(sqlDelete);
                pst2.setInt(1, idCurso);
                pst2.executeUpdate();
                
                
                String getMaxIdSql = "SELECT MAX(id_curso) FROM cursos";
                PreparedStatement pst3 = conn.prepareStatement(getMaxIdSql);
                ResultSet rsMax = pst3.executeQuery();
                
                int maxId = 0;
                if(rsMax.next()){
                    maxId = rsMax.getInt(1);
                }
                
                rsMax.close();
                pst3.close();
                
                String sqlAlter = "ALTER TABLE cursos AUTO_INCREMENT = ?";
                PreparedStatement pst4 = conn.prepareStatement(sqlAlter);
                pst4.setInt(1, maxId + 1);
                pst4.executeUpdate();
                pst4.close();
                
                
                System.out.println("Curso con nombre " + nombre + " eliminado correctamente.");
            } else {
                System.out.println("No se encontró el curso con el nombre: " + nombre);
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    
    
    public void eliminarAsignacion(String profesor, String nombreCurso) throws Exception{
        if (conn == null || conn.isClosed()) {
            conn = conexion.getConnection();
        }
        
        String[] tablaProfesor = profesor.split(" ");
        String nombre = tablaProfesor[0];
        String apellido = tablaProfesor[1];
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try{
            //Obtenemos el id del profesor para luego asignarselo a la tabla de asignaciones
            String consulta1 = "SELECT id_profesor FROM profesores WHERE nombre = ? AND apellido = ?";
            stmt = conn.prepareStatement(consulta1);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            
            rs = stmt.executeQuery();
            int profesorId = -1;
            
            if(rs.next()){
                profesorId = rs.getInt("id_profesor");
            }
            
            rs.close();
            stmt.close();
            
            //Obtenemos el id del curso para asignarlo cunto a la del profesor en la tabla dfe asignaciones
            String consulta2 = "SELECT id_curso FROM cursos WHERE nombre = ?";
            stmt = conn.prepareStatement(consulta2);
            stmt.setString(1, nombreCurso);
            
            rs = stmt.executeQuery();
            int cursoId = -1;
            
            if(rs.next()){
                cursoId = rs.getInt("id_curso");
            }
            
            rs.close();
            stmt.close();
            
            
            //Insertamos los datos en la tabla de asignaciones
            String deleteQuery = "DELETE FROM asignaciones WHERE id_profesor = ? AND id_curso = ?";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setInt(1, profesorId);
            stmt.setInt(2, cursoId);
            
            stmt.executeUpdate();
            
            //Reconfiguramos los ids
            String getMaxIdSql = "SELECT MAX(id_asignacion) FROM asignaciones";
            PreparedStatement pst3 = conn.prepareStatement(getMaxIdSql);
            ResultSet rsMax = pst3.executeQuery();

            int maxId = 0;
            if (rsMax.next()) {
                maxId = rsMax.getInt(1);
            }
            rsMax.close();
            pst3.close();
                
            String sqlAlter = "ALTER TABLE asignaciones AUTO_INCREMENT = ?";
            PreparedStatement pst4 = conn.prepareStatement(sqlAlter);
            pst4.setInt(1, maxId + 1); // Aseguramos que el próximo valor será mayor
            pst4.executeUpdate();
            pst4.close();
            
            
            System.out.println("Profesor eliminado del curso");
            
            
        }catch (SQLException exc){
            throw new Exception("Error al eliminar el profesor.");
        }
        
    }
    
    
    
    public ObservableList<Curso> getCursos() {
        ObservableList<Curso> obs = FXCollections.observableArrayList();
        try {
            //Creación de la conexión y ejecución de la consulta
            
            String sql = "SELECT c.id_curso, c.nombre, c.descripcion, c.duracion_horas, p.nombre AS nombreProfesor, p.apellido AS apellidoProfesor FROM cursos c JOIN asignaciones a ON c.id_curso = a.id_curso JOIN profesores p ON p.id_profesor = a.id_profesor";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            

            // recorro los resultados y guardo los datos en variables
            while (rs.next()) {
                Integer idCurso = rs.getInt("id_curso");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                Integer duracion = rs.getInt("duracion_horas");
                String profesor = rs.getString("nombreProfesor") + " " + rs.getString("apellidoProfesor");
                
                
                // Creo el cliente con los datos recogidos
                Curso c = new Curso(idCurso, nombre, descripcion, duracion, profesor);

                //Lo añado a la lista
                obs.add(c);
            }

            rs.close();
            stmt.close();
            conexion.cerrarConexion();

        } catch (SQLException ex) {
            Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obs;
    }
    
    public ObservableList<String> mostrarNombres(){
        
        ObservableList<String> nombres = FXCollections.observableArrayList();
        
        try{
            
            String sql = "SELECT nombre FROM cursos";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String nombre = rs.getString("nombre");
                
                nombres.add(nombre);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return nombres;
    }
    
    public ObservableList<String> mostrarNombreProfesores(){
        
        ObservableList<String> profesores = FXCollections.observableArrayList();
        
        try{
            
            String sql = "SELECT nombre, apellido FROM profesores";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                
                String concat = nombre + " " + apellido;
                
                profesores.add(concat);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return profesores;
    }
    
    
}
