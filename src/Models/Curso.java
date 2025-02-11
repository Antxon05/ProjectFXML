/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.sql.Connection;

/**
 *
 * @author antxon
 */
public class Curso {
    
    private Integer id_curso;
    private String nombre;
    private String descripcion;
    private Integer duracion_horas;
    
    private ConexionMySQL conexion = new ConexionMySQL("localhost:3307", "codingacademy_database", "root", "");
    private Connection conn = this.conexion.getConnection();

    public Curso() {
    }

    public Curso(Integer id_curso, String nombre, String descripcion, Integer duracion_horas) {
        this.id_curso = id_curso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion_horas = duracion_horas;
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

    @Override
    public String toString() {
        return "Curso{" + "id_curso=" + id_curso + ", nombre=" + nombre + ", descripcion=" + descripcion + ", duracion_horas=" + duracion_horas + '}';
    }
    
    
    
    
    
    
    
}
