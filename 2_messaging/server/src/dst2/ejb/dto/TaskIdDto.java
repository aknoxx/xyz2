package dst2.ejb.dto;

import java.io.Serializable;

public class TaskIdDto implements Serializable {

	private static final long serialVersionUID = 612341386761969939L;
	public Long taskId;
	
	public TaskIdDto(Long taskId) {
		this.taskId = taskId;
	}
}
