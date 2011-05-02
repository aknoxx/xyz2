package dst2.ejb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AuditParameter implements Serializable {

	private static final long serialVersionUID = -6676120651913699892L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private int paramIndex;
	@Column(nullable=false)
	private String className;
	@Column(nullable=false)
	private String value;
	@ManyToOne(optional=false)
	private Audit audit;
	
	public AuditParameter() {
		super();
	}

	public AuditParameter(int paramIndex, String className, String value) {
		super();
		this.paramIndex = paramIndex;
		this.className = className;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getIndex() {
		return paramIndex;
	}

	public void setIndex(int index) {
		this.paramIndex = index;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	public Audit getAudit() {
		return audit;
	}
}
