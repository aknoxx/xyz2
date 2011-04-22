package dst2.ejb.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(uniqueConstraints= { @UniqueConstraint(columnNames="environment_id") })
public class Job implements Serializable {
	
	private static final long serialVersionUID = -4666597014351197778L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private boolean isPaid;
    
    @OneToOne(cascade=CascadeType.REMOVE, optional = false)
    private Execution execution;

    @OneToOne(cascade=CascadeType.REMOVE, optional = false)
    private Environment environment;

    @ManyToOne(optional = false)
    private User user;

	public Job() {
		// used by Hibernate
	}

	public Job(boolean isPaid, Environment environment, Execution execution, User user) {
		// for application use, to create new jobs
		this.isPaid = isPaid;
		this.environment = environment;
		this.execution = execution;
		this.user = user;
	}

    public Long getId() {
		return id;
    }

    private void setId(Long id) {
		this.id = id;
    }

    // derived attribute
    public int getNumCPUs() {
    	List<Computer> computers = execution.getComputers();
    	int numCPUs = 0;
    	
    	for (Computer computer : computers) {
			numCPUs += computer.getCpus();
		}
		return numCPUs;
    }

    // derived attribute
    public int getExecutionTime() {
    	if(execution.getEnd() != null) {
    		return (int) (execution.getEnd().getTime() - execution.getStart().getTime()) / 1000;
    	}
    	return 0;
    }
    
    public boolean isPaid() {
		return isPaid;
    }

    public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
    }

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}

	public Execution getExecution() {
		return execution;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
