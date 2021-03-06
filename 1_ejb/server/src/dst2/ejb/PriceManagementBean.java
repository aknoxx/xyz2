package dst2.ejb;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.Startup;

import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import javax.ejb.Lock;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static javax.ejb.LockType.READ;
import static javax.ejb.LockType.WRITE;

import dst2.ejb.model.*;
import dst2.ejb.util.NoPriceStepException;

@Startup // "eager initialization"
@Singleton
@ConcurrencyManagement(CONTAINER)
public class PriceManagementBean implements PriceManagementLocal {

	@PersistenceContext
	private EntityManager em;
	
	private SortedMap<Integer, BigDecimal> map;
	
	@PostConstruct
	void init() {
		map = new TreeMap<Integer, BigDecimal>();
		Query q = em.createNamedQuery("findPriceSteps");
		List<PriceStep> prices = q.getResultList();
		for (PriceStep priceStep : prices) {
			map.put(priceStep.getNumberOfHistoricalJobs(), priceStep.getPrice());
		}
	}
	
	@Lock(READ)
	public BigDecimal getFeeForNumberOfHistoricalJobs(Long numberOfHistoricalJobs) 
						throws NoPriceStepException {
		if(map.isEmpty()) {
			throw new NoPriceStepException();
		}
		
		Iterator<Integer> iterator = map.keySet().iterator();
	    while (iterator.hasNext()) {
	    	Integer key = (Integer)iterator.next();
	    	if(numberOfHistoricalJobs < (int) key) {
	    		return map.get(key);
	    	}
	    }
		return map.get(map.lastKey());
	}
	
	@Lock(WRITE)
	public void setFeeForNumberOfHistoricalJobs(int numberOfHistoricalJobs, BigDecimal price) {
		// directly store new values to database
		// update in-memory data structure!

		// check if step already exists
		if(map.containsKey(numberOfHistoricalJobs)) {
			Query q = em.createNamedQuery("findPriceStep");
			q.setParameter("numberOfHistoricalJobs", numberOfHistoricalJobs);	
			PriceStep oldPs = (PriceStep) q.getSingleResult();
			
			if(oldPs != null) {
				PriceStep priceStep = em.find(PriceStep.class, oldPs.getId());
				priceStep.setPrice(price);
				em.merge(priceStep);
				
				map.put(priceStep.getNumberOfHistoricalJobs(), priceStep.getPrice());
			}
		}
		else {
			PriceStep priceStep = new PriceStep(numberOfHistoricalJobs, price);
			em.persist(priceStep);
			map.put(priceStep.getNumberOfHistoricalJobs(), priceStep.getPrice());
		}
	}
}
