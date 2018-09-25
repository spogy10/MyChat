package poliv.jr.com.mychat.client.listeners;

public interface ContactAddedListener {
    void onContactAdded(String userName);

    void onContactRemoved(String userName);
}
