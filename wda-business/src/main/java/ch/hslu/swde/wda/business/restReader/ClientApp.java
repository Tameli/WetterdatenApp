package ch.hslu.swde.wda.business.restReader;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherReading;
import ch.hslu.swde.wda.util.JpaUtil;


public class ClientApp {
	
	private static final Logger LOG = LogManager.getLogger(ClientApp.class);

	public ClientApp() {

	}

	public static void main(String[] args) throws Exception {

		ClientApp app1 = new ClientApp();
		City zug = new City(6300, "Zug");
		City saasFee = new City(3906, "Saas-Fee");
		String json = app1.getDataForSpecificCityInTimeInterval(zug, LocalDate.now().minusDays(3), LocalDate.now());
		String json1 = app1.getDataForSpecificCityInTimeInterval(saasFee, LocalDate.now().minusDays(3), LocalDate.now());
		List<WeatherReading> listZug = app1.parseJsonAndMapToObject(json);
		List<WeatherReading> listSaasFee = app1.parseJsonAndMapToObject(json1);
		List<WeatherReading> listResult = new ArrayList<WeatherReading>();
		listResult.addAll(listZug);
		listResult.addAll(listSaasFee);
		String listAsString = app1.converstListtoJSON(listResult);
		app1.getTxtFilefromList("C:\\Users\\lunas\\Desktop\\DAWA_dataset.json", listAsString);
	}
	
	
	public String getDataForSpecificCityInTimeInterval(City city, LocalDate start, LocalDate end) throws Exception {
		List<WeatherReading> resultList = new ArrayList<>();
		RESTMessageClient client = new RESTMessageClient();
		String json1 = client.wheaterDataForSpecificCityAndTimeInterval(city, start, end);
		return json1;
	}


	
	/**
	 * Liefert eine Liste aller Städte zurück, die von der Wetterdatenapplikation
	 * verwaltet werden. Als Objekt!!
	 * 
	 * @return List<WeatherReading>
	 * @throws Exception
	 */

	public List<City> allCititesAsObject() throws Exception {
		int counter = 0;

		List<City> resultList = new ArrayList<>();
		RESTMessageClient client = new RESTMessageClient();
		String json1 = client.allCities();
		ObjectMapper mapper = new ObjectMapper();
		List<MessageAllCities> list = mapper.readValue(json1, new TypeReference<List<MessageAllCities>>() {
		});
		for (MessageAllCities m : list) {
			resultList.add(counter, new City(m.getZip(),m.getName()));
			counter++;
		}
		
		return resultList;

	}
	
	/**
	 * Liefert ein den json vom Server als String zurück.
	 * 
	 * @return String json
	 * @throws Exception
	 */

	public String allDataofOneCity(City city1) throws Exception {
		int counter = 0;
		List<WeatherReading> resultList = new ArrayList<>();
		RESTMessageClient client = new RESTMessageClient();
		String json1 = client.wheaterDataForSpecificCity(city1);
		return json1;

	}
	
	//public String getDataForSpecificCityInTimeInterval(City city, LocalDate start, LocalDate end) throws Exception {
		//List<WeatherReading> resultList = new ArrayList<>();
		//RESTMessageClient client = new RESTMessageClient();
		//String json1 = client.wheaterDataForSpecificCityAndTimeInterval(city, start, end);
		//return json1;
	//}
	
	public String getDataForSpecificCitySinceTimeInterval(City city, LocalDate start) throws Exception{
		List<WeatherReading> resultList = new ArrayList<>();
		RESTMessageClient client = new RESTMessageClient();
		String json1 = client.wheaterDataForSpecificCitySinceTimeInterval(city, start);
		return json1;
	}
	
