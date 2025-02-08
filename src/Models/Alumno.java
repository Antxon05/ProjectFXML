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
public class Alumno {
    
    
    
    private Integer id_alumno;
    private String nombre;
    private String apellido;
    private String correo;
    private Integer telefono;
    private LocalDate fechaRegistro;
    
    private ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
    private Connection conn = this.conexion.getConnection();

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
            Connection conn = conexion.getConnection();
            
            String sql = "SELECT * FROM alumnos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            

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
            
            String sql = "SELECT nombre FROM alumnos";
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
    
    public ObservableList<String> mostrarApellidos(){
        
        ObservableList<String> apellidos = FXCollections.observableArrayList();
        
        try{
            
            String sql = "SELECT apellido FROM alumnos";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String apellido = rs.getString("apellido");
                
                apellidos.add(apellido);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return apellidos;
    }
    
    
    public void añadirAlumno(String nombre, String apellido, String correo, Integer telefono, LocalDate fecha){
        
        try{
            ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
            Connection conn = conexion.getConnection();
            String sql = "INSERT INTO alumnos (nombre, apellido, correo, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?)";
            
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, correo);
            stmt.setInt(4, telefono);
            stmt.setDate(5, java.sql.Date.valueOf(fecha));
            
            
            stmt.executeUpdate();
            
            stmt.close();
            conexion.cerrarConexion();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void eliminarAlumno(String correo){
        try{
            
            ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
            Connection conn = conexion.getConnection();
            
            String consulta = "SELECT id_alumno FROM alumnos WHERE correo = ?";
            PreparedStatement pst1 = conn.prepareStatement(consulta);
            pst1.setString(1, correo);
            ResultSet rs = pst1.executeQuery();
            
            if(rs.next()){
                Integer id = rs.getInt("id_alumno");
                
                rs.close();
                pst1.close();
                
                String sqlDelete = "DELETE FROM alumnos WHERE id_alumno = ?";
                PreparedStatement pst2 = conn.prepareStatement(sqlDelete);
                pst2.setInt(1, id);
                pst2.executeUpdate();
                pst2.close();
                
                //Toda esta operación sirve para que el id se auto incremente despues del mayor id existente
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
                
                System.out.println("Se ha eliminado correctamente el usuario con id = " + id);
            }else{
                System.out.println("No se encontro ningun alumno con ese correo");
            }
            
            conexion.cerrarConexion();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void updateAlumno(String nombre, String apellido, String correo, Integer telefono, LocalDate fecha){
        
        try{
            
            ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
            Connection conn = conexion.getConnection();
            
            String verificarSql = "SELECT id_alumno FROM alumnos WHERE correo = ?";
            PreparedStatement pst1 = conn.prepareStatement(verificarSql);
            pst1.setString(1, correo);
            ResultSet rs = pst1.executeQuery();
            
            if(rs.next()){
                Integer id = rs.getInt("id_alumno");
                
                rs.close();
                pst1.close();
                
                
                String updateSql = "UPDATE alumnos SET nombre = ?, apellido = ?, correo = ?, telefono = ?, fecha_registro = ? WHERE id_alumno = ?";
                PreparedStatement pst2 = conn.prepareStatement(updateSql);
                
                pst2.setString(1, nombre);
                pst2.setString(2, apellido);
                pst2.setString(3, correo);
                pst2.setInt(4, telefono);
                pst2.setDate(5, java.sql.Date.valueOf(fecha));
                pst2.setInt(6, id);
                
                pst2.executeUpdate();
                
                pst2.close();
            }
            
            
            conexion.cerrarConexion();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    
}
