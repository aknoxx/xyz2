package dst2.ejb.dto;

import dst2.ejb.model.TaskComplexity;
import dst2.ejb.model.TaskStatus;

public class TaskDto {

	public Long jobId;
	public TaskStatus status;
	public String ratedBy;
	public TaskComplexity complexity;
	
	public TaskDto(Long jobId, TaskStatus status, String ratedBy,
			TaskComplexity complexity) {
		this.jobId = jobId;
		this.status = status;
		this.ratedBy = ratedBy;
		this.complexity = complexity;
	}
}
