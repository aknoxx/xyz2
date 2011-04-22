package dst2.ejb.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
public class Environment implements Serializable {
	
	private static final long serialVersionUID = -5514556907392833872L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable=false)
	private String workflow;
	@ElementCollection
	@OrderColumn
	@Column(nullable=false)
	private List<String> params;
	
	public Environment() {}
	
	public Environment(String workflow, List<String> params) {
		this.workflow = workflow;
		this.params = params;
	}	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWorkflow() {
		return workflow;
	}
	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public List<String> getParams() {
		return params;
	}
}
