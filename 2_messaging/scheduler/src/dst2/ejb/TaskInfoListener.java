package dst2.ejb;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class TaskInfoListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		ObjectMessage msg = null;		
		try {
			if (message instanceof ObjectMessage) {
	            //msg = (TaskDto) message;
	            //System.out.println("Reading message: " + msg.getText());
	        } else {
	            System.err.println("Message is not a ObjectMessage");
	        }
		 } catch (Throwable t) {
	            System.err.println("Exception in onMessage():" + t.getMessage());
	     }
	}
}
