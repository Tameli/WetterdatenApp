package ch.hslu.swde.wda.business.restReader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageSpecificCityDataset {

	private int zip;
	private String city;
	private String data;
	private String lastUpdateTime;
	
	
	
	public MessageSpecificCityDataset() {
		
	}
	
	public MessageSpecificCityDataset(int zip, String city, String data, String lastUpdateTime ) {
		this.city = city;
		this.zip = zip;
		this.data = data;
		this.lastUpdateTime = lastUpdateTime;
		
	}
	
	

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	
	
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("city")
	private void unpackNested(Map<String, Object> city) {
		this.zip = (Integer)city.get("zip");
		this.city = (String)city.get("city");
        }

	@Override
	public String toString() {
		return "[city:"+ "[zip=" + zip + ", name=" + city + "], data=" + data+ ",lastUpdateTime" + lastUpdateTime+"]";
	}

}
