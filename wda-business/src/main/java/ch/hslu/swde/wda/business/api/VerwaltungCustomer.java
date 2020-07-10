package ch.hslu.swde.wda.business.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ch.hslu.swde.wda.domain.Customer;

public interface VerwaltungCustomer extends Remote {
	
	String RO_CUSTOMER_NAME = "Verwaltung_Customer_RO";
    
    /**
     * Mit dieser Methode registriert sich ein Kunde beim Wetterapp
     * @param customer
     */
    void registerCustomer(Customer customer) throws Exception, RemoteException;
    
    /**
     * Mit dieser Methode werden angemeldete Kunden zurückgegeben
     * @param emailAddress
     * @return Customer that was found
     */
    Customer findCustomer(String emailAddress) throws Exception, RemoteException;
    
    /**
     * Mit dieser Methode können die Kundendaten aktualisiert werden
     * @param Customer
     * @return boolean true if updated successfully
     * @throws Exception
     */
    
    boolean updateCustomer(Customer customer) throws Exception, RemoteException;
    
    /**
     * Mit dieser Methode kann ein Kunde gelöscht werden
     * @param emailAddress
     * @return true if deleted successfully
     * @throws Exception 
     * 
     */
        
    boolean deletCustomer(String emailAddress) throws Exception,RemoteException;

   

}
