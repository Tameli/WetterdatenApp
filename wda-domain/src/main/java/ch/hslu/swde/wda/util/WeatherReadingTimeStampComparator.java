package ch.hslu.swde.wda.util;

import java.time.LocalDateTime;
import java.util.Comparator;

import ch.hslu.swde.wda.domain.WeatherReading;

/**
 * Vergleich von WeatherReadingObjekten, anhand ihres Zeitstempels
 * 
 * @param die 2 WeatherReading Objekte, die verglichen werden sollen
 * @return 0 wenn gleich gross,-1 wenn data2 grösser, +1 wenn data1 grösser ist
 */

public class WeatherReadingTimeStampComparator implements Comparator<WeatherReading> {
	
	@Override
	public int compare(WeatherReading data1, WeatherReading data2) {
		LocalDateTime time1 = data1.getTime();
		LocalDateTime time2 = data2.getTime();
		return time1.compareTo(time2);
	}

}
