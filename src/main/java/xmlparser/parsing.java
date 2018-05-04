package xmlparser;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;

public class parsing {
	public static void main(String[] args) {

		JsonParser parser = new JsonParser();
		try {
			Object obj = parser.parse(new FileReader("new 1.json"));
			/*System.out.println(obj.toString());*/
			JsonObject jobject = ((JsonObject) obj).getAsJsonObject("Value");
		
		String data =jobject.get("ParsedDocument").getAsString();
		JsonElement jelement = new JsonParser().parse(data);
		JsonObject jobjects = jelement.getAsJsonObject();
		JsonObject jobject1 = jobjects.getAsJsonObject("Resume");
		JsonObject jobject2 = jobject1.getAsJsonObject("StructuredXMLResume");
		JsonObject jobject3 = jobject2.getAsJsonObject("ContactInfo");
		
		System.out.println(jobject3.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
