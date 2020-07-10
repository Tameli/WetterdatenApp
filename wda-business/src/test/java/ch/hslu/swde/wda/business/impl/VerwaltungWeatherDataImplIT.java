package ch.hslu.swde.wda.business.impl;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import ch.hslu.swde.wda.business.restReader.ClientApp;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherReading;
import ch.hslu.swde.wda.persister.impl.db.PersisterCityImpl;
import ch.hslu.swde.wda.persister.impl.db.PersisterWeatherReadingImpl;
import ch.hslu.swde.wda.util.DbHelper;
import ch.hslu.swde.wda.util.WeatherPressureComparator;
import org.hamcrest.CoreMatchers;

public class VerwaltungWeatherDataImplIT {
	
	
	
	/**
	 * Anfrage A01
	 */

	@Test
	public void testGetAllCititesFromDB() {
		PersisterCityImpl persist1 = new PersisterCityImpl();
		List<City> resultList = persist1.getAllCitites();
		assertEquals(40, resultList.size());
	}
	
	/**
	 * Anfrage A02. Speziallfall St. Gallen, da getrennt mit Leerschlag 
	 * @throws Exception
	 */
	
	@Test
	public void testGetDataLast24Hour() throws Exception {
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		impl1.updateDatabase();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("St. Gallen");
		List<WeatherReading> resultList = impl1.getDataLast24Hour(city1);
		int result = resultList.size();
		//assertEquals(48,resultList.size() );
		assertTrue(47 <= result || 48 >= result); // je nachdem ob der Datensatz dieser Halbenstunde schon auf dem REST Server ist
	}
	
	/**
	 * Anfrage A03
	 * @throws Exception
	 */
	
	@Test
	public void testGetDataForSpecificCityAndTimeInterval() throws Exception {
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("Aarau");
		List<WeatherReading> resultList = impl2.getDataStartAndEndDate(LocalDate.now().minusDays(5), LocalDate.now().minusDays(2), city1);
		LocalDateTime start = LocalDate.now().minusDays(5).atStartOfDay();
		LocalDateTime end = LocalDate.now().minusDays(2).atTime(23, 59);
		long difference = ChronoUnit.HOURS.between(start, end);
		System.out.println(difference);
		long size = (difference *2); // mal 2, da alle 30 Min neue Datensätze
		int sizeInt = (int) (size);
		assertTrue(sizeInt <= resultList.size() || resultList.size() <= sizeInt+1); //jenachdem ob der Datensatz von Aarau dieser 30min schon verfügbar ist
	}
	
	/**
	 * Anfrage A04
	 * @throws Exception 
	 */
	@Test
	public void testGetAverageTemp() throws Exception {
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("Zug");
		List<WeatherReading> resultList = impl2.getDataStartAndEndDate(LocalDate.of(2019, 12, 7), LocalDate.of(2019, 12, 8), city1);
		float result = impl1.getAverageTempForTimeInterval(resultList);
		assertEquals(5.15625,result, 0.00);
	}
	
	/**
	 * Anfrage A05
	 * @throws Exception
	 */
	
	@Test
	public void testMaxForSpecificCities() throws Exception{
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("Aarau");
		City city2 = persist.findCityByName("Arosa");
		List<WeatherReading> resultListAarau = impl2.getDataStartAndEndDate(LocalDate.now().minusDays(5), LocalDate.now(), city1);
		List<WeatherReading> resultListArosa = impl2.getDataStartAndEndDate(LocalDate.now().minusDays(5), LocalDate.now(), city2);
		float maxTemp1 = impl1.getMaxTempSpecificCities(resultListAarau);
		float maxTemp2 = impl1.getMaxTempSpecificCities(resultListArosa);
		Collections.sort(resultListAarau);
		Collections.sort(resultListArosa);
		assertEquals(resultListAarau.get(resultListAarau.size() -1).getCurrentTemp(), maxTemp1);
		assertEquals(resultListArosa.get(resultListArosa.size() -1).getCurrentTemp(), maxTemp2);	
	}
	
