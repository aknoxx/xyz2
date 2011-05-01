package dst2.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst2.ejb.model.*;

@Stateless
public class TestingBean implements Testing {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void insertData() {		
		
		Grid grid1 = new Grid("grid1", "location1", new BigDecimal(1));
		Grid grid2 = new Grid("grid2", "location2", new BigDecimal(2));	
		em.persist(grid1);
		em.persist(grid2);
		
		User user1 = new User("name1", "lastname1", new Address("street1", "city1", "zipCode1"), 
				"username1", "password1", "accountNo1", "bankNumber1");
		User user2 = new User("name2", "lastname2", new Address("street2", "city2", "zipCode2"), 
				"username2", "password2", "accountNo2", "bankNumber2");
		em.persist(user1);
		em.persist(user2);		
		
		Membership mem1 = user1.addGrid(grid1, new Date(), 0.2);
		Membership mem2 = user2.addGrid(grid2, new Date(), 0.4);
		em.persist(mem1);
		em.persist(mem2);
		
		Admin admin1 = new Admin("firstName1", "lastname1", new Address("street1", "city1", "zipCode1"));
		Admin admin2 = new Admin("firstName2", "lastname2", new Address("street2", "city2", "zipCode2"));
		em.persist(admin1);
		em.persist(admin2);
		
		Cluster cluster1 = new Cluster("name1", new Date(), new Date());
		cluster1.setAdmin(admin1);
		cluster1.setGrid(grid1);
		em.persist(cluster1);
		grid1.getClusters().add(cluster1);
		em.merge(grid1);		
		
		Cluster cluster2 = new Cluster("name2", new Date(), new Date());
		cluster2.setAdmin(admin2);
		cluster2.setGrid(grid2);	
		em.persist(cluster2);
		grid2.getClusters().add(cluster2);
		em.merge(grid2);		
		
		for (int i = 0; i < 5; i++) {
			Computer computer = new Computer("name"+i+1, i+1, "location"+i+1, new Date(), new Date());
			computer.setCluster(cluster1);
			cluster1.getComputers().add(computer);
			em.persist(computer);
		}
		em.merge(cluster1);
		
		for (int i = 5; i < 10; i++) {
			Computer computer = new Computer("name"+i+1, i+1, "location"+i+1, new Date(), new Date());
			computer.setCluster(cluster2);
			cluster2.getComputers().add(computer);
			em.persist(computer);
		}
		em.merge(cluster2);
		
		List<String> params = new ArrayList<String>();
		params.add("param1");
		params.add("param2");
		params.add("param3");
		
		Environment environment1 = new Environment("workflow1", params);	
		em.persist(environment1);
		
		Job job = new Job();
		job.setPaid(false);		
		job.setEnvironment(environment1);
		
		Long start = new Date().getTime() - 1800000; // 30 minutes ago
		Execution execution = new Execution(new Date(start), null, JobStatus.RUNNING);	
		execution.setJob(job);
		
		job.setExecution(execution);
		job.setEnvironment(environment1);
		job.setUser(user1);

        List<Job> jobs = new ArrayList<Job>();
        jobs.add(job);
        user1.setJobs(jobs);

		em.persist(execution);
		em.persist(job);		
		em.merge(user1);
		
		Computer computer = em.find(Computer.class, new Long(8));
		
		execution.getComputers().add(computer);
		computer.getExecutions().add(execution);
		em.merge(execution);
		em.merge(computer);

        System.out.println("All fine");
	}

	@Override
	public void remove() {
		em = null;		
	}
}
