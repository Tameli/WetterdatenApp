package ch.hslu.swde.wda.business.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherReading;

public interface VerwaltungWeatherData extends Remote {

	String RO_Weather_NAME = "Verwaltung_WeatherData_RO";

	/**
	 * Mit dieser Methode werden die Wetterdaten der letzten 24h zurückgegeben.
	 * 
	 * @param Stadt vom Typ City, Abfrage der Objekts anhand des Strings findet im
	 *              Controller statt
	 * @return Liste vom Typ Weatherreading
	 *
	 */

	List<WeatherReading> getDataLast24Hour(City city) throws Exception, RemoteException;

	/**
	 * Mit dieser Methode werden die Wetterdaten zwischen Start und
	 * Endzeitpunkt(übergeben vom GUI) für eine spezifische Stadt zurückgegeben.
	 * 
	 * @param Start und Enddatum vom GUI und die gwünschte Stadt vom GUI
	 * @return Liste vom Typ Weatherreading
	 */

	List<WeatherReading> getDataForSpecificCityAndTimeInterval(LocalDate start, LocalDate end, String city)
			throws Exception, RemoteException;

	/**
	 * Mit dieser Methode wird die durchschnittliche Temperatur für eine Liste von
	 * Wetterdaten berechnet.
	 * 
	 * @param Liste vom Typ Weatherreading
	 * @return average Temp als float Wert
	 */

	public float getAverageTempForTimeInterval(List<WeatherReading> list) throws Exception, RemoteException;

	/**
	 * Mit dieser Methode wird die maximale Temperatur für eine Liste von
	 * Wetterdaten berechnet.
	 * 
	 * @param Liste vom Typ Weatherreading
	 * @return max Temp als float Wert
	 */

	public float getMaxTempSpecificCities(List<WeatherReading> list) throws RemoteException;

	/**
	 * Mit dieser Methode wird die minimale Temperatur für eine Liste von
	 * Wetterdaten berechnet.
	 * 
	 * @param Liste vom Typ Weatherreading
	 * @return min Temp als float Wert
	 */

	public float getMinTempSpecificCities(List<WeatherReading> list) throws RemoteException;

	/**
	 * Mit dieser Methode wird der durchschnittliche Lufdruck für eine Liste von
	 * Wetterdaten berechnet.
	 * 
	 * @param Liste vom Typ Weatherreading
	 * @return average Pressure als int Wert
	 */

	public int getAveragePressureForTimeInterval(List<WeatherReading> list) throws Exception, RemoteException;

	/**
	 * Mit dieser Methode wird der maximale Luftdruck für eine Liste von Wetterdaten
	 * berechnet.
	 * 
	 * @param Liste vom Typ Weatherreading
	 * @return max Pressure als int Wert
	 */

	public int getMaxPressure(List<WeatherReading> list) throws RemoteException;

	/**
	 * Mit dieser Methode wird der minmale Luftdruck für eine Liste von Wetterdaten
	 * berechnet.
	 * 
	 * @param Liste vom Typ Weatherreading
	 * @return max Pressure als int Wert
	 */

	public int getMinPressure(List<WeatherReading> list) throws RemoteException;

	/**
	 * Mit dieser Methode wird die durchschnitttliche Luftfeuchtigkeit für einen
	 * Zeitabschnitt in einer bestimmten Zeit ausgegeben.
	 * 
	 * @throws Exception
	 * @throws RemoteException
	 */

	public float getAverageHumidityForTimeInterval(List<WeatherReading> list) throws Exception, RemoteException;

	/**
	 * Mit dieser Methode wird die maximale Luftfeuchtigkeit für eine Liste von
	 * Wetterdaten berechnet.
	 * 
	 * @param Liste vom Typ Weatherreading
	 * @return max humidity als float Wert
	 */

	public float getMaxHumidity(List<WeatherReading> list) throws RemoteException;

	/**
	 * Mit dieser Methode wird die minimale Luftfeuchtikeit für eine Liste von
	 * Wetterdaten berechnet.
	 * 
	 * @param Liste vom Typ Weatherreading
	 * @return max humidity als float Wert
	 */

	public float getMinHumidity(List<WeatherReading> list) throws RemoteException;

	/**
	 * Mit dieser Methoder wird der Verlauf der Temperatur in Celsius einer
	 * spezifischen Stadt in einem Zeitintervall(Sartdatum bis Enddatum) angegeben
	 * 
	 * @param Start und Enddatum vom GUI und die gwünschte Stadt vom GUI
	 * @return Liste vom Typ Float
	 */

	public List<Float> getProgressTemp(List<WeatherReading> list, LocalDate start, LocalDate end, String city)
			throws Exception, RemoteException;

	/**
	 * Mit dieser Methoder wird der Verlauf der Luftfeuchtigkeit einer spezifischen
	 * Stadt in einem Zeitintervall(Sartdatum bis Enddatum) angegeben
	 * 
	 * @param Start und Enddatum vom GUI und die gwünschte Stadt vom GUI
	 * @return Liste vom Typ Integer
	 */

	public List<Integer> getProgressHumidity(List<WeatherReading> list, LocalDate start, LocalDate end, String city)
			throws Exception, RemoteException;

	/**
	 * Mit dieser Methoder wird der Verlauf des Luftdruckes einer spezifischen Stadt
	 * in einem Zeitintervall(Sartdatum bis Enddatum) angegeben
	 * 
	 * @param Start und Enddatum vom GUI und die gwünschte Stadt vom GUI
	 * @return Liste vom Typ Integer
	 */

	public List<Integer> getProgressPressure(List<WeatherReading> list, LocalDate start, LocalDate end, String city)
			throws Exception, RemoteException;

	/**
	 * Exportieren der angeforderten Daten in ein txt File
	 * 
	 * @param file:  Pfad des zu erstellenden Files
	 * @param value: String der ins File geschrieben werden soll
	 * 
	 */

	public void getTxtFilefromList(final String file, final String value) throws RemoteException;

	/**
	 * Konvertierung der Liste von Wetterdaten in ein pretty JSON
	 */

	public String converstListtoJSON(List<WeatherReading> list) throws RemoteException;
	
	/**
	 * Konvertierung der Liste von Wetterdaten als String in ein pretty JSON
	 */
	
	public String convertStringListToJSON(List<String> list)throws RemoteException;
	
	/**
	 * Methode um nur die neusten Daten(seit dem letzten Update) aus der DB zu holen
	 * 
	 * @throws Exception
	 */

	public void updateDatabase() throws Exception, RemoteException;

}
