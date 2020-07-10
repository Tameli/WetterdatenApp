package ch.hslu.swde.wda.persister.impl.db;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherReading;
import ch.hslu.swde.wda.persister.api.PersisterWeatherReading;
import ch.hslu.swde.wda.util.JpaUtil;

public class PersisterWeatherReadingImpl implements PersisterWeatherReading {

	public List<WeatherReading> getAllDataForSpecificCity(String cityName) {
		PersisterCityImpl impl = new PersisterCityImpl();
		City city = impl.findCityByName(cityName);
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<WeatherReading> tQry = em.createNamedQuery("WeatherReading.findDataOneCity", WeatherReading.class);
		tQry.setParameter("city", city);
		List<WeatherReading> list = tQry.getResultList();
		return list;

	}

	public List<WeatherReading> getDataLast24Hour(City city) {
		LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowMinus24 = LocalDateTime.now().minusHours(24);
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<WeatherReading> tQry = em.createNamedQuery("WeatherReading.findByCityAndLast24Hours",
				WeatherReading.class);
		tQry.setParameter("city", city);
		tQry.setParameter("nowMinus24", nowMinus24);
		tQry.setParameter("now", now);
		List<WeatherReading> list = tQry.getResultList();
		return list;

	}

	public List<WeatherReading> getDataStartAndEndDate(LocalDate start, LocalDate end, City city) {

		LocalDateTime startNew = start.atStartOfDay(); // Start bei 00:00 Uhr
		LocalDateTime endNew = end.atTime(23, 59); // Ende bei 23:59 Uhr
		List<WeatherReading> list = new ArrayList<>();
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<WeatherReading> tQry = em.createNamedQuery("WeatherReading.findByCityAndTimeInterval",
				WeatherReading.class);
		tQry.setParameter("city", city);
		tQry.setParameter("start", startNew);
		tQry.setParameter("end", endNew);
		list = tQry.getResultList();
		return list;
	}

	/**
	 * Hilfsmethode, wenn die Verwaltung schon LocalDateTime liefert.
	 * 
	 * @param start
	 * @param end
	 * @param city
	 * @return
	 */

	public List<WeatherReading> getDataStartAndEndDate(LocalDateTime start, LocalDateTime end, City city) {

		List<WeatherReading> list = new ArrayList<>();
		EntityManager em = JpaUtil.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<WeatherReading> tQry = em.createNamedQuery("WeatherReading.findByCityAndTimeInterval",
				WeatherReading.class);
		tQry.setParameter("city", city);
		tQry.setParameter("start", start);
		tQry.setParameter("end", end);
		list = tQry.getResultList();
		return list;
	}

	public void persistSpecificDataForOneCity(List<WeatherReading> list) throws Exception {
		EntityManager em = JpaUtil.createEntityManager();
		String city = "";
		em.getTransaction().begin();
		for (WeatherReading wr : list) {
			em.persist(wr);
			city = wr.getCity().getName();
		}
		System.out.print("Persisitierung für"+" "+ city+"\n");
		em.getTransaction().commit();
		em.close();
	}

}
