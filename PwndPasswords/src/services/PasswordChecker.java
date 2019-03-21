package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class PasswordChecker {

	private String breachURL = "https://haveibeenpwned.com/api/v2/breaches";
	private String userAgent = "Pwnd_Passwords";
	private JsonParser parser = new JsonParser();
	
	
	public void listHackedSites() {
		try {
			URL url = new URL(breachURL);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", userAgent);

			parseBreachResponse(con.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseBreachResponse(InputStream is) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String inputLine;
		String jsonString = null;

		while ((inputLine = br.readLine()) != null) {
			jsonString = inputLine;
		}	
		br.close();
		
		JsonElement jobj = parser.parse(jsonString);
		
		printBreachResponse(jobj.getAsJsonArray());
	}
	
	private void printBreachResponse(JsonArray jarray) {
		String formatString = "Name: %s, Domain: %s, BreachDate: %s, PwnCount: %s";
			
		
		for(int i = 0; i < jarray.size(); i++) {
			String print = String.format(formatString, jarray.get(i).getAsJsonObject().get("Name").toString(),
					   								   jarray.get(i).getAsJsonObject().get("Domain").toString(),
					   								   jarray.get(i).getAsJsonObject().get("BreachDate").toString(),
					   								   jarray.get(i).getAsJsonObject().get("PwnCount").toString());
			
			System.out.println(print);
		}
		
		System.out.println("\n");
		
		
	}


}
