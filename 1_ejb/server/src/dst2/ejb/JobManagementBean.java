package dst2.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import dst2.ejb.model.*;
import dst2.ejb.util.*;

@Stateful
public class JobManagementBean implements JobManagement {

	@PersistenceContext
	//private EntityManagerFactory emf;
	private EntityManager em;
	
	@Resource
	UserTransaction utx;
	
	private List<GridJobs> temporary_jobs = new ArrayList<GridJobs>();
	private User user;

	@Override
	public boolean login(String username, String password) {
		Query query = em.createNamedQuery("findUserByName");
		query.setParameter("username", username);
		user = (User) query.getSingleResult();
		
		if(user != null) {
			user = em.find(User.class, user.getId());
			if(user.getPassword().equals(password)) {
				System.out.println("User logged in.");
				return true;
			}	
			else {
				user = null;
				System.out.println("Login denied.");
				return false;
			}
		}			
		return false;
	}

	@Override
	public void addJobToGridTemporary(Long gridId, int numCPUs, String workflow,
			List<String> params) throws ComputersNotAvailableException, 
										InvalidGridIdException, UserNotLoggedInException {
		// TODO Auto-generated method stub
		
		// check if enough computers for the grid: sumCPUs of free computers >= numCPUs
		// else custom exception
		
		if(user == null) {
			throw new UserNotLoggedInException("User has to be logged in for this operation.");
		}
		
		Grid grid = em.find(Grid.class, gridId);
		if(grid == null) {
			throw new InvalidGridIdException("Invalid grid id.");
		}
		
		if(getFreeCPUsOfGrid(gridId) >= numCPUs) {
			Job job = new Job(false, 
					new Environment(workflow, params),
					new Execution(),
					user);
			GridJobs gridJobs = new GridJobs(grid);
			gridJobs.getJobs().add(job);
			temporary_jobs.add(gridJobs);
			
			// assign free computers at random
			int assignedCPUs = 0;
			List<Computer> freeComputers = getFreeComputers();
			for (Computer computer : freeComputers) {
				job.getExecution().getComputers().add(computer);
				// TODO check?!
				computer.getExecutions().add(job.getExecution());
				assignedCPUs += computer.getCpus();
				if(assignedCPUs >= numCPUs) {
					return;
				}
			}
		}
		else {
			throw new ComputersNotAvailableException("Not enough CPUs available to assigne job" 
					+ " with workflow " + workflow + " to grid " + gridId);
		}		
	}
	
	@Override
	public void submitJobList() throws UserNotLoggedInException {
		
		// Exception with @ApplicationException(rollback=true) ?
		
		if(user == null) {
			throw new UserNotLoggedInException("User has to be logged in for this operation.");
		}
		
		try {
			utx.begin();
			
			List<Computer> freeComputers = getFreeComputers();
			
			for (GridJobs gridJobs : temporary_jobs) {
				Grid grid = gridJobs.getGrid();
				List<Job> jobs = gridJobs.getJobs();
				for (Job job : jobs) {
					
					em.persist(job.getEnvironment());
						
					job.setEnvironment(job.getEnvironment());
					
					Execution execution = job.getExecution();
					execution.setStart(new Date());
					execution.setStatus(JobStatus.SCHEDULED);					
					execution.setJob(job);
					
					job.setExecution(execution);
					em.refresh(user);
					job.setUser(user);

			        user.setJobs(jobs);

					em.persist(execution);
					em.persist(job);		
					em.merge(user);
					
					// problem?! computers wurden bereits assigned
					
					List<Computer> preAssigned = job.getExecution().getComputers();
					for (Computer computer : preAssigned) {
						
						execution.getComputers().add(computer);
						computer.getExecutions().add(execution);
						em.merge(computer);
					}
				}
			}
			
			utx.commit();
		} catch (Exception e) {
			try {
				utx.rollback();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		// TODO transactionally secure???
		// without error:
		remove();		
	}
	
	private int getFreeCPUsOfGrid(Long gridId) {
		
		
		return 0;
	}
	
	private List<Computer> getFreeComputers() {
		return null;
	}

	@Override
	public void removeTemporaryJobsFromGrid(Long gridId) {
		for (GridJobs gridJobs : temporary_jobs) {
			if(gridJobs.getGrid().getId() == gridId) {
				temporary_jobs.remove(gridJobs);
			}
		}		
	}

	@Override
	public int getCurrentAmountOfTemporaryJobsByGrid(Long gridId) {
		for (GridJobs gridJobs : temporary_jobs) {
			if(gridJobs.getGrid().getId() == gridId) {
				return gridJobs.getJobs().size();
			}
		}
		return 0;
	}	
	
	@Remove()
    public void remove() {
        em = null;
    }
}
