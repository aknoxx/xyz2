package dst2.ejb.interceptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst2.ejb.model.Audit;
import dst2.ejb.model.AuditParameter;

public class AuditInterceptor {
	
	@PersistenceContext
	private EntityManager em;
	
	private static final Logger logger = Logger.getLogger(
    "dst2.ejb.interceptor");

	public AuditInterceptor() {
    }

    @AroundInvoke
    public Object audit(InvocationContext ctx) throws Exception {
        try {
            Object result;
            String resultValue;
            
            if((result = ctx.proceed()) == null) {
            	resultValue = "null";
            }
            else {
            	resultValue = result.toString();
            }            
            
            persistAudit(ctx, resultValue);
            return result;
            
        } catch (Exception e) {
        	persistAudit(ctx, e.getMessage());
        	logger.warning("Error calling ctx.proceed in audit()");
        	
            throw e;
        }
    }
    
    private void persistAudit(InvocationContext ctx, String resultValue) {
    	Object[] params = ctx.getParameters();
        
        List<AuditParameter> parameters = new ArrayList<AuditParameter>();
        
        Audit audit = new Audit(new Date(), ctx.getMethod().getName(), resultValue);
    	em.persist(audit);
        if(params != null) {
        	
        	for (int i = 0; i < params.length; i++) {
            	AuditParameter p = new AuditParameter(i, params[i].getClass().getName(), params[i].toString());
            	p.setAudit(audit);
            	parameters.add(p);
            	em.persist(p);            	
            }
        	audit.setParameters(parameters);
        	em.merge(audit);
        }
    }
}
