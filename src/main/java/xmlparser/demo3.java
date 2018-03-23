package xmlparser;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;



public class demo3 {

	  public static void main(String args[]) 
      {  
    
    	  JSONArray jArrays = new JSONArray();
  		try {
  			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
  			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=practice;" + "user=sa;"
  					+ "password=rohit@crosstab";
  			Connection con = DriverManager.getConnection(connectionUrl);
  			System.out.println("Connected.");
  			Statement st = (Statement) con.createStatement();
  			ResultSet rs = st.executeQuery("select photo from testinomial;");

  			while (rs.next()) {
  				byte[] bytes = rs.getBytes("photo");
  				byte[] encoded = Base64.encodeBase64(bytes);
  				String encodedString = new String(encoded);
  		
  				System.out.println(encodedString);
  				

  				
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		System.out.println(jArrays.toString());
      }
      }

