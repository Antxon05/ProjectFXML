/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;


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
public class Alumno {
    
    
    
    private Integer id_alumno;
    private String nombre;
    private String apellido;
    private String correo;
    private Integer telefono;
    private LocalDate fechaRegistro;

    public Alumno() {
    }

    public Alumno(Integer id_alumno, String nombre, String apellido, String correo, Integer telefono, LocalDate fechaRegistro) {
        this.id_alumno = id_alumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getId_alumno() {
        return id_alumno;
    }

    public void setId_alumno(Integer id_alumno) {
        this.id_alumno = id_alumno;
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

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Alumno{" + "id_alumno=" + id_alumno + ", nombre=" + nombre + ", apellido=" + apellido + ", correo=" + correo + ", telefono=" + telefono + ", fechaRegistro=" + fechaRegistro + '}';
    }
    
    
    public ObservableList<Alumno> getAlumnos() {
        ObservableList<Alumno> obs = FXCollections.observableArrayList();
        try {

            
            //Creación de la conexión y ejecución de la consulta
            ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
            ResultSet rs = conexion.ejecutarConsulta("SELECT * FROM alumnos");

            // recorro los resultados y guardo los datos en variables
            while (rs.next()) {
                Integer idAlumno = rs.getInt("id_alumno");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                Integer telefono = rs.getInt("telefono");
                LocalDate fechaRegistro = rs.getDate("fecha_registro").toLocalDate();
                
                // Creo el cliente con los datos recogidos
                Alumno a = new Alumno(idAlumno, nombre, apellido, correo, telefono, fechaRegistro);

                //Lo añado a la lista
                obs.add(a);

            }

            // Cierro la conexion
            conexion.cerrarConexion();

        } catch (SQLException ex) {
            Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obs;
    }
    
    
    public void añadirAlumno(){
        
        try{
            ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
            String sql = "INSERT INTO alumnos (nombre, apellido, correo, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = conexion.preparedStatement();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
}
