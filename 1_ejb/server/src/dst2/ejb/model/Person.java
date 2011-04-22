package dst2.ejb.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity 
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Person implements Serializable {

	private static final long serialVersionUID = -868215087159547491L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	protected Long id;	
	@Column(nullable=false)
	protected String firstName;
	@Column(nullable=false)
	protected String lastName;
	
	@Embedded
	@Column(nullable=false)
	protected Address address;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}
}
