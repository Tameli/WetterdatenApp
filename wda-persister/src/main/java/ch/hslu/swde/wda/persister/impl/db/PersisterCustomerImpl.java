package ch.hslu.swde.wda.persister.impl.db;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.persister.api.PersisterCustomer;
import ch.hslu.swde.wda.util.JpaUtil;

public class PersisterCustomerImpl implements PersisterCustomer {

    private static final Logger LOG = LogManager.getLogger(PersisterCustomerImpl.class);

    public PersisterCustomerImpl() {

    }

    @Override
    public void saveCustomer(Customer customer) throws Exception {

        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();
        em.persist(customer);
        em.getTransaction().commit();
        em.close();

    }

    @Override
    public boolean updateCustomer(Customer customer) throws Exception {

        boolean result = false;
        String email = customer.getEmail();
        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Customer> tQuery = em.createNamedQuery("Customer.findByEmail", Customer.class);
        tQuery.setParameter("email", email);
        Customer customerUpdated = customer;
        Customer customerQuery = tQuery.getSingleResult();
        if (customerQuery.getEmail().equals(email)) {
            em.remove(customerQuery);
            em.getTransaction().commit();
            result = true;
        }
        em.close();
        
        EntityManager em2 = JpaUtil.createEntityManager();
        em2.getTransaction().begin();
        em2.persist(customerUpdated);
        em2.getTransaction().commit();
        em2.close();
        return result;
    }

    @Override
    public boolean deleteCustomer(Customer customer) throws Exception {
        String email = customer.getEmail();
        boolean result = false;
        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Customer> tQuery = em.createNamedQuery("Customer.findByEmail", Customer.class);
        tQuery.setParameter("email", email);
        Customer customerQuery = tQuery.getSingleResult();
        em.remove(customerQuery);
        em.getTransaction().commit();
        em.close();

        result = true;
//        try {
//            EntityManager em = JpaUtil.createEntityManager();
//            em.getTransaction().begin();
//            em.remove(customer);
//            em.getTransaction().commit();
//            em.close();
//            result = true;
//        } catch (Exception e) {
//            System.out.println("Die Person konnte nicht gelöscht werden.");
//        } 
        return result;

    }

    @Override
    public Customer findCustomer(String email) throws Exception {
        EntityManager em = JpaUtil.createEntityManager();
        TypedQuery<Customer> tQry = em.createNamedQuery("Customer.findByEmail", Customer.class);
        tQry.setParameter("email", email);
        try {
            Customer queryResult = tQry.getSingleResult();
            return queryResult;
        } catch (Exception e) {
            LOG.error("Die Person konnte nicht gefunden werden.");
        }
        return null; // Was zurückgeben bei nicht gefundenem Objekt?

    }

}
