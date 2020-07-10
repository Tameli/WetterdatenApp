package ch.hslu.swde.wda.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.util.WeatherTimeStampComparator;

public class WeatherTimeStampComparatorTest {
	
	@Test
	public void testCompareTimeStampYesterdayNow() {
		WeatherTimeStampComparator comp1 = new WeatherTimeStampComparator();
		LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
		LocalDateTime now = LocalDateTime.now();
		int result = comp1.compare(yesterday,now);
		assertEquals(-1,result);
	}
	
	@Test
	public void testCompareTimeStampNowYesterday() {
		WeatherTimeStampComparator comp1 = new WeatherTimeStampComparator();
		LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
		LocalDateTime now = LocalDateTime.now();
		int result = comp1.compare(now, yesterday);
		assertEquals(1,result);
	}
	
	@Test
	public void testSameTimeStamp() {
		WeatherTimeStampComparator comp1 = new WeatherTimeStampComparator();
		LocalDateTime now1 = LocalDateTime.now();
		LocalDateTime now = now1;
		int result = comp1.compare(now,now1);
		assertEquals(0,result);
	}

}
