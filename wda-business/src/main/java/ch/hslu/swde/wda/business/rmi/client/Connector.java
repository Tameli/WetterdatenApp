package ch.hslu.swde.wda.business.rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import ch.hslu.swde.wda.business.api.VerwaltungCity;
import ch.hslu.swde.wda.business.api.VerwaltungCustomer;
import ch.hslu.swde.wda.business.api.VerwaltungEmployee;
import ch.hslu.swde.wda.business.api.VerwaltungWeatherData;

/**
 * Erstellt den Security Manager und die zwei Stubs, die auf der Clientseite für die RMI Aufrufe benötigt werden.
 * @author lunas
 *
 */

public class Connector {
	int rmiPort;
	String rmiServerIP;
	
	public static void setSecurityManager() throws RemoteException, MalformedURLException, NotBoundException{
		if (System.getSecurityManager() == null) {

			System.setProperty("java.security.policy", "checker.policy");

			System.setSecurityManager(new SecurityManager());
		}
	}
	
	public static VerwaltungCity getCityStub(int rmiPort, String rmiServerIP) throws MalformedURLException, RemoteException, NotBoundException {
		String url = "rmi://" + rmiServerIP + ":" + rmiPort + "/" + VerwaltungCity.RO_CITY_NAME;
		VerwaltungCity cityStub = (VerwaltungCity) Naming.lookup(url);
		return cityStub;
	}
	
	
	public static VerwaltungWeatherData getWeatherDataStub(int rmiPort, String rmiServerIP) throws MalformedURLException, RemoteException, NotBoundException{
		String url = "rmi://" + rmiServerIP + ":" + rmiPort + "/" + VerwaltungWeatherData.RO_Weather_NAME;
		VerwaltungWeatherData weatherStub = (VerwaltungWeatherData) Naming.lookup(url);
		return weatherStub;
	}
	
	public static VerwaltungCustomer getCustomerStub(int rmiPort, String rmiServerIP) throws MalformedURLException, RemoteException, NotBoundException{
		String url = "rmi://" + rmiServerIP + ":" + rmiPort + "/" + VerwaltungCustomer.RO_CUSTOMER_NAME;
		VerwaltungCustomer customerStub = (VerwaltungCustomer) Naming.lookup(url);
		return customerStub;
	}
	
	public static VerwaltungEmployee getEmployeeStub(int rmiPort, String rmiServerIP) throws MalformedURLException, RemoteException, NotBoundException{
		String url = "rmi://" + rmiServerIP + ":" + rmiPort + "/" + VerwaltungEmployee.RO_EMPLOYEE_NAME;
		VerwaltungEmployee employeeStub = (VerwaltungEmployee) Naming.lookup(url);
		return employeeStub;
	}
	
	
}
