package xmlparser;



import org.apache.commons.codec.binary.Base64;



public class demo3 {

	  public static void main(String args[]) 
      {  
    String str="hello";
		  byte[] bytesEncoded = Base64.encodeBase64(str.getBytes());
		  System.out.println(bytesEncoded);

  	
      }

}