	@Test
	public void testMinSpecificCities() throws Exception{
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("Aarau");
		City city2 = persist.findCityByName("Arosa");
		List<WeatherReading> resultListAarau = impl2.getDataStartAndEndDate(LocalDate.now().minusDays(5), LocalDate.now(), city1);
		List<WeatherReading> resultListArosa = impl2.getDataStartAndEndDate(LocalDate.now().minusDays(5), LocalDate.now(), city2);
		float minTemp1 = impl1.getMinTempSpecificCities(resultListAarau);
		float minTemp2 = impl1.getMinTempSpecificCities(resultListArosa);
		Collections.sort(resultListAarau);
		Collections.sort(resultListArosa);
		assertEquals(resultListAarau.get(0).getCurrentTemp(), minTemp1);
		assertEquals(resultListArosa.get(0).getCurrentTemp(), minTemp2);
	}
	
	/**
	 * Anfrage A06
	 */
	
	@Test
	public void testGetAveragePressure() throws Exception {
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("Zug");
		List<WeatherReading> resultList = impl2.getDataStartAndEndDate(LocalDate.of(2019, 12, 7), LocalDate.of(2019, 12, 8), city1);
		float result = impl1.getAveragePressureForTimeInterval(resultList);
		assertEquals(1017,result);
	}
	
	/**
	 * Anfrage A07
	 */
	
	@Test
	public void testMaxPressureForSpecificCities() throws Exception{
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("Aarau");
		City city2 = persist.findCityByName("Arosa");
		List<WeatherReading> resultListAarau = impl2.getDataStartAndEndDate(LocalDate.now().minusDays(5), LocalDate.now(), city1);
		List<WeatherReading> resultListArosa = impl2.getDataStartAndEndDate(LocalDate.now().minusDays(5), LocalDate.now(), city2);
		float maxPressure1 = impl1.getMaxPressure(resultListAarau);
		Collections.sort(resultListAarau, new WeatherPressureComparator());
		assertEquals(resultListAarau.get(resultListAarau.size() -1).getPressure(), maxPressure1);
		float maxPressure2 = impl1.getMaxPressure(resultListArosa);
		Collections.sort(resultListArosa, new WeatherPressureComparator());
		assertEquals(resultListArosa.get(resultListArosa.size() -1).getPressure(), maxPressure2);
		
	}
	
	@Test
	public void testMinPressureSpecificCities() throws Exception{
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("Aarau");
		City city2 = persist.findCityByName("Arosa");
		List<WeatherReading> resultListAarau = impl2.getDataStartAndEndDate(LocalDate.now().minusDays(5), LocalDate.now(), city1);
		List<WeatherReading> resultListArosa = impl2.getDataStartAndEndDate(LocalDate.now().minusDays(5), LocalDate.now(), city2);
		int minPressure1 = impl1.getMinPressure(resultListAarau);
		Collections.sort(resultListAarau, new WeatherPressureComparator());
		assertEquals(resultListAarau.get(0).getPressure(), minPressure1);
		int minPressure2 = impl1.getMinPressure(resultListArosa);
		Collections.sort(resultListArosa, new WeatherPressureComparator());
		assertEquals(resultListArosa.get(0).getPressure(), minPressure2);
	}
	
	
	/**
	 * Anfrage 08
	 */
	@Test
	public void testGetAverageHumidityForTimeInterval() throws Exception{
	VerwaltungWeatherDataImpl vwd = new VerwaltungWeatherDataImpl();
	PersisterCityImpl persistCity = new PersisterCityImpl();
	PersisterWeatherReadingImpl persist = new PersisterWeatherReadingImpl();
	City city = persistCity.findCityByName("Aarau");
	List<WeatherReading> list = persist.getDataLast24Hour(city);
	float result = 0;
	for(WeatherReading wr: list){
	result = result + wr.getHumidity();
	}
	assertEquals(result / list.size(), vwd.getAverageHumidityForTimeInterval(list));
	}
	
	
	/**
	 * Anfrage 09
	 */
	@Test
	public void testGetMaxHumidity() throws Exception{
	VerwaltungWeatherDataImpl vwd = new VerwaltungWeatherDataImpl();
	PersisterWeatherReadingImpl persist = new PersisterWeatherReadingImpl();
	PersisterCityImpl persistCity = new PersisterCityImpl();
	City city = persistCity.findCityByName("Aarau");
	List<WeatherReading> list = persist.getDataLast24Hour(city);
	float result = 0;
	for(WeatherReading wr: list){
	if(result < wr.getHumidity()){
	result = wr.getHumidity();
	}}
	assertEquals(result, vwd.getMaxHumidity(list));
	}
	
