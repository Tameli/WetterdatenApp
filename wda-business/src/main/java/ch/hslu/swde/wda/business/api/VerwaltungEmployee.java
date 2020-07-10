package ch.hslu.swde.wda.business.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ch.hslu.swde.wda.domain.Employee;

public interface VerwaltungEmployee extends Remote {
	
	String RO_EMPLOYEE_NAME = "Verwaltung_Employee_RO";
    
    /**
     * Diese Methode erstellt einen neuen Mitarbeiter.
     * @param employee
     * @throws Exception
     */
	void registerEmployee(Employee employee) throws Exception, RemoteException;
	
	/**
     * Mit dieser Methode können die Mitarbeiterdaten aktualisiert werden
     * @param employee den Mitarbeiter
     * @param id die Mitarbeiter ID
     * @return boolean true if updated successfully
     * @throws Exception
     */
	boolean updateEmployee(Employee employee, long id) throws Exception , RemoteException;
	
	/**
     * Mit dieser Methode kann ein Mitarbeiter gelöscht werden
     * @param id
     * @return true if deleted successfully
     * @throws Exception 
     * 
     */
	
	boolean deletEmployee(long id) throws Exception, RemoteException;
    
    /**
     * Mit dieser Methode werden angemeldete Mitarbeiter zurückgegeben
     * @param id
     * @return Employee that was found
     */
    Employee findEmployee(long id) throws Exception, RemoteException;

    /**
     * Diese Methode gibt einen Mitarbeiter zurück
     * @param name als Name
     * @param pwd als Passwort
     * @return Employee
     * @throws Exception
     */
    Employee findEmployeeByPassword(String name, String pwd) throws Exception, RemoteException;
    

}
