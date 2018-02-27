import java.sql.*;
import javax.jms.*;
import java.math.BigDecimal;
import oracle.jms.*;


public class AQJmsPublisher extends AQApplication {

	static public void main(String[] arguments) {
		AQJmsPublisher application = new AQJmsPublisher(arguments);
	}

	/** Constructor		@param arguments Arguments passed at program start	*/
  protected AQJmsPublisher(String[] arguments) {
		try {
    	JMSQueue aq = createJMSClient();
      doTest(aq);
		}
		catch (Exception ex) {
			System.err.println("AQJmsPublisher.AQJmsPublisher(): " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	protected void doTest(JMSQueue aq) throws JMSException, SQLException {

    AQJmsSingleQueueItem messageData;

    // Start the connection
    aq.connection.start();

    // Create topic publisher
    QueueSender sender = aq.session.createSender(
                null);              // No specific topic here
                                    // Different ones can be used with same publisher
    // Get topic object
    Queue queue = ((AQjmsSession) aq.session).getQueue(
                DB_AQ_ADMIN_NAME,   // Queue owner
                "QUEUE_NAME");     // Queue name

    // Prepare messages
    messageData = new AQJmsSingleQueueItem(
                0, "Published message", "Message has been published to subscriber 0");
    ObjectMessage objectMessage0 = aq.session.createObjectMessage(messageData);

    messageData = new AQJmsSingleQueueItem(
                1, "Published message", "Message has been published to subscriber 1");
    ObjectMessage objectMessage1 = aq.session.createObjectMessage(messageData);

    messageData = new AQJmsSingleQueueItem(
                2, "Published message", "Message has been published to subscriber 2");
    ObjectMessage objectMessage2 = aq.session.createObjectMessage(messageData);

    messageData = new AQJmsSingleQueueItem(
                3, "Published message", "Message has been published to subscribers 1 and 2");
    ObjectMessage objectMessage3 = aq.session.createObjectMessage(messageData);

    // Prepare recipient list: subscriber 0
    AQjmsAgent[] recipientList0 = new AQjmsAgent[1];
    recipientList0[0] = new AQjmsAgent(
                "SUBSCRIPTION0",    // Name and identification of the subscription
                null);              // Address is set for remote subscribers only

    // Prepare recipient list: subscriber 1
    //AQjmsAgent[] recipientList1 = new AQjmsAgent[1];
    //recipientList1[0]  = new AQjmsAgent("SUBSCRIPTION1", null);

    // Prepare recipient list: subscriber 2
    //AQjmsAgent[] recipientList2 = new AQjmsAgent[1];
    //recipientList2[0]  = new AQjmsAgent("SUBSCRIPTION2", null);

    // Prepare recipient list: subscriber 1 + 2
    //AQjmsAgent[] recipientList12 = new AQjmsAgent[2];
    //recipientList12[0] = new AQjmsAgent("SUBSCRIPTION1", null);
    //recipientList12[1] = new AQjmsAgent("SUBSCRIPTION2", null);

    // Publish messages
    ((QueueSender) sender).send(queue, objectMessage0);
    //((AQjmsTopicPublisher) publisher).publish(topic, objectMessage1, recipientList1);
    //((AQjmsTopicPublisher) publisher).publish(topic, objectMessage2, recipientList2);
    //((AQjmsTopicPublisher) publisher).publish(topic, objectMessage3, recipientList12);
		System.out.println("publish() successfully");

    // Commit activities
    aq.session.commit();

    // Close session
    aq.session.close();

    // Close connection
    aq.connection.close();
	}

} // End of class AQJmsPublisher
