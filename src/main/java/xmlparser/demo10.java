package xmlparser;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class demo10 {
	public static void main(String arg[]) {
	     String xmlRecords =
	    		 "<data>" +
					      " <employee>" +
					      "   <name>ajay</name>" +
					      "   <title>singh</title>" +
					      "   <salary>10000</salary>" +
					      " </employee>" +
					      " <employee>" +
					      "   <name>rahul</name>" +
					      "   <title>singh</title>" +
					      "   <salary>10000</salary>" +
					      " </employee>" +
					      " <employee>" +
					      "   <name>ashok</name>" +
					      "   <title>mehra</title>" +
					      "   <salary>10000</salary>" +
					      " </employee>" +
					      " <employee>" +
					      "   <name>abhay</name>" +
					      "   <title>singh</title>" +
					      "   <salary>10000</salary>" +
					      " </employee>" +
					      " <employee>" +
					      "   <name>rajat</name>" +
					      "   <title>kumar</title>" +
					      "   <salary>10000</salary>" +
					      " </employee>" +
					      " <employee>" +
					      "   <name>hari</name>" +
					      "   <title>singh</title>" +
					      "   <salary>10000</salary>" +
					      " </employee>" +
					      " <employee>" +
					      "   <name>vikas</name>" +
					      "   <title>singh</title>" +
					      "   <salary>10000</salary>" +
					      " </employee>" +
					      "</data>";

	    try {
	        DocumentBuilderFactory dbf =
	            DocumentBuilderFactory.newInstance();
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
	           System.out.println("Title: " + getCharacterDataFromElement(line2));
	        }
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    /*
	    output :
	        Name: John
	        Title: Manager
	        Name: Sara
	        Title: Clerk
	    */    
	    
	  }

	  public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    return "?";
	  }
	}