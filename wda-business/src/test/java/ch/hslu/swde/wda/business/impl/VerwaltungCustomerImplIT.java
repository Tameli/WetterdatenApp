package ch.hslu.swde.wda.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.business.api.VerwaltungCustomer;
import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.persister.impl.db.PersisterCustomerImpl;
import ch.hslu.swde.wda.util.DbHelper;
import ch.hslu.swde.wda.util.JpaUtil;

public class VerwaltungCustomerImplIT {

//	@BeforeAll
//    public static void clearDBbeforeFirstTest() {
//        DbHelper.deleteCustomer();
//    }
//
//    @AfterEach
//    public void clearDBafterEachTests() {
//        DbHelper.deleteCustomer();
//    }

    @Test
    public void testRegisterUpdateAndDeleteCustomer() throws RemoteException {
        Customer customer = new Customer("Tester", "Test", "test@test.test", "Teststrasse", "Testort", 1234,
                "passwort");
        VerwaltungCustomer verwaltungCustomer;
        try {
            verwaltungCustomer = new VerwaltungCustomerImpl();
            verwaltungCustomer.registerCustomer(customer);

        } catch (MalformedURLException | NotBoundException e2) {
            e2.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        EntityManager em = JpaUtil.createEntityManager();
        TypedQuery<Customer> tQry = em.createNamedQuery("Customer.findByEmail", Customer.class);
        tQry.setParameter("email", "test@test.test");
        Customer dbCustomer = tQry.getSingleResult();
        em.close();

        assertEquals("test@test.test", dbCustomer.getEmail());

        Customer customer2 = new Customer("Update", "Update", "test@test.test", "Updatestrasse", "Updateort", 1234,
                "passwort");
        try {
            verwaltungCustomer = new VerwaltungCustomerImpl();
            verwaltungCustomer.updateCustomer(customer2);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        EntityManager em3 = JpaUtil.createEntityManager();
        TypedQuery<Customer> tQry3 = em3.createNamedQuery("Customer.findByEmail", Customer.class);
        tQry3.setParameter("email", "test@test.test");
        Customer db3Customer = tQry3.getSingleResult();
        em3.close();

        assertEquals("Update", db3Customer.getLastname());

        try {
            verwaltungCustomer = new VerwaltungCustomerImpl();
            verwaltungCustomer.deletCustomer(dbCustomer.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityManager em2 = JpaUtil.createEntityManager();
        TypedQuery<Customer> tQry2 = em2.createNamedQuery("Customer.findByEmail", Customer.class);
        tQry2.setParameter("email", "test@test.test");
        List<Customer> customerList = tQry2.getResultList();
        em2.close();

        assertEquals(0, customerList.size());

    }
}
