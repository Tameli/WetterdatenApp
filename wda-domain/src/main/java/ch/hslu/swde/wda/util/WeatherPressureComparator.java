package ch.hslu.swde.wda.util;

import java.util.Comparator;


import ch.hslu.swde.wda.domain.WeatherReading;

/**
 * Vergleich von WeatherReadingObjekten, anhand ihres Luftdruckes
 * 
 * @param die 2 WeatherReading Objekte, die verglichen werden sollen
 * @return 0 wenn gleich gross,-1 wenn w2 grösser, +1 wenn w1 grösser ist
 */

public class WeatherPressureComparator implements Comparator<WeatherReading> {
	
	@Override
	public int compare(WeatherReading w1, WeatherReading w2) {
		return Integer.compare(w1.getPressure(), w2.getPressure());
		
	}

}
