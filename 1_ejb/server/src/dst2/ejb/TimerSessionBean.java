package dst2.ejb;


import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.ejb.model.*;

//@Startup // "eager initialization"
@Singleton
public class TimerSessionBean {
	
	@PersistenceContext
	private EntityManager em;

	private Logger logger = Logger.getLogger(
								"com.sun.tutorial.javaee.ejb.timersession.TimerSessionBean");
	
	// called every 2 seconds minute="*", 
	@Schedule(second="*/2", minute="*", hour="*")
	public void automaticTimeout() {
		
		logger.info("Automatic timeout occured");
		
		Query query = em.createNamedQuery("findExecutionsByStatus");
		query.setParameter("status", JobStatus.RUNNING);
		List<Execution> executions = query.getResultList();
		
		for (Execution execution : executions) {
			Execution e = em.find(Execution.class, execution.getId());
			// set job execution duration to 1 minute
			e.setEnd(new Date(e.getStart().getTime() + 60000));
			e.setStatus(JobStatus.FINISHED);
			em.merge(e);
		}	
		
		query = em.createNamedQuery("findExecutionsByStatus");
		query.setParameter("status", JobStatus.SCHEDULED);
		executions = query.getResultList();
		
		for (Execution execution : executions) {
			Execution e = em.find(Execution.class, execution.getId());
			e.setStatus(JobStatus.RUNNING);
			em.merge(e);
		}	
	}
	
	//@Resource
	//TimerService timerService;
	
	/*@PostConstruct
	void init() {
		Timer timer = timerService.createIntervalTimer(4000, 4000, null);
	}
	
	@Timeout
    public void programmaticTimeout(Timer timer) {

        logger.info("Programmatic timeout occurred.");
        
        Query query = em.createNamedQuery("findExecutionsWithEndNull");
		List<Execution> executions = query.getResultList();
		
		for (Execution execution : executions) {
			Execution e = em.find(Execution.class, execution.getId());
			e.setEnd(new Date());
			e.setStatus(JobStatus.FINISHED);
			em.merge(e);
		}	
    }*/
}
