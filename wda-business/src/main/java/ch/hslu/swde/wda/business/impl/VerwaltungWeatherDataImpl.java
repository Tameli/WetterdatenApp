package ch.hslu.swde.wda.business.impl;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.hslu.swde.wda.business.api.VerwaltungCity;
import ch.hslu.swde.wda.business.api.VerwaltungWeatherData;
import ch.hslu.swde.wda.business.restReader.ClientApp;
import ch.hslu.swde.wda.business.rmi.client.Connector;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherReading;
import ch.hslu.swde.wda.persister.impl.db.PersisterCityImpl;
import ch.hslu.swde.wda.persister.impl.db.PersisterWeatherReadingImpl;
import ch.hslu.swde.wda.util.DbHelper;
import ch.hslu.swde.wda.util.WeatherPressureComparator;
import ch.hslu.swde.wda.util.WeatherReadingTimeStampComparator;
import ch.hslu.swde.wda.util.WeatherTimeStampComparator;

public class VerwaltungWeatherDataImpl extends UnicastRemoteObject implements VerwaltungWeatherData {

	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LogManager.getLogger(VerwaltungWeatherDataImpl.class);

	public VerwaltungWeatherDataImpl() throws RemoteException, MalformedURLException, NotBoundException {

	}

	public static void main(String[] args) throws Exception, RemoteException {
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		impl1.updateWholeDatabase();

	}

	/**
	 * Methode, um die komplette DB mit allen zugehörigen Tabellen mit den neusten
	 * Daten zu ersetzen
	 * 
	 * @throws Exception
	 */

	public void updateWholeDatabase() throws Exception, RemoteException {
		PersisterWeatherReadingImpl persist1 = new PersisterWeatherReadingImpl();
		PersisterCityImpl impl1 = new PersisterCityImpl();
		ClientApp app1 = new ClientApp();
		List<City> list = app1.allCititesAsObject();
		impl1.allCitites(list);
		for (City m : list) {
			String json = app1.allDataofOneCity(m);
			List<WeatherReading> resultList = app1.parseJsonAndMapToObject(json);
			persist1.persistSpecificDataForOneCity(resultList);
		}

	}

	/**
	 * Methode um nur die neusten Daten(seit dem letzten Update) aus der DB zu holen
	 * 
	 * @throws Exception
	 */

	public void updateDatabase() throws Exception, RemoteException {
		PersisterWeatherReadingImpl persist1 = new PersisterWeatherReadingImpl();
		LocalDateTime maxTime;
		PersisterCityImpl persistCity = new PersisterCityImpl();
		ClientApp app1 = new ClientApp();
		WeatherTimeStampComparator compareObject = new WeatherTimeStampComparator();
		List<LocalDateTime> list1 = new ArrayList<>();
		List<City> listAllCities = new ArrayList<>();
		List<WeatherReading> listForPersistance = new ArrayList<>();
		listAllCities = persistCity.getAllCitites();
		String city;
		for (City c : listAllCities) {
			city = c.getName();
			List<WeatherReading> list = persist1.getAllDataForSpecificCity(city);
			for (WeatherReading wr : list) {
				list1.add(wr.getTime());
			}
			if ((Collections.max(list1, new WeatherTimeStampComparator()).equals(null))) {// fals eine neue Stadt hinzukommt
				maxTime = LocalDateTime.of(2019, 11, 20, 23, 03, 46);// Letzter vorhandener Eintrag in DB am 07.12
			} else {
				maxTime = Collections.max(list1, new WeatherTimeStampComparator());
			}
			LocalDate max = maxTime.toLocalDate().minusDays(1);
			System.out.println(maxTime);
			String json = app1.getDataForSpecificCitySinceTimeInterval(c, max);
			List<WeatherReading> resultList = app1.parseJsonAndMapToObject(json);
			System.out.println(resultList.size());
			for (WeatherReading r : resultList) {
				
				if (compareObject.compare(r.getTime(), maxTime) == 1) {
					listForPersistance.add(r);
				}
			}
			persist1.persistSpecificDataForOneCity(listForPersistance);
			System.out.print("Liste für" + " " + city + " " + "wird mit"+ " " +listForPersistance.size() +" "+" persistiert" + "\n");
			listForPersistance.clear();
		}

	}

	/**
	 * Hilfsmethode um direkt von der Verwaltung aus mit LocalDateTime Anfragen
	 * machen zu können.GUI liefert normalerweise LocalDate.
	 * 
	 * @param start
	 * @param end
	 * @param city
	 * @return List<WeatherReading> liste mit den Wetterdaten eines bestimmten
	 *         Intervalls
	 * @throws Exception
	 */

	public List<WeatherReading> getDataForSpecificCityAndTimeInterval(LocalDateTime start, LocalDateTime end,
			String city) throws Exception, RemoteException {
		PersisterWeatherReadingImpl persist = new PersisterWeatherReadingImpl();
		PersisterCityImpl persist1 = new PersisterCityImpl();
		City city1 = persist1.findCityByName(city);
		List<WeatherReading> list = persist.getDataStartAndEndDate(start, end, city1);
		return list;
	}

	/**
	 * Anfrage A02
	 */

	@Override
	public List<WeatherReading> getDataLast24Hour(City city) throws Exception, RemoteException {
		List<WeatherReading> list = new ArrayList<WeatherReading>();
		PersisterWeatherReadingImpl impl1 = new PersisterWeatherReadingImpl();
		list = impl1.getDataLast24Hour(city);
		return list;
	}

