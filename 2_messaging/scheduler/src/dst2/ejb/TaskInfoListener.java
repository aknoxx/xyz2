package dst2.ejb;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import dst2.ejb.dto.TaskIdDto;

public class TaskInfoListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		ObjectMessage oMsg = null;	
		TaskIdDto taskIdDto = null;
		try {
			if (message instanceof ObjectMessage) {
				oMsg = (ObjectMessage) message;
				
				taskIdDto = (TaskIdDto) oMsg.getObject();
				
				System.out.println("TaskId: " + taskIdDto.taskId);
	            //System.out.println("Reading message: " + msg.getText());
	        } else {
	            System.err.println("Message is not a ObjectMessage");
	        }
		 } catch (Throwable t) {
	            System.err.println("TaskInfoListener: Exception in onMessage():" + t.getMessage());
	     }
	}
}
