package dst2.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.ejb.model.*;

@Startup // "eager initialization"
@Singleton
public class TimerSessionBean {
	
	@PersistenceContext
	private EntityManager em;
	
	@Resource
	TimerService timerService;
	
	private Logger logger = Logger.getLogger(
		"com.sun.tutorial.javaee.ejb.timersession.TimerSessionBean");
	
	// called every 2 seconds
	@Schedule(second="*/2", hour="*")
	public void automaticTimeout() {
		
		logger.info("Automatic timeout occured");
		
		Query query = em.createNamedQuery("findExecutionsByStatus");
		query.setParameter("status", JobStatus.RUNNING);
		List<Execution> executions = query.getResultList();
		
		for (Execution execution : executions) {
			execution.setEnd(new Date());
			execution.setStatus(JobStatus.FINISHED);
		}
		
		query.setParameter("status", JobStatus.SCHEDULED);
		executions = query.getResultList();
		
		for (Execution execution : executions) {
			execution.setStatus(JobStatus.RUNNING);
		}		
	}
}
