package xmlparser;



import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import com.mysql.jdbc.Statement;

public class demo3 {

	  public static void main(String args[]) 
      {  
      try  
      { 
           // Load the SQLServerDriver class, build the 
           // connection string, and get a connection 
           Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
           String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + 
                                   "database=practice;" + 
                                   "user=sa;" + 
                                   "password=rohit@crosstab"; 
           Connection con = DriverManager.getConnection(connectionUrl); 
           System.out.println("Connected.");

           // Create and execute an SQL statement that returns some data.  
           String SQL = "SELECT * FROM trial";  
           java.sql.Statement stmt = con.createStatement();  
           ResultSet rs = stmt.executeQuery(SQL);

           // Iterate through the data in the result set and display it.  
           while (rs.next())  
           {  
              System.out.println(rs.getString(1) + " " + rs.getString(2));  
           }

      }  
      catch(Exception e)  
      { 
           System.out.println(e.getMessage()); 
           System.exit(0);  
      } 
   } 
}