	/**
	 * Methode um den erhaltenen JSON vom REST Server auf ein Objekt vom Typ WeatherReading zu mappen
	 * @param json
	 * @return List<WeatherReading>
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	
	public List<WeatherReading> parseJsonAndMapToObject(String json) throws JsonMappingException, JsonProcessingException {
		int counter = 0;
		List<WeatherReading> resultList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		List<MessageSpecificCityDataset> list = mapper.readValue(json,
				new TypeReference<List<MessageSpecificCityDataset>>() {
				});
		for (MessageSpecificCityDataset m : list) {
			String str = m.getData();
			String[] arrOfStr = str.split("#");

			// Parse LocalDateTime

			String[] arrOfStr1 = arrOfStr[0].split(":", 2);
			LocalDateTime date = LocalDateTime.parse(arrOfStr1[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			// Parse country
			String[] arrOfStr2 = arrOfStr[1].split(":", 2);
			String country = arrOfStr2[1];
			
			// Parse zip
			String[] arrOfStr3 = arrOfStr[2].split(":", 2);
			int zip = Integer.parseInt(arrOfStr3[1]);
			

			// Parse city name
			String[] arrOfStr4 = arrOfStr[3].split(":", 2);
			String city = arrOfStr4[1];

			// Parse longitude
			String[] arrOfStr5 = arrOfStr[4].split(":", 2);
			float longitude = Float.valueOf(arrOfStr5[1]);

			// Parse latitude
			String[] arrOfStr6 = arrOfStr[5].split(":", 2);
			float latitude = Float.valueOf(arrOfStr6[1]);
			

			// Parse stationId
			String[] arrOfStr7 = arrOfStr[6].split(":", 2);
			int stationId = Integer.parseInt(arrOfStr7[1]);

			// Parse summary
			String[] arrOfStr8 = arrOfStr[7].split(":", 2);
			String summary = arrOfStr8[1];

			// Parse description
			String[] arrOfStr9 = arrOfStr[8].split(":", 2);
			String description = arrOfStr9[1];

			// Parse currentTemp
			String[] arrOfStr10 = arrOfStr[9].split(":", 2);
			float currentTemp = Float.valueOf(arrOfStr10[1]);

			// Parse pressure
			String[] arrOfStr11 = arrOfStr[10].split(":", 2);
			int pressure = Integer.valueOf(arrOfStr11[1]);

			// Parse humidity
			String[] arrOfStr12 = arrOfStr[11].split(":", 2);
			int humidity = Integer.valueOf(arrOfStr12[1]);

			// Parse windSpeed
			String[] arrOfStr13 = arrOfStr[12].split(":", 2);
			float windSpeed = Float.valueOf(arrOfStr13[1]);

			// Parse windDirection
			String[] arrOfStr14 = arrOfStr[13].split(":", 2);
			float windDirection = 0.0f;
			if (arrOfStr14[1].equals("unknown")) {
				windDirection = -1.0f;
			} else {
				windDirection = Float.valueOf(arrOfStr14[1]);
			}
            
			EntityManager em = JpaUtil.createEntityManager();
			TypedQuery<City> tQry = em.createNamedQuery("City.findCityByZipAndName", City.class);
			
			tQry.setParameter("zip",zip);
			tQry.setParameter("name",city);
			City cityResult = tQry.getSingleResult();
			resultList.add(counter, new WeatherReading(date, country, cityResult, longitude, latitude, stationId,
					summary, description, currentTemp, pressure, humidity, windSpeed, windDirection));
		
	}
	return resultList;
	}
	
	/**
	 * Exportieren der angeforderten Daten in ein txt File
	 * 
	 * @param file:  Pfad des zu erstellenden Files
	 * @param value: String der ins File geschrieben werden soll
	 * 
	 */

	public void getTxtFilefromList(final String file, final String value)throws RemoteException {
		try (BufferedWriter wr = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")

				))) {
			wr.write(value);
			wr.flush();
		} catch (IOException ioe) {
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Konvertierung der Liste von Wetterdaten in ein pretty JSON
	 */

	public String converstListtoJSON(List<WeatherReading> list)throws RemoteException {
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create(); // pretty print
		String prettyJson = gson.toJson(list);
		System.out.println(prettyJson);
		return prettyJson;
	}

	public String convertStringListToJSON(List<String> list)throws RemoteException {
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create(); // pretty print
		String prettyJson = gson.toJson(list);
		System.out.println(prettyJson);
		return prettyJson;
	}
	

}
