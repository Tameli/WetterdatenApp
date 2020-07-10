package ch.hslu.swde.wda.persister.api;

import java.util.List;

import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherReading;

/**
 * Diese Schnittstelle gibt die Funktionalitäten vor, welche für die
 * Persistierung von Wetterdaten benötigt werden.
 */



public interface PersisterWeatherReading {
    
    /**
     * Retourniert, wenn von der Verwaltung angefragt, alle Daten der letzten 24h einer bestimmten Stadt.
     * @param city Stadt von der man die Daten haben will
     * @return List<WeatherReading>
     */
    
    List<WeatherReading> getDataLast24Hour(City city);
	

}
