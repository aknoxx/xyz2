package dst2.ejb.util;

import java.util.ArrayList;
import java.util.List;

import dst2.ejb.model.*;

public class GridJobs {

	private Long gridId;
	private Job job;
	private List<Computer> computers;
	
	public GridJobs(Long gridId) {
		this.gridId = gridId;
		this.setComputers(new ArrayList<Computer>());
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGrid(Long gridId) {
		this.gridId = gridId;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void setComputers(List<Computer> computers) {
		this.computers = computers;
	}

	public List<Computer> getComputers() {
		return computers;
	}
}
