package dst2.ejb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Computer implements Serializable {
	
	private static final long serialVersionUID = -7211247093603186714L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long computerId;
	
	@Column(nullable=false, unique = true)
	private String name;
	@Column(nullable=false)
	private int cpus;
	@Column(nullable=false)
	private String location;
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date creation;
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date lastUpdate;
	
	@ManyToOne(optional=false)
	private Cluster cluster;
	
	@ManyToMany(mappedBy="computers")
	@JoinColumn(nullable=true)
	private Set<Execution> executions = new HashSet<Execution>();
	
	public Computer() {
	}

	public Computer(String name, int cpus, String location, Date creation,
			Date lastUpdate) {
		this.name = name;
		this.cpus = cpus;
		this.location = location;
		this.creation = creation;
		this.lastUpdate = lastUpdate;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCpus() {
		return cpus;
	}

	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}	

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public Long getComputerId() {
		return computerId;
	}

	public void setComputerId(Long computerId) {
		this.computerId = computerId;
	}

	public void setExecutions(Set<Execution> executions) {
		this.executions = executions;
	}

	public Set<Execution> getExecutions() {
		return executions;
	}
}
