import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.*;
import org.apache.commons.io.IOUtils;


public class JSONtoCSV {

	public static void main(String[] args) {
		
		String url = "http://api.goeuro.com/api/v2/position/suggest/en/"+args[0];
		
		
		JSONArray myArray = fetchJsonFromURL(url);
		
		try {
			parseAndSaveJSON(myArray,args[0]);
		}  catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public static JSONArray fetchJsonFromURL(String s) {
		 JSONArray myArray = null;
		
		try{
			URL url = new URL(s);
			InputStream is = url.openStream();
			String data = IOUtils.toString(is, StandardCharsets.UTF_8);
			IOUtils.closeQuietly(is);
			myArray = new JSONArray(data);
		} catch (IOException e) {
			// in case of malformed URL or cannot connect to the server, return empty JSON array to create empty CSV file
			System.out.println("the server is down or malformed URL");
		} catch (JSONException e) {
			// in case of wrong JSON structure, return empty JSONArray
			myArray = new JSONArray();
		}
		
		return myArray;
	}
	
	public static void parseAndSaveJSON(JSONArray myArray, String city) throws  IOException{
		File csvFile = new File(city+".csv");
		FileWriter writer = new FileWriter(csvFile);
		
		JSONObject myObject;
		JSONObject position;
		writer.write("Id"+","+"Name"+","+"Type"+","+"Latitude"+","+"Longitude\n");
		for(int i=0;i<myArray.length();i++){
			myObject= myArray.optJSONObject(i);
			writer.write(myObject.optInt("_id")+","+myObject.optString("name")+","+myObject.optString("type"));
			position = myObject.optJSONObject("geo_position");
			writer.write(","+position.optDouble("latitude")+","+position.optDouble("longitude")+"\n");
		}
		
		writer.flush();
		writer.close();
	}
}
