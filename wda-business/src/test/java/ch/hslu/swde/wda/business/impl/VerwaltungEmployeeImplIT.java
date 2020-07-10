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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.business.api.VerwaltungCustomer;
import ch.hslu.swde.wda.business.api.VerwaltungEmployee;
import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.persister.impl.db.PersisterCustomerImpl;
import ch.hslu.swde.wda.persister.impl.db.PersisterEmployeeImpl;
import ch.hslu.swde.wda.util.DbHelper;
import ch.hslu.swde.wda.util.JpaUtil;

public class VerwaltungEmployeeImplIT {

	// Add your Tests here

//	@BeforeAll
//	public static void clearDBbeforeFirstTest() {
//		DbHelper.deleteEmployee();
//	}
//
//	@AfterEach
//	public void clearDBafterEachTests() {
//		DbHelper.deleteEmployee();
//	}

	// Methode, um die Mitarbeitende zu aktualisieren

	@Test
	public void testEmployee() throws RemoteException, MalformedURLException, NotBoundException {
		PersisterEmployeeImpl pers = new PersisterEmployeeImpl();

		VerwaltungEmployee verwaltungEmployee = new VerwaltungEmployeeImpl();

		Employee employee = new Employee("Kevin", "Test", "Password");
		try {
			verwaltungEmployee.registerEmployee(employee);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		EntityManager em = JpaUtil.createEntityManager();
		TypedQuery<Employee> tQuery = em.createNamedQuery("Employee.findByPassword", Employee.class);
		tQuery.setParameter("lastname", "Test");
		tQuery.setParameter("password", "Password");
		Employee employeeQuery = tQuery.getSingleResult();
		em.close();

		assertEquals("Test", employeeQuery.getLastname());

		Employee employeeUpdated = new Employee("Diego", "TestUpdate", "Passwort");

		try {
			verwaltungEmployee.updateEmployee(employeeUpdated, employeeQuery.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long id = employeeUpdated.getId();
		EntityManager em2 = JpaUtil.createEntityManager();
		TypedQuery<Employee> tQuery2 = em2.createNamedQuery("Employee.findById", Employee.class);
		tQuery2.setParameter("id", id);
		Employee employeeQuery2 = tQuery2.getSingleResult();

		em2.close();
		assertEquals("TestUpdate", employeeQuery2.getLastname());
		
		
		
		try {
			verwaltungEmployee.deletEmployee(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		EntityManager em3 = JpaUtil.createEntityManager();
		TypedQuery<Employee> tQuery3 = em3.createNamedQuery("Employee.findById", Employee.class);
		tQuery3.setParameter("id", 1234567);
		List <Employee> employeeQuery3 = tQuery3.getResultList();
		em3.close();
		
		assertEquals(0,employeeQuery3.size());
		

	}

}
