package dst2.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	//@Resource
	//UserTransaction utx;
	
	private List<GridJobs> temporary_jobs = new ArrayList<GridJobs>();
	private User user;
	
	private static Logger log = Logger.getLogger("MyLog");

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
		
		int freeCPUs = getFreeCPUsOfGrid(gridId);
		if(freeCPUs >= numCPUs) {
			
			log.log(Level.INFO, "Enough CPUs: " + freeCPUs + " >= " + numCPUs);
			
			Execution execution = new Execution();
			
			Job job = new Job(false, 
					new Environment(workflow, params),
					null,
					null);			
			
			GridJobs gridJobs = new GridJobs(gridId);
			gridJobs.setJob(job);
			
			// assign free computers at random
			int assignedCPUs = 0;
			List<Computer> freeComputers = getFreeComputersByGrid(gridId);
			log.log(Level.INFO, "Found " + freeComputers.size() + " free Computers.");
			for (Computer computer : freeComputers) {
				gridJobs.getComputers().add(computer);
				
				//execution.getComputers().add(c);
				// TODO check?!
				//c.getExecutions().add(job.getExecution());
				assignedCPUs += computer.getCpus();
				if(assignedCPUs >= numCPUs) {
					break;
				}
			}	
			
			temporary_jobs.add(gridJobs);
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
		
		//try {
			//utx.begin();
			
			log.log(Level.INFO, "Submitting temporary_jobs: " + temporary_jobs.size());
			
			User u = em.find(User.class, user.getId());
			
			for (GridJobs gridJobs : temporary_jobs) {
				
				//List<Computer> freeComputers = getFreeComputersByGrid(gridJobs.getGridId());
				// check if available
				
				Environment environment = gridJobs.getJob().getEnvironment();
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
						
					//job.setEnvironment(environment);				
					/*execution.setJob(job);
					
					em.persist(execution);
					
					//job.setExecution(execution);
					em.refresh(user);
					job.setUser(user);

			        user.getJobs().add(job);*/
			        
			        // problem?! computers wurden bereits assigned
					
			        
			        List<Computer> preAssigned = gridJobs.getComputers();
					for (Computer computer : preAssigned) {
						
						Computer c = em.find(Computer.class, computer.getComputerId());
						
						execution.getComputers().add(c);
						c.getExecutions().add(execution);
						
						em.merge(computer);
						
						//execution.getComputers().add(computer);
						//computer.getExecutions().add(execution);
						//em.merge(computer);
						//em.merge(execution);
					}
					em.merge(execution);
					
					//em.persist(job);		
					//em.merge(user);
					
					
					
					
			//	}
			}
			
			//utx.commit();
		/*} catch (Exception e) {
			try {
				//utx.rollback();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			//} catch (SystemException e1) {
				// TODO Auto-generated catch block
			//	e1.printStackTrace();
			//}
		//}
		
		// TODO transactionally secure???
		// without error:
		//remove();		*/
	}
	
	private int getFreeCPUsOfGrid(Long gridId) {		
		//Query query = em.createNamedQuery("findFreeCPUSByGrid");
		/*Query query = em.createNativeQuery("select sum(x.CPUS) from "
				+ "(select a.COMPUTERID, a.CPUS "
				+ "from (select ID from GRID where ID  = :gridId) g JOIN CLUSTER cl on g.ID=cl.GRID_ID "
					+ "JOIN COMPUTER a on cl.ID=a.CLUSTER_ID "
				+ "LEFT JOIN "
				+ "(select c.COMPUTERID, c.CPUS from "
					+ "(select e.execution_id from EXECUTION e "
						+ "where e.STATUS = 'RUNNING' OR e.STATUS = 'SCHEDULED') e "
					+ "join EXECUTION_COMPUTER ec join COMPUTER c "
					+ "where e.execution_id = ec.executions_execution_id "
					+ "and c.COMPUTERID = ec.computers_COMPUTERID) b "
				+ "ON a.COMPUTERID=b.COMPUTERID "
				+ "where b.COMPUTERID IS NULL) x");
		query.setParameter("gridId", gridId);
		return (Integer) query.getSingleResult();*/
		
		return 10;
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

	@Override
	public void removeTemporaryJobsFromGrid(Long gridId) {
		for (GridJobs gridJobs : temporary_jobs) {
			if(gridJobs.getGridId() == gridId) {
				temporary_jobs.remove(gridJobs);
			}
		}		
	}

	@Override
	public int getCurrentAmountOfTemporaryJobsByGrid(Long gridId) {
		int jobCount = 0;
		for (GridJobs gridJobs : temporary_jobs) {
			if(gridJobs.getGridId() == gridId) {
				jobCount ++;
			}
		}
		return temporary_jobs.size();
	}	
	
	@Remove()
    public void remove() {
        em = null;
    }
}
