package ch.hslu.swde.wda.business.restReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.domain.City;

public class ClientAppIT {
	
	@Test
	public void testAllCititesQuery() throws Exception {
		ClientApp app1 = new ClientApp();
		List<City> result = app1.allCititesAsObject();
		int sizeResult = result.size();
		assertEquals(sizeResult, 40);
	}
}