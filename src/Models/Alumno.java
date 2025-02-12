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

public class Alumno {

    private Integer id_alumno;
    private String nombre;
    private String apellido;
    private String correo;
    private Integer telefono;
    private LocalDate fechaRegistro;
    private String curso;

    private ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
    private Connection conn = this.conexion.getConnection();

    public Alumno() {
    }

    public Alumno(Integer id_alumno, String nombre, String apellido, String correo, Integer telefono, LocalDate fechaRegistro, String curso) {
        this.id_alumno = id_alumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.curso = curso;
    }

    // Getter y setter de las propiedades del alumno.
    public Integer getId_alumno() { return id_alumno; }
    public void setId_alumno(Integer id_alumno) { this.id_alumno = id_alumno; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Integer getTelefono() { return telefono; }
    public void setTelefono(Integer telefono) { this.telefono = telefono; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    // Método para mostrar la información del alumno como texto.
    @Override
    public String toString() {
        return "Alumno{" + "id_alumno=" + id_alumno + ", nombre=" + nombre + ", apellido=" + apellido + ", correo=" + correo + ", telefono=" + telefono + ", fechaRegistro=" + fechaRegistro + ", curso=" + curso + '}';
    }

    // Método para obtener todos los alumnos registrados y devolverlos en una lista observable.
    public ObservableList<Alumno> getAlumnos() {
        ObservableList<Alumno> obs = FXCollections.observableArrayList();
        try {
            // Consulta SQL para obtener los datos de los alumnos con los cursos que están inscritos.
            String sql = "SELECT a.id_alumno, a.nombre, a.apellido, a.correo, a.telefono, a.fecha_registro, c.nombre AS nombreCurso FROM alumnos a JOIN inscripciones i ON a.id_alumno = i.id_alumno JOIN cursos c ON c.id_curso = i.id_curso";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Iteramos sobre los resultados y creamos un nuevo objeto Alumno con los datos.
            while (rs.next()) {
                Integer idAlumno = rs.getInt("id_alumno");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                Integer telefono = rs.getInt("telefono");
                LocalDate fechaRegistro = rs.getDate("fecha_registro").toLocalDate();
                String curso = rs.getString("nombreCurso");

                Alumno a = new Alumno(idAlumno, nombre, apellido, correo, telefono, fechaRegistro, curso);
                obs.add(a); // Agregamos el alumno a la lista observable
            }
            rs.close();
            stmt.close();
            conexion.cerrarConexion();

        } catch (SQLException ex) {
            Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obs;
    }

    // Método para mostrar los nombres completos de los alumnos.
    public ObservableList<String> mostrarAlumnos() {
        ObservableList<String> nombres = FXCollections.observableArrayList();
        try {
            String concat = "";
            String sql = "SELECT nombre, apellido FROM alumnos";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                concat = nombre + " " + apellido;
                nombres.add(concat); // Agregamos el nombre completo a la lista
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nombres;
    }

    // Método para mostrar los nombres de los cursos disponibles.
    public ObservableList<String> mostrarCursos() {
        ObservableList<String> nombres = FXCollections.observableArrayList();
        try {
            String sql = "SELECT nombre FROM cursos";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                nombres.add(nombre); // Agregamos el nombre del curso a la lista
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nombres;
    }

    // Método para añadir un nuevo alumno a la base de datos.
    public void añadirAlumno(String nombre, String apellido, String correo, Integer telefono, LocalDate fecha) throws Exception {
        try {
            // Verificamos que la conexión sea válida
            if (conn == null || conn.isClosed()) {
                conn = conexion.getConnection();
            }
            String sql = "INSERT INTO alumnos (nombre, apellido, correo, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Asignamos los valores a la consulta
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, correo);
            stmt.setInt(4, telefono);
            stmt.setDate(5, java.sql.Date.valueOf(fecha));

            stmt.executeUpdate(); // Ejecutamos la inserción
            stmt.close();

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new Exception("El correo ya esta registrado.");
            } else {
                throw new Exception("Error al registrar el alumno.");
            }
        }
    }

    // Método para asignar un alumno a un curso.
    public void añadirAlumnoInscripcion(String nombre, String apellido, String nombreCurso) throws Exception {
        if (conn == null || conn.isClosed()) {
            conn = conexion.getConnection();
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Obtenemos el ID del alumno a partir de su nombre y apellido
            String consulta1 = "SELECT id_alumno FROM alumnos WHERE nombre = ? AND apellido = ?";
            stmt = conn.prepareStatement(consulta1);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            rs = stmt.executeQuery();
            int alumnoId = -1;

            if (rs.next()) {
                alumnoId = rs.getInt("id_alumno");
            }
            rs.close();
            stmt.close();

            // Obtenemos el ID del curso a partir de su nombre
            String consulta2 = "SELECT id_curso FROM cursos WHERE nombre = ?";
            stmt = conn.prepareStatement(consulta2);
            stmt.setString(1, nombreCurso);
            rs = stmt.executeQuery();
            int cursoId = -1;

            if (rs.next()) {
                cursoId = rs.getInt("id_curso");
            }
            rs.close();
            stmt.close();

            // Insertamos la relación alumno-curso en la tabla de inscripciones
            String insertQuery = "INSERT INTO inscripciones (id_alumno, id_curso) VALUES (?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setInt(1, alumnoId);
            stmt.setInt(2, cursoId);
            stmt.executeUpdate();

            System.out.println("Alumno asignado al curso");

        } catch (SQLException exc) {
            throw new Exception("Error al asignar el alumno.");
        }
    }

    // Método para eliminar un alumno de la base de datos.
    public void eliminarAlumno(Integer idAlumno, String curso) {
        try {
            eliminarInscripcion(curso, idAlumno);

            if (conn == null || conn.isClosed()) {
                conn = conexion.getConnection();
            }

            // Eliminamos el alumno
            String sqlDelete = "DELETE FROM alumnos WHERE id_alumno = ?";
            PreparedStatement pst2 = conn.prepareStatement(sqlDelete);
            pst2.setInt(1, idAlumno);
            pst2.executeUpdate();
            pst2.close();

            // Reajustamos el auto incremento del ID de los alumnos
            String getMaxIdSql = "SELECT MAX(id_alumno) FROM alumnos";
            PreparedStatement pst3 = conn.prepareStatement(getMaxIdSql);
            ResultSet rsMax = pst3.executeQuery();

            int maxId = 0;
            if (rsMax.next()) {
                maxId = rsMax.getInt(1);
            }
            rsMax.close();
            pst3.close();

            String sqlAlter = "ALTER TABLE alumnos AUTO_INCREMENT = ?";
            PreparedStatement pst4 = conn.prepareStatement(sqlAlter);
            pst4.setInt(1, maxId + 1); // Aseguramos que el próximo valor será mayor
            pst4.executeUpdate();
            pst4.close();

            System.out.println("Alumno eliminado correctamente con id = " + idAlumno);
            conexion.cerrarConexion();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar la inscripción de un alumno en un curso.
    public void eliminarInscripcion(String nombreCurso, Integer idAlumno) throws Exception {
        if (conn == null || conn.isClosed()) {
            conn = conexion.getConnection();
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Obtenemos el ID del curso
            String consulta1 = "SELECT id_curso FROM cursos WHERE nombre = ?";
            stmt = conn.prepareStatement(consulta1);
            stmt.setString(1, nombreCurso);
            rs = stmt.executeQuery();
            int cursoId = -1;

            if (rs.next()) {
                cursoId = rs.getInt("id_curso");
            }
            rs.close();
            stmt.close();

            // Eliminamos la inscripción
            String deleteQuery = "DELETE FROM inscripciones WHERE id_alumno = ? AND id_curso = ?";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, cursoId);
            stmt.executeUpdate();

            // Reajustamos el auto incremento de las inscripciones
            String getMaxIdSql = "SELECT MAX(id_inscripcion) FROM inscripciones";
            PreparedStatement pst3 = conn.prepareStatement(getMaxIdSql);
            ResultSet rsMax = pst3.executeQuery();

            int maxId = 0;
            if (rsMax.next()) {
                maxId = rsMax.getInt(1);
            }
            rsMax.close();
            pst3.close();

            String sqlAlter = "ALTER TABLE inscripciones AUTO_INCREMENT = ?";
            PreparedStatement pst4 = conn.prepareStatement(sqlAlter);
            pst4.setInt(1, maxId + 1); // Aseguramos que el próximo valor será mayor
            pst4.executeUpdate();
            pst4.close();

            System.out.println("Inscripción eliminada correctamente");

        } catch (SQLException exc) {
            throw new Exception("Error al eliminar la inscripción.");
        }
    }

    // Método para actualizar los datos de un alumno.
    public void updateAlumno(Integer idAlumno, String nombre, String apellido, String correo, Integer telefono, LocalDate fecha, String nombreCurso) throws Exception {
        try {
            if (conn == null || conn.isClosed()) {
                conn = conexion.getConnection();
            }

            updateInscripcion(idAlumno, nombreCurso);

            String updateSql = "UPDATE alumnos SET nombre = ?, apellido = ?, correo = ?, telefono = ?, fecha_registro = ? WHERE id_alumno = ?";
            PreparedStatement pst2 = conn.prepareStatement(updateSql);

            pst2.setString(1, nombre);
            pst2.setString(2, apellido);
            pst2.setString(3, correo);
            pst2.setInt(4, telefono);
            pst2.setDate(5, java.sql.Date.valueOf(fecha));
            pst2.setInt(6, idAlumno);

            pst2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error en la base de datos");
        }
    }

    // Método para actualizar la inscripción de un alumno en un curso.
    public void updateInscripcion(Integer idAlumno, String nombreCurso) throws SQLException, Exception {
        if (conn == null || conn.isClosed()) {
            conn = conexion.getConnection();
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Obtenemos el ID del curso
            String consulta1 = "SELECT id_curso FROM cursos WHERE nombre = ?";
            stmt = conn.prepareStatement(consulta1);
            stmt.setString(1, nombreCurso);
            rs = stmt.executeQuery();
            int cursoId = -1;

            if (rs.next()) {
                cursoId = rs.getInt("id_curso");
            }
            rs.close();
            stmt.close();

            // Actualizamos la inscripción
            String updateQuery = "UPDATE inscripciones SET id_curso = ? WHERE id_alumno = ?";
            stmt = conn.prepareStatement(updateQuery);
            stmt.setInt(1, cursoId);
            stmt.setInt(2, idAlumno);

            stmt.executeUpdate();

            System.out.println("Alumno actualizado en el curso");

        } catch (SQLException exc) {
            throw new Exception("Error al actualizar la inscripción del alumno.");
        }
    }
}
