package database;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class MySQL
{
    Connection db;
    String user;
    String password;
    String TABLE;
    String componentsTable;
   
    public MySQL(String user, String password) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        this.user = user;
        this.password = password;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        db =  (Connection) DriverManager.getConnection("jdbc:mysql://localhost/" + "Uplug"+"?user="+user+"&password="+password);  
    }

    public MySQL() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        this.user = "";
        this.password = "";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        db =  (Connection) DriverManager.getConnection("jdbc:mysql://localhost/" + "Uplug"+"?user="+user+"&password="+password);  
    }
   
    public void printAllData() throws SQLException {        
        Statement stmt;
        stmt = (Statement) db.createStatement();
        
        stmt.executeQuery("SELECT * FROM Powers");
        
        ResultSet rs = stmt.getResultSet ();
        int count = 0;
            
        while (rs.next ()){            
            System.out.println("ID: "+rs.getString("Id"));
            System.out.println("TimeStamp: "+rs.getString("TimeStamp"));
            System.out.println("Power1: "+rs.getString("Power1"));
            System.out.println("Power2: "+rs.getString("Power2"));
        }
       
    }
   
    public void printPower() throws SQLException{
        Statement stmt;
        stmt = (Statement) db.createStatement();
       
        stmt.executeQuery("SELECT Power1 FROM Powers");
        ResultSet rs = stmt.getResultSet ();
        //int count = 0;
        while (rs.next ()){               
            System.out.println("Power1: "+rs.getString("Power1"));         
        }
       
    }
    
    public String getDataTimeInterval(String query) throws SQLException{
        String query_timeInterval=query;
       
        Statement stmt;
        stmt = (Statement) db.createStatement();
       
        stmt.executeQuery(query_timeInterval);
        ResultSet rs = stmt.getResultSet ();
        String interval="";
       
        while (rs.next ()){            
            interval+=(rs.getString("Power1")+'\n');
        }
        
        return interval;
    }
 
    public String getPowerString() throws SQLException{
        Statement stmt;
        stmt = (Statement) db.createStatement();
        stmt.executeQuery("SELECT Power1 FROM Powers");
        ResultSet rs = stmt.getResultSet ();
        String stringPower="";
       
        while (rs.next ()){                   
            stringPower+=(rs.getString("Power1")+'\n');        
        }
        
        return stringPower;
    }
 
    public void insertDataFromSerialPort(float power1, float power2) throws SQLException{
       
        PreparedStatement SQLPreparedStatement;
        ResultSet rsQuery; 

        String SQL;
       
        SQL = "INSERT INTO Uplug.Powers " +
                "(ID ,TimeStamp ,Power1 ,Power2) " +
                "VALUES (NULL ,CURRENT_TIMESTAMP , "
                + power1 +", " 
                + power2 + ")";
       
        //SQLPreparedStatement.executeUpdate();
        Statement stmt;
        stmt = (Statement) db.createStatement();
        stmt.executeUpdate(SQL);       
    }
    
    
    public void insertLabel(int label) throws SQLException{
        String SQL;
      
        SQL = "INSERT INTO Uplug.Labels " +
                "(ID ,TimeStamp ,Label ) " +
                "VALUES (NULL ,CURRENT_TIMESTAMP , "
                + label + ")";
       
        Statement stmt;
        stmt = (Statement) db.createStatement();
        stmt.executeUpdate(SQL);      
    }    
}