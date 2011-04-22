package dst2.ejb.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Admin extends Person {
	
	private static final long serialVersionUID = -3612133966412256190L;
	
	@OneToMany(mappedBy="admin")
	private List<Cluster> clusters = new ArrayList<Cluster>();

	public Admin() {
		super();
	}

	public Admin(String firstName, String lastName, Address address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public List<Cluster> getClusters() {
		return clusters;
	}
}
