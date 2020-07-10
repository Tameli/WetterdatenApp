package ch.hslu.swde.wda.util;

import java.time.LocalDateTime;
import java.util.Comparator;


/**
 * Vergleich von LocalDateTime Objekten, um eine Sortierung gemäss Zeitstempel zu ermöglichen.
 * 
 * @param die 2 Zeitstempel, die verglichen werden sollen
 * @return 0 wenn gleich gross,-1 wenn time2 grösser, +1 wenn time1 grösser ist
 */

public class WeatherTimeStampComparator implements Comparator<LocalDateTime> {

	@Override
	public int compare(LocalDateTime time1, LocalDateTime time2) {
		return time1.compareTo(time2);
	}

}