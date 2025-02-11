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
public class Profesor {
    
    private Integer id_profesor;
    private String nombre;
    private String apellido;
    private String correo;

    private ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
    private Connection conn = this.conexion.getConnection();

    public Profesor() {
        
    }
    
    
    public Profesor(Integer id_profesor, String nombre, String apellido, String correo) {
        this.id_profesor = id_profesor;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
    }

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

    @Override
    public String toString() {
        return "Profesor{" + "id_profesor=" + id_profesor + ", nombre=" + nombre + ", apellido=" + apellido + ", correo=" + correo + '}';
    }
    
    
     public ObservableList<Profesor> getProfesores() {
        ObservableList<Profesor> obs = FXCollections.observableArrayList();
        try {
            //Creación de la conexión y ejecución de la consulta
            
            String sql = "SELECT * FROM profesores";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            

            // recorro los resultados y guardo los datos en variables
            while (rs.next()) {
                Integer idProfesor = rs.getInt("id_profesor");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                
                // Creo el cliente con los datos recogidos
                Profesor p = new Profesor(idProfesor, nombre, apellido, correo);

                //Lo añado a la lista
                obs.add(p);

            }

            
            
            rs.close();
            stmt.close();
            conexion.cerrarConexion();

        } catch (SQLException ex) {
            Logger.getLogger(Profesor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obs;
    }
     
     public ObservableList<String> mostrarNombres(){
        
        ObservableList<String> nombres = FXCollections.observableArrayList();
        
        try{
            
            String sql = "SELECT nombre FROM profesores";
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
            
            String sql = "SELECT apellido FROM profesores";
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
    
    public void añadirProfesor(String nombre, String apellido, String correo) throws Exception{
        
        try{
            ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
            Connection conn = conexion.getConnection();
            String sql = "INSERT INTO profesores (nombre, apellido, correo) VALUES (?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, correo);
            
            
            stmt.executeUpdate();
            
            stmt.close();
            conexion.cerrarConexion();
            
        }catch(SQLException e){
            if(e.getMessage().contains("Duplicate entry")){
                throw new Exception("El correo ya esta registrado.");
            } else{
                throw new Exception("Error al registrar el profesor.");
            }
        }
    }
    
    public void updateProfesor(String nombre, String apellido, String correo){
        try{
            
            ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
            Connection conn = conexion.getConnection();
            
            //Verificamos con el correo ya que es una clave única y no se puede repetir
            String verificarSql = "SELECT id_profesor FROM profesores WHERE correo = ?";
            PreparedStatement pst1 = conn.prepareStatement(verificarSql);
            pst1.setString(1, correo);
            ResultSet rs = pst1.executeQuery();
            
            if(rs.next()){
                Integer id = rs.getInt("id_profesor");
                
                rs.close();
                pst1.close();
                
                
                String updateSql = "UPDATE profesores SET nombre = ?, apellido = ?, correo = ? WHERE id_profesor = ?";
                PreparedStatement pst2 = conn.prepareStatement(updateSql);
                
                pst2.setString(1, nombre);
                pst2.setString(2, apellido);
                pst2.setString(3, correo);
                pst2.setInt(4, id);
                
                pst2.executeUpdate();
                
                pst2.close();
            }
            
            
            conexion.cerrarConexion();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void eliminarProfesor(String correo){
        try{
            
            ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
            Connection conn = conexion.getConnection();
            
            String consulta = "SELECT id_profesor FROM profesores WHERE correo = ?";
            PreparedStatement pst1 = conn.prepareStatement(consulta);
            pst1.setString(1, correo);
            ResultSet rs = pst1.executeQuery();
            
            if(rs.next()){
                Integer id = rs.getInt("id_profesor");
                
                rs.close();
                pst1.close();
                
                String sqlDelete = "DELETE FROM profesores WHERE id_profesor = ?";
                PreparedStatement pst2 = conn.prepareStatement(sqlDelete);
                pst2.setInt(1, id);
                pst2.executeUpdate();
                pst2.close();
                
                //Toda esta operación sirve para que el id se auto incremente despues del mayor id existente
                String getMaxIdSql = "SELECT MAX(id_profesor) FROM profesores";
                PreparedStatement pst3 = conn.prepareStatement(getMaxIdSql);
                ResultSet rsMax = pst3.executeQuery();

                int maxId = 0;
                if (rsMax.next()) {
                    maxId = rsMax.getInt(1);
                }
                rsMax.close();
                pst3.close();
                
                String sqlAlter = "ALTER TABLE profesores AUTO_INCREMENT = ?";
                PreparedStatement pst4 = conn.prepareStatement(sqlAlter);
                pst4.setInt(1, maxId + 1); // Aseguramos que el próximo valor será mayor
                pst4.executeUpdate();
                pst4.close();
                
                System.out.println("Se ha eliminado correctamente el profesor con id = " + id);
            }else{
                System.out.println("No se encontró ningun profesor con ese correo");
            }
            
            conexion.cerrarConexion();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
}
