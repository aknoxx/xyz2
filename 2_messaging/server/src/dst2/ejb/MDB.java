package dst2.ejb;

import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.ActivationConfigProperty;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.Resource;

import dst2.ejb.dto.AssignJobDto;
import dst2.ejb.dto.TaskIdDto;
import dst2.ejb.model.Task;
import dst2.ejb.model.TaskComplexity;
import dst2.ejb.model.TaskStatus;

import java.util.logging.Logger;

@MessageDriven(mappedName = "queue.dst.MyQueue", activationConfig =  {
	    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
	    , @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
	}
	)
public class MDB implements MessageListener {

	static final Logger logger = Logger.getLogger("SimpleMessageBean");
	
	@PersistenceContext
	private EntityManager em;
	
    @Resource
    private MessageDrivenContext mdc;
	
	@Override
	public void onMessage(Message message) {
		
		AssignJobDto msg = null;
		TextMessage textMessage = null;
		
		ObjectMessage objInMsg = (ObjectMessage)message;

        try {
        	msg = (AssignJobDto) objInMsg.getObject();

            Task task = new Task(msg.jobId, TaskStatus.ASSIGNED, null, TaskComplexity.UNRATED);
            em.persist(task);
            
            TaskIdDto taskIdDto = new TaskIdDto(task.getId());
            
            Queue queue = (Queue)message.getJMSReplyTo();
            
            InitialContext ctx = null;
            try {
    			ctx = new InitialContext();
    		} catch (NamingException e) {
    			e.printStackTrace();
    		}
    		
    		
    		
            //QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("dst.Factory");            
	        //QueueConnection connect = factory.createQueueConnection();	     
	        //QueueSession session = connect.createQueueSession(true,0);	     
	        //QueueSender sender = session.createSender(queue);
    		
    		/*ConnectionFactory factory = (ConnectionFactory) ctx.lookup("dst.Factory");            
	        Connection connect = factory.createConnection();	     
	        Session session = connect.createSession(true,0);	     
	        
	        MessageProducer messageProducer = session.createProducer(queue);
	        
	        
	     
	        ObjectMessage oMsg = session.createObjectMessage();
	        oMsg.setObject(taskIdDto);
	        
	        messageProducer.send(oMsg);
	            
	        connect.close();
        	
        	/*if (message instanceof TextMessage) {
        		textMessage = (TextMessage) message;
        		
        		Task task = new Task(new Long(5), TaskStatus.ASSIGNED, null, TaskComplexity.UNRATED);
                em.persist(task);
        		
                logger.info("MESSAGE BEAN: Message received: " + textMessage.getText());
        	}
        	else if (message instanceof AssignJobDto) {
        		 msg = message.getObject();
                 Task task = new Task(msg.jobId, TaskStatus.ASSIGNED, null, TaskComplexity.UNRATED);
                 em.persist(task);
                 
                 // send id to scheduler
                 
                 // forward Task to next available cluster
                 
                 logger.info("MESSAGE BEAN: Message received: " + msg.jobId);
            } else {
                logger.warning(
                        "Message of wrong type: "
                        + message.getClass().getName());
            }*/
        } catch (Throwable te) {
            te.printStackTrace();
        }		
	}

}
