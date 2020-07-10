package ch.hslu.swde.wda.persister.api;

import java.util.List;

import ch.hslu.swde.wda.domain.City;

/**
 * Diese Schnittstelle gibt die Funktionalitäten vor, welche für die
 * Persistierung aller Städte benötigt werden.
 */

public interface PersisterCity {
	
	/**
	 * Persisitiert alle Städte die von der Wetterapplikation verwaltet werden.
	 * @param Liste vom Typ City
	 * @throws wirft eine Exception falls die Städte nicht persisitiert werden konnten
	 */
	
    void allCitites(List<City> list) throws Exception;

}
