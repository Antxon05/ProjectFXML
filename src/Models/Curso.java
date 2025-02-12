package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Curso {
    
    // Atributos que representan los datos de un curso
    private Integer id_curso;
    private String nombre;
    private String descripcion;
    private Integer duracion_horas;
    private String profesor;
    
    private ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
    private Connection conn = this.conexion.getConnection();

    // Constructor vacío para crear un curso sin datos iniciales
    public Curso() {
    }

    // Constructor con parámetros para inicializar el curso con los datos proporcionados
    public Curso(Integer id_curso, String nombre, String descripcion, Integer duracion_horas, String profesor) {
        this.id_curso = id_curso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion_horas = duracion_horas;
        this.profesor = profesor;
    }

    // Métodos getter y setter para acceder y modificar los atributos del curso
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
    
    // Método para añadir un nuevo curso a la base de datos
    public void añadirCurso(String nombre, String descripcion, String duracion) throws Exception{
        try {
            // Verifica si la conexión a la base de datos está abierta, si no, la abre
            if (conn == null || conn.isClosed()) {
                conn = conexion.getConnection();
            }

            // Inserta un nuevo curso en la tabla "cursos"
            String sql = "INSERT INTO cursos (nombre, descripcion, duracion_horas) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);  // Asigna el nombre del curso
            stmt.setString(2, descripcion);  // Asigna la descripción del curso
            stmt.setInt(3, Integer.parseInt(duracion));  // Asigna la duración en horas

            // Ejecuta la sentencia de inserción
            stmt.executeUpdate();
            stmt.close();
            
        } catch(SQLException e) {
            throw new Exception("Error al registrar el curso.");  // Si hay error, lanza una excepción
        }
    }
    
    // Método para asignar un profesor a un curso
    public void añadirProfesorAsignacion(String profesor, String nombreCurso) throws Exception{
        if (conn == null || conn.isClosed()) {
            conn = conexion.getConnection();
        }
        
        // Divide el nombre completo del profesor en nombre y apellido
        String[] tablaProfesor = profesor.split(" ");
        String nombre = tablaProfesor[0];
        String apellido = tablaProfesor[1];
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Obtiene el ID del profesor buscando en la tabla "profesores"
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
            
            // Obtiene el ID del curso buscando en la tabla "cursos"
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
            
            // Inserta la relación profesor-curso en la tabla "asignaciones"
            String insertQuery = "INSERT INTO asignaciones (id_profesor, id_curso) VALUES (?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setInt(1, profesorId);  // Asigna el ID del profesor
            stmt.setInt(2, cursoId);  // Asigna el ID del curso
            
            stmt.executeUpdate();
            
            System.out.println("Profesor asignado al curso");
        } catch (SQLException exc) {
            throw new Exception("Error al asignar el profesor.");
        }
    }
    
    // Método para eliminar un curso de la base de datos
    public void eliminarCurso(Integer idCurso, String profesor){
        try {
            // Primero, elimina la asignación del curso con el profesor
            eliminarAsignacion(profesor, idCurso);
            
            // Verifica si la conexión está abierta, si no, la abre
            if (conn == null || conn.isClosed()) {
                conn = conexion.getConnection();
            }
                
            // Elimina el curso de la tabla "cursos"
            String sqlDelete = "DELETE FROM cursos WHERE id_curso = ?";
            PreparedStatement pst1 = conn.prepareStatement(sqlDelete);
            pst1.setInt(1, idCurso);
            pst1.executeUpdate();
            
            // Ajusta el valor del auto incremento para la tabla de cursos
            String getMaxIdSql = "SELECT MAX(id_curso) FROM cursos";
            PreparedStatement pst2 = conn.prepareStatement(getMaxIdSql);
            ResultSet rsMax = pst2.executeQuery();
            
            int maxId = 0;
            if(rsMax.next()){
                maxId = rsMax.getInt(1);
            }
            
            rsMax.close();
            pst2.close();
            
            // Ajusta el valor de auto incremento en la tabla
            String sqlAlter = "ALTER TABLE cursos AUTO_INCREMENT = ?";
            PreparedStatement pst3 = conn.prepareStatement(sqlAlter);
            pst3.setInt(1, maxId + 1);
            pst3.executeUpdate();
            pst3.close();
            
            System.out.println("Curso eliminado correctamente.");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    // Método para eliminar la asignación de un profesor a un curso
    public void eliminarAsignacion(String profesor, Integer idCurso) throws Exception {
        if (conn == null || conn.isClosed()) {
            conn = conexion.getConnection();
        }
        
        // Divide el nombre completo del profesor en nombre y apellido
        String[] tablaProfesor = profesor.split(" ");
        String nombre = tablaProfesor[0];
        String apellido = tablaProfesor[1];
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Obtiene el ID del profesor
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
            
            // Elimina la asignación de ese profesor con el curso
            String deleteQuery = "DELETE FROM asignaciones WHERE id_profesor = ? AND id_curso = ?";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setInt(1, profesorId);
            stmt.setInt(2, idCurso);
            
            stmt.executeUpdate();
            
            // Ajusta el valor de auto incremento para la tabla asignaciones
            String getMaxIdSql = "SELECT MAX(id_asignacion) FROM asignaciones";
            PreparedStatement pst3 = conn.prepareStatement(getMaxIdSql);
            ResultSet rsMax = pst3.executeQuery();
            
            int maxId = 0;
            if (rsMax.next()) {
                maxId = rsMax.getInt(1);
            }
            rsMax.close();
            pst3.close();
                
            // Ajusta el auto incremento
            String sqlAlter = "ALTER TABLE asignaciones AUTO_INCREMENT = ?";
            PreparedStatement pst4 = conn.prepareStatement(sqlAlter);
            pst4.setInt(1, maxId + 1);
            pst4.executeUpdate();
            pst4.close();
            
            System.out.println("Profesor eliminado del curso");
        } catch (SQLException exc) {
            throw new Exception("Error al eliminar el profesor.");
        }
    }
    
    // Método para actualizar los datos de un curso
    public void updateCurso(Integer idCurso, String nombre, String descripcion, Integer duracion, String profesor) throws SQLException, Exception{
        try {
            // Verifica si la conexión está abierta, si no, la abre
            if (conn == null || conn.isClosed()) {
                conn = conexion.getConnection();
            }

            // Actualiza la asignación del curso con el profesor
            updateAsignacion(profesor, idCurso);
            
            // Actualiza los detalles del curso en la base de datos
            String updateSql = "UPDATE cursos SET nombre = ?, descripcion = ?, duracion_horas = ? WHERE id_curso = ?";
            PreparedStatement pst2 = conn.prepareStatement(updateSql);
            pst2.setString(1, nombre);
            pst2.setString(2, descripcion);
            pst2.setInt(3, duracion);
            pst2.setInt(4, idCurso);
            
            int filasActualizadas = pst2.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Curso actualizado correctamente.");
            } else {
                System.out.println("No se encontró el curso a actualizar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error en la base de datos.");
        }
    }
    
    // Método para actualizar la asignación del profesor a un curso
    public void updateAsignacion(String profesor, Integer idCurso) throws SQLException, Exception {
        if (conn == null || conn.isClosed()) {
            conn = conexion.getConnection();
        }
        
        // Divide el nombre completo del profesor en nombre y apellido
        String[] tablaProfesor = profesor.split(" ");
        String nombre = tablaProfesor[0];
        String apellido = tablaProfesor[1];
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Obtiene el ID del profesor
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
            
            // Actualiza la asignación del profesor al curso
            String updateQuery = "UPDATE asignaciones SET id_profesor = ? WHERE id_curso = ?";
            stmt = conn.prepareStatement(updateQuery);
            stmt.setInt(1, profesorId);
            stmt.setInt(2, idCurso);
            
            stmt.executeUpdate();
            
            System.out.println("Profesor actualizado");
        } catch (SQLException exc) {
            throw new Exception("Error al actualizar el profesor.");
        }
    }
    
    // Método para obtener todos los cursos con sus respectivos profesores
    public ObservableList<Curso> getCursos() {
        ObservableList<Curso> obs = FXCollections.observableArrayList();
        try {
            // Consulta SQL para obtener los cursos con el nombre del profesor asignado
            String sql = "SELECT c.id_curso, c.nombre, c.descripcion, c.duracion_horas, p.nombre AS nombreProfesor, p.apellido AS apellidoProfesor FROM cursos c JOIN asignaciones a ON c.id_curso = a.id_curso JOIN profesores p ON p.id_profesor = a.id_profesor";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Recorre los resultados y los guarda en un ObservableList
            while (rs.next()) {
                Integer idCurso = rs.getInt("id_curso");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                Integer duracion = rs.getInt("duracion_horas");
                String profesor = rs.getString("nombreProfesor") + " " + rs.getString("apellidoProfesor");
                
                Curso c = new Curso(idCurso, nombre, descripcion, duracion, profesor);
                obs.add(c);
            }
            rs.close();
            stmt.close();
            conexion.cerrarConexion();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obs;
    }
    
    // Método para obtener los nombres de todos los cursos
    public ObservableList<String> mostrarNombres() {
        ObservableList<String> nombres = FXCollections.observableArrayList();
        
        try {
            // Consulta para obtener los nombres de los cursos
            String sql = "SELECT nombre FROM cursos";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Agrega los nombres a la lista
            while(rs.next()){
                String nombre = rs.getString("nombre");
                nombres.add(nombre);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return nombres;
    }
    
    // Método para obtener los nombres completos de todos los profesores
    public ObservableList<String> mostrarNombreProfesores() {
        ObservableList<String> profesores = FXCollections.observableArrayList();
        
        try {
            // Consulta para obtener los nombres y apellidos de los profesores
            String sql = "SELECT nombre, apellido FROM profesores";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Concatenar el nombre y apellido de los profesores y agregarlo a la lista
            while(rs.next()){
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                
                String concat = nombre + " " + apellido;
                profesores.add(concat);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return profesores;
    }
}
