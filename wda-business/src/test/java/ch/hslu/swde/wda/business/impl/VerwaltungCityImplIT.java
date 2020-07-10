package ch.hslu.swde.wda.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.domain.City;

public class VerwaltungCityImplIT {
	
	@Test
	public void testFindCityByName() throws RemoteException {
		VerwaltungCityImpl impl1 = new VerwaltungCityImpl();
		City city = impl1.findCityByName("Zug");
		assertEquals(6300, city.getZip());
	}
	
	/**
	 * Test bei Abfrage einer nicht vorhandenen Stadt
	 */
	@Test
	public void testUnknownCity() throws RemoteException{
		VerwaltungCityImpl impl1 = new VerwaltungCityImpl();
		City city = impl1.findCityByName("Pfaffhausen");
		assertEquals(1234, city.getZip());
	}

}
