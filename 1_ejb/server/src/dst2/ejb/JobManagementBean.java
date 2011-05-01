package dst2.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.ejb.interceptor.AuditInterceptor;
import dst2.ejb.model.*;
import dst2.ejb.util.*;

@Stateful
public class JobManagementBean implements JobManagement {

	@PersistenceContext
	private EntityManager em;
	
	private Map<Long, List<Jobs>> temporary_jobs = new HashMap<Long, List<Jobs>>();
	private Map<Long, List<Computer>> freeComputersPerGrid = new HashMap<Long, List<Computer>>();
	private User user;
	
	private static Logger log = Logger.getLogger("MyLog");

	@Interceptors(AuditInterceptor.class)
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

	@Interceptors(AuditInterceptor.class)
	@Override
	public void addJobToGridTemporary(Long gridId, int numCPUs, String workflow,
			List<String> params) throws ComputersNotAvailableTemporaryException, 
										InvalidGridIdException, UserNotLoggedInException {
		
		// check if enough computers for the grid: sumCPUs of free computers >= numCPUs
		// else custom exception
		
		if(user == null) {
			throw new UserNotLoggedInException("User has to be logged in for this operation.");
		}
		
		Grid grid = em.find(Grid.class, gridId);
		if(grid == null) {
			throw new InvalidGridIdException("Invalid grid id.");
		}
		
		if(!freeComputersPerGrid.containsKey(gridId)) {
			freeComputersPerGrid.put(gridId, getFreeComputersByGrid(gridId));
		}
		
		List<Computer> freeComputers = freeComputersPerGrid.get(gridId);
		
		int freeCPUs = getFreeCPUs(freeComputers);	
		if(freeCPUs >= numCPUs) {
			
			log("Enough CPUs: " + freeCPUs + " >= " + numCPUs + " needed.");
			
			Job job = new Job(false, 
					new Environment(workflow, params),
					new Execution(new Date(), null, JobStatus.SCHEDULED),
					null);			
			
			Jobs gridJobs = new Jobs(gridId);
			gridJobs.setJob(job);
			
			// assign free computers at random
			int assignedCPUs = 0;
			log("Found " + freeComputers.size() + " free Computers.");
			List<Computer> assigned = new ArrayList<Computer>();
			for (Computer computer : freeComputers) {
				gridJobs.getComputers().add(computer);
				
				log("Temporary: Assigned computer " + computer.getName() + ", id: " 
						+ computer.getComputerId()
						+ " with " + computer.getCpus() + " cpus.");
				
				assigned.add(computer);
				assignedCPUs += computer.getCpus();
				if(assignedCPUs >= numCPUs) {
					break;
				}
			}			
			
			// remove just assigned computers from free
			freeComputers.removeAll(assigned);
			
			//temporary_jobs.add(gridJobs);
			if(!temporary_jobs.containsKey(gridId)) {
				List<Jobs> gj = new ArrayList<Jobs>();
				gj.add(gridJobs);
				temporary_jobs.put(gridId, gj);
			}
			else {
				List<Jobs> gj = temporary_jobs.get(gridId);
				gj.add(gridJobs);
				temporary_jobs.put(gridId, gj);
			}
			
		}
		else {
			log("NOT Enough CPUs: " + freeCPUs + " >= " + numCPUs);
			throw new ComputersNotAvailableTemporaryException("Not enough CPUs available to assigne job" 
					+ " with workflow " + workflow + " to grid " + gridId);
		}		
	}
	
	private static void log(String msg) {
		log.log(Level.INFO, msg);
	}
	
	@Interceptors(AuditInterceptor.class)
	@Override
	public void submitJobList() throws UserNotLoggedInException, ComputersNotAvailableException {
		
		// Exception with @ApplicationException(rollback=true) ?
		
		if(user == null) {
			throw new UserNotLoggedInException("User has to be logged in for this operation.");
		}

			log("Submitting temporary_jobs: " + temporary_jobs.size());
			
			User u = em.find(User.class, user.getId());
			
			if(!temporary_jobs.isEmpty()) {
				Iterator iterator = temporary_jobs.keySet().iterator();
			    while (iterator.hasNext()) {
			    	Long gridId = (Long)iterator.next();
			    	List<Jobs> gj = temporary_jobs.get(gridId);
			    	
			    	// check if still enough computers available
					List<Computer> freeComputers = getFreeComputersByGrid(gridId);
					// needed computers
					List<Computer> neededComputers = new ArrayList<Computer>();
					for (Jobs job : gj) {
						neededComputers.addAll(job.getComputers());						
					}
					
					for (Computer ncomputer : neededComputers) {
						boolean available = false;
						for (Computer fcomputer : freeComputers) {
							if(ncomputer.getName().equals(fcomputer.getName())) {
								available = true;
								break;
							}
						}
						if(!available) {
							throw new ComputersNotAvailableException("Not enough free computers " +
									"while submitting jobs to grid " + gridId);
						}
					}
			    	
			    	for (Jobs jobs : gj) {
						Environment environment = jobs.getJob().getEnvironment();
						em.persist(environment);

						Job job = new Job();
						job.setPaid(false);		
						job.setEnvironment(environment);
						
						Execution execution = new Execution(new Date(), null, JobStatus.SCHEDULED);
						execution.setJob(job);
						
						job.setExecution(execution);		
						job.setUser(u);
						
						u.getJobs().add(job);
						
						em.persist(execution);
						em.persist(job);		
						em.merge(u);
							
					        List<Computer> preAssigned = jobs.getComputers();
							for (Computer computer : preAssigned) {
								
								Computer c = em.find(Computer.class, computer.getComputerId());
								
								execution.getComputers().add(c);
								c.getExecutions().add(execution);
								
								em.merge(computer);
							}
							em.merge(execution);
					}
			    }
			}

		remove();
	}
	
	private int getFreeCPUs(List<Computer> computers) {	
		int cpus = 0;
		for (Computer computer : computers) {
			cpus += computer.getCpus();
		}
		return cpus;
	}
	
	private List<Computer> getFreeComputersByGrid(Long gridId) {		
		Query query = em.createNamedQuery("findAllComputersByGrid");
		query.setParameter("gridId", gridId);
		List<Computer> allComputersOfGrid = query.getResultList();
		
		query = em.createNamedQuery("findUsedComputersByGrid");
		query.setParameter("statusRunning", JobStatus.RUNNING);
		query.setParameter("statusScheduled", JobStatus.SCHEDULED);
		List<Computer> usedComputersOfGrid = query.getResultList();
		
		allComputersOfGrid.removeAll(usedComputersOfGrid);
		
		return allComputersOfGrid;
	}

	@Interceptors(AuditInterceptor.class)
	@Override
	public void removeTemporaryJobsFromGrid(Long gridId) {
		if(!temporary_jobs.isEmpty()) {
			if(temporary_jobs.containsKey(gridId)) {
				temporary_jobs.remove(gridId);
			}
		}
		// reset list of free computers / already assigned computers of this grid
		freeComputersPerGrid.remove(gridId);
	}

	@Interceptors(AuditInterceptor.class)
	@Override
	public int getCurrentAmountOfTemporaryJobsByGrid(Long gridId) {
		if(!temporary_jobs.isEmpty()) {
			if(temporary_jobs.containsKey(gridId)) {
				return temporary_jobs.get(gridId).size();
			}
		}
		return 0;
	}	
	
	@Remove()
    public void remove() {
        em = null;
    }
}
