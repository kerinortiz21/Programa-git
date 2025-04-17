/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ejemploconexionjdbc;

/**
 *
 * @author Kerin
 */

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EjemploConexionJDBC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
     String usuario = "root";
     String password = "";
     String url = "jdbc:mysql://localhost:3306/login_register_db";
     Connection conexion;
     Statement statement;
     ResultSet rs;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            
            
            
// TODO code application logic here
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EjemploConexionJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try{
      conexion = DriverManager.getConnection(url,usuario,password);
      statement = conexion.createStatement();
      rs = statement.executeQuery("SELECT * FROM usuarios"); /* Esto permite seleccionar el dato nombre de la tabla usuarios*/
      while(rs.next()){
          
          System.out.println(rs.getString("nombre"));
      }
      
      /* Insercion de datos */
      
      statement.execute("INSERT INTO `usuarios` (`id`, `nombre`, `correo`, `password`) VALUES (NULL,'ChanchitoFeliz','example@gmail.com','abcd123');"); /*Inserta un nuevo usuario*/
      System.out.println("");/*Para dejar un espacio entre los print*/
      rs = statement.executeQuery("SELECT * FROM usuarios"); /* Verificar el usuario chanchito feliz*/
      while(rs.next()){
          
          System.out.println(rs.getString("nombre"));
      }

      
      /*Actualizacion de datos*/
      
      statement.execute("UPDATE `usuarios` SET `nombre` = 'Amada Barcelo' WHERE `usuarios`.`id` =17;"); /*Actualizar datos cuando el id del usuario sea 17.*/
      System.out.println("");/*Para dejar un espacio entre los print*/
      rs = statement.executeQuery("SELECT * FROM usuarios"); /* Verificar el usuario chanchito feliz*/
      while(rs.next()){
          
          System.out.println(rs.getString("nombre"));
      }

      /*Eliminar registros*/
      statement.execute("DELETE FROM `usuarios` WHERE `usuarios`.`id` = 19;"); /*Elimina al usuario con id 19*/
      System.out.println(""); /*Para dejar un espacio entre los print*/
      rs = statement.executeQuery("SELECT * FROM usuarios"); /*Accede a la tabla usuario para que se cumpla el execute*/
      while(rs.next()){
          
          System.out.println(rs.getString("nombre"));
      }
      
        } catch (SQLException ex){
            
            Logger.getLogger(EjemploConexionJDBC.class.getName()).log(Level.SEVERE,null,ex);
            
            
        }
        
        
        
    }
    
}
