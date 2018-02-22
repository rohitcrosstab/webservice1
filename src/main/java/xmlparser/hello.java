package xmlparser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class hello {

  
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String sayHtmlHello1() {
      return "<html> " + "<title>" + "Hello Jersey" + "</title>"
          + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
    }

    // This method is called if XML is request
    @GET
    @Path("/crosstab")
    @Produces(MediaType.TEXT_XML)
    public String sayXMLHello() {
      return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
    }

    // This method is called if HTML is request
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_HTML)
    public String sayHtmlHello() {
      return "<html> " + "<title>" + "Hello Jersey" + "</title>"
          + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
    }
    @GET  
    @Path("{year}/{month}/{day}")  
    public Response getDate(  
            @PathParam("year") int year,  
            @PathParam("month") int month,   
            @PathParam("day") int day) {  
   
       String date = year + "/" + month + "/" + day;  
   
       return Response.status(200)  
        .entity("getDate is called, year/month/day : " + date)  
        .build();  
    }
}  