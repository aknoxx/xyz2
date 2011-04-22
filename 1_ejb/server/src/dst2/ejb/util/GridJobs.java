package dst2.ejb.util;

import java.util.ArrayList;
import java.util.List;

import dst2.ejb.model.*;

public class GridJobs {

	private Grid grid;
	private List<Job> jobs;
	
	public GridJobs(Grid grid) {
		this.grid = grid;
		this.jobs = new ArrayList<Job>();
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}
}
