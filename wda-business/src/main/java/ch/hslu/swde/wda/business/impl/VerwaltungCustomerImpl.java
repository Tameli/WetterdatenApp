package ch.hslu.swde.wda.business.impl;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.business.api.VerwaltungCustomer;
import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.persister.api.PersisterCustomer;
import ch.hslu.swde.wda.persister.impl.db.PersisterCustomerImpl;

public class VerwaltungCustomerImpl extends UnicastRemoteObject implements VerwaltungCustomer{
    
    private static final Logger LOG = LogManager.getLogger(VerwaltungCustomerImpl.class);

//    private final PersisterCustomer persisterCustomer;
    
//    public VerwaltungCustomerImpl(final PersisterCustomer persisterCustomer)throws RemoteException {
//        this.persisterCustomer = persisterCustomer;
//    }
    
    public VerwaltungCustomerImpl() throws RemoteException, MalformedURLException, NotBoundException{
        
    }
    
    @Override
    public void registerCustomer(Customer customer) throws Exception, RemoteException {
        PersisterCustomerImpl persister = new PersisterCustomerImpl();
        persister.saveCustomer(customer);
        //        persisterCustomer.saveCustomer(customer);
        
        LOG.info("Kunde wurde registriert: " + customer.toString());
        
    }

    @Override
    public Customer findCustomer(String emailAddress) throws Exception, RemoteException{
        PersisterCustomerImpl persister = new PersisterCustomerImpl();

        return persister.findCustomer(emailAddress);
        
    }

    @Override
    public boolean updateCustomer(Customer customer) throws Exception, RemoteException{
        PersisterCustomerImpl persister = new PersisterCustomerImpl();

        return persister.updateCustomer(customer);
        
    }

    @Override
    public boolean deletCustomer(String emailAddress) throws Exception, RemoteException {
        Customer customer = findCustomer(emailAddress);
        PersisterCustomerImpl persister = new PersisterCustomerImpl();

        return persister.deleteCustomer(customer);
    }
    
    

}
