package dst2.ejb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Cluster implements Serializable {
	
	private static final long serialVersionUID = 3542643164933510683L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false, unique = true)
	private String name;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	private Date lastService;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	private Date nextService;
	
	@ManyToMany
	@JoinTable(name="COMPOSING_CLUSTERS")
	@JoinColumn(nullable=true)
	private List<Cluster> composingClusters;
	
	@ManyToMany(mappedBy="composingClusters")
	@JoinColumn(nullable=true)
	private List<Cluster> parentClusters;
	
	@ManyToOne(optional=false)
    private Grid grid;
	
	@ManyToOne(optional=false)
    private Admin admin;
	
	@OneToMany(mappedBy="cluster")
	private List<Computer> computers;
	
	public Cluster() {
		this.composingClusters = new ArrayList<Cluster>();
		this.parentClusters = new ArrayList<Cluster>();
		this.computers = new ArrayList<Computer>();
	}

	public Cluster(String name, Date lastService, Date nextService) {
		this.composingClusters = new ArrayList<Cluster>();
		this.parentClusters = new ArrayList<Cluster>();
		this.computers = new ArrayList<Computer>();		
		this.name = name;
		this.lastService = lastService;
		this.nextService = nextService;
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

	public Date getLastService() {
		return lastService;
	}

	public void setLastService(Date lastService) {
		this.lastService = lastService;
	}

	public Date getNextService() {
		return nextService;
	}

	public void setNextService(Date nextService) {
		this.nextService = nextService;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setComposingClusters(List<Cluster> composingClusters) {
		this.composingClusters = composingClusters;
	}

	public List<Cluster> getComposingClusters() {
		return composingClusters;
	}

	public void setComputers(List<Computer> computers) {
		this.computers = computers;
	}

	public List<Computer> getComputers() {
		return computers;
	}

	public void setParentClusters(List<Cluster> parentClusters) {
		this.parentClusters = parentClusters;
	}

	public List<Cluster> getParentClusters() {
		return parentClusters;
	}
}
