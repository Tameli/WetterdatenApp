package ch.hslu.swde.wda.persister.impl.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.util.JpaUtil;

public class PersisterCityImpl {

	public City findCityByName(String city) {
		boolean result = false;
		List<City> list = this.getAllCitites();
		for (City c : list) {
			if (c.getName().equals(city)) {
				result = true;
			}
		}
		if (result) {
			EntityManager em = JpaUtil.createEntityManager();
			em.getTransaction().begin();
			TypedQuery<City> tQry = em.createNamedQuery("City.findCityByName", City.class);
			tQry.setParameter("name", city);
			City city1 = tQry.getSingleResult();
			em.getTransaction().commit();
			em.close();
			return city1;
		}
		else {
			City cityUnknown = new City(1234,"Unknown");
			return cityUnknown;
		}
	}

	public void allCitites(List<City> list) throws Exception {
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		for (City c : list) {
			em.persist(c);
		}
		em.getTransaction().commit();
		em.close();
	}

	public List<City> getAllCitites() {
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<City> tQry = em.createNamedQuery("City.findAll", City.class);
		List<City> list = tQry.getResultList();
		return list;
	}

}
