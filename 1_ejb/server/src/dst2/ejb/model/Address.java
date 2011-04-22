package dst2.ejb.model;

import java.io.Serializable;

import javax.persistence.*;

@Embeddable
public class Address implements Serializable {

	private static final long serialVersionUID = 647710944693076380L;
	
	@Column(nullable=false)	
	private String street;
	@Column(nullable=false)
	private String city;
	@Column(nullable=false)
	private String zipCode;
	
	public Address() {}

	public Address(String street, String city, String zipCode) {
		this.street = street;
		this.city = city;
		this.zipCode = zipCode;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
	
}
