package ch.hslu.swde.wda.business.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import ch.hslu.swde.wda.domain.City;

public interface VerwaltungCity extends Remote {
	
	String RO_CITY_NAME = "Verwaltung_City_RO";
	
	/**
	 * Findet anhand des Städtenamens das zugehörige Objekt.
	 * @param Name eines Stadt, die der Verwaltung vom User über das GUI übergeben wird
	 * @return das Objekt der Stadt, das in der DB gefunden wurde
	 */

	public City findCityByName(String city) throws RemoteException;
	
	/** 
	 * Alle Städte persistieren.
	 * @param list alle Städte als Liste
	 * @throws Exception wenn die Persisitierung nicht funktioniert hat.
	 */
	
	public void persistAllCities(List<City> list) throws RemoteException, Exception;
	
	
	/**
	 * Retourniert alle persistierten Städte.
	 * @return alle Städte als Liste
	 */
	public List<City> getAllCitites() throws RemoteException;
}
