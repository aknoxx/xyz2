package dst2.ejb.dto;

import java.io.Serializable;

public class AssignJobDto implements Serializable{

	private static final long serialVersionUID = -8958475872976406461L;
	public Long jobId;
	
	public AssignJobDto(Long jobId) {
		this.jobId = jobId;
	}
}