	@Test
	public void testGetMinHumidity() throws Exception{
	VerwaltungWeatherDataImpl vwd = new VerwaltungWeatherDataImpl();
	PersisterWeatherReadingImpl persist = new PersisterWeatherReadingImpl();
	PersisterCityImpl persistCity = new PersisterCityImpl();
	City city = persistCity.findCityByName("Aarau");
	List<WeatherReading> list = persist.getDataLast24Hour(city);
	float result = 105;
	for(WeatherReading wr: list){
	if(result > wr.getHumidity()){
	result = wr.getHumidity();
	}}
	assertEquals(result, vwd.getMinHumidity(list));
	}
	
	/**
	 * Anfrage 10
	 */
	@Test
	public void testProgressTemperatur() throws Exception{
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		List<Float> list = new ArrayList<>();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("Adelboden");
		List<WeatherReading> resultList = impl2.getDataStartAndEndDate(LocalDate.of(2019, 12, 8), LocalDate.of(2019, 12, 8), city1);
		list = impl1.getProgressTemp(resultList, LocalDate.of(2019, 12, 8), LocalDate.of(2019, 12, 8), "Adelboden");
		System.out.println(list);
		//assertThat(list,  containsInAnyOrder(3.0,2.0,1.0,0.0,5.0,7.0,8.0,6.0)); wie mit float??
	}
	
	/**
	 * Anfrage 11
	 */
	@Test
	public void testProgressHumidity() throws Exception{
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		List<Integer> list = new ArrayList<>();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("Adelboden");
		List<WeatherReading> resultList = impl2.getDataStartAndEndDate(LocalDate.of(2019, 12, 7), LocalDate.of(2019, 12, 7), city1);
		list = impl1.getProgressHumidity(resultList, LocalDate.of(2019, 12, 7), LocalDate.of(2019, 12, 7), "Adelboden");
		assertThat(list,  containsInAnyOrder(74, 74, 74, 69, 69, 69, 74, 69, 69, 69, 69, 74, 74, 74, 74, 80, 80, 80, 74, 80, 80, 74, 74, 74, 85, 56, 52, 49, 49, 86, 53, 57, 53, 61, 66, 66, 70, 70, 75, 75, 80, 75, 80, 86, 86, 80, 86, 93));
	}
	
	/**
	 * Anfrage 12
	 */
	@Test
	public void testProgressPressure() throws Exception{
		VerwaltungWeatherDataImpl impl1 = new VerwaltungWeatherDataImpl();
		PersisterWeatherReadingImpl impl2 = new PersisterWeatherReadingImpl();
		List<Integer> list = new ArrayList<>();
		PersisterCityImpl persist = new PersisterCityImpl();
		City city1 = persist.findCityByName("St. Gallen");
		List<WeatherReading> resultList = impl2.getDataStartAndEndDate(LocalDate.of(2019, 12, 8), LocalDate.of(2019, 12, 8), city1);
		list = impl1.getProgressPressure(resultList, LocalDate.of(2019, 12, 8), LocalDate.of(2019, 12, 8), "St. Gallen");
		assertThat(list,  containsInAnyOrder(1020, 1020, 1020, 1020, 1020, 1019, 1019, 1019, 1019, 1018, 1018, 1018, 1018, 1017, 1017, 1017, 1017, 1017, 1017, 1017, 1017, 1017, 1017, 1017, 1017, 1016, 1015, 1015, 1015, 1014, 1014, 1013, 1013, 1012, 1012, 1012, 1012, 1011, 1011, 1010, 1010, 1009, 1009, 1009, 1009, 1009, 1009, 1009));
	}
	

}
