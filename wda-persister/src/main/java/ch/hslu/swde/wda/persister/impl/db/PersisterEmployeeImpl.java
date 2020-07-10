package ch.hslu.swde.wda.persister.impl.db;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.persister.api.PersisterEmployee;
import ch.hslu.swde.wda.util.JpaUtil;

public class PersisterEmployeeImpl implements PersisterEmployee {

	private static final Logger LOG = LogManager.getLogger(PersisterEmployeeImpl.class);

	public PersisterEmployeeImpl() {

	}

	/**
	 * Speichert den übergebenen Mitarbeiter.
	 *
	 * @param employee der zu speicherende Kunde
	 * @return der gespeicherte Mitarbeiter
	 * @throws Exception falls das Speichern misslingen sollte
	 */
	@Override
	public void saveEmployee(Employee employee) throws Exception {

		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		em.persist(employee);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public boolean updateEmployee(Employee employee, long id) throws Exception {
		
		boolean result = false;
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<Employee> tQuery = em.createNamedQuery("Employee.findById", Employee.class);
		tQuery.setParameter("id", id);
		Employee employeeUpdated = employee;
		Employee employeeQuery = tQuery.getSingleResult();
		if (employeeQuery.getId() == (id)) {
			em.remove(employeeQuery);
			em.getTransaction().commit();
			result = true;
			em.close();
	        
	        EntityManager em2 = JpaUtil.createEntityManager();
	        em2.getTransaction().begin();
	        em2.persist(employeeUpdated);
	        em2.getTransaction().commit();
	        em2.close();
		}
				
		
		
		return result;
	}

	@Override
	public boolean deleteEmployee(Employee employee) throws Exception {
		long id = employee.getId();
		boolean result = false;
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<Employee> tQuery = em.createNamedQuery("Employee.findById", Employee.class);
		tQuery.setParameter("id", id);
		Employee employeeQuery = tQuery.getSingleResult();
		em.remove(employeeQuery);
		em.getTransaction().commit();
		em.close();

		result = true;
//	        try {
//	            EntityManager em = JpaUtil.createEntityManager();
//	            em.getTransaction().begin();
//	            em.remove(customer);
//	            em.getTransaction().commit();
//	            em.close();
//	            result = true;
//	        } catch (Exception e) {
//	            System.out.println("Die Person konnte nicht gelöscht werden.");
//	        } 
		return result;
	}

	@Override
	public Employee findEmployee(long id) throws Exception {
		EntityManager em = JpaUtil.createEntityManager();
		TypedQuery<Employee> tQry = em.createNamedQuery("Employee.findById", Employee.class);
		tQry.setParameter("id", id);
		try {
			Employee queryResult = tQry.getSingleResult();
			return queryResult;
		} catch (Exception e) {
			LOG.error("Mitarbeiter|in konnte nicht gefunden werden.");
		}
		return null; // Was zurückgeben bei nicht gefundenem Objekt?

	}

    @Override
    public Employee findEmployeeByPassword(String name, String pwd) throws Exception {
        EntityManager em = JpaUtil.createEntityManager();
        TypedQuery<Employee> tQry = em.createNamedQuery("Employee.findByPassword", Employee.class);
        tQry.setParameter("lastname", name);
        tQry.setParameter("password", pwd);
        try {
            Employee queryResult = tQry.getSingleResult();
            return queryResult;
        }catch (Exception e) {
            LOG.error("Mitarbeiter|in konnte nicht gefunden werden.");
        }
        return null;
    }

}
