package menjacnica.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import menjacnica.Valuta;

public class JsonRatesAPIKomunikacija {

	private static final String appKey = "jr-ba8999934fc5a7ab64a4872fb4ed9af7";
	private static final String jsonRatesURL = "http://jsonrates.com/get/";
	private static String url;
	
	public static Valuta[] vratiIznosKurseva(String[] valute) {
		//http://jsonrates.com/get/?from=EUR&to=RSD&apiKey=jr-ba8999934fc5a7ab64a4872fb4ed9af7

		Valuta[] kursevi = new Valuta[valute.length];
		
		for (int i = 0; i < valute.length; i++) {
			for (int j = 0; j < valute.length; j++) {
				
				ispisi(valute[i], valute[j]);

				try {
					String result = sendGet(url);

					Gson gson = new GsonBuilder().create();
					JsonObject jsonResult = gson.fromJson(result, JsonObject.class);

					Valuta obj = new Valuta();
					obj.setNaziv(valute[i]);
					obj.setKurs(Double.parseDouble(jsonResult.get("rate").getAsString()));
					
					kursevi[i] = obj;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return kursevi;
	}
	
	private static String ispisi(String from, String to) {
		return url = jsonRatesURL + "?" +
				"from=" + from +
				"&to=" + to +
				"&apiKey=" + appKey;
	}

	
	private static String sendGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

		connection.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));

		boolean endReading = false;
		String response = "";

		while (!endReading) {
			String s = in.readLine();

			if (s != null) {
				response += s;
			} else {
				endReading = true;
			}
		}
		in.close();

		return response.toString();
	}
}
