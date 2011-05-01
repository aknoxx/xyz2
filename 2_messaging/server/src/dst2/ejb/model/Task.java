package dst2.ejb.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task implements Serializable {

	private static final long serialVersionUID = 3293968046898564201L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private Long jobId;
    private TaskStatus status;
    private String ratedBy;
    private TaskComplexity complexity;
	
    public Task() {
		super();
	}

	public Task(Long jobId, TaskStatus status, String ratedBy,
			TaskComplexity complexity) {
		super();
		this.jobId = jobId;
		this.status = status;
		this.ratedBy = ratedBy;
		this.complexity = complexity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getRatedBy() {
		return ratedBy;
	}

	public void setRatedBy(String ratedBy) {
		this.ratedBy = ratedBy;
	}

	public TaskComplexity getComplexity() {
		return complexity;
	}

	public void setComplexity(TaskComplexity complexity) {
		this.complexity = complexity;
	}
}
