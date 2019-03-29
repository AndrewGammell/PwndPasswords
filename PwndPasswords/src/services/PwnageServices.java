package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class PwnageServices {

	private String breachURL = "https://haveibeenpwned.com/api/v2/breaches";
	private String passwordURL = "https://api.pwnedpasswords.com/range/";
	private String accountURL = "https://haveibeenpwned.com/api/v2/breachedaccount/%s";
	private String userAgent = "Pwnd_Passwords";
	private JsonParser parser = new JsonParser();
	private Scanner scanner = new Scanner(System.in);


	public void breachService() {
		try {
			URL url = new URL(breachURL);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", userAgent);

			printBreachResponse(parseJSONResponse(con.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JsonArray parseJSONResponse(InputStream is) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String inputLine;
		String jsonString = null;

		while ((inputLine = br.readLine()) != null) {
			jsonString = inputLine;
		}	
		br.close();

		JsonElement jobj = parser.parse(jsonString);

		return jobj.getAsJsonArray();
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

	public void passwordService() {
		boolean exit = false;

		do {
			System.out.println("Enter the password you want to check\nE) exit to menu");

			String plainPassword = scanner.next();

			switch(plainPassword) {
			case "e": exit = true;
			case "E": exit = true;
			break;
			default :  checkPassword(plainPassword);
			}

		}while(!exit);

	}

	private void checkPassword(String plainPassword) {
		String hashedPassword;
		try {
			hashedPassword = hashPassword(plainPassword);
			getPwnage(hashedPassword);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			System.out.println("An Error Occured Please Try Again");
		}
	}

	private String hashPassword(String plainPassword) throws GeneralSecurityException {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		byte[] result = digest.digest(plainPassword.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	private void getPwnage(String hashedPassword) {
		try {
			URL url = new URL(passwordURL + hashedPassword.substring(0,5));
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", userAgent);
			compareHash(con.getInputStream(), hashedPassword.substring(5));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void compareHash(InputStream is, String compareTo ) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String string = "This password was found %s times \n";

		String inputLine;

		while ((inputLine = br.readLine()) != null) {			

			if( inputLine.substring(0, inputLine.indexOf(':')).equals(compareTo.toUpperCase())) {
				System.out.println(String.format(string, inputLine.substring(36)));
				break;
			} 
		}	
		br.close();
		System.out.println("This password was not found in any breaches\n");
	}

	public void accountService () {
		boolean exit = false;

		do {
			System.out.println("Enter the email account you want to check\nE) exit to menu");

			String account = scanner.next();

			switch(account) {
			case "e": exit = true;
			case "E": exit = true;
			break;
			default :  checkAccount(account);
			}

		}while(!exit);
	}

	private void checkAccount(String account) {
		try {
			URL url = new URL(String.format(accountURL, account));
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", userAgent);

			printAccountBreaches(parseJSONResponse(con.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printAccountBreaches(JsonArray jarray) throws IOException {
		String formatString = "Name: %s, Domain: %s,";
		System.out.println("\nThis account was compromised because of the following sites");

		for(int i = 0; i < jarray.size(); i++) {
			String print = String.format(formatString,
					jarray.get(i).getAsJsonObject().get("Name").toString(),
					jarray.get(i).getAsJsonObject().get("Domain").toString());

			System.out.println(print);
		}

		System.out.println("\n");
	}

}
