/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author MesutKutlu
 */
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Database {
    public Connection connectToDatabaseOrDie()
  {
    Connection conn = null;
    try
    {
      Class.forName("org.postgresql.Driver");
      String url = "jdbc:postgresql://185.22.184.240:5432/internshiper";
      conn = DriverManager.getConnection(url,"postgres", "1");
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
      System.exit(1);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      System.exit(2);
    }
    return conn;
  }
    public boolean checkExamCode(String exam_code){
           try{
            int count=0;
            
            Connection conn = connectToDatabaseOrDie();
            PreparedStatement ps = null;
            ResultSet rs = null;
        
            ps = conn.prepareStatement("SELECT count(*) FROM job_student where exam_code=?");         
            ps.setString(1,exam_code);
            rs = ps.executeQuery();
            rs.next();
                    
            if(1==rs.getInt("count")){
                conn.close();
                rs.close();
                ps.close();
                return true;            
            }
            else{
                conn.close();
                rs.close();
                ps.close();
                return false;
            }
        }
        catch (SQLException se) {
            System.err.println("Threw a SQLException creating the list of blogs.");
            System.err.println(se.getMessage());
            return false;
        }
       
    }
}
