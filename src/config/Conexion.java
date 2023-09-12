package config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    static Connection conexion = null;
    static String usuario="root";
    static String password="";
    static String url="jdbc:mysql://localhost:3306/autotransportesv1";
    
    public Connection conectar(){
        try {
            if(conexion == null){
                
                conexion = DriverManager.getConnection(url,usuario,password);
                System.out.println("Conexion correcta a MySQL");
                
            }
        } catch (SQLException e) {
            
            System.out.println("Error de conexion. Error: "+e);
            
        }
        return conexion;
    }
    
    
}
