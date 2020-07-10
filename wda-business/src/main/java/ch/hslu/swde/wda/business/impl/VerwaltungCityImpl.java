package ch.hslu.swde.wda.business.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import ch.hslu.swde.wda.business.api.VerwaltungCity;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherReading;
import ch.hslu.swde.wda.persister.impl.db.PersisterCityImpl;
import ch.hslu.swde.wda.util.JpaUtil;

public class VerwaltungCityImpl extends UnicastRemoteObject implements VerwaltungCity {
	
	
	private static final long serialVersionUID = 1L;

	public VerwaltungCityImpl() throws RemoteException {
		
	}


	public City findCityByName(String city) throws RemoteException{
		PersisterCityImpl persist1 = new PersisterCityImpl();
		City city1 = persist1.findCityByName(city);
		return city1;
	}
	
	
	
	public void persistAllCities(List<City> list) throws RemoteException, Exception{
		PersisterCityImpl persist1 = new PersisterCityImpl();
		persist1.allCitites(list);
	}
	
	/**
	 * Anfrage A01
	 */
	
	public List<City> getAllCitites() throws RemoteException{
		PersisterCityImpl persist1 = new PersisterCityImpl();
		List<City> list = persist1.getAllCitites();
		return list;
	}

}
