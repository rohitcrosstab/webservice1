package xmlparser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.ws.rs.Path;
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
		@Consumes({MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.TEXT_HTML})
	@Produces({MediaType.TEXT_HTML,MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.TEXT_HTML})
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
				return Response.status(500)
						;
			}
			String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
			try {
				saveToFile(uploadedInputStream, uploadedFileLocation);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Response.status(200)
					;
		}
		
		private void saveToFile(InputStream inStream, String target)
				throws IOException {
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
		
		private void createFolderIfNotExists(String dirName)
				throws SecurityException {
			File theDir = new File(dirName);
			if (!theDir.exists()) {
				theDir.mkdir();
			}
		}
				@Path("/insert1")
		@POST
		
	@Produces(MediaType.TEXT_HTML)
		public Response addUser(
				@FormParam("email") String email,
				@FormParam("pwd") String pass) {
	        try {
	        	Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jubilant", "root",
						"2309");
	            String query = ("insert into crud(name,email) VALUES(?,?)");
	            PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setString(1,email);
	            pstmt.setString(2, pass);
	            pstmt.executeUpdate();	             
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return Response.status(200)
					.entity("Data is uploaded successfully to MySQL database")
					.build();
			
	    }     
				@Path("/insert2")
				@POST
				
			@Produces(MediaType.TEXT_HTML)
				public Response addUser2(
						@FormParam("email") String email,
						@FormParam("pwd") String pass) {
			        try {
			        	 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
				           String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + 
				                                   "database=practice;" + 
				                                   "user=sa;" + 
				                                   "password=rohit@crosstab"; 
				           Connection con = DriverManager.getConnection(connectionUrl); 
				           System.out.println("Connected.");
			            String query = ("insert into trial(email,pass) VALUES(?,?)");
			            PreparedStatement pstmt = con.prepareStatement(query);
			            pstmt.setString(1,email);
			            pstmt.setString(2, pass);
			            pstmt.executeUpdate();	             
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			        return Response.status(200)
							.entity("Data is uploaded successfully to sqlexpress database")
							.build();
					
			    }  
		@Path("/check")
		@GET
		
	@Produces(MediaType.TEXT_HTML)
		public String addUser() throws ClassNotFoundException
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
		           String SQL = "SELECT * FROM sampledb";  
		           java.sql.Statement stmt = con.createStatement();  
		           ResultSet rs = stmt.executeQuery(SQL);

		           // Iterate through the data in the result set and display it.  
		           while (rs.next())  
		           {  
			           System.out.println(rs.getString(1)); 
			           System.out.println(rs.getString(2)); 
			           System.out.println(rs.getString(3)); 
		           }

		      }  
		      catch(Exception e)  
		      { 
		           System.out.println(e.getMessage()); 
		             
		      }
			return null; 
		   
	       
			
	    }
		@Path("/insertdb1")
		@POST
		
		@Produces({MediaType.TEXT_HTML,MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.TEXT_HTML})
		@Consumes({MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.TEXT_HTML})
		public Response addUser3(
				 @FormDataParam("myfiles") InputStream fileInputStream,				 
				       @FormDataParam("myfiles") FormDataContentDisposition fileInputDetails,				 
				       @FormDataParam("emails") String  email,				 
				       @FormDataParam("pwds") String  pass) {
	        try {
	        	 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
		           String connectionUrl = "jdbc:sqlserver://localhost\\MYSQLEXPRESS;" + 
		                                   "database=practice;" + 
		                                   "user=sa;" + 
		                                   "password=rohit@crosstab"; 
		           Connection con = DriverManager.getConnection(connectionUrl); 
		           System.out.println("Connected.");
	            String query = ("insert into sampledb(email,pass,resume) VALUES(?,?,?)");
	            PreparedStatement pstmt = con.prepareStatement(query);
	            
	            pstmt.setString(1,email);
	            pstmt.setString(2, pass);
	            pstmt.setBinaryStream(3, fileInputStream);
	            pstmt.executeUpdate();	             
	        } catch (Exception e) {
	        	System.out.println(e.getMessage()); 
	        }
	        return Response.status(200)
					.entity("Data is uploaded successfully to sqlexpress database")
					.build();
			
	    }  
	}


