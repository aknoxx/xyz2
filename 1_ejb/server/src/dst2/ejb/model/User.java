package dst2.ejb.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@NamedQueries({
	@NamedQuery(
			name = "findUserByName",
			query = "SELECT u FROM User u WHERE u.username = :username"
			)
})
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"accountNo", "bankCode"})})
public class User extends Person {
	
	private static final long serialVersionUID = -3194687315977147534L;
	
	@Column(nullable=false, unique=true)
	private String username;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private String accountNo;
	@Column(nullable=false)
	private String bankCode;	
		
	@OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE})
    private List<Job> jobs;
	
	@OneToMany(mappedBy="user", cascade = CascadeType.REMOVE )
	private List<Membership> grids = new ArrayList<Membership>();
	
	public User() {
		this.jobs = new ArrayList<Job>();
	}	

	public User(String firstName, String lastname, Address address,
			String username, String password, String accountNo,
			String bankCode) {
		
		this.jobs = new ArrayList<Job>();
		
		this.firstName = firstName;
		this.lastName = lastname;
		this.address = address;
		
		this.username = username;
		this.password = password;
		this.accountNo = accountNo;
		this.bankCode = bankCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<Job> getJobs() {
		return jobs;
	}
	
	public void addJob(Job job) {
		this.jobs.add(job);
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setMemberships(List<Membership> memberships) {
		this.grids = memberships;
	}

	public List<Membership> getMemberships() {
		return grids;
	}
	
	// Add an employee to the project.
	// Create an association object for the relationship and set its' data.
	public Membership addGrid(Grid grid, Date registration, Double discount) {
	    Membership association = new Membership();
	    association.setGrid(grid);
	    association.setUser(this);
	    association.setGridId(grid.getId());
	    association.setUserId(this.getId());
	    association.setRegistration(registration);
	    association.setDiscount(discount);
	 
	    this.grids.add(association);
	    // Also add the association object to the employee.
	    grid.getMemberships().add(association);
	    
	    return association;
	  }
}
