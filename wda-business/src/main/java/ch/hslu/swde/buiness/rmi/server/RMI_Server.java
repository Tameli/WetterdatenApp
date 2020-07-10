package ch.hslu.swde.buiness.rmi.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ch.hslu.swde.wda.business.api.VerwaltungCity;
import ch.hslu.swde.wda.business.api.VerwaltungCustomer;
import ch.hslu.swde.wda.business.api.VerwaltungEmployee;
import ch.hslu.swde.wda.business.api.VerwaltungWeatherData;
import ch.hslu.swde.wda.business.impl.*;
import ch.hslu.swde.wda.domain.Customer;

public class RMI_Server {
	public static void main(String[] args) throws RemoteException, NotBoundException {

		String hostIp = "10.155.107.96";
		int rmiPort = 1099;

		System.setProperty("java.rmi.sever.hostname", hostIp);// Diese Ip wird für den Stub eingeplant -> wenn diese
																// nicht erreichbar ist(ping) wird es nicht
																// funktionieren:Fehlermeldung: Connection refused
		try {
			Registry reg = null;

			reg = LocateRegistry.createRegistry(rmiPort);
			
			VerwaltungCity verwaltungCity = new VerwaltungCityImpl();
			VerwaltungWeatherData verwaltungWeather = new VerwaltungWeatherDataImpl();
			VerwaltungCustomer verwaltungCustomer = new VerwaltungCustomerImpl();
			VerwaltungEmployee verwaltungEmployee = new VerwaltungEmployeeImpl();
			
			

			if (reg != null) {
				reg.rebind(VerwaltungCity.RO_CITY_NAME, verwaltungCity);
				reg.rebind(VerwaltungWeatherData.RO_Weather_NAME, verwaltungWeather);
				reg.rebind(VerwaltungCustomer.RO_CUSTOMER_NAME, verwaltungCustomer);
				reg.rebind(VerwaltungEmployee.RO_EMPLOYEE_NAME, verwaltungEmployee);
				System.out.println("\n Server gestartet:[" + hostIp + ":" + rmiPort + "]");

				System.out.println("\n Beenden mit ENTER Taste!\n");
				new java.util.Scanner(System.in).hasNextLine();

				reg.unbind(VerwaltungCity.RO_CITY_NAME);
				reg.unbind(VerwaltungWeatherData.RO_Weather_NAME);
				reg.unbind(VerwaltungCustomer.RO_CUSTOMER_NAME);
				reg.unbind(VerwaltungEmployee.RO_EMPLOYEE_NAME);

				System.exit(0);
			}
		} catch (Exception e) {

		}
	}
}
