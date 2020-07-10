package ch.hslu.swde.wda.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.util.WeatherPressureComparator;

public class WeatherReadingTimeStampComparatorTest {
	
	@Test
	public void testFirstOlderTimestamp() {
		WeatherReading weather1 = new WeatherReading(LocalDateTime.of(2019,11,16,01,17,27), "CH", new City(6345, "Zug"), 8.52f, 47.17f, 2657908, "Clouds", "few clouds", 5.0f, 1012, 100, 3.4f,160.0f);
		WeatherReading weather2 = new WeatherReading(LocalDateTime.of(2019,12,16,03,17,27), "CH", new City(6345, "Zug"), 8.52f, 47.17f, 2657908, "Clouds", "few clouds", 7.0f, 1009, 100, 5.4f,145.0f);
		WeatherPressureComparator comparator = new WeatherPressureComparator();
		int result = comparator.compare(weather1, weather2);
		assertEquals(result, 1);
	}
	
	@Test
	public void testSameTimestamp() {
		WeatherReading weather1 = new WeatherReading(LocalDateTime.of(2019,12,16,01,17,27), "CH", new City(6345, "Zug"), 8.52f, 47.17f, 2657908, "Clouds", "few clouds", 5.0f, 1012, 100, 3.4f,160.0f);
		WeatherReading weather2 = new WeatherReading(LocalDateTime.of(2019,12,16,01,17,27), "CH", new City(6345, "Zug"), 8.52f, 47.17f, 2657908, "Clouds", "few clouds", 7.0f, 1012, 100, 5.4f,145.0f);
		WeatherPressureComparator comparator = new WeatherPressureComparator();
		int result = comparator.compare(weather1, weather2);
		assertEquals(result, 0);
		
	}
	
	@Test
	public void testSecondOlderTimestamp() {
		WeatherReading weather1 = new WeatherReading(LocalDateTime.of(2019,12,16,01,17,27), "CH", new City(6345, "Zug"), 8.52f, 47.17f, 2657908, "Clouds", "few clouds", 5.0f, 1012, 100, 3.4f,160.0f);
		WeatherReading weather2 = new WeatherReading(LocalDateTime.of(2019,11,16,03,17,27), "CH", new City(6345, "Zug"), 8.52f, 47.17f, 2657908, "Clouds", "few clouds", 7.0f, 1050, 100, 5.4f,145.0f);
		WeatherPressureComparator comparator = new WeatherPressureComparator();
		int result = comparator.compare(weather1, weather2);
		assertEquals(result, -1);
	}

}
