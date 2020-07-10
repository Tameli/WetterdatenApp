package ch.hslu.swde.wda.domain;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.Warning;

import nl.jqno.equalsverifier.EqualsVerifier;

public class WeatherReadingTest {
	
	/**Überprüft die korrekte Implementierung von equals
	 * 
	 */
	
	@Test
    public void equalsContract() {        
        EqualsVerifier.forClass(WeatherReading.class).suppress(Warning.SURROGATE_KEY).verify();
    }

}
