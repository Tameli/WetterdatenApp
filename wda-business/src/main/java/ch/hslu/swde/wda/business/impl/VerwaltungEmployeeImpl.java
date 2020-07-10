package ch.hslu.swde.wda.business.impl;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.business.api.VerwaltungEmployee;
import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.persister.api.PersisterCustomer;
import ch.hslu.swde.wda.persister.api.PersisterEmployee;
import ch.hslu.swde.wda.persister.impl.db.PersisterCustomerImpl;
import ch.hslu.swde.wda.persister.impl.db.PersisterEmployeeImpl;

public class VerwaltungEmployeeImpl extends UnicastRemoteObject implements VerwaltungEmployee {

	private static final Logger LOG = LogManager.getLogger(VerwaltungEmployeeImpl.class);

	

	public VerwaltungEmployeeImpl()  throws RemoteException, MalformedURLException, NotBoundException {
		
	}
	

	@Override
	public void registerEmployee(Employee employee) throws Exception, RemoteException {
		PersisterEmployeeImpl persister = new PersisterEmployeeImpl();
        persister.saveEmployee(employee);

		LOG.info("Kunde wurde registriert: " + employee.toString());

	}

	@Override
	public boolean updateEmployee(Employee employee, long id) throws Exception, RemoteException {
		PersisterEmployeeImpl persister = new PersisterEmployeeImpl();
		return persister.updateEmployee(employee, id);
	}

	@Override
	public boolean deletEmployee(long id) throws Exception, RemoteException {
		Employee employee = findEmployee(id);
		PersisterEmployeeImpl persister = new PersisterEmployeeImpl();

		return persister.deleteEmployee(employee);
	}

	@Override
	public Employee findEmployee(long id) throws Exception, RemoteException {
		PersisterEmployeeImpl persister = new PersisterEmployeeImpl();
		return persister.findEmployee(id);
	}
	
	@Override
	public Employee findEmployeeByPassword (String name, String pwd) throws Exception, RemoteException {
		PersisterEmployeeImpl persister = new PersisterEmployeeImpl();
	    return persister.findEmployeeByPassword(name, pwd);
	}



}
