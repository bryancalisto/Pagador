/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oracle.net.aso.i;



/**
 *
 * @author Fabricio Mero
 * Clase optimizada para utilizarse localmente.
 */
public class ConectorDB {
    
    // Credenciales.
    private String url = "jdbc:sqlserver://FABRICIO\\MSSQLSERVER01:1433;databaseName=RolPAgos;user=sa;password=123;";
    // Objeto de conexion con la base de datos.
    //private Connection con = null;
    
    // Permite conectarse a la base
    private Connection conectar()
    {
        Connection con = null;
        
        try 
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(this.url); 
            
            if (con == null)
            {
                System.out.println("Fallo conexion a DB.");
            }
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println( ex.getMessage( ) );
            return null;
        }
        catch ( SQLException ex ) 
        {
            System.out.println( ex.getMessage( ) );
            return null;
        }
        
        return con;  
    }
    
    // Ejecuta una consulta sql y retorna los datos devueltos por la misma.
    public ResultSet consultar(String query)
    {
        // Contiene la conexion a la BD.
        Connection con = null;
        
        // Contiene lo que devuelve la consulta a la BD.
        ResultSet rs = null;
        
        try
        {
            // Conectamos y ejecutamos consulta.
            con = this.conectar();
            
            // Si la creacion de la conexion falla, retorne null.
            if(con == null)
                return null;
            
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        }
        catch ( SQLException ex ) 
        {
            System.out.println( ex.getMessage( ) );
            return null;
        }
        
        return rs;
    }
    
    
    
    // Ejecuta una consulta sql y retorna los datos devueltos por la misma.
    public boolean modificar(String query)
    {
        // Contiene la conexion a la BD.
        Connection con = null;
        
        try
        {
            // Conectamos y ejecutamos consulta.
            con = this.conectar();
            
            // Si la creacion de la conexion falla, retorne null.
            if(con == null)
                return false;
            
            Statement stmt = con.createStatement();
            if(!stmt.execute(query)){  // si execute devuelve false sabemos que ejecuto correctamente y no devolvio nada.
                return true;            // asi que la funcion devuelve una respuesta afirmativa.
            }
        }
        catch ( SQLException ex ) 
        {
            System.out.println( ex.getMessage( ) );
            return false;
        }
        
        return false;
    }
}
