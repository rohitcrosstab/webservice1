package xmlparser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

@Path("/test")

public class test {
	private static final String UPLOAD_FOLDER = "D:/";

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello1() {

		return "<html> " + "<title>" + "Hello Jersey" + "</title>" + "<body><h1>" + "Hello Jersey" + "</body></h1>"
				+ "</html> ";
	}

	@SuppressWarnings("unused")
	@GET
	@Path("/test2")
	@Produces(MediaType.TEXT_HTML)
	public String sayXMLHello() {

		String xmlRecords = "<data>" + " <employee>" + "   <name>ajay</name>" + "   <title>singh</title>"
				+ "   <salary>10000</salary>" + " </employee>" + " <employee>" + "   <name>rahul</name>"
				+ "   <title>singh</title>" + "   <salary>10000</salary>" + " </employee>" + " <employee>"
				+ "   <name>ashok</name>" + "   <title>mehra</title>" + "   <salary>10000</salary>" + " </employee>"
				+ " <employee>" + "   <name>abhay</name>" + "   <title>singh</title>" + "   <salary>10000</salary>"
				+ " </employee>" + " <employee>" + "   <name>rajat</name>" + "   <title>kumar</title>"
				+ "   <salary>10000</salary>" + " </employee>" + " <employee>" + "   <name>hari</name>"
				+ "   <title>singh</title>" + "   <salary>10000</salary>" + " </employee>" + " <employee>"
				+ "   <name>vikas</name>" + "   <title>singh</title>" + "   <salary>10000</salary>" + " </employee>"
				+ "</data>";

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlRecords));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("employee");

			// iterate the employees
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				NodeList name = element.getElementsByTagName("name");
				Element line1 = (Element) name.item(0);
				System.out.println("Name: " + getCharacterDataFromElement(line1));

				NodeList title = element.getElementsByTagName("title");
				Element line2 = (Element) title.item(0);
				NodeList salary = element.getElementsByTagName("salary");
				Element line3 = (Element) salary.item(0);
				System.out.println("Title: " + getCharacterDataFromElement(line2));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlRecords;
	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}

	@GET
	@Path("/test3")
	@Produces(MediaType.TEXT_HTML)
	public String sayXMLHello2() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element results = doc.createElement("data");
			doc.appendChild(results);

			Class.forName("com.mysql.jdbc.Driver");

			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/college_project", "root",
					"2309");

			ResultSet rs = conn.createStatement().executeQuery("select * from login");

			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();

			while (rs.next()) {
				Element row = doc.createElement("employee");
				results.appendChild(row);
				for (int i = 1; i <= colCount; i++) {
					String columnName = rsmd.getColumnName(i);
					Object value = rs.getObject(i);
					Element node = doc.createElement(columnName);
					node.appendChild(doc.createTextNode(value.toString()));
					row.appendChild(node);
				}
			}
			DOMSource domSource = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);

			System.out.println(sw.toString());

			conn.close();
			rs.close();
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	@GET
	@Path("/test4")
	@Produces(MediaType.TEXT_HTML)

	public String encodeFileToBase64Binary(String fileName) throws IOException {
		File file = new File("D:/phpmyadmin.pdf");
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);
		return encodedString;
	}

	@SuppressWarnings("resource")
	public static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) { // File is too large
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		is.close();
		return bytes;
	}

	@Path("/upload")
	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.TEXT_HTML })
	@Produces({ MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.TEXT_HTML })
	public ResponseBuilder uploadFile(

			@FormDataParam("myfile") InputStream uploadedInputStream,
			@FormDataParam("myfile") FormDataContentDisposition fileDetail) {

		// check if all form parameters are provided
		if (uploadedInputStream == null || fileDetail == null)
			return Response.status(400);
		// create our destination folder, if it not exists

		try {
			createFolderIfNotExists(UPLOAD_FOLDER);
		} catch (SecurityException se) {
			return Response.status(500);
		}
		String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
		try {
			saveToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(200);
	}

	private void saveToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}

	private void createFolderIfNotExists(String dirName) throws SecurityException {
		File theDir = new File(dirName);
		if (!theDir.exists()) {
			theDir.mkdir();
		}
	}

	@Path("/insert1")
	@POST

	@Produces(MediaType.TEXT_HTML)
	public Response addUser(@FormParam("email") String email, @FormParam("pwd") String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jubilant", "root", "2309");
			String query = ("insert into crud(name,email) VALUES(?,?)");
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, email);
			pstmt.setString(2, pass);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("Data is uploaded successfully to MySQL database").build();

	}

	@Path("/jubilant/emp_details_login")
	@POST

	@Produces(MediaType.TEXT_HTML)
	public Response addUser2(@FormParam("email") String email, @FormParam("pwd") String pass) {
		try {
			Date date = Calendar.getInstance().getTime();

			// Display a date in day, month, year format
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String today = formatter.format(date);
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=jubilant;" + "user=sa;"
					+ "password=rohit@crosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			String query1 = ("insert into emp_details_login(EMAIL_ADDR,PSWD,REG_DATE) VALUES(?,?,?)");
			// String query = ("insert into
			// emp_details_login(EMAIL_ADDR,PSWD,REG_DATE) VALUES(?,?,?)");
			PreparedStatement pstmt = con.prepareStatement(query1);
			pstmt.setString(1, email);
			pstmt.setString(2, pass);
			pstmt.setString(3, today);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("Data is uploaded successfully to sqlexpress database").build();

	}

	@Path("/check")
	@GET

	@Produces(MediaType.TEXT_HTML)
	public String addUser() {
		Date date = Calendar.getInstance().getTime();

		// Display a date in day, month, year format
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String today = formatter.format(date);
		int random_id = (int) Math.random() * 100;
		System.out.println("Today : " + today);
		System.out.println("random_id : " + random_id);
		return today;

	}

	@Path("/redirect")
	@GET

	@Produces(MediaType.TEXT_HTML)
	public Response redir() throws URISyntaxException {
		java.net.URI location = new java.net.URI("http://localhost:8008/xmlparser2/map1.html");
		return Response.temporaryRedirect(location).build();

	}

	@Path("/insertdb1")
	@POST

	@Produces({ MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.TEXT_HTML })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.TEXT_HTML })
	public Response addUser3(@FormDataParam("myfiles") InputStream fileInputStream,
			@FormDataParam("myfiles") FormDataContentDisposition fileInputDetails) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=jubilant;" + "user=sa;"
					+ "password=rohit@crosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			String file = fileInputDetails.getFileName();
			String lang = "eng";
			String title = "test resume";
			String sysfile = "jubilant";
			String query = ("insert into emp_resumes(HRS_RESUME_TITLE,LANG_CD,ATTACHUSERFILE,ATTACHSYSFILENAME,RESUME_TEXT) VALUES(?,?,?,?,?)");
			PreparedStatement pstmt = con.prepareStatement(query);

			pstmt.setString(1, title);
			pstmt.setString(2, lang);
			pstmt.setString(3, file);
			pstmt.setString(4, sysfile);
			pstmt.setBinaryStream(5, fileInputStream);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("Data is uploaded successfully to sqlexpress database").build();
	}

	@Path("/jubilant/emp_details_registration")
	@POST
	@Produces(MediaType.TEXT_HTML)
	public Response addUser3(@FormParam("emails") String email, @FormParam("pwds") String pass)
			throws URISyntaxException {
		try {

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=jubilant;" + "user=sa;"
					+ "password=rohit@crosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");

			String query = ("Select * from emp_details_login where EMAIL_ADDR='" + email + "' and PSWD='" + pass + "'");
			Statement pstmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery(query);
			if (rs.next()) {
				java.net.URI location1 = new java.net.URI("http://localhost:8008/xmlparser/rest/test/redirect");
				return Response.temporaryRedirect(location1).build();
			} else {
				java.net.URI location2 = new java.net.URI("http://localhost:8008/xmlparser2/map1.html");
				return Response.temporaryRedirect(location2).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		java.net.URI location2 = new java.net.URI("http://localhost:8008/xmlparser2/map1.html");
		return Response.temporaryRedirect(location2).build();

	}

	@Path("/jubilant/emp_draft")
	@POST
	@Produces(MediaType.TEXT_HTML)

	public Response addUser45(@FormParam("title") String title, @FormParam("fname") String fname,
			@FormParam("lname") String lname, @FormParam("addr1") String addr1, @FormParam("addr2") String addr2,
			@FormParam("addr3") String addr3, @FormParam("addr4") String addr4, @FormParam("country") String country,
			@FormParam("state") String state, @FormParam("city") String city,
			@FormParam("postalcode") String postalcode, @FormParam("countrycode") String countrycode,
			@FormParam("emailid") String emailid, @FormParam("mobilenumber") String phone,
			@FormParam("current") String currentorg, @FormParam("jobtitle") String jobtitle,
			@FormParam("start_date") String startdt, @FormParam("end_date") String enddt,
			@FormParam("ctc") String salary, @FormParam("curr") String currency,
			@FormParam("workexyears") String expyear, @FormParam("workexmonths") String expmonth,
			@FormParam("mgrad") String postgrad, @FormParam("bgrad") String grad,
			@FormParam("industry") String industry, @FormParam("funct") String functionalarea,
			@FormParam("institutename") String institute, @FormParam("passingyear") String passyear,
			@FormParam("course_type") String coursetype, @FormParam("country") String countryedu,
			@FormParam("degree") String degree, @FormParam("branch") String branch, @FormParam("score") String cgpa,
			@FormParam("edu_arr") String edu_arr, @FormParam("work_arr") String work_arr) {

		try {

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=jubilant;" + "user=sa;"
					+ "password=rohit@crosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			String country_nm_format = "PRI";
			String name_prefix = "001";
			String address_type = "HOME";
			String query1 = ("if exists(select * from emp_details_basic where USER_ID = '1') update emp_details_basic set NAME_TYPE = '"
					+ title + "', FIRST_NAME='" + fname + "', LAST_NAME='" + lname
					+ "' where USER_ID = '1' else insert into emp_details_basic (NAME_TYPE,COUNTRY_NM_FORMAT,NAME_PREFIX, FIRST_NAME,LAST_NAME)values(?,?,?,?,?)");

			PreparedStatement pstmt1 = con.prepareStatement(query1);
			pstmt1.setString(1, title);
			pstmt1.setString(2, country_nm_format);
			pstmt1.setString(3, name_prefix);
			pstmt1.setString(4, fname);
			pstmt1.setString(5, lname);
			pstmt1.executeUpdate();
			String query2 = ("if exists(select * from emp_details_address where USER_ID = '1') update emp_details_address set ADDRESS1 = '"
					+ addr1 + "', ADDRESS2='" + addr2 + "', ADDRESS3='" + addr3 + "', ADDRESS4='" + addr4
					+ "', COUNTRY='" + country + "', STATE='" + state + "', CITY='" + city + "', POSTAL='" + postalcode
					+ "'"
					+ " where USER_ID = '1' else insert into emp_details_address (ADDRESS_TYPE,ADDRESS1, ADDRESS2,ADDRESS3,ADDRESS4,COUNTRY,STATE,CITY,POSTAL)values(?,?,?,?,?,?,?,?,?)");
			PreparedStatement pstmt2 = con.prepareStatement(query2);
			pstmt2.setString(1, address_type);
			pstmt2.setString(2, addr1);
			pstmt2.setString(3, addr2);
			pstmt2.setString(4, addr3);
			pstmt2.setString(5, addr4);
			pstmt2.setString(6, country);
			pstmt2.setString(7, state);
			pstmt2.setString(8, city);
			pstmt2.setString(9, postalcode);
			pstmt2.executeUpdate();
			String query3 = ("if exists(select * from emp_details_contact where USER_ID = '1') update emp_details_contact set COUNTRY_CODE = '"
					+ countrycode + "', PHONE='" + phone + "', EMAIL_ADDR='" + emailid + "'"
					+ " where USER_ID = '1' else insert into emp_details_contact (HRS_PHONE_TYPE,COUNTRY_CODE,PHONE,HRS_E_ADDR_TYPE,EMAIL_ADDR,PREF_EMAIL_FLAG)values(?,?,?,?,?,?)");
			PreparedStatement pstmt3 = con.prepareStatement(query3);
			String phonetype = "CELL";
			String addrtype = "HOME";
			String emailflag = "1";
			pstmt3.setString(1, phonetype);
			pstmt3.setString(2, countrycode);
			pstmt3.setString(3, phone);
			pstmt3.setString(4, addrtype);
			pstmt3.setString(5, emailid);
			pstmt3.setString(6, emailflag);
			pstmt3.executeUpdate();

			String query4 = ("insert into emp_details_works (HRS_PROFILE_SEQ,EMPLOYER,ENDING_TITLE,START_DT,END_DT,J_CURRENT_SALARY,CURRENCY)values(?,?,?,?,?,?,?)");
			PreparedStatement pstmt4 = con.prepareStatement(query4);
			JSONArray jsonArrays = new JSONArray(work_arr);
			for (int i1 = 0; i1 < jsonArrays.length(); i1++) {
				int users = 1;
				JSONObject objs = jsonArrays.getJSONObject(i1);
				String employer1 = objs.getString("employer");
				String endingtitle1 = objs.getString("endingttile");
				String start1 = objs.getString("satrtdt");
				String end1 = objs.getString("enddt");
				String current1 = objs.getString("currsal");
				String currency1 = objs.getString("curr");
				String proseq = "90001";

				pstmt4.setString(1, proseq);
				pstmt4.setString(2, employer1);
				pstmt4.setString(3, endingtitle1);
				pstmt4.setString(4, start1);
				pstmt4.setString(5, end1);
				pstmt4.setString(6, current1);
				pstmt4.setString(7, currency1);
				pstmt4.executeUpdate();
			}
			String query5 = ("if exists(select * from emp_details_basic_2 where USER_ID = '1') update emp_details_basic_2 set J_EXP_YEAR = '"
					+ expyear + "', J_EXP_MONTH='" + expmonth + "', J_POST_GRID='" + postgrad + "', J_GRADUATION='"
					+ grad + "', J_INDUSTRY='" + industry + "', J_FUNCTIONAL_AREA='" + functionalarea + "'"
					+ " where USER_ID = '1' else insert into emp_details_basic_2 (J_EXP_YEAR,J_EXP_MONTH,J_POST_GRID,J_GRADUATION,J_INDUSTRY,J_FUNCTIONAL_AREA)values(?,?,?,?,?,?)");
			PreparedStatement pstmt5 = con.prepareStatement(query5);

			pstmt5.setString(1, expyear);
			pstmt5.setString(2, expmonth);
			pstmt5.setString(3, postgrad);
			pstmt5.setString(4, grad);
			pstmt5.setString(5, industry);
			pstmt5.setString(6, functionalarea);

			pstmt5.executeUpdate();
			String query6 = (" insert into emp_details_edu (USER_ID,HRS_PROFILE_SEQ,JPM_CAT_TYPE,SCHOOL_CODE,JPM_INTEGER_2,COUNTRY_EDU,TYPE_OF_STUDY_GER,JPM_CAT_ITEM_ID,MAJOR_CODE,JPM_DECIMAL_1)values(?,?,?,?,?,?,?,?,?,?)");

			PreparedStatement pstmt6 = con.prepareStatement(query6);
			JSONArray jsonArray = new JSONArray(edu_arr);
			for (int i = 0; i < jsonArray.length(); i++) {
				int user = 1;
				JSONObject obj = jsonArray.getJSONObject(i);
				String ins = obj.getString("schoolcode");
				String stu = obj.getString("typeofstudy");
				String itemss = obj.getString("itemid");
				String major = obj.getString("majorcode");
				String catt = obj.getString("jpm_decimal");
				String proseq = "90001";
				String cat_type = "EDUC_QUAL";
				pstmt6.setInt(1, user);
				pstmt6.setString(2, proseq);
				pstmt6.setString(3, cat_type);
				pstmt6.setString(4, ins);
				pstmt6.setString(5, passyear);
				pstmt6.setString(6, countryedu);
				pstmt6.setString(7, stu);
				pstmt6.setString(8, major);
				pstmt6.setString(9, branch);
				pstmt6.setString(10, catt);
				pstmt6.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("Data is uploaded successfully to sqlexpress database").build();

	}

	@Path("/testinomial")
	@POST

	@Produces({ MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.TEXT_HTML })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.TEXT_HTML })
	public Response addUser6(@FormDataParam("photofiles") InputStream fileInputStream,
			@FormDataParam("myfiles") FormDataContentDisposition fileInputDetails,
			@FormDataParam("enames") String enames, @FormDataParam("equote") String equote) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=practice;" + "user=sa;"
					+ "password=rohit@crosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			String query = ("insert into testinomial(emp_name,emp_quote,photo) VALUES(?,?,?)");
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, enames);
			pstmt.setString(2, equote);

			pstmt.setBinaryStream(3, fileInputStream);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("testinomial is uploaded successfully to sqlexpress database").build();
	}

	@Path("consultant/testinomial_delete/{id}")
	@GET

	@Produces(MediaType.TEXT_HTML)
	public String consultant_table2(@PathParam("id") int id) {
		String content = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\SQLMYSERVER;" + "database=jubilant;" + "user=sa;"
					+ "password=rohitcrosstab";

			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			PreparedStatement stmt = con.prepareStatement("delete from consultant_emp_speak where id=" + id);
			stmt.executeUpdate();
			String query = ("select * from consultant_emp_speak");
			Statement pstmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery(query);
			while (rs.next()) {

				byte[] bytes = rs.getBytes("image");
				byte[] encoded = Base64.encodeBase64(bytes);
				String encodedString = new String(encoded);
				content += "<tr> " + "<td class='1'><img src='data:image/jpeg;base64," + encodedString
						+ "' width='100px' height='100px' alt=''/></td>" + "<td class='2'>" + rs.getString("name")
						+ "</td>" + "<td class='3'>" + rs.getString("designation") + "<td class='4'>"
						+ rs.getString("quote") + "</td>"
						+ " <td><button type='button' class='edit'data-toggle='modal' data-target='#myModal' onclick=\"edit_row('"
						+ rs.getString(1)
						+ "');\"><i class='fa fa-pencil'></i></button>&amp;nbsp;&amp;nbsp;&amp;nbsp;<button type='button' class='edits'onclick=\"delete_row('"
						+ rs.getString(1) + "');\"><i class='fa fa-trash-o'></i></button></td>" + "</tr>";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	/*
	 * @Path("consultant/testinomial_update/{id}")
	 * 
	 * @GET
	 * 
	 * @Produces({ MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA,
	 * MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
	 * MediaType.TEXT_HTML })
	 * 
	 * @Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON,
	 * MediaType.APPLICATION_XML, MediaType.TEXT_HTML }) public String
	 * consultants(@FormDataParam("image") InputStream fileInputStream,
	 * 
	 * @FormDataParam("image") FormDataContentDisposition
	 * fileInputDetails, @FormDataParam("name") String enames,
	 * 
	 * @FormDataParam("dsgn") String dsgn, @FormDataParam("quote") String
	 * equote, @PathParam ("id") int id) { String content = null; try {
	 * Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); String
	 * connectionUrl = "jdbc:sqlserver://localhost\\SQLMYSERVER;" +
	 * "database=jubilant;" + "user=sa;" + "password=rohitcrosstab";
	 * 
	 * Connection con = DriverManager.getConnection(connectionUrl);
	 * System.out.println("Connected."); PreparedStatement stmt = con.
	 * prepareStatement("update name, designation, quote, image from consultant_emp_speak where id="
	 * +id); stmt.setString(1, enames); stmt.setString(2, dsgn);
	 * stmt.setString(3, equote); stmt.setBinaryStream(4, fileInputStream);
	 * 
	 * stmt.executeUpdate();
	 * 
	 * String query = ("select * from consultant_emp_speak"); Statement pstmt =
	 * con.createStatement(); ResultSet rs = pstmt.executeQuery(query); while
	 * (rs.next()){
	 * 
	 * byte[] bytes = rs.getBytes("image"); byte[] encoded =
	 * Base64.encodeBase64(bytes); String encodedString = new String(encoded);
	 * content += "<tr> " + "<td class='1'><img src='data:image/jpeg;base64,"
	 * +encodedString+ "' width='100px' height='100px' alt=''/></td>" +
	 * "<td class='2'>" + rs.getString("name") + "</td>" + "<td class='3'>" +
	 * rs.getString("designation") + "<td class='4'>" + rs.getString("quote") +
	 * "</td>" +
	 * " <td><button type='button' class='edit' data-toggle='modal' data-target='#myModal' onclick=\"edit_row('"
	 * + rs.getString(1) +
	 * "');\"><i class='fa fa-pencil'></i></button>&amp;nbsp;&amp;nbsp;&amp;nbsp;<button type='button' class='edits' onclick=\"delete_row('"
	 * + rs.getString(1) + "');\"><i class='fa fa-trash-o'></i></button></td>"
	 * +"</tr>";
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); } return content; }
	 */

	@Path("consultant/forgetpass/{id}")
	@GET

	@Produces({ MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.TEXT_HTML })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.TEXT_HTML })
	public void consultants(@PathParam("id") String id) {
		String text1 = "Jubilant";
		Date date = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String today = formatter.format(date);
		byte[] email = Base64.encodeBase64(id.getBytes());
		byte[] dates = Base64.encodeBase64(today.getBytes());
		byte[] text = Base64.encodeBase64(text1.getBytes());
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\SQLMYSERVER;" + "database=jubilant;" + "user=sa;"
					+ "password=rohitcrosstab";

			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			String query = ("select PSWD from emp_details_login where EMAIL_ADDR='" + id + "'");
			Statement pstmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery(query);
			if (rs.next()) {
				String smtpHost = "smtp.gmail.com"; // replace this with a valid
													// host
				int smtpPort = 587; // replace this with a valid port

				final String sender = "rohitgupta@crosstab.in";
				final String password = "rohit@crosstab";
				String recipient1 = "rohitgupta@crosstab.in";
				Properties properties = new Properties();
				properties.put("mail.smtp.host", smtpHost);
				properties.put("mail.smtp.port", smtpPort);
				properties.put("mail.smtp.auth", "true");
				properties.put("mail.smtp.starttls.enable", "true");
				properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
				properties.put("mail.smtp.debug", "true");
				Authenticator auth = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(sender, password);
					}
				};
				Session session = Session.getDefaultInstance(properties, auth);
				try {
					StringBuffer sb = new StringBuffer();

					MimeMessage message = new MimeMessage(session);

					message.addRecipient(Message.RecipientType.TO, new InternetAddress(id));
					message.setSubject("Quiz link url");
					sb.append("Please Click in the below link to change your Password");
					sb.append("\n");
					sb.append("http://localhost:8000/Quiz/gotoquiz?" + email + "&amp;" + dates + "&amp;" + text1);

					message.setText(sb.toString());

					// Send message
					Transport.send(message);
					System.out.println("message sent successfully....");

				} catch (MessagingException mex) {
					mex.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Path("consultant/testinomial")
	@GET

	@Produces(MediaType.TEXT_HTML)
	public String consultant_table() {
		String content = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\SQLMYSERVER;" + "database=jubilant;" + "user=sa;"
					+ "password=rohitcrosstab";

			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			String query = ("select * from consultant_emp_speak");
			Statement pstmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery(query);
			while (rs.next()) {

				byte[] bytes = rs.getBytes("image");
				byte[] encoded = Base64.encodeBase64(bytes);
				String encodedString = new String(encoded);
				content += "<tr> " + "<td class='1'><img src='data:image/jpeg;base64," + encodedString
						+ "' width='100px' height='100px' alt=''/></td>" + "<td class='2'>" + rs.getString("name")
						+ "</td>" + "<td class='3'>" + rs.getString("designation") + "<td class='4'>"
						+ rs.getString("quote") + "</td>"
						+ " <td><button type='button'data-toggle='modal' data-target='#myModal' class='edit' onclick=\"edit_row('"
						+ rs.getString(1)
						+ "');\"><i class='fa fa-pencil'></i></button>&amp;nbsp;&amp;nbsp;&amp;nbsp;<button type='button' class='edits'onclick=\"delete_row('"
						+ rs.getString(1) + "');\"><i class='fa fa-trash-o'></i></button></td>" + "</tr>";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	@Path("consultant/testinomial_owl")
	@GET

	@Produces(MediaType.TEXT_HTML)
	public Response consultant_table1() {
		JSONArray jArrays = new JSONArray();
		try {

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\SQLMYSERVER;" + "database=jubilant;" + "user=sa;"
					+ "password=rohitcrosstab";

			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			String query = ("select name, designation, quote, image from consultant_emp_speak");
			Statement pstmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery(query);
			while (rs.next()) {

				byte[] bytes = rs.getBytes("image");
				byte[] encoded = Base64.encodeBase64(bytes);
				String encodedString = new String(encoded);
				JSONObject jObjects = new JSONObject();
				jObjects.put("empnames", rs.getString("name"));
				jObjects.put("empdesg", rs.getString("designation"));
				jObjects.put("empquote", rs.getString("quote"));
				jObjects.put("photo", encodedString);
				jArrays.put(jObjects);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(jArrays.toString()).build();
	}

	@Path("consultant/table")
	@POST

	@Produces({ MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.TEXT_HTML })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.TEXT_HTML })
	public Response consultant(@FormDataParam("image") InputStream fileInputStream,
			@FormDataParam("image") FormDataContentDisposition fileInputDetails, @FormDataParam("name") String enames,
			@FormDataParam("dsgn") String dsgn, @FormDataParam("quote") String equote) throws URISyntaxException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\SQLMYSERVER;" + "database=jubilant;" + "user=sa;"
					+ "password=rohitcrosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			String query = ("insert into consultant_emp_speak(name,designation,quote,image) VALUES(?,?,?,?)");
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, enames);
			pstmt.setString(2, dsgn);
			pstmt.setString(3, equote);
			pstmt.setBinaryStream(4, fileInputStream);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		java.net.URI location = new java.net.URI("http://localhost:8008/xmlparser2/test.html");
		return Response.temporaryRedirect(location).build();
	}

	@Path("/testinomialjson")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response gettestinomialjson() {
		JSONArray jArray = new JSONArray();
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=practice;" + "user=sa;"
					+ "password=rohit@crosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select emp_name,emp_quote from testinomial;");
			while (rs.next()) {

				JSONObject jObject = new JSONObject();

				jObject.put("col1", rs.getString(1));
				jObject.put("col2", rs.getString(2));

				jArray.put(jObject);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(jArray.toString());

		return Response.status(200).entity(jArray.toString()).build();

	}

	@Path("/testinomialjson1")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response gettestinomialjson1() {
		JSONArray jArrays = new JSONArray();
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=practice;" + "user=sa;"
					+ "password=rohit@crosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select emp_name,emp_quote,photo from testinomial;");

			while (rs.next()) {
				byte[] bytes = rs.getBytes("photo");
				byte[] encoded = Base64.encodeBase64(bytes);
				String encodedString = new String(encoded);

				JSONObject jObjects = new JSONObject();
				jObjects.put("empnames", rs.getString(1));
				jObjects.put("empquote", rs.getString(2));
				jObjects.put("photo", encodedString);

				jArrays.put(jObjects);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(jArrays.toString());
		return Response.status(200).entity(jArrays.toString()).build();
	}

	@Path("/fulljson")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response fulljson() {
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
			}
			ResultSet rs2 = st.executeQuery("select * from emp_details_address");
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
			JSONArray jarrs = new JSONArray();
			while (rs5.next()) {
				JSONObject jObjects_work = new JSONObject();
				jObjects_work.put("company", rs5.getString("EMPLOYER"));
				jObjects_work.put("jobtitle", rs5.getString("ENDING_TITLE"));
				jObjects_work.put("start_date", rs5.getString("START_DT"));
				jObjects_work.put("end_date", rs5.getString("END_DT"));
				jObjects_work.put("salary", rs5.getString("J_CURRENT_SALARY"));
				jObjects_work.put("currency", rs5.getString("CURRENCY"));
				jarrs.put(jObjects_work);
			}
			ResultSet rs6 = st.executeQuery("select * from emp_details_edu");
			JSONArray jarr = new JSONArray();
			while (rs6.next()) {

				JSONObject objedu = new JSONObject();
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
			jObject.put("work", jarrs);
			jObject.put("edu", jarr);
			jObject.put("basic", jObjects);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(jObject.toString());
		return Response.status(200).entity(jObject.toString()).build();
	}

	@Path("/xmltojson")
	@GET
	@Produces({ MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.TEXT_HTML })
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.TEXT_HTML })
	public Response xmltojson() {
		
		String xml_data = "<job_opening><JobOpeningId>11732</JobOpeningId><JobTitle>Temporary Inspection &amp; Packaging Positions</JobTitle><Joblocation>SPK</Joblocation><Locationdescr>Spokane</Locationdescr><RecruiterId>jubl@jubl.com</RecruiterId><BusinessUnit>CMO01</BusinessUnit><Dept>60809C</Dept><DeptDescr>SVP Packaging I</DeptDescr><Country>USA</Country><Company>JHS</Company><CompanyDescr>Jubilant HollisterStier Spokan</CompanyDescr><CompanySeq>60</CompanySeq><RecruitingLoc>1590</RecruitingLoc><Function>Manufacturing</Function><Business>CMO, Spokane</Business><Vertical>Pharma-NA</Vertical><Subvertical>CMO, Spokane</Subvertical><jobposting><Jobpostingdate>17/01/17</Jobpostingdate><Jobremovaldate /></jobposting></job_opening>";
	JSONObject obj = XML.toJSONObject(xml_data);
	
 try {
		 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		 String connectionUrl = "jdbc:sqlserver://localhost\\SQLMYSERVER;" +
		 "database=jubilant;" + "user=sa;" + "password=rohitcrosstab";
		  Connection con = DriverManager.getConnection(connectionUrl);
		  System.out.println("Connected."); 
		  String querys = (" insert into job_openings (JobOpeningId,Job_title,jobdescr,joblocation,locationdescr,recruiterid,buisnessunit,dept,deptdescr,country,company,companydescr,companyseq,recruitingloc,function,buisness,vertical,subvertical,jobopeningdate,jobremovaldate)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			PreparedStatement pstm= con.prepareStatement(querys);
			JSONArray jsonArray = new JSONArray(obj);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject objs = jsonArray.getJSONObject(i);
				JSONObject objss = objs.getJSONObject("job_opening");
				pstm.setString(1, objss.getString("JobOpeningId"));
				pstm.setString(2, objss.getString("JobOpeningId"));
				pstm.setString(3, objss.getString("JobOpeningId"));
				pstm.setString(4, objss.getString("JobOpeningId"));
				pstm.setString(5, objss.getString("JobOpeningId"));
				pstm.setString(6, objss.getString("JobOpeningId"));
				pstm.setString(7, objss.getString("JobOpeningId"));
				pstm.setString(8, objss.getString("JobOpeningId"));
				pstm.setString(9, objss.getString("JobOpeningId"));
				pstm.setString(10, objss.getString("JobOpeningId"));
				pstm.setString(11, objss.getString("JobOpeningId"));
				pstm.setString(12, objss.getString("JobOpeningId"));
				pstm.setString(13, objss.getString("JobOpeningId"));
				pstm.setString(14, objss.getString("JobOpeningId"));
				pstm.setString(15, objss.getString("JobOpeningId"));
				pstm.setString(16, objss.getString("JobOpeningId"));
				pstm.setString(17, objss.getString("JobOpeningId"));
				pstm.setString(18, objss.getString("JobOpeningId"));
				pstm.setString(19, objss.getString("JobOpeningId"));
				pstm.setString(20, objss.getString("JobOpeningId"));
				pstm.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
 return Response.status(200).entity(obj.toString()).build();
}
	@Path("/retrieve_iamge")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieve_image() {
		JSONArray jArrays = new JSONArray();
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + "database=practice;" + "user=sa;"
					+ "password=rohit@crosstab";
			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("Connected.");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select photo from testinomial;");

			while (rs.next()) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(jArrays.toString());
		return Response.status(200).entity(jArrays.toString()).build();
	}
}
