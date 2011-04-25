package dst2.ejb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;

@NamedQueries({
	@NamedQuery(
			name = "findFreeComputersByGrid",
			query = "select a "
				+ "from Computer a "
					+ "where a.cluster.id = (select cl.id from Cluster cl where cl.grid.id=:gridId) "
				/*+ "LEFT JOIN "
				+ "(select c.COMPUTERID, c.CPUS, c.CREATION, c.LASTUPDATE, c.LOCATION, c.NAME, c.CLUSTER_ID from "
					+ "(select e.execution_id from EXECUTION e "
						+ "where e.STATUS = 'RUNNING' OR e.STATUS = 'SCHEDULED') e "
					+ "join EXECUTION_COMPUTER ec join COMPUTER c "
					+ "where e.execution_id = ec.executions_execution_id "
					+ "and c.COMPUTERID = ec.computers_COMPUTERID) b "
				+ "ON a.COMPUTERID=b.COMPUTERID "
				+ "where b.COMPUTERID IS NULL"*/
			)
})
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
