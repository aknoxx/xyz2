package dst2.ejb.util;

import java.util.ArrayList;
import java.util.List;

import dst2.ejb.model.*;

public class Jobs {

	private Job job;
	private List<Computer> computers;
	
	public Jobs(Long gridId) {
		this.setComputers(new ArrayList<Computer>());
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
