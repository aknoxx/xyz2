package dst2.ejb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@NamedQueries({
	@NamedQuery(
			name = "findExecutionsByStatus",
			query = "SELECT e FROM Execution e WHERE e.status = :status "
			)
})
@Entity
public class Execution implements Serializable {

	private static final long serialVersionUID = -8903786043890502932L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "execution_id", unique = true, nullable = false)
	private Long executionId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date start;
	@Temporal(TemporalType.TIMESTAMP)
	private Date end;
	@Enumerated(value = EnumType.STRING)
	private JobStatus status;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy="execution", optional = false)
	@JoinColumn(nullable=false)
	private Job job;
	
	@ManyToMany
	private List<Computer> computers = new ArrayList<Computer>();
	
	public Execution() {}
	
	public Execution(Date start, Date end, JobStatus status) {
		super();
		this.start = start;
		this.end = end;
		this.status = status;
	}

	public Long getId() {
		return executionId;
	}

	public void setId(Long id) {
		this.executionId = id;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	public void setComputers(List<Computer> computers) {
		this.computers = computers;
	}

	public List<Computer> getComputers() {
		return computers;
	}
}