	/**
	 * Anfrage A03
	 */

	@Override
	public List<WeatherReading> getDataForSpecificCityAndTimeInterval(LocalDate start, LocalDate end, String city)
			throws Exception, RemoteException {
		PersisterWeatherReadingImpl persist = new PersisterWeatherReadingImpl();
		PersisterCityImpl persist1 = new PersisterCityImpl();
		City city1 = persist1.findCityByName(city);
		List<WeatherReading> list = persist.getDataStartAndEndDate(start, end, city1);
		return list;
	}

	/**
	 * Anfrage A04
	 */
	public float getAverageTempForTimeInterval(List<WeatherReading> list) throws Exception, RemoteException {
		float sum = 0.0f;
		float result = 0.0f;
		if (!list.isEmpty()) {
			for (WeatherReading wr : list) {
				sum = sum + wr.getCurrentTemp();
			}
			return result = sum / list.size();
		} else {
			LOG.info("Die übergebene Liste enthält keine Objekte");
		}
		return result = -274.0f; // unmögliche Temperatur, Fehler fällt auf
	}

	/**
	 * Anfrage A05
	 */
	public float getMaxTempSpecificCities(List<WeatherReading> list) throws RemoteException{
		float result = -274.0f;
		if (!list.isEmpty()) {
			result = Collections.max(list).getCurrentTemp();
		}
		return result;
	}

	public float getMinTempSpecificCities(List<WeatherReading> list) {
		float result = -274.0f;
		if (!list.isEmpty()) {
			result = Collections.min(list).getCurrentTemp();
		}
		return result;
	}

	/**
	 * Anfrage A06
	 */

	public int getAveragePressureForTimeInterval(List<WeatherReading> list) throws Exception, RemoteException {
		int sum = 0;
		int result = 0;
		if (!list.isEmpty()) {
			for (WeatherReading wr : list) {
				sum = sum + wr.getPressure();
			}
			return result = sum / list.size();
		} else {
			LOG.info("Die übergebene Liste enthält keine Objekte");
		}
		return result = -1; // unmöglicher Luftdruck, Fehler fällt auf
	}

	/**
	 * Anfrage A07
	 */

	public int getMaxPressure(List<WeatherReading> list)throws RemoteException {
		int result = -1;
		if (!list.isEmpty()) {
			result = Collections.max(list, new WeatherPressureComparator()).getPressure();
		}
		return result;
	}

	public int getMinPressure(List<WeatherReading> list) {
		int result = -1;
		if (!list.isEmpty()) {
			result = Collections.min(list, new WeatherPressureComparator()).getPressure();
		}
		return result;
	}

	/**
	 * Anfrage A08
	 *
	 */
	public float getAverageHumidityForTimeInterval(List<WeatherReading> list) throws Exception,RemoteException {
		float sum = 0;
		float result = 0;
		if (!list.isEmpty()) {
			for (WeatherReading wr : list) {
				sum = sum + wr.getHumidity();
			}
			return result = sum / list.size();
		} else {
			LOG.info("Die übergebene Liste enthält keine Objekte");
		}
		return result = -1; // unmögliche Luftfeuchtigkeit, Fehler fällt auf
	}

	/**
	 * Anfrage A09
	 *
	 */
	public float getMaxHumidity(List<WeatherReading> list) throws RemoteException {
		float result = -1;
		for (WeatherReading w : list) {
			if (w.getHumidity() > result) {
				result = w.getHumidity();
			}
		}
		return result;
	}

	public float getMinHumidity(List<WeatherReading> list)throws RemoteException {
		float result = 101;
		for (WeatherReading w : list) {
			if (w.getHumidity() < result) {
				result = w.getHumidity();
			}
		}
		return result;
	}

	/**
	 * Anfrage 10
	 * 
	 * @throws Exception
	 */

	public List<Float> getProgressTemp(List<WeatherReading> list, LocalDate start, LocalDate end, String city)
			throws Exception, RemoteException  {
		List<WeatherReading> listFromQuery3 = this.getDataForSpecificCityAndTimeInterval(start, end, city);
		List<Float> listTemp = new ArrayList<>();

		Collections.sort(listFromQuery3, new WeatherReadingTimeStampComparator());
		for (WeatherReading wr : listFromQuery3) {
			listTemp.add(wr.getCurrentTemp());
		}

		return listTemp;

	}

	/**
	 * Anfrage 11
	 */

	public List<Integer> getProgressHumidity(List<WeatherReading> list, LocalDate start, LocalDate end, String city)
			throws Exception, RemoteException {
		List<WeatherReading> listFromQuery3 = this.getDataForSpecificCityAndTimeInterval(start, end, city);
		List<Integer> listHumidity = new ArrayList<>();
		Collections.sort(listFromQuery3, new WeatherReadingTimeStampComparator());
		for (WeatherReading wr : listFromQuery3) {
			listHumidity.add(wr.getHumidity());
		}
		return listHumidity;

	}

	/**
	 * Anfrage 12
	 */

	public List<Integer> getProgressPressure(List<WeatherReading> list, LocalDate start, LocalDate end, String city)
			throws Exception, RemoteException {
		List<WeatherReading> listFromQuery3 = this.getDataForSpecificCityAndTimeInterval(start, end, city);
		List<Integer> listPressure = new ArrayList<>();
		Collections.sort(listFromQuery3, new WeatherReadingTimeStampComparator());
		for (WeatherReading wr : listFromQuery3) {
			listPressure.add(wr.getPressure());
		}
		return listPressure;

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
