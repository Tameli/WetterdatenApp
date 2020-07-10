package ch.hslu.swde.wda.domain;

import java.io.Serializable;

import javax.persistence.*;

@NamedQuery(name="Employee.findAll", query="SELECT cu FROM Employee cu")
@NamedQuery(name="Employee.findById", query="SELECT cu FROM Employee cu WHERE cu.id = :id")
@NamedQuery(name="Employee.findByPassword", query="SELECT cu FROM Employee cu WHERE cu.password = :password AND cu.lastname = :lastname")

@Entity
public class Employee implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	private String firstname;
	private String lastname;
	private String password;
	
	public Employee() {
		
	}
	
	public Employee(String firstname, String lastname, String password) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
	}
	public long getId() {
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "Employee [ id=" + id + "name=" + lastname + ", vorname=" + firstname + ", password=" + password+"]";
	}

}
