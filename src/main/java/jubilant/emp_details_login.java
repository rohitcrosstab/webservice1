package jubilant;

import org.apache.commons.codec.binary.Base64;

public class emp_details_login {
public static void main(String[] a){
	String str="cm9oaXRndXB0YUBjcm9zc3RhYi5pbg==";
	String id="rohitgupta@crosstab.in";
	byte[] email = Base64.encodeBase64(id.getBytes());
	/*byte[] bytesEncoded = xmlparser.Base64.encodeBase64(str.getBytes());*/
	/*System.out.println("encoded value is " + new String(bytesEncoded));*/
/*	   String decode=Base64.decode(str);*/
	// Decode data on other side, by processing encoded data
	/*byte[] valueDecoded = Base64.decodeBase64(str.getBytes());*/
	/*byte[] valueDecoded2 = Base64.decodeBase64(valueDecoded);*/
	/*byte[] valueDecoded3 = Base64.decodeBase64(valueDecoded2);*/
	System.out.println("Decoded value is " + new String(email));
}
}
