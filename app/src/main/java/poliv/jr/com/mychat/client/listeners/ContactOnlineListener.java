package poliv.jr.com.mychat.client.listeners;

public interface ContactOnlineListener {
    void onContactOnline(String contactName);

    void onContactOffline(String contactName);
}
