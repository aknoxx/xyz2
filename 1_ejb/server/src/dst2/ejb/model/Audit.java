package dst2.ejb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@NamedQueries({
	@NamedQuery(
			name = "findAllAudits",
			query = "select a "
				+ "from Audit a "
			)
})
@Entity
public class Audit implements Serializable {

	private static final long serialVersionUID = -216983756294528649L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date invocationTime;
	@Column(nullable=false)
	private String methodName;
	@OneToMany(mappedBy="audit")
	@JoinColumn(nullable=true)
	private List<AuditParameter> parameters;
	@Column(nullable=false)
	private String resultValue; // or exceptionValue
	
	public Audit() {
		super();
	}

	public Audit(Date invocationTime, String methodName, String resultValue) {
		super();
		this.invocationTime = invocationTime;
		this.methodName = methodName;
		//this.parameters = parameters;
		this.resultValue = resultValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getInvocationTime() {
		return invocationTime;
	}

	public void setInvocationTime(Date invocationTime) {
		this.invocationTime = invocationTime;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getResultValue() {
		return resultValue;
	}

	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}

	public void setParameters(List<AuditParameter> parameters) {
		this.parameters = parameters;
	}

	public List<AuditParameter> getParameters() {
		return parameters;
	}
}
