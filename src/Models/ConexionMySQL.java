/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


class ConexionMySQL {

    private Connection connection;
    private String  host;
    private String database;
    private String user;
    private String password;
    private ResultSet resultSet;

    public ConexionMySQL(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        
        conectar();
    }

    
    //Método para conectarnos a la base de datos que le pasamos los valores en el constructor
    private void conectar() {
        try{
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            String url = "jdbc:mysql://" + host + "/" + database + "?serverTimezone=UTC";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexion abierta.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    //Nos devuelve la conexión
    public Connection getConnection(){
        return connection;
    }
    
    
    
    //Cerramos la conexión
    public void cerrarConexion(){
        try{
            
            if (connection != null){
                connection.close();
                System.out.println("Conexion cerrada.");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

   
    
    
}
