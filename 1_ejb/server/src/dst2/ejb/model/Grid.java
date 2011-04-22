package dst2.ejb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@NamedQueries({
	@NamedQuery(
			name = "findGridById",
			query = "SELECT g FROM Grid g WHERE g.id = :id"
			)
})
@Entity
public class Grid implements Serializable {
	
	private static final long serialVersionUID = -1644377566295863493L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false, unique = true)
	private String name;
	@Column(nullable=false)
	private String location;
	@Column(nullable=false)
	private BigDecimal costPerCPUMinute;
	
	@OneToMany(mappedBy="grid")
	private List<Membership> users = new ArrayList<Membership>();
	
	@OneToMany(mappedBy="grid")
	private List<Cluster> clusters;
	
	public Grid() {
		super();
		this.clusters = new ArrayList<Cluster>();
	}

	public Grid(String name, String location,
			BigDecimal costPerCPUMinute) {
		super();
		this.clusters = new ArrayList<Cluster>();
		this.name = name;
		this.location = location;
		this.costPerCPUMinute = costPerCPUMinute;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getCostPerCPUMinute() {
		return costPerCPUMinute;
	}

	public void setCostPerCPUMinute(BigDecimal costPerCPUMinute) {
		this.costPerCPUMinute = costPerCPUMinute;
	}
	
	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public List<Cluster> getClusters() {
		return clusters;
	}

	public void setMemberships(List<Membership> memberships) {
		this.users = memberships;
	}

	public List<Membership> getMemberships() {
		return users;
	}
}
