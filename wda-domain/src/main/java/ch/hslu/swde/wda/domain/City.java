package ch.hslu.swde.wda.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@NamedQuery(name="City.findAll", query="SELECT c FROM City c")
@NamedQuery(name="City.findCityByName", query="SELECT c FROM City c WHERE c.name = :name")
@NamedQuery(name="City.findCityByZipAndName", query="SELECT c FROM City c WHERE c.zip = :zip AND c.name = :name")

/**
 * Die Klasse City beschreibt eine Stadt basierend auf der Potleitzahl und deren Namen.
 * @author lunas
 *
 */

@Entity
public class City implements Serializable, Comparable<City> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private int id;
	private int zip;
	private String name;
	
	public City() {
		
	}
	
	public City(int zip, String name) {
		this.zip = zip;
		this.name = name;
		
	}
	
	
	public int getId() {
		return id;
	}


	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "city [zip=" + zip +",name="+ name+"]";
	}
	
	@Override
	public int compareTo(City other) {
	    String name1 = this.getName();
	    String name2 = other.getName();
	    return name1.compareTo(name2);
	}
}
