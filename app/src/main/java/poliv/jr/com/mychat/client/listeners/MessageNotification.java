package poliv.jr.com.mychat.client.listeners;

import model.Message;

public interface MessageNotification {

    void messageReceived(Message receivedMessage);
}
