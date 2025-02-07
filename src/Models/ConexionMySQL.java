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
    private Statement statement;

    public ConexionMySQL(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        
        conectar();
    }

    private void conectar() {
        try{
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            String url = "jdbc:mysql://" + host + "/" + database + "?serverTimezone=UTC";
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            
            System.out.println("Se ha establecido la conexión");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    public ResultSet ejecutarConsulta(String consulta){
        try{
            return statement.executeQuery(consulta);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    public void cerrarConexion(){
        try{
            
        if (statement != null) statement.close();
        if (connection != null) connection.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

   
    
    
}
