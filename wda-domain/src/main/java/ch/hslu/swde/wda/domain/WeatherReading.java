package ch.hslu.swde.wda.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

import javax.persistence.*;

@NamedQuery(name = "WeatherReading.findByCityAndLast24Hours", query = "SELECT wr FROM WeatherReading wr WHERE wr.city =:city AND wr.time BETWEEN :nowMinus24 AND :now")
@NamedQuery(name = "WeatherReading.findByCityAndTimeInterval", query = "SELECT wr FROM WeatherReading wr WHERE wr.city =:city AND wr.time BETWEEN :start AND :end")
@NamedQuery(name = "WeatherReading.findAll", query = "SELECT wr FROM WeatherReading wr")
@NamedQuery(name = "WeatherReading.findDataOneCity", query = "SELECT wr FROM WeatherReading wr WHERE wr.city =:city")

@Entity
@SequenceGenerator(name="seq", sequenceName="weather_seq", initialValue=1, allocationSize=100)
public class WeatherReading implements Serializable, Comparable<WeatherReading> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	private int id;
	@ManyToOne
	private City city;
	private LocalDateTime time;
	private String country;
	private float longitude;
	private float latitude;
	private int stationId;
	private String summary;
	private String description;
	private float currentTemp;
	private int pressure;
	private int humidity;
	private float windSpeed;
	private float windDirection;

	public WeatherReading() {

	}

	public WeatherReading(LocalDateTime time, String country, City city, float longitude, float latitude, int stationId,
			String summary, String description, float currentTemp, int pressure, int humidity, float windSpeed,
			float windDirection) {
		super();
		this.country = country;
		this.city = city;
		this.longitude = longitude;
		this.latitude = latitude;
		this.stationId = stationId;
		this.currentTemp = currentTemp;
		this.summary = summary;
		this.description = description;
		this.pressure = pressure;
		this.humidity = humidity;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
		this.time = time;
	}

	public WeatherReading(City city) {
		this.city = city;

	}
	
	public int getId() {
		return id;
	}


	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getCurrentTemp() {
		return currentTemp;
	}

	public void setCurrentTemp(float currentTemp) {
		this.currentTemp = currentTemp;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

	public float getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(float windDirection) {
		this.windDirection = windDirection;
	}

	@Override
	public int hashCode() {
		//return Objects.hash(this.getCity(),this.time);
		return Objects.hash(this.id);
	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//
//		if (!(obj instanceof WeatherReading)) {
//			return false;
//		}
//
//		WeatherReading other = (WeatherReading) obj;
//
//		if (time != other.time && city != other.city) {
//			return false;
//		}
//
//		return true;
//	}
	
	@Override
    public final boolean equals(final Object other){
        if(other == this){
            return true;
        }
        if (!(other instanceof WeatherReading)){ 
            return false;
        }
        final WeatherReading w1 = (WeatherReading) other;
        //return Objects.equals(w1.getCity(), this.getCity()) &&  Objects.equals(w1.getTime(), this.getTime());
        return Objects.deepEquals(w1.getId(), this.getId());
        
    }

	@Override
	public String toString() {
		return "city [timestamp=" + time + ",country=" + country + ",zip=" + city.getZip() + ", name=" + city.getName()
				+ ",longitude=" + longitude + ",latitude=" + latitude + ", stationId=" + stationId + ",summary="
				+ summary + ",description=" + description + ", currentTemp=" + currentTemp + ",pressure=" + pressure
				+ ",humidity=" + humidity + ",windSpped=" + windSpeed + ",windDirection=" + windDirection + "]";
	}

	/**
	 * Sortierung nach der natürlichen Ordnung der momentanen Temperatur in Celsius.
	 * 
	 * @param other
	 * @return 0 wenn gleich gross,-1 wenn other grösser, +1 wenn this grösser
	 */
	@Override
	public int compareTo(WeatherReading other) {
		return Float.compare(this.currentTemp, other.currentTemp);
	}
	

}
