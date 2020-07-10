package ch.hslu.swde.wda.business.restReader;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;

import ch.hslu.swde.wda.domain.City;

/**
 * Client der die URI's an den REST Server zusammenstellt und den Statuscode der
 * Antwort vom Server überprüft.
 * 
 * @author lunas
 *
 */

public class RESTMessageClient {

	private static final String BASE_URI = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/";
	private HttpClient httpClient = HttpClient.newHttpClient();
	private String mimeType = "application/json";
	
	public static void main(String[] args) throws Exception {
		RESTMessageClient client = new RESTMessageClient();
		City zug = new City(6300, "Zug");
		client.wheaterDataForSpecificCity(zug);
	}

	public String allCities() throws Exception {

		HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URI + "cities")).headers("Accept", mimeType).GET()
				.build();

		HttpResponse<String> res = httpClient.send(req, BodyHandlers.ofString());

		if (res.statusCode() == 200) {
			return res.body().toString();
		} else {
			throw new Exception("Status-Code:" + res.statusCode());
		}
	}

	public String wheaterDataForSpecificCity(City city) throws Exception {
		
		String uri = BASE_URI + city.getName();
		uri = uri.replaceAll(" ", "%20");// für St. Gallen ect.

		HttpRequest req = HttpRequest.newBuilder(URI.create(uri)).headers("Accept", mimeType).GET().build();

		HttpResponse<String> res = httpClient.send(req, BodyHandlers.ofString());

		if (res.statusCode() == 200) {

			return res.body().toString();

		} else {
			throw new Exception("Status-Code:" + res.statusCode());
		}
	}

	public String wheaterDataForSpecificCityAndTimeInterval(City city, LocalDate start, LocalDate end) throws Exception {
		
		
		
		String uri = BASE_URI + city.getName()+"/during?from-year="+start.getYear()+"&from-month="+start.getMonthValue()+"&from-day="+start.getDayOfMonth()+"&to-year="+end.getYear()+"&to-month="+end.getMonthValue()+"&to-day="+end.getDayOfMonth(); 
		uri = uri.replaceAll(" ", "%20");// für St. Gallen ect.
		
		HttpRequest req = HttpRequest.newBuilder(URI.create(uri)).headers("Accept", mimeType).GET().build();

		HttpResponse<String> res = httpClient.send(req, BodyHandlers.ofString());

		if (res.statusCode() == 200) {

			return res.body().toString();

		} else {
			throw new Exception("Status-Code:" + res.statusCode());
		}
	}
	
	public String wheaterDataForSpecificCitySinceTimeInterval(City city, LocalDate start) throws Exception {
		String uri = BASE_URI + city.getName()+"/since?year="+start.getYear()+"&month="+start.getMonthValue()+"&day="+start.getDayOfMonth(); 
		uri = uri.replaceAll(" ", "%20");// für St. Gallen ect.
		
		HttpRequest req = HttpRequest.newBuilder(URI.create(uri)).headers("Accept", mimeType).GET().build();

		HttpResponse<String> res = httpClient.send(req, BodyHandlers.ofString());

		if (res.statusCode() == 200) {

			return res.body().toString();

		} else {
			throw new Exception("Status-Code:" + res.statusCode());
		}
		
	}
	
}
