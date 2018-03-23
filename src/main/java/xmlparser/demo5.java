package xmlparser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class demo5 {

  public static void main(String argv[]) {
	  JSONObject jObject = new JSONObject();
		JSONArray jArrays = new JSONArray();
		try {
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=jubilant;" + "user=sa;"
					+ "password=rohit@crosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			Statement st = con.createStatement();
			JSONObject jObjects = new JSONObject();
			ResultSet rs1 = st.executeQuery("select * from emp_details_basic");			
			while (rs1.next()) {
				jObjects.put("name_type", rs1.getString("NAME_TYPE"));
				jObjects.put("first_name", rs1.getString("FIRST_NAME"));
				jObjects.put("last_name", rs1.getString("LAST_NAME"));
			}		ResultSet rs2 = st.executeQuery("select * from emp_details_address");
			while (rs2.next()) {
				jObjects.put("addr2", rs2.getString("ADDRESS2"));
				jObjects.put("addr3", rs2.getString("ADDRESS3"));
				jObjects.put("addr4", rs2.getString("ADDRESS4"));
				jObjects.put("country", rs2.getString("COUNTRY"));
				jObjects.put("state", rs2.getString("STATE"));
				jObjects.put("city", rs2.getString("CITY"));
				jObjects.put("postal", rs2.getString("POSTAL"));
			}
			ResultSet rs3 = st.executeQuery("select * from emp_details_contact");
			while (rs3.next()) {
				jObjects.put("country_code", rs3.getString("COUNTRY_CODE"));
				jObjects.put("phone", rs3.getString("PHONE"));
				jObjects.put("email", rs3.getString("EMAIL_ADDR"));
			}
			ResultSet rs4 = st.executeQuery("select * from emp_details_basic_2");
			while (rs4.next()) {
				jObjects.put("exp_year", rs4.getString("J_EXP_YEAR"));
				jObjects.put("exp_month", rs4.getString("J_EXP_MONTH"));				
				jObjects.put("post_grad", rs4.getString("J_POST_GRID"));
				jObjects.put("grad", rs4.getString("J_GRADUATION"));
				jObjects.put("industry", rs4.getString("J_INDUSTRY"));
				jObjects.put("functional_area", rs4.getString("J_FUNCTIONAL_AREA"));
			}
			ResultSet rs5 = st.executeQuery("select * from emp_details_works");

			while (rs5.next()) {
				jObjects.put("company", rs5.getString("EMPLOYER"));
			jObjects.put("jobtitle", rs5.getString("ENDING_TITLE"));
			jObjects.put("start_date", rs5.getString("START_DT"));
				jObjects.put("end_date", rs5.getString("END_DT"));
				jObjects.put("salary", rs5.getString("J_CURRENT_SALARY"));
				jObjects.put("currency", rs5.getString("CURRENCY"));
			}
		ResultSet rs6 = st.executeQuery("select * from emp_details_edu");
		JSONArray jarr=new JSONArray();
while (rs6.next()) {
	
	JSONObject objedu=new JSONObject();
	objedu.put("cat_type", rs6.getString("JPM_CAT_TYPE"));
	objedu.put("institution", rs6.getString("SCHOOL_CODE"));
	objedu.put("passing_year", rs6.getString("JPM_INTEGER_2"));
	objedu.put("country_edu", rs6.getString("COUNTRY_EDU"));
	objedu.put("course_type", rs6.getString("TYPE_OF_STUDY_GER"));
	objedu.put("degree", rs6.getString("JPM_CAT_ITEM_ID"));
	objedu.put("branch", rs6.getString("MAJOR_CODE"));
	objedu.put("cgpa", rs6.getString("JPM_DECIMAL_1"));
	jarr.put(objedu);
				
				
			}

jObject.put("edu",jarr);
jObject.put("basic",jObjects);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(jObject.toString());
	
	

  }
}