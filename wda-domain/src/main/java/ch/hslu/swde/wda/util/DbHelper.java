package ch.hslu.swde.wda.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.domain.WeatherReading;

/**
 * Hilfsklasse, um die DB vor und nach jedem Testversuch wieder aufzuräumen,
 * damit doppelte Einträge verhindert werden und die Konsistenz so
 * sichergestellt werden kann.
 * 
 * @author lunas
 *
 */

public class DbHelper {

	public static void deleteCustomer() {

		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<Customer> tQuery = em.createNamedQuery("Customer.findAll", Customer.class);
		List<Customer> resultList = tQuery.getResultList();
		for (Customer c : resultList) {
			em.remove(c);
		}
		em.getTransaction().commit();
		em.close();

	}

	public static void deleteAllCitites() {
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<City> tQuery = em.createNamedQuery("City.findAll", City.class);
		List<City> resultList = tQuery.getResultList();
		for (City c : resultList) {
			em.remove(c);
		}
		em.getTransaction().commit();
		em.close();

	}

	public static void deleteAllWeatherReading() {
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<WeatherReading> tQuery = em.createNamedQuery("WeatherReading.findAll", WeatherReading.class);
		List<WeatherReading> resultList = tQuery.getResultList();
		for (WeatherReading c : resultList) {
			em.remove(c);
		}
		em.getTransaction().commit();
		em.close();

	}

	public static void deleteEmployee() {

		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<Employee> tQuery = em.createNamedQuery("Employee.findAll", Employee.class);
		List<Employee> resultList = tQuery.getResultList();
		for (Employee c : resultList) {
			em.remove(c);
		}
		em.getTransaction().commit();
		em.close();

	}

}
