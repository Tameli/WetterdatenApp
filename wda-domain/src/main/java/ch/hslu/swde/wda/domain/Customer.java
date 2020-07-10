package ch.hslu.swde.wda.domain;

import java.io.Serializable;

import javax.persistence.*;

@NamedQuery(name="Customer.findAll", query="SELECT cu FROM Customer cu")
@NamedQuery(name="Customer.findByEmail", query="SELECT cu FROM Customer cu WHERE cu.email = :email")

@Entity
public class Customer implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	private String firstname;
	private String lastname;
	
	@Column(unique = true)
	private String email;
    private String street;
	private String city;
	private int zip;
	private String password;
	private int counter;
	
	public Customer() {
		
	}
	
	public Customer(String firstname, String lastname, String email, String street, String city, int zip, String password) {
		this.id = counter;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.street = street;
		this.city = city;
		this.zip = zip;
		this.password = password;
		counter++;
	}
	public int getId() {
		return id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
   public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String phone) {
		this.password = phone;
	}
	
	@Override
	public String toString() {
		return "Customer [name=" + lastname + ", vorname=" + firstname + ", email=" + email + ", geburtsDatum="
				+ ", password=" + password + ", city=" + city + ", street=" + street+ ", zip=" + zip+"]";
	}

}
