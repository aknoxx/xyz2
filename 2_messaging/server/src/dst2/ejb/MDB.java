package dst2.ejb;

import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.ActivationConfigProperty;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.Resource;

import dst2.ejb.dto.AssignJobDto;

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
		
		

        try {
        	if (message instanceof TextMessage) {
        		textMessage = (TextMessage) message;
                logger.info("MESSAGE BEAN: Message received: " + textMessage.getText());
        	}
        	else if (message instanceof AssignJobDto) {
                msg = (AssignJobDto) message;
                logger.info("MESSAGE BEAN: Message received: " + msg.jobId);
            } else {
                logger.warning(
                        "Message of wrong type: "
                        + message.getClass().getName());
            }
        } catch (Throwable te) {
            te.printStackTrace();
        }		
	}

}
